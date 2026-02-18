package com.cosmeticPlatform.CosmeticPlatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private UserService userDetailsService;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authManagerBuilder.userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
                return authManagerBuilder.build();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable) // CSRF'yi devre dışı bırak
                                .cors(cors -> cors.configurationSource(request -> {
                                        var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                                        corsConfiguration
                                                        .setAllowedOrigins(java.util.List.of("http://localhost:5173",
                                                                        "http://localhost:3000"));
                                        corsConfiguration.setAllowedMethods(
                                                        java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                                        corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                                        corsConfiguration.setAllowCredentials(true);
                                        return corsConfiguration;
                                }))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                                                .requestMatchers("/api/auth/**").permitAll() // Allow auth endpoints
                                                .requestMatchers("/swagger-ui/**").permitAll()
                                                .requestMatchers("/swagger-resources/**").permitAll()
                                                .requestMatchers("/swagger-ui.html").permitAll()
                                                .requestMatchers("/v3/api-docs/**").permitAll()
                                                // .requestMatchers("/api/user/all").hasAuthority("admin")
                                                .requestMatchers("/api/product/**").permitAll()
                                                .requestMatchers("/api/analysis/**").permitAll()
                                                .requestMatchers("/api/shelf/**").permitAll()
                                                .requestMatchers("/api/wishlist/**").permitAll()
                                                .requestMatchers("/api/price-alert/**").permitAll()
                                                .requestMatchers("/api/recommendations/**").permitAll()
                                                .requestMatchers("/api/routines/**").permitAll()
                                                .requestMatchers("/api/history/**").permitAll()
                                                .requestMatchers("/api/cart/**").permitAll()
                                                .requestMatchers("/api/address/**").permitAll()
                                                .requestMatchers("/api/payment/**").permitAll()
                                                .requestMatchers("/api/photos/**").permitAll()
                                                .requestMatchers("/api/qa/**").permitAll()
                                                .requestMatchers("/api/bundles/**").permitAll()
                                                .requestMatchers("/api/loyalty/**").permitAll()
                                                .requestMatchers("/api/ingredients/**").permitAll()
                                                .requestMatchers("/uploads/**").permitAll()
                                                .requestMatchers("/api/order/**").permitAll()
                                                .requestMatchers("/api/reviews/**").permitAll()
                                                .requestMatchers("/api/blog/**").permitAll()
                                                .requestMatchers("/api/user/**").permitAll()
                                                .requestMatchers("/api/wishlist/**").permitAll()
                                                .requestMatchers("/api/price-alert/**").permitAll()
                                                .requestMatchers("/actuator/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/user/add")
                                                .hasAnyRole("ADMIN", "CLIENT")
                                                .requestMatchers("/api/auth/forgot-password",
                                                                "/api/auth/reset-password")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .oauth2Login(oauth2 -> oauth2
                                                .successHandler((request, response, authentication) -> {
                                                        org.springframework.security.oauth2.core.user.OAuth2User oauthUser = (org.springframework.security.oauth2.core.user.OAuth2User) authentication
                                                                        .getPrincipal();
                                                        String email = oauthUser.getAttribute("email");
                                                        String name = oauthUser.getAttribute("name");

                                                        com.cosmeticPlatform.CosmeticPlatform.model.User user = userDetailsService
                                                                        .processOAuthPostLogin(email, name);

                                                        response.sendRedirect("http://localhost:5173/login?oauth_email="
                                                                        + email + "&oauth_name="
                                                                        + java.net.URLEncoder.encode(name,
                                                                                        java.nio.charset.StandardCharsets.UTF_8)
                                                                        + "&oauth_id=" + user.getId());
                                                }));

                return http.build();
        }

}
