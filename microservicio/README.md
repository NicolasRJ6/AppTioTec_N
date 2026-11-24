# Microservicio de consejos

Backend de TioTec: Spring Boot 3.5, Java 21, Spring Data JPA y H2.

## Ejecutar

Desde esta carpeta:

```
mvn spring-boot:run
```

El servicio queda en `http://localhost:8080`. La base H2 se guarda en `data/tiotec.mv.db`,
por lo que los datos se conservan al reiniciar.

Consola de la base: `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:file:./data/tiotec`, usuario `sa`, sin clave).

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/consejos` | Lista todos los consejos |
| GET | `/api/consejos/{id}` | Obtiene un consejo |
| POST | `/api/consejos` | Crea un consejo |
| PUT | `/api/consejos/{id}` | Actualiza un consejo |
| DELETE | `/api/consejos/{id}` | Elimina un consejo |

Cuerpo para crear o actualizar:

```json
{
  "titulo": "Usa una contrasena larga",
  "descripcion": "Mezcla letras, numeros y simbolos"
}
```

## Pruebas

```
mvn test
```
