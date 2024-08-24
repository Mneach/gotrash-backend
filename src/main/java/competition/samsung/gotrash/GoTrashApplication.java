package competition.samsung.gotrash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class GoTrashApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoTrashApplication.class, args);
	}

}
