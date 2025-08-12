package shoppingmall.hanaro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "Hanaro Shopping Mall API";
    private static final String API_VERSION = "v1.0.0";
    private static final String API_DESCRIPTION = "하나로 쇼핑몰 프로젝트 API 명세서입니다.";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION);

        // Security 스키마 설정
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(java.util.List.of(securityRequirement))
                .info(info);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("01. Public APIs")
                .pathsToMatch("/api/users/**", "/api/products/**")
                .pathsToExclude("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("02. User APIs")
                .pathsToMatch("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("03. Admin APIs")
                .pathsToMatch("/api/admin/**")
                .build();
    }
}
