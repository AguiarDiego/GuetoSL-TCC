package guetosl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Indica ao Spring que esta classe contém configurações da aplicação.
@Configuration
public class CorsConfig {

    // Registra as regras globais de CORS no Spring MVC.
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // Permite que o React acesse os endpoints da nossa API.
                registry.addMapping("/**")

                        // Portas usadas pelo Vite durante o desenvolvimento.
                        .allowedOrigins(
                                "http://localhost:5173",
                                "http://localhost:5174"
                        )

                        // Métodos HTTP utilizados pelo CRUD.
                        .allowedMethods(
                                "GET",
                                "POST",
                                "PUT",
                                "DELETE",
                                "OPTIONS"
                        )

                        // Permite os cabeçalhos das requisições.
                        .allowedHeaders("*");
            }
        };
    }
}