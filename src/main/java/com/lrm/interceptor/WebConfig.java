package com.lrm.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //允许游客访问登录页、注册页 (对应的路径url)
        //spring2更新之后需要取消拦截静态资源了 并且在打开页面时，也需要在请求头中包含token
        //还有一个/error转发的问题
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/layui-v2.5.7/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/loginn.html")
                .excludePathPatterns("/signin.html")
                .excludePathPatterns("/homepage.html")
                .excludePathPatterns("/editmine.html")
                .excludePathPatterns("/comment.html")
                .excludePathPatterns("/mine.html")
                .excludePathPatterns("/message.html")
                .excludePathPatterns("/editor.html")
                .excludePathPatterns("/search.html");

        registry.addInterceptor(new AuthorityInterceptor())
                .addPathPatterns("/admin/**");
    }

}
