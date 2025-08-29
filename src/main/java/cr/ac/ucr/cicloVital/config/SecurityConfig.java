package cr.ac.ucr.cicloVital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // usa el bean CorsConfigurationSource
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/publico/**").permitAll()
                        .anyRequest().permitAll() // ajusta según tu caso
                );
        return http.build();
    }
}
