package com.controller;

import com.openapi.v1.api.UserApi;
import com.openapi.v1.bean.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController implements UserApi {

    @Override
    public User getUser(Long id) {

        System.out.println("id=" + id);
        User user = new User();
        user.setAge(22L);
        user.setId(1L);
        user.setName("lin");
        return user;
    }
}
