# PMDM · Proyecto guiado «Centro de acogida 2.0» · 2.ª parte

## Punto de partida

Es la continuación de la 1.ª parte (semana del 2 al 6 de noviembre, pasos 0 a 7): la app del centro de acogida con el estado en un ViewModel, la navegación lista → detalle y el formulario «Solicitar adopción» con validación. En esta 2.ª parte la lista se adapta a **dos tamaños de pantalla** (UD2), las solicitudes se guardan en una **base de datos con Room** (UD3) y la app pide un **permiso** y usa la **cámara** (inicio de la UD4). El resto de la UD3 se trabaja dentro del proyecto integrado de la UD4, según la programación.

**Usa la IA como guía, no para que lo haga por ti.** Pídele los **pasos necesarios** para implementar cada cosa y que te explique lo que no entiendas; **el código lo pones tú en tu proyecto**, aunque sea copiando y pegando, sabiendo **dónde** lo pones y **para qué**: es la única forma de familiarizarte con tu entorno. El objetivo es que lo que hagas **funcione y cumpla los requisitos** de cada paso («Comprueba que…») y que sepas **dónde está cada cosa y qué habría que tocar para cambiar o añadir algo**: es lo que te van a pedir después.

**El objetivo es entender cada paso:** es lo que necesitas para responder los cuestionarios de autoevaluación y preparar la prueba escrita.

**Evaluación continua.** Algunas semanas hay una **prueba escrita, en papel,** en las dos últimas sesiones del último día de clase, después de que se publiquen las soluciones de lo que entra. La primera es el **viernes 20 de noviembre**, sobre **las dos partes del proyecto** (pasos 0 a 10): Android Studio, Kotlin, la arquitectura típica de una app (pantallas, ViewModel, estado y navegación), la adaptación a varios tamaños de pantalla, Room y el propio proyecto.

Del 10 al 13 de noviembre. Cada paso trae una pista y qué hacer si algo falla.

## Con qué trabajas

