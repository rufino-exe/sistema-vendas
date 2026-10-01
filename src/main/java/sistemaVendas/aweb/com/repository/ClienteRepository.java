package sistemaVendas.aweb.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sistemaVendas.aweb.com.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}