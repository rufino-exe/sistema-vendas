package sistemaVendas.aweb.com.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import sistemaVendas.aweb.com.model.Cliente;
import sistemaVendas.aweb.com.service.ClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView(
                "cliente/list",
                Map.of("clientes", clienteService.listarTodos())
        );
    }

    @GetMapping("/novo")
    public ModelAndView create() {
        return new ModelAndView(
                "cliente/form",
                Map.of("cliente", new Cliente())
        );
    }

    @PostMapping("/novo")
    public String create(@Valid Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return "cliente/form";
        }

        clienteService.salvar(cliente);

        return "redirect:/cliente";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var optionalCliente = clienteService.buscarPorId(id);

        if (optionalCliente.isPresent()) {
            return new ModelAndView(
                    "cliente/form",
                    Map.of("cliente", optionalCliente.get())
            );
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid Cliente cliente,
            BindingResult result) {

        if (result.hasErrors()) {
            return "cliente/form";
        }

        clienteService.atualizar(id, cliente);

        return "redirect:/cliente";
    }

    @GetMapping("/delete/{id}")
public ModelAndView deleteForm(@PathVariable Long id) {
    var optionalCliente = clienteService.buscarPorId(id);

    if (optionalCliente.isPresent()) {
        return new ModelAndView(
                "cliente/delete",
                Map.of("cliente", optionalCliente.get())
        );
    }

    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
}

@PostMapping("/delete/{id}")
public String deleteConfirm(@PathVariable Long id) {
    clienteService.excluir(id);

    return "redirect:/cliente";
}
}