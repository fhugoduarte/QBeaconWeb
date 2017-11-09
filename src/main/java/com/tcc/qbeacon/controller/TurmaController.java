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

import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.service.TurmaService;

@Controller
@RequestMapping(path="/turma")
public class TurmaController {
	
	@Autowired
	TurmaService turmaService;
	
	@GetMapping(path="/listar_turmas")
	public ModelAndView listaTurmas() {
		ModelAndView model = new ModelAndView("turma/listaTurmas");
		List<Turma> turmas = turmaService.pegarTurmas();
		model.addObject("turmas", turmas);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarTurma() {
		ModelAndView model = new ModelAndView("turma/formCadastrarTurma");
		model.addObject("turma", new Turma());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarTurma(@Valid Turma turma, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/turma/cadastrar";
		turmaService.salvarTurma(turma);
		return "redirect:/turma/listar_turmas";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarTurma(@PathVariable("id") Integer id) {
		Turma turma = turmaService.buscarTurma(id);
		turmaService.deletarTurma(turma);
		return "redirect:/turma/listar_turmas";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarTurma(@PathVariable("id") Integer id) {
		Turma turma = turmaService.buscarTurma(id);
		ModelAndView model = new ModelAndView("turma/formEditarTurma");
		model.addObject("turma", turma);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarTurma(@Valid Turma turma, BindingResult result) {	
		turmaService.salvarTurma(turma);
		return "redirect:/turma/listar_turmas";
	}

}
