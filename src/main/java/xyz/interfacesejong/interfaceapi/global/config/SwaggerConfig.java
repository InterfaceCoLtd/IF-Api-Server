package xyz.interfacesejong.interfaceapi.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("세종대 인터페이스 API 정보")
                .version("1.0.0")
                .description("세종대 인터페이스 API 정보");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-AUTH-TOKEN");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");


        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("JWT", securityScheme))
                .security(Collections.singletonList(securityRequirement));
    }

}
