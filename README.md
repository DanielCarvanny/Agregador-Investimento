# Agregador de Investimentos

💫 **Visão Geral**

Este projeto é uma API desenvolvida em Java para um Agregador de Investimentos. A API permite gerenciar usuários, contas, endereços de cobrança e ações (stocks), além de integrar com uma API externa da Bolsa de Valores para obter dados em tempo real. O projeto foi desenvolvido utilizando Spring Boot, e integração com banco de dados MySQL via Docker.

---

## 🛠 **Ferramentas e Conceitos Utilizados**

- **Spring Security 6**: Para garantir a segurança da API.
- **Integração com MySQL via Docker**: Para gerenciar o banco de dados de forma isolada.
- **Spring Cloud OpenFeign**: Para consumir APIs externas, como a API da Bolsa de Valores.
- **JUnit e Mockito**: Para testes unitários.
- **JPA com Spring Boot e Hibernate**: Para mapeamento objeto-relacional (ORM).
- **Relacionamentos entre Entidades**: Como `OneToMany`, `OneToOne` e `ManyToMany`.
- **Testes Unitários**: Com padrão "Triple A" (Arrange, Act, Assert) e uso de Mocks.

---

## ⚙️ **Funcionalidades Implementadas**

### **Gerenciamento de Usuários**
- Criação, consulta, listagem, atualização e exclusão de usuários.

### **Gerenciamento de Contas**
- Criação de contas associadas a usuários.
- Relacionamento `OneToMany` entre `User` e `Account`.

### **Endereço de Cobrança**
- Cada conta possui um endereço de cobrança (`BillingAddress`), com relacionamento `OneToOne`.

### **Gerenciamento de Ações (Stocks)**
- Cadastro de ações e associação a contas.
- Relacionamento `ManyToMany` entre `Account` e `Stock`.

### **Integração com API Externa**
- Consumo de uma API da Bolsa de Valores para obter dados em tempo real.
- Cálculo do valor total das ações em uma conta.

### **Testes Unitários**
- Testes unitários para validação das funcionalidades.
- Uso de mocks para simular comportamentos.
