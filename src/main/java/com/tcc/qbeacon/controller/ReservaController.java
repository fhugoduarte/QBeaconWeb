package com.tcc.qbeacon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Reserva;
import com.tcc.qbeacon.service.ReservaService;

@Controller
@RequestMapping(path="/reserva")
public class ReservaController {
	
	@Autowired
	ReservaService reservaService;
	
	@GetMapping(path="/listar_reservas")
	public ModelAndView listaReservas() {
		ModelAndView model = new ModelAndView("reservar/listaReservas");
		List<Reserva> reservas = reservaService.pegarReservas();
		model.addObject("reservas", reservas);
		return model;
	}

}
