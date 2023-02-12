package cc.sofast.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author apple
 */
@EnableCaching
@SpringBootApplication
public class ExampleApp {

    public static void main(String[] args) {

        SpringApplication.run(ExampleApp.class, args);
    }
}
