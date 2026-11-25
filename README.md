# TioTec Preventivo

Aplicación Android desarrollada para el caso **TioTec** de MCmóvil SpA: una herramienta de
prevención y educación tecnológica que permite a personas no especialistas evaluar sus
hábitos de respaldo, actualización, autenticación y recuperación de cuentas, y recibir
recomendaciones claras para reducir riesgos.

Proyecto académico de la asignatura **DSY1105 — Desarrollo de Aplicaciones Móviles**,
Evaluación Parcial 2, DUOC UC.

## Repositorio

https://github.com/NicolasRJ6/AppTioTec_N

## Integrantes

- Sebastián Antipán
- Nicolás Delgado

**Grupo:** 12

## Funcionalidades

- **Perfil de prueba:** alias, nivel de experiencia digital, dispositivo y sistema operativo,
  con validación campo por campo.
- **Evaluación preventiva:** preguntas por categoría (respaldos, actualizaciones,
  autenticación y recuperación de cuentas) con barra de progreso y respuesta obligatoria.
- **Resultado:** puntaje de 0 a 100, nivel de riesgo (verde, amarillo o rojo), brechas por
  categoría y recomendaciones priorizadas.
- **Checklist de acciones:** tareas pendientes y completadas, guardadas en el dispositivo.
- **Historial:** evaluaciones anteriores almacenadas localmente, con el detalle de sus
  acciones.
- **Ajustes:** activación y hora del recordatorio, guardadas en el dispositivo con DataStore.
- **Foto de perfil desde la galería:** se elige una imagen del teléfono, se copia a la carpeta
  privada de la app y se muestra redonda en el perfil.
- **Notificaciones:** canal de recordatorios, permiso de Android 13 en adelante y botón para
  probar el aviso.
- **Consejos de seguridad desde API REST:** consume `jsonplaceholder.typicode.com/posts` con
  Retrofit y guarda una copia local en Room. La pantalla muestra la última actualización y
  sigue funcionando sin conexión con los datos guardados.
- **Consejos del microservicio:** pantalla con CRUD completo (listar, crear, editar y eliminar)
  contra el microservicio Spring Boot; los cambios quedan guardados en la base H2.

Todos los datos son ficticios. La aplicación no registra información real, sensible ni de
menores.

## Arquitectura

El proyecto sigue el patrón **MVVM** (Modelo — Vista — ViewModel) con Jetpack Compose y
Material 3.

```
app/src/main/java/com/example/apptiotec_n/
  model/              clases de datos del dominio
  data/
    local/            Room: entidades, DAOs y base de datos
    model/            modelos de la API remota (Consejo)
    preference/       DataStore: preferencias y recordatorios
    remote/           Retrofit: ApiService (API externa) y ConsejoApiService (microservicio)
    repository/       repositorios que conectan datos locales y remotos con ViewModel
  ui/
    screens/          una pantalla por flujo
    theme/            colores y tipografía
    utils/            utilidades de tamaño de pantalla
  viewmodel/          estado de la UI y validaciones
  navigation/         rutas y eventos de navegación
```

La interfaz no valida nada: solo observa el estado que expone el ViewModel y le envía
eventos. Toda la lógica de validación vive en el ViewModel.

## Consumo de API REST y persistencia

La pantalla **Consejos de seguridad** implementa el flujo de la Experiencia de Aprendizaje 3:

```
JSONPlaceholder (/posts)
        ↓ Retrofit
ConsejosRepository
        ↓ Room (tabla consejos)
ConsejosViewModel (StateFlow)
        ↓
ConsejosScreen (Compose)
```

1. Al abrir, el ViewModel observa la copia local de Room y la muestra de inmediato.
2. En paralelo, `sincronizar()` descarga los consejos con Retrofit.
3. La respuesta se guarda en Room en una sola transacción, junto con la fecha de actualización.
4. Si la API falla, la lista guardada se conserva y la pantalla muestra un aviso con opción de
   reintentar.
5. La base de datos sube a la versión 2 con una migración que crea la tabla `consejos`, sin
   borrar los datos anteriores de perfil, evaluaciones e historial.

## Microservicio de consejos

