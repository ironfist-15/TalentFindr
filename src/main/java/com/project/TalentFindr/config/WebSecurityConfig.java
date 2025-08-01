package com.project.TalentFindr.config;


import com.project.TalentFindr.service.CustomUsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Autowired
    public CustomUsersDetailsService customUsersDetailsService;
    public CustomAuthenticationSucessHandler customAuthenticationSucessHandler;

    public WebSecurityConfig(CustomUsersDetailsService customUsersDetailsService, CustomAuthenticationSucessHandler customAuthenticationSucessHandler) {
        this.customUsersDetailsService = customUsersDetailsService;
        this.customAuthenticationSucessHandler = customAuthenticationSucessHandler;
    }

    public static final int CPU_COST = 16384;
    public static final int KEY_LENGTH = 256;
    public static final int SALT_LENGTH = 16;
    private final String[] publicUrl={
            "/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**",
            "/favicon.ico",
            "/resources/**",
            "/error",
            "/chat/thread",
            "/char/send",
            "/topic/thread",
            "/ws-chat/**",
            "/app/chat/send"
    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers(publicUrl).permitAll();
            auth.anyRequest().authenticated();
        });

        http.formLogin(form->form.loginPage("/login").permitAll().successHandler(customAuthenticationSucessHandler)).logout(logout->{logout.logoutUrl("/logout");
        logout.logoutSuccessUrl("/");
        }).cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUsersDetailsService);
        return authenticationProvider;
    }

    @Bean                                                    //telling spring security how we want to encrypt. plain text or using some encryption
    public PasswordEncoder passwordEncoder() {               //Scrypt not working for some reason
        return new BCryptPasswordEncoder();
    }


}
