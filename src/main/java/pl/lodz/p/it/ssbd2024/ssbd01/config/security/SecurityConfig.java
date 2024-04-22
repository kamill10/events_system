package pl.lodz.p.it.ssbd2024.ssbd01.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> corsConfigurationSource())
                .authenticationProvider(authenticationProvider)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> {
                    requests
                            .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/accounts/resetPassword").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/accounts/resetPassword/token/{token}").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/accounts/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/accounts/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/accounts/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/accounts//username/").hasAnyAuthority("ADMIN", "PARTICIPANT", "MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/accounts/userData/{id}").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/{id}/setActive").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/{id}/setInactive").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/email/{id}").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/myemail/{id}").hasAnyAuthority("ADMIN", "PARTICIPANT","MANAGER")
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/mypassword/{id}").hasAnyAuthority("ADMIN", "PARTICIPANT", "MANAGER");


                });
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://localhost:5173"));
        configuration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.ACCEPT,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                HttpHeaders.IF_MATCH
        ));
        configuration.setExposedHeaders(Arrays.asList(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.ACCEPT,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                HttpHeaders.ETAG
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}