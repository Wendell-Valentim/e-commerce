Ecossistema de Microserviços - E-commerce
Este repositório contém um ecossistema de microserviços focado em escalabilidade e alta performance, utilizando as tecnologias mais recentes do ecossistema Java. A arquitetura foi desenhada para garantir independência entre os serviços, resiliência através de Service Discovery, roteamento centralizado via API Gateway e comunicação assíncrona via Message Broker.

Arquitetura e Microserviços
1. API Gateway (Spring Cloud Gateway)
   Atua como o ponto de entrada único (Single Entry Point) para todo o ecossistema.

Roteamento Centralizado: Direciona as requisições para os microserviços de forma transparente para o cliente.

Abstração de Infraestrutura: Oculta a complexidade da rede interna, expondo apenas os contratos necessários.

2. Discovery Server (Eureka)
   Servidor de Service Discovery baseado em Netflix Eureka que gerencia o catálogo central da arquitetura.

Desacoplamento de Rede: Permite que os serviços se localizem pelo nome lógico, eliminando dependências de IPs fixos.

Health Checks: Monitoramento constante para garantir que o tráfego seja direcionado apenas para instâncias saudáveis.

3. ms-pedido
   Responsável pela orquestração do fluxo de compras e transações.

Funcionalidades: Gerenciamento de estados do pedido (Recebido, Aprovado, Cancelado).

Integração Síncrona: Utiliza OpenFeign para validação de dados de produtos no ms-produto.

Integração Assíncrona: Publicação de eventos no RabbitMQ para processamento de baixa de estoque.

Camada de Validação: Implementação de validadores isolados seguindo o padrão Fail-Fast.

4. ms-produto
   Responsável pelo gerenciamento do catálogo de produtos e controle de estoque.

Funcionalidades: CRUD completo e pesquisa paginada dinâmica via JPA Specifications.

Gerenciamento de Estoque: Lógica de negócio para atualização de saldo de produtos com validações de consistência.

Tecnologias Utilizadas
Core: JDK 25 e Spring Boot 4.0.5.

Comunicação: Spring Cloud OpenFeign e Spring Cloud Gateway.

Mensageria: RabbitMQ para arquitetura orientada a eventos.

Persistência: Spring Data JPA com banco de dados H2 (em processo de migração para PostgreSQL).

Mapeamento: MapStruct para conversão entre DTOs e Entidades.

Garantia de Qualidade: JUnit 5, Mockito e JaCoCo.

Documentação: Swagger/OpenAPI.

Qualidade e Cobertura de Código
A integridade do sistema é assegurada por testes unitários focados nas camadas de Service e Business Logic. Atualmente, o ms-pedido apresenta 81% de cobertura na camada de Service, garantindo a confiabilidade de fluxos críticos como cálculos de totais, validações de estoque e regras de cancelamento.

Roadmap de Evolução
Migração da persistência de dados para PostgreSQL.

Implementação do ms-user com autenticação Spring Security e JWT.

Implementação de Circuit Breaker para tolerância a falhas em chamadas externas.

Instruções de Execução
Certifique-se de possuir o JDK 25 e o RabbitMQ configurados.

Inicialize os serviços na seguinte ordem:

Discovery Server (Porta 8761).

API Gateway (Porta 8080).

ms-produto e ms-pedido.

O acesso aos microserviços deve ser feito preferencialmente através da porta do API Gateway para garantir o roteamento correto.

Painel de monitoramento Eureka disponível em: http://localhost:8761.