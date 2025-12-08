# 🧩 Customer Service – Microservicio Reactivo con Spring Boot WebFlux (JDK 21)

Este microservicio implementa la gestión de clientes y la creación de órdenes asociadas,
utilizando un enfoque **100% reactivo** basado en **Spring WebFlux**, **Project Reactor** y **Java 21**.

Forma parte de un ecosistema donde este servicio se comunica con el microservicio externo **Order Service** para crear órdenes vinculadas a un cliente.

Se han aplicado buenas prácticas de:

- ✔️ Diseño limpio y desacoplado
- ✔️ Pruebas unitarias con patrón **Arrange – Act – Assert (3A)**
- ✔️ Uso de `@DisplayName`
- ✔️ Pruebas de integración con **MockWebServer**
- ✔️ Documentación clara
- ✔️ Manejo adecuado de excepciones
- ✔️ Arquitectura orientada a microservicios

---

## 🚀 Estructura del proyecto

src/
├── main/java/com.customer.service
│ ├── controller/
│ ├── domain/
│ ├── dto/
│ ├── exception/
│ ├── repository/
│ └── service/
---

## 🚀 Tecnologías utilizadas

| Componente | Versión |
|-----------|---------|
| **Java** | 21 |
| **Spring Boot** | 3.2.x |
| **Spring WebFlux** | Reactive stack |
| **R2DBC + H2** | Base de datos reactiva |
| **Project Reactor** | Mono / Flux |
| **Mockito + JUnit 5** | Testing |
| **MockWebServer** | Pruebas entre microservicios |
| **Maven** | Build |

---

## 📘 Resumen de funcionalidades

El microservicio permite:

### 👤 Gestión de clientes
- Obtener cliente por ID
- Registrar nuevos clientes
- Buscar clientes por nombre (case-insensitive)

### 🧾 Creación de órdenes
- Llama al microservicio **Order Service**
- Envia un `OrderRequest`
- Recibe un `OrderResponse`
- Totalmente reactivo

### ⚠️ Manejo de errores
- `CustomerNotFoundException` → 404
- Datos inválidos → 400
- Errores internos → 500

---

## 📂 Estructura del proyecto


---

## 🧪 Pruebas implementadas

### ✔️ Pruebas unitarias (con **3A Pattern**)

Cada prueba sigue:

1. **Arrange** – Preparar mocks, entradas y contexto
2. **Act** – Ejecutar el método bajo prueba
3. **Assert** – Validar resultados esperados

Ejemplo de método probado:

`should_ReturnNotFound_When_CustomerDoesNotExist_Then_Status404`

### ✔️ Pruebas de integración (MockWebServer)

- Simula el OrderService real
- Respuestas HTTP reales
- JSON real enviado y recibido
- No requiere levantar otro microservicio

---

# ▶️ Cómo ejecutar este microservicio

### 1. Clonar repositorio
```bash
git clone https://github.com/jolucode/customer-service.git
cd customer-service

