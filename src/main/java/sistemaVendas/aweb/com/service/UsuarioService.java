package sistemaVendas.aweb.com.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistemaVendas.aweb.com.model.Usuario;
import sistemaVendas.aweb.com.model.UsuarioRole;
import sistemaVendas.aweb.com.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    /** Cadastra o usuário gravando sempre a senha com hash BCrypt. */
    @Transactional
    public Usuario cadastrar(String nome, String email, String senhaPura, UsuarioRole role) {
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + email);
        }
        Usuario usuario = new Usuario(nome, email.trim().toLowerCase(),
                passwordEncoder.encode(senhaPura), role);
        return usuarioRepository.save(usuario);
    }
}