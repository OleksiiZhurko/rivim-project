package com.rivim.webclient.service.impl;

import com.rivim.webclient.dao.RoleDao;
import com.rivim.webclient.exceptions.RoleNotFoundException;
import com.rivim.webclient.model.RoleEntity;
import com.rivim.webclient.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleDao roleDao;

  @Autowired
  public RoleServiceImpl(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  @Override
  @Transactional(readOnly = true)
  public RoleEntity findRole(String name) {
    return roleDao.findByName(name).orElseThrow(
        () -> new RoleNotFoundException("There are no role " + name));
  }
}
