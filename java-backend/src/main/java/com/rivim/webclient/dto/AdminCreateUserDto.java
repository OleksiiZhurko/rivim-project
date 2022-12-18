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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "userCreate")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminCreateUserDto extends UserDto {

  @NotNull(message = "Password cannot be null")
  @Size(min = 4, max = 128, message = "Password must be between 4 and 128")
  private String password;

  @NotNull(message = "Role cannot be empty")
  @Pattern(regexp = "^user|admin$", message = "Role is incorrect")
  private String role;
}
