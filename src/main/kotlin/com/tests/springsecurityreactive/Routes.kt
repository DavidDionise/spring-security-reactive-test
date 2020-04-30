package com.tests.springsecurityreactive

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
class Routes{

    @Bean
    fun testRoutes(): RouterFunction<ServerResponse> = router {
        GET("test", ::testHandler)
        GET("admin-test", ::adminTestHandler)
    }

    fun testHandler(request: ServerRequest): Mono<ServerResponse> {
//        return request.principal()
//            .flatMap { principal ->
//                if ((principal as User).hasAuthority("TEST")) {
//                    ServerResponse
//                        .ok()
//                        .bodyValue("GREAT")
//                } else {
//                    ServerResponse
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .bodyValue("UNAUTHORIZED")
//                }
//            }
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { context ->
                if ((context.authentication.principal as User).hasAuthority("TEST")) {
                    ServerResponse
                        .ok()
                        .bodyValue("GREAT")
                } else {
                    ServerResponse
                        .status(HttpStatus.UNAUTHORIZED)
                        .bodyValue("UNAUTHORIZED")
                }
            }
    }

    fun adminTestHandler(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse
            .ok()
            .bodyValue("HI ADMIN")
    }
}