package com.tcc.qbeacon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.ThreadService;
import com.tcc.qbeacon.service.UsuarioService;

@Controller
@RequestMapping(path="/")
public class PrincipalController {
	
	@Autowired
	ThreadService t;
	
	@RequestMapping(path="")
	public ModelAndView loginUsuario() {
		ModelAndView model = new ModelAndView("usuario/formLoginUsuario");
		model.addObject("usuario", new Usuario());
		return model;
	}
	
	@GetMapping(path="home")
	public ModelAndView logou() {
		new Thread(t).start();
		ModelAndView model = new ModelAndView("home");
		return model;
	}

}
