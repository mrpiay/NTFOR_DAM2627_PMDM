# PMDM · Proyecto guiado «Centro de acogida 2.0» · 1.ª parte

## Punto de partida

**Hasta esta fecha, según la programación del módulo, deberíais haber estudiado:**

- **UD1 · Tecnologías y entornos de desarrollo para dispositivos móviles**, **UD2 · Desarrollo de aplicaciones móviles e interfaces de usuario** y **UD3 · Persistencia, conectividad y servicios del dispositivo móvil**, completas.
- **UD4 · Desarrollo de aplicaciones multimedia**, empezada.

**La idea.** Esta semana es un **punto de control** para **ponernos al día con la programación, en la medida de lo posible**: un proyecto que repasa lo que se debería haber dado hasta ahora, continuando vuestra app del centro de acogida (o recuperándola con la plantilla, si no la tenéis) para completar la UD2. El proyecto tiene **dos partes**: esta semana, los pasos 0 a 7 (UD1 y UD2); la semana del 9 al 13 de noviembre, los pasos 8 y 9 (dos tamaños de pantalla, de la UD2, y Room, de la UD3) y un permiso y la cámara, que es el inicio de la UD4. El resto de la UD3 se trabaja dentro del proyecto integrado de la UD4, según la programación. Lo que ya sabéis, lo afianzáis; lo que no se llegó a dar, **se explica en el paso donde aparece**.

**Usa la IA como guía, no para que lo haga por ti.** Pídele los **pasos necesarios** para implementar cada cosa y que te explique lo que no entiendas; **el código lo pones tú en tu proyecto**, aunque sea copiando y pegando, sabiendo **dónde** lo pones y **para qué**: es la única forma de familiarizarte con tu entorno. El objetivo es que lo que hagas **funcione y cumpla los requisitos** de cada paso («Comprueba que…») y que sepas **dónde está cada cosa y qué habría que tocar para cambiar o añadir algo**: es lo que te van a pedir después.

**El objetivo es entender cada paso:** es lo que necesitas para responder los cuestionarios de autoevaluación y preparar la prueba escrita.

**Evaluación continua.** Algunas semanas hay una **prueba escrita, en papel,** en las dos últimas sesiones del último día de clase, después de que se publiquen las soluciones de lo que entra. La primera es el **viernes 20 de noviembre**, sobre **las dos partes del proyecto** (pasos 0 a 9): Android Studio, Kotlin, la arquitectura típica de una app (pantallas, ViewModel, estado y navegación), la adaptación a varios tamaños de pantalla, Room y el propio proyecto.

**Y la UD2.** Su nota sale de su entrega y su examen de octubre. Si te falta alguna de las dos, la sustituyen las preguntas de la prueba escrita del viernes 20 de noviembre sobre los pasos 1 a 3 y 5 a 7 de este proyecto, corregidas con los criterios de la UD2.

Semana del 3 al 6 de noviembre. Repasamos y completamos lo que falta de la UD2, paso a paso.

## Objetivo

Partir de la app «Centro de acogida» (lista de animales con `LazyColumn`, `Card` y el interruptor «Adopciones urgentes») y añadirle:

1. el estado guardado en un **ViewModel** con **StateFlow**, para que no se pierda al girar el móvil;
2. una pantalla de **detalle** de cada animal, a la que se llega **navegando** desde la lista;
3. un **formulario «Solicitar adopción»** con campos de texto y **validación**.

Los pasos 1 a 3 ya están hechos (son la tarea «App centro de acogida»): solo se repasan. Desde el paso 4 se completa lo que falta. En la 2.ª parte, la lista se adaptará a dos tamaños de pantalla y las solicitudes se guardarán con Room.

## Qué se repasa de cada unidad

