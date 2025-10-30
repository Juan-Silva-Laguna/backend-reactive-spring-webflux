# API Mantenimiento - Spring Boot Reactiva

API REST reactiva para gestión de mantenimientos con arquitectura hexagonal, SOLID y DynamoDB.

## Stack Tecnológico

- Java 17+
- Spring Boot 3.x WebFlux
- Project Reactor (Mono/Flux)
- AWS DynamoDB
- AWS Secrets Manager
- Spring Security JWT
- Gradle 8.x

## Arquitectura

### Hexagonal (Puertos y Adaptadores)

```
Domain Layer (Modelos inmutables + Validadores)
    ↓
Application Layer (Use Cases + Services)
    ↓
Infrastructure Layer (Controllers + Repositories)
```

### Estructura de Directorios

```
src/main/java/com/company/maintenance/app/
├── domain/
│   ├── model/                    # Maquina, Mantenimiento, Repuesto (inmutables)
│   ├── validator/                # Validadores extensibles
│   └── exception/                # Excepciones de negocio
├── application/
│   ├── port/in/                  # Use Cases (interfaces)
│   ├── port/out/                 # Repositories (interfaces CQRS)
│   └── service/                  # Implementaciones
└── infrastructure/
    ├── adapter/in/rest/          # Controllers + DTOs
    └── adapter/out/persistence/  # Adapters + DynamoDB
```

## Principios SOLID

**Single Responsibility**: Cada clase una responsabilidad única.

**Open/Closed**: Validadores extensibles sin modificar código existente.

**Liskov Substitution**: Interfaces intercambiables.

**Interface Segregation**: CQRS - Repositorios Read/Write separados.

**Dependency Inversion**: Dependencias sobre abstracciones (puertos).

## Programación Reactiva

### Características

- Flujos no bloqueantes con Mono/Flux
- Composición de operaciones asíncronas
- Backpressure automático
- Schedulers para operaciones bloqueantes

### Ejemplo

```java
public Mono<Maquina> createMaquina(String nombre, String modelo, List<String> ids) {
    return Flux.fromIterable(ids)
        .flatMap(mantenimientoRepository::findById)
        .collectList()
        .map(mantenimientos -> new Maquina(nombre, modelo, mantenimientos))
        .flatMap(maquinaRepository::save);
}
```

## Configuración

### Variables de Entorno

```yaml
aws:
  region: us-east-1
  dynamodb:
    endpoint: ${DYNAMODB_ENDPOINT:}
  secrets-manager:
    secret-name: maintenance-api/credentials
```

### AWS Secrets Manager

```json
{
  "jwt.secret": "your-jwt-secret",
  "jwt.expiration": "86400000"
}
```

### Tablas DynamoDB

```bash
# Maquinas
aws dynamodb create-table \
  --table-name Maquinas \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST

# Mantenimientos
aws dynamodb create-table \
  --table-name Mantenimientos \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST

# Repuestos
aws dynamodb create-table \
  --table-name Repuestos \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST
```

## Instalación

```bash
git clone https://github.com/tu-usuario/maintenance-api.git
cd maintenance-api
./gradlew clean build
./gradlew bootRun
```

API disponible en: `http://localhost:8080`

## Endpoints

### Autenticación

```http
POST /api/auth/login
{
  "username": "admin",
  "password": "password123"
}
```

Todos los endpoints requieren: `Authorization: Bearer {token}`

### Máquinas

```http
POST   /api/maquinas
GET    /api/maquinas
GET    /api/maquinas/{id}
GET    /api/maquinas/nombre/{nombre}
PUT    /api/maquinas/{id}
DELETE /api/maquinas/{id}
POST   /api/maquinas/{id}/mantenimientos
DELETE /api/maquinas/{id}/mantenimientos/{mantenimientoId}
```

### Mantenimientos

```http
POST   /api/mantenimientos
POST   /api/mantenimientos/maquina/{maquinaId}
GET    /api/mantenimientos
GET    /api/mantenimientos/{id}
GET    /api/mantenimientos/fecha-range?startDate=&endDate=
GET    /api/mantenimientos/tipo/{tipo}
GET    /api/mantenimientos/precio-range?minPrecio=&maxPrecio=
PUT    /api/mantenimientos/{id}
DELETE /api/mantenimientos/{id}
POST   /api/mantenimientos/{id}/repuestos
DELETE /api/mantenimientos/{id}/repuestos/{repuestoId}
```

Tipos: `PREVENTIVO`, `CORRECTIVO`, `PREDICTIVO`

### Repuestos

```http
POST   /api/repuestos
GET    /api/repuestos
GET    /api/repuestos/{id}
GET    /api/repuestos/nombre/{nombre}
GET    /api/repuestos/precio-range?minPrecio=&maxPrecio=
PUT    /api/repuestos/{id}
PATCH  /api/repuestos/{id}/precio?precio=
DELETE /api/repuestos/{id}
```

## Modelos

### Maquina

```json
{
  "id": "maq-123",
  "nombre": "Excavadora CAT-320",
  "modelo": "320D2L",
  "mantenimientos": [...],
  "cantidadMantenimientos": 2
}
```

### Mantenimiento

```json
{
  "id": "mant-456",
  "fecha": "2024-10-29",
  "descripcion": "Cambio de aceite",
  "precio": 250.50,
  "repuestos": [...],
  "tipo": "PREVENTIVO",
  "maquinaId": "maq-123"
}
```

### Repuesto

```json
{
  "id": "rep-789",
  "nombre": "Filtro de aceite",
  "precio": 45.99
}
```

## Seguridad

### Roles

- `TECNICO`: CRUD básico
- `SUPERVISOR`: TECNICO + reportes
- `ADMIN`: Acceso total

### JWT

```java
@PreAuthorize("hasAnyRole('TECNICO', 'SUPERVISOR')")
public class MaquinaController { }
```

## Testing

### Unitarios

```java
@Test
void createMaquina_DeberiaCrear() {
    StepVerifier.create(service.createMaquina("Test", "Modelo", null))
        .expectNextMatches(m -> m.getNombre().equals("Test"))
        .verifyComplete();
}
```

### Integración

```java
@WebFluxTest(MaquinaController.class)
class MaquinaControllerTest {
    @Test
    @WithMockUser(roles = "TECNICO")
    void createMaquina_Retorna201() {
        webTestClient.post()
            .uri("/api/maquinas")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated();
    }
}

