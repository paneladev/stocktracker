# StockTracker API

StockTracker é uma API REST para controle de compras de ações, desenvolvida em Java com Spring Boot. Permite o cadastro de usuários, autenticação via JWT e gerenciamento de investimentos em ações, com integração ao MongoDB, Redis e consulta de cotações externas.

## Funcionalidades
- Cadastro e autenticação de usuários (JWT)
- Controle de permissões (Usuário/Admin)
- Cadastro, listagem e remoção de ações
- Registro e consulta de compras de ações
- Integração com Brapi para cotações em tempo real
- Cache com Redis
- Documentação da API com Swagger/OpenAPI

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3
- Spring Data MongoDB
- Spring Data Redis
- Spring Security (JWT)
- OpenFeign (cliente HTTP)
- Lombok
- Testcontainers (testes)
- Swagger/OpenAPI

## Como Executar

### Pré-requisitos
- Java 17+
- MongoDB em execução (padrão: localhost:27017)
- Redis em execução (padrão: localhost:6379, senha: sa)

### Configuração
Edite o arquivo `src/main/resources/application.yaml` para ajustar conexões com banco de dados, Redis e configurações de segurança conforme necessário.

### Build e Execução
```bash
./gradlew build
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080`.

### Documentação da API
Swagger UI: [http://localhost:8080/swagger/index.html](http://localhost:8080/swagger/index.html)

## Principais Endpoints

- `POST /stock/register` — Cadastro de novo usuário
- `POST /stock/auth/login` — Autenticação e obtenção do token JWT
- `POST /stock` — Cadastro de nova ação (requer autenticação)
- `POST /stock/add` — Adiciona uma compra a uma ação (requer autenticação)
- `GET /stock` — Lista todas as ações do usuário (requer autenticação)
- `GET /stock/detail/{stockId}` — Lista compras de uma ação (requer autenticação)
- `DELETE /stock/{stockId}` — Remove uma ação (requer autenticação)

## Testes
Execute os testes com:
```bash
./gradlew test
```

## Licença
MIT
