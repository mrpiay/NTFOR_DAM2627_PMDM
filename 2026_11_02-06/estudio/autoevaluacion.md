# Autoevaluación · semana del 2 al 6 de noviembre

Preguntas sobre la 1.ª parte del proyecto «Centro de acogida 2.0». Puede que sufran modificaciones a lo largo de la semana. Responde poniendo el número de cada pregunta.

En las preguntas con código, abre `MainActivity.kt` (en `app/src/main/java/com/example/centroacogida`) y pega el código dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)`, así:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // aquí va el código de la pregunta
    Log.d(TAG, "onCreate")
    ...
```

Antes de pegar el de una pregunta, borra el de la anterior. Ejecuta la app y, en Logcat, filtra por `tag:Pregunta`. Copia en tu respuesta los mensajes (el texto que va después de `Pregunta`) y asegúrate de que lo entiendes: en las pruebas escritas tendrás que explicarlo sin ayuda de la IA. Cuando termines, borra el código de las preguntas.

Si en una pregunta aparece algo que no has visto, búscalo en «Dónde consultar», en el [README de `estudio/`](README.md).

---

**1.** ¿Qué hace el *Sync* de Gradle? ¿Qué se pone en `gradle/libs.versions.toml` y qué en `app/build.gradle.kts`? Para añadir Navigation Compose, ¿qué cambiaste en cada uno?

**2.** La biblioteca de iconos (`material-icons-core`) no lleva versión. ¿Quién la fija y para qué sirve?

**3.** La plantilla tiene `minSdk = 24`. ¿Qué significa? ¿En qué dispositivos se puede instalar la app?

**4.** ¿Para qué sirve Logcat? ¿Qué hace `Log.d(TAG, "onStart")` y para qué sirve el filtro `tag:Ciclo`?

**5.** ¿Qué hay en cada uno de estos archivos del proyecto: `Animal.kt`, `DatosAnimales.kt`, `TarjetaAnimal.kt`, `PantallaLista.kt`, `PantallaDetalle.kt`, `PantallaSolicitud.kt`, `AcogidaUiState.kt`, `FormularioUiState.kt`, `AcogidaViewModel.kt`, `AcogidaApp.kt` y `MainActivity.kt`?

**6.** Al girar el móvil, ¿qué métodos del ciclo de vida salen en Logcat y en qué orden? ¿Por qué con `remember` el interruptor «Adopciones urgentes» volvía a estar desactivado y con el ViewModel no?

**7.** ¿Qué es un composable? En `TarjetaAnimal`, ¿para qué sirve el parámetro `modifier`? ¿Y `Modifier.padding(innerPadding)` en la pantalla de lista?

**8.** ¿Qué hace `remember`? ¿Qué es la recomposición y cuándo ocurre?

**9.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val luna = Animal(1, "Luna", "Perro", 3, R.drawable.ic_launcher_foreground, urgente = true)
val copia = luna.copy(edad = 4)
Log.d("Pregunta", "${luna.nombre}, ${luna.edad} -> ${copia.nombre}, ${copia.edad}")
Log.d("Pregunta", "Iguales: ${luna == luna.copy()}, ${luna == copia}")
Log.d("Pregunta", "Descripción: '${luna.descripcion}'")
```

**10.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val animales = listOf(
    Animal(1, "Luna", "Perro", 3, R.drawable.ic_launcher_foreground, urgente = true),
    Animal(2, "Misi", "Gato", 1, R.drawable.ic_launcher_foreground, urgente = false),
    Animal(3, "Kira", "Perro", 4, R.drawable.ic_launcher_foreground, urgente = true)
)
val urgentes = animales.filter { it.urgente }
val buscado: Animal? = animales.find { it.id == 7 }
Log.d("Pregunta", "Urgentes: ${urgentes.size}")
Log.d("Pregunta", "id 7: ${buscado?.nombre ?: "Desconocido"}")
Log.d("Pregunta", "Edad del id 7: ${buscado?.edad}")
```

**11.** En la lista se escribe `items(uiState.animalesVisibles, key = { it.id })`. ¿Para qué sirve `key`? ¿Qué pasa si dos animales tienen el mismo id?

**12.** En `AcogidaViewModel`, ¿por qué hay dos variables, `_uiState` (`MutableStateFlow`, privada) y `uiState` (`StateFlow`, pública)? ¿Qué hace `_uiState.update { it.copy(soloUrgentes = valor) }`?

