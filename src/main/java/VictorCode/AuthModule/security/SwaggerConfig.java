package VictorCode.AuthModule.security;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AuthModule API")
                        .version("v1.0")
                        .description("API de autenticação com registro, login e recuperação de senha.")
                        .contact(new Contact()
                                .name("Victor Gabriel")
                                .email("victor.git24@gmail.com")
                                .url("https://github.com/VictorGabrielQS"))
                        .license(new License()
                                .name("GitHub Projeto")
                                .url("https://github.com/VictorGabrielQS/AuthModule.git"))
                );
    }
}
