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
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.SalaService;
import com.tcc.qbeacon.service.TurmaService;

@Controller
@RequestMapping(path="/sala")
public class SalaController {

	@Autowired
	SalaService salaService;
	
	@Autowired
	BlocoService blocoService;
	
	@Autowired
	BeaconService beaconService;
	
	@Autowired
	TurmaService turmaService;
	
	@Autowired
	ReservaService reservaService;
	
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
		
		//Adiciona a sala a lista de salas do bloco e salva o bloco.
		bloco = this.adicionarSalaBloco(bloco, salaSalva);
		blocoService.salvarBloco(bloco);
		
		//Se a sala tiver um beacon, adiciona a sala ao beacon e salva o beacon.
		if(salaSalva.getBeacon() != null){
			Beacon beacon = salaSalva.getBeacon();
			beacon.setSala(salaSalva);
			beaconService.salvarBeacon(beacon);
		}
		
		return "redirect:/sala/" + salaSalva.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		
		//Remove a sala da lista de salas do bloco e salva o bloco.
		Bloco bloco = sala.getBloco();
		bloco = this.removerSalaBloco(bloco, sala);
		blocoService.salvarBloco(bloco);
		
		//Se a sala tiver beacon, tira a sala do beacon e salva.
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
		
		//Verifica se o bloco foi alterado, caso sim, chama a função que altera o bloco.
		if(!salaSalva.getBloco().equals(blocoVelho))
			this.alterarBloco(blocoVelho, salaSalva.getBloco(), salaSalva);

		//Verifica se o beacon foi alterado, caso sim, chama a função que altera o beacon.
		if(salaSalva.getBeacon() != beaconVelho)
			this.alterarBeacon(beaconVelho, salaSalva.getBeacon(), salaSalva);
		
		return "redirect:/sala/" + salaSalva.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarSala(@PathVariable("id") Integer id) {
		Sala sala = salaService.buscarSala(id);
		List<Beacon> beacons = beaconService.pegarBeaconsValidos();
		
		ModelAndView model = new ModelAndView("sala/detalhesSala");
		model.addObject("sala", sala);
		model.addObject("beacons", beacons);
		
		return model;
	}
	
	//Adiciona um beacon a uma sala.
	@GetMapping("/{id_sala}/adicionar_beacon/{id_beacon}")
	public String adicionarBeacon(@PathVariable("id_sala") Integer id_sala, @PathVariable("id_beacon") Integer id_beacon) {
		Sala sala = salaService.buscarSala(id_sala);
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		
		sala.setBeacon(beacon);
		salaService.salvarSala(sala);
		
		beacon.setSala(sala);
		beaconService.salvarBeacon(beacon);
			
		return "redirect:/sala/"+ sala.getId();
	}
	
	//Remove um beacon de uma sala.
	@GetMapping("/{id_sala}/remover_beacon/{id_beacon}")
	public String removerBeacon(@PathVariable("id_sala") Integer id_sala, @PathVariable("id_beacon") Integer id_beacon) {
		Sala sala = salaService.buscarSala(id_sala);
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		
		sala.setBeacon(null);
		salaService.salvarSala(sala);
		
		beacon.setSala(null);
		beaconService.salvarBeacon(beacon);
			
		return "redirect:/sala/"+ sala.getId();
	}
	
	//Cria um beacon já com uma sala informada.
	@GetMapping("/{id_sala}/criar_beacon")
	public ModelAndView criarBeacon(@PathVariable("id_sala") Integer id_sala) {
		Sala sala = salaService.buscarSala(id_sala);
		
		Beacon beacon = new Beacon();
		beacon.setSala(sala);
		
		ModelAndView model = new ModelAndView("sala/formCadastrarSala");
		model.addObject("beacon", beacon);
		return model;
	}
	
	//Adiciona a sala a lista de salas do bloco.
	public Bloco adicionarSalaBloco(Bloco bloco, Sala sala) {
		List<Sala> salas = bloco.getSalas();
		salas.add(sala);
		bloco.setSalas(salas);
		
		return bloco;
	}
	
	//Remove a sala da lista de salas do bloco.
	public Bloco removerSalaBloco(Bloco bloco, Sala sala) {
		List<Sala> salas = bloco.getSalas();
		salas.remove(sala);
		bloco.setSalas(salas);
		
		return bloco;
	}
	
	public void alterarBloco (Bloco antigo, Bloco novo, Sala sala) {
		
		//Remove a sala da lista de salas do bloco antigo e salva o bloco.
		List<Sala> salas = antigo.getSalas();
		salas.remove(sala);
		antigo.setSalas(salas);
		blocoService.salvarBloco(antigo);
		
		//Adiciona a sala a lista de salas do bloco novo. e salva o bloco
		salas = novo.getSalas();
		salas.add(sala);
		novo.setSalas(salas);
		blocoService.salvarBloco(novo);
		
	}
	
	public void alterarBeacon (Beacon antigo, Beacon novo, Sala sala) {
		
		//Se o beacon antigo não for nulo, ele seta a sala do beacon como nula e salva o beacon.
		if(antigo != null) {
			antigo.setSala(null);
			beaconService.salvarBeacon(antigo);
		}
		
		//Se o novo beacon não for nulo, ele seta a sala do beacon como a sala passada por parametro e salva o beacon.
		if(novo != null) {
			novo.setSala(sala);
			beaconService.salvarBeacon(novo);
		}
		
	}
	
}
