package com.rivim.webclient.config.security.jwt;

import com.rivim.webclient.dto.AuthUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private final String KEY = Base64.getEncoder().encodeToString("token".getBytes());

  private final long TOKEN_TIME = 86400000L;

  private final UserDetailsService userDetailsService;

  @Autowired
  public JwtTokenProvider(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  public String createToken(AuthUserDto user) {
    Claims claims = Jwts.claims();
    claims.setSubject(user.getUsername());
    claims.put("firstName", user.getFirstName());
    claims.put("lastName", user.getLastName());

    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_TIME))
        .signWith(SignatureAlgorithm.HS256, KEY)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));

    return new UsernamePasswordAuthenticationToken(
        userDetails,
        userDetails.getPassword(),
        userDetails.getAuthorities()
    );
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }

  public boolean validateToken(String token) {
    try {
      return !parseToken(token)
          .getBody()
          .getExpiration()
          .before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtException("Token invalid or expired");
    }
  }

  private String getUsername(String token) {
    return parseToken(token).getBody().getSubject();
  }

  private Jws<Claims> parseToken(String token) {
    return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
  }
}
