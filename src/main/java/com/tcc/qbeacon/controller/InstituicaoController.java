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

import com.tcc.qbeacon.model.Campus;
import com.tcc.qbeacon.model.Instituicao;
import com.tcc.qbeacon.service.CampusService;
import com.tcc.qbeacon.service.InstituicaoService;

@Controller
@RequestMapping(path="/instituicao")
public class InstituicaoController {
	
	@Autowired
	InstituicaoService instituicaoService;
	
	@Autowired
	CampusService campusService;
		
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
		Instituicao insituicaoSalva = instituicaoService.salvarInstituicao(instituicao);
		return "redirect:/instituicao/"+ insituicaoSalva.getId();
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
		return "redirect:/instituicao/"+ instituicao.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarInstituicao(@PathVariable("id") Integer id) {
		Instituicao instituicao = instituicaoService.buscarInstituicao(id);
		ModelAndView model = new ModelAndView("instituicao/detalhesInstituicao");
		model.addObject("instituicao", instituicao);
		
		return model;
	}
	
	@GetMapping("/{id_instituicao}/criar_campus")
	public ModelAndView criarCampus(@PathVariable("id_instituicao") Integer id_instituicao) {
		Instituicao instituicao = instituicaoService.buscarInstituicao(id_instituicao);
		Campus campus = new Campus();
		campus.setInstituicao(instituicao);
		
		ModelAndView model = new ModelAndView("campus/formCadastrarCampus");
		model.addObject("campus", campus);
		return model;
	}
	
	@PostMapping("/{id_instituicao}/criar_campus")
	public String salvarCampus(@PathVariable("id_instituicao") Integer id_instituicao,
							@Valid Campus campus, BindingResult result) {
		if (result.hasErrors()) return "redirect:/institucao/"+id_instituicao+"/criar_campus";
		
		Instituicao instituicao = instituicaoService.buscarInstituicao(id_instituicao);
		campus.setInstituicao(instituicao);
		Campus campusSalvo = campusService.salvarCampus(campus);
		
		List<Campus> campusInst = instituicao.getCampus();
		campusInst.add(campusSalvo);
		instituicao.setCampus(campusInst);
		instituicaoService.salvarInstituicao(instituicao);
		
		return "redirect:/instituicao/"+instituicao.getId();
		
	}
		
}
