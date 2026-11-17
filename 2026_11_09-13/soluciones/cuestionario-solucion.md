# Cuestionario C2 · semana del 9 al 13 de noviembre · soluciones

## Cuestiones sobre el proyecto

**1.** Ejecutándola en dos emuladores, un móvil y una tableta (o en el móvil en vertical y en horizontal), y comparando las capturas. La vista previa `ListaTabletaPreview` (`@Preview`) dibuja la lista con el tamaño de una tableta sin ejecutar la app: `widthDp = 840` le dice que la pantalla tiene 840 dp de ancho, así que se ve la cuadrícula con varias columnas.

**2.** Es una cuadrícula con desplazamiento vertical. `GridCells.Adaptive(minSize = 320.dp)` mete tantas columnas como quepan en el ancho disponible, cada una de al menos 320 dp: en el móvil en vertical (unos 400 dp de ancho) cabe una; en la tableta, dos o tres, según la orientación. No hace falta ningún `if` porque la cuadrícula calcula las columnas sola con el ancho que tiene en cada momento: también al girar.

**3.** *Density-independent pixels*, píxeles independientes de la densidad. Un dp mide lo mismo a la vista en cualquier pantalla: Android lo convierte en más o menos píxeles según la densidad de cada una. Con píxeles, lo mismo se vería grande en una pantalla de poca resolución y diminuto en una de mucha.

**4.** Limita el ancho de la columna a 600 dp como máximo. En el móvil no cambia nada, porque la pantalla es más estrecha; en la tableta, sin él, los textos y los campos ocuparían todo el ancho, con líneas larguísimas y difíciles de leer.

**5.** KSP (*Kotlin Symbol Processing*) lee el código al compilar y genera código nuevo. Room lo usa para escribir, a partir de las anotaciones (`@Entity`, `@Dao`, `@Database`), el código que de verdad abre la base de datos y hace las consultas.

- En `libs.versions.toml`: las versiones de `room` y `ksp`; las tres bibliotecas de Room (`room-runtime`, `room-ktx` y `room-compiler`); y el plugin `ksp`.
- En el `build.gradle.kts` del proyecto: `alias(libs.plugins.ksp) apply false`.
- En el de `app`: `alias(libs.plugins.ksp)` en `plugins` y, en `dependencies`, `implementation` de `room-runtime` y `room-ktx` y `ksp(libs.androidx.room.compiler)`.

**6.** En el proyecto se declara el plugin y su versión, pero no se aplica (`apply false`): el proyecto raíz no tiene código. Se aplica en el módulo `app`, que es donde está el código que KSP tiene que procesar.

**7.** Room es la biblioteca recomendada para usar una base de datos SQLite en Android.

- La **entidad**, `@Entity`, es una tabla: `Solicitud.kt`.
- El **DAO**, `@Dao`, tiene las consultas: `SolicitudDao.kt`.
- La **base de datos**, `@Database`, une las dos: `AcogidaDatabase.kt`.

**8.**

```
id: 0
Tiene fecha: true
Misi, Luis, id 0
Misma fecha: true
```

Solo se han dado los datos obligatorios. `id` toma su valor por defecto, `0`, y `fecha`, el momento en que se creó el objeto (`System.currentTimeMillis()`). `copy` copia todos los campos menos el que se cambia: la copia tiene el mismo animal, el mismo id y la misma fecha, y otro nombre. Todavía no se ha guardado nada: es solo un objeto.

**9.** `autoGenerate = true` hace que sea la base de datos quien ponga el id al insertar la fila: 1, 2, 3… El `= 0` es el valor por defecto, para poder crear una `Solicitud` sin inventarse un id; con `autoGenerate`, Room entiende el 0 como «sin id todavía» y pone el siguiente. Así los ids nunca se repiten.

**10.** `insertar` hace un trabajo que tarda y termina: es `suspend` para que se llame dentro de una corrutina y no bloquee la interfaz. `todas()` no hace la consulta en ese momento: devuelve un `Flow`, que va dando la lista cada vez que cambia la tabla. Así, la pantalla «Mis solicitudes» se actualiza sola al guardar una solicitud, sin volver a pedirla.

**11.** Porque `instancia` es una sola para toda la app (está en el `companion object`): la primera vez se crea y las demás se devuelve la misma. `synchronized` evita que dos hilos la creen a la vez. Si cada pantalla creara la suya, habría varias conexiones abiertas al mismo archivo, gastando memoria, y lo que guarda una podría no avisar al `Flow` de otra.

**12.** Porque para abrir la base de datos hace falta un `Context`, y `AndroidViewModel` recibe la `Application`, que lo es. Un `ViewModel` normal no tiene contexto. No hubo que cambiar nada: `viewModel()` sabe crear un `AndroidViewModel` y le pasa la `Application` solo.

**13.**

```
Antes de launch
Empieza la corrutina
Después de launch
Guardadas: 2, ids: [1, 2]
```

`launch` empieza la corrutina en ese momento: se ve «Empieza la corrutina». Al llegar a `insertar`, que es `suspend`, la corrutina se suspende mientras Room trabaja en otro hilo, y `onCreate` sigue: «Después de launch». Cuando terminan las inserciones, la corrutina continúa, lee la lista con `first()` (el primer valor del `Flow`) y escribe el último mensaje. Los ids 1 y 2 los ha puesto la base de datos (`autoGenerate`).

