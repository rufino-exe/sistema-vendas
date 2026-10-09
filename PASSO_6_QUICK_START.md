# 🎯 PRÓXIMAS AÇÕES - PASSO 6 (QUICK START)

## ⚡ Resumo Rápido

**Está pronto para o Passo 6?** Sim! ✅

**O que fazer agora?**
```
Adicionar @PreAuthorize em 3 Controllers:
  • ClienteController
  • ProdutoController  
  • PedidoController

Tempo estimado: 20-30 minutos
Dificuldade: ⭐ (Muito fácil)
Importância: 🔴 (Crítica)
```

---

## 📋 Passo 6 - Checklist

### Para ClienteController:
```java
@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String list() { ... }  // GET /clientes

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String create() { ... } // GET /clientes/novo

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String create(ClienteForm form) { ... } // POST /clientes

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String edit(Long id) { ... } // GET /clientes/{id}/editar

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String edit(Long id, ClienteForm form) { ... } // POST /clientes/{id}

@PreAuthorize("hasRole('ADMIN')")
public String deleteForm(Long id) { ... } // GET /clientes/{id}/excluir

@PreAuthorize("hasRole('ADMIN')")
public String delete(Long id) { ... } // POST /clientes/{id}/excluir
```

### Para ProdutoController:
```java
@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String list() { ... }  // GET /produtos

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String create() { ... } // GET /produtos/novo

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String create(ProdutoForm form) { ... } // POST /produtos

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String edit(Long id) { ... } // GET /produtos/{id}/editar

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String edit(Long id, ProdutoForm form) { ... } // POST /produtos/{id}

@PreAuthorize("hasRole('ADMIN')")
public String delete(Long id) { ... } // GET /produtos/{id}/excluir

@PreAuthorize("hasRole('ADMIN')")
public String delete(Long id, Model model) { ... } // POST /produtos/{id}/excluir
```

### Para PedidoController:
```java
@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
public String list() { ... }  // GET /pedidos

@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
public String create() { ... } // GET /pedidos/novo

@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
public String create(PedidoForm form) { ... } // POST /pedidos

@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
public String edit(Long id) { ... } // GET /pedidos/{id}/editar

@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE', 'ADMIN')")
public String edit(Long id, PedidoForm form) { ... } // POST /pedidos/{id}

@PreAuthorize("hasRole('ADMIN')")
public String cancelarForm(Long id) { ... } // GET /pedidos/{id}/cancelar

@PreAuthorize("hasRole('ADMIN')")
public String cancelar(Long id) { ... } // POST /pedidos/{id}/cancelar
```

---

## 🔧 Como Implementar Passo 6

### 1. Adicionar Importação em ClienteController:
```java
import org.springframework.security.access.prepost.PreAuthorize;
```

### 2. Adicionar @PreAuthorize antes de cada método

### 3. Fazer o mesmo em ProdutoController e PedidoController

### 4. Testar:
```bash
# Login como VENDEDOR
# Tentar acessar /clientes → Erro 403 ✓
# Tentar acessar /pedidos → Funciona ✓
```

---

## ⏱️ Tempo Estimado

- Adicionar imports: 3 minutos
- ClienteController: 10 minutos
- ProdutoController: 8 minutos
- PedidoController: 8 minutos
- Testes: 5 minutos

**Total: 30-35 minutos**

---

## 📝 Depois do Passo 6

Você estará pronto para:
- ✅ Passo 7: Implementar auditoria em Services (capturar usuário logado)
- ✅ Passo 8: Adicionar logging centralizado
- ✅ Passo 9: Parametrizar properties
- ✅ Passo 10: Email notificação

---

## 💡 Dicas

1. **@PreAuthorize vs @Secured**
   - Use @PreAuthorize (mais flexível, suporta SpEL)
   - @Secured é legacy

2. **Múltiplas Roles**
   ```java
   @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
   ```

3. **Teste Rápido**
   ```bash
   # Login como vendedor
   # Erro 403 = @PreAuthorize funcionando ✓
   ```

---

## 🚀 Comando Final

```
VA PRO PASSO 6! IMPLEMENTA @PREAUTHORIZE NOS CONTROLLERS
```

---

**Status Atual**: ✅ 50% (Passos 1-5 completos)
**Próximo Passo**: Passo 6 (@PreAuthorize) - 20-30 min
**Tempo Total Fases 1-5**: ~4-5 horas
**Tempo Total Projeto**: ~10-12 horas (todas as 10 fases)

Bora? 🚀
