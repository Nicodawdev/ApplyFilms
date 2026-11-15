# ApplyFilms API

API RESTful de gestión de películas con Spring Boot 4, H2, caché RAM, mashup con
[The Movie Database (TMDB)](https://www.themoviedb.org/) y recomendaciones con IA (LangChain4j + OpenAI).

## Requisitos
- JDK 25
- Clave gratuita de TMDB (Ajustes de la cuenta -> API -> "Clave de la API").
- (Opcional) Clave de OpenAI para `/api/hello` y `/api/peliculas/recomendar`.

## Variables de entorno
| Variable         | Uso                                              |
|------------------|--------------------------------------------------|
| `TMDB_API_KEY`   | Mashup con TMDB. Sin ella el mashup usa valores por defecto |
| `OPENAI_API_KEY` | Funcionalidades de IA                            |

En IntelliJ: *Run > Edit Configurations > Environment variables*.

## Arranque
```bash
./mvnw spring-boot:run                                              # perfil dev (por defecto)
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod              # perfil prod (BD en ./data/)
./mvnw test                                                         # batería de tests
```

## URLs
- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2 (dev): http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:applyfilms_dev`, usuario `sa`)
- API: http://localhost:8080/api/peliculas

## Endpoints
| Método | Ruta                               | Respuesta          |
|--------|------------------------------------|--------------------|
| GET    | /api/peliculas                     | 200                |
| GET    | /api/peliculas/{id}                | 200 / 404          |
| GET    | /api/peliculas/buscar?titulo=      | 200                |
| GET    | /api/peliculas/valoradas?minimo=   | 200                |
| POST   | /api/peliculas                     | 201 / 400 / 409    |
| PUT    | /api/peliculas/{id}                | 200 / 400 / 404 / 409 |
| PATCH  | /api/peliculas/{id}                | 200 / 400 / 404 / 409 |
| DELETE | /api/peliculas/{id}                | 204 / 404 / 422    |
| GET    | /api/peliculas/{id}/mashup         | 200 / 404 (cacheado) |
| POST   | /api/peliculas/recomendar          | 200 / 400          |
| GET    | /api/hello                         | 200                |

Regla de negocio: solo se pueden borrar películas con `disponible = false` (si no, 422).

Todas las peticiones de éxito y error están en `pruebas.http`.
