package sistemaVendas.aweb.com.dto;

import java.util.ArrayList;
import java.util.List;

public class PedidoForm {

    private Long clienteId;

    private List<PedidoProdutoForm> produtos = new ArrayList<>();

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<PedidoProdutoForm> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<PedidoProdutoForm> produtos) {
        this.produtos = produtos;
    }
}