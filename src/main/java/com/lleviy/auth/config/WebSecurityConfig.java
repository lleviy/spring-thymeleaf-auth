package com.lleviy.auth.config;

import com.lleviy.auth.repositories.UserRepository;
import com.lleviy.auth.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

    @Configuration
    @EnableWebSecurity
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private MyUserDetailsService myUserDetailsService;

        @Autowired
        public WebSecurityConfig(UserRepository userRepository, MyUserDetailsService myUserDetailsService) {
            this.myUserDetailsService = myUserDetailsService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/**", "/home", "/signup").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(myUserDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }
    }
