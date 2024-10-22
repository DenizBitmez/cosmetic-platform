package com.cosmeticPlatform.CosmeticPlatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
            AuthenticationManagerBuilder authManagerBuilder =  http.getSharedObject(AuthenticationManagerBuilder.class);
            authManagerBuilder.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
            return authManagerBuilder.build();      }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)  // CSRF'yi devre dışı bırak
                    .cors(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/swagger-resources/**").permitAll()
                            .requestMatchers("/swagger-ui.html").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
//                            .requestMatchers("/api/user/all").hasAuthority("admin")
                            .requestMatchers("/api/user/**").hasAnyAuthority("ADMIN, CLIENT, EXPERT") // Kullanıcı yönetim işlemleri
                                    .requestMatchers(HttpMethod.POST, "/api/user/add").hasAnyAuthority("ADMIN", "CLIENT")

                                    .anyRequest()
                                    .authenticated()
                    ).httpBasic(Customizer.withDefaults());

            return http.build();
        }

    }

