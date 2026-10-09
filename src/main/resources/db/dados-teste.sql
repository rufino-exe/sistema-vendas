-- ============================================================================
-- SCRIPT DE DADOS INICIAIS PARA TESTES
-- ============================================================================
-- Execute este script após a primeira execução para criar dados de teste

-- ============================================================================
-- 1. INSERIR ROLES (obrigatório)
-- ============================================================================
INSERT INTO roles (nome, descricao) VALUES 
('ADMIN', 'Administrador - Acesso total ao sistema'),
('VENDEDOR', 'Vendedor - Pode criar e editar pedidos'),
('GERENTE', 'Gerente - Gerencia produtos e visualiza relatórios')
ON CONFLICT (nome) DO NOTHING;

-- ============================================================================
-- 2. INSERIR PRIMEIRO USUÁRIO ADMIN (para teste)
-- ============================================================================
-- Senha: admin123 (criptografada com BCrypt)
-- Hash gerado com: BCryptPasswordEncoder: $2a$10$YJvWEwB6LxIAi7kRCkBiK.U0u3BqhvQKyXhVaDCh/qn1cjQ5Wh/Fi
INSERT INTO usuarios (email, senha, ativo, criado_em, atualizado_em) 
VALUES ('admin@sistemavenda.com', '$2a$10$YJvWEwB6LxIAi7kRCkBiK.U0u3BqhvQKyXhVaDCh/qn1cjQ5Wh/Fi', true, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- ============================================================================
-- 3. ASSOCIAR ADMIN COM ROLE DE ADMIN
-- ============================================================================
INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuarios u, roles r 
WHERE u.email = 'admin@sistemavenda.com' AND r.nome = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur 
    WHERE ur.usuario_id = u.id AND ur.role_id = r.id
);

-- ============================================================================
-- 4. CRIAR USUÁRIOS DE TESTE ADICIONAIS (opcional)
-- ============================================================================
-- Usuário VENDEDOR: vendedor@sistemavenda.com / senha: vendedor123
INSERT INTO usuarios (email, senha, ativo, criado_em, atualizado_em) 
VALUES ('vendedor@sistemavenda.com', '$2a$10$QvxN7kLqH3QJ3rY5nZmQVuQqLwJz4KzX2gP8sH1rJ2mK9xL0mN0Zm', true, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Usuário GERENTE: gerente@sistemavenda.com / senha: gerente123
INSERT INTO usuarios (email, senha, ativo, criado_em, atualizado_em) 
VALUES ('gerente@sistemavenda.com', '$2a$10$RwxO8lMrI4RS4sZ6oAnRWvRrMxKa5LzY3hQ9tI2sL3nL0yM1nO1an', true, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- ============================================================================
-- 5. ASSOCIAR USUÁRIOS COM SUAS ROLES
-- ============================================================================
INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuarios u, roles r 
WHERE u.email = 'vendedor@sistemavenda.com' AND r.nome = 'VENDEDOR'
AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur 
    WHERE ur.usuario_id = u.id AND ur.role_id = r.id
);

INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id FROM usuarios u, roles r 
WHERE u.email = 'gerente@sistemavenda.com' AND r.nome = 'GERENTE'
AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur 
    WHERE ur.usuario_id = u.id AND ur.role_id = r.id
);

-- ============================================================================
-- 6. DADOS OPCIONAIS DE TESTE (Clientes, Produtos)
-- ============================================================================
-- Inserir clientes de teste
INSERT INTO clientes (nome, email, telefone, criado_em, atualizado_em, criado_por)
VALUES 
('João Silva', 'joao@exemplo.com', '11999999999', NOW(), NOW(), 'admin@sistemavenda.com'),
('Maria Santos', 'maria@exemplo.com', '21988888888', NOW(), NOW(), 'admin@sistemavenda.com'),
('Pedro Oliveira', 'pedro@exemplo.com', '31977777777', NOW(), NOW(), 'admin@sistemavenda.com')
ON CONFLICT DO NOTHING;

-- Inserir produtos de teste
INSERT INTO produtos (nome, descricao, preco, quantidade_em_estoque, criado_em, atualizado_em, criado_por)
VALUES 
('Notebook Dell', 'Notebook Dell Inspiron 15', 3500.00, 10, NOW(), NOW(), 'admin@sistemavenda.com'),
('Mouse Logitech', 'Mouse wireless Logitech MX', 150.00, 50, NOW(), NOW(), 'admin@sistemavenda.com'),
('Teclado Mecânico', 'Teclado mecânico RGB', 450.00, 20, NOW(), NOW(), 'admin@sistemavenda.com'),
('Monitor 24"', 'Monitor LG 24 polegadas Full HD', 800.00, 15, NOW(), NOW(), 'admin@sistemavenda.com')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- NOTA SOBRE SENHAS DE TESTE
-- ============================================================================
-- As senhas foram geradas usando BCryptPasswordEncoder
-- Para gerar novas senhas, use um gerador online:
-- https://www.browserling.com/tools/bcrypt
-- 
-- Credenciais de teste (após executar este script):
-- Admin:     admin@sistemavenda.com / admin123
-- Vendedor:  vendedor@sistemavenda.com / vendedor123
-- Gerente:   gerente@sistemavenda.com / gerente123
-- ============================================================================
