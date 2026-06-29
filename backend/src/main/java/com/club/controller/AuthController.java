package com.club.controller;

import com.club.common.Result;
import com.club.dto.LoginRequest;
import com.club.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest request) {
        return Result.success(authService.login(request.getUsername(), request.getPassword()));
    }

    @GetMapping("/health")
    public Result health() {
        return Result.success("club-system backend is running");
    }
}