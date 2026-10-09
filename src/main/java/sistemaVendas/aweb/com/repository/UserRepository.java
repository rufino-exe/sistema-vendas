package sistemaVendas.aweb.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistemaVendas.aweb.com.model.User;

import java.util.Optional;

/**
 * Repository para a entidade User
 * Fornece métodos de acesso aos dados de usuários no banco de dados
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Buscar usuário por email
     * @param email email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Verificar se um email já existe no sistema
     * @param email email a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);

    /**
     * Buscar usuários ativos
     * @return lista de usuários ativos
     */
    java.util.List<User> findByAtivoTrue();
}
