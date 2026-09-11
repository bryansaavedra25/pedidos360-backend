# 🛒 Pedidos360 - Backend REST API

API RESTful robusta y segura construida en **Spring Boot 3.3.4**, empaquetada y desplegada en una instancia **AWS EC2** y protegida con autenticación basada en **AWS Cognito (OAuth2 / JWT)**. 

Este proyecto forma parte de la Evaluación Parcial N° 1 para la asignatura **Desarrollo Cloud Native I (DSY1107)** de Duoc UC.

---

## 🏛️ Arquitectura del Sistema

[ Frontend React (Amplify) ]
│
▼ (HTTP + Bearer Token JWT)
[ AWS EC2 Instance (54.198.41.234:8080) ]
│
▼ (Valida Firma / Issuer)
[ AWS Cognito User Pool ]


* **Infraestructura:** Instancia AWS EC2 corriendo Amazon Linux 2023 con Java OpenJDK 17.
* **Seguridad (Resource Server):** Intercepta cada solicitud a `/api/pedidos/**` para verificar la firma digital del JWT enviado en el header `Authorization: Bearer <token>`.
* **Servicio de Sistema:** Administrado a través de `systemd` bajo el servicio `pedidos360.service` para asegurar ejecución continua y reinicio automático.

---

## 🛠️ Tecnologías y Dependencias

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.3.4
* **Módulos de Spring:**
  * `Spring Boot Starter Web` (API RESTful)
  * `Spring Boot Starter Security` (Filtros de seguridad)
  * `Spring Boot Starter OAuth2 Resource Server` (Validación automática de JWT con Cognito)
  * `Spring Boot Starter Validation` (Validación de DTOs y payloads)
* **Gestor de Construcción:** Maven

---

## 🔒 Seguridad y Autenticación

El backend actúa como un **OAuth2 Resource Server**. Al recibir una solicitud:

1. Extrae el Bearer Token del header HTTP.
2. Consulta el `JWKS` (JSON Web Key Set) de AWS Cognito en la región `us-east-1`.
3. Valida la firma del token, la vigencia (`exp`) y el emisor (`iss`).
4. Si el token es inválido o no está presente, retorna un código **`401 Unauthorized`**.

---

## 📡 Endpoints de la API REST (`/api/pedidos`)

Todas las rutas requieren la cabecera `Authorization: Bearer <TOKEN_JWT>`.

| Método | Endpoint | Descripción | Body (JSON) | Estado HTTP |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/pedidos` | Obtiene el listado completo de pedidos | N/A | `200 OK` |
| `GET` | `/api/pedidos/{id}` | Busca un pedido específico por ID | N/A | `200 OK` / `404 Not Found` |
| `POST` | `/api/pedidos` | Registra un nuevo pedido | `{"cliente": "String", "total": Number, "estado": "String"}` | `201 Created` |
| `PUT` | `/api/pedidos/{id}` | Actualiza un pedido existente | `{"cliente": "String", "total": Number, "estado": "String"}` | `200 OK` |
| `DELETE` | `/api/pedidos/{id}` | Elimina un pedido por ID | N/A | `204 No Content` |

---

## ⚙️ Configuración del Proyecto (`application.properties`)

```properties
server.port=8080

# Integración OAuth2 Resource Server con AWS Cognito
spring.security.oauth2.resourceserver.jwt.issuer-uri=[https://cognito-idp.us-east-1.amazonaws.com/us-east-1_dzuH5paum](https://cognito-idp.us-east-1.amazonaws.com/us-east-1_dzuH5paum)
💻 Comandos de Compilación y Despliegue
1. Compilar localmente y empaquetar en ejecutable JAR
PowerShell
./mvnw clean package -DskipTests
2. Subir el ejecutable a la instancia EC2 via SCP
PowerShell
scp -i "$env:USERPROFILE\Downloads\clavepem1.pem" target/backend-0.0.1-SNAPSHOT.jar ec2-user@54.198.41.234:~
3. Administración del servicio en EC2 (SSH)
Bash
# Reiniciar el servicio backend
sudo systemctl restart pedidos360

# Ver el estado del servicio
sudo systemctl status pedidos360



Autor
Bryan Saavedra, Elena espinoza
Asignatura: Desarrollo Cloud Native I 
Duoc UC - 2026



# Inspeccionar logs en tiempo real
sudo journalctl -u pedidos360 -f
