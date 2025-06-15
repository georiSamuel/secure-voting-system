package sistema.votacao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configura a segurança web para a aplicação do sistema de votação.
 * <p>
 * Esta classe utiliza o Spring Security para definir as regras de acesso aos
 * endpoints HTTP e para configurar o bean de codificação de senhas.
 *
 * @author Lethycia
 * @author Georis
 * @author Suelle
 * @version 1.0
 * @since 10/05/2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define a cadeia de filtros de segurança que protege os endpoints da aplicação.
     * <p>
     * As seguintes regras são aplicadas:
     * <ul>
     * <li>CSRF (Cross-Site Request Forgery) é desabilitado.</li>
     * <li>Endpoints "/usuario/cadastro" e "/usuario/login" são públicos.</li>
     * <li>Qualquer outra requisição exige autenticação.</li>
     * </ul>
     *
     * @param http O objeto HttpSecurity a ser configurado.
     * @return A cadeia de filtros de segurança construída.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuario/cadastro", "/usuario/login").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    /**
     * Cria um bean gerenciado pelo Spring para a codificação de senhas.
     * <p>
     * Utiliza o BCrypt, um algoritmo ROBUSTO e padrão da indústria para hashing de senhas.
     * Este bean pode ser injetado em outras partes da aplicação para garantir o
     * armazenamento seguro das credenciais dos usuários.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}