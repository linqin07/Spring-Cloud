package com.example.config;

import com.example.filter.AuthFilter;
import com.example.filter.ResponseBodyFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import com.example.filter.RequestParamsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 配置类的方式加载filter
 * @author: LinQin
 * @date: 2020/01/11
 */
@Configuration
public class AutoFilter {
    @Bean
    public GlobalFilter authFilter(){
        return new AuthFilter();
    }


    @Bean
    public GlobalFilter validateFilter() {
        return new RequestParamsFilter();
    }

    @Bean
    public GlobalFilter responseBodyFilter() {
        return new ResponseBodyFilter();
    }


}
