package seungkyu.kafka.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogUtil {
    public void info(String format, Object args) {
        log.info(format, args);
    }
}
