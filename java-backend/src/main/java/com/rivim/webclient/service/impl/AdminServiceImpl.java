package com.rivim.webclient.service.impl;

import com.rivim.webclient.dao.UserDao;
import com.rivim.webclient.dto.AdminUpdateUserDto;
import com.rivim.webclient.dto.AdminUserInfoDto;
import com.rivim.webclient.dto.UserStructure;
import com.rivim.webclient.exceptions.EmailNotUniqueException;
import com.rivim.webclient.exceptions.UsernameNotUniqueException;
import com.rivim.webclient.model.UserEntity;
import com.rivim.webclient.service.AdminService;
import com.rivim.webclient.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

  private final BCryptPasswordEncoder encoder;

  private final RoleService roleService;

  private final UserDao userDao;

  @Autowired
  public AdminServiceImpl(BCryptPasswordEncoder encoder, RoleService roleService, UserDao userDao) {
    this.encoder = encoder;
    this.roleService = roleService;
    this.userDao = userDao;
  }

  @Override
  @Transactional
  public void addUser(UserStructure user) {
    userDao.findByLogin(user.getUsername()).ifPresent(func -> {
      throw new UsernameNotUniqueException("Such username already exists: " + user.getUsername());
    });
    userDao.findByEmail(user.getEmail()).ifPresent(func -> {
      throw new EmailNotUniqueException("Such email already exists: " + user.getEmail());
    });

    userDao.create(extractUserFromDto(user));
  }

  @Override
  @Transactional
  public void updateUser(AdminUpdateUserDto user) {
    if (userDao.findByEmail(user.getEmail())
        .map(userEntity -> !userEntity.getUsername().equals(user.getUsername()))
        .orElse(false)) {
      throw new EmailNotUniqueException("Such email already exists: " + user.getEmail());
    }

    if (user.getPassword().isEmpty()) {
      userDao.updateWithoutPassword(extractUserFromDto(user));
    } else {
      userDao.update(extractUserFromDto(user));
    }
  }

  @Override
  @Transactional
  public void deleteUser(String username) {
    userDao.remove(username);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AdminUserInfoDto> getUsers() {
    return userDao.findAll().stream()
        .map(this::extractUserToInfoDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public AdminUpdateUserDto getUser(String username) {
    return extractUserToDto(userDao.findByLogin(username).orElseThrow(
        () -> new UsernameNotFoundException("User not found for username " + username)));
  }

  private UserEntity extractUserFromDto(UserStructure user) {
    return UserEntity.builder()
        .username(user.getUsername())
        .password(encoder.encode(user.getPassword()))
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .birthday(new Date(user.getBirthday().getTime()))
        .role(roleService.findRole(user.getRole()))
        .build();
  }

  private AdminUpdateUserDto extractUserToDto(UserEntity user) {
    return AdminUpdateUserDto.builder()
        .username(user.getUsername())
        .password("")
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .birthday(user.getBirthday())
        .role(user.getRole().getName())
        .build();

  }

  private AdminUserInfoDto extractUserToInfoDto(UserEntity user) {
    return AdminUserInfoDto.builder()
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .age(Period.between(user.getBirthday().toLocalDate(), LocalDate.now()).getYears())
        .role(user.getRole().getName())
        .build();
  }
}
