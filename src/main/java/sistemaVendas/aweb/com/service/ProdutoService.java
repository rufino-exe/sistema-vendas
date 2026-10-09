package sistemaVendas.aweb.com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sistemaVendas.aweb.com.model.Produto;
import sistemaVendas.aweb.com.repository.ProdutoRepository;
import sistemaVendas.aweb.com.security.SecurityUtil;


/**
 * ProdutoService
 */

@Service 
public class ProdutoService {
   // @Autowired 
   // private ProdutoRepository produtoRepository;
    
   private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // CREATE
    @Transactional 
    public Produto salvar(Produto produto) {
        produto.setCriadoPor(SecurityUtil.getCurrentUserEmail());
        produto.setAtualizadoPor(SecurityUtil.getCurrentUserEmail());
        return produtoRepository.save(produto);
    }

    // READ
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // UPDATE
    @Transactional
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        var optionalProduto = buscarPorId(id);
        if (!optionalProduto.isPresent())
            throw new IllegalArgumentException("Produto não encontrado."); //personalizar exception

        var produtoExistente = optionalProduto.get();

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setQuantidadeEmEstoque(produtoAtualizado.getQuantidadeEmEstoque());
        produtoExistente.setAtualizadoPor(SecurityUtil.getCurrentUserEmail());

        var produtoSalvo = produtoRepository.save(produtoExistente);
        return produtoSalvo;

    }

    // DELETE
    @Transactional
    public void excluir(Long id) {
        var optionalProduto = buscarPorId(id);
        if (!optionalProduto.isPresent())
            throw new IllegalArgumentException("Produto não encontrado.");

        produtoRepository.deleteById(id);
    }

}