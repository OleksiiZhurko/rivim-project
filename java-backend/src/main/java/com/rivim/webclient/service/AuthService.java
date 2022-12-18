package com.rivim.webclient.service;

import com.rivim.webclient.dto.AuthUserDto;

import java.util.Optional;

public interface AuthService extends RegisterService {

  Optional<AuthUserDto> getUser(String username);
}
