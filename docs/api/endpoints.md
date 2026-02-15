# 📡 API Documentation

Base URL: `http://localhost:8080/api`

## 🔐 Autenticação

Todos os endpoints (exceto login e registro) requerem autenticação via JWT Token.

### Incluir token nas requisições:
```http
Authorization: Bearer {seu_token_jwt}
```

---

## 👤 Usuários

### Criar Usuário
```http
POST /usuarios
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "senha123",
  "perfil": "OPERADOR"
}
```

**Perfis disponíveis:** `ADMIN`, `OPERADOR`, `VISUALIZADOR`

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "perfil": "OPERADOR",
  "ativo": true,
  "createdAt": "2025-02-15T10:30:00"
}
```

### Login
```http
POST /usuarios/login
Content-Type: application/json

{
  "email": "joao@example.com",
  "senha": "senha123"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "perfil": "OPERADOR"
  }
}
```

### Listar Usuários
```http
GET /usuarios
```

### Buscar Usuário
```http
GET /usuarios/{id}
```

### Atualizar Usuário
```http
PUT /usuarios/{id}
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "email": "joao.santos@example.com",
  "perfil": "ADMIN",
  "ativo": true
}
```

### Deletar Usuário
```http
DELETE /usuarios/{id}
```

---

## 📦 Produtos

### Criar Produto
```http
POST /produtos
Content-Type: application/json

{
  "sku": "TORN-001",
  "nome": "Torneira Monocomando",
  "descricao": "Torneira monocomando para cozinha",
  "preco": 189.90,
  "categoriaId": 1,
  "ativo": true,
  "imagem": "url_da_imagem.jpg"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "sku": "TORN-001",
  "nome": "Torneira Monocomando",
  "descricao": "Torneira monocomando para cozinha",
  "preco": 189.90,
  "categoria": {
    "id": 1,
    "nome": "Torneiras"
  },
  "ativo": true,
  "imagem": "url_da_imagem.jpg",
  "estoqueTotal": 0,
  "createdAt": "2025-02-15T10:30:00"
}
```

### Listar Produtos
```http
GET /produtos
GET /produtos?nome=torneira          # Buscar por nome
GET /produtos?categoriaId=1          # Filtrar por categoria
GET /produtos?ativo=true             # Apenas ativos
```

### Buscar Produto por ID
```http
GET /produtos/{id}
```

### Buscar Produto por SKU
```http
GET /produtos/sku/{sku}
```

### Atualizar Produto
```http
PUT /produtos/{id}
Content-Type: application/json

{
  "nome": "Torneira Monocomando Premium",
  "preco": 249.90,
  "ativo": true
}
```

### Deletar Produto
```http
DELETE /produtos/{id}
```

---

## 📂 Categorias

### Criar Categoria
```http
POST /categorias
Content-Type: application/json

{
  "nome": "Torneiras",
  "descricao": "Torneiras para cozinha e banheiro",
  "categoriaPaiId": null
}
```

### Listar Categorias
```http
GET /categorias
GET /categorias?raiz=true    # Apenas categorias raiz (sem pai)
```

### Buscar Categoria
```http
GET /categorias/{id}
```

### Atualizar Categoria
```http
PUT /categorias/{id}
```

### Deletar Categoria
```http
DELETE /categorias/{id}
```

---

## 📊 Estoque

### Consultar Estoque de Produto
```http
GET /estoque/{produtoId}
```

**Resposta (200 OK):**
```json
{
  "produtoId": 1,
  "produtoNome": "Torneira Monocomando",
  "produtoSku": "TORN-001",
  "quantidadePorLocal": {
    "Loja Física": 15,
    "Fulfillment": 30
  },
  "estoqueTotal": 45,
  "estoqueMinimo": 10,
  "alertaEstoqueBaixo": false
}
```

### Consultar Estoque por Local
```http
GET /estoque/{produtoId}/local/{localId}
```

**Resposta (200 OK):**
```json
{
  "quantidade": 15
}
```

### Listar Produtos com Estoque Baixo
```http
GET /estoque/alertas
```

**Resposta (200 OK):**
```json
[
  {
    "produtoId": 2,
    "produtoNome": "Cuba de Inox",
    "produtoSku": "CUBA-001",
    "estoqueTotal": 3,
    "estoqueMinimo": 5,
    "alertaEstoqueBaixo": true
  }
]
```

### Configurar Estoque Mínimo/Máximo
```http
PUT /estoque/configurar
Content-Type: application/json

{
  "produtoId": 1,
  "localId": 1,
  "estoqueMinimo": 10,
  "estoqueMaximo": 100
}
```

---

## 🔄 Movimentações

### Registrar Entrada
```http
POST /movimentacoes/entrada
Content-Type: application/json

