# Cuestionario C1 · semana del 2 al 6 de noviembre · soluciones

## Cuestiones sobre el proyecto

**1.** ¿Qué hace el *Sync* de Gradle? ¿Qué se pone en `gradle/libs.versions.toml` y qué en `app/build.gradle.kts`? Para añadir Navigation Compose, ¿qué cambiaste en cada uno?

**Respuesta.** El *Sync* hace que Android Studio lea los archivos de Gradle, descargue las dependencias que falten y prepare el proyecto con ellas; sin él, las clases nuevas salen en rojo. En `gradle/libs.versions.toml` (el catálogo de versiones) se da un nombre a cada biblioteca y a su versión: en `[versions]`, `navigationCompose = "2.10.2"`; en `[libraries]`, `androidx-navigation-compose`. En `app/build.gradle.kts`, dentro de `dependencies { }`, se dice qué bibliotecas usa la app, con esos nombres: `implementation(libs.androidx.navigation.compose)`.

**2.** La biblioteca de iconos (`material-icons-core`) no lleva versión. ¿Quién la fija y para qué sirve?

**Respuesta.** El **Compose BOM** (`platform(libs.androidx.compose.bom)`): una lista de versiones de las bibliotecas de Compose que funcionan bien juntas. Las bibliotecas de Compose (`ui`, `material3`, los iconos) no llevan versión propia: la toman del BOM, y así no hay que ajustarlas una a una ni se mezclan versiones incompatibles.

**3.** La plantilla tiene `minSdk = 24`. ¿Qué significa? ¿En qué dispositivos se puede instalar la app?

**Respuesta.** Es el nivel de API mínimo de Android en el que se puede instalar la app: la API 24 (Android 7.0). Funciona en los dispositivos (y en los emuladores, AVD) con API 24 o mayor; en uno más antiguo no se puede instalar.

**4.** ¿Para qué sirve Logcat? ¿Qué hace `Log.d(TAG, "onStart")` y para qué sirve el filtro `tag:Ciclo`?

**Respuesta.** Logcat muestra los mensajes que escribe el sistema y la app mientras se ejecuta, también los errores (como `FATAL EXCEPTION` cuando la app se cierra). `Log.d(TAG, "onStart")` escribe un mensaje de depuración (`d`, *debug*) con la etiqueta `TAG` (aquí, `"Ciclo"`). El filtro `tag:Ciclo` deja ver solo los mensajes con esa etiqueta, entre los muchos que salen.

**5.** ¿Qué hay en cada uno de estos archivos del proyecto: `Animal.kt`, `DatosAnimales.kt`, `TarjetaAnimal.kt`, `PantallaLista.kt`, `PantallaDetalle.kt`, `PantallaSolicitud.kt`, `AcogidaUiState.kt`, `FormularioUiState.kt`, `AcogidaViewModel.kt`, `AcogidaApp.kt` y `MainActivity.kt`?

**Respuesta.**

- `Animal.kt`: la `data class` `Animal`, con los datos de cada animal.
- `DatosAnimales.kt`: la lista de animales de ejemplo.
- `TarjetaAnimal.kt`: el composable de la tarjeta de un animal en la lista.
- `PantallaLista.kt`: la pantalla de la lista, con la barra superior y el interruptor «Adopciones urgentes».
- `PantallaDetalle.kt`: la pantalla de detalle de un animal, con el botón «Solicitar adopción».
- `PantallaSolicitud.kt`: la pantalla del formulario.
- `AcogidaUiState.kt`: el estado de la lista (los animales, si solo se ven los urgentes y cuáles se ven).
- `FormularioUiState.kt`: el estado del formulario y su validación (los errores y `esValido`).
- `AcogidaViewModel.kt`: el ViewModel; guarda los dos estados y tiene las funciones que los cambian.
- `AcogidaApp.kt`: el `NavHost`, con los destinos (lista, detalle y solicitud), que conecta cada pantalla con el ViewModel y la navegación.
- `MainActivity.kt`: la actividad que arranca la app; carga `AcogidaApp` y escribe el ciclo de vida en Logcat.

**6.** Al girar el móvil, ¿qué métodos del ciclo de vida salen en Logcat y en qué orden? ¿Por qué con `remember` el interruptor «Adopciones urgentes» volvía a estar desactivado y con el ViewModel no?

