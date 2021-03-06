package com.lrm.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                //允许游客访问首页、登录页、注册页 (对应的路径url)
                .excludePathPatterns("/")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login");
        registry.addInterceptor(new AuthorityInterceptor())
                .addPathPatterns("/admin/**");
    }
}
