package com.rivim.webclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "userInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminUserInfoDto {

  private String username;

  private String firstName;

  private String lastName;

  private int age;

  private String role;
}
