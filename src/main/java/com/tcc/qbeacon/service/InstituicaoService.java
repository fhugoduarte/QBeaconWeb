package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Instituicao;
import com.tcc.qbeacon.repository.InstituicaoRepository;

@Service
public class InstituicaoService {

	@Autowired
	private InstituicaoRepository instituicaoRepo;
	
	public Instituicao salvarInstituicao (Instituicao instituicao) {
		return instituicaoRepo.save(instituicao);
	}
	
	public Instituicao buscarInstituicao (Integer id) {
		return instituicaoRepo.findOne(id);
	}
	
	public List<Instituicao> pegarInstituicoes () {
		return instituicaoRepo.findAll();
	}
	
	public void deletarInstituicao (Instituicao instituicao) {
		instituicaoRepo.delete(instituicao);
	}
}
