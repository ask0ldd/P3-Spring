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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                // https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
                .cors(Customizer.withDefaults())
                // .headers(header -> header.contentTypeOptions(null))
                // .headers(header -> header.contentTypeOptions())
                .authorizeHttpRequests(authorize -> {
                    authorize
                            // .dispatcherTypeMatchers(DispatcherType.ERROR,
                            // DispatcherType.FORWARD).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/test1")).hasRole("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/test2")).hasRole("USER")
                            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/auth/*")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users2")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/user/*")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/rental/*", "PUT")).hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // allows h2-console frames to be displayed
                .headers(headers -> headers.frameOptions(frame -> frame.disable())/* .disable() */)
                .build();
        // html login
        // .formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
    }

}

// MVC Request Matcher
// https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html
// Authorize HTTP Request
// https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests
// Basic Auth
// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html

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