{
  "produtoId": 1,
  "localId": 1,
  "quantidade": 50,
  "motivo": "Compra do fornecedor XYZ",
  "usuarioId": 1
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "produtoNome": "Torneira Monocomando",
  "localNome": "Loja Física",
  "quantidade": 50,
  "tipo": "ENTRADA",
  "dataHora": "2025-02-15T10:30:00",
  "motivo": "Compra do fornecedor XYZ",
  "usuarioNome": "João Silva"
}
```

### Registrar Saída
```http
POST /movimentacoes/saida
Content-Type: application/json

{
  "produtoId": 1,
  "localId": 1,
  "quantidade": 5,
  "motivo": "Venda Mercado Livre #12345",
  "usuarioId": 1
}
```

### Listar Movimentações por Produto
```http
GET /movimentacoes/produto/{produtoId}
GET /movimentacoes/produto/{produtoId}?tipo=ENTRADA
GET /movimentacoes/produto/{produtoId}?dataInicio=2025-01-01&dataFim=2025-01-31
```

### Listar Movimentações por Local
```http
GET /movimentacoes/local/{localId}
```

### Listar Movimentações por Período
```http
GET /movimentacoes?dataInicio=2025-01-01&dataFim=2025-01-31
```

---

## 🔁 Transferências

### Criar Transferência
```http
POST /transferencias
Content-Type: application/json

{
  "produtoId": 1,
  "localOrigemId": 1,
  "localDestinoId": 2,
  "quantidade": 10,
  "observacao": "Transferência para fulfillment",
  "usuarioId": 1
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "produtoNome": "Torneira Monocomando",
  "localOrigemNome": "Loja Física",
  "localDestinoNome": "Fulfillment",
  "quantidade": 10,
  "data": "2025-02-15T10:30:00",
  "status": "PENDENTE",
  "usuarioNome": "João Silva"
}
```

### Concluir Transferência
```http
PUT /transferencias/{id}/concluir
```

### Cancelar Transferência
```http
PUT /transferencias/{id}/cancelar
```

### Listar Transferências Pendentes
```http
GET /transferencias/pendentes
```

### Buscar Transferência
```http
GET /transferencias/{id}
```

---

## 📋 Inventário

### Iniciar Inventário
```http
POST /inventarios
Content-Type: application/json

{
  "localId": 1,
  "observacao": "Inventário mensal de fevereiro",
  "usuarioId": 1
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "localNome": "Loja Física",
  "data": "2025-02-15T10:30:00",
  "status": "EM_ANDAMENTO",
  "valorTotalSistema": 0.00,
  "valorTotalContado": 0.00,
  "acuracidadeGeral": 0.00,
  "totalItens": 0
}
```

### Adicionar Item ao Inventário
```http
POST /inventarios/{inventarioId}/itens
Content-Type: application/json

{
  "produtoId": 1,
  "quantidadeInventariada": 48,
  "observacao": "Encontradas 2 unidades danificadas"
}
```

**Resposta (200 OK):**
```json
{
  "produtoNome": "Torneira Monocomando",
  "quantidadeSistema": 50,
  "quantidadeInventariada": 48,
  "diferenca": -2,
  "acuracidadeItem": 96.00,
  "valorUnitario": 189.90,
  "valorTotalSistema": 9495.00,
  "valorTotalContado": 9115.20,
  "valorDiferenca": -379.80,
  "ajustado": false
}
```

### Finalizar Inventário
```http
PUT /inventarios/{id}/finalizar
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "localNome": "Loja Física",
  "data": "2025-02-15T10:30:00",
  "status": "FINALIZADO",
  "valorTotalSistema": 50000.00,
  "valorTotalContado": 49500.00,
  "acuracidadeGeral": 99.00,
  "totalItens": 25
}
```

### Buscar Inventário
```http
GET /inventarios/{id}
```

### Listar Inventários por Local
```http
GET /inventarios/local/{localId}
```

---

## 📥 Importação

### Importar Excel
```http
POST /importacoes/upload
Content-Type: multipart/form-data

file: arquivo.xlsx
localId: 1
tipo: BLING
usuarioId: 1
```

**Tipos disponíveis:** `BLING`, `FULFILLMENT`, `ENTRADA`

**Formato do Excel esperado:**
| Nome do Produto | Quantidade |
|----------------|------------|
| Torneira Monocomando | 5 |
| Cuba de Inox | 3 |

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nomeArquivo": "vendas_janeiro_2025.xlsx",
  "dataImportacao": "2025-02-15T10:30:00",
  "tipo": "BLING",
  "totalItens": 25,
  "status": "PROCESSADO",
  "localNome": "Loja Física",
  "usuarioNome": "João Silva"
}
```

