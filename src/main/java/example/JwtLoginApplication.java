package example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The application entry point.
 *
 * @author Josh Cummings
 */
@SpringBootApplication
@MapperScan(basePackages = {"example.mapper"})
public class JwtLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtLoginApplication.class, args);
    }

}
