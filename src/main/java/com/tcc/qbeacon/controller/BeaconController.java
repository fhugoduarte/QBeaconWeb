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
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.BeaconService;
import com.tcc.qbeacon.service.SalaService;

@Controller
@RequestMapping(path="/beacon")
public class BeaconController {

	@Autowired
	BeaconService beaconService;
	
	@Autowired
	SalaService salaService;
	
	@GetMapping(path="/listar_beacons")
	public ModelAndView listaBeacons() {
		ModelAndView model = new ModelAndView("beacon/listaBeacons");		
		List<Beacon> beacons = beaconService.pegarBeacons();
		model.addObject("beacons", beacons);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarBeacon() {
		List<Sala> salas = salaService.pegarSalasValidas();
		
		ModelAndView model = new ModelAndView("beacon/formCadastrarBeacon");
		model.addObject("beacon", new Beacon());
		model.addObject("salas", salas);
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarBeacon(@Valid Beacon beacon, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/beacon/cadastrar";
		
		Beacon beaconSalvo = beaconService.salvarBeacon(beacon);
		
		if(beaconSalvo.getSala() != null){
			Sala sala = beaconSalvo.getSala();
			sala.setBeacon(beaconSalvo);
			salaService.salvarSala(sala);
		}
		
		
		return "redirect:/beacon/" + beaconSalvo.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		
		if(beacon.getSala() != null){
			Sala sala = beacon.getSala();
			sala.setBeacon(null);
			salaService.salvarSala(sala);
		}
		
		beaconService.deletarBeacon(beacon);
		return "redirect:/beacon/listar_beacons";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		List<Sala> salas = salaService.pegarSalasValidas();
		
		ModelAndView model = new ModelAndView("beacon/formEditarBeacon");
		model.addObject("beacon", beacon);
		model.addObject("salas", salas);
		
		return model;
	}
	
	@PostMapping("/editar")
	public String editarBeacon(@Valid Beacon beacon, BindingResult result) {	
		Beacon velho = beaconService.buscarBeacon(beacon.getId());
		Sala salaVelha = velho.getSala();
		
		Beacon beaconSalvo = beaconService.salvarBeacon(beacon);
		
		if(salaVelha != beaconSalvo.getSala())			
			this.alterarSala(salaVelha, beaconSalvo.getSala(), beaconSalvo);
		
		return "redirect:/beacon/" +  beacon.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		List<Sala> salas = salaService.pegarSalasValidas();
		
		ModelAndView model = new ModelAndView("beacon/detalhesBeacon");
		model.addObject("beacon", beacon);
		model.addObject("salas", salas);
		
		return model;
	}
	
	@GetMapping("/{id_beacon}/adicionar_sala/{id_sala}")
	public String adicionarSala(@PathVariable("id_beacon") Integer id_beacon, @PathVariable("id_sala") Integer id_sala) {
		Sala sala = salaService.buscarSala(id_sala);
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		
		sala.setBeacon(beacon);
		salaService.salvarSala(sala);
		
		beacon.setSala(sala);
		beaconService.salvarBeacon(beacon);
			
		return "redirect:/beacon/"+ beacon.getId();
	}
	
	@GetMapping("/{id_beacon}/remover_sala/{id_sala}")
	public String removerSala(@PathVariable("id_beacon") Integer id_beacon, @PathVariable("id_sala") Integer id_sala) {
		Sala sala = salaService.buscarSala(id_sala);
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		
		sala.setBeacon(null);
		salaService.salvarSala(sala);
		
		beacon.setSala(null);
		beaconService.salvarBeacon(beacon);
			
		return "redirect:/beacon/"+ beacon.getId();
	}
	
	public void alterarSala (Sala antiga, Sala nova, Beacon beacon) {
		
		if(antiga != null) {
			antiga.setBeacon(null);
			salaService.salvarSala(antiga);
		}
		
		if(nova != null) {
			nova.setBeacon(beacon);
			salaService.salvarSala(nova);
		}
		
	}

}
