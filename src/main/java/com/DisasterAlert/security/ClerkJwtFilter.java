//package com.DisasterAlert.security;
//
//import com.DisasterAlert.service.UserService;
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.jwk.source.RemoteJWKSet;
//import com.nimbusds.jose.proc.JWSVerificationKeySelector;
//import com.nimbusds.jose.proc.SecurityContext;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
//import com.nimbusds.jwt.proc.DefaultJWTProcessor;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.*;
//
//@Component
//public class ClerkJwtFilter extends OncePerRequestFilter {
//
//    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
//
//    @Autowired
//    private UserService userService;
//
//    public ClerkJwtFilter(@Value("${clerk.jwks.url}") String jwksUrl) throws Exception {
//        jwtProcessor = new DefaultJWTProcessor<>();
//        JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(new URL(jwksUrl));
//        jwtProcessor.setJWSKeySelector(
//                new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource)
//        );
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getRequestURI().equals("/api/users/sync");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            String token = authHeader.substring(7);
//            JWTClaimsSet claims = jwtProcessor.process(token, null);
//
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            String finalRole = "ROLE_USER";
//
//            Map<String, Object> publicMetadata = (Map<String, Object>) claims.getClaim("public_metadata");
//
//            if (publicMetadata != null && publicMetadata.containsKey("roles")) {
//                List<String> roles = (List<String>) publicMetadata.get("roles");
//                for (String role : roles) {
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//                    if ("ADMIN".equals(role)) finalRole = "ROLE_ADMIN";
//                }
//            } else {
//                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//            }
//
//            // 🔥 Save user if not exists
//            userService.saveUserIfNotExists(
//                    claims.getSubject(),
//                    (String) claims.getClaim("email"),
//                    (String) claims.getClaim("name"),
//                    finalRole
//            );
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            claims.getSubject(),
//                            null,
//                            authorities
//                    );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // 🔹 Important: Pass Clerk ID to controllers
//            request.setAttribute("clerkUserId", claims.getSubject());
//
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid or expired Clerk token");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}

package com.DisasterAlert.security;

import com.DisasterAlert.service.UserService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Component
public class ClerkJwtFilter extends OncePerRequestFilter {

    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

    @Autowired
    private UserService userService;

    public ClerkJwtFilter(@Value("${clerk.jwks.url}") String jwksUrl) throws Exception {
        jwtProcessor = new DefaultJWTProcessor<>();
        JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(new URL(jwksUrl));
        jwtProcessor.setJWSKeySelector(
                new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource)
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/users/sync");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            JWTClaimsSet claims = jwtProcessor.process(token, null);

            List<GrantedAuthority> authorities = new ArrayList<>();
            String finalRole = "ROLE_USER";

            // 🔹 Get roles from Clerk token
            Map<String, Object> publicMetadata = (Map<String, Object>) claims.getClaim("public_metadata");

            if (publicMetadata != null && publicMetadata.containsKey("roles")) {
                List<String> roles = (List<String>) publicMetadata.get("roles");

                // 🔹 Debug: print roles to console
                System.out.println("Clerk roles: " + roles);

                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    if ("ADMIN".equals(role)) finalRole = "ROLE_ADMIN";
                }
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            // 🔥 Save user in DB if not exists
            userService.saveUserIfNotExists(
                    claims.getSubject(),
                    (String) claims.getClaim("email"),
                    (String) claims.getClaim("name"),
                    finalRole
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 🔹 Pass Clerk ID to controllers
            request.setAttribute("clerkUserId", claims.getSubject());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired Clerk token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

