package com.is.biblioteca.controller.authController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.logic.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;

	@GetMapping(value = "login")
	public String login(@RequestParam(required = false) String error, ModelMap modelo) {

		if (error != null) {
			modelo.put("error", "Usuario o Contraseña invalidos!");
		}

		return "login.html";
	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletResponse response) throws ErrorServiceException {
		LoginRequest loginRequest = new LoginRequest(username, password);
		authService.login(loginRequest, response);
		return "redirect:/usuario/inicio"; // Redirigir después de un login exitoso
	}

	@PostMapping("/register")
	public String register(@ModelAttribute RegisterRequest registerRequest,
			@RequestParam("archivo") MultipartFile archivo,
			HttpServletResponse response) throws ErrorServiceException {
		authService.register(registerRequest, archivo, response);
		return "redirect:/usuario/inicio"; // Redirigir después de un registro exitoso
	}

	@GetMapping(value = "register")
	public String irEditAlta() {
		return "registro.html";
	}

}
