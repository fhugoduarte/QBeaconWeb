package com.tcc.qbeacon.controller;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import com.tcc.qbeacon.model.Beacon;
import com.tcc.qbeacon.model.Sala;
import com.tcc.qbeacon.service.AulaService;
import com.tcc.qbeacon.service.BeaconService;
import com.tcc.qbeacon.service.ReservaService;
import com.tcc.qbeacon.service.SalaService;
import com.tcc.qbeacon.service.TurmaService;
import com.tcc.qbeacon.util.Constants;

@Controller
public class ThreadController {

	@Autowired
	SalaService salaService;
	
	@Autowired
	AulaService aulaService;
	
	@Autowired
	BeaconService beaconService;
	
	@Autowired
	TurmaService turmaService;
	
	@Autowired
	ReservaService reservaService;
	
	@Async("principal")
	public void inicializar() throws IOException, InterruptedException{
		while(true) {
			Calendar calendar = new GregorianCalendar();
			 Date trialTime = new Date();
			 calendar.setTime(trialTime);
			 
			 String diaSemana = this.traduzDiaSemana(calendar.get(Calendar.DAY_OF_WEEK));
			 int hora = calendar.get(Calendar.HOUR_OF_DAY);
			 int minutos = calendar.get(Calendar.MINUTE);
			 if(!diaSemana.equals("")) {
				 this.verificaHorario(hora, minutos, diaSemana);
			 }
			//Sleep por 5min
			Thread.sleep(300000);
		}
	}
	
	private String traduzDiaSemana(int diaSemana) {
		switch (diaSemana) {
		case 2:
			return "Segunda-Feira";
		case 3:
			return "Terça-Feira";
		case 4:
			return "Quarta-Feira";
		case 5:
			return "Quinta-Feira";
		case 6:
			return "Sexta-Feira";
		default:
			return "";
		}
	}
	
