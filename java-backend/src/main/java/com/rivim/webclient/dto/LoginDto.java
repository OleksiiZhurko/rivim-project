package com.rivim.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

  @NotNull(message = "Login cannot be null")
  private String username;

  @NotNull(message = "Password cannot be null")
  private String password;
}
