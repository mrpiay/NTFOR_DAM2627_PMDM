# Autoevaluación · semana del 2 al 6 de noviembre

Preguntas sobre la 1.ª parte del proyecto «Centro de acogida 2.0». Puede que sufran modificaciones a lo largo de la semana. Responde poniendo el número de cada pregunta.

En las preguntas con código, pega el código en `MainActivity`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)`; antes, borra el de la pregunta anterior. Ejecuta la app y, en Logcat, filtra por `tag:Pregunta`. Copia en tu respuesta los mensajes (el texto que va después de `Pregunta`) y asegúrate de que lo entiendes: en las pruebas escritas tendrás que explicarlo sin ayuda de la IA.

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

**9.** Pega este código y ejecuta la app. Copia los mensajes de Logcat.

```kotlin
val luna = Animal(1, "Luna", "Perro", 3, R.drawable.ic_launcher_foreground, urgente = true)
val copia = luna.copy(edad = 4)
Log.d("Pregunta", "${luna.nombre}, ${luna.edad} -> ${copia.nombre}, ${copia.edad}")
Log.d("Pregunta", "Iguales: ${luna == luna.copy()}, ${luna == copia}")
Log.d("Pregunta", "Descripción: '${luna.descripcion}'")
```

**10.** Pega este código y ejecuta la app. Copia los mensajes de Logcat.

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

**13.** Pega este código y ejecuta la app. Copia los mensajes de Logcat.

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

**17.** Pega este código y ejecuta la app. Copia los mensajes de Logcat.

```kotlin
val form = FormularioUiState(nombre = "Ana", email = "ana@correo", telefono = "612345678")
Log.d("Pregunta", "Nombre: ${form.errorNombre}")
Log.d("Pregunta", "Correo: ${form.errorEmail}")
Log.d("Pregunta", "Teléfono: ${form.errorTelefono}")
Log.d("Pregunta", "Condiciones: ${form.errorCondiciones}")
Log.d("Pregunta", "Válido: ${form.esValido}")
```

**18.** Pega este código y ejecuta la app. Copia los mensajes de Logcat.

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
