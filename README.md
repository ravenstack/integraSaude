# PsicoAgenda V2 — Sistema de Agendamento para Psicólogos

> **Versão:** 1.1.5 Beta  
> **Stack:** Spring Boot · Spring Data JPA · H2 · HTML / JS / Tailwind CSS

---

## Visão Geral

PsicoAgenda V2 é uma aplicação web para gestão de consultas psicológicas. O sistema permite o cadastro e autenticação de dois perfis — **Paciente** e **Psicólogo** — com rotas, dashboards e regras de integridade referencial independentes para cada um.

---

## Arquitetura

A aplicação segue o padrão de **arquitetura em camadas**:

```
org.example/
├── controller/     # Endpoints REST e receção de pedidos HTTP
├── service/        # Lógica de negócio, validações e processamento
├── repository/     # Interfaces Spring Data JPA (acesso à base de dados)
├── model/          # Entidades JPA e Enums
├── dto/            # Objetos de transferência (Request / Response)
└── exception/      # Tratamento personalizado de erros
```

---

## Principais Entidades

### `Psicologo`
| Campo | Tipo |
|---|---|
| `id` | `Integer` (PK, auto) |
| `nome` | `String` |
| `crp` | `String` |
| `especialidade` | `String` |
| `valorSessao` | `BigDecimal` |
| `email` | `String` |
| `senhaHash` | `String` |

### `Paciente`
Estrutura análoga à de `Psicologo`, com campos específicos para dados do paciente.

---

## API REST

### Autenticação

```http
POST /api/login
Content-Type: application/json

{
  "email": "usuario@email.com",
  "senha": "senha123",
  "perfil": "PACIENTE"   // ou "PSICOLOGO"
}
```

**Resposta de sucesso:** `200 OK` com o DTO do utilizador autenticado.  
**Resposta de erro:** `404` se o e-mail não existir · `401` se a senha for inválida.

---

## Frontend

O frontend é composto por páginas HTML com Tailwind CSS e comunicação assíncrona via **Fetch API**.

### Fluxo de Navegação

```
cadastro-escolha.html
    ├── cadastro-paciente.html
    └── cadastro-psicologo.html

login.html → dashboard.html
```

### Sessão do Utilizador

Após login bem-sucedido, os dados do utilizador são armazenados em `localStorage`:

```javascript
localStorage.setItem('user_session', JSON.stringify(userData));
```

---

## Segurança e Integridade

- **Senhas** armazenadas como hash (simulado na versão Beta).
- **Repositórios** usam `Optional<T>` para evitar `NullPointerException`.
- **Integridade referencial:** não é possível excluir profissionais ou pacientes com consultas ou agendas vinculadas.
- **Validação de entrada:** anotações `@NotBlank`, `@Email` e `@CPF` nos DTOs de request.

---

## Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Passos

```bash
# 1. Compilar o projeto
mvn clean install

# 2. Executar a aplicação
# Execute a classe principal: org.example.Main

# 3. Aceder ao sistema
# http://localhost:8080/login.html
```

### H2 Console (desenvolvimento)

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
```

Use o H2 Console para inspecionar as tabelas em tempo de execução.

---

## Licença

Projeto em fase Beta. Consulte os responsáveis do projeto para informações sobre licenciamento.