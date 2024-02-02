package paypaldemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Service
@Slf4j
public class PaypalService {
    @Value("${application.paypal.paypal-client-id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${application.paypal.paypal-client-secret}")
    private String PAYPAL_CLIENT_SECRET;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${application.paypal.url}")
    private String basePaypalUrl;

    private Mono<String> generateAccessToken() {
        if (PAYPAL_CLIENT_ID == null || PAYPAL_CLIENT_SECRET == null) {
            throw new RuntimeException("Missing api credentials");
        }
        String basicAuth = Base64.getEncoder().encodeToString((PAYPAL_CLIENT_ID + ":" + PAYPAL_CLIENT_SECRET).getBytes());
        var client = WebClient.create();
        return client.post()
                .uri(basePaypalUrl + "/v1/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        var node = objectMapper.readTree(response);
                        log.info(response);
                        return Mono.just(node.get("access_token").textValue());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Mono<String> createOrder(PaypalOrder order) {
        try {
            log.info(objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return generateAccessToken()
                .flatMap(accessToken ->
                        {
                            try {
                                return WebClient
                                        .create()
                                        .post()
                                        .uri(basePaypalUrl + "/v2/checkout/orders")
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                                        .bodyValue(objectMapper.writeValueAsString(order))
                                        .retrieve()
                                        .onStatus(HttpStatusCode::is4xxClientError,clientResponse -> {
                                            clientResponse.bodyToMono(String.class).subscribe(log::info);
                                            return Mono.error(new RuntimeException());
                                        })
                                        .bodyToMono(String.class)
                                        ;
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    public Mono<String> captureOrder(String orderId) {
        return generateAccessToken()
                .flatMap(accessToken -> {
                    return WebClient.create().post()
                            .uri(basePaypalUrl + "/v2/checkout/orders/" + orderId + "/capture")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .header(HttpHeaders.CONTENT_TYPE, "application/json")
                            .retrieve()
                            .bodyToMono(String.class);
                });

    }
}
