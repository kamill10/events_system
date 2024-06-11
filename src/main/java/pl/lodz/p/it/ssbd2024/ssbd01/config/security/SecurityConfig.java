package pl.lodz.p.it.ssbd2024.ssbd01.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
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
                            .requestMatchers(HttpMethod.POST, "/api/auth/refresh-token").hasAnyRole("ADMIN", "PARTICIPANT", "MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/accounts/reset-password").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/reset-password/token/{token}").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/accounts/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/accounts/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/accounts/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/accounts/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/accounts/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/api/me/change-password/token/{token}").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/api/me/change-email/token/{token}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/me").authenticated()
                            .requestMatchers(HttpMethod.GET, "/api/me/*").authenticated()
                            .requestMatchers(HttpMethod.PATCH, "/api/me/**").hasAnyRole("ADMIN", "PARTICIPANT", "MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/me/*").hasAnyRole("ADMIN", "PARTICIPANT", "MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/me/*").hasAnyRole("ADMIN", "PARTICIPANT", "MANAGER")


                            .requestMatchers(HttpMethod.GET, "/api/events").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/events/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/events/sessions/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/events/session/{id}/participants").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/events/session/{id}").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/events/session").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.DELETE, "/api/events/session/{id}").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/events/{id}").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/events").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.DELETE, "/api/events/{id}").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/events/mail").hasRole("MANAGER")


                            .requestMatchers(HttpMethod.GET, "/api/location/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.DELETE, "/api/location/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/location/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PATCH, "/api/location/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/location/**").hasRole("MANAGER")

                            .requestMatchers(HttpMethod.GET, "/api/rooms/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PATCH, "/api/rooms/**").hasRole("MANAGER")


                            .requestMatchers(HttpMethod.GET, "/api/speakers").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.GET, "/api/speakers/search").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.POST, "/api/speakers").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.GET, "/api/speakers/{id}").hasRole("MANAGER")
                            .requestMatchers(HttpMethod.PUT, "/api/speakers/{id}").hasRole("MANAGER")


                            .requestMatchers(HttpMethod.GET, "/api/events/me/**").hasRole("PARTICIPANT")
                            .requestMatchers(HttpMethod.POST, "/api/events/me/**").hasRole("PARTICIPANT")
                            .requestMatchers(HttpMethod.DELETE, "/api/events/me/**").hasRole("PARTICIPANT");


                });
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.ACCEPT,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                HttpHeaders.IF_MATCH,
                HttpHeaders.ORIGIN
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