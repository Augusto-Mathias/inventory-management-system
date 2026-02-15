# 🏠 Wiki - Inventory Management System

Bem-vindo à documentação completa do **Inventory Management System**!

Este sistema foi desenvolvido para resolver os desafios de gestão de estoque em e-commerce com vendas em múltiplos marketplaces.

---

## 📚 Índice da Documentação

### 🎯 Início Rápido
- **[Home](Home)** - Você está aqui
- **[Instalação](Instalação)** - Como instalar e configurar
- **[Primeiros Passos](Primeiros-Passos)** - Tutorial para começar

### 🏗️ Arquitetura
- **[Visão Geral da Arquitetura](Arquitetura)** - Como o sistema foi estruturado
- **[Diagrama de Classes](Diagrama-de-Classes)** - Modelo completo de classes
- **[Modelo de Dados](Modelo-de-Dados)** - Entidades e relacionamentos
- **[Padrões de Design](Padrões-de-Design)** - Padrões utilizados

### 📖 Guias
- **[Guia de Desenvolvimento](Guia-de-Desenvolvimento)** - Como adicionar features
- **[Configuração do Ambiente](Configuração-do-Ambiente)** - Setup completo
- **[Integração com NeonDB](Integração-NeonDB)** - Configurar banco de dados
- **[Importação de Excel](Importação-Excel)** - Como funciona a importação

### 🔌 API
- **[Documentação da API](API-Documentation)** - Endpoints REST
- **[Autenticação](Autenticação)** - JWT e segurança
- **[Exemplos de Uso](Exemplos-API)** - Casos práticos

### 📦 Funcionalidades
- **[Gestão de Produtos](Gestão-de-Produtos)** - CRUD e consultas
- **[Controle de Estoque](Controle-de-Estoque)** - Multi-local
- **[Movimentações](Movimentações)** - Entradas, saídas e transferências
- **[Inventário](Inventário)** - Como realizar inventários
- **[Alertas](Alertas)** - Sistema de notificações

### 🧪 Testes
- **[Guia de Testes](Guia-de-Testes)** - Como testar
- **[Testes Unitários](Testes-Unitários)** - Com JUnit e Mockito
- **[Testes de Integração](Testes-Integração)** - Com TestContainers

### 🚀 Deploy
- **[Deploy em Produção](Deploy-Produção)** - Como fazer deploy
- **[Docker](Docker)** - Containerização
- **[CI/CD](CI-CD)** - GitHub Actions

### 🤝 Contribuição
- **[Como Contribuir](Como-Contribuir)** - Guia para contribuidores
- **[Código de Conduta](Código-de-Conduta)** - Regras da comunidade
- **[Roadmap](Roadmap)** - Próximas features

---

## 🎯 Sobre o Sistema

### Problema Resolvido

Empresas que vendem em múltiplos canais (Mercado Livre, Amazon, Shopee, Magalu, etc.) enfrentam:

❌ **Dificuldade** em controlar estoque em diferentes locais  
❌ **Falta de visibilidade** do inventário total  
❌ **Processo manual** de importação de vendas  
❌ **Divergências** entre estoque físico e sistema  
❌ **Falta de alertas** quando produtos estão acabando  

### Nossa Solução

✅ **Controle multi-local** (loja física + fulfillment)  
✅ **Importação automática** via Excel  
✅ **Visão unificada** do estoque  
✅ **Sistema de inventário** com acuracidade  
✅ **Alertas automáticos** de estoque baixo  
✅ **Histórico completo** de movimentações  
✅ **Controle de acesso** por perfil de usuário  

---

## 🛠️ Stack Tecnológica

### Backend
- **Java 17+** - Linguagem robusta e confiável
- **Spring Boot 3.2** - Framework moderno
- **Spring Data JPA** - ORM simplificado
- **Spring Security** - Segurança enterprise
- **PostgreSQL** - Banco de dados relacional

### Qualidade
- **Lombok** - Menos boilerplate
- **JUnit 5** - Testes unitários
- **Mockito** - Mocks para testes
- **TestContainers** - Testes de integração

### DevOps
- **Maven** - Build e dependências
- **Docker** - Containerização
- **GitHub Actions** - CI/CD
- **NeonDB** - PostgreSQL serverless

---

## 📊 Principais Entidades

