package com.rivim.webclient.controller;

import com.rivim.webclient.config.security.jwt.JwtTokenProvider;
import com.rivim.webclient.controller.captcha.CaptchaData;
import com.rivim.webclient.controller.captcha.CaptchaProducer;
import com.rivim.webclient.dto.AuthUserDto;
import com.rivim.webclient.dto.LoginDto;
import com.rivim.webclient.exceptions.UserNotFoundException;
import com.rivim.webclient.service.AuthService;
import com.rivim.webclient.util.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  private final CaptchaProducer captcha;

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  private final ValidatorUtils validatorUtils;

  @Autowired
  public AuthController(
      AuthService authService,
      CaptchaProducer captcha,
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      ValidatorUtils validatorUtils
  ) {
    this.authService = authService;
    this.captcha = captcha;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.validatorUtils = validatorUtils;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, ?>> login(@RequestBody LoginDto login) {
    validatorUtils.checkForBindingError(login);

    AuthUserDto user;

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
      );
      user = authService.getUser(login.getUsername())
          .orElseThrow(() -> new BadCredentialsException("Combination not found"));
    } catch (RuntimeException e) {
      throw new UserNotFoundException("Invalid username or password");
    }

    return ResponseEntity.ok(
        Map.of(
            "token", jwtTokenProvider.createToken(user),
            "username", user.getUsername(),
            "role", user.getRole(),
            "firstName", user.getFirstName(),
            "lastName", user.getLastName()
        )
    );
  }

  @GetMapping("/generateCaptcha")
  public ResponseEntity<Map<String, ?>> generateCaptcha() throws IOException {
    CaptchaData captchaData = captcha.captchaGenerator();

    return ResponseEntity.ok(Map.of(
        CaptchaProducer.CAPTCHA_VALUE, captchaData.getImg(),
        CaptchaProducer.CAPTCHA_CODE, captchaData.getCode()
    ));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody AuthUserDto user) {
    validatorUtils.checkForBindingError(user);
    authService.addUser(user);

    return ResponseEntity.ok().build();
  }
}