- **Si terminaste la 1.ª parte y funciona**: sigue con tu proyecto, en la misma carpeta.
- **Si no la terminaste o no te funciona**: cambia el nombre de tu carpeta a `CentroAcogida-anterior`. Descarga la plantilla, [`plantilla.zip`](https://github.com/mrpiay/NTFOR_DAM2627_PMDM/raw/main/2026_11_09-13/actividades/plantilla.zip), haz doble clic en el zip, arrastra la carpeta de dentro, `CentroAcogida`, a una carpeta sin tildes ni espacios, por ejemplo `C:\dam2627\pmdm`, y ábrela en Android Studio: en la pantalla de bienvenida, **Open** (si ya hay un proyecto abierto, *File > Open*), y elige la carpeta `CentroAcogida`, la que contiene `settings.gradle.kts`. Es la 1.ª parte terminada; los comentarios `TODO Paso 8`, `TODO Paso 9…` y `TODO Paso 10` (con `//` en Kotlin y `#` en `libs.versions.toml`) marcan dónde se añade código.

## Objetivo

Añadir a la app:

1. una lista que se adapta al ancho de la pantalla, probada en **dos tamaños** (móvil y tableta);
2. las solicitudes guardadas en una **base de datos con Room**, que siguen ahí aunque se cierre la app;
3. un **buscador** que filtra la lista por nombre.

## Qué se trabaja

| Unidad | Qué | Dónde |
|---|---|---|
| UD1 · Tecnologías y entornos | AVD de distintos tamaños | Paso 8 |
| UD2 · Compose y adaptación a tamaños | `LazyVerticalGrid`, `GridCells.Adaptive`, dp, vistas previas | Paso 8 |
| UD2 · Estado y flujo unidireccional | El texto de búsqueda en el estado, el ViewModel y un `OutlinedTextField` | Paso 10 |
| UD3 · Persistencia y servicios del dispositivo | Room (entidad, DAO, base de datos) y corrutinas (paso 9); permisos en tiempo de ejecución («Un permiso y la cámara») | Paso 9 y «Un permiso y la cámara» |
| UD4 · Multimedia (inicio) | Capturar una imagen con la cámara | «Un permiso y la cámara» |

## Plan de la semana

Cada sesión dura 50 minutos.

| Día y hora | Qué se hace |
|---|---|
| Mar 10 nov, 08:30 | Al empezar: añadir las dependencias de **9.1** y pulsar *Sync Now*. Mientras descarga, **paso 8** (dos tamaños de pantalla). |
| Mar 10 nov, 09:20 | **Paso 9** (Room: 9.2 a 9.4). |
| Mié 11 nov, 08:30 | **Paso 9** (9.5, «Mis solicitudes») y **Un permiso y la cámara** (15 min). |
| Mié 11 nov, 09:20 y 10:10 | Sesiones de la UD4, fuera de este proyecto (ver `programacion/`, en la raíz del repositorio). |
| Vie 13 nov, 13:00 | **Paso 10** (buscar por nombre). |
| Vie 13 nov, 13:50 | Comprobar todo con la lista final («Cómo comprobarlo»). |

**Los pasos se hacen en orden**, cada uno en la sesión que le toca según el plan de la semana: cada paso usa lo del anterior.

**Descargas.** Con toda la clase en la misma red, las descargas de Gradle tardan. Haz el *Sync* de 9.1 al principio de la sesión del martes, no a mitad.

## Cómo está escrito cada paso

**Objetivo** (qué consigues), **Pista** (código o explicación corta), **Comprueba que…** (cómo saber que está bien) y **Si te falla…** (errores típicos y cómo resolverlos).

El código usa el paquete `com.example.centroacogida`. Cuando Android Studio marque en rojo un nombre, pon el cursor encima y pulsa **Alt + Intro** para añadir el `import`.

---

## Paso 8 · Dos tamaños de pantalla

**Objetivo.** Que la lista aproveche el ancho de una tableta y probarlo en el móvil y en una tableta. El emulador de tableta se crea como explica [`software-PMDM.md`](software-PMDM.md).

Antes de empezar, haz **9.1** (dependencias y *Sync Now*); mientras descarga, sigue aquí.

**Pista.** En `PantallaLista.kt`, cambia la `LazyColumn` (donde está el comentario `// TODO Paso 8`) por una cuadrícula adaptativa:

```kotlin
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 320.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(uiState.animalesVisibles, key = { it.id }) { animal ->
        TarjetaAnimal(animal = animal, onClick = { onAnimalClick(animal) })
    }
}
```

Borra los imports `androidx.compose.foundation.lazy.LazyColumn` y `androidx.compose.foundation.lazy.items` y añade `androidx.compose.foundation.lazy.grid.LazyVerticalGrid`, `androidx.compose.foundation.lazy.grid.GridCells` y `androidx.compose.foundation.lazy.grid.items`.

`GridCells.Adaptive(320.dp)` mete tantas columnas como quepan con un mínimo de 320 dp: en el móvil sale una y en la tableta dos o tres, sin ningún `if`. Los **dp** miden lo mismo en pantallas con distinta resolución. Para que en la tableta no queden líneas larguísimas, en `PantallaDetalle.kt` y en `PantallaSolicitud.kt` añade `.widthIn(max = 600.dp)` al final del `Modifier` de la `Column` principal, después de `.verticalScroll(rememberScrollState())` (import `androidx.compose.foundation.layout.widthIn`).

**Comprueba que…**

- en el **móvil** en vertical hay una columna;
- en la **tableta** (o el móvil en horizontal) hay varias;
- en la tableta, el detalle y el formulario no ocupan todo el ancho;
- todo se ve y se puede usar en los dos (nada cortado, botones accesibles).

Haz una captura en cada dispositivo (botón de cámara del emulador) y compáralas.

**Y una vista previa de tableta.** Al final de `PantallaLista.kt`, añade una vista previa con el ancho de una tableta: así ves la cuadrícula con varias columnas sin ejecutar la app.

```kotlin
@Preview(name = "Tableta", widthDp = 840, heightDp = 1200, showBackground = true)
@Composable
fun ListaTabletaPreview() {
    CentroAcogidaTheme {
        PantallaLista(uiState = AcogidaUiState(), onCambiarUrgentes = {}, onAnimalClick = {})
    }
}
```

Imports: `androidx.compose.ui.tooling.preview.Preview` y `com.example.centroacogida.ui.theme.CentroAcogidaTheme`. Ábrela en modo *Split* para verla.

---

## Paso 9 · Guardar las solicitudes con Room (UD3)

**Objetivo.** Que las solicitudes de adopción del formulario se guarden en una base de datos del móvil y se vean en una pantalla nueva, «Mis solicitudes». Tienen que seguir ahí aunque se cierre la app.

**Mini-explicación.** Room guarda los datos en SQLite con tres piezas, la entidad, el DAO y la base de datos, y genera el resto del código al compilar, con KSP. Como acceder a la base de datos puede tardar, se hace en una corrutina. Todo esto está en [UD3, apartados 2 y 3](../apuntes/UD3-persistencia-y-servicios.md#2-room).

### 9.1 · Dependencias (al empezar el martes)

Añádelas al empezar la primera sesión y pulsa *Sync Now*: mientras descarga, haz el paso 8. Son las versiones probadas con esta plantilla.

En `gradle/libs.versions.toml` hay tres comentarios `# TODO Paso 9.1`, uno en cada sección. Cambia cada comentario por las líneas de su sección. **No copies los encabezados** `[versions]`, `[libraries]` y `[plugins]`: ya existen.

```toml
[versions]
room = "2.8.4"
ksp = "2.3.4"

[libraries]
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

En los dos `build.gradle.kts`, la línea ya está escrita en un comentario `// TODO Paso 9.1:`: borra ese comienzo para activarla. En el del proyecto (el de la raíz), dentro de `plugins { }`: `alias(libs.plugins.ksp) apply false`. En la raíz se declara y en `app` se aplica ([UD3, apartado 2](../apuntes/UD3-persistencia-y-servicios.md#2-room)).

En `app/build.gradle.kts`, dentro de `plugins { }`: `alias(libs.plugins.ksp)`. Y dentro de `dependencies { }`, en lugar del comentario `// TODO Paso 9.1: Room …`:

```kotlin
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

**Sync Now.**

**Si el *Sync* falla por KSP** (por ejemplo, *Plugin [id: 'com.google.devtools.ksp'] was not found*): comprueba las tres piezas de arriba (el plugin en `[plugins]` del catálogo y en los dos `build.gradle.kts`) y vuelve a pulsar *Sync Now*. No sigas con 9.2 hasta que el *Sync* termine bien.

### 9.2 · La entidad y el DAO

Para crear cada archivo nuevo: clic derecho en el paquete `com.example.centroacogida` → *New → Kotlin Class/File* → *File*, con el nombre indicado; pega el código sustituyendo todo.

Crea `Solicitud.kt`:

```kotlin
package com.example.centroacogida

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // el id lo pone la base de datos
    val animalId: Int,
    val animalNombre: String,
    val nombre: String,
    val email: String,
    val telefono: String,
    val fecha: Long = System.currentTimeMillis()
)
```

Y `SolicitudDao.kt`:

```kotlin
package com.example.centroacogida

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {
    @Insert
    suspend fun insertar(solicitud: Solicitud)

    // Un Flow: la pantalla se actualiza sola cada vez que cambia la tabla
    @Query("SELECT * FROM solicitudes ORDER BY fecha DESC")
    fun todas(): Flow<List<Solicitud>>
}
```

### 9.3 · La base de datos

Crea `AcogidaDatabase.kt`. Hay una sola base de datos para toda la app:

```kotlin
package com.example.centroacogida

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Solicitud::class], version = 1, exportSchema = false)
abstract class AcogidaDatabase : RoomDatabase() {
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        @Volatile private var instancia: AcogidaDatabase? = null

