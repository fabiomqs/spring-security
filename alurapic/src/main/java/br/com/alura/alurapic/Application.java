package br.com.alura.alurapic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

import static br.com.alura.alurapic.util.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		new File(USER_FOLDER).mkdirs();
	}

}
