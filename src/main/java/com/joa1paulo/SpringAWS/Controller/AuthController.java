package com.joa1paulo.SpringAWS.Controller;

import com.joa1paulo.SpringAWS.Dto.LoginRequest;
import com.joa1paulo.SpringAWS.Dto.LoginResponse;
import com.joa1paulo.SpringAWS.Dto.RegisterRequest;
import com.joa1paulo.SpringAWS.Dto.UsuarioResponse;
import com.joa1paulo.SpringAWS.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        UsuarioResponse criado = usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid   @RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }
}