package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Aula;
import com.tcc.qbeacon.repository.AulaRepository;

@Service
public class AulaService {

	@Autowired
	AulaRepository aulaRepo;
	
	public Aula salvarAula (Aula aula) {
		return aulaRepo.save(aula);
	}
	
	public Aula buscarAula (Integer id) {
		return aulaRepo.findOne(id);
	}
	
	public List<Aula> pegarAulas () {
		return aulaRepo.findAll();
	}
	
	public void deletarAula (Aula aula) {
		aulaRepo.delete(aula);
	}
	
}
