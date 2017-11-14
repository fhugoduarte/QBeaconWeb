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
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.BlocoService;
import com.tcc.qbeacon.service.SalaService;

@Controller
@RequestMapping(path="/bloco")
public class BlocoController {

	@Autowired
	BlocoService blocoService;
	
	@Autowired
	SalaService salaService;
	
	@GetMapping(path="/listar_blocos")
	public ModelAndView listaBlocos() {
		ModelAndView model = new ModelAndView("bloco/listaBlocos");
		List<Bloco> blocos = blocoService.pegarBlocos();
		model.addObject("blocos", blocos);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarBloco() {
		ModelAndView model = new ModelAndView("bloco/formCadastrarBloco");
		model.addObject("bloco", new Bloco());
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarBloco(@Valid Bloco bloco, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/bloco/cadastrar";
		blocoService.salvarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		blocoService.deletarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		ModelAndView model = new ModelAndView("bloco/formEditarBloco");
		model.addObject("bloco", bloco);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarBloco(@Valid Bloco bloco, BindingResult result) {	
		blocoService.salvarBloco(bloco);
		return "redirect:/bloco/listar_blocos";
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarBloco(@PathVariable("id") Integer id) {
		Bloco bloco = blocoService.buscarBloco(id);
		ModelAndView model = new ModelAndView("bloco/detalhesBloco");
		model.addObject("bloco", bloco);
		
		List<Sala> salas = salaService.pegarSalasValidas();		
		model.addObject("outrasSalas",salas);
		return model;
	}
	
	@GetMapping("/{id_bloco}/adicionar_sala/{id_sala}")
	public String adicionarSala(@PathVariable("id_bloco") Integer id_bloco,
									@PathVariable("id_sala") Integer id_sala) {
		Bloco bloco = blocoService.buscarBloco(id_bloco);
		
		Sala sala = salaService.buscarSala(id_sala);
		sala.setBloco(bloco);
		sala = salaService.salvarSala(sala);
		
		List<Sala> salas = bloco.getSalas();
		salas.add(sala);
		
		bloco.setSalas(salas);
		blocoService.salvarBloco(bloco);
		
		return "redirect:/bloco/" + bloco.getId();
	}
	
	@GetMapping("/{id_bloco}/remover_sala/{id_sala}")
	public String removerSala(@PathVariable("id_bloco") Integer id_bloco,
			@PathVariable("id_sala") Integer id_sala) {
		Bloco bloco = blocoService.buscarBloco(id_bloco);	
		Sala sala = salaService.buscarSala(id_sala);
		
		List<Sala> salas = bloco.getSalas();
		salas.remove(sala);
		
		bloco.setSalas(salas);
		blocoService.salvarBloco(bloco);
		
		sala.setBloco(null);
		salaService.salvarSala(sala);
		
		return "redirect:/bloco/" + bloco.getId();
	}
	
	@GetMapping("/{id_bloco}/criar_sala")
	public ModelAndView criarSala(@PathVariable("id_bloco") Integer id_bloco) {
		ModelAndView model = new ModelAndView("sala/formCadastrarSala");
		
		model.addObject("sala", new Sala());
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
		
		blocoService.salvarBloco(bloco);
		
		return "redirect:/bloco/"+bloco.getId();
		
	}
	
}
