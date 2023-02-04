package com.workmotion.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class WorkMotionTaskApp {
	public static void main(String[] args) {
		SpringApplication.run(WorkMotionTaskApp.class, args);
	}

}