### Listar Importações
```http
GET /importacoes
GET /importacoes?status=PROCESSADO
GET /importacoes?tipo=BLING
```

### Buscar Importação
```http
GET /importacoes/{id}
```

---

## 🏢 Locais de Estoque

### Criar Local
```http
POST /locais
Content-Type: application/json

{
  "nome": "Loja Física",
  "endereco": "Rua das Flores, 123 - Joinville/SC",
  "tipo": "LOJA",
  "ativo": true
}
```

**Tipos disponíveis:** `LOJA`, `FULFILLMENT`

### Listar Locais
```http
GET /locais
GET /locais?tipo=FULFILLMENT
GET /locais?ativo=true
```

### Buscar Local
```http
GET /locais/{id}
```

### Atualizar Local
```http
PUT /locais/{id}
```

### Deletar Local
```http
DELETE /locais/{id}
```

---

## 🏭 Fornecedores

### Criar Fornecedor
```http
POST /fornecedores
Content-Type: application/json

{
  "nome": "Metalúrgica Silva Ltda",
  "cnpj": "12.345.678/0001-90",
  "contato": "Maria Silva",
  "telefone": "(47) 99999-9999",
  "email": "contato@metalurgica.com",
  "endereco": "Rua Industrial, 456 - Joinville/SC"
}
```

### Listar Fornecedores
```http
GET /fornecedores
GET /fornecedores?nome=silva
```

### Buscar Fornecedor
```http
GET /fornecedores/{id}
```

### Buscar por CNPJ
```http
GET /fornecedores/cnpj/{cnpj}
```

### Atualizar Fornecedor
```http
PUT /fornecedores/{id}
```

### Deletar Fornecedor
```http
DELETE /fornecedores/{id}
```

---

## ⚠️ Códigos de Status HTTP

| Código | Significado |
|--------|------------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Requisição bem-sucedida sem conteúdo de retorno |
| 400 | Bad Request - Dados inválidos |
| 401 | Unauthorized - Não autenticado |
| 403 | Forbidden - Sem permissão |
| 404 | Not Found - Recurso não encontrado |
| 409 | Conflict - Conflito (ex: email duplicado) |
| 422 | Unprocessable Entity - Estoque insuficiente |
| 500 | Internal Server Error - Erro no servidor |

---

## 🐛 Formato de Erro

Todos os erros seguem o formato padrão:

```json
{
  "timestamp": "2025-02-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Produto com ID 999 não encontrado",
  "path": "/api/produtos/999"
}
```

---

## 📊 Paginação

Endpoints de listagem suportam paginação:

```http
GET /produtos?page=0&size=20&sort=nome,asc
```

**Parâmetros:**
- `page`: Número da página (começa em 0)
- `size`: Itens por página
- `sort`: Campo de ordenação + direção (asc/desc)

**Resposta:**
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 5,
  "size": 20,
  "number": 0
}
```

---

## 🔍 Filtros Avançados

Muitos endpoints suportam filtros via query parameters:

```http
GET /produtos?nome=torneira&categoriaId=1&ativo=true&precoMin=100&precoMax=500
GET /movimentacoes?dataInicio=2025-01-01&dataFim=2025-01-31&tipo=ENTRADA&localId=1
```

---

## 📝 Notas Importantes

1. **Autenticação**: Guarde o token JWT após login e inclua em todas as requisições
2. **Estoque Insuficiente**: Saídas/Transferências validam estoque disponível
3. **Importação**: O sistema tenta encontrar produtos por nome exato (case-insensitive)
4. **Inventário**: Ao finalizar, o sistema ajusta automaticamente o estoque
5. **Soft Delete**: Usuários e produtos são desativados, não deletados fisicamente

---

## 🧪 Exemplos com cURL

### Criar Produto e Registrar Entrada

```bash
# 1. Fazer login
TOKEN=$(curl -s -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","senha":"senha123"}' \
  | jq -r '.token')

# 2. Criar categoria
CATEGORIA_ID=$(curl -s -X POST http://localhost:8080/api/categorias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Torneiras","descricao":"Torneiras diversas"}' \
  | jq -r '.id')

# 3. Criar produto
PRODUTO_ID=$(curl -s -X POST http://localhost:8080/api/produtos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"sku\":\"TORN-001\",\"nome\":\"Torneira Monocomando\",\"preco\":189.90,\"categoriaId\":$CATEGORIA_ID}" \
  | jq -r '.id')

# 4. Registrar entrada de estoque
curl -X POST http://localhost:8080/api/movimentacoes/entrada \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"produtoId\":$PRODUTO_ID,\"localId\":1,\"quantidade\":50,\"motivo\":\"Compra inicial\"}"
```

---

**[⬅️ Voltar ao README](../../README.md)**
