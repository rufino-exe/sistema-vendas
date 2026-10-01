package sistemaVendas.aweb.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sistemaVendas.aweb.com.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}