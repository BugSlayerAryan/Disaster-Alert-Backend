//package com.DisasterAlert.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SecurityConfig {
//
//    private final ClerkJwtFilter clerkJwtFilter;
//
//    public SecurityConfig(ClerkJwtFilter clerkJwtFilter) {
//        this.clerkJwtFilter = clerkJwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        // PUBLIC
//                        .requestMatchers("/api/disasters/active").permitAll()
//
//                        // 🔹 USER SYNC / PROFILE UPDATE
//                        .requestMatchers("/api/users/sync").permitAll()            // allow first-time sync
//                        .requestMatchers("/api/users/update-profile").permitAll()  // allow popup update
//
//                        // ADMIN
//                        .requestMatchers("/api/disasters/**").hasRole("ADMIN")
//                        .requestMatchers("/api/help/all").hasRole("ADMIN")
//
//                        // USER
//                        .requestMatchers("/api/help/request").hasRole("USER")
//                        .requestMatchers("/api/help/my/**").hasRole("USER")
//
//                        // ADMIN/USER can update help status
//                        .requestMatchers("/api/help/*/status").hasAnyRole("ADMIN","USER")
//
//                        // any other requests
//                        .anyRequest().authenticated()
//                )
//                // Clerk JWT Filter
//                .addFilterBefore(clerkJwtFilter, UsernamePasswordAuthenticationFilter.class)
//
//                // Allow OPTIONS requests (for CORS preflight)
//                .cors();
//
//        return http.build();
//    }
//}

package com.DisasterAlert.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final ClerkJwtFilter clerkJwtFilter;

    public SecurityConfig(ClerkJwtFilter clerkJwtFilter) {
        this.clerkJwtFilter = clerkJwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 🔓 PUBLIC
                        .requestMatchers("/api/users/sync").permitAll()
                        .requestMatchers("/api/disasters/active").permitAll()

                        // 🔐 Preflight requests (important!)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔐 ADMIN
                        .requestMatchers("/api/disasters/**").hasRole("ADMIN")
                        .requestMatchers("/api/help/all").hasRole("ADMIN")

                        // 👤 USER
                        .requestMatchers("/api/help/request").hasRole("USER")
                        .requestMatchers("/api/help/my/**").hasRole("USER")

                        // 🔄 ADMIN + USER
                        .requestMatchers("/api/help/*/status").hasAnyRole("ADMIN", "USER")

                        // 🔐 everything else needs auth
                        .anyRequest().authenticated()
                )
                .addFilterBefore(clerkJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(); // enable CORS

        return http.build();
    }
}

