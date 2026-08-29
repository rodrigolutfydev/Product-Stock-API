# Product Stock API

API REST para controle de estoque, com gestão de categorias e produtos, exclusão lógica e tratamento de erros centralizado. Projeto pessoal.

## Stack

- Java 17
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- MySQL 8.4
- Flyway
- Bean Validation

## Modelo de dados

- **Category**: `id`, `name`, `active`, `productCount`
- **Product**: `id`, `name`, `price`, `stock`, `active`, `category`

## Como rodar

### Pré-requisitos

- Java 17+
- MySQL 8+
- Maven (ou o wrapper `./mvnw` incluso no projeto)

### 1. Clonar o repositório

```bash
git clone https://github.com/rodrigolutfydev/product-stock.git
cd product-stock
```

### 2. Criar o banco de dados

Conecte no MySQL e crie o banco vazio:

```sql
CREATE DATABASE product_stock;
```

### 3. Configurar as variáveis de ambiente

A aplicação lê usuário e senha do banco via variáveis de ambiente — nenhuma credencial fica no código.

| Variável | Descrição |
|---|---|
| `DB_USERNAME` | Usuário do MySQL |
| `DB_PASSWORD` | Senha do MySQL |

**Via terminal:**
```bash
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
```

**Via IntelliJ:** Run > Edit Configurations > Environment Variables, e adicione as duas variáveis lá.

### 4. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

Na primeira execução, o Flyway aplica as migrations automaticamente, criando as tabelas `categories` e `products` já com a estrutura completa — não é necessário nenhum script manual.

### 5. Testar

A API sobe em `http://localhost:8080`. Use qualquer cliente HTTP (Insomnia, Postman, curl) para testar os endpoints listados abaixo. Exemplo rápido com curl:

```bash
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Eletrônicos"}'
```

## Endpoints

### Categories

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/categories` | Cadastra categoria |
| `GET` | `/categories` | Lista categorias ativas (paginado) |
| `GET` | `/categories/{id}` | Detalha categoria |
| `PUT` | `/categories` | Atualiza categoria |
| `DELETE` | `/categories/{id}` | Exclui logicamente (bloqueado se houver produtos ativos vinculados) |

### Products

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/products` | Cadastra produto |
| `GET` | `/products` | Lista produtos ativos (paginado) |
| `GET` | `/products/{id}` | Detalha produto |
| `PUT` | `/products` | Atualiza produto (incluindo troca de categoria) |
| `DELETE` | `/products/{id}` | Exclui logicamente |

## Tratamento de erros

`@RestControllerAdvice` centralizado (`ErrorHandler`):

- `404` — recurso inexistente
- `400` — falha de Bean Validation ou corpo malformado
- `409` — violação de regra de negócio (ex: excluir categoria com produtos vinculados)
- `500` — catch-all

## Decisões de design

- Regras de negócio (exclusão lógica, validação de estado) encapsuladas nas entities, não nos controllers.
- `productCount` mantido como campo persistido em `Category`, atualizado a cada cadastro/exclusão/troca de categoria de produto — trade-off consciente frente a calcular via `COUNT` em tempo de leitura.
- Exclusão lógica (`active = false`) em vez de remoção física.