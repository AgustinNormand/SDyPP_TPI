package com.example.splitter;

import com.example.splitter.dto.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.UUID;

@SpringBootApplication
public class SplitterApplication implements CommandLineRunner {

	@Autowired
	StreamBridge bridge;

	Logger logger = LoggerFactory.getLogger(SplitterApplication.class);

	@Autowired
	RedisHandler redisHandler;

	public static void main(String[] args) {
		SpringApplication.run(SplitterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			logger.info("Preparing to send tasks...");

			UUID uuid = UUID.randomUUID();
			redisHandler.save(uuid.toString(), "total-tasks", 3);
			redisHandler.save(uuid.toString(), "done-tasks", 0);


			Task task1 = new Task(uuid.toString(), 1, 50);
			Task task2 = new Task(uuid.toString(), 2, 50);
			Task task3 = new Task(uuid.toString(), 3, 50);

			bridge.send("output", task1);
			bridge.send("output", task2);
			bridge.send("output", task3);

			logger.info("Tasks sent :)");
		}
		catch (Exception e) {
			logger.error("Ups... something went wrong - {}", e.getMessage());
		}
	}
}
