package com.example.filter;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/13
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取请求体  ModifyRequestBodyGatewayFilterFactory  预言类 ReadBodyPredicateFactory
 * @author yibo
 */
@Slf4j
@Deprecated
public class RequestParamsFilter implements GlobalFilter, Ordered {

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // ServerRequest serverRequest = new DefaultServerRequest(exchange);
        ServerRequest serverRequest = new DefaultServerRequest(exchange, messageReaders);
        // mediaType
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        // read & modify body
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                                                 .flatMap(body -> {
                                                     if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {

                                                         // origin body map
                                                         Map<String, Object> bodyMap = decodeBody(body);

                                                         // TODO decrypt & auth

                                                         // new body map
                                                         Map<String, Object> newBodyMap = new HashMap<>();

                                                         return Mono.just(encodeBody(newBodyMap));
                                                     }

                                                     if (MediaType.APPLICATION_JSON
                                                             .isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON_UTF8
                                                             .isCompatibleWith(mediaType)) {
                                                         // TODO 获取请求体
                                                         log.info(body);
                                                     }
                                                     return Mono.empty();
                                                 });

        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());

        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage,  new BodyInserterContext())
                           .then(Mono.defer(() -> {
                               ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                                       exchange.getRequest()) {

                                   public HttpHeaders getHeaders() {
                                       long contentLength = headers.getContentLength();
                                       HttpHeaders httpHeaders = new HttpHeaders();
                                       httpHeaders.putAll(super.getHeaders());
                                       if (contentLength > 0) {
                                           httpHeaders.setContentLength(contentLength);
                                       } else {
                                           httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                       }
                                       return httpHeaders;
                                   }


                                   public Flux<DataBuffer> getBody() {
                                       return outputMessage.getBody();
                                   }
                               };
                               return chain.filter(exchange.mutate().request(decorator).build());
                           }));
    }


    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private Map<String, Object> decodeBody(String body) {
        return Arrays.stream(body.split("&"))
                     .map(s -> s.split("="))
                     .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    private String encodeBody(Map<String, Object> map) {
        String collect = map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
                            .collect(Collectors.joining("&"));
        log.info(collect);
        return collect;
    }
}
