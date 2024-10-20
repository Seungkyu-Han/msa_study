package seungkyu.kafka.config;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import seungkyu.kafka.util.LogUtil;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LogNameUnitTest {

    StreamFunctionConfig streamFunctionConfig = new StreamFunctionConfig();

    @Mock
    LogUtil log;

    @SneakyThrows
    @Test
    public void logName(){
        var nameFlux = Flux.just("Hello", "Han", "seungkyu");

        streamFunctionConfig.logName(log)
                .accept(nameFlux);

        Thread.sleep(500);

        verify(log).info("Name: {}", "Hello");
        verify(log).info("Name: {}", "Han");
        verify(log).info("Name: {}", "seungkyu");
    }
}
