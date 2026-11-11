-- Contas de demonstração (ambiente de teste).
-- teste@teste.com  / 123456teste  -> USER
-- admin@admin.com  / 123456admin  -> ADMIN
-- As senhas estão guardadas como hash BCrypt, igual ao cadastro normal.
INSERT INTO usuario (nome, email, senha, perfil) VALUES
                                                     ('Usuário de teste',      'teste@teste.com', '$2b$10$G5PtTUH8OTHpBX71B.p9z.sO7BNmawg9aHkFC9Nuvybbi2XmMdvhS', 'USER'),
                                                     ('Administrador de teste', 'admin@admin.com', '$2b$10$YAwT2mxUphHuXbKKMG2JN.sAr9eQxj3N3WVY5YvuF6SnKu4ZUqWz2', 'ADMIN')
    ON CONFLICT (email) DO UPDATE
                               SET nome   = EXCLUDED.nome,
                               senha  = EXCLUDED.senha,
                               perfil = EXCLUDED.perfil;