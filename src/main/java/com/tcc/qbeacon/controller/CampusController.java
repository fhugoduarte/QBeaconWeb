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
import com.tcc.qbeacon.model.Campus;
import com.tcc.qbeacon.model.Instituicao;
import com.tcc.qbeacon.service.BlocoService;
import com.tcc.qbeacon.service.CampusService;
import com.tcc.qbeacon.service.InstituicaoService;

@Controller
@RequestMapping(path="/campus")
public class CampusController {
	
	@Autowired
	CampusService campusService;
	
	@Autowired
	InstituicaoService instituicaoService;
	
	@Autowired
	BlocoService blocoService;
	
	@GetMapping(path="/listar_campus")
	public ModelAndView listaCampus() {
		ModelAndView model = new ModelAndView("campus/listaCampus");
		List<Campus> campus = campusService.pegarTodosCampus();
		model.addObject("campus", campus);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarCampus() {
		List<Instituicao> instituicoes = instituicaoService.pegarInstituicoes();
		
		ModelAndView model = new ModelAndView("campus/formCadastrarCampus");
		model.addObject("campus", new Campus());
		model.addObject("instituicoes", instituicoes);
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarCampus(@Valid Campus campus, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/campus/cadastrar";
		Campus campusSalvo = campusService.salvarCampus(campus);
		Instituicao instituicao = campusSalvo.getInstituicao();
		
		instituicao = this.adicionarCampusInstituicao(instituicao, campusSalvo);
		instituicaoService.salvarInstituicao(instituicao);
		
		return "redirect:/campus/" + campusSalvo.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarCampus(@PathVariable("id") Integer id) {
		Campus campus = campusService.buscarCampus(id);
		
		Instituicao instituicao = campus.getInstituicao();
		instituicao = this.removerCampusInstituicao(instituicao, campus);
		instituicaoService.salvarInstituicao(instituicao);
		
		campusService.deletarCampus(campus);
		return "redirect:/campus/listar_campus";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarCampus(@PathVariable("id") Integer id) {
		Campus campus = campusService.buscarCampus(id);
		List<Instituicao> instituicoes = instituicaoService.pegarInstituicoes();
		
		ModelAndView model = new ModelAndView("campus/formEditarCampus");
		model.addObject("instituicoes", instituicoes);
		model.addObject("campus", campus);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarCampus(@Valid Campus campus, BindingResult result) {
		Campus velho = campusService.buscarCampus(campus.getId());
		Instituicao instituicaoVelha = velho.getInstituicao();
		
		Campus campusSalvo = campusService.salvarCampus(campus);
		
		if(!campus.getInstituicao().equals(instituicaoVelha))
			this.alterarInstituicao(instituicaoVelha, campusSalvo.getInstituicao(), campusSalvo);
		
		return "redirect:/campus/" + campusSalvo.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarCampus(@PathVariable("id") Integer id) {
		Campus campus = campusService.buscarCampus(id);
		ModelAndView model = new ModelAndView("campus/detalhesCampus");
		model.addObject("campus", campus);
		
		return model;
	}
	
	@GetMapping("/{id_campus}/criar_bloco")
	public ModelAndView criarBloco(@PathVariable("id_campus") Integer id_campus) {
		Campus campus = campusService.buscarCampus(id_campus);
		Bloco bloco = new Bloco();
		bloco.setCampus(campus);
		
		ModelAndView model = new ModelAndView("bloco/formCadastrarBloco");
		model.addObject("bloco", bloco);
		return model;
	}
	
	@PostMapping("/{id_campus}/criar_bloco")
	public String salvarBloco(@PathVariable("id_campus") Integer id_campus,
							@Valid Bloco bloco, BindingResult result) {
		if (result.hasErrors()) return "redirect:/campus/"+id_campus+"/criar_bloco";
		
		Campus campus = campusService.buscarCampus(id_campus);
		bloco.setCampus(campus);
		Bloco blocoSalvo = blocoService.salvarBloco(bloco);
		
		List<Bloco> blocos = campus.getBlocos();
		blocos.add(blocoSalvo);
		campus.setBlocos(blocos);
		
		campusService.salvarCampus(campus);
		
		return "redirect:/campus/"+campus.getId();
		
	}
	
	public Instituicao adicionarCampusInstituicao(Instituicao instituicao, Campus campus) {
		List<Campus> lcampus = instituicao.getCampus();
		lcampus.add(campus);
		instituicao.setCampus(lcampus);
		
		return instituicao;
	}
	
	public Instituicao removerCampusInstituicao(Instituicao instituicao, Campus campus) {
		List<Campus> lcampus = instituicao.getCampus();
		lcampus.remove(campus);
		instituicao.setCampus(lcampus);
		
		return instituicao;
	}
	
	public void alterarInstituicao (Instituicao antiga, Instituicao nova, Campus campus) {
		
		List<Campus> lcampus = antiga.getCampus();
		lcampus.remove(campus);
		antiga.setCampus(lcampus);
		instituicaoService.salvarInstituicao(antiga);
		
		lcampus = nova.getCampus();
		lcampus.add(campus);
		nova.setCampus(lcampus);
		instituicaoService.salvarInstituicao(nova);
		
	}
}
