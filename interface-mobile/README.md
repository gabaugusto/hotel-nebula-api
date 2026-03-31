# Interface Mobile • Hotel Nebula

Este módulo contém a versão Android com **Jetpack Compose**, focada em **UI/UX** e navegação local, sem consumo de API por enquanto.

## Objetivo

Replicar a simplicidade estrutural da interface web vanilla e evoluir a experiência mobile com componentes reutilizáveis, visual moderno e fluxo de telas claro.

## Fluxo atual de navegação

1. **Login** (sem autenticação real)
2. **Perfil do usuário**
3. **Home** com seleção de quartos
4. **Reserva** do quarto escolhido
5. **Confirmação da reserva**

### Regras aplicadas

- Login navega para perfil.
- Perfil navega para home.
- Home envia para reserva ao escolher um quarto.
- Reserva envia para confirmação.
- Confirmação permite iniciar nova reserva ou voltar ao perfil.

## Estrutura principal

`app/src/main/java/com/hotelnebula/`

- `MainActivity.kt` → entrada da aplicação
- `ui/navigation/AppScreen.kt` → definição das rotas e modelo de quarto
- `ui/navigation/NebulaApp.kt` → orquestração de navegação e scaffold principal
- `ui/components/NebulaComponents.kt` → top bar, card base e hero reutilizáveis
- `ui/screens/LoginScreen.kt`
- `ui/screens/ProfileScreen.kt`
- `ui/screens/HomeScreen.kt`
- `ui/screens/ReservationScreen.kt`
- `ui/screens/ReservationConfirmationScreen.kt`

## Como executar

```powershell
Set-Location "interface-mobile"
.\gradlew.bat :app:assembleDebug
```

## Observação

Esta etapa é um protótipo de interface. As integrações com backend (API) podem ser conectadas na próxima fase sem alterar o fluxo visual já definido.