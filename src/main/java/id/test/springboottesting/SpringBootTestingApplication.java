package id.test.springboottesting;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestingApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
	 return new ModelMapper();
	}

}
