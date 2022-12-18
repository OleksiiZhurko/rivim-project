package com.rivim.webclient.service.impl;

import com.rivim.webclient.dao.UserDao;
import com.rivim.webclient.dto.AuthUserDto;
import com.rivim.webclient.dto.UserStructure;
import com.rivim.webclient.exceptions.EmailNotUniqueException;
import com.rivim.webclient.exceptions.UsernameNotUniqueException;
import com.rivim.webclient.model.UserEntity;
import com.rivim.webclient.service.AuthService;
import com.rivim.webclient.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

  private final BCryptPasswordEncoder encoder;

  private final RoleService roleService;

  private final UserDao userDao;

  @Autowired
  public AuthServiceImpl(BCryptPasswordEncoder encoder,
                         RoleService roleService, UserDao userDao) {
    this.encoder = encoder;
    this.roleService = roleService;
    this.userDao = userDao;
  }

  @Override
  @Transactional
  public void addUser(UserStructure user) {
    userDao.findByLogin(user.getUsername()).ifPresent(func -> {
      throw new UsernameNotUniqueException("Such login already exists: " + user.getUsername());
    });
    userDao.findByEmail(user.getEmail()).ifPresent(func -> {
      throw new EmailNotUniqueException("Such email already exists: " + user.getEmail());
    });

    userDao.create(UserEntity.builder()
        .username(user.getUsername())
        .password(encoder.encode(user.getPassword()))
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .birthday(new Date(user.getBirthday().getTime()))
        .role(roleService.findRole(user.getRole()))
        .build());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<AuthUserDto> getUser(String username) {
    return userDao.findByLogin(username)
        .map(user -> AuthUserDto.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .birthday(user.getBirthday())
            .role(user.getRole().getName())
            .build());
  }
}
