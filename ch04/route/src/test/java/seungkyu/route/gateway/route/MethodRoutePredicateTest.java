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

import java.io.IOException;

@ActiveProfiles("method-predicate")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
public class MethodRoutePredicateTest {


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
    void test(){
        var message = "Hello world";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(message));

        webClient.get()
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(message);


        Assertions.assertEquals(1, mockWebServer.getRequestCount());
    }

    @SneakyThrows
    @Test
    void test2(){
        var message = "Hello world";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(message));

        webClient.put()
                .exchange()
                .expectStatus().isNotFound();


        Assertions.assertEquals(0, mockWebServer.getRequestCount());
    }
}
