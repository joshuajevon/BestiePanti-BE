package com.app.bestiepanti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.bestiepanti.configuration.ApplicationConfig;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BestiepantiApplication{

	@Autowired
	ApplicationConfig applicationConfig;
	public static void main(String[] args) {
		SpringApplication.run(BestiepantiApplication.class, args);
	}

	@PostConstruct
	public void logFrontEndUrl() {
		System.out.println("Resolved Front-End URL: " + applicationConfig.getUrlFrontEnd());
	}

}
