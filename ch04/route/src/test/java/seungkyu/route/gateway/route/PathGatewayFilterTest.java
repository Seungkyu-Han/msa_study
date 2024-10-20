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

@ActiveProfiles("gatewayfilter-path")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
public class PathGatewayFilterTest {
    @Autowired
    private WebTestClient webClient;

    private MockWebServer mockWebServer;


    @SneakyThrows
    @BeforeEach
    void setUp() {
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
    void prefixPath(){
        webClient.get()
                .uri("/prefix")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getPath();

        Assertions.assertEquals("/hello/prefix", value);
    }

    @SneakyThrows
    @Test
    void stripPath(){
        webClient.get()
                .uri("/strip/a/b/hello/abc")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getPath();
        Assertions.assertEquals("/hello/abc", value);
    }

    @SneakyThrows
    @Test
    void setPath(){
        webClient.get()
                .uri("/set/abc")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getPath();
        Assertions.assertEquals("/hello/abc", value);
    }

    @SneakyThrows
    @Test
    void rewritePath(){

        webClient.get()
                .uri("/rewrite/abc/def")
                .exchange()
                .expectStatus().isOk();

        var request = mockWebServer.takeRequest();
        var value = request.getPath();

        Assertions.assertEquals("/hello/abc/def/hoi", value);
    }
}
