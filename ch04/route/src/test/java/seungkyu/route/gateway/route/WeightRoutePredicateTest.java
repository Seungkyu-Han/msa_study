package seungkyu.route.gateway.route;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

@ActiveProfiles("weight-predicate")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = RouteApplication.class
)
@Slf4j
public class WeightRoutePredicateTest {


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
        //given
        int testcase = 1000;

        for(int i = 0; i < testcase; i++){
            mockWebServer.enqueue(new MockResponse());
        }

        for(int i = 0; i < testcase; i++){
            webClient.get()
                    .uri("/")
                    .exchange()
                    .expectStatus().isOk();
        }

        var mainCount = 0;

        for(int i = 0; i < testcase; i++){
            var request = mockWebServer.takeRequest();
            var path = request.getPath();
            if(path.equals("/branch/main"))
                mainCount++;
        }

        log.info("mainCount: {}", mainCount);
        log.info("canaryCount: {}", testcase-mainCount);
    }
}
