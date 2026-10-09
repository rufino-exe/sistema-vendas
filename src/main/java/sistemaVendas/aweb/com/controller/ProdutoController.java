package sistemaVendas.aweb.com.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import sistemaVendas.aweb.com.model.Produto;
import sistemaVendas.aweb.com.service.ProdutoService;

/**
 * ProdutoController
 */
@Controller 
@RequestMapping ("/produto")
public class ProdutoController {
    
    private final ProdutoService produtoService;
    
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Listar produtos
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("produto/list", Map.of("produtos", produtoService.listarTodos()));
    }

    // Formulário de cadastro
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/novo")
    public ModelAndView create() {
        return new ModelAndView("produto/form", Map.of("produto", new Produto()));
    }

    // Salvar produto
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/novo")
    public String create(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return "produto/form";
        }
        produtoService.salvar(produto);
        return "redirect:/produto";
    }

    // Formulário de edição
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var optionalProduto = produtoService.buscarPorId(id);
        if (optionalProduto.isPresent()) {
            return new ModelAndView("produto/form", Map.of("produto", optionalProduto.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // Atualizar produto
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/edit/{id}")
    public String edit(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return "produto/form";
        }

        produtoService.atualizar(produto.getId(), produto);

        return "redirect:/produto";
    }

    // Excluir produto
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        var optionalProduto = produtoService.buscarPorId(id);
        if (optionalProduto.isPresent()) {
            return new ModelAndView("produto/delete", Map.of("produto", optionalProduto.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String delete(Produto produto) {
        produtoService.excluir(produto.getId());
        return "redirect:/produto";
    }


}