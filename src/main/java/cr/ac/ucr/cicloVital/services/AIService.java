package cr.ac.ucr.cicloVital.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.groq.com/openai/v1/chat/completions")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String obtenerRespuestaDesdeIA(List<Map<String, String>> historial) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3-8b-8192"); // Modelo más ligero y compatible
        requestBody.put("messages", historial);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 500);

        try {
            Map<String, Object> response = webClient.post()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class)
                            .flatMap(body -> {
                                System.err.println("Error al llamar a IA: " + body);
                                return Mono.error(new RuntimeException("Respuesta de error desde IA: " + body));
                            }))
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }

        } catch (Exception e) {
            System.err.println("Excepción al obtener respuesta IA: " + e.getMessage());
        }

        return "Lo siento, no pude generar una respuesta en este momento.";
    }
}
