package com.backend.api_gateway.security;

import com.backend.api_gateway.utils.JwtUtil;
import com.backend.api_gateway.utils.dtos.TokenRenewResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final JwtUtil jwtUtil;
    private final WebClient.Builder webClientBuilder;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/auth/send-otp",
            "/auth/verify-otp",
            "/auth/complete-profile",
            "/auth/renew-refresh",
            "/auth/logout",
            "/auth/admin/login",
            "/swagger-ui.html",
            "/api-docs"
    );

    private boolean isPublic(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public @NonNull ServerResponse filter(
            @NonNull ServerRequest request,
            @NonNull HandlerFunction<ServerResponse> next
    ) throws Exception {

        String path = request.uri().getPath();
        if (isPublic(path)) return next.handle(request);

        String auth = request.headers().firstHeader("authorization");
        if (auth == null) auth = request.headers().firstHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            return ServerResponse.status(401).body("Missing or invalid Authorization header");
        }

        System.out.println("HEADERS = " + request.headers().asHttpHeaders());

        String token = auth.substring(7);

        try {
            var claims = jwtUtil.validateToken(token);

            Object refClaim = claims.get("refId");
            String ref = refClaim == null ? "-1" : refClaim.toString();

            ServerRequest mutated = ServerRequest.from(request)
                    .header("X-User-Id", claims.getSubject())
                    .header("X-Ref-Id", ref)
                    .header("X-Role", claims.get("role").toString())
                    .header("X-Phone", claims.get("phone").toString())
                    .build();

            return next.handle(mutated);

        } catch (ExpiredJwtException ex) {

            String refresh = request.headers().firstHeader("X-Refresh-Token");
            Long authUserId = Long.parseLong(ex.getClaims().getSubject());

            if (refresh == null) {
                return forceLogout(authUserId);
            }

            TokenRenewResponse response = webClientBuilder
                    .build()
                    .post()
                    .uri("lb://AUTH-SERVICE/auth/renew-refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "authUserId", authUserId,
                            "refreshToken", refresh
                    ))
                    .retrieve()
                    .bodyToMono(TokenRenewResponse.class)
                    .onErrorResume(err -> null)
                    .block();

            if (response == null || response.getAccessToken() == null)
                return forceLogout(authUserId);

            String newAccess = response.getAccessToken();
            var newClaims = jwtUtil.validateToken(newAccess);

            Object newRefClaim = newClaims.get("refId");
            String newRef = newRefClaim == null ? "-1" : newRefClaim.toString();

            ServerRequest mutated = ServerRequest.from(request)
                    .header("X-User-Id", newClaims.getSubject())
                    .header("X-Ref-Id", newRef)
                    .header("X-Role", newClaims.get("role").toString())
                    .header("X-Phone", newClaims.get("phone").toString())
                    .header("X-New-Access-Token", newAccess)
                    .build();

            return next.handle(mutated);

        } catch (Exception e) {
            return ServerResponse.status(401).body("Invalid or expired token");
        }
    }

    private ServerResponse forceLogout(Long authUserId) {

        try {
            webClientBuilder.build()
                    .post()
                    .uri("lb://AUTH-SERVICE/auth/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("authUserId", authUserId))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorResume(err -> null)
                    .block();
        } catch (Exception ignored) {}

        return ServerResponse.status(401).body("logged_out");
    }

}