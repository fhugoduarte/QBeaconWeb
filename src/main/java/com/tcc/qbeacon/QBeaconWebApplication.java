package com.tcc.qbeacon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tcc.qbeacon.service.ThreadService;

@SpringBootApplication
public class QBeaconWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(QBeaconWebApplication.class, args);
	}
}
