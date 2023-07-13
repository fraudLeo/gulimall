package com.leo.gulimall.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GulimallCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {

        //UrlBaseCorsConfigurationSource这是CorsConfigurationSource的实现类
//        CorsConfigurationSource;

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //允许所有的请求头
        corsConfiguration.addAllowedHeader("*");
        //允许所有的请求方式
        corsConfiguration.addAllowedMethod("*");
        //允许所有的请求来源
        corsConfiguration.addAllowedOriginPattern("*");
        //是否允许携带cookie进行跨域
        corsConfiguration.setAllowCredentials(true);

        //注册配置文件
        source.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsWebFilter(source);
    }
}
