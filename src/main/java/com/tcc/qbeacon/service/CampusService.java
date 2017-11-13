package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Campus;
import com.tcc.qbeacon.repository.CampusRepository;

@Service
public class CampusService {

	@Autowired
	private CampusRepository campusRepo;
	
	public Campus salvarCampus (Campus campus) {
		return campusRepo.save(campus);
	}
	
	public Campus buscarCampus (Integer id) {
		return campusRepo.findOne(id);
	}
	
	public List<Campus> pegarTodosCampus () {
		return campusRepo.findAll();
	}
	
	public void deletarCampus (Campus campus) {
		campusRepo.delete(campus);
	}
	
	public List<Campus> pegarCampusValidos () {
		return campusRepo.campusValidos();
	}
	
}
