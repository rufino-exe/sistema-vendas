package sistemaVendas.aweb.com.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import sistemaVendas.aweb.com.dto.PedidoForm;
import sistemaVendas.aweb.com.dto.PedidoProdutoForm;
import sistemaVendas.aweb.com.model.StatusPedido;
import sistemaVendas.aweb.com.repository.ClienteRepository;
import sistemaVendas.aweb.com.repository.ProdutoRepository;
import sistemaVendas.aweb.com.service.PedidoService;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(
            PedidoService pedidoService,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.pedidoService = pedidoService;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
    @GetMapping
    public ModelAndView list() {

        return new ModelAndView(
                "pedido/list",
                Map.of("pedidos", pedidoService.listarTodos())
        );
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
    @GetMapping("/novo")
    public ModelAndView create() {

        PedidoForm form = montarFormulario();

        return new ModelAndView(
                "pedido/form",
                Map.of(
                        "pedidoForm", form,
                        "clientes", clienteRepository.findAll()
                )
        );
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
    @PostMapping("/novo")
    public ModelAndView create(PedidoForm pedidoForm) {

        try {

            pedidoService.criar(pedidoForm);

            return new ModelAndView("redirect:/pedido");

        } catch (RuntimeException e) {

            return new ModelAndView(
                    "pedido/form",
                    Map.of(
                            "pedidoForm", pedidoForm,
                            "clientes", clienteRepository.findAll(),
                            "erro", e.getMessage()
                    )
            );
        }
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {

        var optionalPedido = pedidoService.buscarPorId(id);

        if (optionalPedido.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var pedido = optionalPedido.get();

        if (pedido.getStatus() != StatusPedido.ATIVO) {
            return new ModelAndView("redirect:/pedido");
        }

        PedidoForm form = montarFormulario();

        form.setClienteId(pedido.getCliente().getId());

        for (var produtoForm : form.getProdutos()) {

            for (var item : pedido.getItens()) {

                if (item.getProduto().getId()
                        .equals(produtoForm.getProdutoId())) {

                    produtoForm.setSelecionado(true);
                    produtoForm.setQuantidade(
                            item.getQuantidade()
                    );

                    /*
                     * Mostra também o estoque que já está
                     * reservado nesse pedido.
                     */
                    produtoForm.setEstoque(
                            produtoForm.getEstoque()
                            + item.getQuantidade()
                    );
                }
            }
        }

        return new ModelAndView(
                "pedido/form",
                Map.of(
                        "pedidoForm", form,
                        "clientes", clienteRepository.findAll(),
                        "pedidoId", pedido.getId()
                )
        );
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
    @PostMapping("/edit/{id}")
    public ModelAndView edit(
            @PathVariable Long id,
            PedidoForm pedidoForm) {

        try {

            pedidoService.atualizar(id, pedidoForm);

            return new ModelAndView("redirect:/pedido");

        } catch (RuntimeException e) {

            return new ModelAndView(
                    "pedido/form",
                    Map.of(
                            "pedidoForm", pedidoForm,
                            "clientes", clienteRepository.findAll(),
                            "pedidoId", id,
                            "erro", e.getMessage()
                    )
            );
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cancelar/{id}")
    public ModelAndView cancelarForm(@PathVariable Long id) {

        var pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)
                );

        return new ModelAndView(
                "pedido/cancelar",
                Map.of("pedido", pedido)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {

        pedidoService.cancelar(id);

        return "redirect:/pedido";
    }

    private PedidoForm montarFormulario() {

        PedidoForm form = new PedidoForm();

        var lista = new ArrayList<PedidoProdutoForm>();

        for (var produto : produtoRepository.findAll()) {

            PedidoProdutoForm item = new PedidoProdutoForm();

            item.setProdutoId(produto.getId());
            item.setNomeProduto(produto.getNome());
            item.setEstoque(produto.getQuantidadeEmEstoque());
            item.setQuantidade(1);

            lista.add(item);
        }

        form.setProdutos(lista);

        return form;
    }
}