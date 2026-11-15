# C3 · Cuestionario · semana del 16 al 20 de noviembre

Preguntas sobre la 3.ª parte del proyecto «Centro de acogida 2.0» y sobre los apuntes. Puede que sufran modificaciones a lo largo de la semana. Responde poniendo el número de cada pregunta.

En la pregunta con código, abre `MainActivity.kt` (en `app/src/main/java/com/example/centroacogida`) y pega el código dentro de `onCreate`, justo después de `super.onCreate(savedInstanceState)`. Si algo sale en rojo, pon el cursor encima y pulsa **Alt + Intro** para añadir el `import`. Ejecuta la app y, en Logcat, filtra por `tag:Pregunta`. Copia en tu respuesta los mensajes (el texto que va después de `Pregunta`) y asegúrate de que lo entiendes: en las pruebas escritas tendrás que explicarlo sin ayuda de la IA. Cuando termines, borra el código de la pregunta.

Si en una pregunta aparece algo que no has visto, búscalo en «Dónde consultar», en el [README de `apuntes/`](apuntes/README.md). De lo que se estudia solo en los apuntes, las preguntas son de conceptos y criterio, no de código.

---

## Cuestiones sobre el proyecto

**1.** En el paso 11, ¿a qué dirección completa hace la petición `@GET("animales.json")`? ¿Por qué la función es `suspend` y desde dónde se llama? Si no hay conexión, ¿qué hace la app y por qué no se cierra? ¿Para qué sirve el bloque `finally`?

**2.** Pega este código y ejecuta la app (necesita el paso 11 hecho). Copia los mensajes de Logcat. ¿Por qué la descripción sale vacía y la imagen es la de por defecto? ¿Qué hace `ignoreUnknownKeys = true` y qué pasa sin él? ¿Por qué en el último JSON no aparece `descripcion`?

```kotlin
val json = Json { ignoreUnknownKeys = true }
val texto = """{"id": 11, "nombre": "Bimba", "especie": "Perro", "edad": 2, "urgente": true, "chip": "941000123"}"""
val a = json.decodeFromString<Animal>(texto)
Log.d("Pregunta", "nombre=${a.nombre}, edad=${a.edad}, urgente=${a.urgente}")
Log.d("Pregunta", "descripcion=[${a.descripcion}]")
Log.d("Pregunta", "imagen por defecto: ${a.imagen == R.drawable.ic_launcher_foreground}")
Log.d("Pregunta", json.encodeToString(Animal.serializer(), a))
try {
    Json.decodeFromString<Animal>(texto)
} catch (e: Exception) {
    Log.d("Pregunta", "Sin ignoreUnknownKeys: ${e::class.simpleName}")
}
```

**3.** En el paso 12, ¿qué calcula `sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH` y cuánto vale con el móvil quieto? ¿Para qué sirven `UMBRAL` y `ESPERA_MS`? ¿Por qué el sensor se quita en `onDispose`? ¿Por qué hay, además, un botón de recargar?

**4.** En el paso 13, ¿por qué los permisos de ubicación se declaran en el manifiesto **y** se piden mientras se usa la app, y el de Internet no? ¿Por qué se piden a la vez la ubicación precisa y la aproximada? ¿Qué hace la app si la persona deniega el permiso?

**5.** En `Multimedia.kt` (paso 14), ¿qué función **procesa** la foto y cuál la **convierte**? ¿Qué cambiaría en el archivo guardado si `CALIDAD` valiera 30 en lugar de 80? ¿Por qué el reproductor se libera con `release()`? ¿Por qué el sonido va dentro de `LaunchedEffect(form.enviado)`?

**6.** En el paso 15, ¿qué diferencia hay entre los mensajes de `Log.d` y `Log.e`, y cómo se ven solo los de la app en Logcat? ¿Qué es el APK y en qué se diferencia una versión *debug* de una *release*? ¿Para quién se escribe cada parte de `DOCUMENTACION.md`?

## Cuestiones sobre los apuntes

Estas preguntas se responden con los apuntes de esta semana, en `apuntes/`.

**7.** Para cada caso, ¿usarías una corrutina, un servicio de primer plano o WorkManager? Razónalo. (a) Cargar la lista de animales al abrir la pantalla. (b) Recordar mañana a la persona que tiene una solicitud pendiente, aunque cierre la app. (c) Una audioguía del centro que sigue sonando con la pantalla apagada.

**8.** El centro quiere dos cosas: una **etiqueta** en cada jaula que, al acercar el móvil, abra la ficha del animal, y una **báscula** que envíe el peso de cada animal al móvil. ¿Qué tecnología inalámbrica usarías para cada una y por qué? ¿Qué permisos pide Bluetooth desde Android 12?

**9.** ¿En qué formato guardarías la foto de un animal, y en cuál el icono de la app con fondo transparente? ¿Por qué? ¿Qué diferencia hay entre el **contenedor** y el **códec** de un vídeo? ¿Cuándo se usa `SoundPool` y cuándo `MediaPlayer`?

**10.** ¿En qué se diferencia una app nativa de una multiplataforma? ¿Qué es un *widget* en Flutter y qué hace `setState`? ¿Qué elegirías para una app solo para Android que usa sensores y la cámara, y qué para una app sencilla que tiene que estar en Android y en iPhone con un equipo pequeño?