**Respuesta.** `onPause`, `onStop`, `onDestroy` y después `onCreate`, `onStart`, `onResume`: al girar, la actividad se destruye y se vuelve a crear. Lo guardado con `remember` vive dentro de la actividad, así que se pierde con ella y el interruptor vuelve a su valor inicial. El ViewModel no se destruye al girar: la actividad nueva recibe el mismo ViewModel, con el mismo estado.

**7.** ¿Qué es un composable? En `TarjetaAnimal`, ¿para qué sirve el parámetro `modifier`? ¿Y `Modifier.padding(innerPadding)` en la pantalla de lista?

**Respuesta.** Un composable es una función marcada con `@Composable` que describe una parte de la interfaz a partir de los datos que recibe. El parámetro `modifier` deja que quien usa la tarjeta decida sus márgenes, tamaño, etc., sin cambiar `TarjetaAnimal`. `Modifier.padding(innerPadding)` deja el hueco que ocupa la barra superior del `Scaffold`; sin él, la lista quedaría debajo de la barra.

**8.** ¿Qué hace `remember`? ¿Qué es la recomposición y cuándo ocurre?

**Respuesta.** `remember` guarda un valor entre recomposiciones: sin él, cada vez que se vuelve a ejecutar la función, el valor volvería al inicial. La recomposición es cuando Compose vuelve a ejecutar los composables que leen un estado porque ese estado ha cambiado (por ejemplo, al mover el interruptor): así la pantalla se actualiza sola.

**9.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val luna = Animal(1, "Luna", "Perro", 3, R.drawable.ic_launcher_foreground, urgente = true)
val copia = luna.copy(edad = 4)
Log.d("Pregunta", "${luna.nombre}, ${luna.edad} -> ${copia.nombre}, ${copia.edad}")
Log.d("Pregunta", "Iguales: ${luna == luna.copy()}, ${luna == copia}")
Log.d("Pregunta", "Descripción: '${luna.descripcion}'")
```

**Respuesta.**

```
Luna, 3 -> Luna, 4
Iguales: true, false
Descripción: ''
```

`copy(edad = 4)` crea otro `Animal` igual pero con otra edad; `luna` no cambia. En una `data class`, `==` compara los datos: `luna.copy()` tiene los mismos datos y es igual; `copia` tiene otra edad y no. `descripcion` no se ha pasado, así que toma su valor por defecto, `""`.

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

**Respuesta.**

```
Urgentes: 2
id 7: Desconocido
Edad del id 7: null
```

`filter` devuelve una lista nueva con los que cumplen la condición (Luna y Kira). `find` devuelve el primero que la cumple o `null` si no hay ninguno: por eso `buscado` es `Animal?`. `?.` (llamada segura) devuelve `null` en lugar de dar error si `buscado` es `null`, y `?:` (Elvis) da un valor por si lo de la izquierda es `null`.

**11.** En la lista se escribe `items(uiState.animalesVisibles, key = { it.id })`. ¿Para qué sirve `key`? ¿Qué pasa si dos animales tienen el mismo id?

**Respuesta.** `key` identifica cada elemento de la lista por su id. Así, cuando la lista cambia (al filtrar los urgentes), Compose sabe qué elemento es cada uno y no los confunde. Si dos animales tienen el mismo id, la app se cierra: las claves tienen que ser únicas.

**12.** En `AcogidaViewModel`, ¿por qué hay dos variables, `_uiState` (`MutableStateFlow`, privada) y `uiState` (`StateFlow`, pública)? ¿Qué hace `_uiState.update { it.copy(soloUrgentes = valor) }`?

**Respuesta.** Para que solo el ViewModel pueda cambiar el estado: `_uiState` es modificable, pero privado; `uiState` es la misma información, pública y de solo lectura, y es la que leen las pantallas. `update { it.copy(soloUrgentes = valor) }` toma el estado actual (`it`), crea una copia con `soloUrgentes` cambiado y la pone como estado nuevo; quien lo esté leyendo recibe el valor nuevo.

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

**Respuesta.**

```
Visibles: 2 y 1
Solo urgentes: false y true
Animales: 2 y 2
```

`animalesVisibles` se calcula cada vez a partir de `animales` y `soloUrgentes`: sin el filtro se ven los dos; con él, solo Luna. `copy` cambia solo `soloUrgentes`: la lista `animales` es la misma en los dos estados. Filtrar no borra animales, solo cambia los que se ven.

**14.** Las pantallas (`PantallaLista`, `PantallaDetalle`, `PantallaSolicitud`) no reciben el ViewModel ni el `navController`: reciben el estado y lambdas. ¿Qué es el flujo unidireccional de datos? ¿Qué se gana haciéndolo así?

**Respuesta.** El estado baja (del ViewModel a las pantallas) y los eventos suben (las pantallas avisan con lambdas como `onCambiarUrgentes` o `onAnimalClick`, y es el ViewModel o el `NavHost` quien decide qué hacer). Las pantallas solo muestran lo que reciben. Así cada cosa está en un solo sitio (el estado en el ViewModel, la navegación en `AcogidaApp`), y las pantallas se pueden previsualizar y probar por separado, pasándoles un estado cualquiera.

**15.** La lista navega con `navController.navigate("detalle/${animal.id}")` y el destino tiene la ruta `"detalle/{animalId}"`. ¿Cómo llega el id del animal a `PantallaDetalle`? ¿Qué se ve si llega un id que no existe?

**Respuesta.** Al pulsar la tarjeta, se navega a, por ejemplo, `"detalle/2"`. El destino `"detalle/{animalId}"` recoge ese `2` en el argumento `animalId`, de tipo `Int`. En el destino, `entrada.arguments?.getInt("animalId")` lo lee (si no hay, `-1`) y `viewModel.buscarAnimal(id)` busca el animal. Si el id no existe, `buscarAnimal` devuelve `null` y `PantallaDetalle` muestra «Animal no encontrado» en la barra y «No se ha encontrado el animal.»: como `animal` es `Animal?`, el código tiene que tratar ese caso y no se cierra.

**16.** En la lista pulsas en Misi y, en su detalle, «Solicitar adopción». ¿Qué pantallas hay en la pila de navegación? ¿A dónde lleva la flecha de «Volver» (`popBackStack()`)? ¿Y el botón «Volver a la lista» (`popBackStack("lista", inclusive = false)`)?

**Respuesta.** Lista, detalle de Misi y solicitud (abajo, la lista; arriba, la solicitud). `popBackStack()` quita la de arriba: vuelve al detalle de Misi. `popBackStack("lista", inclusive = false)` quita todas las que hay encima de la lista, pero no la lista (`inclusive = false`): vuelve a la lista.

**17.** Pega este código en `MainActivity.kt`, dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)` (borra antes el código de la pregunta anterior). Ejecuta la app y copia los mensajes de Logcat con el filtro `tag:Pregunta`.

