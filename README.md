## 1. Arquitetura do Sistema e Microsserviços

O ecossistema é composto por microsserviços totalmente independentes, cada um possuindo seu próprio ciclo de vida, banco de dados isolado (Database-per-Service) e rodando sob uma rede virtualizada interna.

###  API Gateway (`mscloudgateway`)
Atua como a porta de entrada única (**Single Entry Point**) pública do sistema na porta `8085`.
* **Roteamento Dinâmico:** Centraliza e redireciona o tráfego para os microsserviços internos de forma transparente.
* **Segurança e CORS:** Gerencia as permissões de compartilhamento de recursos de origens cruzadas (CORS) em um único ponto.
* **Proxy Reverso Integrado:** Configurado com estratégias de repasse de cabeçalhos (`X-Forwarded-Host`, `X-Forwarded-Port`) para garantir a integridade de redirecionamentos de segurança vindos de dentro do Docker.

###  Discovery Server (`eurekaserver`)
Servidor de Service Discovery baseado em **Netflix Eureka**.
* **Desacoplamento de Rede:** Gerencia o catálogo dinâmico de instâncias. Os microsserviços se localizam puramente por seus nomes lógicos, eliminando qualquer acoplamento por IPs ou portas fixas.
* **Health Checks:** Monitora constantemente a saúde das aplicações, garantindo que o Gateway envie tráfego apenas para contêineres saudáveis.

###  Authorization Server & User (`msauth` & `msuser`)
O núcleo de segurança e identidade do ecossistema, baseado no **Spring Authorization Server** moderno e especificações **OAuth2 / OpenID Connect 1.0**.
* **Portas Randômicas e Isolamento:** Roda em porta dinâmica interna (`server.port=0`), totalmente protegido do acesso externo direto, respondendo apenas através do Gateway.
* **Criptografia Assimétrica (Chaves RSA):** Emite tokens JWT estruturados e autossuficientes, assinados digitalmente usando um par de chaves RSA de 2048 bits geradas em memória através de um `JWKSource` padronizado.
* **Fluxos OAuth2 Implementados:**
   * *Authorization Code com PKCE:* Para autenticação segura de usuários humanos com tela de login customizada.
   * *Client Credentials:* Para autenticação segura e automática de Máquina para Máquina (M2M).
* **Userinfo Endpoint:** Disponibiliza a rota `/userinfo` customizada via `OidcUserInfoMapper` para o fornecimento seguro de claims de perfil (nome, e-mail, foto) baseados em escopos.

###  Módulo de Pedidos (`mspedido`)
Responsável pela orquestração do fluxo de compras e transações financeiras.
* **Validação Segura com Feign:** Intercepta requisições síncronas usando OpenFeign injetando tokens de sistema (`Client Credentials`) no cabeçalho para consultar e validar a existência de itens no `msproduto` de forma protegida.
* **Extração de Identidade Global:** Lê o campo `sub` (Subject) de dentro do JWT enviado pelo usuário logado para amarrar o pedido ao ID global e imutável do comprador, mantendo o desacoplamento de banco de dados.

###  Módulo de Produtos e Estoque (`msproduto`)
Responsável pelo gerenciamento do catálogo de itens e controle rígido de inventário.
* **Pesquisa Avançada:** CRUD completo e pesquisa paginada dinâmica via JPA Specifications.
* **Arquitetura Orientada a Eventos:** Atua como Consumidor de Mensageria. Escuta a fila do broker para processar de forma assíncrona e resiliente a baixa ou reposição de estoque assim que um pedido muda de estado.

---

##  2. Tecnologias Utilizadas

* **Core:** Java 25 & Spring Boot 4.x
* **Segurança:** Spring Security, Spring Authorization Server (OAuth2 / OIDC 1.0, JWT, Nimbus JOSE + JWT)
* **Comunicação e Roteamento:** Spring Cloud OpenFeign & Spring Cloud Gateway
* **Service Discovery:** Netflix Eureka Server & Client
* **Mensageria:** RabbitMQ (AMQP Protocol)
* **Persistência de Dados:** Spring Data JPA & PostgreSQL (Bancos de dados isolados por serviço)
* **Orquestração e Infraestrutura:** Docker & Docker Compose
* **Mapeamento e Organização:** MapStruct & Lombok
* **Garantia de Qualidade:** JUnit 5, Mockito & JaCoCo (81% de cobertura de testes na camada crítica de serviços)

---

##  3. Infraestrutura e Orquestração (Docker)

Toda a infraestrutura do ecossistema foi conteinerizada para garantir paridade absoluta entre os ambientes de desenvolvimento e produção. Os segredos, credenciais e conexões do sistema são injetados dinamicamente nos contêineres através de um arquivo de configuração centralizado `.env`.

O ambiente utiliza uma rede externa dedicada do Docker (`ecommerce-net`), permitindo o isolamento completo do tráfego de dados sensíveis entre as bases do PostgreSQL, o Message Broker e as aplicações Java.

---

##  4. Como Executar o Ecossistema

### Pré-requisitos
* Possuir o **Docker** e o **Docker Compose** instalados na máquina.
* Criar um arquivo `.env` na raiz do projeto com as credenciais de ambiente (Baseado no modelo `.env.example` disponível no repositório).

### Inicialização do Ambiente
Graças à orquestração do Docker Compose, todo o ecossistema (bancos, broker, discovery, gateway e microsserviços) pode ser inicializado com um único comando no terminal:

```bash
# Clone o repositório
git clone git@github.com:Wendell-Valentim/e-commerce.git
cd e-commerce

# Inicie todo o ecossistema em segundo plano
docker compose up -d --build