package dbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "dbms/model")
public class DbmsLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbmsLabApplication.class, args);
	}

}
