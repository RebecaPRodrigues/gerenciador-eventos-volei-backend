# 🏐 Gerenciador de Eventos de Vôlei - Backend

API REST desenvolvida em Java com Spring Boot para gerenciar eventos de vôlei. Permite o cadastro de usuários (atletas e admins), criação de eventos, inscrições, confirmação de presença e exibição pública dos eventos.

---

## ⚙️ Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- Spring Security + JWT  
- Maven  
- PostgreSQL  
- Docker (opcional)  
- SpringDoc OpenAPI (Swagger)  

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java 17 instalado  
- Maven  
- PostgreSQL (local ou em nuvem)  
- Docker (opcional)  

### Clone o repositório

git clone https://github.com/RebecaPRodrigues/gerenciador-eventos-volei-backend.git
cd gerenciador-eventos-volei-backend

### Configure o `application.properties`

Crie um arquivo `.env` ou configure as variáveis de ambiente:

PORT=8080
JWT_SECRET=minha-chave-secreta
DATABASE_URL=jdbc:postgresql://localhost:5432/nome_do_banco
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha

Ou edite diretamente o arquivo `src/main/resources/application.properties`.

### Executando

./mvnw spring-boot:run

A API estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## 🔌 Endpoints da API

### 🔐 /auth - Autenticação

| Método | Caminho           | Descrição                            |
|--------|-------------------|--------------------------------------|
| POST   | /auth/login       | Autentica usuário e retorna JWT      |
| POST   | /auth/register    | Cria um novo usuário (atleta)        |

### 👤 /admin/usuarios - Administração de Usuários

| Método | Caminho                          | Descrição                                  |
|--------|----------------------------------|--------------------------------------------|
| GET    | /admin/usuarios                  | Lista todos os usuários                    |
| GET    | /admin/usuarios/{id}             | Busca usuário por ID                       |
| POST   | /admin/usuarios                  | Cria um novo usuário                       |
| PUT    | /admin/usuarios/{id}             | Atualiza dados de um usuário               |
| PUT    | /admin/usuarios/{id}/reset-senha | Reseta a senha para 12345678               |
| DELETE | /admin/usuarios/{id}             | Deleta um usuário                          |

### 📅 /admin/eventos - Administração de Eventos

| Método | Caminho                 | Descrição                    |
|--------|-------------------------|------------------------------|
| GET    | /admin/eventos          | Lista todos os eventos       |
| GET    | /admin/eventos/{id}     | Busca evento por ID          |

### 📋 /admin/inscricoes - Inscrições de Usuários em Eventos

| Método | Caminho                               | Descrição                                     |
|--------|----------------------------------------|----------------------------------------------|
| GET    | /admin/inscricoes/evento/{eventoId}    | Lista usuários inscritos em um evento        |
| GET    | /admin/inscricoes/usuario/{usuarioId}  | Lista eventos em que o usuário está inscrito |
| POST   | /admin/inscricoes                      | Inscreve um usuário em um evento             |

### 🧍 /atleta - Funcionalidades para o usuário atleta

| Método | Caminho                            | Descrição                                   |
|--------|------------------------------------|---------------------------------------------|
| POST   | /atleta/inscrever/{eventoId}       | Atleta se inscreve em um evento             |
| GET    | /atleta/meus-eventos               | Lista eventos em que o atleta está inscrito |
| PATCH  | /atleta/confirmar/{eventoId}       | Atleta confirma presença em um evento       |

### 🌐 /public - Acesso público a eventos

| Método | Caminho                            | Descrição                              |
|--------|------------------------------------|----------------------------------------|
| GET    | /public/proximos-eventos           | Lista próximos eventos disponíveis     |
| GET    | /public/detalhes-eventos/{id}      | Retorna detalhes públicos de um evento |

### ✅ /ping - Verificação de status

| Método | Caminho | Descrição               |
|--------|---------|-------------------------|
| GET    | /ping   | Retorna “Pong” (API ok) |

---

## 🧪 Testes

Você pode usar o Postman ou acessar o Swagger:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 👩‍💻 Autor

@RebecaPRodrigues
