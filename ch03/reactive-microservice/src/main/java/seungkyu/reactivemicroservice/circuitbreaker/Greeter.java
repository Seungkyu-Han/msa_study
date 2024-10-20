package seungkyu.reactivemicroservice.circuitbreaker;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Greeter {
    public String generate(String who) {
        log.info("Greeting {}", who);
        return "Hello " + who + "!";
    }
}