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

@ActiveProfiles("gatewayfilter-greeting")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
public class AddGreetingPathGatewayFilterTest {

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
        mockWebServer.enqueue(new MockResponse());

        webClient.get()
                .uri("/abc/def")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var path = request.getPath();

        Assertions.assertEquals(path, "/hello/abc/def");
    }
}
