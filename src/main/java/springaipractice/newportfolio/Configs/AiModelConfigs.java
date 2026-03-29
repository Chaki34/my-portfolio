package springaipractice.newportfolio.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiModelConfigs {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .defaultHeader("HTTP-Referer", "http://localhost:8080")
                .defaultHeader("X-Title", "Quiz AI");
    }
}