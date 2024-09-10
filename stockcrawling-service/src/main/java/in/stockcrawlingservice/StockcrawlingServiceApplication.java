package in.stockcrawlingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockcrawlingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockcrawlingServiceApplication.class, args);
	}

}
