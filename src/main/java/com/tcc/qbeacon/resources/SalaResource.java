package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.datas.SalaData;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.SalaService;

@RestController
@RequestMapping("/api/sala")
public class SalaResource {
	
	@Autowired
	SalaService salaService;
	
	@GetMapping
	public ResponseEntity<List<SalaData>> listaSalas(){
		List<Sala> salasBanco = salaService.pegarSalas();
		List<SalaData> salas = new ArrayList<>();
		
		for (Sala sala : salasBanco) {
			if(sala.getBeacon() != null) {
				salas.add(new SalaData(sala.getId(), sala.getNome(), 
					sala.getBloco().getNome(), 
					sala.getBloco().getCampus().getNome(),
					sala.getBloco().getCampus().getInstituicao().getNome(),
					sala.getBeacon().getId()));
			}else {
				salas.add(new SalaData(sala.getId(), sala.getNome(), 
						sala.getBloco().getNome(), 
						sala.getBloco().getCampus().getNome(),
						sala.getBloco().getCampus().getInstituicao().getNome(),
						null));
			}

		}
		
		return new ResponseEntity<List<SalaData>>(salas, HttpStatus.OK);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<SalaData> buscaSala(@PathVariable("id") Integer id){
		Sala salaBanco = salaService.buscarSala(id);		
		
		if(salaBanco.getBeacon() != null) {
			SalaData sala = new SalaData(salaBanco.getId(), salaBanco.getNome(), 
				salaBanco.getBloco().getNome(), 
				salaBanco.getBloco().getCampus().getNome(),
				salaBanco.getBloco().getCampus().getInstituicao().getNome(),
				salaBanco.getBeacon().getId());
			
			return new ResponseEntity<SalaData>(sala, HttpStatus.OK);
			
		}else {
			SalaData sala = new SalaData(salaBanco.getId(), salaBanco.getNome(), 
					salaBanco.getBloco().getNome(), 
					salaBanco.getBloco().getCampus().getNome(),
					salaBanco.getBloco().getCampus().getInstituicao().getNome(),
					null);
			return new ResponseEntity<SalaData>(sala, HttpStatus.OK);
		}
	
	}

}
