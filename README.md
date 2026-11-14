
# Diario Espacial – PC2 DAM 2026-2

Aplicación móvil Android desarrollada en Kotlin con Jetpack Compose que permite consultar información astronómica de la NASA mediante las APIs APOD y NeoWs.

La aplicación utiliza Retrofit para consumir servicios web y Room (SQLite) para almacenar los datos localmente, permitiendo consultar imágenes, asteroides y gestionar favoritos.

## Integrantes

| Integrante | Código | Responsabilidad |
|------------|--------|-----------------|
| Bruno Sebastian Gamarra | 24100022 | Web API, Retrofit, DTOs, mappers, Repository, ViewModel, interfaces y navegación |
| Piero Jair Chahuailacc | 24100634 | Persistencia local con Room, Entities, DAOs y AppDatabase |

La división del trabajo se realizó por tecnología, de acuerdo con las indicaciones del docente.

## Funcionalidades

### 1. Imágenes astronómicas (APOD)

- Consulta de imágenes astronómicas de los últimos 7 días.
- Visualización de la imagen del día o la más reciente disponible.
- Presentación de título, fecha y fotografía.
- Pantalla de detalle con explicación completa.
- Visualización de imágenes mediante Coil.
- Identificación de contenidos de tipo video.
- Agregar y quitar imágenes de favoritos.
- Consulta de imágenes favoritas guardadas localmente.

### 2. Asteroides cercanos a la Tierra (NeoWs)

- Consulta de asteroides de los últimos 7 días.
- Visualización del nombre y fecha de aproximación.
- Presentación del diámetro estimado en metros.
- Presentación de velocidad en km/h.
- Presentación de distancia de aproximación en kilómetros.
- Identificación de asteroides potencialmente peligrosos.
- Filtro para mostrar únicamente asteroides peligrosos.
- Pantalla de detalles de cada asteroide.
- Agregar y quitar asteroides de favoritos.
- Consulta de asteroides favoritos guardados localmente.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material Design 3
- Retrofit
- Gson
- Room (SQLite)
- Kotlin Coroutines
- StateFlow
- ViewModel
- Navigation Compose
- Coil
- Git y GitHub

## APIs utilizadas

### Astronomy Picture of the Day (APOD)

Endpoint actualizado:

https://science.nasa.gov/wp-json/wp/v2/apod-basic

Permite consultar las publicaciones astronómicas de NASA utilizando los parámetros `date_from` y `date_to`.

### Near Earth Object Web Service (NeoWs)

Endpoint:

https://api.nasa.gov/neo/rest/v1/feed

Permite consultar asteroides cercanos a la Tierra mediante los parámetros `start_date`, `end_date` y `api_key`.

## Arquitectura del proyecto

La aplicación sigue una estructura organizada por capas:

1. **Retrofit:** realiza las consultas a los servicios de NASA.
2. **DTOs:** representan las respuestas JSON recibidas.
3. **Mappers:** convierten los DTOs a entidades locales.
4. **Room:** almacena imágenes y asteroides en SQLite.
5. **Repository:** conecta Retrofit con la base de datos local.
6. **ViewModel:** administra los datos y los estados de carga mediante StateFlow.
7. **Jetpack Compose:** presenta la información en las pantallas.
8. **Navigation Compose:** administra las rutas entre listas, detalles y favoritos.

Las pantallas consultan los datos almacenados en Room, mientras que el Repository se encarga de actualizar la información obtenida de las APIs.

## Persistencia local

Se implementaron dos entidades:

- `ApodEntity`: almacena los datos de imágenes astronómicas.
- `AsteroideEntity`: almacena los datos de asteroides.

También se implementaron:

- `ApodDao`
- `AsteroideDao`
- `AppDatabase`

Los DAO permiten consultar registros, buscar favoritos, actualizar información y conservar el estado de los favoritos cuando se actualizan los datos.

## Cómo ejecutar el proyecto

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en Android Studio.
3. Generar una API Key personal en https://api.nasa.gov/.
4. Abrir el archivo `local.properties`, ubicado en la raíz del proyecto.
5. Agregar la siguiente propiedad:

    API_KEY=tu_clave_personal

6. Sincronizar el proyecto con Gradle.
7. Ejecutar la aplicación en un emulador o dispositivo Android con conexión a internet.

**Importante:** el archivo `local.properties` no debe subirse a GitHub, ya que contiene la clave personal utilizada para consultar NeoWs.

## Acuerdo de trabajo

### Bruno Sebastian Gamarra 

Responsable de la integración con servicios web y de la lógica de presentación:

- Configuración de Retrofit.
- Definición de servicios y endpoints NASA.
- Creación de DTOs para APOD y NeoWs.
- Implementación de mappers.
- Integración de datos mediante Repository.
- Implementación de ViewModel y StateFlow.
- Desarrollo de las pantallas con Jetpack Compose.
- Implementación de navegación, detalles y favoritos.
- Actualización de los endpoints de APOD.

### Piero Jair Chahuailacc

Responsable de la persistencia local:

- Creación de `ApodEntity`.
- Creación de `AsteroideEntity`.
- Implementación de `ApodDao`.
- Implementación de `AsteroideDao`.
- Configuración de `AppDatabase`.
- Integración de Room y KSP.
- Implementación de consultas y operaciones para conservar favoritos.

La integración de ambas partes se realizó mediante ramas de trabajo y Pull Requests en GitHub.

## Flujo de trabajo en GitHub

Se utilizó Git para el control de versiones y GitHub para integrar el trabajo de ambos participantes.

Pull Requests realizados:

1. **PR #1:** Agrega README con integrantes y acuerdo de trabajo.
2. **PR #2:** Implementar Web API NASA con Retrofit.
3. **PR #3:** Implementación de SQLite y Room.
4. **PR #4:** Implementación de pantallas y navegación.
5. **PR #5:** Corrección de endpoints de NASA.

Los cinco Pull Requests fueron fusionados en la rama `main`.

## Ramas utilizadas

- `main`
- `feature/web-api`
- `feature/sqlite-room`
- `feature/pantallas-navegacion`
- `docs/readme`

## Repositorio

https://github.com/brunoo-gq/24100022_GAMARRA_24100634_CHAHUAILACC_PC02
