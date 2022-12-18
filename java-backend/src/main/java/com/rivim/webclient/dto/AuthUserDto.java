package com.rivim.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto extends UserDto {

  @NotNull(message = "Password cannot be null")
  @Size(min = 4, max = 128, message = "Password must be between 4 and 128")
  private String password;

  @NotNull(message = "Role cannot be null")
  @Pattern(regexp = "^user$", message = "Role is incorrect")
  private String role;
}
