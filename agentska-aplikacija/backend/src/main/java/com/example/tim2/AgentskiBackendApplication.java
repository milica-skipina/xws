package com.example.tim2;

import com.example.tim2.soap.clients.AdvertisementClient;
import com.example.tim2.soap.gen.AllAdvertisementsResponse;
import com.example.tim2.soap.gen.AllPricelistsResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgentskiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentskiBackendApplication.class, args);
	}
/*
	@Bean
	CommandLineRunner lookup(AdvertisementClient quoteClient) {
		return args -> {
			AllPricelistsResponse response = quoteClient.sendAllPricelists();
			System.out.println("STIGAO odgovor");
			System.err.println(response.isOk());

			AllAdvertisementsResponse response2 = quoteClient.sendAllAdvertisements();
			System.out.println("STIGAO odgovor");
			System.err.println(response2.isOk());
		};
	}
*/
}
