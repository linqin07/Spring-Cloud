package com.example;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 版本：
 * Spring Cloud: Greenwich.RC2
 * Spring Boot: 2.1.1.RELEASE
 * @Description:
 * @author: LinQin
 * @date: 2020/01/11
 */

@RestController
@SpringBootApplication
public class Application {

    public static final String XXX_SDFSD_20121212 = "xxx_sdfsd_20121212";

    @PostMapping("/hello")
    public String hystrixfallback() {
        return "This is a hello";
    }



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
