package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/11
 */

@RestController
@SpringBootApplication
public class Application {

    @PostMapping("/hello")
    public String hystrixfallback() {
        return "This is a hello";
    }

    // @Bean
    // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //     //@formatter:off
    //     return builder.routes()
    //                   .route("path_route", r -> r.path("/get")
    //                                              .uri("http://httpbin.org"))
    //                   .route("host_route", r -> r.host("*.myhost.org")
    //                                              .uri("http://httpbin.org"))
    //                   .route("rewrite_route", r -> r.host("*.rewrite.org")
    //                                                 .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
    //                                                         "/${segment}"))
    //                                                 .uri("http://httpbin.org"))
    //                   .route("hystrix_route", r -> r.host("*.hystrix.org")
    //                                                 .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
    //                                                 .uri("http://httpbin.org"))
    //                   .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
    //                                                          .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
    //                                                          .uri("http://httpbin.org"))
    //                   // .route("limit_route", r -> r
    //                   //         .host("*.limited.org").and().path("/anything/**")
    //                   //         .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
    //                   //         .uri("http://httpbin.org"))
    //                   .route("websocket_route", r -> r.path("/echo")
    //                                                   .uri("ws://localhost:9000"))
    //                   .route("test", r -> r.path("/**")
    //                                        // .filters()
    //                                        .uri("http://127.0.01:8000"))
    //                   .build();
    //     //@formatter:on
    // }

    // @Bean
    // RedisRateLimiter redisRateLimiter() {
    //     return new RedisRateLimiter(1, 2);
    // }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.httpBasic().and()
                   .csrf().disable()
                   .authorizeExchange()
                   .pathMatchers("/anything/**").authenticated()
                   .anyExchange().permitAll()
                   .and()
                   .build();
    }

    // @Bean
    // public MapReactiveUserDetailsService reactiveUserDetailsService() {
    //     UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build();
    //     return new MapReactiveUserDetailsService(user);
    // }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
