package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.repository.ReservaRepository;

@Service
public class ReservaService {

	@Autowired
	private ReservaRepository reservaRepo;
	
	public Reserva salvarReserva (Reserva reserva) {
		return reservaRepo.save(reserva);
	}
	
	public Reserva buscarReserva (Integer id) {
		return reservaRepo.findOne(id);
	}
	
	public List<Reserva> pegarReservas () {
		return reservaRepo.findAll();
	}
	
	public void deletarReserva (Reserva reserva) {
		reservaRepo.delete(reserva);
	}
	
}
