# Microservicio de Solicitudes - CrediYa

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![PiTest](https://img.shields.io/badge/PiTest-Enabled-red.svg)](https://pitest.org/)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-green.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## 📋 Descripción

Microservicio responsable de la gestión completa del ciclo de vida de solicitudes de préstamo en el sistema CrediYa. Implementa Clean Architecture con Spring WebFlux para programación reactiva, evaluación automática de solicitudes y comunicación segura con el microservicio de autenticación.

## 🏗️ Arquitectura

### Clean Architecture
```
├── domain/
│   ├── model/          # Entidades de negocio y reglas
│   └── usecase/        # Casos de uso y lógica de negocio
├── infrastructure/
│   ├── driven-adapters/    # Adaptadores de salida (BD, APIs)
│   ├── entry-points/       # Adaptadores de entrada (REST)
│   └── helpers/            # Utilidades y configuraciones
└── applications/
    └── app-service/        # Aplicación principal
```

### Responsabilidades
- **Registro de Solicitudes**: CLIENTE puede crear solicitudes
- **Listado de Solicitudes**: ASESOR puede listar y filtrar solicitudes
- **Evaluación Automática**: Validación automática según reglas de negocio
- **Integración con Auth**: Validación de tokens y usuarios
- **Gestión de Estados**: Control del flujo de aprobación

## 🚀 Stack Tecnológico

- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.4** - Framework principal
- **Spring WebFlux** - Programación reactiva
- **Spring R2DBC** - Acceso reactivo a base de datos
- **PostgreSQL 15** - Base de datos
- **JWT** - Autenticación y autorización
- **Lombok** - Reducción de boilerplate
- **Gradle 8.14.3** - Gestión de dependencias
- **PiTest** - Testing de mutaciones
- **JaCoCo** - Cobertura de código

## 📊 Base de Datos

### Esquema: crediya_solicitudes

#### Entidades Principales
- **solicitud**: Solicitudes de préstamo con información completa
- **tipo_prestamo**: Catálogo de productos financieros
- **estados**: Estados del flujo de aprobación

#### Estados del Sistema
1. **PENDIENTE** - Solicitud creada, pendiente de evaluación
2. **EN_EVALUACION** - En proceso de evaluación manual
3. **APROBADA** - Solicitud aprobada
4. **RECHAZADA** - Solicitud rechazada
5. **CANCELADA** - Solicitud cancelada por el usuario

#### Tipos de Préstamo
1. **PERSONAL** - Préstamo personal (validación automática)
2. **HIPOTECARIO** - Préstamo hipotecario (evaluación manual)
3. **VEHICULAR** - Préstamo vehicular (evaluación manual)
4. **EDUCATIVO** - Préstamo educativo (validación automática)

## 🔐 Seguridad e Integración

### Comunicación con Microservicio de Autenticación
- **Validación de Tokens**: Servicio a servicio para validar JWT
- **Obtención de Usuario**: Información del usuario autenticado
- **Autenticación**: Header Authorization: Bearer {token}

### Flujo de Autenticación
1. Cliente envía request con JWT token
2. Microservicio valida token con servicio de autenticación
3. Obtiene información del usuario (ID, rol, permisos)
4. Procesa la solicitud según autorización

### Autorización por Rol
- **ASESOR**: Listar y gestionar todas las solicitudes
- **CLIENTE**: Solo crear sus propias solicitudes

## 📡 API Endpoints

### Principales Servicios

#### Gestión de Solicitudes
- `GET /api/v1/solicitud` - Listar solicitudes
- `POST /api/v1/solicitud` - Crear nueva solicitud

### Documentación Completa
- **Swagger UI**: `/webjars/swagger-ui/index.html`


## 🛠️ Configuración

### Variables de Entorno

#### Base de Datos
```env
DB_HOST=<database-host>
DB_PORT=<database-port>
DB_NAME=<database-name>
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
```

#### Integración con Autenticación
```env
AUTH_SERVICE_URL=<auth-service-url>
AUTH_VALIDATE_ENDPOINT=<validate-endpoint>
AUTH_SERVICE_SECRET=<service-secret-key-256-bits-minimum>
```

#### JWT y Aplicación
```env
SERVER_PORT=<application-port>
JWT_SECRET=<jwt-secret-key-256-bits-minimum>
JWT_EXPIRATION_HOURS=<token-expiration-hours>
LOG_LEVEL=<log-level>
DB_POOL_INITIAL=<initial-pool-size>
DB_POOL_MAX=<max-pool-size>
DB_POOL_IDLE=<idle-timeout>
```

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Microservicio de Autenticación ejecutándose

### 1. Configurar Base de Datos
```bash
# Desde el directorio raíz del proyecto
cd ../database
docker-compose up -d crediya-solicitudes-db
```

### 2. Compilar y Ejecutar

#### Desarrollo Local
```bash
# Compilar
./gradlew build

# Ejecutar aplicación
./gradlew bootRun
```

#### Producción con Docker
```bash
# Construir imagen
docker build -f deployment/Dockerfile -t crediya-solicitudes:latest .

# Ejecutar contenedor
docker run -d \
  --name crediya-solicitudes \
  --network crediya-network \
  -p 8082:8082 \
  crediya-solicitudes:latest
```

## 📊 Monitoreo

### Actuator Endpoints
- **Health Check**: `/actuator/health` - Estado del servicio y dependencias
- **Métricas**: `/actuator/metrics` - Métricas de la aplicación
- **Info**: `/actuator/info` - Información de la aplicación
- **Prometheus**: `/actuator/prometheus` - Métricas para Prometheus

### Uso de Actuator
```bash
# Verificar estado del servicio
curl http://localhost:8082/actuator/health

# Ver métricas específicas
curl http://localhost:8082/actuator/metrics/jvm.memory.used
```

### Métricas Clave
- Solicitudes creadas/aprobadas/rechazadas
- Tiempo de evaluación automática
- Tiempo de validación de tokens
- Conexiones de base de datos
- Comunicación con servicio de autenticación