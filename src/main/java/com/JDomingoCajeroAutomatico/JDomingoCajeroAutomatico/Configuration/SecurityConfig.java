package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.Configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/Inicio").hasAnyAuthority("Cliente", "Administrador")
                .requestMatchers("/Inicio/Rellenar").hasAuthority("Administrador")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/Login")
                .loginProcessingUrl("/authenticateTheUser")
                .defaultSuccessUrl("/Inicio", true)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIOID")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);
                    response.sendRedirect("/login?logout");
                })
                )
                .exceptionHandling((exceptions) -> exceptions
                .accessDeniedPage("/access-denied")
                )
                .headers(headers -> headers
                .cacheControl(cache -> cache.disable())
                );
        return http.build();
    }
    
    @Bean
    public UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT UserName, NIP, Estatus FROM Usuario WHERE UserName = ?"
        );
        
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT UserName, NombreRol FROM RolManager WHERE UserName =?");
        
        return jdbcUserDetailsManager;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
}
