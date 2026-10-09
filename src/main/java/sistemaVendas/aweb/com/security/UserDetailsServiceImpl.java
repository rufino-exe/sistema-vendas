package sistemaVendas.aweb.com.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sistemaVendas.aweb.com.repository.UserRepository;

import java.util.stream.Collectors;

/**
 * Implementação customizada de UserDetailsService
 * Busca usuários no banco de dados e carrega seus dados de autenticação
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carrega usuário por email (username)
     * @param email email do usuário (usado como username)
     * @return UserDetails para autenticação Spring Security
     * @throws UsernameNotFoundException se usuário não encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // Buscar usuário por email no banco de dados
        sistemaVendas.aweb.com.model.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado com email: " + email
                ));

        // Verificar se usuário está ativo
        if (!user.getAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }

        // Converter roles para GrantedAuthority (formato esperado por Spring Security)
        // Adiciona prefixo "ROLE_" necessário para @PreAuthorize e @Secured
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNome().name()))
                .collect(Collectors.toList());

        // Retornar UserDetails com credenciais e autoridades
        return User.builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getAtivo())
                .build();
    }
}
