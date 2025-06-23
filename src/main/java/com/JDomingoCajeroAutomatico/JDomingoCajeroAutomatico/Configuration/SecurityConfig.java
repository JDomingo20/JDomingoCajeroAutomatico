package com.JDomingoCajeroAutomatico.JDomingoCajeroAutomatico.Configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .permitAll() // usa página de login por defecto
                )
                .logout(logout -> logout
                .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT UserName, NIP, Estatus FROM Usuario WHERE UserName = ?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT UserName, NombreRol FROM RolManager WHERE UserName = ?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
