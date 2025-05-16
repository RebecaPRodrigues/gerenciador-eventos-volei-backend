package br.com.rasgamortalha.rm_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RmBackendApplication {

//	public static void main(String[] args) {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		boolean senhaConfere = encoder.matches("12345678", "$2a$10$Zq7O.5wJzgbkbpWLPBdOWuXoKpRCd2e8sly3TzXYvLgj.9pIRaEGq");
//		System.out.println("Senha confere? " + senhaConfere);
//	}


	public static void main(String[] args) {
		SpringApplication.run(RmBackendApplication.class, args);
	}

}
