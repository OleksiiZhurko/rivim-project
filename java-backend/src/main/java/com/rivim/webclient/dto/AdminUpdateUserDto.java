package com.rivim.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "userUpdate")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminUpdateUserDto extends UserDto {

  @NotNull(message = "password cannot be null")
  @Pattern(regexp = "^$|^.{4,128}$",
      message = "Password must be between 4 and 128 or empty")
  private String password;

  @NotNull(message = "Role cannot be empty")
  @Pattern(regexp = "^user|admin$", message = "Role is incorrect")
  private String role;
}