        fun obtener(context: Context): AcogidaDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(context, AcogidaDatabase::class.java, "acogida.db")
                    .build()
                    .also { instancia = it }
            }
    }
}
```

### 9.4 · El ViewModel guarda la solicitud

Para abrir la base de datos, el ViewModel necesita el contexto de la aplicación. Por eso pasa de `ViewModel()` a **`AndroidViewModel(application)`**: `viewModel()` lo crea igual, sin cambiar nada más.

En `AcogidaViewModel.kt`, cambia la cabecera de la clase y añade, justo debajo, el DAO y la lista de solicitudes. No borres nada de lo que ya hay:

```kotlin
class AcogidaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AcogidaDatabase.obtener(application).solicitudDao()

    // Las solicitudes guardadas, como estado para la interfaz
    val solicitudes: StateFlow<List<Solicitud>> = dao.todas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ... lo que ya tenías (uiState, formulario, etc.)
```

`stateIn` deja las solicitudes como un `StateFlow` que la pantalla lee con `collectAsState()`, igual que `uiState`. Qué hacen `WhileSubscribed(5_000)` y `emptyList()`: [UD3, apartado 2](../apuntes/UD3-persistencia-y-servicios.md#2-room).

Desde aquí, `AcogidaViewModel` necesita la aplicación: si en algún sitio se crea a mano con `AcogidaViewModel()` (por ejemplo, para probar algo en `MainActivity`), ahora hay que escribir `AcogidaViewModel(application)`.

Y **sustituye la función `enviarSolicitud()` entera** (son 3 líneas) por esta, que recibe el animal y guarda la solicitud si es válida:

```kotlin
    fun enviarSolicitud(animal: Animal?) {
        val f = _formulario.value
        _formulario.update { it.copy(intentadoEnviar = true, enviado = it.esValido) }
        if (f.esValido && animal != null) {
            viewModelScope.launch {   // en segundo plano
                dao.insertar(
                    Solicitud(
                        animalId = animal.id, animalNombre = animal.nombre,
                        nombre = f.nombre, email = f.email, telefono = f.telefono
                    )
                )
            }
        }
    }
