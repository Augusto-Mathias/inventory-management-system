# 📦 Inventory Management System

> Sistema completo de controle de estoque para e-commerce com múltiplos locais e integração com marketplaces

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

---

## 📋 Sobre o Projeto

Sistema robusto de controle de estoque desenvolvido para gerenciar inventário de e-commerce com múltiplos locais de armazenamento (loja física e fulfillment). Permite importação automatizada de movimentações via Excel e oferece controle completo sobre entradas, saídas, transferências e inventários.

### 🎯 Problema que Resolve

Empresas que vendem em múltiplos marketplaces (Mercado Livre, Shopee, Amazon, Magalu, etc.) precisam:
- ✅ Controlar estoque em diferentes locais
- ✅ Importar movimentações de sistemas como Bling
- ✅ Gerenciar transferências entre locais
- ✅ Realizar inventários com cálculo de acuracidade
- ✅ Receber alertas de estoque baixo
- ✅ Ter visibilidade total do inventário

---

## ✨ Funcionalidades

### 📊 Gestão de Estoque
- **Múltiplos Locais**: Controle de estoque em loja física e fulfillment
- **Alertas Automáticos**: Notificações quando estoque atinge nível mínimo
- **Visão Unificada**: Consulta de estoque total e por local

### 📥 Importação de Dados
- **Importação Excel**: Processa planilhas do Bling e marketplaces
- **Histórico Completo**: Rastreabilidade de todas as movimentações
- **Validação Automática**: Verifica dados antes de processar

### 🔄 Movimentações
- **Entradas**: Registro de compras e recebimentos
- **Saídas**: Baixa automática de vendas
- **Transferências**: Movimentação entre locais com rastreamento

### 📋 Inventário
- **Contagem Física**: Registro de inventários por local
- **Cálculo de Acuracidade**: Análise de divergências
- **Ajustes Automáticos**: Correção de estoque após inventário
- **Valores Financeiros**: Cálculo de diferenças em valor monetário

### 👥 Controle de Acesso
- **Múltiplos Perfis**: Admin, Operador, Visualizador
- **Autenticação JWT**: Segurança e controle de sessões
- **Auditoria**: Registro de quem fez cada operação

---

## 🏗️ Arquitetura

O sistema segue uma arquitetura em camadas com Spring Boot:

```
┌─────────────┐
│ Controllers │ ← Endpoints REST
├─────────────┤
│  Services   │ ← Lógica de Negócio
├─────────────┤
│ Repositories│ ← Acesso a Dados
├─────────────┤
│  Entities   │ ← Modelo de Domínio
└─────────────┘
```

### 📐 Diagrama de Classes

![Diagrama de Classes](docs/diagrams/diagrama_classes.png)

👉 **[Ver diagrama completo e interativo](docs/diagrams/diagrama_definitivo_com_todas_conexoes.drawio)**

**Entidades Principais:**
- 11 Entidades do Domínio
- 7 Enumerações
- 8 Exceptions Customizadas
- 11 Repositories
- 11 Services
- 7 Controllers REST

📚 **[Documentação completa da arquitetura na Wiki](../../wiki/Arquitetura)**

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17+** - Linguagem de programação
- **Spring Boot 3.2+** - Framework principal
- **Spring Data JPA** - ORM e persistência
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados
- **Lombok** - Redução de boilerplate

### Banco de Dados
- **PostgreSQL 16** - Banco de dados principal
- **NeonDB** - PostgreSQL serverless (produção)
- **Flyway** - Migrations e versionamento

### Processamento
- **Apache POI** - Leitura de arquivos Excel
- **Jackson** - Serialização JSON

### Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocks e testes unitários
- **TestContainers** - Testes de integração

### DevOps
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização
- **GitHub Actions** - CI/CD

---

## 🚀 Como Executar

### Pré-requisitos

```bash
- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 16+ (ou conta no NeonDB)
- Git
```

### 1️⃣ Clone o Repositório

```bash
git clone https://github.com/seu-usuario/inventory-management-system.git
cd inventory-management-system
```

### 2️⃣ Configure o Banco de Dados

**Opção A: NeonDB (Recomendado)**

1. Crie uma conta em [neon.tech](https://neon.tech)
2. Crie um novo projeto
3. Copie a connection string

**Opção B: PostgreSQL Local**

```bash
# Criar banco de dados
createdb inventory_management

# Ou com Docker
docker run -d \
  --name postgres-inventory \
  -e POSTGRES_DB=inventory_management \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  postgres:16-alpine
```

### 3️⃣ Configure as Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# Database
DB_URL=jdbc:postgresql://seu-host.neon.tech/inventory_management
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha

# JWT
JWT_SECRET=sua_chave_secreta_aqui
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

Ou edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

### 4️⃣ Execute o Projeto

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run
```

O servidor estará rodando em: **http://localhost:8080**

### 5️⃣ Teste a API

```bash
# Verificar status
curl http://localhost:8080/actuator/health

# Criar primeiro usuário (admin)
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Admin",
    "email": "admin@example.com",
    "senha": "senha123",
    "perfil": "ADMIN"
  }'
