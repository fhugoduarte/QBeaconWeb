package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Horario;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.repository.SalaRepository;

@Service
public class SalaService {
	
	@Autowired
	SalaRepository salaRepo;

	public Sala salvarSala (Sala sala) {
		return salaRepo.save(sala);
	}
	
	public Sala buscarSala (Integer id) {
		return salaRepo.findOne(id);
	}
	
	public List<Sala> pegarSalas () {
		return salaRepo.findAll();
	}
	
	public void deletarSala (Sala sala) {
		salaRepo.delete(sala);
	}
	
	public List<Sala> pegarSalasValidas() {
		return salaRepo.salasValidas();
	}
	
	public List<Sala> pegarSalasVagas(Horario horario) {
		return salaRepo.salasVagas(horario.getDiaSemana(), horario.getPeriodo());
	}
	
}
