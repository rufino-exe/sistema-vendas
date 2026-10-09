package sistemaVendas.aweb.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistemaVendas.aweb.com.model.Role;

import java.util.Optional;

/**
 * Repository para a entidade Role
 * Fornece métodos de acesso aos dados de roles no banco de dados
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Buscar role por nome
     * @param nome nome da role (enum RoleEnum)
     * @return Optional contendo a role se encontrada
     */
    Optional<Role> findByNome(Role.RoleEnum nome);
}
