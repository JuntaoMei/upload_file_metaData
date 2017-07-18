package juntao;

import juntao.service.iStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FileMetadataUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileMetadataUploadApplication.class, args);
	}

	@Bean
	CommandLineRunner init(iStorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
