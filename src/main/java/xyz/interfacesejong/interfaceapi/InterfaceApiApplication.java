package xyz.interfacesejong.interfaceapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@OpenAPIDefinition(servers = {@Server(url = "https://api.interfacesejong.xyz", description = "Default Server URL")})
@SpringBootApplication
@EnableJpaAuditing
public class InterfaceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterfaceApiApplication.class, args);
    }

}
