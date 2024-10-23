package com.is.biblioteca.controller.authController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.logic.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping(value = "login")
  public String login(@ModelAttribute LoginRequest request, ModelMap modelo) {
		 try {

			authService.login(request);
			modelo.put("exito", "Usuario registrado correctamente!");
			
			return "index.html";

		} catch (ErrorServiceException ex) {

			modelo.put("error", ex.getMessage());
			modelo.put("username", request.getUsername());

			return "login.html";
		}
  }

	@GetMapping(value = "register")
	public String irEditAlta() {
		return "registro.html";
	}

  @PostMapping(value = "register")
  public String register(@ModelAttribute RegisterRequest request, @RequestParam("archivo") MultipartFile archivo, ModelMap modelo) {
    
    // Lógica para manejar el archivo
		try {

			authService.register(request, archivo);

			modelo.put("exito", "Usuario registrado correctamente!");

			return "redirect:/usuario/index";

		} catch (ErrorServiceException ex) {

			modelo.put("error", ex.getMessage());
			modelo.put("nombre", request.getNombre());
			modelo.put("email", request.getEmail());

			return "registro.html";
		}
	}

	
  
  
}
