package com.tcc.qbeacon;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
//Habilita a criação de métodos assíncronos
@EnableAsync
public class QBeaconWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(QBeaconWebApplication.class, args);
	}
	
	//Configuração ThreadPool para a thread principal, deixa apenas que ela seja executada apenas uma vez.
	@Bean(name = "principal")
    public Executor asyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();
        return executor;
    }

}
