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
	
	//Pega todas as salas que não possuem beacon.
	public List<Sala> pegarSalasValidas() {
		return salaRepo.salasValidas();
	}
	
	//Pega todas as salas que tem horário vago no dia da semana e no periodo passado como parametro.
	public List<Sala> pegarSalasVagas(Horario horario) {
		return salaRepo.salasVagas(horario.getDiaSemana(), horario.getPeriodo());
	}
	
	//Pega todas as salas que tem horário vago no dia da semana e no periodo passado como parametro.
	public List<Sala> pegarSalasVagas(String periodo, String diaSemana) {
		return salaRepo.salasVagas(diaSemana, periodo);
	}
	
	//Retorna todas as salas que estão agendadas nesse horário.
	public List<Sala> salasComAula(String hora, String diaSemana) {
		return salaRepo.salasComAula(hora, diaSemana);
	}
	
	//Retorna todas as salas que estão agendadas nesse dia da semana.
	public List<Sala> salasComAula(String diaSemana){
		return salaRepo.salasComAula(diaSemana);
	}
	
}
