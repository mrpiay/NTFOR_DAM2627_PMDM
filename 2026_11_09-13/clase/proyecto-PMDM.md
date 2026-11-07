# PMDM · Proyecto guiado «Centro de acogida 2.0» · 2.ª parte

## Punto de partida

Es la continuación de la 1.ª parte (semana del 2 al 6 de noviembre, pasos 0 a 7): la app del centro de acogida con el estado en un ViewModel, la navegación lista → detalle y el formulario «Solicitar adopción» con validación. En esta 2.ª parte la lista se adapta a **dos tamaños de pantalla** (UD2), las solicitudes se guardan en una **base de datos con Room** (UD3) y, con el docente, vemos un **permiso** y la **cámara** (inicio de la UD4). El resto de la UD3 lo trabajaremos dentro del proyecto integrado de la UD4 (del 17 de noviembre al 2 de diciembre).

**Usa la IA como guía, no para que lo haga por ti.** Pídele los **pasos necesarios** para implementar cada cosa y que te explique lo que no entiendas; **el código lo pones tú en tu proyecto**, aunque sea copiando y pegando, sabiendo **dónde** lo pones y **para qué**: es la única forma de familiarizarte con tu entorno. El objetivo es que lo que hagas **funcione y cumpla los requisitos** de cada paso («Comprueba que…») y que sepas **dónde está cada cosa y qué habría que tocar para cambiar o añadir algo**: es lo que te van a pedir después.

**Evaluación continua.** En la última clase de cada semana respondes en clase a una pregunta de la autoevaluación que hayas entregado. Esta vez es distinto: el **viernes 13 de noviembre** hay una **prueba escrita, en papel,** sobre la 1.ª parte del proyecto (pasos 0 a 7): Android Studio, Kotlin, la arquitectura típica de una app (pantallas, ViewModel, estado y navegación) y el propio proyecto. Esta 2.ª parte no entra en la prueba.

Del 10 al 13 de noviembre. Cada paso trae una pista y qué hacer si algo falla.

## Con qué trabajas

- **Si terminaste la 1.ª parte y funciona**: sigue con tu proyecto. Haz antes una copia de la carpeta (por ejemplo `CentroAcogida2`) y trabaja sobre la copia.
- **Si no la terminaste o no te funciona**: descarga el repositorio del módulo (*Code → Download ZIP*), copia `2026_11_09-13/clase/plantilla/CentroAcogida` a una carpeta sin tildes ni espacios (por ejemplo, `Documentos\PMDM`) y abre con *File > Open* la carpeta `CentroAcogida`, la que contiene `settings.gradle.kts`. Es la 1.ª parte terminada; los comentarios `// TODO Paso 8` y `// TODO Paso 9.1` marcan dónde se añade código.

## Objetivo

Añadir a la app:

1. una lista que se adapta al ancho de la pantalla, probada en **dos tamaños** (móvil y tableta);
2. las solicitudes guardadas en una **base de datos con Room**, que siguen ahí aunque se cierre la app.

## Qué se repasa de cada unidad

| Unidad | Qué repasamos | Dónde |
|---|---|---|
| UD1 · Tecnologías y entornos | AVD de distintos tamaños | Paso 8 |
| UD2 · Compose y adaptación a tamaños | `LazyVerticalGrid`, `GridCells.Adaptive`, dp, vistas previas | Paso 8 |
| UD2 · Flutter | Solo como extra opcional | Extra B |
| UD3 · Persistencia y servicios del dispositivo | Room (entidad, DAO, base de datos) y corrutinas (paso 9); permisos en tiempo de ejecución («Lo vemos juntos»); servicios web (Extra C) | Paso 9, «Lo vemos juntos» y Extra C |
| UD4 · Multimedia (inicio) | Capturar una imagen con la cámara | «Lo vemos juntos» |

## Plan por sesiones (50 minutos cada una)

| Día y hora | Qué hacemos |
|---|---|
| Mar 10 nov, 08:30 | Al empezar: añadir las dependencias de **9.1** y pulsar *Sync Now*. Mientras descarga, **paso 8** (dos tamaños de pantalla). |
| Mar 10 nov, 09:20 | **Paso 9** (Room: 9.2 a 9.4). |
| Mié 11 nov, 08:30 | **Paso 9** (9.5, «Mis solicitudes») y **Lo vemos juntos: un permiso y la cámara** (15 min, con el docente). |
| Mié 11 nov, 09:20 y 10:10 | UD4, según el plan del curso. |
| Vie 13 nov, 13:00 y 13:50 | **Evaluación continua: prueba escrita**, en papel, sobre la 1.ª parte del proyecto (ver [`entrega.md`](entrega.md)). El resto del tiempo, terminar el paso 9 o hacer los extras. |

