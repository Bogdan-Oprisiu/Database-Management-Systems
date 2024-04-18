package org.example.computer_store_backend_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.example.computer_store_backend_1.entity"})
public class ComputerStoreBackEnd1Application {

	public static void main(String[] args) {
		SpringApplication.run(ComputerStoreBackEnd1Application.class, args);
	}
}