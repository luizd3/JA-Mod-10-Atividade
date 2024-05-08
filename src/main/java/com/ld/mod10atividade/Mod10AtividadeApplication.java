package com.ld.mod10atividade;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableRabbit
@EnableReactiveMongoRepositories
@SpringBootApplication
public class Mod10AtividadeApplication extends AbstractReactiveMongoConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(Mod10AtividadeApplication.class, args);
	}

	@Override
	protected String getDatabaseName() {
		return "mentorama";
	}
}
