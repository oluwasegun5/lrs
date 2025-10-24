package africa.enumverse.lrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LrsApplication.class, args);
    }

}
