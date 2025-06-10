package sistema.votacao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuario/cadastro", "/usuario/login").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean // Indica que o método produz um bean a ser gerenciado pelo Spring
    public BCryptPasswordEncoder passwordEncoder() {
        // Retorna uma nova instância de BCryptPasswordEncoder.
        // O Spring irá gerenciar esta instância e injetá-la onde for solicitada.
        return new BCryptPasswordEncoder();
    }



}
