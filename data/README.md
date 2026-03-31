# Dados do Projeto

Esta pasta concentra os dados de apoio usados para desenvolvimento, testes e demonstrações do **Hotel Nebula API**.

## Qual o papel dos dados neste projeto?

- Servir como base inicial para subir os ambientes localmente.
- Padronizar exemplos para toda a equipe (mesmos registros de teste).
- Permitir validar endpoints de forma rápida, sem precisar criar dados manualmente.

## Tipos de dados encontrados

### 1) Dados relacionais (SQL)

- Arquivo: `exemplos_hotel_nebula.sql`
- Uso: cria banco, tabelas, relacionamentos e registros iniciais para a API **Java/SQL** (MySQL).

### 2) Dados NoSQL (JSON por coleção)

- Pasta: `colecoes/`
- Arquivos: `hospedes.json`, `quartos.json`, `reservas.json`, `hospedagens.json`, `servicos.json`, `feedbacks.json`, entre outros.
- Uso: alimentar coleções MongoDB para a API **Kotlin/NoSQL**.

## Observação

Os dados desta pasta são voltados para ambiente de desenvolvimento e estudo. Para produção, recomenda-se usar scripts de migração ou ferramentas de seed específicas, garantindo controle de versão e segurança.