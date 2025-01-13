package ma.youcode.api;

import freemarker.core.Environment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlcortiApp {


	public static void main(String[] args) {
		SpringApplication.run(AlcortiApp.class, args);
	}

}
