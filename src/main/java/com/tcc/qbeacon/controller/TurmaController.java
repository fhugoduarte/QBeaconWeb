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
import com.tcc.qbeacon.model.Disciplina;
import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.service.AulaService;
import com.tcc.qbeacon.service.DisciplinaService;
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.TurmaService;

@Controller
@RequestMapping(path="/turma")
public class TurmaController {
	
	@Autowired
	TurmaService turmaService;
	
	@Autowired
	DisciplinaService disciplinaService;
	
	@Autowired
	AulaService aulaService;
	
	@Autowired
	ReservaService reservaService;
	
	@GetMapping(path="/listar_turmas")
	public ModelAndView listaTurmas() {
		ModelAndView model = new ModelAndView("turma/listaTurmas");
		List<Turma> turmas = turmaService.pegarTurmas();
		model.addObject("turmas", turmas);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarTurma() {
		ModelAndView model = new ModelAndView("turma/formCadastrarTurma");
		List<Disciplina> disciplinas = disciplinaService.pegarDisciplinas();
		model.addObject("turma", new Turma());
		model.addObject("disciplinas", disciplinas);
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarTurma(@Valid Turma turma, BindingResult result ) {
		Turma turmaSalva = turmaService.salvarTurma(turma);
		Disciplina disciplina = turmaSalva.getDisciplina();
		
		//Adiciona a turma a lista de turmas da disciplina e salva a disciplina.
		disciplina = this.adicionarTurmaDisciplina(disciplina, turmaSalva);
		disciplinaService.salvarDisciplina(disciplina);
		
		return "redirect:/turma/listar_turmas";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarTurma(@PathVariable("id") Integer id) {
		Turma turma = turmaService.buscarTurma(id);
		
		//Remove a turma da lista de turmas da disciplina e salva a disciplina.
		Disciplina disciplina = turma.getDisciplina();
		disciplina = this.removerTurmaDisciplina(disciplina, turma);
		disciplinaService.salvarDisciplina(disciplina);
		
		turmaService.deletarTurma(turma);
		return "redirect:/turma/listar_turmas";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarTurma(@PathVariable("id") Integer id) {
		Turma turma = turmaService.buscarTurma(id);
		List<Disciplina> disciplinas = disciplinaService.pegarDisciplinas();
		
		ModelAndView model = new ModelAndView("turma/formEditarTurma");
		model.addObject("turma", turma);
		model.addObject("disciplinas", disciplinas);
		return model;
	}
	
	@PostMapping("/editar")
	public String editarTurma(@Valid Turma turma, BindingResult result) {	
		Turma turmaAntiga = turmaService.buscarTurma(turma.getId());
		
		//Se a disciplina não tiver sido alterada, ele salva a turma.
		if(turma.getDisciplina().equals(turmaAntiga.getDisciplina())){
			turmaService.salvarTurma(turma);
			return "redirect:/turma/listar_turmas";
		}
		
		//Caso contrário, ele altera a disciplina.
		Disciplina disciplinaAntiga = turmaAntiga.getDisciplina();
		
		//Remove a turma da lista de turmas da disciplina antiga e salva a disciplina antiga.
		disciplinaAntiga = this.removerTurmaDisciplina(disciplinaAntiga, turmaAntiga);
		disciplinaService.salvarDisciplina(disciplinaAntiga);
		
		//Adiciona a turma na lista de turmas da disciplina nova e salva a disciplina nova.
		Disciplina disciplinaNova = turma.getDisciplina();
		disciplinaNova = this.adicionarTurmaDisciplina(disciplinaNova, turma);
		disciplinaNova = disciplinaService.salvarDisciplina(disciplinaNova);
		
		//Adiciona a disciplina nova na turma e salva a turma.
		turma.setDisciplina(disciplinaNova);
		turmaService.salvarTurma(turma);
		
		return "redirect:/turma/listar_turmas";
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarTurma(@PathVariable("id") Integer id) {
		Turma turma = turmaService.buscarTurma(id);
		ModelAndView model = new ModelAndView("turma/detalhesTurma");
		model.addObject("turma", turma);
		
		return model;
	}
	
	//Cria uma nova aula já setando a turma da aula.
	@GetMapping("/{id_turma}/criar_aula")
	public ModelAndView criarAula(@PathVariable("id_turma") Integer id_turma) {
		
		Turma turma = turmaService.buscarTurma(id_turma);
		Aula aula = new Aula();
		aula.setTurma(turma);
		
		ModelAndView model = new ModelAndView("aula/formCadastrarAula");
		model.addObject("aula", aula);
		return model;
	
	}
	
	@PostMapping("/{id_turma}/criar_aula")
	public String salvarAula(@PathVariable("id_turma") Integer id_turma,
							@Valid Aula aula, BindingResult result) {
		
		//Adiciona a turma na aula, coloca a frequencia como nula e salva a aula.
		Turma turma = turmaService.buscarTurma(id_turma);
		aula.setTurma(turma);
		aula.setFrequencia(null);
		Aula aulaSalva = aulaService.salvarAula(aula);
		
		//Adiciona aula a lista de aulas da turma e salva a turma.
		List<Aula> aulas = turma.getAulas();
		aulas.add(aulaSalva);
		turma.setAulas(aulas);
		turmaService.salvarTurma(turma);
		
		//Adiciona a aula a lista de aulas da reserva e salva a reserva.
		Reserva reserva = reservaService.buscarReserva(aulaSalva.getReserva().getId());
		aulas = reserva.getAulas();
		aulas.add(aulaSalva);
		reserva.setAulas(aulas);
		reservaService.salvarReserva(reserva);
		
		return "redirect:/turma/"+turma.getId();
	}
	
	//Adiciona a turma a lista de turmas da disciplina.
	public Disciplina adicionarTurmaDisciplina(Disciplina disciplina, Turma turma) {
		List<Turma> turmas = disciplina.getTurmas();
		turmas.add(turma);
		disciplina.setTurmas(turmas);
		
		return disciplina;
	}
	
	//Remove a turma da lista de turmas da disciplina.
	public Disciplina removerTurmaDisciplina(Disciplina disciplina, Turma turma) {
		List<Turma> turmas = disciplina.getTurmas();
		turmas.remove(turma);
		disciplina.setTurmas(turmas);
		
		return disciplina;
	}

}
