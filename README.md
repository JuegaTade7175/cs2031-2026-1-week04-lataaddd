# CS2031 - Week 04 Lab: Testing con Spring Boot

## ¿Para qué sirve este repositorio?

Este repositorio es una API REST construida con **Spring Boot** que gestiona un sistema universitario con **Cursos**, **Estudiantes** y **Docentes**, usando **PostgreSQL** como base de datos y **Spring Data JPA** para el acceso a datos.

El objetivo principal de esta sesión de laboratorio es que los estudiantes comprendan cómo escribir pruebas automatizadas en una aplicación Spring Boot de producción real, usando tres herramientas complementarias:

1. **Mockito**: cómo aislar unidades de código reemplazando dependencias reales por objetos simulados (_mocks_), permitiendo probar la lógica de negocio sin levantar el contexto de Spring ni tocar la base de datos.
2. **TestContainers**: cómo levantar una base de datos PostgreSQL real dentro de un contenedor Docker durante las pruebas, garantizando que los tests de repositorio e integración se ejecuten contra la misma tecnología que producción.
3. **GitHub Actions**: cómo automatizar la ejecución de todas las pruebas en cada `push` o `pull request` a `main`, integrando el ciclo de CI/CD desde el primer día.

---

## Cómo ejecutar el proyecto

### Prerrequisitos

- Java 21
- Maven 3.6+
- Docker (requerido para TestContainers y para el entorno de desarrollo)

### Levantar la base de datos de desarrollo

```bash
docker-compose up -d
```

Esto levanta un contenedor PostgreSQL en `localhost:5432` con la base de datos `week04-db`.

### Configurar las variables de entorno

Copia el archivo de ejemplo y ajusta los valores si es necesario:

```bash
cp .env.example .env
```

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=week04-db
DB_USER=postgres
DB_PASSWORD=postgres
```

### Iniciar la aplicación

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

### Ejecutar las pruebas

```bash
./mvnw clean verify
```

> TestContainers levantará automáticamente un contenedor PostgreSQL para los tests de repositorio. Docker debe estar corriendo.

---

## Arquitectura y estructura del proyecto

El proyecto sigue una **arquitectura en capas** con separación de responsabilidades por paquete, agrupando cada entidad en sus propias subcarpetas `application`, `domain` e `infrastructure`.

```
src/
├── main/
│   ├── java/org/week04lab01/
│   │   ├── Week04Lab01Application.java          # Punto de entrada + bean ModelMapper
│   │   ├── GlobalExceptionHandler.java          # Manejo global de errores (@RestControllerAdvice)
│   │   ├── exceptions/
│   │   │   └── ResourceNotFoundException.java  # Excepción de dominio (404)
│   │   ├── course/
│   │   │   ├── application/
│   │   │   │   └── CourseController.java        # Capa HTTP: endpoints REST
│   │   │   ├── domain/
│   │   │   │   ├── Course.java                  # Entidad JPA
│   │   │   │   └── CourseService.java           # Lógica de negocio
│   │   │   ├── dto/
│   │   │   │   ├── CourseRequestDto.java        # DTO de entrada
│   │   │   │   ├── CourseResponseDto.java       # DTO de salida
│   │   │   │   └── AddStudentToCourseDto.java   # DTO de respuesta para matrícula
│   │   │   └── infrastructure/
│   │   │       └── CourseRepository.java        # Spring Data JPA repository
│   │   ├── student/
│   │   │   ├── application/StudentController.java
│   │   │   ├── domain/
│   │   │   │   ├── Student.java
│   │   │   │   └── StudentService.java
│   │   │   └── infrastructure/StudentRepository.java
│   │   └── teacher/
│   │       ├── application/TeacherController.java
│   │       ├── domain/
│   │       │   ├── Teacher.java
│   │       │   └── TeacherService.java
│   │       └── infrastructure/TeacherRepository.java
│   └── resources/
│       └── application.properties               # Configuración de producción (PostgreSQL)
└── test/
    ├── java/org/week04lab01/
    │   ├── AbstractContainerBaseTest.java        # Clase base con el contenedor TestContainers
    │   └── course/
    │       ├── CourseControllerTests.java        # Tests de integración (MockMvc + H2)
    │       └── CourseRepositoryTests.java        # Tests de repositorio (TestContainers + PostgreSQL)
    └── resources/
        └── application-test.properties          # Configuración de test (H2 en memoria)
