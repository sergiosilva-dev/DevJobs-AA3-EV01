# DevJobs — Spring Boot + Thymeleaf

Proyecto académico (**SENA – AA3-EV01**) para la publicación y gestión de ofertas de empleo tech. Incluye **API REST** y **Web MVC** con buenas prácticas de SEO, accesibilidad y CI.

> Repositorio: `sergiosilva-dev/DevJobs-AA3-EV01`

---

## 📦 Stack

- **Java 17+**, **Spring Boot 3** (Web, Validation, Thymeleaf, Data JPA)
- **H2** (dev) o cualquier RDBMS compatible con JPA
- **Maven**
- Calidad: **Spotless** + **Checkstyle**
- Front Server-Side: **Thymeleaf** + Tailwind (utilidades CSS)
- Imágenes estáticas en `src/main/resources/static`

---

## 🚀 Ejecutar el proyecto

### Requisitos

- Java 17+ y Maven 3.9+
- Puerto libre **8080**

### Dev (hot reload desde Maven/IDE)

```bash
mvn spring-boot:run
```

### Producción (JAR ejecutable)

```bash
mvn -DskipTests package
java -jar target/*.jar
```

**URLs locales**

- Web: `http://localhost:8080/jobs`
- API: `http://localhost:8080/api/jobs`

> El `DataSeeder` crea datos de ejemplo para probar la app la primera vez.

---

## 🧩 Dominio principal

Entidad `Job` (resumen de campos):

- `title`, `company`, `description`, `location`
- `workMode` (`REMOTE | HYBRID | ONSITE`)
- `type` (`FULL_TIME | PART_TIME | CONTRACT | INTERNSHIP`)
- `salaryMin`, `salaryMax`
- `postedAt` (fecha publicación)

---

## 🌐 Rutas Web (Thymeleaf)

- `GET /jobs` — listado con filtros y paginación
- `GET /jobs/{id}` — detalle
- `GET /jobs/new` — formulario creación
- `POST /jobs` — crear
- `GET /jobs/{id}/edit` — formulario edición
- `POST /jobs/{id}` o `PUT /jobs/{id}` — actualizar
- `POST /jobs/{id}/delete` — eliminar

> Las vistas aplican **SEO base** (title/description/canonical/OG/Twitter), **accesibilidad mínima** (H1, alt, labels) y **Schema.org JobPosting (JSON‑LD)** en el detalle.

---

## 🛠️ API REST `/api/jobs`

### Esquema (DTO de request)

```json
{
  "title": "Backend Engineer",
  "company": "Acme Corp",
  "description": "Microservicios con Spring Boot",
  "location": "Bogotá",
  "workMode": "REMOTE",
  "type": "FULL_TIME",
  "salaryMin": 6000000,
  "salaryMax": 9000000,
  "postedAt": "2025-09-01"
}
```

### Endpoints

- `GET /api/jobs` — lista paginada/filtrada  
  **Query params comunes**:  
  `page` (0..n), `size` (default 10), `sort` (p.ej. `title,asc`), `q` (búsqueda por título/empresa), `type`, `workMode`.

- `GET /api/jobs/{id}` — detalle
- `POST /api/jobs` — crear (valida campos requeridos)
- `PUT /api/jobs/{id}` — actualizar
- `DELETE /api/jobs/{id}` — eliminar

### cURL (bash)

```bash
# Listar (página 0 tamaño 5 ordenado por fecha desc)
curl -s "http://localhost:8080/api/jobs?page=0&size=5&sort=postedAt,desc"

# Filtrar por tipo y palabra clave
curl -s "http://localhost:8080/api/jobs?q=java&type=FULL_TIME&workMode=REMOTE"

# Crear
curl -s -X POST "http://localhost:8080/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Backend Engineer",
    "company":"Acme Corp",
    "description":"Microservicios con Spring Boot",
    "location":"Bogotá",
    "workMode":"REMOTE",
    "type":"FULL_TIME",
    "salaryMin":6000000,
    "salaryMax":9000000,
    "postedAt":"2025-09-01"
  }'

# Actualizar
curl -s -X PUT "http://localhost:8080/api/jobs/1" \
  -H "Content-Type: application/json" \
  -d '{ "title":"Backend Engineer Sr.", "salaryMax":10000000 }'

# Eliminar
curl -s -X DELETE "http://localhost:8080/api/jobs/1"
```

