package com.example.immo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final RSAKeyProperties rsaKeys;

    public SecurityConfiguration(RSAKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys; // component wiring
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.getPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.getPublicKey()).privateKey(rsaKeys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * @Bean
     * AuthenticationManager authenticationManager(UserDetailsService
     * userDetailsService) {
     * DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
     * daoProvider.setUserDetailsService(userDetailsService);
     * daoProvider.setPasswordEncoder(passwordEncoder());
     * return new ProviderManager(daoProvider);
     * }
     */

    // !!!!!!!!!!!!!! protect routes
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                // https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
                .cors(Customizer.withDefaults())
                // .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/api/auth/login")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/auth/register")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/auth/me")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/rentals/**")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/user/**")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/messages")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/auth/*")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/img/**")).permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
    }

    /*
     * public class CustomAuthenticationEntryPoint implements
     * AuthenticationEntryPoint {
     * 
     * @Override
     * public void commence(HttpServletRequest request, HttpServletResponse
     * response,
     * AuthenticationException authException) throws IOException {
     * response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
     * }
     * }
     */
}

// MVC Request Matcher
// https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html
// Authorize HTTP Request
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests
// Basic Auth
// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html

// oauth server jwt
// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html

/*
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * public class SessionManagementSecurityConfig {
 * 
 * @Bean
 * public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
 * Exception {
 * http
 * .authorizeRequests((authorizeRequests) ->
 * authorizeRequests
 * .anyRequest().hasRole("USER")
 * )
 * .formLogin((formLogin) ->
 * formLogin
 * .permitAll()
 * )
 * .sessionManagement((sessionManagement) ->
 * sessionManagement
 * .sessionConcurrency((sessionConcurrency) ->
 * sessionConcurrency
 * .maximumSessions(1)
 * .expiredUrl("/login?expired")
 * )
 * );
 * return http.build();
 * }
 */
// basic popup login form
// .httpBasic(Customizer.withDefaults())

// latest example
// https://github.com/spring-projects/spring-security-samples/blob/6.2.x/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java