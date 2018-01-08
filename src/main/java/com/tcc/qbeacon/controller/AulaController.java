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
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.AulaService;
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.TurmaService;
import com.tcc.qbeacon.service.UsuarioService;

@Controller
@RequestMapping(path="/aula")
public class AulaController {
	
	@Autowired
	AulaService aulaService;
	
	@Autowired
	TurmaService turmaService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/{id}")
	public ModelAndView visualizarAula(@PathVariable("id") Integer id) {
		Aula aula = aulaService.buscarAula(id);
		ModelAndView model = new ModelAndView("aula/detalhesAula");
		model.addObject("aula", aula);
		
		return model;
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarAula(@PathVariable("id") Integer id) {
		Aula aula = aulaService.buscarAula(id);
		
		Turma turma = aula.getTurma();
		turma = this.removerAulaTurma(turma, aula);
		turmaService.salvarTurma(turma);
		
		Reserva reserva = aula.getReserva();
		reserva = this.removerAulaReserva(reserva, aula);
		reservaService.salvarReserva(reserva);
		
		this.removerAulaAlunos(aula.getFrequencia(), aula);
		
		aulaService.deletarAula(aula);
		return "redirect:/turma/" + turma.getId();
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarAula(@PathVariable("id") Integer id) {
		Aula aula = aulaService.buscarAula(id);
		Reserva reserva1 = aula.getTurma().getReserva1();
		Reserva reserva2 = aula.getTurma().getReserva2();
		
		ModelAndView model = new ModelAndView("aula/formEditarAula");
		model.addObject("aula", aula);
		model.addObject("reserva1", reserva1);
		model.addObject("reserva2", reserva2);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarAula(@Valid Aula aula, BindingResult result) {
		aulaService.salvarAula(aula);
		
		return "redirect:/aula/" + aula.getId();
	}
	
	@GetMapping("/editar_frequencia/{id}")
	public ModelAndView editarFrequenciaAula(@PathVariable("id") Integer id) {
		Aula aula = aulaService.buscarAula(id);
		List<Usuario> alunos = aula.getFrequencia();
		
		ModelAndView model = new ModelAndView("aula/formEditarFrequencia");
		model.addObject("aula", aula);
		model.addObject("alunos", alunos);
		return model;
	}
	
	@PostMapping("/editar_frequencia/{id}")
	public String editarFrequenciaAula(@PathVariable("id") Integer id, @Valid List<Usuario> alunos, BindingResult result) {
		Aula aula = aulaService.buscarAula(id);
		List<Usuario> alunosAntigos = aula.getFrequencia();
		
		for (Usuario aluno : alunosAntigos) {
			if(!alunos.contains(aluno)) {
				aluno = this.removerAulaAluno(aluno, aula);
				usuarioService.salvarUsuario(aluno);
			}	
		}
		
		for (Usuario aluno : alunos) {
			aluno = this.adicionarAulaAluno(aluno, aula);
			usuarioService.salvarUsuario(aluno);
		}
		
		aula.setFrequencia(alunos);
		aulaService.salvarAula(aula);
		
		return "redirect:/aula/" + aula.getId();
	}
	
	public Turma removerAulaTurma(Turma turma, Aula aula) {
		List<Aula> aulas = turma.getAulas();
		aulas.remove(aula);
		turma.setAulas(aulas);
		
		return turma;
	}
	
	public Reserva removerAulaReserva(Reserva reserva, Aula aula) {
		List<Aula> aulas = reserva.getAulas();
		aulas.remove(aula);
		reserva.setAulas(aulas);
		
		return reserva;
	}
	
	public void removerAulaAlunos(List<Usuario> alunos, Aula aula) {
		
		for (Usuario aluno : alunos) {
			List<Aula> aulas = aluno.getAulas();
			aulas.remove(aula);
			aluno.setAulas(aulas);
			usuarioService.salvarUsuario(aluno);
		}
	}
	
	public Usuario removerAulaAluno(Usuario aluno, Aula aula) {
		List<Aula> aulas = aluno.getAulas();
		aulas.remove(aula);
		aluno.setAulas(aulas);
		
		return aluno;
	}
	
	public Usuario adicionarAulaAluno(Usuario aluno, Aula aula) {
		List<Aula> aulas = aluno.getAulas();
		aulas.add(aula);
		aluno.setAulas(aulas);
		
		return aluno;
	}

}