| Unidad | Qué repasamos | Dónde |
|---|---|---|
| UD1 · Tecnologías y entornos | Android Studio, abrir y ejecutar un proyecto, Gradle, Logcat, AVD, estructura del proyecto, ciclo de vida, modificar una app ya hecha | Pasos 0, 1, 4, 5 y 6 |
| UD2 · Kotlin | null safety, data class, `listOf`, `filter`, `find`, lambdas | Pasos 1, 5 y 6 |
| UD2 · Compose, estado y Material | composables, `Modifier`, `Image`, `remember`, `mutableStateOf`, recomposición, `Switch`, `TextField`, validación, `LazyColumn`, `Card`, `Scaffold`, `TopAppBar`, `MaterialTheme` | Pasos 2, 3 y 7 |
| UD2 · ViewModel y navegación | ViewModel, UI State, StateFlow, flujo unidireccional de datos, Navigation Compose | Pasos 5 y 6 |

## Plan por sesiones (50 minutos cada una)

| Día y hora | Qué hacemos |
|---|---|
| Mar 3 nov, 08:30 | Leer «Punto de partida», «Objetivo» y «Las herramientas» (30 min). **Paso 0** (abrir la app y AVD, 20 min); quien tenga que instalar algo, lo hace mientras. |
| Mar 3 nov, 09:20 | **Pasos 1–3** (repaso de lo ya hecho) y empezar el **Paso 4** (ciclo de vida). |
| Mié 4 nov, 08:30 | Al empezar: añadir las dependencias de **5.1 y 6.1** y pulsar *Sync Now*. Mientras descarga, **paso 4** (ciclo de vida). Después, **paso 5** (ViewModel y StateFlow). |
| Mié 4 nov, 09:20 | **Paso 6** (navegación lista → detalle). |
| Mié 4 nov, 10:10 | **Paso 7** (formulario con validación). |
| Vie 6 nov, 13:00 | Terminar los pasos 6 y 7. |
| Vie 6 nov, 13:50 | Terminar el **paso 7** y repasar la lista final («Cómo comprobarlo»). |

**Los pasos se hacen en orden**, cada uno en la sesión que le toca según el plan de la semana: cada paso usa lo del anterior.

**Descargas.** Con toda la clase en la misma red, las descargas de Gradle tardan. Haz el *Sync* de 5.1 y 6.1 al principio de la sesión del miércoles, no a mitad.

## Las herramientas

Todo se hace en **Android Studio**, el entorno de desarrollo oficial de Android. Cómo instalarlo y crear el emulador: [`software-PMDM.md`](software-PMDM.md).

| Herramienta | Qué es | Dónde está en Android Studio | Pasos |
|---|---|---|---|
| **Android Studio** | El programa donde se abre el proyecto, se escribe el código y se ejecuta la app | El panel **Project** (a la izquierda, con la vista *Android*) muestra los archivos; el botón **Run** (el triángulo verde de arriba, o `Mayús + F10`) ejecuta la app | Todos |
| **Gradle** | La herramienta que construye la app y descarga las bibliotecas que usa. Se configura con `gradle/libs.versions.toml` y `app/build.gradle.kts` | Al cambiar esos archivos, sale arriba un aviso: **Sync Now**. Los errores, en la pestaña *Build* (abajo) | 0, 5.1 y 6.1 |
| **Emulador** (AVD, *Android Virtual Device*) | Un móvil Android simulado en el ordenador, para ejecutar la app | *View > Tool Windows > Device Manager*. Al ejecutar, sale en la ventana *Running Devices* | 0 y siguientes |
| **Logcat** | Los mensajes que escribe la app mientras se ejecuta (con `Log.d`) y sus errores | *View > Tool Windows > Logcat* (abajo) | 1, 4, 5 y 6 |
| **Vista previa** | Dibuja un composable sin ejecutar la app | En un archivo con `@Preview`, el modo **Split** o **Design** (arriba a la derecha del editor) | 2 |

## Cómo está escrito cada paso

**Objetivo** (qué consigues), **Pista** (código o explicación corta), **Comprueba que…** (cómo saber que está bien) y **Si te falla…** (errores típicos y cómo resolverlos).