	private void verificaHorario(int hora, int minutos, String diaSemana) {
		if(hora == 7) {
			if(minutos >= 40 && minutos <= 50) {
				List<Sala> salasComAula = salaService.salasComAula("08:00 às 10:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				List<Sala> salasSemAula = salaService.pegarSalasVagas("08:00 às 10:00", diaSemana);
				for(Sala sala: salasSemAula) {
					try {
						this.desligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora ==  8) {
			if(minutos >= 15 && minutos <= 25) {
				List<Sala> salasComAula = salaService.salasComAula("08:00 às 10:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 25 && minutos <= 36) {
				List<Sala> salasComAula = salaService.salasComAula("08:00 às 10:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 9) {
			if(minutos >= 40 && minutos <= 50) {
				List<Sala> salasComAula = salaService.salasComAula("10:00 às 12:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 10) {
			if(minutos >= 15 && minutos <= 25) {
				List<Sala> salasComAula = salaService.salasComAula("10:00 às 12:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				List<Sala> salasSemAula = salaService.pegarSalasVagas("10:00 às 12:00", diaSemana);
				for (Sala sala : salasSemAula) {
					try {
						this.desligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 26 && minutos <= 36) {
				List<Sala> salasComAula = salaService.salasComAula("10:00 às 12:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 12) {
			if(minutos >= 15 && minutos <= 25) {
				List<Sala> salasComAula = salaService.salasComAula("10:00 às 12:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 13) {
			if(minutos >= 10 && minutos <= 20) {
				List<Sala> salasComAula = salaService.salasComAula("13:30 às 15:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 45 && minutos <= 55) {
				List<Sala> salasComAula = salaService.salasComAula("13:30 às 15:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 56) {
				List<Sala> salasComAula = salaService.salasComAula("13:30 às 15:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 14) {
			if(minutos <= 6) {
				List<Sala> salasComAula = salaService.salasComAula("13:30 às 15:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 15) {
			if(minutos >= 10 && minutos <= 20) {
				List<Sala> salasComAula = salaService.salasComAula("15:30 às 17:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				List<Sala> salasSemAula = salaService.pegarSalasVagas("15:30 às 17:30", diaSemana);
				for (Sala sala : salasSemAula) {
					try {
						this.desligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 45 && minutos <= 55) {
				List<Sala> salasComAula = salaService.salasComAula("15:30 às 17:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 56) {
				List<Sala> salasComAula = salaService.salasComAula("15:30 às 17:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 16) {
			if(minutos <= 6) {
				List<Sala> salasComAula = salaService.salasComAula("15:30 às 17:30", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 17) {
			if(minutos >= 40 && minutos <= 50) {
				List<Sala> salasComAula = salaService.salasComAula("18:00 às 20:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				List<Sala> salasSemAula = salaService.pegarSalasVagas("18:00 às 20:00", diaSemana);
				for (Sala sala : salasSemAula) {
					try {
						this.desligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 18) {
			if(minutos >= 15 && minutos <= 25) {
				List<Sala> salasComAula = salaService.salasComAula("18:00 às 20:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 26 && minutos <= 36) {
				List<Sala> salasComAula = salaService.salasComAula("18:00 às 20:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 19) {
			if(minutos >= 40 && minutos <= 50) {
				List<Sala> salasComAula = salaService.salasComAula("20:00 às 22:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 20) {
			if(minutos >= 15 && minutos <= 25) {
				List<Sala> salasComAula = salaService.salasComAula("20:00 às 22:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.ligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				List<Sala> salasSemAula = salaService.pegarSalasVagas("20:00 às 22:00", diaSemana);
				for (Sala sala : salasSemAula) {
					try {
						this.desligaEnergia(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else if(minutos >= 26 && minutos <= 36) {
				List<Sala> salasComAula = salaService.salasComAula("20:00 às 22:00", diaSemana);
				for (Sala sala : salasComAula) {
					try {
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else if(hora == 22) {
			if(minutos >= 15) {
				System.err.println("TESTE");
				List<Sala> salas = salaService.pegarSalas();
				for (Sala sala : salas) {
					try {
						System.err.println("DESLIGOU A LUZ E OS BEACON DA SALA: " + sala.getNome());
						this.desligaEnergia(sala);
						this.desligaBeacon(sala);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else {
			System.err.println("NENHUMA HORA");
		}
	}

	private void ligaEnergia(Sala sala) throws MqttException {
		
		if(!sala.isEnergia()) {
			String mqttTopico = (sala.getBloco().getCampus().getInstituicao().getNome() + "/" 
					+ sala.getBloco().getCampus().getNome() + "/"
					+ sala.getBloco().getNome() + "/"
					+ sala.getNome()).toUpperCase();
			
			//Remove acentos, caracteres especiais e espaços do tópico.
			mqttTopico = Normalizer.normalize(mqttTopico, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
					.replaceAll(" ", "");
			
			MqttClient client = new MqttClient(Constants.URI_MQTT, MqttClient.generateClientId());
		    client.connect();
		    MqttMessage message = new MqttMessage();
		    message.setPayload("1".getBytes());
		    client.publish(mqttTopico, message);
		}
	}
	
	private void desligaEnergia(Sala sala) throws MqttException {
		if(sala.isEnergia()) {
			String mqttTopico = (sala.getBloco().getCampus().getInstituicao().getNome() + "/" 
					+ sala.getBloco().getCampus().getNome() + "/"
					+ sala.getBloco().getNome() + "/"
					+ sala.getNome()).toUpperCase();
			
			//Remove acentos, caracteres especiais e espaços do tópico.
			mqttTopico = Normalizer.normalize(mqttTopico, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
					.replaceAll(" ", "");
			
			MqttClient client = new MqttClient(Constants.URI_MQTT, MqttClient.generateClientId());
		    client.connect();
		    MqttMessage message = new MqttMessage();
		    message.setPayload("0".getBytes());
		    client.publish(mqttTopico, message);
		}
	}
	
	//Para ativar a presença no beacon ele manda para o tópico referente ao beacon 8bits com o primeiro bit = 1.
	private void ligaBeacon(Sala sala) throws MqttException {
		
		if(sala.getBeacon() != null) {
			Beacon beacon = sala.getBeacon();
			if(!beacon.isAtivado()) {
				String mqttTopico = beacon.getNome().toUpperCase();
				mqttTopico = Normalizer.normalize(mqttTopico, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
						.replaceAll(" ", "");
				
				String mensagem = sala.getId().toString();
				String zeros = "0000000";

				mensagem = "1" + zeros.substring(0, 7 - mensagem.length()) + mensagem;
				
				MqttClient client = new MqttClient(Constants.URI_MQTT, MqttClient.generateClientId());
			    client.connect();
			    MqttMessage message = new MqttMessage();
			    message.setPayload(mensagem.getBytes());
			    client.publish(mqttTopico, message);
			    
			    beacon.setAtivado(true);
			    beaconService.salvarBeacon(beacon);
			}
		}

	}
	
	//Para ativar a presença no beacon ele manda para o tópico referente ao beacon 8bits com o primeiro bit = 0.
	private void desligaBeacon(Sala sala) throws MqttException {
		
		if(sala.getBeacon() != null) {
			Beacon beacon = sala.getBeacon();
			if(!beacon.isAtivado()) {
				String mqttTopico = beacon.getNome().toUpperCase();
				
				String mensagem = sala.getId().toString();
				String zeros = "0000000";

				mensagem = "0" + zeros.substring(0, 7 - mensagem.length()) + mensagem;
				
				MqttClient client = new MqttClient(Constants.URI_MQTT, MqttClient.generateClientId());
			    client.connect();
			    MqttMessage message = new MqttMessage();
			    message.setPayload(mensagem.getBytes());
			    client.publish(mqttTopico, message);
			    
			    beacon.setAtivado(true);
			    beaconService.salvarBeacon(beacon);
			}
		}
		
	}

}
