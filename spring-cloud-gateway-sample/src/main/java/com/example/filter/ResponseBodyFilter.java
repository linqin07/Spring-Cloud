package com.example.filter;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/13
 */
@Slf4j
public class ResponseBodyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // 解决返回体分段传输
                        List<String> list = Lists.newArrayList();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            try {
                                list.add(new String(content, "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                log.error("handle response error WrapperResponseFilter", e);
                            }
                        });
                        String responseData = Joiner.on("").join(list);
                        // TODO 自定义操作
                        // Response response = new Response();
                        // response.setCode("1");
                        // response.setMessage("请求成功");
                        // response.setData(rs);

                        // byte[] newRs = JSON.toJSONString(response).getBytes(Charset.forName("UTF-8"));
                        log.info("ResponseBody:{}", responseData);
                        byte[] newRs = responseData.getBytes(Charset.forName("UTF-8"));
                        originalResponse.getHeaders().setContentLength(newRs.length);//如果不重新设置长度则收不到消息。
                        return bufferFactory.wrap(newRs);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
