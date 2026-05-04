# VeciApp API

API backend para la aplicacion movil `VeciApp`, enfocada en seguridad ciudadana, reportes, emergencias, suscripciones y grupo familiar.

## Stack

- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Docker

## Funcionalidades base

- Registro e inicio de sesion con JWT
- Perfil del usuario y actualizacion de ubicacion
- Envio de alertas de emergencia
- Registro de reportes ciudadanos
- Historial consolidado de actividad
- Suscripciones `BASIC`, `PREMIUM` y `FAMILY`
- Gestion de miembros familiares y mapa familiar
- Dashboard inicial para la pantalla principal

## Variables de entorno

La API lee estas variables:

- `PORT`
- `DB_URL`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `DB_SSL_MODE`
- `DB_MAX_POOL_SIZE`
- `DB_MIN_IDLE`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `APP_CORS_ALLOWED_ORIGINS`
- `SHOW_SQL`

Puedes usar `DB_URL` completo o la combinacion `DB_HOST`, `DB_PORT`, `DB_NAME`.
Si tu plan de Aiven tiene pocas conexiones disponibles, usa `DB_MAX_POOL_SIZE=1` y `DB_MIN_IDLE=0`.

## Ejecucion local

```bash
mvn spring-boot:run
```

## Render

- `Root Directory`: dejar vacio
- `Dockerfile Path`: `./Dockerfile`

## Healthcheck

- `GET /`
- `GET /api/health`
- `GET /actuator/health`
