package com.is.biblioteca.business.logic.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

  // Método login modificado para manejar la cookie
  public AuthResponse login(LoginRequest request, HttpServletResponse response) throws ErrorServiceException {
    try {
      // Autenticar al usuario
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      UserDetails user = usuarioRepository.buscarUsuarioPorEmail(request.getUsername());

      // Generar el token JWT
      String token = jwtService.getToken(user);

      // Crear una cookie para almacenar el token JWT
      Cookie jwtCookie = new Cookie("jwtToken", token);
      jwtCookie.setHttpOnly(true); // Proteger la cookie
      jwtCookie.setMaxAge(24 * 60 * 60); // Duración de 1 día
      jwtCookie.setPath("/"); // Cookie disponible en toda la aplicación

      // Agregar la cookie a la respuesta
      response.addCookie(jwtCookie);

      // Retornar el AuthResponse con el token
      return AuthResponse.builder()
          .token(token)
          .build();

    } catch (AuthenticationException exception) {
      throw new ErrorServiceException(exception.getMessage());
    }
  }

  // Método register modificado para manejar la cookie
  public AuthResponse register(RegisterRequest request, MultipartFile archivo, HttpServletResponse response)
      throws ErrorServiceException {
    try {
      // Crear un nuevo usuario
      Usuario user = Usuario.builder()
          .nombre(request.getNombre())
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword()))
          .rol(Rol.USER)
          .build();
      usuarioRepository.save(user);

      // Generar el token JWT
      String token = jwtService.getToken(user);

      // Crear una cookie para almacenar el token JWT
      Cookie jwtCookie = new Cookie("jwtToken", token);
      jwtCookie.setHttpOnly(true); // Proteger la cookie
      jwtCookie.setMaxAge(24 * 60 * 60); // Duración de 1 día
      jwtCookie.setPath("/"); // Cookie disponible en toda la aplicación

      // Agregar la cookie a la respuesta
      response.addCookie(jwtCookie);

      // Retornar el AuthResponse con el token
      return AuthResponse.builder()
          .token(token)
          .build();

    } catch (Exception exception) {
      throw new ErrorServiceException(exception.getMessage());
    }
  }
}
