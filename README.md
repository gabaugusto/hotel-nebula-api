# Hotel Nébula API

Projeto educacional para iniciantes que desejam praticar desenvolvimento de software em um cenário realista de hotelaria, com API, dados e interfaces.

## Sobre o projeto

O **Hotel Nébula** foi pensado como um laboratório completo de aprendizado. Aqui você pode explorar diferentes stacks mantendo o mesmo domínio de negócio (hóspedes, quartos, reservas, hospedagens, serviços e avaliações).

Você pode estudar:

- API com **Java + Spring Boot + MySQL**
- API com **Kotlin + Spring Boot + MongoDB**
- Dados relacionais e não relacionais
- Integração com interfaces Web e Mobile

## Estrutura do repositório

- [`api-java-sql`](./api-java-sql/) → API REST com Java e banco relacional (MySQL)
- [`api-kotlin-nosql`](./api-kotlin-nosql/) → API REST com Kotlin e banco NoSQL (MongoDB)
- [`data`](./data/) → scripts SQL e coleções JSON de dados de exemplo
- [`api-clients`](./api-clients/) → coleções Postman/Insomnia para testes
- [`interface-web`](./interface-web/) → base para interface web
- [`interface-mobile`](./interface-mobile/) → base para interface mobile

## Para quem este projeto é indicado

Este projeto é voltado para:

- estudantes e iniciantes em backend
- pessoas aprendendo Spring Boot
- prática de integração com banco de dados e APIs REST

## Aviso importante de uso

> Este repositório é **educacional** e foi criado para estudo.
> **Não é recomendado para uso em produção**, ambientes profissionais ou cenários que exijam requisitos robustos de segurança, compliance e alta disponibilidade.

## Começando rapidamente

1. Escolha um dos módulos de API:
	- [`api-java-sql`](./api-java-sql/)
	- [`api-kotlin-nosql`](./api-kotlin-nosql/)
2. Siga o `README.md` do módulo escolhido.
3. Carregue os dados de exemplo da pasta [`data`](./data/).
4. Teste os endpoints com os arquivos de [`api-clients`](./api-clients/).

## Contribuição

Contribuições são bem-vindas, especialmente melhorias didáticas para iniciantes.

- Leia o guia em [`CONTRIBUTING.md`](./CONTRIBUTING.md)
- Abra uma issue com contexto claro
- Envie PRs pequenos e objetivos

## Licença

Este projeto está licenciado sob a licença MIT. Veja [`LICENSE`](./LICENSE).