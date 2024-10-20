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
import java.util.List;

@ActiveProfiles("gatewayfilter-request-header")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
public class RequestHeaderGatewayFilterTest {


    @Autowired
    private WebTestClient webClient;

    private MockWebServer mockWebServer;
    @Autowired
    private WebTestClient webTestClient;

    @SneakyThrows
    @BeforeEach
    void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start(8001);
        mockWebServer.enqueue(new MockResponse());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @SneakyThrows
    @Test
    void addRequestHeader(){
        webClient.get()
                .uri("/add")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getHeader("X-Test");
        Assertions.assertEquals("hola", value);
    }

    @SneakyThrows
    @Test
    void setRequestHeader(){
        //when
        webClient.get()
                .uri("/set")
                .header("X-Test", "hi")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getHeader("X-Test");
        Assertions.assertEquals("hola", value);
    }

    @SneakyThrows
    @Test
    void mapRequestHeader(){
        webClient.get()
                .uri("/map")
                .header("X-Test", "hi")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getHeader("X-Test-Next");
        Assertions.assertEquals("hi", value);

        var value2 = request.getHeader("X-Test");
        Assertions.assertEquals("hi", value2);
    }

    @SneakyThrows
    @Test
    void removeRequestHeader(){
        webClient.get()
                .uri("/remove")
                .header("X-Test", "hi")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getHeader("X-Test");
        Assertions.assertNull(value);
    }
}
