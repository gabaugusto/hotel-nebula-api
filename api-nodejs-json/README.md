# Hotel Nebula API (Node.js)

Implementacao da API do Hotel Nebula em Node.js usando Express, mantendo o mesmo dominio e endpoints principais dos modulos Java/SQL e Kotlin/NoSQL.

## Stack

- Node.js 20+
- Express
- CORS
- Persistencia em arquivo JSON local (`api-nodejs/data/database.json`)

## Como funciona o dado inicial

Na primeira execucao, o modulo cria `data/database.json` com base nos seeds em `../data/colecoes/*.json`.
Depois disso, operacoes de escrita (POST, PUT, DELETE) persistem nesse arquivo local.

## Executar

```bash
npm install
npm run dev
```

Servidor padrao: `http://localhost:8083`

Se precisar mudar porta:

```bash
PORT=8084 npm run dev
```

PowerShell:

```powershell
$env:PORT=8084; npm run dev
```

## Endpoints

### Hospedes
- `GET /hospedes`
- `GET /hospedes/{id}`
- `GET /hospedes/email/{email}`
- `GET /hospedes/historico/{id}`
- `POST /hospedes`
- `PUT /hospedes/{id}`
- `DELETE /hospedes/{id}`

### Quartos
- `GET /quartos`
- `GET /quartos/{id}`
- `GET /quartos/disponiveis`
- `POST /quartos`
- `PUT /quartos/{id}`
- `DELETE /quartos/{id}`

### Reservas
- `GET /reservas`
- `GET /reservas/{id}`
- `GET /reservas/ativas`
- `POST /reservas`
- `PUT /reservas/{id}`
- `DELETE /reservas/{id}`

### Hospedagens
- `GET /hospedagens`
- `GET /hospedagens/{id}`
- `POST /hospedagens`
- `PUT /hospedagens/{id}`
- `DELETE /hospedagens/{id}`

### Servicos
- `GET /servicos`
- `GET /servicos/{id}`
- `GET /servicos/disponiveis`
- `POST /servicos`
- `PUT /servicos/{id}`
- `DELETE /servicos/{id}`

### Avaliacoes
- `GET /avaliacoes`
- `GET /avaliacoes/{id}`
- `GET /avaliacoes/resumo`
- `POST /avaliacoes`
- `PUT /avaliacoes/{id}`
- `DELETE /avaliacoes/{id}`

### Dashboard
- `GET /dashboard/faturamento`

### Health
- `GET /health`

## Observacoes

- O foco segue educacional, nao producao.
- Por padrao, esta API tambem usa a porta `8083`; execute apenas um backend por vez (ou ajuste `PORT`).
