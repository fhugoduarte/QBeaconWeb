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
import com.tcc.qbeacon.service.BlocoService;

@Controller
@RequestMapping(path="/bloco")
public class BlocoController {

	@Autowired
	BlocoService blocoService;
	
	@GetMapping(path="/listar_blocos")
	public ModelAndView listaBlocos() {
		ModelAndView model = new ModelAndView("listaBlocos");
		List<Bloco> blocos = blocoService.pegarBlocos();
		model.addObject("blocos", blocos);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarBloco() {
		ModelAndView model = new ModelAndView("formCadastrarBloco");
		model.addObject("bloco", new Bloco());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarBloco(@Valid Bloco bloco, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/bloco/cadastrar";
		blocoService.salvarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		blocoService.deletarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		ModelAndView model = new ModelAndView("formEditarBloco");
		model.addObject("bloco", bloco);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarBloco(@Valid Bloco bloco, BindingResult result) {	
		blocoService.salvarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
}
