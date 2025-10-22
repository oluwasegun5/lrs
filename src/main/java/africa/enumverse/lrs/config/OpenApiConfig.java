package africa.enumverse.lrs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI lrsOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8088");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Enumverse Africa");
        contact.setEmail("support@enumverse.africa");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Learning Record Store (LRS) API")
                .version("1.0.0")
                .description("A comprehensive API for managing learning records and xAPI statements. " +
                        "This Learning Record Store allows you to track, store, and analyze learning activities " +
                        "following the xAPI (Experience API) specification.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}

