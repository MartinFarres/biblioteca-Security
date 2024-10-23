package com.is.biblioteca.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.logic.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/inicio")
	public String inicio(){
		return "inicio.html";
	}
	// public String inicio(@RequestParam(value = "email") String email, @RequestParam(value = "password") String clave,
	// 		HttpSession session, ModelMap modelo) {

	// 	try {

	// 		Usuario usuario = usuarioService.login(email, clave);
	// 		session.setAttribute("usuariosession", usuario);

	// 		if (usuario.getRol().toString().equals("ADMIN")) {
	// 			return "redirect:/admin/dashboard";
	// 		}

	// 		return "inicio.html";

	// 	} catch (ErrorServiceException ex) {
	// 		modelo.put("error", ex.getMessage());
	// 		return "login.html";
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		modelo.put("error", e.getMessage());
	// 		return "login.html";
	// 	}
	// }

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
