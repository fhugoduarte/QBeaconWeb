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
import com.tcc.qbeacon.model.Campus;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.BeaconService;
import com.tcc.qbeacon.service.BlocoService;
import com.tcc.qbeacon.service.CampusService;
import com.tcc.qbeacon.service.SalaService;

@Controller
@RequestMapping(path="/bloco")
public class BlocoController {

	@Autowired
	BlocoService blocoService;
	
	@Autowired
	SalaService salaService;
	
	@Autowired
	CampusService campusService;
	
	@Autowired
	BeaconService beaconService;
	
	@GetMapping(path="/listar_blocos")
	public ModelAndView listaBlocos() {
		ModelAndView model = new ModelAndView("bloco/listaBlocos");
		List<Bloco> blocos = blocoService.pegarBlocos();
		model.addObject("blocos", blocos);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarBloco() {
		List<Campus> campus = campusService.pegarTodosCampus();
		
		ModelAndView model = new ModelAndView("bloco/formCadastrarBloco");
		model.addObject("campus", campus);
		model.addObject("bloco", new Bloco());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarBloco(@Valid Bloco bloco, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/bloco/cadastrar";
		Bloco blocoSalvo = blocoService.salvarBloco(bloco);
		Campus campus = blocoSalvo.getCampus();
		
		campus = this.adicionarBlocoCampus(campus, blocoSalvo);
		campusService.salvarCampus(campus);

		return "redirect:/bloco/" + blocoSalvo.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		
		Campus campus = bloco.getCampus();
		campus = this.removerBlocoCampus(campus, bloco);
		campusService.salvarCampus(campus);
		
		blocoService.deletarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		List<Campus> campus = campusService.pegarTodosCampus();
		
		ModelAndView model = new ModelAndView("bloco/formEditarBloco");
		model.addObject("bloco", bloco);
		model.addObject("campus", campus);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarBloco(@Valid Bloco bloco, BindingResult result) {
		Bloco velho = blocoService.buscarBloco(bloco.getId());
		Campus campusVelho = velho.getCampus();
		
		Bloco blocoSalvo = blocoService.salvarBloco(bloco);
		
		if(!bloco.getCampus().equals(campusVelho))
			this.alterarCampus(campusVelho, blocoSalvo.getCampus(), blocoSalvo);
		
		return "redirect:/bloco/" + blocoSalvo.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		ModelAndView model = new ModelAndView("bloco/detalhesBloco");
		model.addObject("bloco", bloco);
		
		return model;
	}
	
	@GetMapping("/{id_bloco}/criar_sala")
	public ModelAndView criarSala(@PathVariable("id_bloco") Integer id_bloco) {
		List<Beacon> beacons = beaconService.pegarBeaconsValidos();
		
		Bloco bloco = blocoService.buscarBloco(id_bloco);
		Sala sala = new Sala();
		sala.setBloco(bloco);
		
		ModelAndView model = new ModelAndView("sala/formCadastrarSala");
		model.addObject("sala", sala);
		model.addObject("beacons", beacons);
		return model;
	}
	
	@PostMapping("/{id_bloco}/criar_sala")
	public String salvarSala(@PathVariable("id_bloco") Integer id_bloco,
							@Valid Sala sala, BindingResult result) {
		if (result.hasErrors()) return "redirect:/bloco/"+id_bloco+"/criar_sala";
		
		Bloco bloco = blocoService.buscarBloco(id_bloco);
		sala.setBloco(bloco);
		Sala salaSalva = salaService.salvarSala(sala);
		
		List<Sala> salas = bloco.getSalas();
		salas.add(salaSalva);
		bloco.setSalas(salas);
		
		if(salaSalva.getBeacon() != null){
			Beacon beacon = salaSalva.getBeacon();
			beacon.setSala(salaSalva);
			beaconService.salvarBeacon(beacon);
		}
		
		blocoService.salvarBloco(bloco);
		
		return "redirect:/bloco/"+bloco.getId();
		
	}
	
	public Campus adicionarBlocoCampus(Campus campus, Bloco bloco) {
		List<Bloco> blocos = campus.getBlocos();
		blocos.add(bloco);
		campus.setBlocos(blocos);
		
		return campus;
	}
	
	public Campus removerBlocoCampus(Campus campus, Bloco bloco) {
		List<Bloco> blocos = campus.getBlocos();
		blocos.remove(bloco);
		campus.setBlocos(blocos);
		
		return campus;
	}
	
	public void alterarCampus (Campus antigo, Campus novo, Bloco bloco) {
		
		List<Bloco> blocos = antigo.getBlocos();
		blocos.remove(bloco);
		antigo.setBlocos(blocos);
		campusService.salvarCampus(antigo);
		
		blocos = novo.getBlocos();
		blocos.add(bloco);
		novo.setBlocos(blocos);
		campusService.salvarCampus(novo);
		
	}
	
}
