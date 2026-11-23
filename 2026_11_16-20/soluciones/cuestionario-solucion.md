# Autoevaluación · semana del 16 al 20 de noviembre · soluciones

Entre paréntesis, dónde está la respuesta para repasarla. El proyecto con la 3.ª parte terminada está en la carpeta `CentroAcogida`.

## Cuestiones sobre el proyecto

**1.** En el paso 11, ¿a qué dirección completa hace la petición `@GET("animales.json")`? ¿Por qué la función es `suspend` y desde dónde se llama? Si no hay conexión, ¿qué hace la app y por qué no se cierra? ¿Para qué sirve el bloque `finally`?

**Respuesta.** *(proyecto, paso 11; apuntes de la UD3, apartado 1)* A `https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/2026_11_16-20/actividades/animales.json`: la **URL base** del objeto `Retrofit` más la ruta de `@GET`. Es `suspend` porque la petición tarda: puede pausarse mientras espera la respuesta sin bloquear el hilo principal. Se llama desde una **corrutina** del ViewModel (`viewModelScope.launch` en `cargarAnimales()`); en el hilo principal, Android lanzaría `NetworkOnMainThreadException`. Sin conexión, la petición lanza una excepción; el `catch` la recoge, escribe el error en Logcat y pone el aviso «No se ha podido actualizar la lista: se muestran los animales guardados en la app», y la lista sigue con los animales que ya tenía: por eso no se cierra. El `finally` se ejecuta siempre, haya ido bien o mal, y quita la barra de progreso (`cargando = false`).

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

**Respuesta.** *(proyecto, paso 11.3; apuntes de la UD3, apartado 1)* Los mensajes:

```
nombre=Bimba, edad=2, urgente=true
descripcion=[]
imagen por defecto: true
{"id":11,"nombre":"Bimba","especie":"Perro","edad":2,"urgente":true}
Sin ignoreUnknownKeys: JsonDecodingException
```

La descripción no viene en el JSON y `descripcion` tiene valor por defecto (`""`): se usa ese. La imagen es `@Transient`: nunca se lee del JSON, y siempre se usa su valor por defecto. `ignoreUnknownKeys = true` hace que los campos del JSON que `Animal` no tiene (`"chip"`) se ignoren; sin él, la conversión falla con `JsonDecodingException`. Al convertir a JSON, por defecto no se escriben las propiedades que tienen su valor por defecto: `descripcion` vale `""` y no aparece (`imagen`, por ser `@Transient`, tampoco).

**3.** En el paso 12, ¿qué calcula `sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH` y cuánto vale con el móvil quieto? ¿Para qué sirven `UMBRAL` y `ESPERA_MS`? ¿Por qué el sensor se quita en `onDispose`? ¿Por qué hay, además, un botón de recargar?

**Respuesta.** *(proyecto, paso 12; apuntes de la UD3, apartado 3)* El **módulo** de la aceleración en «gravedades»; quieto vale **1** (solo la gravedad). `UMBRAL` (2,7) es a partir de cuánto se considera una sacudida, para que mover el móvil normalmente no recargue; `ESPERA_MS` (1 segundo) evita contar varias sacudidas por un solo gesto. Se quita en `onDispose`, al salir de la pantalla, porque un sensor escuchando sin necesidad **gasta batería**. El botón es la **alternativa**: no todo el mundo puede agitar el móvil, no todos los dispositivos tienen acelerómetro, y en el emulador no se puede agitar.

**4.** En el paso 13, ¿por qué los permisos de ubicación se declaran en el manifiesto **y** se piden mientras se usa la app, y el de Internet no? ¿Por qué se piden a la vez la ubicación precisa y la aproximada? ¿Qué hace la app si la persona deniega el permiso?

**Respuesta.** *(proyecto, pasos 11.2 y 13; apuntes de la UD3, apartado 4, y apuntes de la semana del 9 al 13, apartado 4)* La ubicación es un permiso **peligroso**: revela dónde está la persona, así que además de declararlo hay que pedirlo mientras se usa la app, cuando hace falta, y la persona decide. Internet es un permiso **normal**: se concede al instalar. Se piden las dos porque desde Android 12 la persona puede elegir dar solo la aproximada; para calcular la distancia al centro basta con ella. Si se deniega, la app no se cierra: muestra «Sin permiso de ubicación no se puede calcular la distancia.» y todo lo demás funciona.

**5.** En `Multimedia.kt` (paso 14), ¿qué función **procesa** la foto y cuál la **convierte**? ¿Qué cambiaría en el archivo guardado si `CALIDAD` valiera 30 en lugar de 80? ¿Por qué el reproductor se libera con `release()`? ¿Por qué el sonido va dentro de `LaunchedEffect(form.enviado)`?

