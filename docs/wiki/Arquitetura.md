# 🏗️ Arquitetura do Sistema

Esta página descreve a arquitetura completa do **Inventory Management System**.

---

## 📊 Visão Geral

O sistema segue uma **arquitetura em camadas** (Layered Architecture) com Spring Boot, separando responsabilidades de forma clara e facilitando manutenção e testes.

```
┌─────────────────────────────────────┐
│         PRESENTATION LAYER          │
│          (Controllers)              │
│  - Recebe requisições HTTP          │
│  - Valida entrada                   │
│  - Retorna respostas JSON           │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         BUSINESS LAYER              │
│           (Services)                │
│  - Lógica de negócio                │
│  - Regras de validação              │
│  - Orquestração de operações        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       PERSISTENCE LAYER             │
│         (Repositories)              │
│  - Acesso ao banco de dados         │
│  - Queries customizadas             │
│  - Abstração do JPA                 │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          DATA LAYER                 │
│      (Database - PostgreSQL)        │
│  - Armazenamento persistente        │
│  - Integridade referencial          │
└─────────────────────────────────────┘
```

---

## 🎯 Princípios Arquiteturais

### 1. Separation of Concerns (SoC)
Cada camada tem uma responsabilidade específica e não se mistura com outras.

### 2. Dependency Inversion
Camadas superiores dependem de abstrações (interfaces), não de implementações concretas.

### 3. Single Responsibility Principle (SRP)
Cada classe tem apenas uma razão para mudar.

### 4. DRY (Don't Repeat Yourself)
Código reutilizável através de classes base e utilitários.

---

## 📦 Estrutura de Pacotes

```
com.inventory/
├── config/              # Configurações (Security, CORS, Beans)
├── controller/          # Controllers REST
├── dto/                 # Data Transfer Objects
├── exception/           # Exceptions customizadas
├── model/               # Entidades JPA
│   └── enums/          # Enumerações
├── repository/          # Repositories (Spring Data JPA)
├── service/             # Services (Lógica de negócio)
└── util/                # Classes utilitárias
```

---

## 🔷 Diagrama de Classes Completo

![Diagrama de Classes](../diagrams/diagrama_classes.png)

**[📥 Baixar diagrama editável (.drawio)](../diagrams/diagrama_definitivo_com_todas_conexoes.drawio)**

### Componentes Principais

#### 🔵 Entidades do Domínio (11)
```
Usuario
Produto
Categoria
Fornecedor
LocalEstoque
EstoqueProduto
MovimentacaoEstoque
Transferencia
Inventario
ItemInventario
LoteImportacao
```

Todas herdam de **BaseEntity** que fornece:
- `id: Long`
- `createdAt: LocalDateTime`
- `updatedAt: LocalDateTime`

#### 🟡 Enumerações (7)
```
Role                   → ADMIN, OPERADOR, VISUALIZADOR
TipoLocal              → LOJA, FULFILLMENT
TipoMovimentacao       → ENTRADA, SAIDA, TRANSFERENCIA, AJUSTE_INVENTARIO
StatusTransferencia    → PENDENTE, CONCLUIDA, CANCELADA
StatusInventario       → EM_ANDAMENTO, FINALIZADO
TipoImportacao         → BLING, FULFILLMENT, ENTRADA
StatusLote             → PROCESSADO, ERRO
```

#### 🔴 Exceptions (8 + Handler)
```
BusinessException (base abstrata)
├── ResourceNotFoundException    (404)
├── DuplicateResourceException   (409)
├── BadRequestException          (400)
├── UnauthorizedException        (401)
├── ForbiddenException           (403)
├── EstoqueInsuficienteException (422)
└── ArquivoInvalidoException     (400)

GlobalExceptionHandler → Tratamento centralizado
```

#### 🔵 Repositories (11)
Interfaces que estendem `JpaRepository<Entity, Long>`:
```
UsuarioRepository
ProdutoRepository
CategoriaRepository
FornecedorRepository
LocalEstoqueRepository
EstoqueProdutoRepository
MovimentacaoEstoqueRepository
TransferenciaRepository
InventarioRepository
ItemInventarioRepository
LoteImportacaoRepository
```

#### 🟢 Services (11)
```
UsuarioService          → CRUD + autenticação
ProdutoService          → CRUD + busca por SKU
CategoriaService        → CRUD
FornecedorService       → CRUD
EstoqueService          → Consultas + alertas
MovimentacaoService     → Entrada/saída + histórico
TransferenciaService    → Criar/concluir/cancelar
InventarioService       → Iniciar/finalizar + acuracidade
ImportacaoService       → Processar Excel
AlertaService           → Verificar estoque baixo
RelatorioService        → Gerar relatórios
```

#### 🟡 Controllers (7)
```
UsuarioController
ProdutoController
EstoqueController
MovimentacaoController
TransferenciaController
InventarioController
ImportacaoController
```

---

## 🔄 Fluxo de Dados

### Exemplo: Registrar Venda (Saída de Estoque)

```
1. CLIENT (Frontend/Postman)
   ↓
   POST /api/movimentacoes/saida
   {
     "produtoId": 1,
     "localId": 1,
     "quantidade": 5,
     "motivo": "Venda ML #12345"
   }

2. MovimentacaoController
   ↓
   @PostMapping("/saida")
   - Valida dados (Bean Validation)
   - Chama service

3. MovimentacaoService
   ↓
   - Busca produto e local
   - Valida estoque suficiente
   - Cria MovimentacaoEstoque
   - Atualiza EstoqueProduto
   - Salva tudo (transação)

4. Repositories
   ↓
   - MovimentacaoEstoqueRepository.save()
   - EstoqueProdutoRepository.save()

5. DATABASE (PostgreSQL)
   ↓
   - INSERT em movimentacao_estoque
   - UPDATE em estoque_produto

6. RESPONSE
   ↓
   MovimentacaoDTO com dados da movimentação criada
```

