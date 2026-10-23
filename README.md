# TioTec Preventivo

Aplicación Android desarrollada para el caso **TioTec** de MCmóvil SpA: una herramienta de
prevención y educación tecnológica que permite a personas no especialistas evaluar sus
hábitos de respaldo, actualización, autenticación y recuperación de cuentas, y recibir
recomendaciones claras para reducir riesgos.

Proyecto académico de la asignatura **DSY1105 — Desarrollo de Aplicaciones Móviles**,
Evaluación Parcial 2, DUOC UC.

## Integrantes

- Sebastián Anipán
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
- **Historial:** evaluaciones anteriores almacenadas localmente.
- **Evidencia con galería:** adjuntar una imagen ficticia al perfil.
- **Recordatorios:** notificación local para volver a evaluar.

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
    preference/       DataStore: preferencias y recordatorios
    repository/       repositorios que conectan datos con ViewModel
  ui/
    screens/          una pantalla por flujo
    components/       componentes reutilizables
    theme/            colores y tipografía
  viewmodel/          estado de la UI y validaciones
  navigation/         rutas y eventos de navegación
```

La interfaz no valida nada: solo observa el estado que expone el ViewModel y le envía
eventos. Toda la lógica de validación vive en el ViewModel.

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
- Android Studio, Gradle 9.6

## Planificación

El avance y el reparto de tareas se registran en el tablero de Trello del equipo:
https://trello.com/b/f0eKbBfy/ep2-tiotec

## Estado del proyecto

| Indicador | Estado |
|---|---|
| Interfaz y navegación | `NavHost` con Home, Perfil, Evaluación, Resultado y Ajustes |
| Formularios validados | Formulario de perfil con 4 campos y error por campo |
| Validación desacoplada | Validación en `PerfilViewModel.validarFormulario()` |
| Animaciones | Aviso de perfil guardado con `AnimatedVisibility` |
| Modularidad y persistencia local | Room en uso: perfil, evaluaciones, respuestas y checklist |
| GitHub y Trello | Repositorio y tablero activos |
| Recursos nativos | Pendiente (galería y notificaciones) |
