package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Horario;
import com.tcc.qbeacon.repository.HorarioRepository;

@Service
public class HorarioService {
	
	@Autowired
	HorarioRepository horarioRepo;
	
	public Horario salvarHorario(Horario horario) {
		return horarioRepo.save(horario);
	}
	
	public Horario buscarHorario(Integer id) {
		return horarioRepo.findOne(id);
	}
	
	public List<Horario> pegarHorarios() {
		return horarioRepo.findAll();
	}
	
	public void deletarHorario(Horario horario) {
		horarioRepo.delete(horario);
	}
	
	public Horario buscarHorario(Horario horario) {
		return horarioRepo.igual(horario.getDiaSemana(), horario.getPeriodo());
	}

}
