package com.is.biblioteca.business.logic.service;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.is.biblioteca.Jwt.JwtService;
import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.domain.enumeration.Rol;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.persistence.repository.UsuarioRepository;
import com.is.biblioteca.controller.authController.AuthResponse;
import com.is.biblioteca.controller.authController.LoginRequest;
import com.is.biblioteca.controller.authController.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthResponse login(LoginRequest request) throws ErrorServiceException {
    try{

      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      UserDetails user = usuarioRepository.buscarUsuarioPorEmail(request.getUsername());
      
      String token = jwtService.getToken(user);
      return AuthResponse.builder()
        .token(token)
        .build();
    }catch(AuthenticationException exception){
      throw new ErrorServiceException(exception.getMessage());
    }
  }

  public AuthResponse register(RegisterRequest request, MultipartFile archivo) throws ErrorServiceException {
    try{

      Usuario user = Usuario.builder()
        .nombre(request.getNombre())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .rol(Rol.USER)
        .build();
      usuarioRepository.save(user);
  
      return AuthResponse.builder()
        .token(jwtService.getToken(user))
        .build();
      
    }catch(Exception exception){
      throw new ErrorServiceException(exception.getMessage());
    }
  }

}
