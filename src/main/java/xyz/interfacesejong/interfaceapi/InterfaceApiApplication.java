package xyz.interfacesejong.interfaceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InterfaceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterfaceApiApplication.class, args);
    }

}
