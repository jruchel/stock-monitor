package com.jruchel.stockmonitor.controllers;

import com.jruchel.stockmonitor.models.entities.Role;
import com.jruchel.stockmonitor.models.entities.User;
import com.jruchel.stockmonitor.security.autoconfig.Controller;
import com.jruchel.stockmonitor.security.autoconfig.SecuredMapping;
import com.jruchel.stockmonitor.services.users.RoleService;
import com.jruchel.stockmonitor.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends Controller {

    private final UserService userService;
    private final RoleService roleService;

    @SecuredMapping(path = "/users", method = RequestMethod.GET, role = "admin")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @SecuredMapping(path = "/roles", method = RequestMethod.GET, role = "admin")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

}
