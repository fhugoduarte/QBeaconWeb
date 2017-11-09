package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Disciplina;
import com.tcc.qbeacon.repository.DisciplinaRepository;

@Service
public class DisciplinaService {
	
	@Autowired
	DisciplinaRepository disciplinaRepo;
	
	public Disciplina salvarDisciplina(Disciplina disciplina) {
		return disciplinaRepo.save(disciplina);
	}
	
	public Disciplina buscarDisciplina(Integer id) {
		return disciplinaRepo.findOne(id);
	}
	
	public List<Disciplina> pegarDisciplinas() {
		return disciplinaRepo.findAll();
	}
	
	public void deletarDisciplina(Disciplina disciplina) {
		disciplinaRepo.delete(disciplina);
	}
	
}