El proyecto incluye un microservicio propio en Spring Boot (carpeta `microservicio/`) que
expone el CRUD de los consejos de seguridad sobre una base H2 persistente.

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/consejos` | Lista los consejos |
| GET | `/api/consejos/{id}` | Obtiene un consejo |
| POST | `/api/consejos` | Crea un consejo |
| PUT | `/api/consejos/{id}` | Actualiza un consejo |
| DELETE | `/api/consejos/{id}` | Elimina un consejo |

Para ejecutarlo, desde la carpeta `microservicio/`:

```
mvn spring-boot:run
```

Queda disponible en `http://localhost:8080`. Los datos se guardan en `microservicio/data/`
y se conservan entre reinicios. Las pruebas se ejecutan con `mvn test`.

### Integración con la app

La pantalla **Consejos del microservicio** consume estos endpoints con Retrofit:

```
ConsejoApiService (GET, POST, PUT, DELETE)
        ↓
ConsejosServidorRepository
        ↓
ConsejosServidorViewModel (StateFlow)
        ↓
ConsejosServidorScreen (lista + formulario de crear/editar + confirmar eliminar)
```

Después de cada cambio la app vuelve a consultar `GET /api/consejos`, por lo que la lista
muestra siempre lo que quedó en la base H2.

Para que el teléfono llegue al microservicio, con el teléfono conectado por USB:

```
adb reverse tcp:8080 tcp:8080
```

La URL base está en `data/remote/RetrofitMicroservicio.kt`. Si el teléfono se conecta por
Wi-Fi a la misma red del computador, se reemplaza por `http://IP_DEL_COMPUTADOR:8080/`
(por ejemplo `http://192.168.1.11:8080/`); en el emulador de Android Studio se usa
`http://10.0.2.2:8080/`.

## Cómo ejecutar

1. Clonar el repositorio.
2. Abrir la carpeta del proyecto en Android Studio.
3. Esperar la sincronización de Gradle.
4. Ejecutar en un dispositivo o emulador con Android 8.0 (API 26) o superior.

Para compilar desde la terminal:

```
./gradlew assembleDebug
```

## Tecnologías

- Kotlin 2.2.10
- Jetpack Compose (BOM 2026.02.01) y Material 3
- Navigation Compose
- Room (persistencia local)
- DataStore Preferences
- Retrofit 2.11 + Gson (consumo de API REST)
- Spring Boot 3.5.6 + Spring Data JPA + H2 (microservicio)
- Android Studio, Gradle 9.6

## Planificación

El avance y el reparto de tareas se registran en el tablero de Trello del equipo:
https://trello.com/b/f0eKbBfy/ep2-tiotec

## Estado del proyecto

| Indicador | Estado |
|---|---|
| Interfaz y navegación | `NavHost` con Home, Perfil, Evaluación, Resultado, Historial y Ajustes |
| Formularios validados | Perfil con 4 campos y evaluación con 10 preguntas, con error por campo |
| Validación desacoplada | Validación en `PerfilViewModel`, `EvaluacionViewModel` y `AjustesViewModel` |
| Animaciones | Errores de la evaluación, checklist del resultado, aviso de guardado y detalle del historial, todas con `AnimatedVisibility` |
| Modularidad y persistencia local | Room para perfil, evaluaciones, respuestas y checklist; DataStore para los ajustes del recordatorio; el historial se lee desde la base local |
| GitHub y Trello | Repositorio y tablero activos |
| Recursos nativos | Galería (`ActivityResultContracts.GetContent`) y notificaciones (`POST_NOTIFICATIONS`) integradas en Perfil y Ajustes |
| Consumo de API REST | `ApiService` + `RetrofitInstance` consumen JSONPlaceholder; `ConsejosScreen` con carga, error y reintento |
| Persistencia de la API | Room guarda los consejos y su fecha de actualización; la pantalla funciona sin conexión |
| Integración con el microservicio | `ConsejoApiService` + `ConsejosServidorRepository` consumen `/api/consejos`; `ConsejosServidorScreen` permite listar, crear, editar y eliminar con avisos de error |
| Pruebas | 32 pruebas unitarias de la app con Kotest, JUnit5, MockK y corrutinas de prueba; 6 pruebas del microservicio |
| Microservicio | Spring Boot 3.5 con CRUD de consejos en `/api/consejos`, validación y base H2 persistente |
