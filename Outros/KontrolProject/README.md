# Kontrol

Aplicativo Android de organização pessoal com Finanças, Controle de Horas, Rotina, Mercado e Notas.

## Projetos
- `Kontrol/` — Android Kotlin + Jetpack Compose + Retrofit.
- `KontrolAPI/` — Kotlin + Spring Boot + MySQL + JWT.

## Executar a API
1. Instale Docker.
2. No diretório `KontrolAPI`, execute `docker compose up -d`.
3. Abra o projeto no IntelliJ/Android Studio ou rode `./gradlew bootRun`.
4. API: `http://localhost:8080/api`.

## Executar Android
Abra `Kontrol/` no Android Studio.
- Emulador Android: a API usa `http://10.0.2.2:8080/api/`.
- Dispositivo físico: altere `BASE_URL` em `ApiConfig.kt` para o IP da máquina na rede local.

O projeto é um MVP funcional, com autenticação JWT, dashboard, finanças, marcação de ponto, notas, mercado e rotina. Room/DataStore estão preparados para evolução; o MVP usa DataStore para sessão e Retrofit como fonte principal.
