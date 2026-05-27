package com.futtips.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.futtips.project.entities.dto.LoginRequestDTO;
import com.futtips.project.entities.dto.LoginResponseDTO;
import com.futtips.project.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }
}
