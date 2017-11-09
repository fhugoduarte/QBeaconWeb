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

import com.tcc.qbeacon.model.Instituicao;
import com.tcc.qbeacon.service.InstituicaoService;

@Controller
@RequestMapping(path="/instituicao")
public class InstituicaoController {
	
	@Autowired
	InstituicaoService instituicaoService;
	
	@GetMapping(path="/listar_instituicoes")
	public ModelAndView listaInstituicoes() {
		ModelAndView model = new ModelAndView("instituicao/listaInstituicoes");
		List<Instituicao> instituicoes = instituicaoService.pegarInstituicoes();
		model.addObject("instituicoes", instituicoes);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarInstituicao() {
		ModelAndView model = new ModelAndView("instituicao/formCadastrarInstituicao");
		model.addObject("instituicao", new Instituicao());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarInstituicao(@Valid Instituicao instituicao, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/instituicao/cadastrar";
		instituicaoService.salvarInstituicao(instituicao);
		return "redirect:/instituicao/listar_instituicoes";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarInstituicao(@PathVariable("id") Integer id) {
		Instituicao instituicao = instituicaoService.buscarInstituicao(id);
		instituicaoService.deletarInstituicao(instituicao);
		return "redirect:/instituicao/listar_instituicoes";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarInstituicao(@PathVariable("id") Integer id) {
		Instituicao instituicao = instituicaoService.buscarInstituicao(id);
		ModelAndView model = new ModelAndView("instituicao/formEditarInstituicao");
		model.addObject("instituicao", instituicao);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarInstituicao(@Valid Instituicao instituicao, BindingResult result) {	
		instituicaoService.salvarInstituicao(instituicao);
		return "redirect:/instituicao/listar_instituicoes";
	}
}
