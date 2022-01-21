package com.openapi.v1.api;

import com.openapi.v1.bean.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserApi {
    @RequestMapping(value ="/getUser")
    User getUser(@RequestParam Long id);
}
