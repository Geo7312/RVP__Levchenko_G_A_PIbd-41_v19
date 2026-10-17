package ru.ulstu.library.gateway;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayRoutingTest {

    private static final HttpServer mainBackend = backend("/api/books", 201, "{\"id\":7}");
    private static final HttpServer reportBackend = backend("/api/reports/books", 200, "[{\"id\":7}]");

    @Value("${local.server.port}")
    private int gatewayPort;

    @DynamicPropertySource
    static void routes(DynamicPropertyRegistry registry) {
        registry.add("LIBRARY_SERVICE_URL", () -> "http://localhost:" + mainBackend.getAddress().getPort());
        registry.add("REPORT_SERVICE_URL", () -> "http://localhost:" + reportBackend.getAddress().getPort());
    }

    @AfterAll
    static void stopBackends() {
        mainBackend.stop(0);
        reportBackend.stop(0);
    }

    @Test
    void forwardsBookPostAndResponse() throws Exception {
        String payload = "{\"title\":\"Test\"}";
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + gatewayPort + "/api/books"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("{\"id\":7}", response.body());
    }

    @Test
    void forwardsReportRequest() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + gatewayPort + "/api/reports/books"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[{\"id\":7}]", response.body());
    }

    private static HttpServer backend(String path, int status, String responseBody) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            server.createContext(path, exchange -> {
                byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);
                exchange.getRequestBody().readAllBytes();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(status, body.length);
                try (var output = exchange.getResponseBody()) {
                    output.write(body);
                }
            });
            server.start();
            return server;
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot start test backend", ex);
        }
    }
}
