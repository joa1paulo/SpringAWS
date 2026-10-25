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

É necessário ter o Docker com Docker Compose instalado. Na raiz do projeto, execute:

```bash
git clone https://github.com/joa1paulo/SpringAWS
cd SpringAWS
docker compose up --build
```

O Compose compila a aplicação com Maven dentro do Docker, cria o banco `springaws` no PostgreSQL e inicia a API na porta `8080`. O banco fica disponível na porta `5432`. Acesse a aplicação em http://localhost:8080.

Para encerrar os serviços, pressione `Ctrl+C` e execute `docker compose down`. Os dados do banco são mantidos no volume do Docker entre inicializações. As credenciais padrão do Compose são apenas para desenvolvimento local; configure `DB_PASSWORD` e `JWT_SECRET` antes de usar em outros ambientes.

Se a API falhar com `password authentication failed for user "app"`, o volume do PostgreSQL pode ter sido criado anteriormente com outra senha. Se não precisar dos dados locais, recrie o banco e inicie os serviços:

```bash
docker compose down -v
docker compose up --build
```

`docker compose down -v` apaga os dados do banco armazenados no volume. Para preservá-los, configure `DB_PASSWORD` com a senha usada quando o volume foi criado, tanto para o PostgreSQL quanto para a aplicação.

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
