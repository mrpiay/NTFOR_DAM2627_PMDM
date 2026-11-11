# C2 · Cuestionario · semana del 9 al 13 de noviembre

Preguntas sobre la 2.ª parte del proyecto «Centro de acogida 2.0» y sobre los apuntes. Puede que sufran modificaciones a lo largo de la semana. Responde poniendo el número de cada pregunta.

En las preguntas con código, abre `MainActivity.kt` (en `app/src/main/java/com/example/centroacogida`) y pega el código dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)`, así:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // aquí va el código de la pregunta
    Log.d(TAG, "onCreate")
    ...
```

Antes de pegar el de una pregunta, borra el de la anterior. Si algo sale en rojo, pon el cursor encima y pulsa **Alt + Intro** para añadir el `import`. Ejecuta la app y, en Logcat, filtra por `tag:Pregunta`. Copia en tu respuesta los mensajes (el texto que va después de `Pregunta`) y asegúrate de que lo entiendes: en las pruebas escritas tendrás que explicarlo sin ayuda de la IA. Cuando termines, borra el código de las preguntas.

Si en una pregunta aparece algo que no has visto, búscalo en «Dónde consultar», en el [README de `apuntes/`](apuntes/README.md).

---

## Cuestiones sobre el proyecto

**1.** ¿Cómo se comprueba que la app funciona en dos tamaños de pantalla? ¿Para qué sirve la vista previa `ListaTabletaPreview` del paso 8 y qué indica `widthDp = 840`?

**2.** En `PantallaLista.kt`, ¿qué hace `LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 320.dp), …)`? ¿Cuántas columnas salen en el móvil en vertical y cuántas en la tableta? ¿Por qué no hace falta ningún `if`?

**3.** ¿Qué son los **dp**? ¿Por qué se usan en lugar de píxeles?

**4.** ¿Para qué sirve `.widthIn(max = 600.dp)` en las columnas del detalle y del formulario? ¿Qué pasaría en la tableta sin él?

**5.** ¿Qué es **KSP** y por qué lo necesita Room? En 9.1, ¿qué añadiste en `libs.versions.toml`, en el `build.gradle.kts` del proyecto y en el de `app`?

**6.** En el `build.gradle.kts` del proyecto se escribe `alias(libs.plugins.ksp) apply false` y en el de `app`, `alias(libs.plugins.ksp)`. ¿Qué significa `apply false`?

**7.** ¿Qué es Room? ¿Cuáles son sus tres piezas, con qué anotación se marca cada una y qué archivo del proyecto es cada una?

**8.** Pega este código y ejecuta la app. Copia los mensajes de Logcat.

```kotlin
val s = Solicitud(animalId = 2, animalNombre = "Misi", nombre = "Ana", email = "ana@correo.es", telefono = "612345678")
val otra = s.copy(nombre = "Luis")
Log.d("Pregunta", "id: ${s.id}")
Log.d("Pregunta", "Tiene fecha: ${s.fecha > 0}")
Log.d("Pregunta", "${otra.animalNombre}, ${otra.nombre}, id ${otra.id}")
Log.d("Pregunta", "Misma fecha: ${s.fecha == otra.fecha}")
```

**9.** En `Solicitud`, ¿por qué el id se escribe `@PrimaryKey(autoGenerate = true) val id: Int = 0`? ¿Quién pone el id de verdad y cuándo?

**10.** En `SolicitudDao`, `insertar` es `suspend` y `todas()` no lo es, pero devuelve un `Flow<List<Solicitud>>`. ¿Por qué cada una es así? ¿Qué gana la pantalla con un `Flow`?

**11.** ¿Por qué `AcogidaDatabase.obtener(context)` devuelve siempre la misma base de datos? ¿Qué pasaría si cada pantalla creara la suya?

**12.** ¿Por qué `AcogidaViewModel` pasa de heredar de `ViewModel()` a heredar de `AndroidViewModel(application)`? ¿Hubo que cambiar cómo se crea con `viewModel()`?

**13.** Pega este código y ejecuta la app. Copia los mensajes de Logcat, en el orden en que salen. Es una base de datos en memoria, solo para la pregunta: no toca tus solicitudes.

```kotlin
val bd = Room.inMemoryDatabaseBuilder(this, AcogidaDatabase::class.java).build()
val dao = bd.solicitudDao()
Log.d("Pregunta", "Antes de launch")
lifecycleScope.launch {
    Log.d("Pregunta", "Empieza la corrutina")
    dao.insertar(Solicitud(animalId = 1, animalNombre = "Luna", nombre = "Ana", email = "ana@correo.es", telefono = "612345678"))
    dao.insertar(Solicitud(animalId = 2, animalNombre = "Misi", nombre = "Luis", email = "luis@correo.es", telefono = "698765432"))
    val lista = dao.todas().first()
    Log.d("Pregunta", "Guardadas: ${lista.size}, ids: ${lista.map { it.id }.sorted()}")
}
Log.d("Pregunta", "Después de launch")
```

**14.** ¿Por qué `insertar` se llama dentro de `viewModelScope.launch { }`? ¿Qué error da Room si se accede a la base de datos en el hilo de la interfaz?

**15.** En el ViewModel, ¿qué hace `dao.todas().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())`? ¿Por qué `emptyList()`?

**16.** Cuenta, paso a paso, qué pasa desde que pulsas «Enviar» con datos correctos hasta que la solicitud aparece en «Mis solicitudes». Di qué función de qué archivo se ejecuta en cada paso.

**17.** ¿Por qué `enviarSolicitud` pasó a recibir el animal? ¿Qué hubo que cambiar en `AcogidaApp.kt`? ¿Qué pasa si el animal es `null`?

**18.** ¿Cómo se llega a «Mis solicitudes» y cómo se vuelve? ¿Por qué el parámetro nuevo de `PantallaLista` se escribe `onVerSolicitudes: () -> Unit = {}`, con un valor por defecto?

**19.** ¿Por qué las solicitudes siguen ahí después de cerrar la app y el interruptor «Adopciones urgentes» no? Las pantallas no saben que existe Room: ¿quién habla con la base de datos? ¿Qué cambiaría si mañana los animales vinieran de un servicio web?

**20.** Para hacer una foto, ¿dónde se declara el permiso de cámara y por qué, además, hay que pedirlo mientras se usa la app? ¿Qué pasa si lo deniegas?

**21.** En el paso 10, ¿por qué `animalesVisibles` encadena dos `filter`? ¿Qué sale con el texto vacío? Cuenta qué pasa desde que escribes una letra en el campo de búsqueda hasta que la lista cambia: qué hacen `onValueChange`, `onBuscar`, `cambiarBusqueda` y `uiState`.

## Cuestiones sobre los apuntes

Estas preguntas se responden con los apuntes de cada unidad, en `apuntes/`.

**22.** ¿Cuándo usarías DataStore y cuándo Room? Pon un ejemplo de cada uno en la app del centro de acogida.

**23.** ¿Qué diferencia hay entre un permiso normal y uno peligroso? Pon un ejemplo de cada uno. ¿Qué tres respuestas puede dar la persona cuando se le pide uno peligroso?

**24.** ¿Qué hace falta para que una app hable con un servicio web y por qué HTTPS? ¿Qué diferencia hay entre un APK y un AAB, y entre una versión de depuración y una de publicación?