```kotlin
val form = FormularioUiState(nombre = "Ana", email = "ana@correo", telefono = "612345678")
Log.d("Pregunta", "Nombre: ${form.errorNombre}")
Log.d("Pregunta", "Correo: ${form.errorEmail}")
Log.d("Pregunta", "Teléfono: ${form.errorTelefono}")
Log.d("Pregunta", "Condiciones: ${form.errorCondiciones}")
Log.d("Pregunta", "Válido: ${form.esValido}")
```

**Respuesta.**

```
Nombre: null
Correo: Correo no válido
Teléfono: null
Condiciones: Debes aceptar las condiciones
Válido: false
```

Cada `error…` es `null` si el campo está bien y, si no, el texto del error. `ana@correo` no tiene un punto después de la `@`, así que no cumple la expresión regular del correo. `aceptaCondiciones` no se ha pasado y vale `false`. `esValido` solo es `true` si todos los errores son `null`.

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

**Respuesta.**

```
1: 612
2: 612
3: true false
4: '' false
```

`cambiarTelefono` solo guarda el valor si son todo cifras y como mucho 9: `"612"` se guarda; `"612a"` (con una letra) y `"6123456789"` (10 cifras) no, y el teléfono sigue en `612`. `enviarSolicitud` marca `intentadoEnviar` y, como el formulario no es válido, `enviado` queda en `false`. `reiniciarFormulario` vuelve al formulario vacío del principio. (Aquí el ViewModel se crea directamente solo para probar sus funciones; en la app se crea con `viewModel()`, que es lo que hace que sobreviva al giro.)

**19.** Cuenta, paso a paso, qué pasa desde que escribes una cifra en el campo «Teléfono» hasta que aparece en pantalla. Di qué función de qué archivo se ejecuta en cada paso. ¿Por qué no se puede escribir una letra?

**Respuesta.**

1. Al escribir, el `OutlinedTextField` del teléfono (`PantallaSolicitud.kt`) llama a su `onValueChange` con el texto nuevo.
2. Esa lambda es `onTelefonoChange`, que en `AcogidaApp.kt` está conectada a `viewModel::cambiarTelefono`.
3. `cambiarTelefono` (`AcogidaViewModel.kt`) comprueba que son cifras y como mucho 9, y actualiza `_formulario` con `copy(telefono = valor)`.
4. `AcogidaApp` recoge el estado nuevo con `collectAsState()` y Compose recompone.
5. `PantallaSolicitud` recibe el `form` nuevo y el campo muestra `form.telefono`.

