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

import com.tcc.qbeacon.model.Beacon;
import com.tcc.qbeacon.service.BeaconService;

@Controller
@RequestMapping(path="/beacon")
public class BeaconController {

	@Autowired
	BeaconService beaconService;
	
	@GetMapping(path="/listar_beacons")
	public ModelAndView listaBeacons() {
		ModelAndView model = new ModelAndView("listaBeacons");
		List<Beacon> beacons = beaconService.pegarBeacons();
		model.addObject("beacons", beacons);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarBeacon() {
		ModelAndView model = new ModelAndView("formCadastrarBeacon");
		model.addObject("beacon", new Beacon());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarBeacon(@Valid Beacon beacon, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/beacon/cadastrar";
		beaconService.salvarBeacon(beacon);
		return "redirect:/beacon/listar_beacons";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		beaconService.deletarBeacon(beacon);
		return "redirect:/beacon/listar_beacons";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		ModelAndView model = new ModelAndView("formEditarBeacon");
		model.addObject("beacon", beacon);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarBeacon(@Valid Beacon beacon, BindingResult result) {	
		beaconService.salvarBeacon(beacon);
		return "redirect:/beacon/listar_beacons";
	}
	
}