**Los pasos se hacen en orden**, cada uno en la sesión que le toca según el plan de la semana: cada paso usa lo del anterior. Si terminas el paso del día antes de tiempo, puedes adelantar el siguiente o hacer los extras.

**Descargas.** Con toda la clase en la misma red, las descargas de Gradle tardan. Haz el *Sync* de 9.1 al principio de la sesión del martes, no a mitad.

## Cómo está escrito cada paso

**Objetivo** (qué consigues), **Pista** (código o explicación corta), **Comprueba que…** (cómo saber que está bien) y **Si te falla…** (errores típicos y alternativas).

El código usa el paquete `com.example.centroacogida`; si tu app tiene otro, usa el tuyo. Cuando Android Studio marque en rojo un nombre, pon el cursor encima y pulsa **Alt + Intro** para añadir el `import`.

---

## Paso 8 · Dos tamaños de pantalla

**Objetivo.** Que la lista aproveche el ancho de una tableta y probarlo en los dos AVD del paso 0.

**Pista.** Cambia la `LazyColumn` de `PantallaLista` por una cuadrícula adaptativa:

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

Aquí el `items` correcto es `androidx.compose.foundation.lazy.grid.items` (borra el import de `lazy.items` si queda sin usar).

`GridCells.Adaptive(320.dp)` mete tantas columnas como quepan con un mínimo de 320 dp: en el móvil sale una y en la tableta dos o tres, sin ningún `if`. Los **dp** miden lo mismo en pantallas con distinta resolución. En el detalle y en el formulario puedes añadir `.widthIn(max = 600.dp)` al `Modifier` de la `Column` para que en la tableta no queden líneas larguísimas.

**Comprueba que…**

- en el **móvil** en vertical hay una columna;
- en la **tableta** (o el móvil en horizontal) hay varias;
- todo se ve y se puede usar en los dos (nada cortado, botones accesibles).

Haz una captura en cada dispositivo (botón de cámara del emulador): las enseñarás en clase.

**Si te falla…** *No tienes AVD de tableta o el emulador no va*: añade una vista previa con otro ancho:

```kotlin
@Preview(name = "Tableta", widthDp = 840, heightDp = 1200, showBackground = true)
@Composable
fun ListaTabletaPreview() {
    CentroAcogidaTheme {
        PantallaLista(uiState = AcogidaUiState(), onCambiarUrgentes = {}, onAnimalClick = {})
    }
}
```

---

## Paso 9 · Guardar las solicitudes con Room (UD3)

**Objetivo.** Que las solicitudes de adopción del formulario se guarden en una base de datos del móvil y se vean en una pantalla nueva, «Mis solicitudes». Tienen que seguir ahí aunque se cierre la app.

**Mini-explicación.** **Room** es la forma recomendada de usar una base de datos SQLite en Android. Se escribe en tres piezas:

- la **entidad** (`@Entity`): una tabla; cada objeto es una fila;
- el **DAO** (`@Dao`): las consultas;
- la **base de datos** (`@Database`): une las dos.

Room genera el resto del código al compilar, con **KSP**. Acceder a la base de datos puede tardar, así que no se hace en el hilo de la interfaz, sino en una **corrutina** (`suspend` y `viewModelScope.launch`).

### 9.1 · Dependencias (al empezar el martes)

Añádelas al empezar la primera sesión y pulsa *Sync Now*: mientras descarga, haz el paso 8. Son las versiones probadas con esta plantilla.

En `gradle/libs.versions.toml`:

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

En el `build.gradle.kts` del proyecto (el de la raíz), dentro de `plugins { }`: `alias(libs.plugins.ksp) apply false`.

En `app/build.gradle.kts`, dentro de `plugins { }`: `alias(libs.plugins.ksp)`. Y dentro de `dependencies { }`:

```kotlin
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

**Sync Now.**

**Si el *Sync* falla por KSP** (por ejemplo, *Plugin [id: 'com.google.devtools.ksp'] was not found*): comprueba las tres piezas de arriba (el plugin en `[plugins]` del catálogo y en los dos `build.gradle.kts`) y vuelve a pulsar *Sync Now*. No sigas con 9.2 hasta que el *Sync* termine bien.

### 9.2 · La entidad y el DAO

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

En `AcogidaViewModel.kt`, cambia la cabecera de la clase y añade el DAO y la lista de solicitudes:

```kotlin
class AcogidaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AcogidaDatabase.obtener(application).solicitudDao()

    // Las solicitudes guardadas, como estado para la interfaz
    val solicitudes: StateFlow<List<Solicitud>> = dao.todas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ... lo que ya tenías (uiState, formulario, etc.)
