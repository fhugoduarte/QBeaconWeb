package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.repository.TurmaRepository;

@Service
public class TurmaService {

	@Autowired
	TurmaRepository turmaRepo;
	
	public Turma salvarTurma (Turma turma) {
		return turmaRepo.save(turma);
	}
	
	public Turma buscarTurma (Integer id) {
		return turmaRepo.findOne(id);
	}
	
	public List<Turma> pegarTurmas () {
		return turmaRepo.findAll();
	}
	
	public void deletarTurma (Turma turma) {
		turmaRepo.delete(turma);
	}
	
	public List<Turma> pegarTurmasReservaveis () {
		return turmaRepo.turmasReservaveis();
	}
	
}