```

---

## 📚 Documentação da API

### Principais Endpoints

#### 🔐 Autenticação
```http
POST /api/usuarios/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "senha": "senha123"
}
```

#### 📦 Produtos
```http
GET    /api/produtos              # Listar todos
GET    /api/produtos/{id}         # Buscar por ID
POST   /api/produtos              # Criar novo
PUT    /api/produtos/{id}         # Atualizar
DELETE /api/produtos/{id}         # Deletar
```

#### 📊 Estoque
```http
GET /api/estoque/{produtoId}                    # Consultar estoque
GET /api/estoque/alertas                        # Produtos com estoque baixo
GET /api/estoque/{produtoId}/local/{localId}   # Estoque por local
```

#### 🔄 Movimentações
```http
POST /api/movimentacoes/entrada    # Registrar entrada
POST /api/movimentacoes/saida      # Registrar saída
GET  /api/movimentacoes/produto/{id} # Histórico por produto
```

#### 📥 Importação
```http
POST /api/importacoes/upload
Content-Type: multipart/form-data

file: arquivo.xlsx
localId: 1
tipo: BLING
```

#### 📋 Inventário
```http
POST /api/inventarios              # Iniciar inventário
POST /api/inventarios/{id}/itens   # Adicionar item contado
PUT  /api/inventarios/{id}/finalizar # Finalizar e ajustar estoque
```

📖 **[Documentação completa da API](docs/api/endpoints.md)**

---

## 🗂️ Estrutura do Projeto

```
inventory-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/inventory/
│   │   │       ├── config/          # Configurações (Security, CORS)
│   │   │       ├── controller/      # Controllers REST
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── exception/       # Exceptions customizadas
│   │   │       ├── model/           # Entidades JPA
│   │   │       │   └── enums/       # Enumerações
│   │   │       ├── repository/      # Repositories
│   │   │       ├── service/         # Lógica de negócio
│   │   │       └── util/            # Utilitários
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/        # Flyway migrations
│   └── test/                        # Testes unitários e integração
├── docs/                            # Documentação
│   ├── api/                         # Documentação da API
│   └── diagrams/                    # Diagramas
├── .github/
│   └── workflows/                   # GitHub Actions CI/CD
├── .gitignore
├── README.md
├── pom.xml
└── LICENSE
```

---

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com coverage
mvn test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html
```

---

## 📊 Modelo de Dados

### Principais Entidades

**Usuario** → Usuários do sistema  
**Produto** → Produtos do estoque  
**Categoria** → Categorias dos produtos  
**LocalEstoque** → Locais de armazenamento  
**EstoqueProduto** → Quantidade por produto/local  
**MovimentacaoEstoque** → Histórico de movimentações  
**Transferencia** → Transferências entre locais  
**Inventario** → Inventários realizados  
**Fornecedor** → Fornecedores  
**LoteImportacao** → Importações de Excel  

📚 **[Ver modelo completo de dados na Wiki](../../wiki/Modelo-de-Dados)**

---

## 🤝 Como Contribuir

Contribuições são sempre bem-vindas! 

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

📋 **[Guia completo de contribuição](CONTRIBUTING.md)**

---

## 📝 Roadmap

- [x] CRUD de Produtos e Categorias
- [x] Controle de estoque multi-local
- [x] Importação de Excel
- [x] Sistema de inventário
- [x] Alertas de estoque baixo
- [ ] Dashboard com gráficos
- [ ] Relatórios em PDF
- [ ] API de integração com marketplaces
- [ ] App mobile (React Native)
- [ ] Notificações por email/WhatsApp

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

Desenvolvido com ❤️ para resolver problemas reais de gestão de estoque em e-commerce.

---

## 🌟 Mostre seu apoio

Se este projeto foi útil para você, considere dar uma ⭐️!

---

## 📞 Suporte

Encontrou um bug? Tem alguma sugestão?

- 🐛 [Reportar Bug](../../issues/new?labels=bug)
- 💡 [Sugerir Feature](../../issues/new?labels=enhancement)
- 📖 [Ver Wiki](../../wiki)
- 💬 [Discussões](../../discussions)

---

<div align="center">
  
**[⬆ Voltar ao topo](#-inventory-management-system)**

</div>
