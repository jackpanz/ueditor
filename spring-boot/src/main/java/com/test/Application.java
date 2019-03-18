package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

@Configuration
@SpringBootApplication
@ComponentScan(
        basePackages = {
                "com.test"
        }
)
public class Application extends SpringBootServletInitializer implements WebMvcConfigurer {

    /**
     * 静态化处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 发布到TOMCAT后模拟web.xml
     * main启动不需要
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Bean
    public HttpMessageConverters converters() {
        return new HttpMessageConverters(
                false, Arrays.asList(
                new StringHttpMessageConverter(Charset.forName("UTF-8"))
        ));
    }


    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);



        //File config = new File("C:\\project\\uediter-spring-aliyun-root\\uediter-spring-aliyun\\src\\main\\resources\\ueditor.json");
//        InputStream inputStream = Application.class.getResourceAsStream("/ueditor.json");
//        JSONObject jsonObject = new JSONObject(inputStream);


//        InputStream inputStream = Application.class.getResourceAsStream("/ueditor.json");
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte bytes[] = new byte[1024];
//        int length = -1;
//        while ((length = inputStream.read(bytes)) != -1) {
//            byteArrayOutputStream.write(bytes, 0, length);
//        }
//        String configContent = new String(byteArrayOutputStream.toByteArray(), "utf-8");
//        JSONObject jsonObject = new JSONObject(configContent)

    }

}
