package com.colab.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ScrapperMlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrapperMlApplication.class, args);
	}

	@Configuration
	@EnableScheduling
	@ConditionalOnProperty(name = "scheduling.enable", matchIfMissing = true)
	class SchedulingConfiguration{
		
	}
}
