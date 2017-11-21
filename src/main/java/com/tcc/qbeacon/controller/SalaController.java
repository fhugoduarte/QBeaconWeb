package com.tcc.qbeacon.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Bloco;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.BlocoService;
import com.tcc.qbeacon.service.SalaService;

@Controller
@RequestMapping(path="/sala")
public class SalaController {

	@Autowired
	SalaService salaService;
	
	@Autowired
	BlocoService blocoService;
	
	@GetMapping(path="/listar_salas")
	public ModelAndView listaSalas() {
		ModelAndView model = new ModelAndView("sala/listaSalas");
		List<Sala> salas = salaService.pegarSalas();
		model.addObject("salas", salas);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarSala() {
		List<Bloco> blocos = blocoService.pegarBlocos();
		
		ModelAndView model = new ModelAndView("sala/formCadastrarSala");
		model.addObject("blocos", blocos);
		model.addObject("sala", new Sala());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarSala(@Valid Sala sala, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/sala/cadastrar";
		salaService.salvarSala(sala);
		return "redirect:/sala/listar_salas";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		salaService.deletarSala(sala);
		return "redirect:/sala/listar_salas";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		ModelAndView model = new ModelAndView("sala/formEditarSala");
		model.addObject("sala", sala);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarSala(@Valid Sala sala, BindingResult result) {	
		salaService.salvarSala(sala);
		return "redirect:/sala/listar_salas";
	}
	
}
