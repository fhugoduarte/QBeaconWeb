package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.datas.InstituicaoData;
import com.tcc.qbeacon.model.Instituicao;
import com.tcc.qbeacon.service.InstituicaoService;

@RestController
@RequestMapping("/api/instituicao")
public class InstituicaoResource {
	
	@Autowired
	InstituicaoService instituicaoService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<List<InstituicaoData>> listarInstituicao(){
		List<Instituicao> instituicoes = instituicaoService.pegarInstituicoes();
		List<InstituicaoData>instituicoesData = new ArrayList<>();
		
		for (Instituicao instituicao : instituicoes) {
			instituicoesData.add(new InstituicaoData(instituicao.getId(), instituicao.getNome()));
		}
		
		return new ResponseEntity<List<InstituicaoData>>(instituicoesData, HttpStatus.OK);
	}
	
}
