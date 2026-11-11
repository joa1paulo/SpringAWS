# SpringAWS: API de produtos com autenticação JWT

API REST de gerenciamento de produtos, feita com Spring Boot, com cadastro e login de usuários, perfis de acesso (`USER` e `ADMIN`) e um painel web para usar a API direto do navegador.

O projeto foi construído em etapas para praticar o que é comum em back-end no mercado: autenticação stateless com JWT, validação de dados, tratamento padronizado de erros, migrações de banco versionadas, Docker e deploy.

## Demonstração online

**https://springaws.onrender.com**

A hospedagem é gratuita, então o primeiro acesso depois de um período parado pode levar cerca de um minuto para carregar.

### Contas de teste

A tela de login mostra estas contas prontas para uso:

| Perfil | E-mail | Senha | O que pode fazer |
|---|---|---|---|
| `USER` | `teste@teste.com` | `123456teste` | Listar, consultar e cadastrar produtos |
| `ADMIN` | `admin@admin.com` | `123456admin` | Tudo do `USER` e também excluir produtos |

Essas contas existem só para demonstração. Elas são criadas pela migração `V3` e não devem ser mantidas em um ambiente real.

## Funcionalidades

- Cadastro e login de usuários, com senha guardada em hash BCrypt.
- Autenticação por token JWT, sem sessão no servidor (stateless).
- Perfis de acesso: somente o `ADMIN` exclui produtos (`403` para quem não tem permissão).
- CRUD parcial de produtos: listar, buscar por id, cadastrar e excluir.
- Validação dos dados de entrada com Bean Validation (`400` com a mensagem de cada campo).
- Tratamento global de erros, com um formato de resposta único.
- Painel web: tela de login e cadastro, lista de produtos com filtro, busca por id e histórico das requisições enviadas (método, status, tempo e resposta).
- Banco versionado com Flyway.
- Execução em contêiner Docker.

## Tecnologias

| Área | Tecnologia |
|---|---|
| Linguagem e framework | Java, Spring Boot 4.1 |
| Segurança | Spring Security, JWT (biblioteca `jjwt`), BCrypt |
| Persistência | Spring Data JPA, Hibernate |
| Banco de dados | PostgreSQL |
| Migrações | Flyway |
| Validação | Jakarta Bean Validation |
| Frontend | HTML, CSS e JavaScript puro, servidos com Thymeleaf |
| Outros | Lombok, Docker |
| Deploy | Render (aplicação) e Neon (PostgreSQL gerenciado) |

## Arquitetura

```
Navegador (login.html / index.html)
     │  JSON + header  Authorization: Bearer <token>
     ▼
Filtros do Spring Security   ← JwtAuthFilter valida o token e o perfil
     ▼
Controller                   ← recebe DTOs validados (@Valid)
     ▼
Service                      ← regras de negócio
     ▼
Repository (Spring Data JPA)
     ▼
PostgreSQL                   ← tabelas criadas pelo Flyway
```

Tratamento de erros centralizado em um `@RestControllerAdvice`.

## Endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | Público | Cria uma conta (sempre com perfil `USER`) |
| `POST` | `/auth/login` | Público | Devolve o token JWT |
| `GET` | `/produtos` | Autenticado | Lista os produtos |
| `GET` | `/produtos/{id}` | Autenticado | Busca um produto por id |
| `POST` | `/produtos` | Autenticado | Cadastra um produto |
| `DELETE` | `/produtos/{id}` | `ADMIN` | Exclui um produto |

Rotas de páginas: `/` (painel) e `/login` (entrada e cadastro).

### Exemplos

Login:

```http
POST /auth/login
Content-Type: application/json

{ "email": "teste@teste.com", "senha": "123456teste" }
```

```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

Cadastrar um produto:

```http
POST /produtos
Authorization: Bearer <token>
Content-Type: application/json

