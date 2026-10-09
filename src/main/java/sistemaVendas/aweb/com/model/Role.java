package sistemaVendas.aweb.com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade Role (Perfil de Usuário)
 * Define os papéis/permissões no sistema: ADMIN, VENDEDOR, GERENTE
 */
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleEnum nome;

    @Column(length = 255)
    private String descricao;

    /**
     * Enum dos tipos de role disponíveis
     */
    public enum RoleEnum {
        ADMIN("Administrador - Acesso total"),
        VENDEDOR("Vendedor - Criar e editar pedidos"),
        GERENTE("Gerente - Gerenciar produtos e relatórios");

        private final String descricao;

        RoleEnum(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getNome() {
        return nome;
    }

    public void setNome(RoleEnum nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", nome=" + nome +
                '}';
    }
}