**Respuesta.** *(proyecto, paso 14; apuntes de la UD4, apartados 2 y 3)* **Procesa** `recortarCuadrado` (cambia el contenido: recorta y escala a 300 × 300); **convierte** `guardarComoJpeg` (cambia el formato: de `Bitmap` a un archivo JPEG con `compress`). Con calidad 30 el archivo ocuparía menos, pero se notaría más la pérdida (bloques, bordes borrosos). `release()` libera el reproductor y el audio del sistema; si no, cada sonido dejaría un reproductor ocupando memoria. `LaunchedEffect(form.enviado)` ejecuta el bloque solo cuando cambia `form.enviado`: el sonido suena una vez, al enviar, y no cada vez que la pantalla se vuelve a dibujar (por ejemplo, al escribir).

**6.** En el paso 15, ¿qué diferencia hay entre los mensajes de `Log.d` y `Log.e`, y cómo se ven solo los de la app en Logcat? ¿Qué es el APK y en qué se diferencia una versión *debug* de una *release*? ¿Para quién se escribe cada parte de `DOCUMENTACION.md`?

**Respuesta.** *(proyecto, paso 15; apuntes de la UD4, apartado 6)* `Log.d` es de **depuración** (información para quien programa); `Log.e`, de **error** (y aquí lleva la excepción). Se filtran por su etiqueta, `tag~:^CentroAcogida$`. El APK es el archivo que se instala en un móvil Android. La *debug* es para probar y va firmada con una clave de prueba; la *release* es para publicar, va optimizada y firmada con la clave propia (para Google Play se genera un AAB). «Qué hace» y «Cómo se usa» son para quien **usa** la app; «Cómo se compila e instala», «Permisos» y «Pruebas», para quien la **mantiene** o la revisa.

## Cuestiones sobre los apuntes

**7.** Para cada caso, ¿usarías una corrutina, un servicio de primer plano o WorkManager? Razónalo. (a) Cargar la lista de animales al abrir la pantalla. (b) Recordar mañana a la persona que tiene una solicitud pendiente, aunque cierre la app. (c) Una audioguía del centro que sigue sonando con la pantalla apagada.

**Respuesta.** *(apuntes de la UD3, apartado 2)* (a) Una **corrutina**: es mientras se usa la pantalla, y si se sale, no hace falta terminarla. (b) **WorkManager**: tiene que hacerse aunque se cierre la app o se reinicie el móvil, y no importa el momento exacto. (c) Un **servicio de primer plano**: es algo largo que la persona sabe que sigue, con su notificación visible, como un reproductor de música.

**8.** El centro quiere dos cosas: una **etiqueta** en cada jaula que, al acercar el móvil, abra la ficha del animal, y una **báscula** que envíe el peso de cada animal al móvil. ¿Qué tecnología inalámbrica usarías para cada una y por qué? ¿Qué permisos pide Bluetooth desde Android 12?

**Respuesta.** *(apuntes de la UD3, apartado 5)* La etiqueta, **NFC**: basta con acercar el móvil (unos 4 cm), sin emparejar, y la etiqueta no necesita batería. La báscula, **Bluetooth**, mejor de **baja energía** (BLE): envía pocos datos de vez en cuando con poco consumo, a unos metros, y se empareja una vez. Desde Android 12, `BLUETOOTH_SCAN` (buscar dispositivos) y `BLUETOOTH_CONNECT` (conectarse), que se piden mientras se usa la app.

**9.** ¿En qué formato guardarías la foto de un animal, y en cuál el icono de la app con fondo transparente? ¿Por qué? ¿Qué diferencia hay entre el **contenedor** y el **códec** de un vídeo? ¿Cuándo se usa `SoundPool` y cuándo `MediaPlayer`?

**Respuesta.** *(apuntes de la UD4, apartados 1 y 3)* La foto, en **JPEG** (o WebP con pérdida): es una foto y la compresión con pérdida apenas se nota y ocupa mucho menos. El icono, en **PNG** (o WebP sin pérdida): admite transparencia y mantiene los bordes nítidos. El **contenedor** es el archivo que junta las pistas (MP4, WebM); el **códec**, cómo está comprimida cada pista (H.264 para el vídeo, AAC para el audio). `SoundPool`, para efectos cortos que tienen que sonar al instante, varias veces y a la vez; `MediaPlayer` (o Media3), para audio largo y vídeo.

**10.** ¿En qué se diferencia una app nativa de una multiplataforma? ¿Qué es un *widget* en Flutter y qué hace `setState`? ¿Qué elegirías para una app solo para Android que usa sensores y la cámara, y qué para una app sencilla que tiene que estar en Android y en iPhone con un equipo pequeño?

**Respuesta.** *(apuntes de la UD1, multiplataforma)* La **nativa** se escribe para un sistema, con su lenguaje (Kotlin en Android), y accede a todo lo del sistema con el mejor rendimiento; la **multiplataforma** se escribe una vez para varios sistemas, con menos coste, pero lo específico de cada sistema llega más tarde o hay que hacerlo aparte. En Flutter, un *widget* es cada elemento de la interfaz (texto, botón, fila, la pantalla), anidados unos dentro de otros; `setState` cambia el estado de un `StatefulWidget` y hace que se vuelva a dibujar, como la recomposición de Compose. Solo Android con sensores y cámara: **nativa** (Kotlin y Compose). Android e iPhone con un equipo pequeño: **multiplataforma**, por ejemplo **Flutter**.

