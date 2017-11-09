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

import com.tcc.qbeacon.model.Disciplina;
import com.tcc.qbeacon.service.DisciplinaService;

@Controller
@RequestMapping(path="/disciplina")
public class DisciplinaController {

	@Autowired
	DisciplinaService disciplinaService;
	
	@GetMapping(path="/listar_disciplinas")
	public ModelAndView listaDisciplinas() {
		ModelAndView model = new ModelAndView("listaDisciplinas");
		List<Disciplina> disciplinas = disciplinaService.pegarDisciplinas();
		model.addObject("disciplinas", disciplinas);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarDisciplina() {
		ModelAndView model = new ModelAndView("formCadastrarDisciplina");
		model.addObject("disciplina", new Disciplina());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarDisciplina(@Valid Disciplina disciplina, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/disciplina/cadastrar";
		disciplinaService.salvarDisciplina(disciplina);
		return "redirect:/disciplina/listar_disciplinas";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarDisciplina(@PathVariable("id") Integer id) {
		Disciplina disciplina = disciplinaService.buscarDisciplina(id);
		disciplinaService.deletarDisciplina(disciplina);
		return "redirect:/disciplina/listar_disciplinas";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarDisciplina(@PathVariable("id") Integer id) {
		Disciplina disciplina = disciplinaService.buscarDisciplina(id);
		ModelAndView model = new ModelAndView("formEditarDisciplina");
		model.addObject("disciplina", disciplina);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarDisciplina(@Valid Disciplina disciplina, BindingResult result) {	
		disciplinaService.salvarDisciplina(disciplina);
		return "redirect:/disciplina/listar_disciplinas";
	}
	
}
