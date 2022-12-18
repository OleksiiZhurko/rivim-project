package com.rivim.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class UserDto implements UserStructure {

  @NotNull(message = "Login cannot be empty")
  @Size(min = 2, max = 20, message = "Username must be between 2 and 20")
  private String username;

  @NotNull(message = "Email cannot be empty")
  @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$",
      message = "Email should be valid")
  private String email;

  @NotNull(message = "Firstname cannot be empty")
  @Size(min = 2, max = 32, message = "Lastname must be between 2 and 32")
  private String firstName;

  @NotNull(message = "Lastname cannot be empty")
  @Size(min = 2, max = 32, message = "Lastname must be between 2 and 32")
  private String lastName;

  @NotNull(message = "Date cannot be null")
  @Past(message = "Date of birth must be less than today")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birthday;
}
