package seungkyu.route.gateway.route;

import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import seungkyu.route.RouteApplication;

import java.io.IOException;

@ActiveProfiles("gatewayfilter-response-header")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
public class ResponseHeaderGatewayFilterTest {

    @Autowired
    private WebTestClient webClient;

    private MockWebServer mockWebServer;

    @SneakyThrows
    @BeforeEach
    void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start(8001);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @SneakyThrows
    @Test
    void addResponseHeader(){
        mockWebServer.enqueue(new MockResponse());

        webClient.get()
                .uri("/add")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .valueEquals("X-Test", "hola");
    }

    @SneakyThrows
    @Test
    void addResponseHeaderIfExists(){
        mockWebServer.enqueue(new MockResponse().setHeader("X-Test", "hi"));

        webClient.get()
                .uri("/add")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .valueEquals("X-Test", "hi", "hola");
    }

    @SneakyThrows
    @Test
    void setResponseHeader(){
        mockWebServer.enqueue(new MockResponse().setHeader("X-Test", "hi"));

        webClient.get()
                .uri("/set")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .valueEquals("X-Test", "hola");
    }

    @SneakyThrows
    @Test
    void rewriteResponseHeader(){
        mockWebServer.enqueue(new MockResponse().setHeader("X-Test", "abchi"));

        webClient.get()
                .uri("/rewrite")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .valueEquals("X-Test", "abchola");
    }

    @SneakyThrows
    @Test
    void removeResponseHeader(){
        mockWebServer.enqueue(new MockResponse().setHeader("X-Test", "hi"));

        webClient.get()
                .uri("/remove")
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .doesNotExist("X-Test");
    }


}
