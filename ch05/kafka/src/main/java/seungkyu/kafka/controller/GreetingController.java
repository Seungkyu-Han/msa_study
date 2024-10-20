package seungkyu.kafka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/greeting")
@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final StreamBridge streamBridge;

    @GetMapping
    public void greeting(
            @RequestParam String name
    ){
        streamBridge.send("addGreeting-in-0", name);
    }
}
