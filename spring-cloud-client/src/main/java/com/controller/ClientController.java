package com.controller;

import com.openapi.v1.api.FeignUserApi;
import com.openapi.v1.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    private FeignUserApi userApi;

    @RequestMapping("/getUserClient")
    public void getUser(@RequestParam Long id) {
        User user = userApi.getUser(id);
        System.out.println(user.toString());
    }
}
