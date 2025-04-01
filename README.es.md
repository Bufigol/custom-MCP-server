# 🚀 Servidor MCP para Agentes Autónomos de Desarrollo

Este servidor MCP (Message Control Protocol) es una plataforma que permite a diferentes IAs (como Claude) actuar como agentes autónomos de desarrollo, proporcionando capacidades para automatizar todo el proceso de desarrollo de software, desde la concepción de la idea hasta el testeo final.

## 🎯 Objetivo Principal

El objetivo principal de este servidor es permitir que las IAs puedan desarrollar software de manera autónoma, realizando tareas como:
- Creación de estructura de proyectos
- Generación de archivos de requisitos
- Desarrollo de código
- Implementación de pruebas
- Gestión de versiones
- Y cualquier otra tarea relacionada con el desarrollo de software

## ✨ Características Principales

El servidor proporciona capacidades para:
- Leer y escribir archivos
- Realizar peticiones de red
- Conectarse y consultar bases de datos MySQL
- Ejecutar comandos del sistema
- Gestionar repositorios Git
- Y más...

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 📋 Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- MySQL Server (opcional, solo si se planea usar la funcionalidad de base de datos)
- Git (para funcionalidades de control de versiones)

## 📁 Estructura del Proyecto

```tree
src/main/java/com/claude/mcp/
├── MCPServer.java           # Servidor principal que orquesta todos los servicios
├── model/
│   └── Message.java        # Modelo de mensajes para la comunicación
└── service/
    ├── FileService.java    # Servicio para operaciones de archivos
    ├── DatabaseService.java # Servicio para operaciones de base de datos
    ├── NetworkService.java  # Servicio para operaciones de red
    ├── GitService.java      # Servicio para operaciones de Git
    └── CommandService.java  # Servicio para ejecutar comandos del sistema
```

## 📝 Formato de Mensajes

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

### 📨 Tipos de Mensajes

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

5. **GIT_COMMAND**
   ```json
   {
       "type": "GIT_COMMAND",
       "parameters": {
           "command": "commit",
           "args": ["-m", "mensaje del commit"]
       }
   }
   ```

6. **SYSTEM_COMMAND**
   ```json
   {
       "type": "SYSTEM_COMMAND",
       "parameters": {
           "command": "npm",
           "args": ["install"]
       }
   }
   ```

## 🚀 Uso

1. Compilar el proyecto:
   ```bash
   mvn clean package
   ```

2. Ejecutar el servidor:
   ```bash
   java -jar target/servidor-mcp-1.0-SNAPSHOT.jar
   ```

3. Conectar desde cualquier IA compatible con el protocolo MCP.

## 🔒 Seguridad

- El servidor debe ejecutarse con los permisos mínimos necesarios
- Se recomienda implementar autenticación antes de usar en producción
- Las credenciales de base de datos deben manejarse de forma segura
- Se deben implementar límites y validaciones para comandos del sistema
- Se recomienda usar un entorno aislado para pruebas

## 📊 Logging

El servidor utiliza SLF4J con Logback para el registro de eventos. Los logs se pueden configurar en `src/main/resources/logback.xml`.

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor, asegúrate de:
1. Seguir las guías de estilo del proyecto
2. Incluir pruebas para nuevas funcionalidades
3. Actualizar la documentación según sea necesario
4. Crear un issue antes de comenzar trabajos mayores 