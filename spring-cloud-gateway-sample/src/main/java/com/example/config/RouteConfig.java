package com.example.config;

import com.example.filter.AuthFilter;
import com.example.filter.ResponseBodyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import com.example.filter.RequestParamsFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * @Description: 配置类的方式加载filter
 * @author: LinQin
 * @date: 2020/01/11
 */
@Configuration
@Slf4j
public class RouteConfig
{
    @Bean
    public GlobalFilter authFilter(){
        return new AuthFilter();
    }


    // @Bean
    // public GlobalFilter validateFilter() {
    //     return new RequestParamsFilter();
    // }

    @Bean
    public GlobalFilter responseBodyFilter() {
        return new ResponseBodyFilter();
    }


    private static final String SERVICE = "/**";
    private static final String URI = "http://127.0.0.1:9090";
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        //@formatter:off
        return builder.routes()
                      .route("path_route", r -> r.path("/get")
                                                 .uri("http://httpbin.org").order(-1999))
                      .route("host_route", r -> r.host("*.myhost.org")
                                                 .uri("http://httpbin.org"))
                      .route("rewrite_route", r -> r.host("*.rewrite.org")
                                                    .filters(f -> f.rewritePath("/foo/(?<segment>.*)",
                                                            "/${segment}"))
                                                    .uri("http://httpbin.org"))
                      .route("hystrix_route", r -> r.host("*.hystrix.org")
                                                    .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
                                                    .uri("http://httpbin.org"))
                      .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
                                                             .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
                                                             .uri("http://httpbin.org"))
                      // .route("limit_route", r -> r
                      //         .host("*.limited.org").and().path("/anything/**")
                      //         .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                      //         .uri("http://httpbin.org"))
                      .route("websocket_route", r -> r.path("/echo")
                                                      .uri("ws://localhost:9000"))

                      /*
                    route1 是get请求，get请求使用readBody会报错
                    route2 是post请求，Content-Type是application/x-www-form-urlencoded，readbody为String.class
                    route3 是post请求，Content-Type是application/json,readbody为Object.class
                     */
                      .route("route1",
                              r -> r
                                      .method(HttpMethod.GET)
                                      .and()
                                      .path(SERVICE)
                                      .filters(f -> {
                                          // f.filter(requestFilter);
                                          return f;
                                      })
                                      .uri(URI))
                      .route("route2",
                              r -> r
                                      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                      .and()
                                      .method(HttpMethod.POST)
                                      .and()
                                      .readBody(String.class, readBody -> {
                                          log.info("request method POST, Content-Type is application/x-www-form-urlencoded, body  is:{}", readBody);
                                          return true;
                                      })
                                      .and()
                                      .path(SERVICE)
                                      .filters(f -> {
                                          // f.filter(requestFilter);
                                          return f;
                                      })
                                      .uri(URI))
                      .route("route3",
                              r -> r
                                      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                      .and()
                                      .method(HttpMethod.POST)
                                      .and()
                                      .readBody(Object.class, readBody -> {
                                          log.info("request method POST, Content-Type is application/json, body  is:{}", readBody);
                                          return true;
                                      })
                                      .and()
                                      .path(SERVICE)
                                      .filters(f -> {
                                          // f.filter(requestFilter);
                                          return f;
                                      })
                                      .uri(URI))
                      .build();
        //@formatter:on
    }

}
