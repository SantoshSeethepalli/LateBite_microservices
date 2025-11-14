package com.backend.api_gateway.security;

import com.backend.api_gateway.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Set;

@Component
public class JwtAuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final JwtUtil jwtUtil;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/auth/send_otp",
            "/auth/verify_otp",
            "/auth/complete_profile",
            "/auth/renew_refresh",
            "/auth/logout",
            "/swagger-ui.html",
            "/api-docs"
    );

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean isPublic(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {

        String path = request.uri().getPath();

        if (isPublic(path)) return next.handle(request);

        String auth = request.headers().firstHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            return ServerResponse.status(401).body("Missing or invalid Authorization header");
        }

        try {
            String token = auth.substring(7);
            var claims = jwtUtil.validateToken(token);

            ServerRequest mutated = ServerRequest.from(request)
                    .header("X-User-Id", claims.getSubject())
                    .header("X-Ref-Id", claims.get("refId").toString())
                    .header("X-Role", claims.get("role").toString())
                    .header("X-Phone", claims.get("phone").toString())
                    .build();

            return next.handle(mutated);

        } catch (Exception e) {
            return ServerResponse.status(401).body("Invalid or expired token");
        }
    }

}