```
Usuario          → Usuários do sistema
Produto          → Produtos do estoque
Categoria        → Categorias dos produtos
LocalEstoque     → Locais de armazenamento
EstoqueProduto   → Quantidade por produto/local
MovimentacaoEstoque → Histórico de movimentações
Transferencia    → Transferências entre locais
Inventario       → Inventários realizados
Fornecedor       → Fornecedores
LoteImportacao   → Importações de Excel
```

**👉 [Ver modelo completo](Modelo-de-Dados)**

---

## 🔑 Conceitos Importantes

### Multi-Local
O sistema permite gerenciar estoque em múltiplos locais simultaneamente:
- **Loja Física**: Estoque na loja
- **Fulfillment**: Estoque em centro de distribuição

### Movimentações
Todos os eventos de estoque são registrados:
- **ENTRADA**: Compras, recebimentos
- **SAIDA**: Vendas, perdas
- **TRANSFERENCIA**: Movimentação entre locais
- **AJUSTE_INVENTARIO**: Correções após inventário

### Importação
Processa planilhas Excel com vendas:
- **Bling**: Vendas do ERP Bling
- **Fulfillment**: Vendas do fulfillment
- **Entrada**: Compras de fornecedores

### Inventário
Processo de contagem física:
1. Iniciar inventário
2. Adicionar itens contados
3. Calcular divergências
4. Finalizar e ajustar estoque automaticamente

---

## 🚀 Começando

### 1. Pré-requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL ou conta NeonDB
- Git

### 2. Clonar e Configurar
```bash
git clone https://github.com/seu-usuario/inventory-management-system.git
cd inventory-management-system

# Configurar banco de dados
cp .env.example .env
# Editar .env com suas credenciais

# Executar
mvn spring-boot:run
```

**👉 [Guia completo de instalação](Instalação)**

### 3. Primeiro Uso
```bash
# Criar usuário admin
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Admin",
    "email": "admin@example.com",
    "senha": "senha123",
    "perfil": "ADMIN"
  }'
```

**👉 [Tutorial completo](Primeiros-Passos)**

---

## 📖 Exemplos Práticos

### Caso de Uso 1: Entrada de Produtos
```
1. Criar fornecedor
2. Criar categoria
3. Criar produto
4. Registrar entrada no estoque
5. Verificar estoque atualizado
```

### Caso de Uso 2: Importar Vendas
```
1. Exportar vendas do Bling (Excel)
2. Fazer upload no sistema
3. Sistema processa e dá baixa automática
4. Verificar movimentações registradas
```

### Caso de Uso 3: Inventário
```
1. Iniciar inventário do local
2. Contar produtos fisicamente
3. Registrar quantidades no sistema
4. Finalizar inventário
5. Sistema ajusta divergências automaticamente
```

**👉 [Mais exemplos na documentação da API](Exemplos-API)**

---

## 🎓 Recursos de Aprendizado

### Vídeos (em breve)
- [ ] Instalação e configuração
- [ ] Tour pelo sistema
- [ ] Criando seu primeiro produto
- [ ] Importação de Excel passo a passo

### Tutoriais
- **[Primeiros Passos](Primeiros-Passos)** - Setup inicial
- **[Configurando o NeonDB](Integração-NeonDB)** - Banco em nuvem
- **[Importação Automática](Importação-Excel)** - Excel para estoque

---

## 💬 Suporte

Precisa de ajuda?

- 📖 **[Documentação](../../wiki)** - Leia a wiki completa
- 🐛 **[Issues](../../issues)** - Reporte bugs
- 💡 **[Discussions](../../discussions)** - Tire dúvidas
- 📧 **Email**: Contate os mantenedores

---

## 🤝 Contribua

Este é um projeto open-source! Contribuições são bem-vindas:

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/NovaFeature`)
3. Commit (`git commit -m 'Adiciona NovaFeature'`)
4. Push (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

**👉 [Guia completo de contribuição](Como-Contribuir)**

---

## 📜 Licença

Este projeto está sob a licença MIT. Veja [LICENSE](../../LICENSE) para detalhes.

---

## 🌟 Agradecimentos

Obrigado a todos que contribuíram para este projeto!

---

**Navegue pela wiki usando o menu lateral →**

**[⬅️ Voltar ao repositório](../../)**
