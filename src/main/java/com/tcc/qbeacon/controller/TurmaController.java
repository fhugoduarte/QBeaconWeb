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
		
		disciplina = this.adicionarTurmaDisciplina(disciplina, turmaSalva);
		disciplinaService.salvarDisciplina(disciplina);
		
		return "redirect:/turma/listar_turmas";
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarTurma(@PathVariable("id") Integer id) {
		Turma turma = turmaService.buscarTurma(id);
		
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
		
		if(turma.getDisciplina().equals(turmaAntiga.getDisciplina())){
			turmaService.salvarTurma(turma);
			return "redirect:/turma/listar_turmas";
		}
		
		Disciplina disciplinaAntiga = turmaAntiga.getDisciplina();
		
		disciplinaAntiga = this.removerTurmaDisciplina(disciplinaAntiga, turmaAntiga);
		disciplinaService.salvarDisciplina(disciplinaAntiga);
		
		Disciplina disciplinaNova = turma.getDisciplina();
		disciplinaNova = this.adicionarTurmaDisciplina(disciplinaNova, turma);
		
		disciplinaNova = disciplinaService.salvarDisciplina(disciplinaNova);
		
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
		
		Turma turma = turmaService.buscarTurma(id_turma);
		aula.setTurma(turma);
		aula.setFrequencia(null);
		Aula aulaSalva = aulaService.salvarAula(aula);
		
		List<Aula> aulas = turma.getAulas();
		aulas.add(aulaSalva);
		turma.setAulas(aulas);
		turmaService.salvarTurma(turma);
		
		Reserva reserva = reservaService.buscarReserva(aulaSalva.getReserva().getId());
		aulas = reserva.getAulas();
		aulas.add(aulaSalva);
		reserva.setAulas(aulas);
		reservaService.salvarReserva(reserva);
		
		return "redirect:/turma/"+turma.getId();
	}
	
	public Disciplina adicionarTurmaDisciplina(Disciplina disciplina, Turma turma) {
		List<Turma> turmas = disciplina.getTurmas();
		turmas.add(turma);
		disciplina.setTurmas(turmas);
		
		return disciplina;
	}
	
	public Disciplina removerTurmaDisciplina(Disciplina disciplina, Turma turma) {
		List<Turma> turmas = disciplina.getTurmas();
		turmas.remove(turma);
		disciplina.setTurmas(turmas);
		
		return disciplina;
	}

}
