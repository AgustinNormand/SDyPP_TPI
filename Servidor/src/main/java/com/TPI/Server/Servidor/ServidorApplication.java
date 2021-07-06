package com.TPI.Server.Servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@RestController
public class ServidorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServidorApplication.class, args);
	}

	@GetMapping("/")
	public String test(){
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("kubectl get nodes");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getResults(process);
	}

	public String getResults(Process process){
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String result = "";
		if(process != null)
			while (true) {
				String line = "";
				try {
					line = reader.readLine();
					if (line == null)
						break;
					else
						result = result + line + "\r\n";
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return result;
	}
}
