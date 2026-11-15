package com.umc.movie.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH = "bearerAuth"; // 보안 스킴의 이름 (아래 두 곳에서 같은 이름을 써야 연결됨)

    @Bean
    public OpenAPI openAPI() {
        // 1. "Authorization: Bearer {토큰}" 형태의 인증 방식을 정의 → Swagger 화면에 Authorize 🔒 버튼이 생김
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(BEARER_AUTH, bearerScheme))
                // 2. 위 인증 방식을 모든 API에 적용 → Authorize에 넣은 토큰이 요청 헤더에 자동으로 붙음
                //    (로그인처럼 토큰이 필요 없는 API는 서버의 인터셉터가 알아서 통과시킴)
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
