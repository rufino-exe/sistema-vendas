package sistemaVendas.aweb.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sistemaVendas.aweb.com.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}