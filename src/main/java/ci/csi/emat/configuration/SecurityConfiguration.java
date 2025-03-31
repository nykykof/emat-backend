package ci.csi.emat.configuration;

import ci.csi.emat.domain.authentication.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfiguration {

    private static final String MATCH_ALL = "/**";

    private final JwtAuthFilter jwtAuthFilter;

    private final UserDetailsServiceImpl userService;

    public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // refer to https://stytch.com/blog/argon2-vs-bcrypt-vs-scrypt/
        //return new BCryptPasswordEncoder();
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain usernamePassword(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userService);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.securityMatcher(new AntPathRequestMatcher(MATCH_ALL))
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                //we are building an API
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/logout").permitAll()
                            .requestMatchers("/h2-console/**").permitAll();
                    authorize.anyRequest().authenticated();
                    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                })
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS
                        ))
                .authenticationManager(authenticationManager)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOriginPatterns(Collections.singletonList("*"));
        cors.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.PUT.name()));
        cors.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION.toLowerCase(),
                HttpHeaders.CONTENT_TYPE.toLowerCase()));
        cors.setExposedHeaders(Collections.singletonList(HttpHeaders.LOCATION));
        cors.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
