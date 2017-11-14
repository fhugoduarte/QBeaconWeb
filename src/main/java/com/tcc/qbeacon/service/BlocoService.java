package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Bloco;
import com.tcc.qbeacon.repository.BlocoRepository;

@Service
public class BlocoService {
	
	@Autowired
	BlocoRepository blocoRepo;
	
	public Bloco salvarBloco (Bloco bloco) {
		return blocoRepo.save(bloco);
	}
	
	public Bloco buscarBloco (Integer id) {
		return blocoRepo.findOne(id);
	}
	
	public List<Bloco> pegarBlocos () {
		return blocoRepo.findAll();
	}
	
	public void deletarBloco (Bloco bloco) {
		blocoRepo.delete(bloco);
	}
	
	public List<Bloco> pegarBlocosValidos () {
		return blocoRepo.blocosValidos();
	}
}