```

Y cambia `enviarSolicitud` para que reciba el animal y guarde la solicitud si es válida:

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

En `PantallaLista.kt`, añade el parámetro `onVerSolicitudes: () -> Unit = {}` y un botón en la barra superior (import `androidx.compose.material3.TextButton`):

```kotlin
TopAppBar(
    title = { Text("Centro de acogida") },   // o el título que pusiste en el paso 3
    actions = { TextButton(onClick = onVerSolicitudes) { Text("Mis solicitudes") } }
)
```

En `AcogidaApp.kt`:

- debajo de `val formulario by …`: `val solicitudes by viewModel.solicitudes.collectAsState()`;
- en `PantallaLista(…)`: `onVerSolicitudes = { navController.navigate("solicitudes") }`;
- un destino nuevo en el `NavHost`: `composable("solicitudes") { PantallaSolicitudes(solicitudes = solicitudes, onVolver = { navController.popBackStack() }) }`;
- en el destino de la solicitud, guarda el animal en una variable (`val animal = viewModel.buscarAnimal(id)`), pásalo como `animal = animal` y cambia `onEnviar = viewModel::enviarSolicitud` por `onEnviar = { viewModel.enviarSolicitud(animal) }`.

**Comprueba que…**

- envías una solicitud válida y aparece en «Mis solicitudes», con el animal, tus datos y la fecha;
- si cierras la app desde Recientes y la vuelves a abrir, la solicitud sigue ahí;
- en *App Inspection > Database Inspector* (con la app abierta en el emulador) aparece la tabla `solicitudes`.

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

## Lo vemos juntos: pedir un permiso y hacer una foto (UD3 y UD4, miércoles, 15 min, con el docente)

Lo que queda de la UD3 (permisos) y el inicio de la UD4 (multimedia), en la misma app ampliada. En el detalle de cada animal, «Hacer una foto» **pide el permiso de cámara**. Si lo concedes, abre la cámara y muestra la foto; si no, explica por qué no puede. El docente lo ejecuta y recorremos el código; no hay que escribir nada. No entra en la prueba escrita del viernes 13: se trabaja en la UD4.

| Dónde | Qué hay que saber |
|---|---|
| `AndroidManifest.xml` | Un permiso peligroso, como `CAMERA`, se declara aquí **y además** se pide mientras se usa la app |
| `PantallaDetalle.kt` · `FotoDelAnimal` | `rememberLauncherForActivityResult` con `RequestPermission` (el diálogo del sistema) y con `TakePicturePreview` (la cámara) |

**Explícalo tú:** ¿qué pasa si deniegas el permiso? ¿Y si lo concedes «solo esta vez»?

El resto de la UD3 (servicios REST con Retrofit, DataStore, WorkManager, sensores, Bluetooth y localización) lo trabajaremos dentro del proyecto integrado de la UD4, del 17 de noviembre al 2 de diciembre, continuando esta misma app.

---

## Para quien termine (opcional)

**Extra A · Buscador.** Un `OutlinedTextField` encima de la lista que filtre por nombre. Pista: un campo `textoBusqueda` en `AcogidaUiState` y, en `animalesVisibles`, un segundo `filter { it.nombre.contains(textoBusqueda, ignoreCase = true) }`.

**Extra B · Flutter (comparación).** Si tienes Flutter instalado (`flutter doctor` sin errores graves), crea un proyecto con `flutter create acogida_flutter`, ejecútalo y busca en `lib/main.dart` el equivalente de: un composable (widget), `remember`/`mutableStateOf` (`StatefulWidget` y `setState`) y `LazyColumn` (`ListView.builder`). Escribe tres líneas comparando. Si no lo tienes, sáltatelo: es opcional.

**Extra C · Avance de UD3 (solo leer y pensar).** Las solicitudes ya están en Room, pero los animales siguen escritos en el código. Piensa: ¿qué cambiaría si los animales vinieran de un servicio web (Retrofit)? ¿Qué parte de tu código cambiaría y cuál no? (Pista: si el ViewModel da los datos a las pantallas, las pantallas no tendrían que cambiar.)

---

## Entrega

Qué se entrega esta semana y cuándo: [`entrega.md`](entrega.md).

## Cómo comprobarlo (lista final)

| Comprobación | Hecho |
|---|---|
| El proyecto compila y se ejecuta en un AVD (o en un móvil real) |  |
| La lista se ve en una columna en el móvil y en varias en la tableta |  |
| Las solicitudes se guardan con Room, salen en «Mis solicitudes» y siguen al cerrar la app |  |
