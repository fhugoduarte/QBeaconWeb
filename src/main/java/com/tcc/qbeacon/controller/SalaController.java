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
import com.tcc.qbeacon.model.Bloco;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.BeaconService;
import com.tcc.qbeacon.service.BlocoService;
import com.tcc.qbeacon.service.SalaService;

@Controller
@RequestMapping(path="/sala")
public class SalaController {

	@Autowired
	SalaService salaService;
	
	@Autowired
	BlocoService blocoService;
	
	@Autowired
	BeaconService beaconService;
	
	@GetMapping(path="/listar_salas")
	public ModelAndView listaSalas() {
		ModelAndView model = new ModelAndView("sala/listaSalas");
		List<Sala> salas = salaService.pegarSalas();
		model.addObject("salas", salas);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarSala() {
		List<Bloco> blocos = blocoService.pegarBlocos();
		List<Beacon> beacons = beaconService.pegarBeaconsValidos();
		
		ModelAndView model = new ModelAndView("sala/formCadastrarSala");
		model.addObject("beacons", beacons);
		model.addObject("blocos", blocos);
		model.addObject("sala", new Sala());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarSala(@Valid Sala sala, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/sala/cadastrar";
		Sala salaSalva = salaService.salvarSala(sala);
		Bloco bloco = salaSalva.getBloco();
		
		bloco = this.adicionarSalaBloco(bloco, salaSalva);
		blocoService.salvarBloco(bloco);
		
		if(salaSalva.getBeacon() != null){
			Beacon beacon = salaSalva.getBeacon();
			beacon.setSala(salaSalva);
			beaconService.salvarBeacon(beacon);
		}
		
		return "redirect:/sala/listar_salas";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		
		Bloco bloco = sala.getBloco();
		bloco = this.removerrSalaBloco(bloco, sala);
		blocoService.salvarBloco(bloco);
		
		if(sala.getBeacon() != null){
			Beacon beacon = sala.getBeacon();
			beacon.setSala(null);
			beaconService.salvarBeacon(beacon);
		}
		
		salaService.deletarSala(sala);
		return "redirect:/sala/listar_salas";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		List<Bloco> blocos = blocoService.pegarBlocos();
		List<Beacon>beacons = beaconService.pegarBeaconsValidos();
		
		ModelAndView model = new ModelAndView("sala/formEditarSala");
		model.addObject("sala", sala);
		model.addObject("blocos", blocos);
		model.addObject("beacons", beacons);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarSala(@Valid Sala sala, BindingResult result) {	
		Sala velha = salaService.buscarSala(sala.getId());
		Beacon beaconVelho = velha.getBeacon();
		Bloco blocoVelho = velha.getBloco();
		
		Sala salaSalva = salaService.salvarSala(sala);
		
		if(!salaSalva.getBloco().equals(blocoVelho))
			this.alterarBloco(blocoVelho, salaSalva.getBloco(), salaSalva);

		if(salaSalva.getBeacon() != beaconVelho)
			this.alterarBeacon(beaconVelho, salaSalva.getBeacon(), salaSalva);
		
		return "redirect:/sala/listar_salas";
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		ModelAndView model = new ModelAndView("sala/detalhesSala");
		model.addObject("sala", sala);
		
		return model;
	}
	
	public Bloco adicionarSalaBloco(Bloco bloco, Sala sala) {
		List<Sala> salas = bloco.getSalas();
		salas.add(sala);
		bloco.setSalas(salas);
		
		return bloco;
	}
	
	public Bloco removerrSalaBloco(Bloco bloco, Sala sala) {
		List<Sala> salas = bloco.getSalas();
		salas.remove(sala);
		bloco.setSalas(salas);
		
		return bloco;
	}
	
	public void alterarBloco (Bloco antigo, Bloco novo, Sala sala) {
		
		List<Sala> salas = antigo.getSalas();
		salas.remove(sala);
		antigo.setSalas(salas);
		blocoService.salvarBloco(antigo);
		
		salas = novo.getSalas();
		salas.add(sala);
		novo.setSalas(salas);
		blocoService.salvarBloco(novo);
		
	}
	
public void alterarBeacon (Beacon antigo, Beacon novo, Sala sala) {
		
		if(antigo != null) {
			antigo.setSala(null);
			beaconService.salvarBeacon(antigo);
		}
		
		if(novo != null) {
			novo.setSala(sala);
			beaconService.salvarBeacon(novo);
		}
		
	}
	
}