En la plantilla, los comentarios `// TODO Paso N` marcan dónde va cada cambio; los de los pasos 8 y 9 son de la 2.ª parte: déjalos como están. Para crear un archivo nuevo: clic derecho en el paquete `com.example.centroacogida` → *New → Kotlin Class/File* → **File**, con el nombre indicado; pega el código debajo de la línea `package` que crea Android Studio. Los imports que queden en gris ya no se usan: bórralos con *Code → Optimize Imports*.

El código usa el paquete `com.example.centroacogida`. Cuando Android Studio marque en rojo un nombre, pon el cursor encima y pulsa **Alt + Intro** para añadir el `import`.

---

## Paso 0 · Abrir la app y preparar el emulador

**Objetivo.** Tener la app ejecutándose en el emulador de móvil.

**Pista.**

1. **Si tienes tu propia app del centro de acogida**: cambia el nombre de su carpeta a `CentroAcogida-anterior` y guárdala para consultarla. El proyecto se hace en `CentroAcogida`, desde la plantilla.
2. **El proyecto**: descarga el repositorio del módulo (https://github.com/mrpiay/NTFOR_DAM2627_PMDM → *Code → Download ZIP*), descomprímelo y copia `2026_11_02-06/clase/plantilla/CentroAcogida` a una carpeta sin tildes ni espacios (por ejemplo, `Documentos\PMDM`).
   **Ábrelo en Android Studio**: en la pantalla de bienvenida, **Open** (si ya hay un proyecto abierto, *File > Open*), elige la carpeta `CentroAcogida`, la que contiene `settings.gradle.kts`, y pulsa *OK*. Si pregunta si confías en el proyecto, *Trust Project*.
3. Espera a que termine el **Sync** de Gradle (barra inferior).
4. En **Device Manager** (*View > Tool Windows > Device Manager*) crea, si no lo tienes, un **Pixel** (*Phone*), como explica [`software-PMDM.md`](software-PMDM.md).
5. Ejecuta la app (**Run**) en el móvil.

La plantilla está probada con **Android Studio 2026.2** (*Help > About*). Con una versión anterior, el *Sync* puede fallar con un error de versión del plugin de Android (AGP): actualiza Android Studio.

Cada imagen de sistema corresponde a un **nivel de API**. Tu app funciona en un dispositivo si su API es igual o mayor que el `minSdk` del proyecto (en `app/build.gradle.kts`).

**Comprueba que…** aparece la lista de animales y el interruptor «Adopciones urgentes» filtra.

**Si te falla…**

- *El emulador no arranca o va muy lento*: la virtualización (Intel VT-x o AMD-V) tiene que estar activada en la BIOS/UEFI.
- *Error de Gradle al hacer el Sync*: lee la primera línea roja de la pestaña *Build*. Si habla de `jlink` o del JDK, en *File > Settings > Build, Execution, Deployment > Build Tools > Gradle* elige como **Gradle JDK** el que trae Android Studio (`jbr`).

---

## Paso 1 · Repaso: modelo y datos

**Objetivo.** Entender `Animal.kt` y `DatosAnimales.kt`.

**Pista.** Abre los dos archivos y piensa:

- ¿Qué aporta `data class` frente a una clase normal? (Pista: `toString`, `equals`, `copy`.)
- ¿Por qué `descripcion` tiene un valor por defecto (`= ""`) y los demás campos no?
- ¿Qué es `R.drawable...` y dónde están esas imágenes? (Código en `app/src/main/java`, recursos en `app/src/main/res`, la actividad principal declarada en `AndroidManifest.xml`, dependencias en `build.gradle.kts` y `gradle/libs.versions.toml`.)

**Pequeño cambio.** Añade un noveno animal: copia la última línea de `DatosAnimales` y cambia el id a `9`, el nombre y los datos. Cada animal necesita un id distinto: la lista lo usa como clave y, si se repite, la app se cierra. Prueba en `MainActivity.onCreate` este mini-repaso de Kotlin y mira el resultado en Logcat:

```kotlin
val urgentes = DatosAnimales.animales.filter { it.urgente }       // lambda: lista nueva
val buscado: Animal? = DatosAnimales.animales.find { it.id == 2 } // puede ser null
val nombre = buscado?.nombre ?: "Desconocido"                     // llamada segura y Elvis
Log.d("Repaso", "Urgentes: ${urgentes.size}, id 2: $nombre")
```

**Comprueba que…** el animal nuevo sale en la lista y en Logcat (filtro `tag:Repaso`) ves el mensaje. Después puedes borrar esas líneas.

**Si te falla…**

- *Aviso amarillo en `@DrawableRes` («This annotation is currently applied to the value parameter only…»)*: es solo un aviso; compila igual.
- *`Unresolved reference: R`*: revisa el `package` del archivo y el nombre de la imagen (en minúsculas, sin extensión, sin espacios ni tildes).

---

## Paso 2 · Repaso: tarjeta reutilizable

**Objetivo.** Entender `TarjetaAnimal.kt`.

**Pista.** Un composable es una función que **describe** la interfaz: recibe datos (`animal`) y avisa de los eventos con lambdas (`onClick`). El parámetro `modifier` permite que quien lo usa decida márgenes o tamaño. Localiza en el código: la `Card`, la `Image` con `painterResource`, un estilo de `MaterialTheme.typography`, un color de `MaterialTheme.colorScheme` y una forma de `MaterialTheme.shapes`.

**Pequeño cambio.** Abre la vista previa (`TarjetaAnimalPreview`, modo *Split*) y cambia el tamaño de la imagen de `72.dp` a `96.dp`, o el estilo del nombre a `titleLarge`.

**Comprueba que…** la vista previa cambia sin ejecutar la app.

**Para pensar:** ¿qué es un composable y para qué sirve el parámetro `modifier`?

**Si te falla…** *La imagen sale enorme*: falta `Modifier.size(...)` en la `Image`.

---

## Paso 3 · Repaso: pantalla de lista con Scaffold, Switch y estado

**Objetivo.** Entender `PantallaLista.kt` y cómo funciona el interruptor.

**Pista.** Cuando cambia `soloUrgentes`, Compose **recompone** (vuelve a ejecutar) las funciones que lo leen y la lista se actualiza sola. `remember` hace que el valor no se pierda en cada recomposición. `Modifier.padding(innerPadding)` evita que la lista quede debajo de la `TopAppBar`.

**Pequeño cambio.** Cambia el texto de la barra superior y añade un `Text("Mostrando ${visibles.size} animales")` después del `}` que cierra la `Row` del interruptor, antes de la `LazyColumn`.

**Comprueba que…** el contador cambia al activar y desactivar el interruptor.

**Para pensar:** ¿qué hace `remember` y qué pasa cuando cambia el estado (recomposición)?

**Si te falla…**

- *`by remember` da error*: faltan los imports `androidx.compose.runtime.getValue` y `androidx.compose.runtime.setValue`.
- *`TopAppBar` dice que es experimental*: añade `@OptIn(ExperimentalMaterial3Api::class)` encima de la función.

---

## Paso 4 · Ciclo de vida y giro de pantalla

**Objetivo.** Ver en Logcat el ciclo de vida de la actividad y descubrir por qué hace falta un ViewModel.

**Pista.** En `MainActivity` (busca los `TODO Paso 4`):

```kotlin
private val TAG = "Ciclo"

override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
```

Y en `onCreate`, después de `super.onCreate(savedInstanceState)`: `Log.d(TAG, "onCreate")`.

Experimento: ejecuta la app, abre **Logcat** con el filtro `tag:Ciclo`, activa el interruptor y **gira** el emulador.

**Comprueba que…** aparecen `onPause`, `onStop`, `onDestroy` y luego `onCreate`, `onStart`, `onResume`: al girar, la actividad **se destruye y se vuelve a crear**. Y el interruptor vuelve a estar desactivado: el estado guardado con `remember` se ha perdido. (Otra solución rápida es `rememberSaveable`, pero para el estado de la pantalla se usa un ViewModel, en el paso 5.)

**Si te falla…**

- *La pantalla no gira*: activa la rotación automática en los ajustes rápidos del emulador.
- *Logcat sale vacío*: comprueba que está seleccionado el dispositivo y el proceso de tu app.

---

## Paso 5 · ViewModel, UI State y StateFlow

**Objetivo.** Sacar el estado de la pantalla a un ViewModel. La pantalla solo **muestra** el estado y **avisa** de los eventos (flujo unidireccional de datos).

### 5.1 · Dependencia

En `gradle/libs.versions.toml`, sección `[libraries]`, en lugar del comentario `# TODO Paso 5.1` (la versión `lifecycleRuntimeKtx` ya existe y se reutiliza):

```toml
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
```

En `app/build.gradle.kts`, dentro de `dependencies { }`, la línea ya está en un comentario `// TODO Paso 5.1:`: borra ese comienzo para que quede `implementation(libs.androidx.lifecycle.viewmodel.compose)`. Pulsa **Sync Now** (mejor junto con 6.1, al empezar la sesión).

No copies números de versión de internet al azar.

### 5.2 · Estado de la pantalla y ViewModel

Crea estos dos archivos en el paquete del proyecto:

```kotlin
// AcogidaUiState.kt
data class AcogidaUiState(
    val animales: List<Animal> = DatosAnimales.animales,
    val soloUrgentes: Boolean = false
) {
    val animalesVisibles: List<Animal>
        get() = if (soloUrgentes) animales.filter { it.urgente } else animales
}
```

```kotlin
// AcogidaViewModel.kt
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AcogidaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AcogidaUiState())
    val uiState: StateFlow<AcogidaUiState> = _uiState.asStateFlow()

    fun cambiarSoloUrgentes(valor: Boolean) {
        _uiState.update { it.copy(soloUrgentes = valor) }
    }

    fun buscarAnimal(id: Int): Animal? =
        _uiState.value.animales.find { it.id == id }
}
```

`_uiState` es **privado y modificable**; `uiState` es **público y de solo lectura**: solo el ViewModel cambia el estado. `copy(...)` crea una copia cambiando un campo. El ViewModel **sobrevive al giro**: la actividad se recrea, pero el ViewModel es el mismo.

### 5.3 · La pantalla recibe el estado

En `PantallaLista`:

1. Cambia la cabecera:

   ```kotlin
   fun PantallaLista(
       uiState: AcogidaUiState,
       onCambiarUrgentes: (Boolean) -> Unit,
       onAnimalClick: (Animal) -> Unit
   )
   ```

2. Borra el `var soloUrgentes by remember ...` y el `val visibles = ...`, con el comentario que tienen encima, y en el contador del paso 3 cambia `visibles.size` por `uiState.animalesVisibles.size`.
3. El interruptor: `Switch(checked = uiState.soloUrgentes, onCheckedChange = onCambiarUrgentes)`.
4. La lista: `items(uiState.animalesVisibles, key = { it.id }) { ... }`.

En `MainActivity`, de momento:

```kotlin
setContent {
    CentroAcogidaTheme {
        val viewModel: AcogidaViewModel = viewModel()
        val uiState by viewModel.uiState.collectAsState()
        PantallaLista(
            uiState = uiState,
            onCambiarUrgentes = viewModel::cambiarSoloUrgentes,
            onAnimalClick = { }
        )
    }
}
```

**Comprueba que…** el interruptor funciona igual y que, **al girar, ya no se pierde** su valor (aunque Logcat siga mostrando `onDestroy` y `onCreate`).

**Para pensar:** ¿qué cambia cuando el estado está en el ViewModel?

**Si te falla…**

- *`viewModel()` en rojo*: falta la dependencia de 5.1 (¿has pulsado *Sync Now*?) o el import `androidx.lifecycle.viewmodel.compose.viewModel`.
- *`by` en rojo*: añade el import `androidx.compose.runtime.getValue`.
- *`collectAsState` en rojo*: import `androidx.compose.runtime.collectAsState`. (Existe también `collectAsStateWithLifecycle`, recomendado en la documentación, pero necesita la dependencia `lifecycle-runtime-compose`.)
- *El interruptor no se mueve*: comprueba que `onCheckedChange` llama al ViewModel y que el `Switch` lee `uiState.soloUrgentes`.

---

## Paso 6 · Navegación: lista → detalle

**Objetivo.** Al pulsar una tarjeta se abre el detalle de ese animal; con la flecha o el botón Atrás se vuelve a la lista.

### 6.1 · Dependencias

En `gradle/libs.versions.toml`, cada línea va en su sección, en lugar de los comentarios `# TODO Paso 6.1`. **No copies las líneas** `[versions]` y `[libraries]`: ya existen.

```toml
[versions]
navigationCompose = "2.10.2"   # la versión probada con esta plantilla

[libraries]
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
```

En `app/build.gradle.kts`, las dos líneas ya están en comentarios `// TODO Paso 6.1:`: borra ese comienzo en cada una para que queden así:

```kotlin
implementation(libs.androidx.navigation.compose)
implementation(libs.androidx.compose.material.icons.core)
```

Y **Sync Now**.

La biblioteca de iconos hace falta para la flecha de «Volver» (`Icons.AutoMirrored.Filled.ArrowBack`), porque `material3` ya no la incluye. No lleva versión: como `ui` y `material3`, la fija el **Compose BOM** (`platform(libs.androidx.compose.bom)`), que da versiones compatibles entre sí.

### 6.2 · Pantalla de detalle

```kotlin
// PantallaDetalle.kt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    animal: Animal?,
    onVolver: () -> Unit,
    onSolicitar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(animal?.nombre ?: "Animal no encontrado") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (animal == null) {
            Text(
                "No se ha encontrado el animal.",
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(animal.imagen),
                    contentDescription = animal.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(MaterialTheme.shapes.large)
                )
                Text(animal.nombre, style = MaterialTheme.typography.headlineMedium)
                val anios = if (animal.edad == 1) "año" else "años"
                Text("${animal.especie} · ${animal.edad} $anios", style = MaterialTheme.typography.titleMedium)
                if (animal.urgente) {
                    Text("Necesita adopción urgente", color = MaterialTheme.colorScheme.error)
                }
                Text(animal.descripcion, style = MaterialTheme.typography.bodyLarge)
                Button(onClick = onSolicitar, modifier = Modifier.fillMaxWidth()) {
                    Text("Solicitar adopción")
                }
            }
        }
    }
}
```

**Null safety**: `animal` puede ser `null` (si llega un id que no existe). Dentro del `else`, Kotlin ya sabe que no es `null` y deja usar `animal.nombre` sin `?.`.

### 6.3 · El grafo de navegación

```kotlin
// AcogidaApp.kt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AcogidaApp(viewModel: AcogidaViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "lista") {

        composable("lista") {
            PantallaLista(
                uiState = uiState,
                onCambiarUrgentes = viewModel::cambiarSoloUrgentes,
                onAnimalClick = { animal -> navController.navigate("detalle/${animal.id}") }
            )
        }

        composable(
            route = "detalle/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("animalId") ?: -1
            PantallaDetalle(
                animal = viewModel.buscarAnimal(id),
                onVolver = { navController.popBackStack() },
                onSolicitar = { /* paso 7 */ }
            )
        }
    }
}
```

Y en `MainActivity` el `setContent` queda así:

```kotlin
setContent {
    CentroAcogidaTheme {
        AcogidaApp()
    }
}
```

Cada pantalla es un **destino** con una **ruta** (`"lista"`, `"detalle/{animalId}"`). `navigate(...)` apila una pantalla y `popBackStack()` vuelve a la anterior. Las pantallas no conocen el `navController`: solo reciben lambdas, así se pueden previsualizar por separado. El ViewModel se crea fuera del `NavHost`, así que **las dos pantallas comparten el mismo**.

**Comprueba que…**

- al pulsar una tarjeta se abre el detalle de **ese** animal;
- la flecha y el botón Atrás del sistema vuelven a la lista;
- al volver, el interruptor sigue como estaba.

**Si te falla…**

- *`Icons` o `Icons.AutoMirrored...` en rojo (`Unresolved reference 'icons'`)*: falta la biblioteca de iconos de 6.1 o no has pulsado *Sync Now*. Los imports son `androidx.compose.material.icons.Icons` y `androidx.compose.material.icons.automirrored.filled.ArrowBack`.
- *La app se cierra al pulsar una tarjeta*: mira en Logcat el error rojo (`FATAL EXCEPTION`). Lo habitual es que la ruta de `navigate` no coincida con la del `composable`.
- *Siempre sale «Animal no encontrado»*: revisa que cada animal tiene un `id` distinto y que el argumento se llama igual en la ruta (`{animalId}`) y en `getInt("animalId")`.

---

## Paso 7 · Formulario «Solicitar adopción» con validación

**Objetivo.** Desde el detalle se abre un formulario con nombre, correo, teléfono y una casilla de aceptación. Al pulsar **Enviar** se muestran los errores; si todo es correcto, aparece un mensaje de confirmación.

### 7.1 · Estado del formulario

```kotlin
// FormularioUiState.kt
data class FormularioUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val aceptaCondiciones: Boolean = false,
    val intentadoEnviar: Boolean = false,
    val enviado: Boolean = false
) {
    val errorNombre: String?
        get() = if (nombre.isBlank()) "Escribe tu nombre" else null

    val errorEmail: String?
        get() = if (!email.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) "Correo no válido" else null

    val errorTelefono: String?
        get() = if (telefono.length != 9) "El teléfono debe tener 9 cifras" else null

    val errorCondiciones: String?
        get() = if (!aceptaCondiciones) "Debes aceptar las condiciones" else null

    val esValido: Boolean
        get() = errorNombre == null && errorEmail == null &&
                errorTelefono == null && errorCondiciones == null
}
```

### 7.2 · Funciones en el ViewModel

Añade dentro de la clase `AcogidaViewModel`, antes de la última `}`:

```kotlin
private val _formulario = MutableStateFlow(FormularioUiState())
val formulario: StateFlow<FormularioUiState> = _formulario.asStateFlow()

fun cambiarNombre(valor: String) = _formulario.update { it.copy(nombre = valor) }

fun cambiarEmail(valor: String) = _formulario.update { it.copy(email = valor.trim()) }

fun cambiarTelefono(valor: String) {
    // Solo se aceptan cifras y como mucho 9
    if (valor.all { it.isDigit() } && valor.length <= 9) {
        _formulario.update { it.copy(telefono = valor) }
    }
}

fun cambiarAcepta(valor: Boolean) = _formulario.update { it.copy(aceptaCondiciones = valor) }

fun enviarSolicitud() {
    _formulario.update { it.copy(intentadoEnviar = true, enviado = it.esValido) }
}

fun reiniciarFormulario() {
    _formulario.value = FormularioUiState()
}
```

### 7.3 · Pantalla del formulario

```kotlin
// PantallaSolicitud.kt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSolicitud(
    animal: Animal?,
    form: FormularioUiState,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onAceptaChange: (Boolean) -> Unit,
    onEnviar: () -> Unit,
    onVolver: () -> Unit,
    onTerminar: () -> Unit
) {
    // Los errores solo se muestran después de pulsar «Enviar»
    val errorNombre = if (form.intentadoEnviar) form.errorNombre else null
    val errorEmail = if (form.intentadoEnviar) form.errorEmail else null
    val errorTelefono = if (form.intentadoEnviar) form.errorTelefono else null
    val errorCondiciones = if (form.intentadoEnviar) form.errorCondiciones else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adoptar a ${animal?.nombre ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (form.enviado) {
                Text(
                    "Solicitud enviada. El centro se pondrá en contacto contigo.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = onTerminar, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver a la lista")
                }
            } else {
                OutlinedTextField(
                    value = form.nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre y apellidos") },
                    singleLine = true,
                    isError = errorNombre != null,
                    supportingText = { if (errorNombre != null) Text(errorNombre) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = form.email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = errorEmail != null,
                    supportingText = { if (errorEmail != null) Text(errorEmail) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = form.telefono,
                    onValueChange = onTelefonoChange,
                    label = { Text("Teléfono") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = errorTelefono != null,
                    supportingText = { if (errorTelefono != null) Text(errorTelefono) },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = form.aceptaCondiciones, onCheckedChange = onAceptaChange)
                    Text("Acepto las condiciones de adopción")
                }
                if (errorCondiciones != null) {
                    Text(errorCondiciones, color = MaterialTheme.colorScheme.error)
                }
                Button(onClick = onEnviar, modifier = Modifier.fillMaxWidth()) {
                    Text("Enviar")
                }
            }
        }
    }
}
```

### 7.4 · Nuevo destino en el NavHost

En `AcogidaApp`, debajo de `val uiState ...`:

```kotlin
val formulario by viewModel.formulario.collectAsState()
```

En el destino de detalle, rellena `onSolicitar`:

```kotlin
onSolicitar = {
    viewModel.reiniciarFormulario()
    navController.navigate("solicitud/$id")
}
```

Y añade el destino nuevo dentro del `NavHost`:

```kotlin
composable(
    route = "solicitud/{animalId}",
    arguments = listOf(navArgument("animalId") { type = NavType.IntType })
) { entrada ->
    val id = entrada.arguments?.getInt("animalId") ?: -1
    PantallaSolicitud(
        animal = viewModel.buscarAnimal(id),
        form = formulario,
        onNombreChange = viewModel::cambiarNombre,
        onEmailChange = viewModel::cambiarEmail,
        onTelefonoChange = viewModel::cambiarTelefono,
        onAceptaChange = viewModel::cambiarAcepta,
        onEnviar = viewModel::enviarSolicitud,
        onVolver = { navController.popBackStack() },
        onTerminar = { navController.popBackStack("lista", inclusive = false) }
    )
}
```

El campo de texto **no guarda** lo que escribes: cada tecla llama a `onValueChange`, el ViewModel actualiza el estado y Compose vuelve a dibujar el campo con el valor nuevo. Por eso `cambiarTelefono` puede impedir que se escriban letras.

**Comprueba que…**

- si pulsas **Enviar** con todo vacío, salen los cuatro errores en rojo;
- en el teléfono no puedes escribir letras ni más de 9 cifras;
- con datos correctos aparece la confirmación y «Volver a la lista» te lleva a la lista;
- si giras la pantalla a mitad del formulario, **no se borra** lo escrito.

**Si te falla…**

- *No se puede escribir en un campo*: su `onValueChange` no llega al ViewModel o el `value` no lee del estado.
- *`Smart cast to 'String' is impossible`*: pasa si escribes `Text(form.errorNombre)` directamente; usa las variables locales `errorNombre`, etc.
- *Al abrir otro animal salen los datos del anterior*: falta `viewModel.reiniciarFormulario()` antes de navegar.

**Para pensar:** cómo sabe el detalle qué animal mostrar, qué hacen `navigate` y `popBackStack`, y dónde se valida el formulario.

---

## Entrega

Qué se entrega esta semana y cuándo: [`entrega.md`](entrega.md).

## Cómo comprobarlo (lista final)

| Comprobación | Hecho |
|---|---|
| El proyecto compila y se ejecuta en un AVD (o en un móvil real) |  |
| Sé explicar el modelo, la tarjeta y el interruptor (pasos 1–3) |  |
| En Logcat se ve el ciclo de vida al girar la pantalla |  |
| El estado está en un ViewModel con `StateFlow` y no se pierde al girar |  |
| Al pulsar un animal se navega a su detalle y se puede volver |  |
| El formulario muestra errores y no deja enviar datos incorrectos |  |
