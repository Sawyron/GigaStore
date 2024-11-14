package com.gigastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GigaStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GigaStoreApplication.class, args);
	}

}
