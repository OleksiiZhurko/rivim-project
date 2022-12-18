package com.rivim.webclient.dao;

import com.rivim.webclient.model.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface RoleDao {

  void create(RoleEntity role);

  void update(RoleEntity role);

  void remove(RoleEntity role);

  List<RoleEntity> findAll();

  Optional<RoleEntity> findByName(String name);
}