**13.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val estado = AcogidaUiState(
    animales = listOf(
        Animal(1, "Luna", "Perro", 3, R.drawable.ic_launcher_foreground, urgente = true),
        Animal(2, "Misi", "Gato", 1, R.drawable.ic_launcher_foreground, urgente = false)
    )
)
val urgente = estado.copy(soloUrgentes = true)
Log.d("Pregunta", "Visibles: ${estado.animalesVisibles.size} y ${urgente.animalesVisibles.size}")
Log.d("Pregunta", "Solo urgentes: ${estado.soloUrgentes} y ${urgente.soloUrgentes}")
Log.d("Pregunta", "Animales: ${estado.animales.size} y ${urgente.animales.size}")
```

**14.** Las pantallas (`PantallaLista`, `PantallaDetalle`, `PantallaSolicitud`) no reciben el ViewModel ni el `navController`: reciben el estado y lambdas. ¿Qué es el flujo unidireccional de datos? ¿Qué se gana haciéndolo así?

**15.** La lista navega con `navController.navigate("detalle/${animal.id}")` y el destino tiene la ruta `"detalle/{animalId}"`. ¿Cómo llega el id del animal a `PantallaDetalle`? ¿Qué se ve si llega un id que no existe?

**16.** En la lista pulsas en Misi y, en su detalle, «Solicitar adopción». ¿Qué pantallas hay en la pila de navegación? ¿A dónde lleva la flecha de «Volver» (`popBackStack()`)? ¿Y el botón «Volver a la lista» (`popBackStack("lista", inclusive = false)`)?

**17.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val form = FormularioUiState(nombre = "Ana", email = "ana@correo", telefono = "612345678")
Log.d("Pregunta", "Nombre: ${form.errorNombre}")
Log.d("Pregunta", "Correo: ${form.errorEmail}")
Log.d("Pregunta", "Teléfono: ${form.errorTelefono}")
Log.d("Pregunta", "Condiciones: ${form.errorCondiciones}")
Log.d("Pregunta", "Válido: ${form.esValido}")
```

**18.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val vm = AcogidaViewModel()
vm.cambiarTelefono("612")
vm.cambiarTelefono("612a")
Log.d("Pregunta", "1: ${vm.formulario.value.telefono}")
vm.cambiarTelefono("6123456789")
Log.d("Pregunta", "2: ${vm.formulario.value.telefono}")
vm.enviarSolicitud()
Log.d("Pregunta", "3: ${vm.formulario.value.intentadoEnviar} ${vm.formulario.value.enviado}")
vm.reiniciarFormulario()
Log.d("Pregunta", "4: '${vm.formulario.value.telefono}' ${vm.formulario.value.intentadoEnviar}")
```

**19.** Cuenta, paso a paso, qué pasa desde que escribes una cifra en el campo «Teléfono» hasta que aparece en pantalla. Di qué función de qué archivo se ejecuta en cada paso. ¿Por qué no se puede escribir una letra?

**20.** Cuenta, paso a paso, qué pasa al pulsar «Enviar» con el formulario vacío: qué función se ejecuta, qué cambia en el estado y por qué ahora sí se ven los errores. ¿Para qué se llama a `viewModel.reiniciarFormulario()` antes de abrir el formulario?

## UD1 y UD2

Estas preguntas se responden con los apuntes de cada unidad, en `estudio/`.

**21.** Di cuatro limitaciones de un móvil frente a un ordenador y cómo afecta cada una a cómo se programa una app.

**22.** ¿En qué se diferencian una app nativa, una multiplataforma y una web (PWA)? Di una tecnología de cada una.

**23.** ¿Qué capas tiene Android? ¿Qué es el nivel de API? ¿Qué diferencia hay entre `targetSdk` y `compileSdk`?

**24.** ¿Qué son los **sp** y en qué se diferencian de los dp? ¿Para qué sirven carpetas de recursos como `values-es` o `values-night`?

**25.** ¿Qué hay en `AndroidManifest.xml` y qué en la carpeta `res`? ¿Cómo se usa desde el código una imagen de `res/drawable`?

**26.** ¿Qué hace `!!` en Kotlin y por qué conviene evitarlo? ¿Qué se pierde al girar el móvil si se guarda con `remember`, y qué no se pierde con `rememberSaveable` o con un ViewModel?
