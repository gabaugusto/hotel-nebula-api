# Interface Web React (Vite)

Este módulo implementa a mesma proposta funcional da pasta `../vanilla`, agora com React e Vite.

## Objetivo

Fornecer uma interface didática com:

- páginas equivalentes à versão vanilla
- menu componentizado e reutilizável
- consumo da API via `fetch` centralizado em serviço
- configuração por variável de ambiente (`.env`)

## Estrutura principal

- `src/components/`
	- `AppLayout.jsx` (layout e estrutura principal)
	- `AppNavbar.jsx` (menu reutilizável)
	- `StatusAlert.jsx` (feedback acessível)
- `src/pages/`
	- `HomePage.jsx`
	- `BuscarHospedesPage.jsx`
	- `CadastrarHospedePage.jsx`
	- `ReservarQuartosPage.jsx`
	- `DashboardPage.jsx`
- `src/services/api.js`
	- função central de requests e endpoints da aplicação

## Variáveis de ambiente

Arquivo: `.env`

```env
VITE_API_BASE_URL=http://localhost:8083
```

## Rodando o projeto

```bash
npm install
npm run dev
```

Build de produção:

```bash
npm run build
```

## Observação

Este projeto tem foco educacional e não aplica camadas completas de segurança para produção.
