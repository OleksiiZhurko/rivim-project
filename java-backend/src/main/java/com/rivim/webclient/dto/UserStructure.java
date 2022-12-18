package com.rivim.webclient.dto;

import java.util.Date;

public interface UserStructure {

  String getUsername();

  String getEmail();

  String getFirstName();

  String getLastName();

  Date getBirthday();

  String getPassword();

  String getRole();
}
