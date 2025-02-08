package net.ohnonick2.naturzooprojekt.utils;


import jakarta.servlet.http.HttpServletResponse;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**", "/images/**", "/css/**", "/js/**")
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/static/js/");
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/logout", "/static/**" , "/api/public/**" ,"/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/images/{filename}" , "/" , "/api/food/getFutterplan" , "/").permitAll()
                        .requestMatchers("/usermanagement").hasAnyAuthority("USER_MANAGEMENT_READ" , "USER_MANAGEMENT_WRITE" , "*")
                        .requestMatchers("/editUser/").hasAnyAuthority("USER_MANAGEMENT_WRITE" , "*")
                        .requestMatchers("/editUser/**").hasAnyAuthority("USER_MANAGEMENT_WRITE" , "*")
                        .requestMatchers("/addUser").hasAnyAuthority("USER_MANAGEMENT_WRITE" , "*")
                        .requestMatchers("/addUser/**").hasAnyAuthority("USER_MANAGEMENT_WRITE" , "*")
                        .requestMatchers("/deleteUser/**").hasAnyAuthority("USER_MANAGEMENT_WRITE" , "*")


                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureHandler((request, response, exception) -> {
                            request.getSession().setAttribute("error", "Benutzernamen oder Passwort falsch oder Benutzer gesperrt");
                            response.sendRedirect("/login?error=error");
                        })
                        .defaultSuccessUrl("/dashboard", false)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?error=session") // URL für ungültige Sitzungen
                        .sessionFixation().migrateSession() // Sitzungsfixierung verhindern
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Sitzung bei Bedarf erstellen
                        .maximumSessions(1) // Nur eine aktive Sitzung pro Benutzer
                        .expiredSessionStrategy(event -> { // Strategie bei Ablauf der Sitzung
                            HttpServletResponse response = event.getResponse();
                            response.sendRedirect("/login?error=session"); // Weiterleitung bei abgelaufener Sitzung
                        })
                        .maxSessionsPreventsLogin(true) // Zusätzliche Anmeldungen verhindern
                        .sessionRegistry(sessionRegistry()) // SessionRegistry für Verwaltung
                )
                .sessionManagement(session -> session
                        .maximumSessions(1) // Nur eine aktive Sitzung pro Benutzer
                        .maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry())

                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                )
            ;

        return http.build();
    }






}