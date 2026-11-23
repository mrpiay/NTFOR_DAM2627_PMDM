# Centro de acogida

App Android en Kotlin con Jetpack Compose para consultar los animales de un centro de acogida y solicitar su adopción.

| Qué abarca | Dónde |
|---|---|
| Modelo de datos y datos de ejemplo | `Animal.kt`, `DatosAnimales.kt` |
| Componente reutilizable: la tarjeta de un animal | `TarjetaAnimal.kt` |
| Lista con `Scaffold`, `Switch` para filtrar, buscador por nombre y estado | `PantallaLista.kt`, `AcogidaUiState.kt` |
| Ciclo de vida de la actividad, visible en Logcat con el filtro `tag:Ciclo` | `MainActivity.kt` |
| `ViewModel`, estado de la pantalla y `StateFlow` | `AcogidaViewModel.kt`, `AcogidaUiState.kt` |
| Navegación entre pantallas con Navigation Compose | `AcogidaApp.kt` |
| Pantalla de detalle | `PantallaDetalle.kt` |
| Formulario «Solicitar adopción» con validación | `FormularioUiState.kt`, `PantallaSolicitud.kt` |
| Diseño para varios tamaños: cuadrícula adaptativa en la lista y ancho máximo en el detalle y el formulario | `PantallaLista.kt`, `PantallaDetalle.kt`, `PantallaSolicitud.kt` |
| Persistencia con Room: las solicitudes se guardan y se listan en «Mis solicitudes» | `Solicitud.kt`, `SolicitudDao.kt`, `AcogidaDatabase.kt`, `PantallaSolicitudes.kt` |
| Permiso en tiempo de ejecución (cámara) y foto del animal con la cámara del sistema | `AndroidManifest.xml`, `PantallaDetalle.kt` |
| La lista de animales desde Internet con Retrofit y kotlinx.serialization, con aviso si no hay conexión | `ServicioAnimales.kt`, `Animal.kt`, `AcogidaViewModel.kt`, `PantallaLista.kt` |
| Recargar la lista al agitar el móvil (acelerómetro) o con un botón | `Sacudidas.kt`, `PantallaLista.kt` |
| La distancia al centro con la ubicación y sus permisos | `Ubicacion.kt`, `PantallaLista.kt`, `AndroidManifest.xml` |
| La foto recortada y guardada como JPEG, un sonido al enviar una solicitud y una animación en el aviso de urgencia | `Multimedia.kt`, `PantallaDetalle.kt`, `PantallaSolicitud.kt`, `TarjetaAnimal.kt`, `res/raw/adoptado.wav` |
| Mensajes en Logcat con el filtro `tag~:^CentroAcogida$` y la documentación de la app | `AcogidaViewModel.kt`, `DOCUMENTACION.md` |

Los archivos de código están en `app/src/main/java/com/example/centroacogida/`.

## Abrirlo en Android Studio

1. Copia la carpeta `CentroAcogida` a una carpeta sin tildes ni espacios, por ejemplo `C:\dam2627\pmdm`.
2. En Android Studio: *File > Open* y elige la carpeta `CentroAcogida`, la que contiene `settings.gradle.kts`. Si pregunta si confías en el proyecto, *Trust Project*.
3. Espera a que termine el **Sync** de Gradle (barra inferior). La primera vez descarga las bibliotecas, entre ellas Room, KSP y Retrofit, y tarda unos minutos.
4. Elige el emulador del móvil en la barra superior y pulsa **Run** (el triángulo verde).

**Comprueba que…** aparece la lista de animales; al tocar uno se abre su detalle; «Solicitar adopción» valida el formulario; la solicitud enviada aparece en «Mis solicitudes», también después de cerrar y volver a abrir la app.

Está probado con **Android Studio 2026.2**. `local.properties` no viene en la carpeta: Android Studio lo crea al abrir el proyecto, con la ruta de tu SDK.

## Si el Sync falla

Lee la primera línea roja de la pestaña *Build*. Si habla de `jlink` o del JDK, en *File > Settings > Build, Execution, Deployment > Build Tools > Gradle* elige como **Gradle JDK** el que trae Android Studio (`jbr`).
