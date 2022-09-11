package com.bridgelabz.fundoonotesservice.configuration;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

//@SuppressWarnings({"All", "deprecation"})
@Configuration
//@EnableSwagger2WebMvc
//@Import(SpringDataRestConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {
    public static final String AUTHORIZATION_HEADER = "Authorization";

   /*@Bean

   public Docket api() {

       return new Docket(DocumentationType.SWAGGER_2)

               .select()

               .apis(RequestHandlerSelectors.any())

               .paths(PathSelectors.any())

               .build()

               .apiInfo(getApiInfo());

   }*/

    @Bean

    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)

                .apiInfo(apiInfo())

                .securityContexts(Arrays.asList(securityContext()))

                .securitySchemes(Arrays.asList(apiKey()))

                .select()

                .apis(RequestHandlerSelectors.basePackage("com.bridgelabz.fundoouserservice.controller"))

                .paths(PathSelectors.any())

                .build()

                .apiInfo(apiInfo());

    }

    private ApiKey apiKey() {

        return new ApiKey(AUTHORIZATION_HEADER, "JWT", "header");

    }

    private SecurityContext securityContext() {

        return SecurityContext.builder()

                .securityReferences(defaultAuth())

                .build();

    }


    List<SecurityReference> defaultAuth() {

        AuthorizationScope authorizationScope

                = new AuthorizationScope("global", "accessEverything");

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];

        authorizationScopes[0] = authorizationScope;

        return Arrays.asList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));

    }

   /*private ApiInfo getApiInfo() {

       Contact contact = new Contact("Learner Management System", "http://bridgelabz.com",

               "sunil.patil@bridgelabz.com");

       return new ApiInfoBuilder().title("LMS Service Swagger API")

               .description("LMS Service Swagger API for Learner Management System").version("0.0.1-SNAPSHOT")

               .license("Apache 2.0").licenseUrl("http://www.apache.org/licenses/LICENSE-2.0").contact(contact)

               .build();

   }*/

    private ApiInfo apiInfo() {

        return new ApiInfo("FundooUserService",

                "APIs for E.",

                "1.0",

                "Terms of service",

                new Contact("FundooUserService", "http://bridgelabz.fundoouserservice", "himanshi1252@gmail.com"),

                "Apache 2.0",

                "http://www.apache.org/licenses/LICENSE-2.0",

                Collections.emptyList());

    }

    @Bean

    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {

        return new BeanPostProcessor() {

            @Override

            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {

                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));

                }

                return bean;

            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {

                List<T> copy = mappings.stream()

                        .filter(mapping -> mapping.getPatternParser() == null)

                        .collect(Collectors.toList());

                mappings.clear();

                mappings.addAll(copy);

            }

            @SuppressWarnings("unchecked")

            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {

                try {

                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");

                    field.setAccessible(true);

                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);

                } catch (IllegalArgumentException | IllegalAccessException e) {

                    throw new IllegalStateException(e);

                }

            }

        };

    }
}
