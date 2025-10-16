package transfi.web.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
@Getter
public class ExchangeRateService {
    private final RestTemplate restTemplate;

    private final Map<String, Double> rates = new HashMap<>();

    private final Set<String> CURRENCIES = Set.of("USD", "EUR", "JPY", "GBP", "NGN", "INR");

    @Value("${currencyApiKey}")
    private String apiKey;

    public void getExchangeRate() {
        String CURRENCY_URL = "https://api.currencyapi.com/v3/latest?";
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                CURRENCY_URL,
                HttpMethod.GET,
                entity,
                JsonNode.class
        );

        var data = Objects.requireNonNull(response.getBody()).get("data");

        for (var currency : CURRENCIES) {
            if (data.has(currency)) {
                rates.put(currency, data.get(currency).get("value").doubleValue());
            }
        }

        System.out.println("Rates: " + rates);
        System.out.println("Currencies: " + rates.keySet());
    }
}
