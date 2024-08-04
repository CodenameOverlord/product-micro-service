package com.ver.inventory_service;

import com.ver.inventory_service.model.Inventory;
import com.ver.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	/* commented so that repeated data is not there
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args-> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setQuantity(0);
			inventory1.setSkuCode("iphone_13_red");
			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory);
		};
	}

	 */
}
