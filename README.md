# Diario espacial – PC2 DAM 2026-2

Aplicación Android (Kotlin + Jetpack Compose) que consume datos de la API
de la NASA (APOD y NeoWs) y los guarda localmente con Room (SQLite).

## Integrantes

| Integrante | Rol |
|------------|-----|
| [Nombre 1 – GAMARRA] | Web API (Retrofit, DTO, mappers) |
| [Nombre 2 – CHAHUAILACC] | Persistencia (Room, Entities, DAOs, Database) |

> Nota: la división del trabajo por tecnología (API / Room) fue indicada
> por el docente.

## Cómo ejecutar el proyecto

1. Clonar el repositorio y abrirlo en Android Studio.
2. Generar una API key personal en https://api.nasa.gov (no usar DEMO_KEY).
3. Agregar en el archivo `local.properties` (en la raíz, no se sube a Git):

   ```
   API_KEY=tu_clave_aqui
   ```

4. Sincronizar Gradle y ejecutar en un emulador o dispositivo.

## Acuerdo de trabajo (base común)

- Retrofit, `AppDatabase` y la navegación principal son compartidos.
- **Entities y `AppDatabase`:** los prepara [Persona 2] en la rama
  `feature/sqlite-room`. [Persona 1] los usa para escribir los mappers.
- **Retrofit, DTOs y mappers:** los prepara [Persona 1] en `feature/web-api`.
- **Repository, ViewModels y navegación principal:** [definir quién].
- Cada integrante integra lo del otro haciendo `git pull origin main` después
  de que el PR correspondiente sea fusionado.

## Flujo de Git

- `main` está protegida: no se hacen commits directos.
- Cada integrante trabaja en su rama y entrega con Pull Request.
- Cada PR es revisado y aprobado por el otro integrante, con al menos un
  comentario.
- Commits pequeños con mensajes que expliquen el cambio.

## Ramas

- `feature/web-api`
- `feature/sqlite-room`
- `docs/readme`
