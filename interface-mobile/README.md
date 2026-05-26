# Interface Mobile - Hotel Nebula

Este modulo contem o aplicativo Android do Hotel Nebula feito com Jetpack Compose.
A versao atual conecta na API usando Retrofit e replica as principais funcoes da
interface web:

- configurar a URL da API;
- listar, buscar, cadastrar, atualizar e excluir hospedes;
- listar quartos disponiveis;
- criar reservas;
- consultar os indicadores do dashboard.

## Como executar

Abra um terminal na pasta `interface-mobile`:

```powershell
.\gradlew.bat clean assembleDebug
```

Para rodar os testes locais:

```powershell
.\gradlew.bat test
```

No emulador Android, a URL padrao da API e:

```text
http://10.0.2.2:8083/
```

Essa URL parece estranha no comeco, mas tem uma ideia simples: dentro do
emulador, `localhost` aponta para o proprio emulador. O endereco `10.0.2.2`
e o caminho especial para chegar no `localhost` do computador onde a API Spring
esta rodando.

## Estrutura geral

A parte principal da aplicacao esta em:

```text
app/src/main/java/com/example/hotel_nebula/
```

Ela e dividida assim:

```text
com/example/hotel_nebula/
  MainActivity.kt
  data/
    ApiModels.kt
    HotelNebulaApi.kt
    ApiClient.kt
    HotelRepository.kt
  viewmodel/
    HotelViewModel.kt
  ui/
    HotelNebulaApp.kt
    theme/
```

Tambem existe uma pasta antiga/prototipo em:

```text
app/src/main/java/com/hotelnebula/
```

Ela contem telas visuais de login, perfil, home e reserva, mas a aplicacao
conectada na API usa o pacote `com.example.hotel_nebula`. Se voce estiver
estudando a integracao com backend, comece sempre por `com.example.hotel_nebula`.

## A historia da chamada para a API

Pense no aplicativo como uma recepcao de hotel.

A tela e o atendente conversando com o usuario. O ViewModel e a pessoa que
organiza o pedido. O Repository e o funcionario que sabe falar com o sistema
do hotel. O Retrofit e o telefone usado para ligar para a API.

O caminho de uma acao e este:

```text
Tela Compose
  -> HotelViewModel
    -> HotelRepository
      -> HotelNebulaApi
        -> ApiClient / Retrofit
          -> API Spring Boot
```

Quando a resposta volta, ela faz o caminho contrario:

```text
API Spring Boot
  -> Retrofit
    -> Repository transforma em Result
      -> ViewModel atualiza AppUiState
        -> Compose redesenha a tela
```

## Backend files dentro do app

No Android, estes arquivos nao sao o backend real. O backend real esta nos
modulos `api-java-sql` e `api-kotlin-nosql`. Mas estes arquivos formam a
"ponte" entre o app e o backend. Para estudar, leia nesta ordem:

### 1. `data/ApiModels.kt`

Este arquivo define os modelos de dados que viajam entre o app e a API.

Exemplos:

- `Hospede`
- `Quarto`
- `Reserva`
- `DashboardFaturamento`
- `AvaliacoesResumo`

Cada classe representa o formato JSON esperado pelos endpoints. Por exemplo,
quando a API responde `GET /hospedes`, Retrofit transforma o JSON em uma lista
de `Hospede`.

Leia este arquivo primeiro porque ele mostra o vocabulario do sistema. Antes
de entender a conversa, precisamos conhecer as palavras.

### 2. `data/HotelNebulaApi.kt`

Este arquivo descreve os endpoints da API.

Ele e como uma lista de telefones da recepcao:

- `GET /hospedes`
- `GET /hospedes/{id}`
- `POST /hospedes`
- `PUT /hospedes/{id}`
- `DELETE /hospedes/{id}`
- `GET /quartos/disponiveis`
- `POST /reservas`
- `GET /dashboard/faturamento`
- `GET /avaliacoes/resumo`

As anotacoes do Retrofit, como `@GET`, `@POST`, `@PUT` e `@DELETE`, dizem qual
rota sera chamada. O corpo enviado para a API aparece com `@Body`, e valores
que entram na URL aparecem com `@Path`.

Leia este arquivo depois dos modelos porque aqui voce entende onde cada modelo
entra e sai.

### 3. `data/ApiClient.kt`

Este arquivo monta o Retrofit.

Ele configura:

- a URL base da API;
- o cliente HTTP OkHttp;
- o log basico das requisicoes;
- o conversor Gson, que transforma JSON em objetos Kotlin e objetos Kotlin em JSON.

