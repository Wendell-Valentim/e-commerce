# Ecossistema de Microserviços - E-commerce

Este repositório contém um ecossistema de microserviços focado em escalabilidade e alta performance, utilizando as tecnologias mais recentes do ecossistema Java. A arquitetura foi desenhada para garantir independência entre os serviços e resiliência através de service discovery.

## Microserviços Atuais

### ms-produto
O **ms-produto** é responsável pelo gerenciamento completo do catálogo de produtos e controlo de stock centralizado.

#### **Principais Funcionalidades:**
* CRUD completo de produtos (Cadastrar, Atualizar, Consultar e Deletar).
* Pesquisa paginada dinâmica utilizando **JPA Specifications** para consultas complexas.
* Gerenciamento de stock (Aumento e Baixa) com validações de estado e negócio.
* Documentação interativa e contratos de API via **Swagger/OpenAPI**.
* Autorregistro automático no Service Discovery para comunicação dinâmica.

#### **Tecnologias Utilizadas:**
* **JDK 25** e **Spring Boot 4.0.5**
* **Spring Data JPA** com suporte a banco de dados relacional.
* **MapStruct** para conversão eficiente entre DTOs e Entidades.
* **JUnit 5 e Mockito** para garantia de qualidade via testes unitários.
* **JaCoCo** para análise técnica de cobertura de código.

#### **Qualidade e Testes:**
* Cobertura de testes focada na camada de Service, garantindo a integridade das regras críticas de stock e persistência.
* Implementação de **Global Exception Handler** para padronização de respostas de erro da API.

---

## Infraestrutura

### Discovery Server (Eureka)
Servidor de Service Discovery baseado em **Netflix Eureka**, que atua como o catálogo central da arquitetura.

* **Desacoplamento de Rede:** Permite que os microserviços se localizem pelo nome lógico, eliminando a dependência de IPs e portas fixas (hardcoded).
* **Monitoramento de Saúde:** O servidor realiza health checks constantes, garantindo que apenas instâncias saudáveis recebam tráfego.
* **Escalabilidade:** Estrutura preparada para balanceamento de carga entre múltiplas instâncias de um mesmo serviço.

---

## Como executar

1. Clone o repositório para a sua máquina local.
2. Certifique-se de possuir o **JDK 25** devidamente configurado nas variáveis de ambiente.
3. Inicie o **Discovery Server** (Porta 8761) e aguarde a inicialização completa.
4. Inicie o **ms-produto** (Porta 8081).
5. Verifique o registro do serviço acedendo ao painel do Eureka em: `http://localhost:8761`.
6. Explore os endpoints do produto através da interface do Swagger em: `http://localhost:(porta aleatoria gerada)/swagger-ui/index.html`.
