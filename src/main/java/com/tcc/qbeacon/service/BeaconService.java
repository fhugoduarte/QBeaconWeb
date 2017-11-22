package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Beacon;
import com.tcc.qbeacon.repository.BeaconRepository;

@Service
public class BeaconService {

	@Autowired
	BeaconRepository beaconRepo;
	
	public Beacon salvarBeacon (Beacon beacon) {
		return beaconRepo.save(beacon);
	}
	
	public Beacon buscarBeacon (Integer id) {
		return beaconRepo.findOne(id);
	}
	
	public List<Beacon> pegarBeacons () {
		return beaconRepo.findAll();
	}
	
	public void deletarBeacon (Beacon beacon) {
		beaconRepo.delete(beacon);
	}
	
	public List<Beacon> pegarBeaconsValidos () {
		return beaconRepo.beaconsValidos();
	}

}