> En **Windows PowerShell**, reemplaza `\'` por `"` y usa `iwr`/`Invoke-RestMethod` si lo prefieres.

### Errores y validación

Las respuestas de error incluyen código HTTP + payload de detalle (handler global):

- `400` validación (campos obligatorios/formato)
- `404` no encontrado
- `409` conflictos de negocio (si aplica)
- `500` error inesperado

---

## 🔎 SEO y Accesibilidad

### SEO

- **Meta** `title`/`description`/`canonical` + OG/Twitter por vista.
- **JobPosting (JSON‑LD)** en `GET /jobs/{id}` con `script type="application/ld+json"` (Thymeleaf inline).
- **robots.txt** en `static/robots.txt`.
- **sitemap.xml** dinámico en `GET /sitemap.xml` (incluye `/`, `/jobs` y cada detalle `jobs/{id}`).

### Accesibilidad

- Landmarks (`header`, `nav`, `main`, `footer`) y **skip link** al inicio.
- **H1 único** por página.
- Formularios con **label + th:field** y errores con `aria-live`.
- Foco visible (`:focus-visible`).

### Recursos estáticos

- `favicon.ico`, `android-chrome-192x192.png`, `android-chrome-512x512.png`, `apple-touch-icon.png`
- `site.webmanifest` con `theme_color` y `icons`.

---

## 🧪 Calidad y CI

Local:

```bash
# Formateo (Spotless) y análisis (Checkstyle) integrados al ciclo Maven
mvn -DskipTests=false verify
# O aplicar formateo directamente
mvn spotless:apply
```

> El repo incluye workflow de GitHub Actions (build + checks). Mantener **CI en verde**.

---

## 🗺️ Sitemap y robots

- `GET /robots.txt`  
  Contiene referencia al sitemap:  
  `Sitemap: http://localhost:8080/sitemap.xml` (cambiar host en despliegue).

- `GET /sitemap.xml`  
  Generado por `SitemapController`, itera sobre `JobService.findAll()` y emite `<url>` con `lastmod` (si `postedAt` existe).

---

## 📄 Documentación de API (Swagger/Postman)

Se puede documentar de dos formas (elige una, o ambas):

### Opción A — Swagger / OpenAPI (springdoc)

1. Agregar dependencia en `pom.xml`:
   ```xml
   <dependency>
     <groupId>org.springdoc</groupId>
     <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
     <version>2.6.0</version>
   </dependency>
   ```
2. Levantar la app y abrir:
   - UI: `http://localhost:8080/swagger-ui.html`
   - JSON: `http://localhost:8080/v3/api-docs`

### Opción B — Postman Collection

- Exportar colección en `docs/postman/devjobs.postman_collection.json` con ejemplos CRUD.

> En este proyecto seguiremos con **Swagger** en el siguiente paso.

---

## 🧰 Desarrollo y ramas

- Trabajamos **una cosa a la vez**, en **rama nueva** y **PR a `master`**.
- Confirmamos “OK” antes de pasar al siguiente paso.

Sugerencias de nombres de ramas:

- `feat/schema-jobposting`
- `feat/seo-robots-sitemap`
- `feat/assets-favicon-manifest`
- `feat/a11y-min`
- `docs/swagger`

---

## 📸 Evidencias sugeridas (para entregar)

- Capturas: `/jobs` (lista), `/jobs/{id}` (detalle), formularios.
- Validación JSON-LD (Rich Results Test).
- `robots.txt` y `sitemap.xml` en el navegador.
- Favicon y manifest cargados (pestaña y Network).
- Swagger UI funcionando (cuando se agregue).
- PRs en GitHub y CI verde.

---

## 📜 Licencia

Uso académico — SENA (AA3‑EV01).
