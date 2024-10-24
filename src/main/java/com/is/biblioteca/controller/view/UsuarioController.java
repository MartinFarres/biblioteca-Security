package com.is.biblioteca.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is.biblioteca.Jwt.JwtService;
import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.logic.service.UsuarioService;
import com.is.biblioteca.business.persistence.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/inicio")
	public String inicio(@CookieValue(value = "jwtToken", defaultValue = "") String jwtToken, ModelMap modelo) {
		if (jwtToken.isEmpty()) {
			// Manejar el caso donde no se encuentra la cookie o está vacía
			return "redirect:/login"; // Redirigir al login o mostrar error según tu necesidad
		}

		try {
			// Obtener el nombre de usuario desde el token JWT
			String username = jwtService.getUsernameFromToken(jwtToken);
			UserDetails user = usuarioRepository.buscarUsuarioPorEmail(username);

			// Poner el usuario en el modelo para ser accedido en la vista
			modelo.put("usersession", user);

			return "inicio.html";
		} catch (Exception e) {
			// Manejar excepciones, como token inválido o expirado
			return "redirect:/login"; // Redirigir al login o mostrar error
		}
	}

	@GetMapping("/logout")
	public String salir(HttpSession session) {
		session.setAttribute("usuariosession", null);
		return "index.html";
	}

	@GetMapping("/registrar")
	public String irEditAlta() {
		return "registro.html";
	}

	@GetMapping("/perfil")
	public String irEditModificar(ModelMap modelo, HttpSession session) {

		Usuario usuario = (Usuario) session.getAttribute("usuariosession");
		modelo.put("usuario", usuario);

		return "usuario_modificar.html";
	}

	@GetMapping("/perfil/{id}")
	public String irEditModificar(ModelMap modelo, @PathVariable String id) {

		try {

			Usuario usuario = usuarioService.buscarUsuario(id);
			modelo.put("usuario", usuario);

			return "usuario_modificar.html";

		} catch (ErrorServiceException e) {
			modelo.put("error", e.getMessage());
			return "usuario_list";
		} catch (Exception e) {
			modelo.put("error", "Error de Sistemas");
			return "usuario_list";
		}
	}

	@PostMapping("/perfil/{id}")
	public String irEditModificar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,
			@RequestParam String email, @RequestParam String password, @RequestParam String password2,
			ModelMap modelo) {

		try {

			usuarioService.modificarUsuario(id, nombre, email, password, password2, archivo);

			modelo.put("exito", "Usuario actualizado correctamente!");

			return "redirect:/admin/dashboard";

		} catch (ErrorServiceException ex) {

			modelo.put("error", ex.getMessage());
			modelo.put("nombre", nombre);
			modelo.put("email", email);

			return "usuario_modificar.html";
		}

	}

}