```

---

## Modelos de dominio

### `Course` — Curso

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | Clave primaria autogenerada |
| `name` | `String` | Nombre del curso |
| `creditNumber` | `Integer` | Número de créditos |
| `description` | `String` | Descripción del curso |
| `weeklyHours` | `Integer` | Horas semanales de contacto |
| `students` | `List<Student>` | Relación Many-to-Many con estudiantes |

### `Student` — Estudiante

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | Clave primaria autogenerada |
| `firstName` | `String` | Nombre |
| `lastName` | `String` | Apellido |
| `email` | `String` | Correo único |
| `birthday` | `LocalDate` | Fecha de nacimiento |
| `courses` | `List<Course>` | Relación Many-to-Many (dueña via tabla `student_course`) |

### `Teacher` — Docente

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | Clave primaria autogenerada |
| `firstName` | `String` | Nombre |
| `lastName` | `String` | Apellido |
| `email` | `String` | Correo único |
| `birthday` | `LocalDate` | Fecha de nacimiento |
| `hourlyWage` | `Double` | Pago por hora |
| `courses` | `List<Course>` | Relación One-to-Many con cursos |

---

## Endpoints disponibles

### Cursos (`/courses`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/courses` | Crea un nuevo curso |
| `GET` | `/courses` | Retorna todos los cursos |
| `GET` | `/courses/{id}` | Retorna un curso por ID |
| `PATCH` | `/courses/{id}` | Actualización parcial de un curso |
| `DELETE` | `/courses/{id}` | Elimina un curso |
| `GET` | `/courses/teacher/{teacherId}` | Cursos asignados a un docente |
| `POST` | `/courses/{courseId}/students/{studentId}` | Matricula un estudiante en un curso |

### Estudiantes (`/students`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/students` | Crea un nuevo estudiante |
| `GET` | `/students` | Retorna todos los estudiantes |
| `GET` | `/students/{id}` | Retorna un estudiante por ID |
| `PUT` | `/students/{id}` | Actualización completa de un estudiante |
| `DELETE` | `/students/{id}` | Elimina un estudiante |
| `GET` | `/students/course/{courseId}` | Estudiantes matriculados en un curso |

### Docentes (`/teachers`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/teachers` | Crea un nuevo docente |
| `GET` | `/teachers` | Retorna todos los docentes |
| `GET` | `/teachers/{id}` | Retorna un docente por ID |
| `PUT` | `/teachers/{id}` | Actualización completa de un docente |
| `DELETE` | `/teachers/{id}` | Elimina un docente |
| `GET` | `/teachers/email/{email}` | Busca docente por correo |
| `GET` | `/teachers/firstname/{firstName}` | Busca docente por nombre |
| `GET` | `/teachers/lastname/{lastName}` | Busca docente por apellido |
| `GET` | `/teachers/birthday/{birthday}` | Busca docente por fecha de nacimiento |

---

## Estrategia de pruebas

Este repositorio implementa dos niveles de pruebas, cada uno con herramientas y objetivos distintos:

### Nivel 1: Pruebas de repositorio con TestContainers (`@DataJpaTest`)

**Clase:** `CourseRepositoryTests.java`

Estas pruebas validan que las consultas JPA funcionen correctamente contra una base de datos **PostgreSQL real** levantada en un contenedor Docker, no contra H2. Esto es crítico porque H2 y PostgreSQL tienen diferencias en comportamiento SQL que pueden ocultar errores en producción.

