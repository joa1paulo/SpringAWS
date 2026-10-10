package com.joa1paulo.SpringAWS.Service;

import com.joa1paulo.SpringAWS.Dto.LoginRequest;
import com.joa1paulo.SpringAWS.Dto.LoginResponse;
import com.joa1paulo.SpringAWS.Dto.RegisterRequest;
import com.joa1paulo.SpringAWS.Dto.UsuarioResponse;
import com.joa1paulo.SpringAWS.Exception.CredenciaisInvalidasException;
import com.joa1paulo.SpringAWS.Exception.EmailJaCadastradoException;
import com.joa1paulo.SpringAWS.Model.Usuario;
import com.joa1paulo.SpringAWS.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UsuarioService(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = repository.findByEmail(request.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        return new LoginResponse(tokenService.gerarToken(usuario));
    }

    public UsuarioResponse registrar(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha())); // aqui entra o BCrypt

        Usuario salvo = repository.save(usuario);
        return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }
}