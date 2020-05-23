package com.example.turnirken;

import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableAutoConfiguration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select().paths(postPaths())
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build();
    }

    private Predicate<String> postPaths() {
        return or(regex("/.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Turnirken API")
                .description("Turnirken API 2020")
                .termsOfServiceUrl("https://vk.com/mathyber")
                .contact("mathyber (ilan23f@gmail.com)")
                .version("1.0")
                .build();
    }

}