```

Imports nuevos: `android.app.Application`, `androidx.lifecycle.AndroidViewModel`, `androidx.lifecycle.viewModelScope`, `kotlinx.coroutines.flow.SharingStarted`, `kotlinx.coroutines.flow.stateIn` y `kotlinx.coroutines.launch`. Quita el de `androidx.lifecycle.ViewModel`.

Ahora `AcogidaApp.kt` no compila, porque `enviarSolicitud` necesita el animal. Arréglalo en `composable(route = "solicitud/{animalId}")`: justo debajo de `val id = …`, añade `val animal = viewModel.buscarAnimal(id)`; en `PantallaSolicitud(…)` pon `animal = animal` y cambia `onEnviar = viewModel::enviarSolicitud` por `onEnviar = { viewModel.enviarSolicitud(animal) }`. Así el proyecto vuelve a compilar.

### 9.5 · La pantalla «Mis solicitudes» y su ruta

Crea `PantallaSolicitudes.kt`. Es Compose que ya conoces: una `LazyColumn` de tarjetas.

```kotlin
package com.example.centroacogida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSolicitudes(solicitudes: List<Solicitud>, onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis solicitudes") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (solicitudes.isEmpty()) {
            Text("Todavía no has enviado ninguna solicitud.", modifier = Modifier.padding(innerPadding).padding(16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(solicitudes, key = { it.id }) { s ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(s.animalNombre, style = MaterialTheme.typography.titleMedium)
                            Text("${s.nombre} · ${s.email} · ${s.telefono}")
                            Text(DateFormat.getDateTimeInstance().format(Date(s.fecha)), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
```

En `PantallaLista.kt`, añade el parámetro `onVerSolicitudes: () -> Unit = {}` al final de los parámetros de `fun PantallaLista(`, después de `onAnimalClick: (Animal) -> Unit` y con una coma. Después, dentro de `topBar = { }`, sustituye `TopAppBar(title = { Text("Centro de acogida") })` por esta barra con un botón (import `androidx.compose.material3.TextButton`):

```kotlin
TopAppBar(
    title = { Text("Centro de acogida") },   // o el título que pusiste en el paso 3
    actions = { TextButton(onClick = onVerSolicitudes) { Text("Mis solicitudes") } }
)
```

En `AcogidaApp.kt`:

- debajo de `val formulario by …`: `val solicitudes by viewModel.solicitudes.collectAsState()`;
- en `PantallaLista(…)`, después de `onAnimalClick = …` (pon una coma al final de esa línea): `onVerSolicitudes = { navController.navigate("solicitudes") }`;
- un destino nuevo en el `NavHost`, después del bloque `composable("lista") { … }`: `composable("solicitudes") { PantallaSolicitudes(solicitudes = solicitudes, onVolver = { navController.popBackStack() }) }`.

Cuando tengas los cuatro archivos nuevos, borra `PasosSiguientes.kt`: solo era la lista de lo que faltaba.

**Comprueba que…**

- envías una solicitud válida y aparece en «Mis solicitudes», con el animal, tus datos y la fecha;
- si cierras la app desde Recientes y la vuelves a abrir, la solicitud sigue ahí;
- en *View > Tool Windows > App Inspection*, pestaña *Database Inspector* (con la app abierta en el emulador) aparece la tabla `solicitudes`.

**Si te falla…**

- *Unresolved reference: ksp* o *Plugin not found*: falta algo de 9.1 (el plugin en los dos `build.gradle.kts`) o el *Sync*.
- *Cannot access database on the main thread*: estás llamando a `insertar` fuera de `viewModelScope.launch`.
- *Cannot create an instance of class AcogidaViewModel*: la cabecera tiene que recibir `application: Application` y heredar de `AndroidViewModel(application)`.
- No aparece nada en «Mis solicitudes»: comprueba que en la ruta de la solicitud pasas `animal` a `enviarSolicitud`; si es `null`, no se guarda.

**Para pensar**

1. ¿Por qué las solicitudes siguen ahí después de cerrar la app, y el interruptor «Adopciones urgentes» no?
2. ¿Por qué `insertar` es `suspend` y se llama dentro de `viewModelScope.launch`?
3. Las pantallas no saben que existe Room. ¿Quién habla con la base de datos? ¿Qué cambiaría si mañana los animales vinieran de un servicio web?

---

## Un permiso y la cámara (UD3 y UD4, miércoles, 15 min)

Lo que queda de la UD3 (permisos) y el inicio de la UD4 (multimedia), en la misma app. Se trabaja más a fondo en la UD4.

**Objetivo.** En el detalle de cada animal, un botón «Hacer una foto» **pide el permiso de cámara**. Si lo concedes, abre la cámara y muestra la foto; si no, explica por qué no puede.

**Pista.**

1. En `app/src/main/AndroidManifest.xml`, encima de `<application`, declara el permiso. Como `CAMERA` es un permiso peligroso, además se pide mientras se usa la app ([UD3, apartado 4](../apuntes/UD3-persistencia-y-servicios.md#4-permisos)):

   ```xml
   <uses-permission android:name="android.permission.CAMERA" />
   <uses-feature android:name="android.hardware.camera" android:required="false" />
   ```

2. Al final de `PantallaDetalle.kt`, fuera de `PantallaDetalle`, añade este composable. Usa dos lanzadores (`rememberLauncherForActivityResult`, [UD3, apartado 4](../apuntes/UD3-persistencia-y-servicios.md#4-permisos)): uno pide el permiso y el otro abre la cámara; los comentarios del código dicen qué hace cada uno.

   ```kotlin
   @Composable
   fun FotoDelAnimal(animal: Animal) {
       val context = LocalContext.current
       // rememberSaveable: la foto y el aviso no se pierden al girar la pantalla
       var foto by rememberSaveable { mutableStateOf<Bitmap?>(null) }
       var aviso by rememberSaveable { mutableStateOf<String?>(null) }

       // Abre la cámara del sistema y devuelve una foto pequeña (o null si se cancela)
       val hacerFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
           foto = bitmap
       }
       // Muestra el diálogo del sistema «¿Permitir que la app haga fotos?»
       val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
           if (concedido) hacerFoto.launch(null)
           else aviso = "Sin permiso de cámara no se puede hacer la foto."
       }

       OutlinedButton(
           onClick = {
               aviso = null
               val tengoPermiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                   PackageManager.PERMISSION_GRANTED
               if (tengoPermiso) hacerFoto.launch(null) else pedirPermiso.launch(Manifest.permission.CAMERA)
           },
           modifier = Modifier.fillMaxWidth()
       ) {
           Text("Hacer una foto de ${animal.nombre}")
       }
       aviso?.let { Text(it, color = MaterialTheme.colorScheme.error) }
       foto?.let {
           Image(
               bitmap = it.asImageBitmap(),
               contentDescription = "Foto de ${animal.nombre}",
               modifier = Modifier.fillMaxWidth().height(200.dp).clip(MaterialTheme.shapes.medium),
               contentScale = ContentScale.Crop
           )
       }
   }
   ```

   Los imports nuevos, junto a los demás del archivo:

   ```kotlin
   import android.Manifest
   import android.content.pm.PackageManager
   import android.graphics.Bitmap
   import androidx.activity.compose.rememberLauncherForActivityResult
   import androidx.activity.result.contract.ActivityResultContracts
   import androidx.compose.material3.OutlinedButton
   import androidx.compose.runtime.getValue
   import androidx.compose.runtime.mutableStateOf
   import androidx.compose.runtime.saveable.rememberSaveable
   import androidx.compose.runtime.setValue
   import androidx.compose.ui.graphics.asImageBitmap
   import androidx.compose.ui.platform.LocalContext
   import androidx.core.content.ContextCompat
   ```

3. En `PantallaDetalle`, justo después del `}` que cierra el botón «Solicitar adopción», llama al composable: `FotoDelAnimal(animal)`.

**Comprueba que…**

- la primera vez, el botón muestra el diálogo del permiso;
- si lo deniegas, sale el aviso en rojo;
- si lo concedes, se abre la cámara y, al aceptar la foto, aparece debajo del botón. El emulador tiene una cámara simulada: muestra una habitación virtual.

**Si te falla…**

- *La cámara no se abre en el emulador*: en *Device Manager*, edita el dispositivo (*Edit*) y, en *Advanced Settings*, elige *VirtualScene* o *Emulated* en *Back camera*.
- *Ya no sale el diálogo del permiso*: el sistema recuerda la respuesta. Para volver a probar, en el emulador: mantén pulsado el icono de la app → *Información de la app* → *Permisos* → *Cámara*.

**Explícalo tú:** ¿qué pasa si deniegas el permiso? ¿Y si lo concedes «solo esta vez»?

---

## Paso 10 · Buscar por nombre

**Objetivo.** Un campo de texto encima de la lista que la filtre por nombre mientras se escribe, junto con el interruptor «Adopciones urgentes».

**Pista.** Es el mismo camino que ya sigue el interruptor: el texto se guarda en el **estado** (`AcogidaUiState`), lo cambia el **ViewModel** y la pantalla solo lo muestra y avisa cuando cambia. Son cuatro cambios, uno en cada archivo.

1. En `AcogidaUiState.kt`, añade el texto de búsqueda al estado y úsalo para filtrar:

   ```kotlin
   data class AcogidaUiState(
       val animales: List<Animal> = DatosAnimales.animales,
       val soloUrgentes: Boolean = false,
       val textoBusqueda: String = ""
   ) {
       val animalesVisibles: List<Animal>
           get() = animales
               .filter { !soloUrgentes || it.urgente }
               .filter { it.nombre.contains(textoBusqueda.trim(), ignoreCase = true) }
   }
   ```

   Los dos `filter` se encadenan: primero, los urgentes (si el interruptor está activado); después, los que contienen el texto, sin distinguir mayúsculas. Con el texto vacío, `contains("")` es verdadero para todos: no se quita ninguno.

2. En `AcogidaViewModel.kt`, debajo de `cambiarSoloUrgentes`, una función igual para el texto:

   ```kotlin
   fun cambiarBusqueda(texto: String) {
       _uiState.update { it.copy(textoBusqueda = texto) }
   }
   ```

3. En `PantallaLista.kt`, añade un parámetro al final de `PantallaLista`, después de `onVerSolicitudes`:

   ```kotlin
   onBuscar: (String) -> Unit = {}
   ```

   Y, dentro de la `Column`, justo antes de la `Row` del interruptor, el campo de texto:

   ```kotlin
   OutlinedTextField(
       value = uiState.textoBusqueda,
       onValueChange = onBuscar,
       label = { Text("Buscar por nombre") },
       singleLine = true,
       modifier = Modifier.fillMaxWidth()
   )
   ```

   Imports: `androidx.compose.material3.OutlinedTextField` y `androidx.compose.foundation.layout.fillMaxWidth`.

4. En `AcogidaApp.kt`, en la llamada a `PantallaLista(…)`, pasa la función del ViewModel (pon una coma al final de la línea anterior, `onVerSolicitudes = …`):

   ```kotlin
   onBuscar = viewModel::cambiarBusqueda
   ```

El campo solo muestra `uiState.textoBusqueda`: al escribir, `onValueChange` avisa al ViewModel, que cambia el estado, y la lista se vuelve a dibujar. Es el **flujo unidireccional de datos**: el estado baja a la pantalla y los eventos suben al ViewModel.

**Comprueba que…**

| Escribes | Interruptor «Adopciones urgentes» | Sale |
|---|---|---|
| `na` | desactivado | Luna y Nala |
| `LUNA` | desactivado | Luna |
| `to` | desactivado | Toby |
| `to` | activado | ninguno (Toby no es urgente) |
| (nada) | activado | Luna, Nala y Kira |

Son los animales de la plantilla. Si en la 1.ª parte añadiste otro, también sale cuando su nombre coincide.

Y que, si giras la pantalla o vas al detalle de un animal y vuelves, el texto y la lista filtrada siguen igual: el estado está en el ViewModel, no en la pantalla.

**Si te falla…**

- *Al escribir no aparece nada en el campo*: falta `onBuscar = viewModel::cambiarBusqueda` en `AcogidaApp.kt`, o `cambiarBusqueda` no hace el `copy`. El campo solo muestra lo que hay en el estado.
- *`LUNA` no encuentra a Luna*: falta `ignoreCase = true`.
- *Con el interruptor activado salen animales que no son urgentes*: el primer `filter` no está; tienen que ir los dos.
- *`No value passed for parameter 'onBuscar'`* en la vista previa: falta el `= {}` del parámetro.

---

## Entrega

Qué se entrega esta semana y cuándo: [`entrega.md`](entrega.md).

## Cómo comprobarlo (lista final)

| Comprobación | Hecho |
|---|---|
| El proyecto compila y se ejecuta en un AVD (o en un móvil real) |  |
| La lista se ve en una columna en el móvil y en varias en la tableta |  |
| Las solicitudes se guardan con Room, salen en «Mis solicitudes» y siguen al cerrar la app |  |
| El buscador filtra por nombre, sin distinguir mayúsculas, junto con el interruptor de urgentes |  |
