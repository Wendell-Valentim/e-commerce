package com.io.github.wendellvalentim.msuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsuserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsuserApplication.class, args);
	}

}
