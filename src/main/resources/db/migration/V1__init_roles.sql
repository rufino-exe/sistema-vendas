-- Script para criar dados iniciais de ROLES no banco de dados
-- Execute este script após primeira execução da aplicação

-- Inserir roles padrão (execute uma única vez)
INSERT INTO roles (nome, descricao) VALUES 
('ADMIN', 'Administrador - Acesso total'),
('VENDEDOR', 'Vendedor - Criar e editar pedidos'),
('GERENTE', 'Gerente - Gerenciar produtos e relatórios')
ON CONFLICT (nome) DO NOTHING;

-- Criar primeiro usuário ADMIN (opcional - descomente e adapte)
-- INSERT INTO usuarios (email, senha, ativo, criado_em, atualizado_em)
-- VALUES ('admin@sistemavenda.com', '$2a$10$...', true, NOW(), NOW());
-- 
-- INSERT INTO usuario_roles (usuario_id, role_id)
-- SELECT u.id, r.id FROM usuarios u, roles r 
-- WHERE u.email = 'admin@sistemavenda.com' AND r.nome = 'ADMIN';
