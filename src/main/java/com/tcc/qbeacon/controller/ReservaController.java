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
		
		//Verifica se já existe o horário informado, caso exista ele adiciona esse horário a reserva e redireciona para o cadastro da mesma.
		if(horarioService.buscarHorario(horario) != null) {
			Horario horarioBanco = horarioService.buscarHorario(horario);
			return "redirect:/reserva/" + horarioBanco.getId() + "/cadastrar";
		}
		//Caso não exista ele cria um novo horário, salva esse horário e redireciona para o cadastro da reserva.
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
		
		//Refireciona para a página de cadastrar uma reserva já com o horário setado.
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
		
		//Adiciona a reserva a sala informada e salva a sala.
		Sala sala = reservaSalva.getSala();
		sala = this.adicionarReservaSala(sala, reservaSalva);
		salaService.salvarSala(sala);

		//Adiciona a reserva a turma informada e salva a turma.
		Turma turma = reservaSalva.getTurma();
		turma = this.adicionarReservaTurma(turma, reservaSalva);
		turmaService.salvarTurma(turma);
		
		//Adiciona a reserva ao horário informado e salva o horário.
		horario = this.adicionarReservaHorario(horario, reservaSalva);
		horarioService.salvarHorario(horario);		
	
		return "redirect:/reserva/" + reservaSalva.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarReserva(@PathVariable("id") Integer id) {
		Reserva reserva = reservaService.buscarReserva(id);
		
		//Remove a reserva da turma e salva a turma.
		Turma turma = reserva.getTurma();
		turma = this.removerReservaTurma(turma, reserva);
		turmaService.salvarTurma(turma);
		
		//Remove a reserva da lista de reservas do horário e salva o horário.
		Horario horario = reserva.getHorario();
		horario = this.removerReservaHorario(horario, reserva);
		horarioService.salvarHorario(horario);
		
		//Remove a reserva da lista de reservas da sala e salva a sala.
		Sala sala = reserva.getSala();
		sala = this.removerReservaSala(sala, reserva);
		salaService.salvarSala(sala);
		
		//Remove as aulas da reserva.
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
	
	//Adiciona a reserva a lista de reservas da sala.
	public Sala adicionarReservaSala(Sala sala, Reserva reserva) {
		List<Reserva> reservas = sala.getReservas();
		reservas.add(reserva);
		sala.setReservas(reservas);
		
		return sala;
	}
	
	//Remove a reserva da lista de reservas da sala.
	public Sala removerReservaSala(Sala sala, Reserva reserva) {
		List<Reserva> reservas = sala.getReservas();
		reservas.remove(reserva);
		sala.setReservas(reservas);
		
		return sala;
	}
	
	//Adiciona a reserva da turma.
	public Turma adicionarReservaTurma(Turma turma, Reserva reserva) {
		if(turma.getReserva1() == null) {
			turma.setReserva1(reserva);
			return turma;
		}
		turma.setReserva2(reserva);
		return turma;
	}
	
	//Remove a reserva da turma.
	public Turma removerReservaTurma(Turma turma, Reserva reserva) {
		if(turma.getReserva1().equals(reserva)) {
			turma.setReserva1(null);
			return turma;
		}
		turma.setReserva2(null);
		return turma;
	}
	
	//Adiciona a reserva a lista de reservas do horário.
	public Horario adicionarReservaHorario(Horario horario, Reserva reserva) {
		List<Reserva> reservas = horario.getReservas();
		reservas.add(reserva);
		horario.setReservas(reservas);
		
		return horario;
	}
	
	//Remove a reserva da lista de reservas do horário.
	public Horario removerReservaHorario(Horario horario, Reserva reserva) {
		List<Reserva> reservas = horario.getReservas();
		reservas.remove(reserva);
		horario.setReservas(reservas);
		
		return horario;
	}
	
	//Remove a reserva de todas as aulas da reserva e salva as aulas.
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
