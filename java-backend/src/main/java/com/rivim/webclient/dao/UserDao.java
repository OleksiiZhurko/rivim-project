package com.rivim.webclient.dao;

import com.rivim.webclient.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {

  void create(UserEntity user);

  void update(UserEntity user);

  void updateWithoutPassword(UserEntity user);

  void remove(String login);

  List<UserEntity> findAll();

  Optional<UserEntity> findByLogin(String login);

  Optional<UserEntity> findByEmail(String email);
}