---

## 🛡️ Segurança

### Autenticação JWT

```
1. Login
   POST /api/usuarios/login
   ↓
   UsuarioController → UsuarioService
   ↓
   Valida credenciais
   ↓
   Gera token JWT
   ↓
   Retorna token

2. Requisições Protegidas
   GET /api/produtos
   Header: Authorization: Bearer {token}
   ↓
   JwtAuthenticationFilter
   ↓
   Valida token
   ↓
   Extrai usuário
   ↓
   SecurityContext
   ↓
   Controller (acesso autorizado)
```

### Controle de Acesso por Role

```
ADMIN        → Acesso total
OPERADOR     → CRUD produtos, movimentações, inventários
VISUALIZADOR → Apenas consultas
```

---

## 💾 Persistência

### JPA + Hibernate

- **ORM**: Mapeamento objeto-relacional
- **Lazy Loading**: Otimização de queries
- **Cascade**: Operações em cascata
- **Transações**: @Transactional para atomicidade

### Estratégias de ID

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Usa auto-increment do PostgreSQL para IDs.

### Auditoria

```java
@CreatedDate
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;
```

Spring Data JPA atualiza automaticamente.

---

## 🧩 Padrões de Design Utilizados

### 1. Repository Pattern
Abstração do acesso a dados.

```java
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findBySku(String sku);
}
```

### 2. DTO Pattern
Separação entre entidade e representação.

```java
// Entity (não exposta diretamente)
@Entity
public class Produto { ... }

// DTO (exposto na API)
public class ProdutoDTO { ... }
```

### 3. Service Layer Pattern
Lógica de negócio centralizada.

```java
@Service
public class ProdutoService {
    // Lógica de negócio aqui
}
```

### 4. Dependency Injection
Inversão de controle via Spring.

```java
@RestController
public class ProdutoController {
    @Autowired
    private ProdutoService service; // Injetado pelo Spring
}
```

### 5. Builder Pattern
Construção de objetos complexos (via Lombok).

```java
@Builder
public class ProdutoDTO { ... }

// Uso:
ProdutoDTO dto = ProdutoDTO.builder()
    .nome("Torneira")
    .preco(189.90)
    .build();
```

---

## 🔧 Componentes Auxiliares

### Config
- **SecurityConfig**: Configuração Spring Security + JWT
- **CorsConfig**: Configuração CORS para frontend
- **OpenApiConfig**: Documentação Swagger (futuro)

### Util
- **PasswordUtil**: Criptografia de senhas (BCrypt)
- **ExcelReader**: Leitura de arquivos Excel (Apache POI)
- **DateUtil**: Manipulação de datas

### Exception Handling
- **GlobalExceptionHandler**: Tratamento centralizado
- **ErrorResponse**: Formato padrão de erro

---

## 📊 Modelo de Dados

### Principais Relacionamentos

```
Usuario 1 ──── * MovimentacaoEstoque
Usuario 1 ──── * Transferencia
Usuario 1 ──── * Inventario
Usuario 1 ──── * LoteImportacao

Produto 1 ──── 1 Categoria
Produto 1 ──── * EstoqueProduto
Produto 1 ──── * MovimentacaoEstoque
Produto * ──── * Fornecedor

LocalEstoque 1 ──── * EstoqueProduto
LocalEstoque 1 ──── * MovimentacaoEstoque

Inventario 1 ──── * ItemInventario
```

**👉 [Ver modelo completo](Modelo-de-Dados)**

---

## 🚀 Performance

### Otimizações

1. **Indexes**: Campos frequentemente consultados (SKU, email, etc)
2. **Lazy Loading**: Relacionamentos carregados sob demanda
3. **Connection Pooling**: HikariCP (padrão Spring Boot)
4. **Caching**: Cache de segundo nível (futuro)
5. **Pagination**: Listagens paginadas por padrão

### Transações

```java
@Transactional
public MovimentacaoDTO registrarSaida(MovimentacaoCreateDTO dto) {
    // Tudo ou nada - atomicidade garantida
}
```

---

## 🧪 Testabilidade

### Arquitetura facilita testes

- **Controllers**: Testes com MockMvc
- **Services**: Testes unitários com Mockito
- **Repositories**: Testes com TestContainers
- **Integração**: Testes end-to-end

```java
@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {
    @MockBean
    private ProdutoService service;
    // ...
}
```

---

## 📈 Escalabilidade

### Horizontal
- Stateless (JWT sem sessão)
- Múltiplas instâncias atrás de load balancer

### Vertical
- Pool de conexões ajustável
- JVM tuning
- Índices no banco

---

## 🔮 Evolução Futura

### Microsserviços (Possível)
```
inventory-product-service
inventory-stock-service
inventory-report-service
inventory-import-service
```

### Event-Driven (Possível)
```
Kafka/RabbitMQ para eventos assíncronos:
- ProdutoCreated
- EstoqueBaixo
- InventarioFinalizado
```

---

## 📚 Referências

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

**[⬅️ Voltar para Home](Home)** | **[Próximo: Modelo de Dados →](Modelo-de-Dados)**
