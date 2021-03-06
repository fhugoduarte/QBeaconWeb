package com.tcc.qbeacon.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Usuario;

@Controller
@RequestMapping(path="/")
public class PrincipalController {
	
	@Autowired
	ThreadController threadController;
	
	@RequestMapping(path="")
	public ModelAndView loginUsuario() throws IOException, InterruptedException {
		threadController.inicializar();
		ModelAndView model = new ModelAndView("usuario/formLoginUsuario");
		model.addObject("usuario", new Usuario());
		return model;
	}
	
	@GetMapping(path="home")
	public ModelAndView logou() {
		ModelAndView model = new ModelAndView("home");
		return model;
	}

}
