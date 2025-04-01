# 🚀 Autonomous Dev Agent

This MCP (Message Control Protocol) server is a platform that enables different AIs (like Claude) to act as autonomous development agents, providing capabilities to automate the entire software development process, from idea conception to final testing.

## 🎯 Main Objective

The main objective of this server is to enable AIs to develop software autonomously, performing tasks such as:

- Project structure creation
- Requirements file generation
- Code development
- Test implementation
- Version control management
- And any other software development related tasks

## ✨ Main Features

The server provides capabilities for:

- Reading and writing files
- Making network requests
- Connecting and querying MySQL databases
- Executing system commands
- Managing Git repositories
- And more...

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📋 Requirements

- Java 17 or higher
- Maven 3.6 or higher
- MySQL Server (optional, only if database functionality is planned)
- Git (for version control functionalities)

## 📁 Project Structure

```tree
src/main/java/com/claude/mcp/
├── MCPServer.java           # Main server orchestrating all services
├── model/
│   └── Message.java        # Message model for communication
└── service/
    ├── FileService.java    # Service for file operations
    ├── DatabaseService.java # Service for database operations
    ├── NetworkService.java  # Service for network operations
    ├── GitService.java      # Service for Git operations
    └── CommandService.java  # Service for system command execution
```

## 📝 Message Format

Messages are exchanged in JSON format with the following structure:

```json
{
    "type": "MESSAGE_TYPE",
    "content": "optional content",
    "parameters": {
        // Specific parameters according to message type
    }
}
```

### 📨 Message Types

1. **FILE_READ**

   ```json
   {
       "type": "FILE_READ",
       "parameters": {
           "filePath": "/path/to/file.txt"
       }
   }
   ```

2. **FILE_WRITE**

   ```json
   {
       "type": "FILE_WRITE",
       "parameters": {
           "filePath": "/path/to/file.txt",
           "content": "content to write"
       }
   }
   ```

3. **NETWORK_REQUEST**

   ```json
   {
       "type": "NETWORK_REQUEST",
       "parameters": {
           "url": "https://api.example.com",
           "method": "GET",
           "body": "{}"  // Optional, only for POST
       }
   }
   ```

4. **DATABASE_QUERY**

   ```json
   {
       "type": "DATABASE_QUERY",
       "parameters": {
           "connectionId": "conn1",
           "query": "SELECT * FROM table WHERE id = ?",
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
           "args": ["-m", "commit message"]
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

## 🚀 Usage

1. Build the project:

   ```bash
   mvn clean package
   ```

2. Run the server:

   ```bash
   java -jar target/autonomous-dev-agent-1.0-SNAPSHOT.jar
   ```

3. Connect from any AI compatible with the MCP protocol.

## 🔒 Security

- The server should run with minimum necessary permissions
- Authentication is recommended before using in production
- Database credentials should be handled securely
- System command limits and validations must be implemented
- Using an isolated environment for testing is recommended

## 📊 Logging

The server uses SLF4J with Logback for event logging. Logs can be configured in `src/main/resources/logback.xml`.

## 🤝 Contributing

Contributions are welcome. Please ensure to:

1. Follow the project's style guidelines
2. Include tests for new functionality
3. Update documentation as needed
4. Create an issue before starting major work
