package com.tcc.qbeacon.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Papel;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.UsuarioService;

@Controller
@RequestMapping(path="/usuario")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping(path="/cadastrar")
	public ModelAndView cadastrarUsuario() {
		ModelAndView model = new ModelAndView("usuario/formCadastrarUsuario");
		model.addObject("usuario", new Usuario());
		return model;
	}
	
	@PostMapping(path="/cadastrar")
	public String salvarUsuario(@Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) return "redirect:/cadastrar";
		usuario.setPapel(Papel.ADMINISTRADOR);
		usuarioService.salvarUsuario(usuario);
		return "redirect:/";
	}

}
