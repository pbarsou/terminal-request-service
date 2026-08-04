# Terminal Request Service - Reserva de Terminais POS

## Contexto

Este projeto é uma solução para o gerenciamento de solicitações de terminais POS. O sistema é responsável por receber solicitações, validar clientes, reservar terminais disponíveis e agendar a entrega, seguindo um fluxo de estados bem definido.

## Fluxo de Negócio

Máquina de estados:

1.  **SOLICITADO**: Estado inicial após a criação da requisição.
2.  **VALIDADO**: Após a confirmação de que o cliente é válido e ativo.
3.  **RESERVADO**: Após a reserva bem-sucedida de um terminal POS.
4.  **AGENDADO**: Estado final após o agendamento da entrega na logística.

### Estados de Falha
*   **REJEITADO**: Cliente inexistente ou inativo.
*   **ERRO_RESERVA**: Falha na reserva do terminal (ex: falta de estoque).
*   **ERRO_AGENDAMENTO**: Falha no agendamento da entrega.

## Simulação de Estados

Para facilitar os testes, a aplicação (e as Lambdas no LocalStack) possuem regras de negócio para simular os diferentes cenários:

| Status Alvo | Critério de Simulação                                                                                      |
| :--- |:-----------------------------------------------------------------------------------------------------------|
| **REJEITADO** | `customerId` começando com `"INVALID"` ou `"INACTIVE"`.                                                    |
| **ERRO_RESERVA** | `terminalType` definido como `POS_5G`. Esse tipo de POS está em fase de implantação.                       |
| **ERRO_AGENDAMENTO** | `address.state` sendo: `"BA"`, `"ES"` ou `"AM"`. O nosso serviço de entrega ainda não atua nessas regiões. |
| **AGENDADO** | Qualquer outro cenário válido.                                                                             |

## Arquitetura e Decisões Técnicas

O projeto foi desenvolvido seguindo os princípios da **Arquitetura Hexagonal (Ports and Adapters)** para garantir desacoplamento entre a lógica de domínio e as tecnologias externas.

### Principais Componentes:
*   **Domain**: Contém as entidades de negócio, enums, exceções e as interfaces (Ports) para repositórios e serviços.
*   **UseCase**: Camada de orquestração que executa as regras de negócio de forma isolada.
*   **Infrastructure**: Implementações concretas (Adapters) para JPA/PostgreSQL e integrações com serviços externos via AWS Lambda (simulados via LocalStack).
*   **Application**: Interface de entrada da aplicação, contendo api e os listeners de eventos.

### Comunicação Assíncrona:
O processamento do fluxo é realizado de forma assíncrona utilizando **Spring Application Events** com `@Async`. Isso permite que a API responda rapidamente ao usuário enquanto a comunicação com serviços externos ocorre em background.

## Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot**
*   **PostgreSQL** 
*   **Flyway** (Migrations)
*   **LocalStack** (Simulação de serviços AWS Lambda)
*   **Docker & Docker Compose** 
*   **Datadog Agent** (Observabilidade e tracing)
*   **JaCoCo** (Cobertura de testes)

## Como Executar

### Pré-requisitos
*   Docker e Docker Compose
*   Java 21
*   Maven

### Passo a Passo

1.  **Subir a Infraestrutura (Postgres e LocalStack):**
    ```bash
    make up-infra
    ```

2.  **Compilar e Empacotar a Aplicação:**
    ```bash
    make package
    ```

3.  **Executar a Aplicação via Docker Compose:**
    ```bash
    make up
    ```
    A aplicação estará disponível em `http://localhost:8080`.

4.  **Executar Localmente (fora do Docker):**
    ```bash
    make run
    ```

## Endpoints Principais

### Criar Solicitação
**POST** `/terminal-requests`
```json
{
  "customerId": "CUST-123",
  "terminalType": "POS_WIFI",
  "address": {
    "street": "Rua Exemplo",
    "number": "100",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01000-000"
  }
}
```

### Consultar Solicitação
**GET** `/terminal-requests/{id}`

## Testes

Para garantir a qualidade e o cumprimento dos requisitos, foram implementados testes unitários e de integração.

*   **Executar todos os testes:**
    ```bash
    make verify
    ```
*   **Executar apenas testes unitários:**
    ```bash
    make unit
    ```
*   **Executar testes de integração:**
    ```bash
    make it-integrated
    ```

## Pontos de Melhoria Futuros

1.  **Mensageria**: Utilizar um broker real (SQS, RabbitMQ ou Kafka) para garantir a persistência dos eventos em caso de queda da aplicação e garantir re-try.
2.  **Idempotência**: Implementar controle de idempotência no processamento de eventos para evitar duplicidade em casos de re-try.
3.  **Circuit Breaker**: Adicionar um Circuit Breaker para lidar com falhas nas integrações externas, tornando a aplicação mais resiliente.
4.  **Histórico de Status (Auditoria)**: Implementar o registro de transições de estado em uma tabela dedicada, permitindo uma auditoria detalhada quanto as transições de estado e tempo do fluxo.
5.  **Timeouts nos Adapters**: Configurar timeouts para as chamadas aos serviços externos, prevenindo que latências excessivas prejudiquem a performance do sistema.
6.  **Segurança (OAuth2/JWT)**: Criar uma camada de autenticação e autorização para proteger os endpoints, garantindo que apenas usuários ou sistemas autenticados possam criar ou consultar solicitações.
7.  **Paginação e Filtros**: Criar endpoint para consulta de terminal requests baseado em paginação e filtros.