package com.rivim.webclient.config.security.jwt;

import com.rivim.webclient.dto.AuthUserDto;
import com.rivim.webclient.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

  private final AuthService authService;

  @Autowired
  public JwtUserDetailsService(AuthService userService) {
    this.authService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AuthUserDto user = authService.getUser(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

    return new JwtUser(
        user.getUsername(),
        user.getPassword(),
        user.getFirstName(),
        user.getLastName(),
        List.of(new SimpleGrantedAuthority(user.getRole()))
    );
  }
}
