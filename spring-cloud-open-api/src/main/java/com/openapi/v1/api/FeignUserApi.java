package com.openapi.v1.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(value = "server",
        path = "/server")
public interface FeignUserApi extends UserApi{
}
