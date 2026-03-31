# API Clients (Postman + Insomnia)

Esta pasta centraliza os arquivos de testes manuais das APIs do projeto `hotel-nebula-api`.

## Estrutura

- `postman/`
  - `hotel-nebula-kotlin-nosql.postman_collection.json`
- `insomnia/`
  - `hotel-nebula-kotlin-nosql.insomnia-export.json` (workspace + environment + requests)

## Objetivo

Padronizar os testes de endpoints entre desenvolvedores, evitando coleções espalhadas por módulo e facilitando onboarding do time.

## Como importar no Postman

1. Abra o Postman.
2. Clique em **Import**.
3. Selecione o arquivo:
   - `api-clients/postman/hotel-nebula-kotlin-nosql.postman_collection.json`
4. Garanta que a variável `baseUrl` esteja definida para `http://localhost:8083`.

## Como importar no Insomnia

1. Abra o Insomnia.
2. Vá em **Application > Preferences > Data > Import Data** (ou opção equivalente da versão).
3. Importe o arquivo:
   - `api-clients/insomnia/hotel-nebula-kotlin-nosql.insomnia-export.json`
4. Confira o environment `Base Environment` com:
   - `baseUrl`: `http://localhost:8083`
   - `mongoUri`: `mongodb://localhost:27017/hotel_nebula`

## Convenções

- Sempre atualizar os arquivos desta pasta quando novos endpoints forem adicionados.
- Evitar criar coleções duplicadas em submódulos.
- Manter nomes de requests orientados à ação (ex.: `Listar hóspedes`, `Criar reserva`).
- Versionar alterações junto com a mudança de endpoint correspondente.

## Observações

- A coleção atual cobre os principais endpoints CRUD e rotas avançadas do módulo Kotlin/NoSQL.
- Se precisar de ambiente por stage (dev/homolog/prod), criar novos environments no export do Insomnia e variáveis equivalentes no Postman.
