package LMC.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
    @EnableWebSecurity(debug = true)
    public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authProvider;

        @Autowired
        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authProvider) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
            this.authProvider = authProvider;
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(customizeRequests -> {
                        customizeRequests
                                .requestMatchers("/auth/**")
                                .permitAll()
                                .anyRequest().authenticated();

                                //.authenticated();
                    }
            ).csrf(AbstractHttpConfigurer::disable);
                    //.httpBasic(Customizer.withDefaults());

            //http.authorizeHttpRequests().anyRequest().permitAll();

            return http.sessionManagement(sessionManager->
                    sessionManager
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authProvider)
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();

        }
    }
