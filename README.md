# Servidor MCP para Claude Desktop

Este servidor MCP (Message Control Protocol) permite a Claude Desktop interactuar con el sistema local, proporcionando capacidades para:
- Leer y escribir archivos
- Realizar peticiones de red
- Conectarse y consultar bases de datos MySQL

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- MySQL Server (opcional, solo si se planea usar la funcionalidad de base de datos)

## Estructura del Proyecto

```
src/main/java/com/claude/mcp/
├── MCPServer.java           # Servidor principal que orquesta todos los servicios
├── model/
│   └── Message.java        # Modelo de mensajes para la comunicación
└── service/
    ├── FileService.java    # Servicio para operaciones de archivos
    ├── DatabaseService.java # Servicio para operaciones de base de datos
    └── NetworkService.java  # Servicio para operaciones de red
```

## Formato de Mensajes

Los mensajes se intercambian en formato JSON con la siguiente estructura:

```json
{
    "type": "TIPO_MENSAJE",
    "content": "contenido opcional",
    "parameters": {
        // Parámetros específicos según el tipo de mensaje
    }
}
```

### Tipos de Mensajes

1. **FILE_READ**
   ```json
   {
       "type": "FILE_READ",
       "parameters": {
           "filePath": "/ruta/al/archivo.txt"
       }
   }
   ```

2. **FILE_WRITE**
   ```json
   {
       "type": "FILE_WRITE",
       "parameters": {
           "filePath": "/ruta/al/archivo.txt",
           "content": "contenido a escribir"
       }
   }
   ```

3. **NETWORK_REQUEST**
   ```json
   {
       "type": "NETWORK_REQUEST",
       "parameters": {
           "url": "https://api.ejemplo.com",
           "method": "GET",
           "body": "{}"  // Opcional, solo para POST
       }
   }
   ```

4. **DATABASE_QUERY**
   ```json
   {
       "type": "DATABASE_QUERY",
       "parameters": {
           "connectionId": "conn1",
           "query": "SELECT * FROM tabla WHERE id = ?",
           "queryParams": [1]
       }
   }
   ```

## Uso

1. Compilar el proyecto:
   ```bash
   mvn clean package
   ```

2. Ejecutar el servidor:
   ```bash
   java -jar target/servidor-mcp-1.0-SNAPSHOT.jar
   ```

3. Conectar desde Claude Desktop usando el protocolo MCP.

## Seguridad

- El servidor debe ejecutarse con los permisos mínimos necesarios
- Se recomienda implementar autenticación antes de usar en producción
- Las credenciales de base de datos deben manejarse de forma segura

## Logging

El servidor utiliza SLF4J con Logback para el registro de eventos. Los logs se pueden configurar en `src/main/resources/logback.xml`. 