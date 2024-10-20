package seungkyu.route.gateway.route;

import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import seungkyu.route.RouteApplication;

@ActiveProfiles("greet-predicate")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
public class GreetPathRoutePredicateTest {

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
    @SneakyThrows
    void tearDown(){
        mockWebServer.shutdown();
    }

    @SneakyThrows
    @Test
    void helloTest(){
        //given
        var message = "Hello world";
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(message));

        webClient.get()
                .uri("/hello")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(message);

        var recordedRequest = mockWebServer.takeRequest();
        var expectedPath = "/hello";
        Assertions.assertEquals(expectedPath, recordedRequest.getPath());
    }

    @SneakyThrows
    @Test
    void holaTest(){
        //given
        var message = "Hola world";
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(message));

        webClient.get()
                .uri("/ho")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(message);


        var recordedRequest = mockWebServer.takeRequest();
        var expectedPath = "/hello";
        Assertions.assertEquals(expectedPath, recordedRequest.getPath());
    }

    @Test
    void notFound(){
        webClient.get()
                .uri("/hi")
                .exchange()
                .expectStatus()
                .isNotFound();

        Assertions.assertEquals(0, mockWebServer.getRequestCount());
    }
}
