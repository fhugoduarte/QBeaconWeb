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
import com.tcc.qbeacon.service.BlocoService;
import com.tcc.qbeacon.service.CampusService;

@Controller
@RequestMapping(path="/campus")
public class CampusController {
	
	@Autowired
	CampusService campusService;
	
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
		ModelAndView model = new ModelAndView("campus/formCadastrarCampus");
		model.addObject("campus", new Campus());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarCampus(@Valid Campus campus, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/campus/cadastrar";
		campusService.salvarCampus(campus);
		return "redirect:/campus/listar_campus";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarCampus(@PathVariable("id") Integer id) {
		Campus campus = campusService.buscarCampus(id);
		campusService.deletarCampus(campus);
		return "redirect:/campus/listar_campus";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarCampus(@PathVariable("id") Integer id) {
		Campus campus = campusService.buscarCampus(id);
		ModelAndView model = new ModelAndView("campus/formEditarCampus");
		model.addObject("campus", campus);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarCampus(@Valid Campus campus, BindingResult result) {	
		campusService.salvarCampus(campus);
		return "redirect:/campus/listar_campus";
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarCampus(@PathVariable("id") Integer id) {
		Campus campus = campusService.buscarCampus(id);
		ModelAndView model = new ModelAndView("campus/detalhesCampus");
		model.addObject("campus", campus);
		
		List<Bloco> blocos = blocoService.pegarBlocosValidos();		
		model.addObject("outrosBlocos",blocos);
		return model;
	}
	
	@GetMapping("/{id_campus}/adicionar_bloco/{id_bloco}")
	public String adicionarBloco(@PathVariable("id_campus") Integer id_campus,
									@PathVariable("id_bloco") Integer id_bloco) {
		Campus campus = campusService.buscarCampus(id_campus);
		
		Bloco bloco = blocoService.buscarBloco(id_bloco);
		bloco.setCampus(campus);
		bloco = blocoService.salvarBloco(bloco);
		
		List<Bloco> blocos = campus.getBlocos();
		blocos.add(bloco);
		
		campus.setBlocos(blocos);
		campusService.salvarCampus(campus);
		
		return "redirect:/campus/" + campus.getId();
	}
	
	@GetMapping("/{id_campus}/remover_bloco/{id_bloco}")
	public String removerBloco(@PathVariable("id_campus") Integer id_campus,
			@PathVariable("id_bloco") Integer id_bloco) {
		Campus campus = campusService.buscarCampus(id_campus);	
		Bloco bloco = blocoService.buscarBloco(id_bloco);
		
		List<Bloco> blocos = campus.getBlocos();
		blocos.remove(bloco);
		
		campus.setBlocos(blocos);
		campusService.salvarCampus(campus);
		
		bloco.setCampus(null);
		blocoService.salvarBloco(bloco);
		
		return "redirect:/campus/" + campus.getId();
	}
	
	@GetMapping("/{id_campus}/criar_bloco")
	public ModelAndView criarBloco(@PathVariable("id_campus") Integer id_campus) {
		ModelAndView model = new ModelAndView("bloco/formCadastrarBloco");
		
		model.addObject("bloco", new Bloco());
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
}
