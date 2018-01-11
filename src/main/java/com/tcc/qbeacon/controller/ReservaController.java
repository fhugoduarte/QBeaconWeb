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

import com.tcc.qbeacon.model.Aula;
import com.tcc.qbeacon.model.Horario;
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.service.AulaService;
import com.tcc.qbeacon.service.HorarioService;
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
	
	@Autowired
	HorarioService horarioService;
	
	@Autowired
	AulaService aulaService;
	
	@GetMapping(path="/listar_reservas")
	public ModelAndView listaReservas() {
		ModelAndView model = new ModelAndView("reserva/listaReservas");
		List<Reserva> reservas = reservaService.pegarReservas();
		model.addObject("reservas", reservas);
		return model;
	}
	
	@GetMapping("/verificar_disponibilidade")
	public ModelAndView verificarHorarios() {
		ModelAndView model = new ModelAndView("reserva/formDisponibilidadeHorario");
		model.addObject("horario", new Horario());
		return model;
	}
	
	@PostMapping("/verificar_disponibilidade")
	public String verificaDisponibilidade(@Valid Horario horario, BindingResult result) {
		if (result.hasErrors()) return "redirect:/reserva/verificar_disponibilidade";
		
		if(horarioService.buscarHorario(horario) != null) {
			Horario horarioBanco = horarioService.buscarHorario(horario);
			return "redirect:/reserva/" + horarioBanco.getId() + "/cadastrar";
		}
		horarioService.salvarHorario(horario);	
		
		return "redirect:/reserva/" + horario.getId() + "/cadastrar";
	}
	
	@GetMapping("/{id_horario}/cadastrar")
	public ModelAndView cadastrarReserva(@PathVariable("id_horario") Integer id_horario) {
		Horario horario = horarioService.buscarHorario(id_horario);
		List<Sala> salas = salaService.pegarSalasVagas(horario);
		List<Turma> turmas = turmaService.pegarTurmasReservaveis();
		
		Reserva reserva = new Reserva();
		reserva.setHorario(horario);
		
		ModelAndView model = new ModelAndView("reserva/formCadastrarReserva");
		model.addObject("salas", salas);
		model.addObject("turmas", turmas);
		model.addObject("reserva", reserva);
		return model;
	}
	
	@PostMapping("/{id_horario}/cadastrar")
	public String salvarReserva(@PathVariable("id_horario") Integer id_horario, @Valid Reserva reserva, BindingResult result) {
		if (result.hasErrors()) return "redirect:/reserva/" + id_horario + "cadastrar";
		
		Horario horario = horarioService.buscarHorario(id_horario);
		reserva.setHorario(horario);
		
		Reserva reservaSalva = reservaService.salvarReserva(reserva);
		
		Sala sala = reservaSalva.getSala();
		sala = this.adicionarReservaSala(sala, reservaSalva);
		salaService.salvarSala(sala);

		Turma turma = reservaSalva.getTurma();
		turma = this.adicionarReservaTurma(turma, reservaSalva);
		turmaService.salvarTurma(turma);
		
		
		horario = this.adicionarReservaHorario(horario, reservaSalva);
		horarioService.salvarHorario(horario);		
	
		return "redirect:/reserva/" + reservaSalva.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarReserva(@PathVariable("id") Integer id) {
		Reserva reserva = reservaService.buscarReserva(id);
		
		Turma turma = reserva.getTurma();
		turma = this.removerReservaTurma(turma, reserva);
		turmaService.salvarTurma(turma);
		
		Horario horario = reserva.getHorario();
		horario = this.removerReservaHorario(horario, reserva);
		horarioService.salvarHorario(horario);
		
		Sala sala = reserva.getSala();
		sala = this.removerReservaSala(sala, reserva);
		salaService.salvarSala(sala);
		
		this.removerReservaAulas(reserva);
		
		reservaService.deletarReserva(reserva);
		return "redirect:/sala/" + sala.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarReserva(@PathVariable("id") Integer id) {
		Reserva reserva = reservaService.buscarReserva(id);
		ModelAndView model = new ModelAndView("reserva/detalhesReserva");
		model.addObject("reserva", reserva);
		
		return model;
	}
	
	public Sala adicionarReservaSala(Sala sala, Reserva reserva) {
		List<Reserva> reservas = sala.getReservas();
		reservas.add(reserva);
		sala.setReservas(reservas);
		
		return sala;
	}
	
	public Sala removerReservaSala(Sala sala, Reserva reserva) {
		List<Reserva> reservas = sala.getReservas();
		reservas.remove(reserva);
		sala.setReservas(reservas);
		
		return sala;
	}
	
	public Turma adicionarReservaTurma(Turma turma, Reserva reserva) {
		if(turma.getReserva1() == null) {
			turma.setReserva1(reserva);
			return turma;
		}
		turma.setReserva2(reserva);
		return turma;
	}
	
	public Turma removerReservaTurma(Turma turma, Reserva reserva) {
		if(turma.getReserva1().equals(reserva)) {
			turma.setReserva1(null);
			return turma;
		}
		turma.setReserva2(null);
		return turma;
	}
	
	public Horario adicionarReservaHorario(Horario horario, Reserva reserva) {
		List<Reserva> reservas = horario.getReservas();
		reservas.add(reserva);
		horario.setReservas(reservas);
		
		return horario;
	}
	
	public Horario removerReservaHorario(Horario horario, Reserva reserva) {
		List<Reserva> reservas = horario.getReservas();
		reservas.remove(reserva);
		horario.setReservas(reservas);
		
		return horario;
	}
	
	public void removerReservaAulas(Reserva reserva) {
		if(! reserva.getAulas().isEmpty())
			for (Aula aula : reserva.getAulas()) {
				aula.setReserva(null);
				aulaService.salvarAula(aula);
				if(reserva.getAulas().isEmpty())
					break;
			}
	}

}
