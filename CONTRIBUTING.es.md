# Contribuir a Servidor MCP

Primero, ¡gracias por considerar contribuir a Servidor MCP! Son personas como tú las que hacen que Servidor MCP sea una herramienta tan excelente.

## Código de Conducta

Este proyecto y todos los que participan en él se rigen por nuestro [Código de Conducta](CODE_OF_CONDUCT.es.md). Al participar, se espera que sigas este código.

## ¿Cómo Puedo Contribuir?

### Reportar Errores

Antes de crear reportes de errores, por favor revisa la lista de issues ya que podrías descubrir que no necesitas crear uno. Cuando estés creando un reporte de error, por favor incluye tantos detalles como sea posible:

* Usa un título claro y descriptivo
* Describe los pasos exactos que reproducen el problema
* Proporciona ejemplos específicos para demostrar los pasos
* Describe el comportamiento que observaste después de seguir los pasos
* Explica qué comportamiento esperabas ver en su lugar y por qué
* Incluye capturas de pantalla y GIFs animados si es posible
* Incluye mensajes de error y trazas de pila si son aplicables

### Sugerir Mejoras

Las sugerencias de mejora se rastrean como issues de GitHub. Crea un issue y proporciona la siguiente información:

* Usa un título claro y descriptivo
* Proporciona una descripción paso a paso de la mejora sugerida
* Proporciona ejemplos específicos para demostrar los pasos
* Describe el comportamiento actual y explica qué comportamiento esperabas ver en su lugar
* Explica por qué esta mejora sería útil
* Lista cualquier solución o característica alternativa que hayas considerado

### Pull Requests

* Completa la plantilla requerida
* No incluyas números de issue en el título del PR
* Incluye capturas de pantalla y GIFs animados en tu pull request cuando sea posible
* Sigue las guías de estilo de Java
* Termina todos los archivos con una nueva línea

## Proceso de Desarrollo

Usamos GitHub para alojar el código, rastrear issues y solicitudes de características, así como aceptar pull requests.

1. Haz fork del repositorio y crea tu rama desde `main`.
2. Si has agregado código que debe ser probado, agrega pruebas.
3. Si has cambiado APIs, actualiza la documentación.
4. Asegúrate de que la suite de pruebas pase.
5. Asegúrate de que tu código cumpla con los estándares.
6. ¡Envía ese pull request!

## Guías de Estilo

### Mensajes de Commit de Git

* Usa el tiempo presente ("Agregar característica" no "Agregada característica")
* Usa el modo imperativo ("Mover cursor a..." no "Mueve cursor a...")
* Limita la primera línea a 72 caracteres o menos
* Referencia issues y pull requests libremente después de la primera línea
* Considera comenzar el mensaje de commit con un emoji aplicable:
    * 🎨 `:art:` cuando mejoras el formato/estructura del código
    * 🐎 `:racehorse:` cuando mejoras el rendimiento
    * 🚱 `:non-potable_water:` cuando corriges fugas de memoria
    * 📝 `:memo:` cuando escribes documentación
    * 🐛 `:bug:` cuando corriges un error
    * 🔥 `:fire:` cuando eliminas código o archivos
    * 💚 `:green_heart:` cuando corriges la compilación CI
    * ✅ `:white_check_mark:` cuando agregas pruebas
    * 🔒 `:lock:` cuando tratas con seguridad
    * ⬆️ `:arrow_up:` cuando actualizas dependencias
    * ⬇️ `:arrow_down:` cuando degradas dependencias

### Guía de Estilo de Java

* Usa 4 espacios para la indentación
* Usa nombres significativos para variables y métodos
* Agrega comentarios para lógica compleja
* Sigue las convenciones de nombres de Java
* Mantén los métodos enfocados y pequeños
* Usa modificadores de acceso apropiados
* Maneja las excepciones apropiadamente

## Notas Adicionales

### Etiquetas de Issues y Pull Requests

Esta sección lista las etiquetas que usamos para ayudarnos a rastrear y gestionar issues y pull requests.

### Tipo de Issue y Estado del Issue

- `enhancement`: Solicitudes de características
- `bug`: Algo no está funcionando
- `documentation`: Mejoras o adiciones a la documentación
- `good first issue`: Bueno para principiantes
- `help wanted`: Se necesita atención extra
- `question`: Se solicita más información
- `wontfix`: Esto no se trabajará
- `invalid`: Esto no parece correcto
- `duplicate`: Este issue o pull request ya existe

### Etiquetas de Pull Request

- `work in progress`: Pull requests que aún están en desarrollo, seguirán más cambios
- `needs review`: Pull requests que necesitan revisión de código y aprobación de los mantenedores o que necesitan pruebas finales
- `under review`: Pull requests siendo revisados por los mantenedores
- `requires changes`: Pull requests que necesitan ser actualizados basados en comentarios de revisión y luego revisados nuevamente
- `needs testing`: Pull requests que necesitan pruebas manuales 