El campo no guarda nada por sí mismo: muestra lo que hay en el estado. Si se escribe una letra, `cambiarTelefono` no cambia el estado y el campo sigue mostrando el teléfono anterior.

**20.** Cuenta, paso a paso, qué pasa al pulsar «Enviar» con el formulario vacío: qué función se ejecuta, qué cambia en el estado y por qué ahora sí se ven los errores. ¿Para qué se llama a `viewModel.reiniciarFormulario()` antes de abrir el formulario?

**Respuesta.** «Enviar» llama a `onEnviar`, conectado a `viewModel.enviarSolicitud()`, que pone `intentadoEnviar = true` y `enviado = esValido` (falso, porque todo está vacío). Con el estado nuevo, `PantallaSolicitud` se recompone y, como ya se intentó enviar, sus variables `errorNombre`, `errorEmail`… toman los errores del estado: los campos salen en rojo con su mensaje. Antes de pulsar, esas variables eran `null`, por eso no se veía ningún error. `reiniciarFormulario()` se llama antes de navegar porque el ViewModel es el mismo para toda la app: sin reiniciarlo, al abrir el formulario de otro animal saldrían los datos (y la confirmación) del anterior.

## Cuestiones sobre los apuntes

**21.** Di cuatro limitaciones de un móvil frente a un ordenador y cómo afecta cada una a cómo se programa una app.

**Respuesta.** *(UD1, apartado 1)* Por ejemplo: la batería (evitar trabajo innecesario en segundo plano); la pantalla pequeña y de muchos tamaños (adaptar la interfaz); poca memoria (el sistema puede cerrar la app en segundo plano); la red, que puede faltar (funcionar sin conexión o avisar); y que al girar se recrea la pantalla (guardar el estado).

**22.** ¿En qué se diferencian una app nativa, una multiplataforma y una web (PWA)? Di una tecnología de cada una.

**Respuesta.** *(UD1, apartado 2)* La nativa se hace con las herramientas de cada sistema (Kotlin y Compose en Android, Swift en iOS): mejor rendimiento y acceso a todo el dispositivo. La multiplataforma usa un solo código para Android e iOS (Flutter, React Native, Kotlin Multiplatform). La web o PWA es una web adaptada al móvil que se puede instalar, con acceso limitado al dispositivo.

**23.** ¿Qué capas tiene Android? ¿Qué es el nivel de API? ¿Qué diferencia hay entre `targetSdk` y `compileSdk`?

**Respuesta.** *(UD1, apartado 3)* El kernel Linux, las librerías y el entorno de ejecución ART, el framework de Android y las apps. El nivel de API es el número de cada versión de Android. `targetSdk` es la versión para la que está pensada y probada la app; `compileSdk`, la versión con la que se compila.

**24.** ¿Qué son los **sp** y en qué se diferencian de los dp? ¿Para qué sirven carpetas de recursos como `values-es` o `values-night`?

**Respuesta.** *(UD1, apartado 7)* Los sp son la unidad de los textos: como los dp, miden lo mismo en cualquier pantalla, pero además respetan el tamaño de letra que elige la persona en los ajustes. Las carpetas con sufijo son recursos alternativos que Android elige solo: `values-es`, los textos en español; `values-night`, los colores del modo oscuro.

**25.** ¿Qué hay en `AndroidManifest.xml` y qué en la carpeta `res`? ¿Cómo se usa desde el código una imagen de `res/drawable`?

**Respuesta.** *(UD1, apartado 5)* El manifiesto tiene la identidad de la app: su nombre, su actividad principal y los permisos. `res` tiene los recursos: imágenes, textos, colores e iconos. Una imagen se usa con `R.drawable.nombre`, por ejemplo en `painterResource(R.drawable.foto)`.

**26.** ¿Qué hace `!!` en Kotlin y por qué conviene evitarlo? ¿Qué se pierde al girar el móvil si se guarda con `remember`, y qué no se pierde con `rememberSaveable` o con un ViewModel?

**Respuesta.** *(UD2, apartados 1 y 3)* `!!` obliga a tratar un valor como no nulo: si es `null`, la app se cierra con un error. Mejor `?.` y `?:`. Con `remember` el valor se pierde al girar, porque la actividad se recrea; con `rememberSaveable` y con un ViewModel, no.