**14.** Porque acceder a la base de datos puede tardar, y en el hilo de la interfaz la app se quedaría congelada mientras tanto. `viewModelScope.launch` lanza una corrutina ligada al ViewModel: no bloquea la interfaz y se cancela sola si el ViewModel se destruye. Si se accede en el hilo principal, Room lanza el error *Cannot access database on the main thread*.

**15.** Convierte el `Flow` de Room en un `StateFlow`, que siempre tiene un valor y que la interfaz lee con `collectAsState()`, como `uiState`. `WhileSubscribed(5_000)` hace que escuche a la base de datos mientras alguna pantalla lo está leyendo, y 5 segundos más (para no cortar y volver a empezar al girar la pantalla). `emptyList()` es el valor inicial, el que tiene antes de que llegue la primera lista de Room.

**16.**

1. «Enviar» llama a `onEnviar`, que en `AcogidaApp.kt` es `{ viewModel.enviarSolicitud(animal) }`.
2. `enviarSolicitud` (`AcogidaViewModel.kt`) marca el formulario como enviado y, como es válido y hay animal, lanza una corrutina con `viewModelScope.launch`.
3. En ella, `dao.insertar(Solicitud(…))` (`SolicitudDao.kt`) guarda la fila en la tabla `solicitudes`.
4. Como la tabla ha cambiado, el `Flow` de `dao.todas()` da la lista nueva, y `stateIn` la pone en `solicitudes`.
5. `AcogidaApp` la recoge con `collectAsState()` y se la pasa a `PantallaSolicitudes`, que se recompone y la muestra.

**17.** Porque para guardar la solicitud hace falta saber de qué animal es (`animalId` y `animalNombre`), y el formulario solo tiene los datos de la persona. En `AcogidaApp.kt`, en el destino `solicitud/{animalId}`: `val animal = viewModel.buscarAnimal(id)`, `animal = animal` en `PantallaSolicitud(…)` y `onEnviar = { viewModel.enviarSolicitud(animal) }`. Si el animal es `null`, el formulario se marca como enviado pero no se guarda nada: el `if` solo inserta si `animal != null`.

**18.** Con el botón «Mis solicitudes» de la barra superior de la lista: `onVerSolicitudes` hace `navController.navigate("solicitudes")`, una ruta nueva del `NavHost`. Se vuelve con la flecha (`popBackStack()`) o con el botón Atrás del sistema. El valor por defecto (`= {}`) hace que quien llama a `PantallaLista` sin ese parámetro (la vista previa, por ejemplo) siga compilando: si no se pasa, el botón no hace nada.

**19.** Las solicitudes están en un archivo de la base de datos (`acogida.db`), en la memoria del móvil, que sigue ahí aunque se cierre la app. El interruptor está en el ViewModel, en memoria: sobrevive al giro, pero no a cerrar la app. Con la base de datos solo habla el ViewModel, a través del DAO; las pantallas reciben listas y estados. Si los animales vinieran de un servicio web, cambiaría solo la parte que obtiene los datos (el ViewModel y lo que use para pedirlos, por ejemplo con Retrofit): las pantallas seguirían recibiendo la misma lista y no habría que tocarlas.

**20.** En `AndroidManifest.xml`, con `<uses-permission android:name="android.permission.CAMERA" />`. La cámara es un permiso peligroso: declararlo no basta, porque es la persona quien decide si lo da. Por eso se pide mientras se usa la app, con el diálogo del sistema (`RequestPermission`), justo cuando hace falta. Si lo deniegas, la app no abre la cámara y muestra el aviso «Sin permiso de cámara no se puede hacer la foto.»; el resto de la app sigue funcionando.

**21.** El primer `filter` deja solo los urgentes si el interruptor está activado (`!soloUrgentes || it.urgente`), y el segundo, los que contienen el texto sin distinguir mayúsculas: así se aplican los dos filtros a la vez. Con el texto vacío, `contains("")` es verdadero para todos y no se quita ninguno. Al escribir, el `OutlinedTextField` llama a `onValueChange` con el texto nuevo; es `onBuscar`, que en `AcogidaApp.kt` es `viewModel::cambiarBusqueda`; `cambiarBusqueda` copia el estado con el texto nuevo (`_uiState.update { it.copy(textoBusqueda = texto) }`); `uiState` cambia, Compose vuelve a dibujar la pantalla y `animalesVisibles` da la lista filtrada. Es el flujo unidireccional de datos: el estado baja a la pantalla y los eventos suben al ViewModel.

## Cuestiones sobre los apuntes

**22.** *(UD3, apartado 1)* DataStore, para unos pocos valores sueltos de clave y valor (el último filtro elegido, el modo oscuro); Room, para datos con estructura y muchos registros que se consultan (las solicitudes de adopción).

**23.** *(UD3, apartado 4)* Los dos se declaran en el manifiesto; el normal (`INTERNET`) se concede solo al instalar y el peligroso (`CAMERA`, `ACCESS_FINE_LOCATION`) se pide además mientras se usa la app. La persona puede concederlo, concederlo solo esta vez o denegarlo.

**24.** *(UD3, apartado 5)* El permiso `INTERNET` y una librería como Retrofit, que hace las peticiones a la API (normalmente en JSON). HTTPS porque va cifrado. El APK es el paquete que se instala; el AAB, el que se sube a Google Play. La de depuración (*debug*) es para probar; la de publicación (*release*) va firmada con una clave.
