# Interface Web • Hotel Nébula

Este módulo reúne a interface web do projeto **Hotel Nébula**, com foco em aprendizado prático de integração entre front-end e APIs REST.

## Resumo do projeto

A proposta é oferecer uma experiência simples e didática para consumir os recursos principais do domínio de hotelaria (hóspedes, quartos, reservas e dashboard), usando uma interface leve e acessível.

## Técnicas utilizadas

- **HTML semântico e acessível**: uso de landmarks (`header`, `main`, `section`), labels, navegação por teclado e feedback com `aria-live`.
- **Bootstrap 5**: estrutura visual rápida, responsiva e consistente com componentes de formulário, cards e tabelas.
- **JavaScript com `fetch`**: comunicação com a API para operações de leitura e escrita de dados.
- **Organização por páginas**: telas separadas para cadastro, busca, reserva e dashboard, facilitando manutenção.

## Aplicações práticas no aprendizado

- Entender o fluxo completo de uma aplicação web conectada a API.
- Praticar CRUD no front-end sem frameworks complexos.
- Exercitar boas práticas de acessibilidade desde o início.
- Validar e testar endpoints de forma visual para apoiar estudos de backend.

## Estrutura principal

- `vanilla/` → implementação em HTML, CSS e JavaScript puro.
- `vanilla/assets/styles/` → estilos complementares ao Bootstrap.
- `vanilla/assets/js/` → lógica de integração com a API via `fetch`.

## Observação

Este módulo é voltado para **estudo e prototipação**. Não é destinado a uso em produção sem camadas adicionais de segurança, validação e observabilidade.

