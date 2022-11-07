package com.fullmugu.nanumeal.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swagger(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select() //ApiSelectorBuilder를 생성
                .apis(RequestHandlerSelectors.any()) //api스펙이 작성되어 있는 패키지 지정, 즉,컨트롤러가 존재하는 패키지를 basePackage로 지정하여, RequestMapping이 선언된 API를 문서화함
                .paths(PathSelectors.any()) //apis로 선택되어진 API중 특정 path조건에 맞는 API들을 다시 필터링, 문서화
                .build();
    }

}
