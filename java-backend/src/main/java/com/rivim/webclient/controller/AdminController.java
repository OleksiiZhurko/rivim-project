package com.rivim.webclient.controller;

import com.rivim.webclient.config.security.jwt.JwtUser;
import com.rivim.webclient.dto.AdminCreateUserDto;
import com.rivim.webclient.dto.AdminUpdateUserDto;
import com.rivim.webclient.dto.AdminUserInfoDto;
import com.rivim.webclient.service.AdminService;
import com.rivim.webclient.util.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final AdminService adminService;

  private final ValidatorUtils validatorUtils;

  @Autowired
  public AdminController(AdminService adminService, ValidatorUtils validatorUtils) {
    this.adminService = adminService;
    this.validatorUtils = validatorUtils;
  }

  @GetMapping("/get_all_users")
  public ResponseEntity<List<AdminUserInfoDto>> getAllUsers() {
    return ResponseEntity.ok(adminService.getUsers());
  }

  @GetMapping("/get_user/{username}")
  public ResponseEntity<AdminUpdateUserDto> getUser(@PathVariable("username") String username) {
    return ResponseEntity.ok(adminService.getUser(username));
  }

  @PostMapping("/add_user")
  public ResponseEntity<?> addUser(@RequestBody AdminCreateUserDto user) {
    validatorUtils.checkForBindingError(user);

    adminService.addUser(user);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/update_user")
  public ResponseEntity<?> updateUser(@RequestBody AdminUpdateUserDto user) {
    validatorUtils.checkForBindingError(user);

    if (isSameUser(user.getUsername()) && user.getRole().equals("user")) {
      ResponseEntity.status(HttpStatus.IM_USED).build();
    }

    adminService.updateUser(user);

    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @DeleteMapping("/delete_user/{username}")
  public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
    if (isSameUser(username)) {
      ResponseEntity.status(HttpStatus.IM_USED).build();
    }

    adminService.deleteUser(username);

    return ResponseEntity.ok().build();
  }

  private String retrieveUsername() {
    return ((JwtUser) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal()).getUsername();
  }

  private boolean isSameUser(String username) {
    return retrieveUsername().equals(username);
  }
}
