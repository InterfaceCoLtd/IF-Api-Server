package xyz.interfacesejong.interfaceapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("세종대 인터페이스 API 정보")
                .version("1.0.0")
                .description("세종대 인터페이스 API 정보");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
