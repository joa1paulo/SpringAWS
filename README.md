# SpringAWS

API REST com Java e Spring Boot para gerenciamento de produtos, com autenticação JWT, banco PostgreSQL e deploy em nuvem.

**Demo:** https://springaws.onrender.com
> Hospedada no plano gratuito do Render, então a primeira requisição pode demorar alguns segundos.

## Funcionalidades
- Cadastro e login de usuários com senha criptografada (BCrypt)
- Autenticação com token JWT e proteção das rotas
- CRUD de produtos
- Validação de dados com DTOs e tratamento global de erros
- Dashboard web para testar a API

## Tecnologias
Java, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL, Flyway, Docker

## Como rodar localmente
```bash
git clone https://github.com/joa1paulo/SpringAWS
cd SpringAWS
docker compose up
```
Acesse: http://localhost:8080

## Endpoints principais
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /auth/cadastro | Cria um usuário |
| POST | /auth/login | Retorna o token JWT |
| GET | /produtos | Lista produtos |
| POST | /produtos | Cria produto |
| DELETE | /produtos/{id} | Remove produto |

## Autor
João Paulo Duarte Ramos