{ "nome": "Teclado", "preco": 150.0 }
```

### Formato de erro

```json
{
  "status": 400,
  "mensagem": "Dados inválidos",
  "campos": { "email": "E-mail inválido" }
}
```

O campo `campos` só aparece em erros de validação.

| Status | Quando |
|---|---|
| `400` | Dados inválidos ou corpo ausente |
| `401` | Sem token, token inválido ou expirado, ou login incorreto |
| `403` | Usuário sem permissão para a ação (por exemplo, `USER` excluindo) |
| `404` | Produto não encontrado |
| `409` | E-mail já cadastrado |

## Segurança

- Senhas guardadas com BCrypt, nunca em texto puro.
- O token tem validade de 1 hora e é assinado com uma chave secreta (`JWT_SECRET`).
- O perfil do usuário vai dentro do token. Uma mudança de perfil só vale no próximo login.
- O cadastro não aceita perfil: toda conta nova nasce como `USER`.
- O login devolve a mesma mensagem para e-mail inexistente e senha errada.
- Por simplicidade, o painel guarda o token no `localStorage`. Em um sistema real, o mais seguro é um cookie `HttpOnly`.

## Variáveis de ambiente

| Variável | Descrição | Valor padrão (só para uso local) |
|---|---|---|
| `DB_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/springaws` |
| `DB_USER` | Usuário do banco | `app` |
| `DB_PASSWORD` | Senha do banco | `segredo` |
| `JWT_SECRET` | Chave de assinatura do token (mínimo de 32 caracteres) | chave de desenvolvimento |

Em produção, defina sempre essas variáveis no ambiente. Nunca versione senhas ou a chave do JWT.

## Como rodar localmente

### Pré-requisitos

- Java e Maven (ou o `mvnw` do projeto)
- PostgreSQL
- Docker (opcional)

### 1. Criar o banco

O Flyway cria as tabelas, mas **não cria o banco**. No PostgreSQL:

```sql
CREATE USER app WITH PASSWORD 'segredo';
CREATE DATABASE springaws OWNER app;
```

### 2. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

No Windows: `.\mvnw.cmd spring-boot:run`. Ao iniciar, o Flyway aplica as migrações. Acesse `http://localhost:8080`.

### 3. Rodar com Docker

Compile o projeto e gere a imagem:

```bash
./mvnw clean package -DskipTests
docker build -t springaws .
```

Suba o contêiner apontando para o seu banco (ajuste conforme o seu `Dockerfile`):

```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/springaws \
  -e DB_USER=app \
  -e DB_PASSWORD=segredo \
  -e JWT_SECRET=troque-por-uma-chave-aleatoria-com-32-caracteres-ou-mais \
  springaws
```

Dentro do contêiner, `localhost` é o próprio contêiner. Por isso o banco da sua máquina é acessado por `host.docker.internal`.

## Banco de dados e migrações

As tabelas são versionadas com Flyway, em `src/main/resources/db/migration`:

| Migração | O que faz |
|---|---|
| `V1__criar_tabelas.sql` | Cria as tabelas `produto` e `usuario` |
| `V2__adicionar_perfil_usuario.sql` | Adiciona a coluna `perfil` ao usuário |
| `V3__contas_de_teste.sql` | Cria as contas de demonstração (`USER` e `ADMIN`) |

O Hibernate roda com `ddl-auto=validate`: ele só confere se as entidades batem com as tabelas. Quem cria e altera o schema é o Flyway. Uma migração que já rodou não deve ser editada, e qualquer mudança vira uma migração nova.

Para promover outro usuário a administrador:

```sql
UPDATE usuario SET perfil = 'ADMIN' WHERE email = 'email@exemplo.com';
```

## Deploy

A aplicação roda no **Render** (como contêiner Docker) e o banco é um **PostgreSQL gerenciado no Neon**, ambos em planos gratuitos.

1. Crie o banco no Neon e copie host, usuário e senha.
2. No serviço web do Render, em **Environment**, cadastre `DB_URL`, `DB_USER`, `DB_PASSWORD` e `JWT_SECRET`.
3. A `DB_URL` usa o formato JDBC com SSL: `jdbc:postgresql://HOST:5432/BANCO?sslmode=require`.
4. A cada `push`, o Render publica uma nova versão e o Flyway aplica as migrações pendentes.

## Estrutura do projeto

```
src/main/java/com/joa1paulo/SpringAWS
├── Config        SecurityConfig, JwtAuthFilter
├── Controller    AuthController, ProdutoController, HomeController
├── Dto           RegisterRequest, LoginRequest, LoginResponse, UsuarioResponse,
│                 ProdutoRequest, ProdutoResponse
├── Exception     GlobalExceptionHandler, ErroResponse e exceções de negócio
├── Model         Usuario, Perfil, Produto
├── Repository    UsuarioRepository, ProdutoRepository
└── Service       UsuarioService, ProdutoService, TokenService

src/main/resources
├── db/migration  V1, V2, V3 (Flyway)
├── templates     login.html, index.html
└── application.properties
```

## Próximos passos

- Testes automatizados (JUnit e Mockito para os serviços, `@SpringBootTest` para a API).
- Documentação da API com Swagger/OpenAPI.
- Trocar `Double` por `BigDecimal` no preço do produto.
- Guardar o token em cookie `HttpOnly`.
- Edição de produtos (`PUT`).
