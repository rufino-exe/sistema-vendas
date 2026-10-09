package sistemaVendas.aweb.com.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistemaVendas.aweb.com.model.Role;
import sistemaVendas.aweb.com.model.User;
import sistemaVendas.aweb.com.repository.RoleRepository;
import sistemaVendas.aweb.com.repository.UserRepository;
import sistemaVendas.aweb.com.security.SecurityUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Serviço de Usuário
 * Gerencia operações de CRUD e negócio relacionadas a usuários
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Criar novo usuário com roles específicas
     * @param email email do usuário
     * @param senha senha em texto plano (será criptografada)
     * @param roles conjunto de roles a atribuir
     * @return usuário criado
     * @throws IllegalArgumentException se email já existe
     */
    @Transactional
    public User criarUsuario(String email, String senha, Set<Role> roles) {
        
        // Validar se email já existe
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }
        
        // Validar entrada
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Pelo menos uma role é obrigatória");
        }
        
        // Criar novo usuário
        User user = new User();
        user.setEmail(email.toLowerCase().trim());
        user.setSenha(passwordEncoder.encode(senha)); // Criptografar senha
        user.setAtivo(true);
        user.setRoles(roles);
        user.setCriadoEm(LocalDateTime.now());
        user.setAtualizadoEm(LocalDateTime.now());
        user.setCriadoPor(SecurityUtil.getCurrentUserEmail() != null ? 
            SecurityUtil.getCurrentUserEmail() : "SISTEMA");
        
        return userRepository.save(user);
    }

    /**
     * Buscar usuário por email
     * @param email email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email.toLowerCase().trim());
    }

    /**
     * Buscar usuário por ID
     * @param id id do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Listar todos os usuários
     * @return lista de usuários
     */
    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    /**
     * Listar usuários ativos
     * @return lista de usuários com status ativo = true
     */
    public List<User> listarAtivos() {
        return userRepository.findByAtivoTrue();
    }

    /**
     * Atualizar dados do usuário (sem alterar senha)
     * @param id id do usuário
     * @param usuarioAtualizado dados atualizados
     * @return usuário atualizado
     * @throws IllegalArgumentException se usuário não encontrado
     */
    @Transactional
    public User atualizar(Long id, User usuarioAtualizado) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        // Atualizar campos (não atualizar senha aqui)
        if (usuarioAtualizado.getEmail() != null && 
            !usuarioAtualizado.getEmail().equals(user.getEmail())) {
            
            // Validar novo email
            if (userRepository.existsByEmail(usuarioAtualizado.getEmail())) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            user.setEmail(usuarioAtualizado.getEmail().toLowerCase().trim());
        }
        
        if (usuarioAtualizado.getAtivo() != null) {
            user.setAtivo(usuarioAtualizado.getAtivo());
        }
        
        if (usuarioAtualizado.getRoles() != null && !usuarioAtualizado.getRoles().isEmpty()) {
            user.setRoles(usuarioAtualizado.getRoles());
        }
        
        user.setAtualizadoEm(LocalDateTime.now());
        user.setAtualizadoPor(SecurityUtil.getCurrentUserEmail() != null ? 
            SecurityUtil.getCurrentUserEmail() : "SISTEMA");
        
        return userRepository.save(user);
    }

    /**
     * Alterar senha do usuário
     * @param id id do usuário
     * @param senhaAtual senha atual (para validação)
     * @param novaSenha nova senha em texto plano
     * @throws IllegalArgumentException se senha atual está incorreta
     */
    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        // Validar senha atual
        if (!passwordEncoder.matches(senhaAtual, user.getSenha())) {
            throw new IllegalArgumentException("Senha atual está incorreta");
        }
        
        // Validar nova senha
        if (novaSenha == null || novaSenha.isBlank() || novaSenha.length() < 6) {
            throw new IllegalArgumentException("Nova senha deve ter pelo menos 6 caracteres");
        }
        
        user.setSenha(passwordEncoder.encode(novaSenha));
        user.setAtualizadoEm(LocalDateTime.now());
        user.setAtualizadoPor(SecurityUtil.getCurrentUserEmail() != null ? 
            SecurityUtil.getCurrentUserEmail() : "SISTEMA");
        
        userRepository.save(user);
    }

    /**
     * Ativar/Desativar usuário
     * @param id id do usuário
     * @param ativo true para ativar, false para desativar
     */
    @Transactional
    public void ativarDesativar(Long id, Boolean ativo) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        user.setAtivo(ativo);
        user.setAtualizadoEm(LocalDateTime.now());
        user.setAtualizadoPor(SecurityUtil.getCurrentUserEmail() != null ? 
            SecurityUtil.getCurrentUserEmail() : "SISTEMA");
        
        userRepository.save(user);
    }

    /**
     * Adicionar role a um usuário
     * @param userId id do usuário
     * @param roleEnum nome da role
     */
    @Transactional
    public void adicionarRole(Long userId, Role.RoleEnum roleEnum) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + userId));
        
        Role role = roleRepository.findByNome(roleEnum)
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + roleEnum));
        
        user.getRoles().add(role);
        user.setAtualizadoEm(LocalDateTime.now());
        user.setAtualizadoPor(SecurityUtil.getCurrentUserEmail() != null ? 
            SecurityUtil.getCurrentUserEmail() : "SISTEMA");
        
        userRepository.save(user);
    }

    /**
     * Remover role de um usuário
     * @param userId id do usuário
     * @param roleEnum nome da role
     */
    @Transactional
    public void removerRole(Long userId, Role.RoleEnum roleEnum) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + userId));
        
        Role role = roleRepository.findByNome(roleEnum)
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + roleEnum));
        
        user.getRoles().remove(role);
        user.setAtualizadoEm(LocalDateTime.now());
        user.setAtualizadoPor(SecurityUtil.getCurrentUserEmail() != null ? 
            SecurityUtil.getCurrentUserEmail() : "SISTEMA");
        
        userRepository.save(user);
    }

    /**
     * Excluir usuário (exclusão física - cuidado!)
     * @param id id do usuário
     */
    @Transactional
    public void excluir(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Criar usuário com role simples (helper method)
     * @param email email do usuário
     * @param senha senha em texto plano
     * @param roleEnum role a atribuir
     * @return usuário criado
     */
    @Transactional
    public User criarUsuarioComRole(String email, String senha, Role.RoleEnum roleEnum) {
        Role role = roleRepository.findByNome(roleEnum)
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada: " + roleEnum));
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        return criarUsuario(email, senha, roles);
    }
}
