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
		
		//Remove a aula que vai ser deletada da lista de aulas da turma.
		Turma turma = aula.getTurma();
		turma = this.removerAulaTurma(turma, aula);
		turmaService.salvarTurma(turma);
		
		//Remove a aula da lista de aulas da reserva, assim esse horário fica livre para ser reservado.
		Reserva reserva = aula.getReserva();
		reserva = this.removerAulaReserva(reserva, aula);
		reservaService.salvarReserva(reserva);
		
		//Remove a aula da lista de aulas de cada aluno que estava na aula.
		this.removerAulaAlunos(aula.getFrequencia(), aula);
		
		//Exclui a aula do DB.
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
		List<Usuario> alunosPresentes = aula.getFrequencia();
		List<Usuario> todosAlunos = aula.getTurma().getAlunos();
		
		ModelAndView model = new ModelAndView("aula/formEditarFrequencia");
		model.addObject("aula", aula);
		model.addObject("alunosPresentes", alunosPresentes);
		model.addObject("todosAlunos", todosAlunos);
		return model;
	}
	
	@PostMapping("/editar_frequencia/{id}")
	public String editarFrequenciaAula(@PathVariable("id") Integer id, @Valid Aula aulaEditada, BindingResult result) {
		Aula aula = aulaService.buscarAula(id);
		List<Usuario> alunosAntigos = aula.getFrequencia();
		List<Usuario> alunosAdicionados = aulaEditada.getFrequencia();
		
		//Adiciona a lista de alunos antigos os novos alunos selecionados, 
		//porém se o aluno já estava na lista antes, ele não é adicionado
		for (Usuario aluno : alunosAntigos) {
			if(!alunosAdicionados.contains(aluno)) {
				aluno = this.removerAulaAluno(aluno, aula);
				usuarioService.salvarUsuario(aluno);
			}	
		}
		
		//Adiciona a aula a lista de aulas dos alunos adicionados recentemente,
		//porém se o aluno já estava na lista antes, ele não é adicionado
		for (Usuario aluno : alunosAdicionados) {
			if(!alunosAdicionados.contains(aluno)) {
				aluno = this.adicionarAulaAluno(aluno, aula);
				usuarioService.salvarUsuario(aluno);
			}
		}
		
		//Atualiza a lista de alunos da aula e salva a aula no DB.
		aula.setFrequencia(alunosAdicionados);
		aulaService.salvarAula(aula);
		
		return "redirect:/aula/" + aula.getId();
	}
	
	//Remove a aula da lista de aulas da turma.
	public Turma removerAulaTurma(Turma turma, Aula aula) {
		List<Aula> aulas = turma.getAulas();
		aulas.remove(aula);
		turma.setAulas(aulas);
		
		return turma;
	}
	
	//Remove a aula da lista de aulas da reserva.
	public Reserva removerAulaReserva(Reserva reserva, Aula aula) {
		List<Aula> aulas = reserva.getAulas();
		aulas.remove(aula);
		reserva.setAulas(aulas);
		
		return reserva;
	}
	
	//Remove a aula da lista de aulas de cada aluno passado na lista "alunos".
	public void removerAulaAlunos(List<Usuario> alunos, Aula aula) {
		
		for (Usuario aluno : alunos) {
			List<Aula> aulas = aluno.getAulas();
			aulas.remove(aula);
			aluno.setAulas(aulas);
			usuarioService.salvarUsuario(aluno);
		}
	}
	
	//Remove a aula da lista de aulas de um aluno especifico.
	public Usuario removerAulaAluno(Usuario aluno, Aula aula) {
		List<Aula> aulas = aluno.getAulas();
		aulas.remove(aula);
		aluno.setAulas(aulas);
		
		return aluno;
	}
	
	//Adiciona a aula a lista de aulas de um aluno especifico.
	public Usuario adicionarAulaAluno(Usuario aluno, Aula aula) {
		List<Aula> aulas = aluno.getAulas();
		aulas.add(aula);
		aluno.setAulas(aulas);
		
		return aluno;
	}

}
