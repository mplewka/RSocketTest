package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@SpringBootApplication
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

}

@Controller
class GreetingsRSocketController {
	@MessageMapping("greet")
	GreetingsResponse greet( GreetingsRequest request) {
		return new GreetingsResponse(request.getName());
	}
}

class GreetingsRequest {

	private String name;

	public GreetingsRequest(String name) {
		this.name = name;
	}

	public GreetingsRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class GreetingsResponse {

	private String greeting;

	GreetingsResponse(String name) {
		this.greeting = "Hello " + name + " @ " + Instant.now();
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
}