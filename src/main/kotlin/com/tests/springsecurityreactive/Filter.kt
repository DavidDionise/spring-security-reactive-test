package com.tests.springsecurityreactive

import org.springframework.http.HttpMethod
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class Filter : HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        println(">> HIT FILTER")
        return next.handle(request)
    }
}

@Component
class CustomAfterFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return PathPatternParserServerWebExchangeMatcher("/admin-test", HttpMethod.GET)
            .matches(exchange)
            .flatMap { matchResult ->
                if (matchResult.isMatch) {
                    ReactiveSecurityContextHolder.getContext().flatMap {
                        println(">>> Context: $it")
                        chain.filter(exchange)
                    }
                } else {
                    chain.filter(exchange)
                }
            }
    }
}