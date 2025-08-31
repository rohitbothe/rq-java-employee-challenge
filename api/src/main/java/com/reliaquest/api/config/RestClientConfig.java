package com.reliaquest.api.config;

import com.reliaquest.api.client.EmployeeRestApi;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @author Rohit Bothe
 */
@Configuration
public class RestClientConfig {

    private static final Logger log = LoggerFactory.getLogger(RestClientConfig.class);

    @Value("${employee.server.url:http://localhost:8112}")
    private String baseUrl;

    public ClientHttpRequestInterceptor logRequest() {
        return (request, body, execution) -> {
            StringBuilder sb = new StringBuilder("Request headers:");
            request.getHeaders()
                    .forEach((name, values) -> values.forEach(
                            value -> sb.append(name).append(": ").append(value).append(" ")));

            log.debug("Log request: Method: {}, URI:{}, Headers:{}", request.getMethod(), request.getURI(), sb);
            return execution.execute(request, body);
        };
    }

    public RestClient.ResponseSpec.ErrorHandler handleClientError() {
        return (request, response) -> {
            response.getBody();
            byte[] bodyBytes = response.getBody().readAllBytes();
            String errorBody = new String(bodyBytes, StandardCharsets.UTF_8);

            log.error(
                    "Error occurred while calling Method: {}, URI:{}, ResponseCode: {}, Response: {}",
                    request.getMethod(),
                    request.getURI(),
                    response.getStatusCode(),
                    errorBody);
            if (HttpStatus.TOO_MANY_REQUESTS.equals(response.getStatusCode())) {
                throw new HttpClientErrorException(response.getStatusCode(), response.getStatusText());
            }

            throw new RestClientResponseException(
                    "HTTP error: " + response.getStatusCode(),
                    response.getStatusCode().value(),
                    response.getStatusText(),
                    response.getHeaders(),
                    errorBody.getBytes(),
                    null);
        };
    }

    @Bean
    public EmployeeRestApi employeeRestApiService(RestClient.Builder builder, CircuitBreakerRegistry registry) {
        RestClient restClient = builder.baseUrl(baseUrl)
                .requestInterceptor(logRequest())
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .defaultStatusHandler(HttpStatusCode::isError, handleClientError())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(EmployeeRestApi.class);
    }
}
