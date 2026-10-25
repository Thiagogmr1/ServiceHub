# ServiceHub

API REST para gerenciamento de clientes e prestadores de serviços.

O projeto está atualmente em uma versão inicial funcional, com operações de cadastro, consulta, atualização completa, atualização parcial e remoção de clientes e prestadores, utilizando PostgreSQL como banco de dados.

## Sumário

- [Visão geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades atuais](#funcionalidades-atuais)
- [Modelo de dados](#modelo-de-dados)
- [Endpoints](#endpoints)
- [Configuração do ambiente](#configuração-do-ambiente)
- [Como executar](#como-executar)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
- [Limitações atuais](#limitações-atuais)
- [Próximos passos recomendados](#próximos-passos-recomendados)

## Visão geral

O ServiceHub organiza a comunicação entre clientes e prestadores de serviços por meio de uma API HTTP.

Atualmente, a aplicação contempla:

- Cadastro de clientes;
- Cadastro de prestadores;
- Consulta de registros;
- Atualização completa com `PUT`;
- Atualização parcial com `PATCH`;
- Exclusão de registros;
- Validação básica dos dados;
- Persistência em PostgreSQL;
- Documentação OpenAPI/Swagger.

## Tecnologias

- Java 21;
- Spring Boot 3.5.16;
- Spring Web;
- Spring Data JPA;
- PostgreSQL 16;
- Bean Validation;
- Springdoc OpenAPI;
- Maven;
- JUnit e Spring Boot Test;
- Docker Compose para o banco de dados.

As dependências do projeto estão declaradas em [`pom.xml`](pom.xml).

## Arquitetura

O projeto utiliza uma organização em camadas:

```text
src/
├── main/
│   ├── java/com/servicehub/
│   │   ├── ServiceHubApplication.java
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/servicehub/
        └── ServiceHubApplicationTests.java
```

### Camadas

- **Controllers:** recebem as requisições HTTP e retornam as respostas da API.
- **Services:** concentram as operações de negócio e verificam a existência dos registros.
- **Repositories:** utilizam Spring Data JPA para acesso ao banco.
- **Models:** representam as entidades persistidas no PostgreSQL.

## Funcionalidades atuais

### Clientes

O recurso de clientes permite:

- Listar todos os clientes;
- Consultar um cliente por ID;
- Criar um cliente;
- Substituir os dados de um cliente;
- Atualizar parcialmente um cliente;
- Excluir um cliente.

### Prestadores

O recurso de prestadores permite:

- Listar todos os prestadores;
- Consultar um prestador por ID;
- Criar um prestador;
- Substituir os dados de um prestador;
- Atualizar parcialmente um prestador;
- Excluir um prestador.

### Validações

Os modelos possuem as seguintes validações:

- `name` é obrigatório;
- `email` é obrigatório;
- `email` deve possuir formato válido;
- `phone` é obrigatório;
- `service` é obrigatório para prestadores.

Quando um cliente ou prestador não é encontrado pelo ID, a aplicação retorna HTTP `404 Not Found`.

## Modelo de dados

### Cliente

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| `id` | `Long` | Não | Identificador gerado pelo banco |
| `name` | `String` | Sim | Nome do cliente |
| `email` | `String` | Sim | E-mail do cliente |
| `phone` | `String` | Sim | Telefone do cliente |

### Prestador

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| `id` | `Long` | Não | Identificador gerado pelo banco |
| `name` | `String` | Sim | Nome do prestador |
| `email` | `String` | Sim | E-mail do prestador |
| `phone` | `String` | Sim | Telefone do prestador |
| `service` | `String` | Sim | Serviço oferecido |

## Endpoints

### Endpoint de verificação

```http
GET /hello
```

Resposta:

```text
ServiceHub API funcionando!
```

### Clientes

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| `GET` | `/clients` | Lista clientes | `200 OK` |
| `GET` | `/clients/{id}` | Consulta cliente por ID | `200 OK` ou `404 Not Found` |
| `POST` | `/clients` | Cria cliente | `201 Created` |
| `PUT` | `/clients/{id}` | Atualiza todos os dados do cliente | `200 OK` ou `404 Not Found` |
| `PATCH` | `/clients/{id}` | Atualiza somente campos informados | `200 OK` ou `404 Not Found` |
| `DELETE` | `/clients/{id}` | Remove cliente | `204 No Content` ou `404 Not Found` |

#### Exemplo de criação

```http
POST /clients
Content-Type: application/json
```

```json
{
  "name": "Maria Silva",
  "email": "maria@example.com",
  "phone": "(11) 99999-9999"
}
```

#### Exemplo de atualização parcial

```http
PATCH /clients/1
Content-Type: application/json
```

```json
{
  "phone": "(11) 98888-8888"
}
```

### Prestadores

| Método | Endpoint | Descrição | Resposta esperada |
|---|---|---|---|
| `GET` | `/providers` | Lista prestadores | `200 OK` |
| `GET` | `/providers/{id}` | Consulta prestador por ID | `200 OK` ou `404 Not Found` |
| `POST` | `/providers` | Cria prestador | `201 Created` |
| `PUT` | `/providers/{id}` | Atualiza todos os dados do prestador | `200 OK` ou `404 Not Found` |
| `PATCH` | `/providers/{id}` | Atualiza somente campos informados | `200 OK` ou `404 Not Found` |
| `DELETE` | `/providers/{id}` | Remove prestador | `204 No Content` ou `404 Not Found` |

#### Exemplo de criação

```http
POST /providers
Content-Type: application/json
```

```json
{
  "name": "João Serviços",
  "email": "joao@example.com",
  "phone": "(11) 97777-7777",
  "service": "Manutenção residencial"
}
```

#### Exemplo de atualização parcial

```http
PATCH /providers/1
Content-Type: application/json
```

```json
{
  "service": "Manutenção elétrica"
}
```

## Configuração do ambiente

### Pré-requisitos

- Java 21;
- Docker Desktop, com o Docker Engine em execução;
- Windows PowerShell ou terminal compatível.

### Banco de dados

O arquivo [`docker-compose.yml`](docker-compose.yml) define um PostgreSQL 16 com:

| Configuração | Valor |
|---|---|
| Banco | `servicehub` |
| Usuário | `servicehub` |
| Porta local | `5433` |
| Porta do container | `5432` |
| Container | `servicehub-postgres` |

As credenciais definidas no Compose são destinadas ao desenvolvimento local. Para outros ambientes, elas devem ser substituídas por variáveis de ambiente ou por um mecanismo seguro de configuração.

## Como executar

### 1. Iniciar o banco

Na raiz do projeto:

```powershell
docker compose up -d
```

Para verificar o container:

```powershell
docker compose ps
```

### 2. Iniciar a aplicação

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível, por padrão, em:

```text
http://localhost:8080
```

### 3. Gerar o pacote

```powershell
.\mvnw.cmd package
```

O JAR será gerado no diretório `target/`.

Para gerar o pacote sem executar os testes:

```powershell
.\mvnw.cmd -DskipTests package
```

## Testes

Para executar os testes:

```powershell
.\mvnw.cmd test
```

Atualmente existe um teste de carregamento do contexto da aplicação em [`ServiceHubApplicationTests.java`](src/test/java/com/servicehub/ServiceHubApplicationTests.java).

Esse teste inicializa o contexto completo do Spring e, portanto, precisa de um PostgreSQL acessível em `127.0.0.1:5433`.

Ainda não existem testes automatizados específicos para:

- Controllers;
- Services;
- Repositories;
- Fluxos completos de CRUD;
- Validações dos payloads;
- Atualizações parciais;
- Respostas documentadas da API.

## Documentação da API

O Swagger/OpenAPI está habilitado por meio do [`application.properties`](src/main/resources/application.properties).

Com a aplicação em execução, os endereços esperados são:

```text
Swagger UI: http://localhost:8080/swagger-ui/index.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
```

## Configurações atuais

As principais configurações estão em [`application.properties`](src/main/resources/application.properties):

- Conexão PostgreSQL em `127.0.0.1:5433`;
- Banco `servicehub`;
- Hibernate com `ddl-auto=update`;
- SQL do JPA habilitado nos logs;
- Swagger UI habilitado;
- Inclusão de mensagens e stack traces nos erros HTTP.

As configurações de SQL detalhado, atualização automática do schema e exposição de stack traces são apropriadas apenas para desenvolvimento e devem ser revistas antes de uma implantação em produção.

## Limitações atuais

O projeto ainda não contempla:

- Autenticação e autorização;
- Perfis de configuração separados para desenvolvimento, teste e produção;
- DTOs específicos para entrada e saída;
- Tratamento global padronizado de erros;
- Paginação, filtros ou ordenação;
- Busca por nome, e-mail ou tipo de serviço;
- Migrações versionadas de banco com Flyway ou Liquibase;
- Testes de integração completos;
- Testes de controller e service;
- Pipeline de integração contínua;
- Monitoramento e métricas;
- Interface web ou aplicativo cliente.

Além disso, a validação ainda não está aplicada de forma uniforme em todos os endpoints de criação e atualização. Essa é uma área recomendada para a próxima evolução do projeto.

## Próximos passos recomendados

1. Garantir a execução automatizada do banco nos testes, preferencialmente com Testcontainers.
2. Adicionar testes unitários e de integração para os fluxos de CRUD.
3. Padronizar o uso de `@Valid` nos endpoints.
4. Criar DTOs para separar o contrato HTTP das entidades JPA.
5. Implementar tratamento global de exceções.
6. Criar perfis de configuração para desenvolvimento, teste e produção.
7. Remover a exposição de stack traces e SQL detalhado em produção.
8. Adicionar migrações versionadas do banco.
9. Documentar e configurar variáveis de ambiente.
10. Avaliar autenticação, autorização, paginação e filtros.

