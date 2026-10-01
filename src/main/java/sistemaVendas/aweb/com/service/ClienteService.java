package sistemaVendas.aweb.com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sistemaVendas.aweb.com.model.Cliente;
import sistemaVendas.aweb.com.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow();

        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setTelefone(cliente.getTelefone());

        return clienteRepository.save(clienteExistente);
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }
}