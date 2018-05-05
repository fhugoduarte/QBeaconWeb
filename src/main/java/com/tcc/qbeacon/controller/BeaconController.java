package com.tcc.qbeacon.controller;

import java.util.List;

import javax.validation.Valid;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tcc.qbeacon.model.Beacon;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.BeaconService;
import com.tcc.qbeacon.service.SalaService;
import com.tcc.qbeacon.util.Constants;

@Controller
@RequestMapping(path="/beacon")
public class BeaconController {

	@Autowired
	BeaconService beaconService;
	
	@Autowired
	SalaService salaService;
	
	@GetMapping(path="/listar_beacons")
	public ModelAndView listaBeacons() {
		ModelAndView model = new ModelAndView("beacon/listaBeacons");		
		List<Beacon> beacons = beaconService.pegarBeacons();
		model.addObject("beacons", beacons);
		return model;
	}
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrarBeacon() {
		List<Sala> salas = salaService.pegarSalasValidas();
		
		ModelAndView model = new ModelAndView("beacon/formCadastrarBeacon");
		model.addObject("beacon", new Beacon());
		model.addObject("salas", salas);
		return model;
	}
	
	@PostMapping("/cadastrar")
	public String salvarBeacon(@Valid Beacon beacon, BindingResult result ) {
		if (result.hasErrors()) return "redirect:/beacon/cadastrar";
		beacon.setAtivado(false);
		beacon.setPresenca(false);
		Beacon beaconSalvo = beaconService.salvarBeacon(beacon);
		
		//Se o beacon cadastrado tiver uma sala ele adiciona o beacon a sala e atualiza a sala.
		if(beaconSalvo.getSala() != null){
			Sala sala = beaconSalvo.getSala();
			sala.setBeacon(beaconSalvo);
			salaService.salvarSala(sala);
		}
		
		
		return "redirect:/beacon/" + beaconSalvo.getId();
	}
	
	@GetMapping("/deletar/{id}")
	public String deletarBeacon(@PathVariable("id") Integer id) throws MqttException {
		Beacon beacon = beaconService.buscarBeacon(id);
		
		//Se o beacon que vai ser apagado tem uma sala, ele tira o beacon da sala e atualiza a sala.
		if(beacon.getSala() != null){
			Sala sala = beacon.getSala();
			sala.setBeacon(null);
			salaService.salvarSala(sala);
		}
		
		this.publicar("00000000", beacon.getNome().toUpperCase());
		
		beaconService.deletarBeacon(beacon);
		return "redirect:/beacon/listar_beacons";
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		List<Sala> salas = salaService.pegarSalasValidas();
		
		ModelAndView model = new ModelAndView("beacon/formEditarBeacon");
		model.addObject("beacon", beacon);
		model.addObject("salas", salas);
		
		return model;
	}
	
	@PostMapping("/editar")
	public String editarBeacon(@Valid Beacon beacon, BindingResult result) {	
		Beacon velho = beaconService.buscarBeacon(beacon.getId());
		Sala salaVelha = velho.getSala();
		
		Beacon beaconSalvo = beaconService.salvarBeacon(beacon);
		
		//Verifica se a sala do beacon foi alterada, caso sim, chama a função que altera a sala.
		if(salaVelha != beaconSalvo.getSala())			
			this.alterarSala(salaVelha, beaconSalvo.getSala(), beaconSalvo);
		
		return "redirect:/beacon/" +  beacon.getId();
	}
	
	@GetMapping("/{id}")
	public ModelAndView visualizarBeacon(@PathVariable("id") Integer id) {
		Beacon beacon = beaconService.buscarBeacon(id);
		List<Sala> salas = salaService.pegarSalasValidas();
		
		ModelAndView model = new ModelAndView("beacon/detalhesBeacon");
		model.addObject("beacon", beacon);
		model.addObject("salas", salas);
		
		return model;
	}
	
	//Função adiciona uma determinada sala ao beacon.
	@GetMapping("/{id_beacon}/adicionar_sala/{id_sala}")
	public String adicionarSala(@PathVariable("id_beacon") Integer id_beacon, @PathVariable("id_sala") Integer id_sala) {
		Sala sala = salaService.buscarSala(id_sala);
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		
		sala.setBeacon(beacon);
		salaService.salvarSala(sala);
		
		beacon.setSala(sala);
		beaconService.salvarBeacon(beacon);
			
		return "redirect:/beacon/"+ beacon.getId();
	}
	
	//Função ativa o beacon, enviando o ID do beacon para o arduino através do MQTT.
	@GetMapping("/ativar/{id_beacon}")
	public String ativarBeacon(@PathVariable("id_beacon") Integer id_beacon) {
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		String mensagem = beacon.getSala().getId().toString();

		String zeros = "00000000";

		mensagem = zeros.substring(0, 8 - mensagem.length()) + mensagem;
		
		try {			
			this.publicar(mensagem, beacon.getNome().toUpperCase());
			
			beacon.setAtivado(true);
			beaconService.salvarBeacon(beacon);
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		return "redirect:/beacon/"+ beacon.getId();
	}
	
	//Função desativa o beacon, enviando o ID 90000000 padrão para o arduino através do MQTT.
	@GetMapping("/desativar/{id_beacon}")
	public String desativarBeacon(@PathVariable("id_beacon") Integer id_beacon) {
		Beacon beacon = beaconService.buscarBeacon(id_beacon);
		
		try {
			this.publicar("90000000", beacon.getNome().toUpperCase());
			
			beacon.setAtivado(false);
			beaconService.salvarBeacon(beacon);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "redirect:/beacon/"+ beacon.getId();
	}
	
	//Função remove uma sala do beacon.
		@GetMapping("/{id_beacon}/remover_sala/{id_sala}")
		public String removerSala(@PathVariable("id_beacon") Integer id_beacon, @PathVariable("id_sala") Integer id_sala) {
			Sala sala = salaService.buscarSala(id_sala);
			Beacon beacon = beaconService.buscarBeacon(id_beacon);
			
			sala.setBeacon(null);
			salaService.salvarSala(sala);
			
			beacon.setSala(null);
			beaconService.salvarBeacon(beacon);
				
			return "redirect:/beacon/"+ beacon.getId();
		}
	
	//Remove o beacon da sala antiga e adiciona na nova sala e atualiza as duas.
	public void alterarSala (Sala antiga, Sala nova, Beacon beacon) {
		
		if(antiga != null) {
			antiga.setBeacon(null);
			salaService.salvarSala(antiga);
		}
		
		if(nova != null) {
			nova.setBeacon(beacon);
			salaService.salvarSala(nova);
		}
		
	}
	
	//Envia via MQTT uma mensagem para o arduino.
	public void publicar(String mensagem, String topico) throws MqttException {
		MqttClient client = new MqttClient(Constants.URI_MQTT, MqttClient.generateClientId());
	    client.connect();
	    MqttMessage message = new MqttMessage();
	    message.setPayload(mensagem.getBytes());
	    client.publish(topico, message);
	}

}
