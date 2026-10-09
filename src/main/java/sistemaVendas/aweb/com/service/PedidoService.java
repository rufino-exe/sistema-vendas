package sistemaVendas.aweb.com.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistemaVendas.aweb.com.dto.PedidoForm;
import sistemaVendas.aweb.com.dto.PedidoProdutoForm;
import sistemaVendas.aweb.com.model.Cliente;
import sistemaVendas.aweb.com.model.Pedido;
import sistemaVendas.aweb.com.model.PedidoItem;
import sistemaVendas.aweb.com.model.Produto;
import sistemaVendas.aweb.com.model.StatusPedido;
import sistemaVendas.aweb.com.repository.ClienteRepository;
import sistemaVendas.aweb.com.repository.PedidoRepository;
import sistemaVendas.aweb.com.repository.ProdutoRepository;
import sistemaVendas.aweb.com.security.SecurityUtil;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public void criar(PedidoForm form) {

        Cliente cliente = clienteRepository.findById(form.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = new Pedido();

        pedido.setCliente(cliente);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.ATIVO);

        List<PedidoItem> itens = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (PedidoProdutoForm produtoForm : form.getProdutos()) {

            if (!produtoForm.isSelecionado()) {
                continue;
            }

            if (produtoForm.getQuantidade() == null
                    || produtoForm.getQuantidade() <= 0) {
                throw new RuntimeException(
                        "Informe uma quantidade válida para os produtos selecionados."
                );
            }

            Produto produto = produtoRepository
                    .findById(produtoForm.getProdutoId())
                    .orElseThrow(() ->
                            new RuntimeException("Produto não encontrado")
                    );

            if (produtoForm.getQuantidade() > produto.getQuantidadeEmEstoque()) {
                throw new RuntimeException(
                        "Estoque insuficiente para o produto: "
                        + produto.getNome()
                );
            }

            PedidoItem item = new PedidoItem();

            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(produtoForm.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            itens.add(item);

            BigDecimal subtotal = produto.getPreco()
                    .multiply(
                            BigDecimal.valueOf(produtoForm.getQuantidade())
                    );

            total = total.add(subtotal);

            produto.setQuantidadeEmEstoque(
                    produto.getQuantidadeEmEstoque()
                    - produtoForm.getQuantidade()
            );

            produtoRepository.save(produto);
        }

        if (itens.isEmpty()) {
            throw new RuntimeException(
                    "Selecione pelo menos um produto."
            );
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);
        pedido.setCriadoPor(SecurityUtil.getCurrentUserEmail());
        pedido.setAtualizadoPor(SecurityUtil.getCurrentUserEmail());

        pedidoRepository.save(pedido);
    }

    @Transactional
    public void atualizar(Long id, PedidoForm form) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Pedido não encontrado")
                );

        if (pedido.getStatus() != StatusPedido.ATIVO) {
            throw new RuntimeException(
                    "Pedidos cancelados não podem ser alterados."
            );
        }

        /*
         * Primeiro devolvemos ao estoque tudo que pertencia
         * ao pedido antigo.
         */
        for (PedidoItem item : pedido.getItens()) {

            Produto produto = item.getProduto();

            produto.setQuantidadeEmEstoque(
                    produto.getQuantidadeEmEstoque()
                    + item.getQuantidade()
            );

            produtoRepository.save(produto);
        }

        Cliente cliente = clienteRepository
                .findById(form.getClienteId())
                .orElseThrow(() ->
                        new RuntimeException("Cliente não encontrado")
                );

        pedido.setCliente(cliente);

        pedido.getItens().clear();

        BigDecimal total = BigDecimal.ZERO;

        int quantidadeItens = 0;

        for (PedidoProdutoForm produtoForm : form.getProdutos()) {

            if (!produtoForm.isSelecionado()) {
                continue;
            }

            if (produtoForm.getQuantidade() == null
                    || produtoForm.getQuantidade() <= 0) {
                throw new RuntimeException(
                        "Informe uma quantidade válida."
                );
            }

            Produto produto = produtoRepository
                    .findById(produtoForm.getProdutoId())
                    .orElseThrow(() ->
                            new RuntimeException("Produto não encontrado")
                    );

            if (produtoForm.getQuantidade()
                    > produto.getQuantidadeEmEstoque()) {

                throw new RuntimeException(
                        "Estoque insuficiente para "
                        + produto.getNome()
                );
            }

            PedidoItem item = new PedidoItem();

            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(produtoForm.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            pedido.getItens().add(item);

            BigDecimal subtotal = produto.getPreco()
                    .multiply(
                            BigDecimal.valueOf(produtoForm.getQuantidade())
                    );

            total = total.add(subtotal);

            produto.setQuantidadeEmEstoque(
                    produto.getQuantidadeEmEstoque()
                    - produtoForm.getQuantidade()
            );

            produtoRepository.save(produto);

            quantidadeItens++;
        }

        if (quantidadeItens == 0) {
            throw new RuntimeException(
                    "O pedido deve possuir pelo menos um produto."
            );
        }

        pedido.setValorTotal(total);
        pedido.setAtualizadoPor(SecurityUtil.getCurrentUserEmail());

        pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelar(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Pedido não encontrado")
                );

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            return;
        }

        for (PedidoItem item : pedido.getItens()) {

            Produto produto = item.getProduto();

            produto.setQuantidadeEmEstoque(
                    produto.getQuantidadeEmEstoque()
                    + item.getQuantidade()
            );

            produtoRepository.save(produto);
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setAtualizadoPor(SecurityUtil.getCurrentUserEmail());

        pedidoRepository.save(pedido);
    }
}