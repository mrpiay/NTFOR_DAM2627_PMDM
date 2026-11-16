# PMDM · Proyecto guiado «Centro de acogida 2.0» · 3.ª parte

## Punto de partida

Es la continuación de la 2.ª parte (semana del 9 al 13 de noviembre, pasos 8 a 10): la app del centro de acogida con la lista adaptada a dos tamaños de pantalla, las solicitudes guardadas con Room, el buscador y la foto con la cámara. En esta 3.ª parte se toca cada tema que falta de la UD3 y la UD4 con un caso práctico corto:

- la lista se carga **desde Internet** (UD3);
- al **agitar el móvil** se recarga (UD3, un sensor);
- la app calcula a qué **distancia** está el centro, con la **ubicación** (UD3);
- la foto del animal se **procesa y se guarda como JPEG**, suena un **sonido** al enviar una solicitud y el aviso de adopción urgente tiene una **animación** (UD4);
- se **depura** con Logcat, se genera el **APK** y se **documenta** la app (UD3 y UD4).

El código técnico (cómo se habla con el servidor, con el acelerómetro, con la ubicación o con las imágenes) viene **ya hecho**, en un archivo por paso: tú lo colocas en el proyecto, lo conectas con la app y compruebas que funciona. Lo que no se practica (el segundo plano con WorkManager, los mapas, Bluetooth, el vídeo y Flutter) está en los [apuntes de esta semana](../apuntes).

**Usa la IA como guía, no para que lo haga por ti.** Pídele los **pasos necesarios** para implementar cada cosa y que te explique lo que no entiendas; **el código lo pones tú en tu proyecto**, aunque sea copiando y pegando, sabiendo **dónde** lo pones y **para qué**: es la única forma de familiarizarte con tu entorno. El objetivo es que lo que hagas **funcione y cumpla los requisitos** de cada paso («Comprueba que…») y que sepas **dónde está cada cosa y qué habría que tocar para cambiar o añadir algo**: es lo que te van a pedir después.

**El objetivo es entender cada paso:** es lo que necesitas para responder el cuestionario y preparar los exámenes.

**Evaluación.** Esta actividad guiada (**A3**) comprende los pasos 11 a 15. Se califica por el nivel de cumplimiento de sus pasos: nada (0), poco (2,5), medio (5), alto (7,5) o completo (10). Los «Comprueba que…» de cada paso indican si está cumplido; qué supone cada nivel, en las [rúbricas](../../programacion/rubricas.md). El cuestionario de la semana (**C3**) se entrega hasta el lunes 23 de noviembre. La semana del 23 al 27, el martes 1 y el miércoles 2 de diciembre son de **repaso**. El **viernes 4 de diciembre** hay un examen parcial, **P2**, en papel, con todo lo de la 1ª evaluación que no evaluó Julia: las tres partes de esta actividad y los apuntes de la UD1 a la UD4. De lo que solo está en los apuntes, las preguntas son de conceptos y criterio, no de código. Qué entra y cuánto vale cada parte: [1ª evaluación](../../programacion/1a-evaluacion.md).

Del 17 al 20 de noviembre. Cada paso trae una pista y qué hacer si algo falla.

## Con qué trabajas

