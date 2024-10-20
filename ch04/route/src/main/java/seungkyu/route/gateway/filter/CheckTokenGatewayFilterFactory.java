package seungkyu.route.gateway.filter;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class CheckTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<CheckTokenGatewayFilterFactory.Config> {

    public CheckTokenGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("token");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                var tokenHeader = exchange.getRequest().getHeaders()
                        .getFirst(config.token);

                if(tokenHeader == null || tokenHeader.isEmpty()) {
                    return chain.filter(exchange);
                }

                String userId = getUserIdByToken(tokenHeader);

                var nextRequest = exchange.getRequest()
                        .mutate().header("X-User-Id", userId).build();

                var nextExchange = exchange.mutate().request(nextRequest).build();

                return chain.filter(nextExchange);
            }
        };
    }

    private static final Map<String, String> tokenUserMap;

    static{
        tokenUserMap = Map.of("123456", "100");
    }

    @Nullable
    private String getUserIdByToken(String tokenHeader){
        return tokenUserMap.get(tokenHeader);
    }

    @Getter
    @Setter
    public static class Config{
        private String token;
    }
}
