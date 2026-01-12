# Manual de Usuario - Proyecto Dragolandia

## 1. Introducción

Este documento describe cómo instalar, configurar y utilizar la aplicación **Dragolandia** incluida en este repositorio. La aplicación es una pequeña demo Java que gestiona entidades como `Dragon`, `Mago`, `Monstruo` y `Bosque` y utiliza Hibernate para persistencia.

## 2. Requisitos previos

- Java 17+ (se recomienda Java 17 o superior). En el proyecto se usó OpenJDK 21 en el entorno de desarrollo.
- Maven 3.6+ para compilar y ejecutar el proyecto.
- Base de datos (según configuración en `hibernate.cfg.xml`) — la demo puede usar una base embebida o la configuración que proporcione el `persistence.xml`.

## 3. Estructura del proyecto

- `demo/src/main/java/com/example/entidades/` : clases de entidad (`Dragon`, `Mago`, `Monstruo`, etc.).
- `demo/src/main/java/com/example/controllers/` : controladores que contienen la lógica de acceso a datos a través de Hibernate.
- `demo/src/main/java/com/example/` : clases principales `Main.java`, `MenuConsola.java`, `Batalla.java`.
- `demo/src/main/resources/hibernate.cfg.xml`` y `META-INF/persistence.xml` : configuración de persistencia.

## 4. Compilación

Desde la raíz del proyecto ejecute:

```bash
mvn -f demo/pom.xml clean package
```

Esto generará el jar en `demo/target/` y compilará las clases.

## 5. Ejecución

**Opción 1 — Ejecutar desde Maven:**

```bash
mvn -f demo/pom.xml exec:java -Dexec.mainClass=com.example.Main
```

**Opción 2 — Ejecutar el jar compilado:**

```bash
java -cp demo/target/classes:demo/target/dependency/* com.example.Main
```

> Nota: Ajuste el classpath según dependencias y el sistema operativo.

## 6. Uso de la aplicación

Al iniciar, `MenuConsola` presenta un menú por consola para gestionar entidades y simular batallas.

- Crear/editar/eliminar `Dragon`, `Mago` y `Monstruo` a través de las opciones del menú.
- `Batalla` permite simular enfrentamientos entre entidades usando sus hechizos/ataques.

## 7. Configuración de la base de datos

Edite `demo/src/main/resources/hibernate.cfg.xml` para indicar la URL, usuario y contraseña de la base de datos que desee usar. Para pruebas locales puede usar H2 o SQLite si la configuración está preparada.

## 8. Estructura de datos y entidades

- `Dragon`: atributos principales y métodos específicos del dragón.
- `Mago`: contiene una lista de `Hechizo` y métodos para lanzar hechizos.
- `Monstruo`: entidad para oponentes genéricos.
- `Bosque`: entidad de entorno.

Consulte el código fuente en `demo/src/main/java/com/example/entidades/` para detalles de cada propiedad y mapeo Hibernate.

## 9. Desarrollo y pruebas

- Para ejecutar pruebas unitarias (si hay), use:

```bash
mvn -f demo/pom.xml test
```

- Para depurar, ejecute la clase `com.example.Main` desde un IDE (por ejemplo, VS Code o IntelliJ) y establezca puntos de interrupción en `MenuConsola` o en los controladores.

## 10. Docker / despliegue

El repositorio contiene un `Docker-compose.yml` en la raíz. Revise el contenido para ver servicios disponibles (base de datos, etc.). Ajuste la configuración de `hibernate.cfg.xml` para apuntar al servicio de base de datos del contenedor si procede.

## 11. Buenas prácticas y notas

- Haga backup de la base de datos antes de ejecutar operaciones destructivas.
- Use los `controllers` para todas las operaciones de persistencia en lugar de manipular directamente `Session` en la UI.

## 12. Contacto y soporte

Para dudas sobre el proyecto, contacte al autor o revise el `README.md` del repositorio para información adicional.

---

**Versión del documento:** 1.0