```java
@DataJpaTest
class CourseRepositoryTests extends AbstractContainerBaseTest {
    @Autowired
    CourseRepository courseRepository;

    @Test
    void testCreateAndSaveCourse() { ... }

    @Test
    void testUpdateCourse() { ... }
}
```

**¿Qué hace `AbstractContainerBaseTest`?**

```java
@Testcontainers
public class AbstractContainerBaseTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("week04-test-db")
            .withUsername("postgres")
            .withPassword("postgres")
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}
```

- Define un contenedor PostgreSQL que se inicia una sola vez y se reutiliza (`withReuse(true)`) para toda la suite.
- `@DynamicPropertySource` inyecta la URL del contenedor en el contexto de Spring, reemplazando los valores de `application.properties`.

### Nivel 2: Pruebas de integración con MockMvc (`@SpringBootTest`)

**Clase:** `CourseControllerTests.java`

Estas pruebas levantan el contexto completo de Spring Boot y simulan peticiones HTTP reales usando `MockMvc`. Usan el perfil `test` que apunta a **H2 en memoria** para una ejecución más rápida.

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseControllerTests {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CourseRepository courseRepository;

    @Test
    void shouldReturnOkWhenCreatingCourse() { ... }

    @Test
    void shouldReturnOkWhenUpdatingCourse() { ... }
}
```

**Patrón Arrange-Act-Assert en cada test:**
- **Arrange**: prepara los datos de entrada (DTO, entidad existente en BD).
- **Act**: ejecuta la petición HTTP con `mockMvc.perform(...)`.
- **Assert**: verifica el status HTTP, la respuesta JSON, y el estado real de la base de datos vía repositorio.

### Cuadro resumen

| Tipo de prueba | Clase | Contexto Spring | Base de datos | Herramientas |
|----------------|-------|-----------------|---------------|--------------|
| Repositorio | `CourseRepositoryTests` | Parcial (`@DataJpaTest`) | PostgreSQL (contenedor) | TestContainers |
| Integración HTTP | `CourseControllerTests` | Completo (`@SpringBootTest`) | H2 en memoria | MockMvc, `@ActiveProfiles` |

---

## CI/CD con GitHub Actions

El archivo `.github/workflows/tests.yml` define un pipeline que **ejecuta todas las pruebas automáticamente** en cada `push` o `pull request` hacia `main`.

```yaml
on:
  push:
    branches: [ "main" ]
  pull_requests:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven
      - run: mvn -B clean verify --file pom.xml
```

**¿Por qué funciona TestContainers en GitHub Actions?**

El runner `ubuntu-latest` tiene Docker preinstalado. TestContainers detecta el Docker daemon automáticamente y levanta el contenedor PostgreSQL durante la ejecución de `mvn verify`, sin ninguna configuración adicional de servicios.

---

## Configuración de base de datos

### Producción (`application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=create-drop
```

Usa variables de entorno definidas en `.env` (ver `.env.example`).

### Tests de integración (`application-test.properties`)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
```

H2 en memoria para los tests de `@SpringBootTest` — rápidos y sin dependencia de Docker.

### Tests de repositorio

La URL de la base de datos es inyectada dinámicamente por `AbstractContainerBaseTest` mediante `@DynamicPropertySource`, apuntando al contenedor PostgreSQL levantado por TestContainers.

---

## Stack tecnológico

| Categoría | Tecnología |
|-----------|-----------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.5 |
| ORM | Spring Data JPA / Hibernate |
| Base de datos (prod) | PostgreSQL 16 |
| Base de datos (test) | H2 (in-memory) |
| Mapeo de objetos | ModelMapper 3.2.4 |
| Reducción de boilerplate | Lombok 1.18.38 |
| Testing unitario | JUnit 5 + Mockito |
| Testing de integración | Spring Boot Test + MockMvc |
| Testing de repositorio | TestContainers (PostgreSQL) |
| CI/CD | GitHub Actions |
| Contenedores dev | Docker Compose |
