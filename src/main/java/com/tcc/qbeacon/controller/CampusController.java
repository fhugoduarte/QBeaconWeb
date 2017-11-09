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
import com.tcc.qbeacon.service.CampusService;

@Controller
@RequestMapping(path="/campus")
public class CampusController {
	
	@Autowired
	CampusService campusService;
	
	@GetMapping(path="/listar_campus")
	public ModelAndView listaCampus() {
		ModelAndView model = new ModelAndView("listaCampus");
		List<Campus> campus = campusService.pegarCampus();
		model.addObject("campus", campus);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarCampus() {
		ModelAndView model = new ModelAndView("formCadastrarCampus");
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
		ModelAndView model = new ModelAndView("formEditarCampus");
		model.addObject("campus", campus);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarCampus(@Valid Campus campus, BindingResult result) {	
		campusService.salvarCampus(campus);
		return "redirect:/campus/listar_campus";
	}
}
