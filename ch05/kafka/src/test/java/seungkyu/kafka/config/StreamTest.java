package seungkyu.kafka.config;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import seungkyu.kafka.KafkaApplication;
import seungkyu.kafka.util.LogUtil;

import java.util.List;

import static org.mockito.Mockito.verify;

@Import(TestChannelBinderConfiguration.class)
@ActiveProfiles("stream")
@AutoConfigureWebTestClient
@SpringBootTest(
        classes = KafkaApplication.class
)
public class StreamTest {

    @Autowired
    InputDestination inputDestination;

    @Autowired
    OutputDestination outputDestination;

    @Autowired
    WebTestClient webTestClient;

    @SpyBean
    LogUtil spyLogUtil;

    StreamFunctionConfig streamFunctionConfig = new StreamFunctionConfig();

    @Test
    void contextLoad(){}

    @Test
    void logName(){
        var payload = "seungkyu";
        var input = new GenericMessage<>(payload);
        var inputBinding = "logName-in-0";

        inputDestination.send(input, inputBinding);

        verify(spyLogUtil).info("Name: {}", payload);
    }

    @Test
    void supplyNames(){
        var outputBinding = "supplyNames-out-0";
        var expectedNames = List.of("Hello", "Han", "seungkyu");

        for(var name: expectedNames){
            var output = outputDestination.receive(0, outputBinding);
            String outputMessage = new String(output.getPayload());

            Assertions.assertEquals(name, outputMessage);
        }
    }

    @Test
    void addGreeting(){
        var payload = "world";
        var input = new GenericMessage<>(payload);
        var inputBinding = "addGreeting-in-0";
        var outputBinding = "addGreeting-out-0";

        inputDestination.send(input, inputBinding);

        var output = outputDestination.receive(0, outputBinding);
        String outputMessage = new String(output.getPayload());

        var expected = "Hello "+ payload + "!";
        Assertions.assertEquals(expected, outputMessage);
    }

    @Test
    void streamBridge(){
        var name = "seungkyu";
        var outputBinding = "addGreeting-out-0";

        webTestClient.get()
                .uri("/greeting?name="+name)
                .exchange()
                .expectStatus().isOk();

        var output = outputDestination.receive(0, outputBinding);
        String outputMessage = new String(output.getPayload());

        var expected = "Hello " + name + "!";
        Assertions.assertEquals(expected, outputMessage);
    }
}