Este e o momento em que o "telefone" fica pronto para uso. O app ainda nao fez
nenhuma chamada, mas ja sabe para qual endereco ligar e como traduzir as
mensagens.

### 4. `data/HotelRepository.kt`

Este arquivo e a camada de traducao entre Retrofit e o resto do app.

As telas nao chamam Retrofit diretamente. Elas pedem ajuda ao ViewModel, e o
ViewModel chama o Repository.

O Repository faz tres coisas importantes:

- chama os metodos definidos em `HotelNebulaApi`;
- verifica se a resposta HTTP deu certo;
- transforma erros tecnicos em mensagens mais claras para o usuario.

Por exemplo, em vez de deixar uma excecao crua aparecer na tela, ele retorna
mensagens como:

- `Erro HTTP 404`;
- `Tempo de conexao esgotado`;
- `Nao foi possivel conectar na API`.

Essa camada deixa o restante do app mais simples, porque todo mundo recebe um
`Result<T>`: sucesso com dados ou falha com mensagem.

### 5. `viewmodel/HotelViewModel.kt`

Este arquivo e o organizador da aplicacao.

Ele guarda o estado principal em `AppUiState`, incluindo:

- URL da API;
- indicador de carregamento;
- mensagem de sucesso ou erro;
- lista de hospedes;
- hospede encontrado;
- formulario de hospede;
- lista de quartos;
- formulario de reserva;
- dados do dashboard.

Quando o usuario toca em "Buscar", "Criar", "Atualizar", "Excluir" ou
"Recarregar", a tela chama uma funcao do ViewModel. O ViewModel valida os
campos, chama o Repository e atualiza o estado.

Compose observa esse estado. Quando o estado muda, a tela muda junto.

## Arquivos da interface

### `MainActivity.kt`

E a porta de entrada do app Android. Ela ativa o tema e chama `HotelNebulaApp`.

### `ui/HotelNebulaApp.kt`

Contem as telas Compose principais e a navegacao inferior:

- Inicio/configuracao da API;
- Hospedes;
- Cadastro;
- Reservas;
- Dashboard.

Este arquivo tambem mostra como os eventos da tela chegam ao ViewModel. Por
exemplo, o botao de salvar hospede chama `viewModel.criarHospede()`.

### `ui/theme/`

Contem cores, tipografia e tema Material 3. Estes arquivos controlam a aparencia
visual comum da aplicacao.

## Fluxo de exemplo: criar um hospede

1. O usuario preenche o formulario na tela de cadastro.
2. A tela chama `criarHospede()` no `HotelViewModel`.
3. O ViewModel valida ID, nome, e-mail e CPF.
4. O ViewModel monta um objeto `Hospede`.
5. O ViewModel chama `HotelRepository.criarHospede(hospede)`.
6. O Repository chama `HotelNebulaApi.criarHospede(...)`.
7. Retrofit envia `POST /hospedes` para a API.
8. A API responde sucesso ou erro.
9. O Repository transforma a resposta em `Result`.
10. O ViewModel atualiza `AppUiState`.
11. Compose redesenha a tela e mostra a mensagem.

## Fluxo de exemplo: dashboard

1. A tela de dashboard chama `carregarDashboard()`.
2. O ViewModel pede dois dados ao Repository:
   `buscarFaturamento()` e `buscarResumoAvaliacoes()`.
3. O Repository chama:
   `GET /dashboard/faturamento` e `GET /avaliacoes/resumo`.
4. Quando as duas respostas chegam, o ViewModel atualiza:
   `faturamento` e `avaliacoes`.
5. A tela mostra total de hospedagens, faturamento total, total de avaliacoes
   e media geral.

## Dicas para estudantes

- Comece lendo `ApiModels.kt`; ele mostra quais dados existem.
- Depois leia `HotelNebulaApi.kt`; ele mostra quais endpoints o app usa.
- Em seguida leia `HotelRepository.kt`; ele mostra como erros HTTP viram
  mensagens compreensiveis.
- Por fim leia `HotelViewModel.kt` junto com `HotelNebulaApp.kt`; ali voce ve
  a ligacao entre clique, estado e tela.

Se algo falhar no app, investigue nesta ordem:

1. A API esta rodando?
2. A URL base esta correta?
3. O endpoint existe nas APIs Java/Kotlin?
4. O modelo em `ApiModels.kt` combina com o JSON?
5. O Repository esta retornando erro?
6. O ViewModel esta atualizando o estado esperado?