- **Si terminaste la 2.ª parte y funciona**: sigue con tu proyecto, en la misma carpeta.
- **Si no la terminaste o no te funciona**: cambia el nombre de tu carpeta a `CentroAcogida-anterior`. Descarga la plantilla, [`plantilla.zip`](https://github.com/mrpiay/NTFOR_DAM2627_PMDM/raw/main/2026_11_16-20/actividades/plantilla.zip), haz doble clic en el zip, arrastra la carpeta de dentro, `CentroAcogida`, a una carpeta sin tildes ni espacios, por ejemplo `C:\dam2627\pmdm`, y ábrela en Android Studio: en la pantalla de bienvenida, **Open** (si ya hay un proyecto abierto, *File > Open*), y elige la carpeta `CentroAcogida`, la que contiene `settings.gradle.kts`. Es la 2.ª parte terminada; los comentarios `TODO Paso 11.1`, `TODO Paso 12.2`… (con `//` en Kotlin, `#` en `libs.versions.toml` y `<!-- -->` en el manifiesto) marcan dónde se añade código, y `PasosSiguientes.kt` dice qué archivos se crean.

## Objetivo

Añadir a la app:

1. la lista de animales **desde Internet**, con un aviso si no hay conexión;
2. **recargar** la lista al agitar el móvil o con un botón;
3. la **distancia** desde donde estás hasta el centro;
4. la foto del animal **recortada y guardada como JPEG**, un **sonido** al enviar una solicitud y una **animación** en el aviso de adopción urgente;
5. los mensajes de la app en **Logcat**, el **APK** y una **documentación** breve.

## Qué se trabaja

| Unidad | Qué | Dónde |
|---|---|---|
| UD3 · Conectividad | HTTP, JSON, Retrofit, el permiso `INTERNET`, corrutinas y tratar el error | Paso 11 |
| UD3 · Sensores | El acelerómetro: registrar y quitar el sensor, detectar una sacudida | Paso 12 |
| UD3 · Permisos y localización | Permisos de ubicación aproximada y precisa, la ubicación actual y la distancia | Paso 13 |
| UD4 · Imágenes | Procesar (recortar y escalar) y convertir (JPEG) una foto, y guardarla | Paso 14 |
| UD4 · Audio y animaciones | Reproducir un sonido con `MediaPlayer` y liberarlo; una animación sin fin | Paso 14 |
| UD3 y UD4 · Desplegar, depurar y documentar | Logcat, el APK y la documentación | Paso 15 |
| UD2 · Probar y documentar | Las pruebas de la app, en una tabla | Paso 15 |

## Plan de la semana

Cada sesión dura 50 minutos.

| Día y hora | Qué se hace |
|---|---|
| Mar 17 nov, 08:30 | Al empezar: añadir las dependencias de **11.1** y pulsar *Sync Now*. Mientras descarga, leer el apartado 1 de los [apuntes de la UD3](../apuntes/UD3-conectividad-sensores-y-segundo-plano.md). Después, **11.2 a 11.4**. |
| Mar 17 nov, 09:20 | **Paso 11** (11.5 y 11.6) y su «Comprueba que…». |
| Mié 18 nov, 08:30 | **Paso 12** (agitar para recargar). |
| Mié 18 nov, 09:20 | **Paso 13** (la distancia al centro). |
| Mié 18 nov, 10:10 | **Paso 14** (multimedia: la foto, el sonido y la animación). |
| Vie 20 nov, 13:00 | **Paso 15** (Logcat, el APK y la documentación). |
| Vie 20 nov, 13:50 | Comprobar todo con la lista final («Cómo comprobarlo»). |

**Los pasos se hacen en orden**, cada uno en la sesión que le toca según el plan de la semana: cada paso usa lo del anterior.

**Descargas.** Con toda la clase en la misma red, las descargas de Gradle tardan. Haz el *Sync* de 11.1 al principio de la sesión del martes, no a mitad.

## Cómo está escrito cada paso

**Objetivo** (qué consigues), **Pista** (código o explicación corta), **Comprueba que…** (cómo saber que está bien) y **Si te falla…** (errores típicos y cómo resolverlos).

El código usa el paquete `com.example.centroacogida`. Cuando Android Studio marque en rojo un nombre, pon el cursor encima y pulsa **Alt + Intro** para añadir el `import`. Para crear un archivo nuevo de Kotlin: en el panel *Project*, clic derecho sobre la carpeta `com.example.centroacogida` → *New > Kotlin Class/File* → *File*, y escribe el nombre sin `.kt`.

---

## Paso 11 · Los animales desde Internet

**Objetivo.** Que la lista de animales venga de un servidor, en JSON, y que si no hay conexión la app avise y siga con los animales que ya tenía.

**Mini-explicación.** La lista está en un archivo JSON publicado en el repositorio del módulo: [`animales.json`](animales.json). Tiene diez animales: los ocho de la app y dos nuevos, Bruno y Lola. La app hace una petición **GET** con **Retrofit**, que convierte el JSON en una `List<Animal>` con **kotlinx.serialization**. La petición se hace en una **corrutina**, fuera del hilo principal. Qué es cada cosa y qué otras opciones hay: [apuntes de la UD3, apartado 1](../apuntes/UD3-conectividad-sensores-y-segundo-plano.md#1-datos-desde-internet-http-y-retrofit).

**Pista.**

**11.1 · Las bibliotecas.** Como en el paso 9.1 con Room, en tres archivos:

- En `gradle/libs.versions.toml`, en `[versions]`:

  ```toml
  retrofit = "3.0.0"
  kotlinxSerialization = "1.9.0"
  ```

  En `[libraries]`:

  ```toml
  retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
  retrofit-converter-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
  kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
  ```

  Y en `[plugins]` (usa la misma versión que Kotlin):

  ```toml
  kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
  ```

- En el `build.gradle.kts` del proyecto (la raíz), dentro de `plugins { }`:

  ```kotlin
  alias(libs.plugins.kotlin.serialization) apply false
  ```

- En `app/build.gradle.kts`, dentro de `plugins { }`:

  ```kotlin
  alias(libs.plugins.kotlin.serialization)
  ```

  Y dentro de `dependencies { }`, debajo de Room:

  ```kotlin
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.kotlinx.serialization)
  implementation(libs.kotlinx.serialization.json)
  ```

Pulsa **Sync Now** (arriba a la derecha) y espera a que termine.

**11.2 · El permiso.** En `AndroidManifest.xml`, debajo del permiso de la cámara. Es un permiso **normal**: se concede al instalar, no hay que pedirlo.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

**11.3 · El animal, convertible desde JSON.** En `Animal.kt`, `@Serializable` delante de la clase. La imagen no viene en el JSON: con `@Transient` y un valor por defecto, se usa ese valor.

```kotlin
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Animal(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    @Transient @DrawableRes val imagen: Int = R.drawable.ic_launcher_foreground,
    val urgente: Boolean,
    val descripcion: String = ""
)
```

Los nombres de las propiedades tienen que coincidir con los del JSON (`"nombre"`, `"edad"`…).

**11.4 · El servicio (ya hecho).** Crea el archivo `ServicioAnimales.kt` con este contenido:

```kotlin
package com.example.centroacogida

// ServicioAnimales.kt: la lista de animales desde Internet, con Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

// La interfaz: cada función es una petición. Retrofit escribe el código que la hace.
interface ServicioAnimales {
    // GET a URL_BASE + "animales.json". suspend: se llama desde una corrutina
    @GET("animales.json")
    suspend fun animales(): List<Animal>
}

// Un solo objeto Retrofit para toda la app
object Red {
    // La parte común de todas las direcciones (termina en /)
    private const val URL_BASE =
        "https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/2026_11_16-20/actividades/"

    // ignoreUnknownKeys: si el JSON trae campos que Animal no tiene, se ignoran
    private val json = Json { ignoreUnknownKeys = true }

    val servicio: ServicioAnimales = Retrofit.Builder()
        .baseUrl(URL_BASE)
        // El conversor: pasa el JSON de la respuesta a objetos Animal
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ServicioAnimales::class.java)
}
```

**11.5 · El estado y el ViewModel.** En `AcogidaUiState.kt`, dos propiedades más, después de `textoBusqueda` (pon una coma al final de esa línea):

```kotlin
val cargando: Boolean = false,
val aviso: String? = null
```

En `AcogidaViewModel.kt`, debajo de `val uiState …`, pide la lista al crear el ViewModel y cada vez que se llame a `cargarAnimales()`:

```kotlin
// Al crear el ViewModel, se pide la lista a Internet
init {
    cargarAnimales()
}

// Pide la lista de animales al servidor, en una corrutina (fuera del hilo principal)
fun cargarAnimales() {
    // Si ya se está cargando (por ejemplo, al agitar el móvil varias veces), no se empieza otra carga
    if (_uiState.value.cargando) return
    _uiState.update { it.copy(cargando = true, aviso = null) }

    viewModelScope.launch {
        try {
            val lista = Red.servicio.animales()
            Log.d("CentroAcogida", "Animales cargados de Internet: ${lista.size}")
            _uiState.update { it.copy(animales = lista, aviso = "Lista actualizada: ${lista.size} animales") }
        } catch (e: Exception) {
            // Sin conexión, error del servidor o JSON inesperado: se quedan los animales que ya había
            Log.e("CentroAcogida", "No se ha podido cargar la lista", e)
            _uiState.update { it.copy(aviso = "No se ha podido actualizar la lista: se muestran los animales guardados en la app") }
        } finally {
            _uiState.update { it.copy(cargando = false) }
        }
    }
}
```

Import: `android.util.Log`. `Log.d` y `Log.e` escriben en **Logcat**; se usan en el paso 15.

**11.6 · La pantalla.** En `PantallaLista.kt`:

- un parámetro más, al final, después de `onBuscar` (con una coma al final de esa línea):

  ```kotlin
  onRecargar: () -> Unit = {}
  ```

- dentro de la `Column`, justo después de la `Row` del interruptor «Adopciones urgentes», la barra de progreso mientras carga y el aviso:

  ```kotlin
  if (uiState.cargando) {
      LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
  }
  uiState.aviso?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
  ```

Y en `AcogidaApp.kt`, en la llamada a `PantallaLista(…)`, después de `onBuscar = viewModel::cambiarBusqueda` (con una coma al final de esa línea):

```kotlin
onRecargar = viewModel::cargarAnimales
```

`onRecargar` todavía no lo usa nadie: lo usan el paso 12 (agitar) y el botón de recargar.

**Comprueba que…**

- al abrir la app, durante un momento se ve la barra de progreso y después el aviso «**Lista actualizada: 10 animales**»;
- en la lista están **Bruno** (perro, urgente) y **Lola** (cobaya), que no están en `DatosAnimales.kt`;
- con el emulador **sin conexión** (desliza desde arriba y activa el **modo avión**), al cerrar y abrir la app sale «**No se ha podido actualizar la lista: se muestran los animales guardados en la app**» y los ocho animales de siempre. La app no se cierra. Vuelve a desactivar el modo avión;
- sabes explicar por qué la petición va en una corrutina y qué hace el conversor.

**Si te falla…**

- *`Unresolved reference: serialization`* o *`Plugin … was not found`*: falta el plugin en uno de los dos `build.gradle.kts`, o no has pulsado *Sync Now*.
- *Siempre sale «No se ha podido actualizar la lista»*: mira la línea roja de Logcat (paso 15) debajo de «No se ha podido cargar la lista». Si dice `SecurityException` o `Permission denied`, falta el permiso `INTERNET` (desinstala la app del emulador y vuelve a ejecutarla). Si dice `UnknownHostException`, el emulador no tiene Internet: comprueba que no esté en modo avión. Si dice `Serializer for class 'Animal' is not found`, falta `@Serializable` o el plugin.
- *`Field 'imagen' is required`*: falta `@Transient` o el valor por defecto de `imagen`.
- *La app se cierra con `NetworkOnMainThreadException`*: la llamada a `Red.servicio.animales()` no está dentro de `viewModelScope.launch`.

---

## Paso 12 · Agitar para recargar

**Objetivo.** Que al agitar el móvil se vuelva a pedir la lista a Internet, y que haya un botón que haga lo mismo.

**Mini-explicación.** El **acelerómetro** mide la aceleración en los tres ejes. En reposo, el módulo de la aceleración es la gravedad; si pasa de 2,7 veces la gravedad, es una sacudida. El sensor se **registra** al mostrar la pantalla y se **quita** al salir, para no gastar batería. Y como no todo el mundo puede agitar el móvil (ni todos los móviles tienen acelerómetro), siempre hay una **alternativa**: un botón. [Apuntes de la UD3, apartado 3](../apuntes/UD3-conectividad-sensores-y-segundo-plano.md#3-los-sensores).

**Pista.**

**12.1 · El detector (ya hecho).** Crea el archivo `Sacudidas.kt`:

```kotlin
package com.example.centroacogida

// Sacudidas.kt: avisa cuando se agita el móvil, con el acelerómetro
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlin.math.sqrt

// Cuántas veces la gravedad tiene que medir la aceleración para contar como sacudida
private const val UMBRAL = 2.7f

// Tiempo mínimo entre dos sacudidas, para no contar varias por una sola
private const val ESPERA_MS = 1000L

@Composable
fun AlAgitar(onSacudida: () -> Unit) {
    val context = LocalContext.current
    // Siempre la última versión de la acción, aunque la pantalla se vuelva a dibujar
    val accion by rememberUpdatedState(onSacudida)

    // DisposableEffect: registra el sensor al mostrar la pantalla y lo quita al salir
    DisposableEffect(Unit) {
        val gestor = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val acelerometro = gestor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var ultima = 0L

        val oyente = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val (x, y, z) = event.values
                // Módulo de la aceleración, en «gravedades»: en reposo vale 1
                val g = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH
                val ahora = System.currentTimeMillis()
                if (g > UMBRAL && ahora - ultima > ESPERA_MS) {
                    ultima = ahora
                    accion()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Si el dispositivo no tiene acelerómetro, no se registra nada
        if (acelerometro != null) {
            gestor.registerListener(oyente, acelerometro, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { gestor.unregisterListener(oyente) }
    }
}
```

**12.2 · Usarlo en la lista.** En `PantallaLista.kt`, al principio del cuerpo de `PantallaLista`, antes de `Scaffold(`:

```kotlin
// Agitar el móvil recarga la lista
AlAgitar(onSacudida = onRecargar)
```

**12.3 · El botón.** En la `TopAppBar`, cambia `actions` para que tenga un botón de recargar antes de «Mis solicitudes»:

```kotlin
actions = {
    // El mismo efecto que agitar, para quien no puede o no quiere agitar el móvil
    IconButton(onClick = onRecargar) {
        Icon(Icons.Filled.Refresh, contentDescription = "Recargar la lista")
    }
    TextButton(onClick = onVerSolicitudes) { Text("Mis solicitudes") }
}
```

Imports: `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Refresh`, `androidx.compose.material3.Icon` y `androidx.compose.material3.IconButton`.

**Agitar el emulador.** El emulador no se puede agitar con la mano. En la terminal de Android Studio (**PowerShell**), esta línea pone durante un momento una aceleración de unas tres gravedades y la devuelve a la normal:

```
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"; & $adb emu sensor set acceleration 30:0:9.8; Start-Sleep -Milliseconds 300; & $adb emu sensor set acceleration 0:9.8:0
```

En un móvil real, basta con agitarlo.

**Comprueba que…**

- al agitar (con la línea de la terminal o con el móvil real) se ve un momento la barra de progreso y el aviso vuelve a decir «Lista actualizada: 10 animales»;
- el botón de recargar (la flecha circular, arriba) hace lo mismo;
- con un lector de pantalla, o dejando el dedo sobre el botón, se anuncia «Recargar la lista» (`contentDescription`);
- sabes explicar por qué el sensor se quita en `onDispose`.

**Si te falla…**

- *No pasa nada al agitar*: falta `AlAgitar(onSacudida = onRecargar)` en `PantallaLista`, o `onRecargar` no está pasado en `AcogidaApp.kt` (paso 11.6).
- *La terminal dice que no encuentra `adb.exe`*: el SDK está en otra carpeta. En Android Studio, *File > Settings > Languages & Frameworks > Android SDK* muestra su ruta (*Android SDK Location*); `adb.exe` está en su carpeta `platform-tools`.

---

## Paso 13 · La distancia al centro

**Objetivo.** Un botón «¿A qué distancia está el centro?» que pida permiso de ubicación y muestre los kilómetros que hay hasta el centro.

**Mini-explicación.** La ubicación es un permiso **peligroso**: se declara en el manifiesto y se pide mientras se usa la app, como el de la cámara. Se piden a la vez la **precisa** y la **aproximada**, y la persona elige cuál da: la app tiene que funcionar con las dos, y también si se deniega. El centro está en la plaza del Obradoiro, en Santiago de Compostela. [Apuntes de la UD3, apartado 4](../apuntes/UD3-conectividad-sensores-y-segundo-plano.md#4-la-ubicación-y-los-mapas).

**Pista.**

**13.1 · Los permisos.** En `AndroidManifest.xml`, debajo del de `INTERNET`:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

**13.2 · La ubicación (ya hecho).** Crea el archivo `Ubicacion.kt`:

```kotlin
package com.example.centroacogida

// Ubicacion.kt: dónde está el dispositivo y a qué distancia del centro
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// Dónde está el centro de acogida (la plaza del Obradoiro, en Santiago de Compostela)
const val CENTRO_LATITUD = 42.8806
const val CENTRO_LONGITUD = -8.5446

// Pide la ubicación actual. Devuelve null si no se puede (localización desactivada).
// Antes de llamarla hay que tener el permiso de ubicación: por eso @SuppressLint
@SuppressLint("MissingPermission")
suspend fun obtenerUbicacion(context: Context): Location? = suspendCancellableCoroutine { continuacion ->
    val gestor = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    // GPS si está activado; si no, la red (antenas y Wi-Fi)
    val proveedor = when {
        gestor.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
        gestor.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> null
    }
    if (proveedor == null) {
        continuacion.resume(null)
    } else {
        val cancelar = CancellationSignal()
        continuacion.invokeOnCancellation { cancelar.cancel() }
        LocationManagerCompat.getCurrentLocation(
            gestor, proveedor, cancelar, ContextCompat.getMainExecutor(context)
        ) { ubicacion -> continuacion.resume(ubicacion) }
    }
}

// Distancia en kilómetros desde una ubicación hasta el centro
fun distanciaAlCentroKm(ubicacion: Location): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        ubicacion.latitude, ubicacion.longitude, CENTRO_LATITUD, CENTRO_LONGITUD, resultado
    )
    return resultado[0] / 1000f
}
```

**13.3 · El botón y el texto.** En `PantallaLista.kt`, una función nueva, fuera de `PantallaLista` (por ejemplo, justo antes de la vista previa `@Preview`). Pide el permiso como en «Un permiso y la cámara» de la 2.ª parte, pero con `RequestMultiplePermissions`, que pide varios a la vez; y busca la ubicación en una corrutina, porque tarda:

```kotlin
// Pide el permiso de ubicación y muestra a qué distancia está el centro
@Composable
fun DistanciaAlCentro() {
    val context = LocalContext.current
    val alcance = rememberCoroutineScope()
    var texto by rememberSaveable { mutableStateOf<String?>(null) }

    fun calcular() {
        alcance.launch {
            texto = "Buscando tu ubicación…"
            val ubicacion = obtenerUbicacion(context)
            texto = if (ubicacion == null) {
                "No se ha podido saber dónde estás: activa la ubicación del dispositivo."
            } else {
                "Estás a %.1f km del centro.".format(distanciaAlCentroKm(ubicacion))
            }
        }
    }

    // Pide a la vez la ubicación precisa y la aproximada: la persona elige cuál da
    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { respuesta ->
        if (respuesta.values.any { it }) calcular()
        else texto = "Sin permiso de ubicación no se puede calcular la distancia."
    }

    OutlinedButton(
        onClick = {
            val tengoPermiso = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (tengoPermiso) calcular()
            else pedirPermiso.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("¿A qué distancia está el centro?")
    }
    texto?.let { Text(it) }
}
```

Y, dentro de la `Column` de `PantallaLista`, justo después del aviso del paso 11.6:

```kotlin
DistanciaAlCentro()
```

Imports: `android.Manifest`, `android.content.pm.PackageManager`, `androidx.core.content.ContextCompat` y `kotlinx.coroutines.launch`; los demás, con Alt + Intro.

**Poner la ubicación del emulador.** En la barra del emulador, los tres puntos (**⋮**, *Extended controls*) → **Location**. Busca un sitio en el mapa (por ejemplo, «Santiago de Compostela») y pulsa **Set location**.

**Comprueba que…**

| Ubicación del emulador | Respuesta al permiso | Sale |
|---|---|---|
| Plaza del Obradoiro, Santiago | Precisa, *While using the app* | «Estás a 0.0 km del centro.», o pocas décimas (con el emulador en español, con coma: 0,0) |
| Puerta del Sol, Madrid | Cualquiera | Unos 486 km |
| — | *Don't allow* | «Sin permiso de ubicación no se puede calcular la distancia.» |

Para volver a ver el diálogo del permiso después de denegarlo: mantén pulsado el icono de la app → *App info* → *Permissions* → *Location* → *Ask every time*.

Y que sabes explicar la diferencia entre la ubicación aproximada y la precisa, y por qué la app tiene que funcionar si se deniega el permiso.

**Si te falla…**

- *El diálogo del permiso no aparece y sale «Sin permiso…»*: falta declarar los permisos en el manifiesto (13.1).
- *Sale una distancia de miles de kilómetros*: el emulador sigue en su ubicación por defecto, en California. Cámbiala (*Extended controls* → *Location* → *Set location*) y vuelve a pulsar el botón.
- *Se queda en «Buscando tu ubicación…»*: la ubicación del emulador está desactivada. Desliza desde arriba y activa **Location**.
- *`Unresolved reference: launch`*: falta `import kotlinx.coroutines.launch`.

---

## Paso 14 · Multimedia: la foto, un sonido y una animación

**Objetivo.** Que la foto del animal se recorte en un cuadrado, se guarde como JPEG y siga ahí al volver; que suene un sonido al enviar una solicitud; y que el aviso «Adopción urgente» de las tarjetas parpadee suavemente.

**Mini-explicación.** **Procesar** es cambiar el contenido de la imagen (recortarla, escalarla); **convertir** es cambiar su formato (de `Bitmap` en memoria a un archivo JPEG). Los sonidos cortos van en `res/raw` y se reproducen con `MediaPlayer`, que hay que **liberar** al terminar. Y una animación tiene que ayudar: aquí, que el aviso de urgencia se note más. [Apuntes de la UD4, apartados 2, 3 y 5](../apuntes/UD4-multimedia.md).

**Pista.**

**14.1 · El código multimedia (ya hecho) y el sonido.** Crea el archivo `Multimedia.kt`:

```kotlin
package com.example.centroacogida

// Multimedia.kt: procesar y guardar la foto, y reproducir un sonido
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.annotation.RawRes
import java.io.File

// Lado en píxeles de la foto guardada
private const val LADO = 300

// Calidad del JPEG, de 0 a 100: menos calidad, archivo más pequeño
private const val CALIDAD = 80

// El archivo de la foto de un animal, en la carpeta privada de la app
fun archivoFoto(context: Context, animalId: Int): File =
    File(context.filesDir, "foto_$animalId.jpg")

// PROCESAR: recorta el centro de la foto en un cuadrado y lo escala a LADO x LADO
fun recortarCuadrado(foto: Bitmap): Bitmap {
    val lado = minOf(foto.width, foto.height)
    val x = (foto.width - lado) / 2
    val y = (foto.height - lado) / 2
    val cuadrada = Bitmap.createBitmap(foto, x, y, lado, lado)
    return Bitmap.createScaledBitmap(cuadrada, LADO, LADO, true)
}

// CONVERTIR: guarda la foto como JPEG y devuelve el archivo
fun guardarComoJpeg(context: Context, foto: Bitmap, animalId: Int): File {
    val archivo = archivoFoto(context, animalId)
    archivo.outputStream().use { salida ->
        foto.compress(Bitmap.CompressFormat.JPEG, CALIDAD, salida)
    }
    return archivo
}

// Lee la foto guardada de un animal; null si todavía no tiene
fun cargarFoto(context: Context, animalId: Int): Bitmap? {
    val archivo = archivoFoto(context, animalId)
    return if (archivo.exists()) BitmapFactory.decodeFile(archivo.path) else null
}

// Reproduce un sonido de res/raw y libera el reproductor al terminar
fun reproducirSonido(context: Context, @RawRes sonido: Int) {
    val reproductor = MediaPlayer.create(context, sonido) ?: return
    reproductor.setOnCompletionListener { it.release() }
    reproductor.start()
}
```

El sonido: en la terminal de Android Studio (**PowerShell**), desde la carpeta del proyecto (la que contiene `settings.gradle.kts`, que es donde se abre la terminal):

```
New-Item -ItemType Directory -Force app\src\main\res\raw
Invoke-WebRequest https://github.com/mrpiay/NTFOR_DAM2627_PMDM/raw/main/2026_11_16-20/actividades/adoptado.wav -OutFile app\src\main\res\raw\adoptado.wav
```

En el panel *Project* (vista *Android*) aparece `res/raw/adoptado.wav`. Desde el código se usa como `R.raw.adoptado`.

**14.2 · La foto, procesada y guardada.** En `PantallaDetalle.kt`, dentro de `FotoDelAnimal`, cambia la línea de `foto` para que, al abrir el detalle, cargue la foto guardada:

```kotlin
// Al abrir el detalle, la foto guardada del animal, si tiene
var foto by remember { mutableStateOf(cargarFoto(context, animal.id)) }
```

Y cambia el lanzador `hacerFoto` para que recorte, guarde y avise:

```kotlin
val hacerFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
    if (bitmap != null) {
        val cuadrada = recortarCuadrado(bitmap)                     // procesar
        val archivo = guardarComoJpeg(context, cuadrada, animal.id) // convertir y guardar
        foto = cuadrada
        aviso = "Foto guardada: ${archivo.name}, ${archivo.length() / 1024} KB"
    }
}
```

Como `aviso` ya no es solo para errores, quita el color rojo de su `Text`: `aviso?.let { Text(it) }`. Y, para que se vea cuadrada, en el `Image` de la foto cambia el `modifier` por `Modifier.size(200.dp).clip(MaterialTheme.shapes.medium)`. Imports: `androidx.compose.runtime.remember` y `androidx.compose.foundation.layout.size`.

**14.3 · El sonido al enviar.** En `PantallaSolicitud.kt`, al principio del cuerpo de `PantallaSolicitud`, después de las líneas de los errores (`val errorCondiciones = …`):

```kotlin
// Cuando la solicitud pasa a enviada, suena una vez
val context = LocalContext.current
LaunchedEffect(form.enviado) {
    if (form.enviado) reproducirSonido(context, R.raw.adoptado)
}
```

`LaunchedEffect(form.enviado)` ejecuta su bloque cuando cambia `form.enviado`: el sonido suena una vez, al enviar, y no cada vez que la pantalla se vuelve a dibujar. Imports: `androidx.compose.runtime.LaunchedEffect` y `androidx.compose.ui.platform.LocalContext`.

**14.4 · La animación.** En `TarjetaAnimal.kt`, cambia el bloque `if (animal.urgente) { … }` por este:

```kotlin
if (animal.urgente) {
    // Animación sin fin: la transparencia va de 1 a 0,3 y vuelve, cada 800 ms
    val latido = rememberInfiniteTransition(label = "urgente")
    val transparencia by latido.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "transparencia"
    )
    Text(
        "Adopción urgente",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.alpha(transparencia)
    )
}
```

Imports (con Alt + Intro): los de `androidx.compose.animation.core` (`rememberInfiniteTransition`, `animateFloat`, `infiniteRepeatable`, `tween`, `RepeatMode`), `androidx.compose.runtime.getValue` y `androidx.compose.ui.draw.alpha`.

**Comprueba que…**

- al hacer la foto de un animal sale «**Foto guardada: foto_1.jpg, 4 KB**» (el número y el tamaño dependen del animal y de la foto) y la foto se ve cuadrada;
- al volver a la lista y entrar otra vez en ese animal, **y también después de cerrar la app**, la foto sigue ahí;
- el archivo está en el dispositivo: *View > Tool Windows > Device Explorer* → `data/data/com.example.centroacogida/files/foto_1.jpg`;
- al enviar una solicitud **suena** el sonido una vez (sube el volumen del emulador con los botones de su barra);
- en la lista, el texto «Adopción urgente» se desvanece y vuelve sin parar, y el resto de la tarjeta no se mueve;
- sabes explicar qué parte del código **procesa** la foto y cuál la **convierte**, y por qué se llama a `release()`.

**Si te falla…**

- *`Unresolved reference: adoptado`*: el archivo no está en `app/src/main/res/raw/` o tiene otro nombre. Los nombres de los recursos van en minúsculas, sin tildes ni espacios.
- *La foto no sigue al volver*: la línea de `foto` sigue siendo `rememberSaveable { mutableStateOf<Bitmap?>(null) }`; tiene que cargarla con `cargarFoto`.
- *No suena*: el volumen del emulador está bajado, o falta el `LaunchedEffect`.
- *Suena cada vez que escribes en el formulario*: el `reproducirSonido` está fuera del `LaunchedEffect`.

---

## Paso 15 · Depurar, generar el APK y documentar

**Objetivo.** Ver los mensajes de la app en Logcat, generar el archivo instalable (APK) y escribir una documentación breve con las pruebas hechas.

**Mini-explicación.** **Logcat** muestra lo que escriben `Log.d` y `Log.e` (paso 11), y también las excepciones con la línea donde ocurren. El **APK** es el archivo que se instala en un móvil; la versión *debug* es para probar. La documentación es para quien **usa** la app y para quien la **mantiene**. [Apuntes de la UD4, apartado 6](../apuntes/UD4-multimedia.md#6-depurar-empaquetar-y-documentar).

**Pista.**

**15.1 · Logcat.** Con la app en marcha, abre **Logcat** (*View > Tool Windows > Logcat*). En el campo de filtro, escribe:

```
tag~:^CentroAcogida$
```

`tag~:` busca con una expresión regular: `^` es el principio de la etiqueta y `$` el final, así que solo salen los mensajes cuya etiqueta es exactamente `CentroAcogida`. Con `tag:CentroAcogida` también saldrían los que el sistema escribe sobre la app, como los de la memoria («GC freed…»), porque su etiqueta contiene el nombre de la app.

Recarga la lista (agitando o con el botón) y mira las líneas `D` («Animales cargados de Internet: 10»). Activa el modo avión, recarga y mira la línea `E` y la excepción que la acompaña. Desactiva el modo avión.

**15.2 · El APK.** *Build > Generate App Bundles or APKs > Generate APKs*. Al terminar, abajo a la derecha sale un aviso con el enlace **locate**: abre la carpeta `app\build\outputs\apk\debug\`, con `app-debug.apk`.

Instálalo **en el emulador**, sin Android Studio, como se instalaría en un móvil:

1. En el emulador, mantén pulsado el icono de «Centro de acogida» → *App info* → **Uninstall** (o arrástralo a *Uninstall*). Así se comprueba que lo que se instala es el APK y no lo que dejó Android Studio.
2. Arrastra `app-debug.apk` desde la carpeta hasta la ventana del emulador. Sale un aviso de que se está instalando.
3. Desliza hacia arriba en la pantalla de inicio del emulador para ver todas las apps y abre «Centro de acogida» desde ahí. No pulses **Run** en Android Studio: volvería a instalar la app y ya no estarías probando el APK.

**Si además tienes un móvil Android**, instálalo también en él:

1. Copia `app-debug.apk` al móvil: con el cable USB (en el móvil, elige *Transferencia de archivos*) a la carpeta `Download`, o enviándotelo por correo o a tu nube.
2. En el móvil, abre la app **Archivos** (o el correo) y toca `app-debug.apk`.
3. Si avisa de que no se permite instalar apps de esa fuente, toca **Ajustes**, activa **Permitir de esta fuente** y vuelve atrás.
4. Toca **Instalar**. Si Play Protect avisa de que la app es desconocida, toca **Instalar de todas formas**: es tu app, sin firmar para publicar.
5. Ábrela. En un móvil real, agitar recarga la lista y la distancia sale con tu ubicación de verdad.

**15.3 · La documentación.** En la raíz del proyecto (la carpeta que contiene `settings.gradle.kts`), crea el archivo `DOCUMENTACION.md` (en el panel *Project*, cambia la vista de *Android* a *Project* arriba a la izquierda; clic derecho sobre `CentroAcogida` → *New > File*) con esta estructura, completada con lo tuyo:

```markdown
# Centro de acogida

## Qué hace

(Dos o tres líneas: para qué sirve la app y para quién es.)

## Cómo se usa

(Una línea por tarea: ver los animales, buscar, filtrar los urgentes,
recargar la lista, ver la distancia al centro, hacer la foto de un animal,
solicitar una adopción, ver mis solicitudes.)

## Cómo se compila e instala

(Qué hace falta para abrirla en Android Studio y cómo se genera el APK.)

## Permisos que pide y para qué

| Permiso | Para qué | Si se deniega |
|---|---|---|
| Internet | | |
| Cámara | | |
| Ubicación | | |

## Pruebas

| Prueba | Cómo | Resultado esperado | Resultado |
|---|---|---|---|
| Carga desde Internet | Abrir la app con conexión | | |
| Sin conexión | Modo avión y recargar | | |
| Agitar | Línea de la terminal o móvil real | | |
| Distancia | Ubicación en Santiago y en Madrid | | |
| Permiso denegado | *Don't allow* en la ubicación | | |
| Foto | Hacer la foto, cerrar la app y volver | | |
| Sonido | Enviar una solicitud | | |
| Giro de pantalla | Girar con la lista filtrada y con la foto | | |
```

**Comprueba que…**

- en Logcat, con el filtro `tag~:^CentroAcogida$`, sale una línea `D` por cada recarga, y una `E` con la excepción cuando no hay conexión;
- existe `app-debug.apk` en `app\build\outputs\apk\debug\` y sabes decir qué es y en qué se diferencia de una versión *release*;
- después de desinstalar la app, el APK arrastrado al emulador la instala y se abre desde la lista de apps, con los 10 animales;
- `DOCUMENTACION.md` tiene las cinco secciones completas y la tabla de pruebas con las ocho filas hechas y su resultado.

**Si te falla…**

- *Logcat no muestra nada*: arriba de Logcat, el desplegable del dispositivo tiene que ser tu emulador, y el de la app, `com.example.centroacogida`. Borra el filtro y vuelve a escribirlo.
- *No aparece el menú Generate…*: en algunas versiones de Android Studio está en *Build > Build App Bundle(s) / APK(s) > Build APK(s)*.
- *Al arrastrar el APK no pasa nada o sale «Install failed»*: espera a que el emulador esté en la pantalla de inicio, comprueba que desinstalaste la app antes y vuelve a arrastrarlo.
- *El botón Run (▶) no hace nada y a su izquierda ya no pone «app»*: se ha borrado la configuración de ejecución (pasa al elegir **Delete** en el menú de los tres puntos de «app», junto a ▶). Para crearla otra vez: *Run > Edit Configurations…* → **+** → **Android App**; en *Name* escribe `app`, en *Module* elige `CentroAcogida.app` y pulsa **OK**. Al lado de ▶ vuelve a salir **app**.
- *En el móvil sale «App no instalada»*: hay otra versión de la app instalada desde otro ordenador; desinstálala del móvil y vuelve a tocar el APK.

---

## Entrega

Qué se entrega esta semana y cuándo: [`entrega.md`](entrega.md).

## Cómo comprobarlo (lista final)

| Comprobación | Hecho |
|---|---|
| El proyecto compila y se ejecuta en un AVD (o en un móvil real) |  |
| La lista se carga de Internet (10 animales, con Bruno y Lola) y, sin conexión, avisa y sigue con los suyos |  |
| Agitar y el botón de recargar vuelven a pedir la lista |  |
| La distancia al centro sale con la ubicación del emulador, y sin permiso la app avisa |  |
| La foto se recorta, se guarda como JPEG y sigue al cerrar la app |  |
| Suena un sonido al enviar una solicitud y el aviso de urgencia tiene su animación |  |
| Logcat muestra los mensajes de la app, el APK está generado y `DOCUMENTACION.md` está completo |  |
