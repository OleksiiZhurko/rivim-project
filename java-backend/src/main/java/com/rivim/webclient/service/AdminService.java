package com.rivim.webclient.service;

import com.rivim.webclient.dto.AdminUpdateUserDto;
import com.rivim.webclient.dto.AdminUserInfoDto;

import java.util.List;

public interface AdminService extends RegisterService {

  void updateUser(AdminUpdateUserDto user);

  void deleteUser(String username);

  List<AdminUserInfoDto> getUsers();

  AdminUpdateUserDto getUser(String username);
}
