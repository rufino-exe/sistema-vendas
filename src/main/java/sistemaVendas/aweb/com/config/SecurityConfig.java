package sistemaVendas.aweb.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


/**
 * Configuração de Segurança da Aplicação
 * Define autenticação, autorização, CSRF, sessão e permissões por endpoint
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configuração de autenticação e criptografia de senha
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        /**
     * Provider de autenticação DAO (Ajustado para evitar conflitos de versão)
     */
        @Bean
    public org.springframework.security.authentication.AuthenticationProvider authenticationProvider() {
        var authProvider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider(userDetailsService);
        // O userDetailsService já foi configurado acima! Apenas defina a criptografia da senha abaixo:
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    /**
     * Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configuração do FilterChain de segurança
     * Define regras de acesso, login, logout e proteção CSRF
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Ajustado: Libera recursos estáticos da raiz real (css, js, imagens) sem travar o layout
                        .requestMatchers("/", "/login", "/acesso-negado", "/error/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/favicon.ico").permitAll()
                        
                        // Admin: acesso total
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        
                        // Clientes: apenas ADMIN e GERENTE
                        .requestMatchers("/clientes/**").hasAnyRole("ADMIN", "GERENTE")
                        
                        // Produtos: apenas ADMIN e GERENTE
                        .requestMatchers("/produtos/**").hasAnyRole("ADMIN", "GERENTE")
                        
                        // Pedidos: VENDEDOR, GERENTE e ADMIN
                        .requestMatchers("/pedidos/**").hasAnyRole("VENDEDOR", "GERENTE", "ADMIN")
                        
                        // Qualquer outro request requer autenticação
                        .anyRequest().authenticated()
                )
                
                // Configuração de Login
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                )
                
                // Configuração de Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                
                // Tratamento de acesso negado (403)
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acesso-negado")
                )
                
                // Configuração de sessão
                .sessionManagement(session -> session
                        // Ajustado para o padrão fluído do Spring Security 6
                        .sessionFixation(fixation -> fixation.migrateSession())
                        
                        // Máximo 1 sessão por usuário
                        .maximumSessions(1)
                        .expiredUrl("/login?expired")
                )
                
                // CSRF Protection (Perfeito para Thymeleaf)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                );
        
        // Adicionar custom provider
        http.authenticationProvider(authenticationProvider());
        
        return http.build();
    }
}
