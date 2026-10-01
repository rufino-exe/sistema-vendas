package sistemaVendas.aweb.com.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "produtos")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@EqualsAndHashCode (of = "id")

public class Produto {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Nome é obrigatório")
    @Column (nullable = false, length = 100)
    private String nome;

    @NotBlank (message = "Descrição é obrigatório")
    @Column (nullable = false, length = 255)
    private String descricao;

    @Positive (message = "Preço deve ser maior que zero")
    @Column (nullable = false)
    private BigDecimal preco;

    @PositiveOrZero (message = "Quantidade em estoque deve ser maior ou igual a zero")
    @Column (nullable = false)
    private Integer quantidadeEmEstoque;

    public Long getId() {
    return id;
}

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setQuantidadeEmEstoque(Integer quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }
        
    }