package com.tcc.qbeacon.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.SalaService;
import com.tcc.qbeacon.service.TurmaService;

@Controller
@RequestMapping(path="/reserva")
public class ReservaController {
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	SalaService salaService;
	
	@Autowired
	TurmaService turmaService;
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarReserva() {
		List<Sala> salas = salaService.pegarSalas();
		List<Turma> turmas = turmaService.pegarTurmas();
		
		ModelAndView model = new ModelAndView("beacon/formCadastrarBeacon");
		model.addObject("reserva", new Reserva());
		model.addObject("salas", salas);
		model.addObject("turmas", turmas);
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarReserva(@Valid Reserva reserva, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/reserva/cadastrar";
		
		Reserva reservaSalva = reservaService.salvarReserva(reserva);
		
		System.out.println(reservaSalva.getHorarios().get(0).getTurno());
		
		Sala sala = reservaSalva.getSala();
		sala = this.adicionarReservaSala(sala, reservaSalva);
		salaService.salvarSala(sala);
		
		Turma turma = reservaSalva.getTurma();
		turma.setReserva(reservaSalva);
		turmaService.salvarTurma(turma);		
		
		return "redirect:/reserva/" + reservaSalva.getId();
	}
	
	public Sala adicionarReservaSala(Sala sala, Reserva reserva) {
		List<Reserva> reservas = sala.getReservas();
		reservas.add(reserva);
		sala.setReservas(reservas);
		
		return sala;
	}

}
