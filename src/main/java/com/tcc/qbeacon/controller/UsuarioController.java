package com.tcc.qbeacon.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.UsuarioService;

@Controller
@RequestMapping(path="/usuario")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping(path="/cadastrar")
	public ModelAndView cadastrarUsuario() {
		ModelAndView model = new ModelAndView("formCadastrarUsuario");
		model.addObject("usuario", new Usuario());
		return model;
	}
	
	@PostMapping(path="/cadastrar")
	public String salvarUsuario(@Valid Usuario usuario, BindingResult result) {
		if (result.hasErrors()) return "redirect:/cadastrar";
		usuarioService.salvarUsuario(usuario);
		return "redirect:/";
	}
	
	@PostMapping(path="/logar")
	public ModelAndView logar(@Valid Usuario usuario, BindingResult result) {
		System.out.println("ENTROU");
		Usuario usuarioLogado = usuarioService.logar(usuario.getEmail(), usuario.getSenha());
		System.out.println("NOME: " + usuarioLogado.getNome());
		
		if(usuarioLogado == null || result.hasErrors()) {
			ModelAndView model = new ModelAndView("formLoginUsuario");
			model.addObject("usuario", new Usuario());
			return model;
		} else {
			ModelAndView model = new ModelAndView("home");
			model.addObject("usuarioLogado", usuarioLogado);
			return model;
		}
	}
	
}
