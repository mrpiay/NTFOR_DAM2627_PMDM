# UD4 · Aplicaciones multimedia

Lo básico de la UD4. Cada apartado tiene la idea clave, lo que tienes que saber y, cuando hay conceptos que se confunden, una tabla para compararlos. Los ejemplos son de la app del Centro de acogida del proyecto. Las preguntas para comprobar que lo entiendes están en el cuestionario de la semana, en «Cuestiones sobre los apuntes».

Los apartados 2, 3, 5 y 6 se practican en el proyecto (pasos 14 y 15). Los apartados 1 y 4 se estudian solo aquí: en el examen, las preguntas sobre ellos son de conceptos y criterio, no de código.

---

## 1. Lo multimedia en Android: formatos y librerías

**En una frase:** imagen, audio y vídeo se guardan en **formatos** que comprimen de distinta manera, y Android trae librerías para capturarlos, procesarlos y reproducirlos.

**Lo que tienes que saber**

- **Comprimir con pérdida o sin pérdida:**
  - **con pérdida**: se descarta información que apenas se nota; ocupa mucho menos (JPEG, MP3, AAC, H.264);
  - **sin pérdida**: se puede recuperar el original exacto; ocupa más (PNG, FLAC, WAV sin comprimir).
- **Contenedor y códec** (en audio y vídeo): el **contenedor** es el archivo que lo junta todo (MP4, WebM, MKV); el **códec** es cómo está comprimida cada pista (H.264 o H.265 para el vídeo, AAC para el audio). Un MP4 puede no reproducirse si su códec no está en el móvil.
- **Las librerías de Android:**
  - **imagen:** `Bitmap` y `BitmapFactory` (cargar, cambiar y guardar imágenes); **CameraX** (cámara con vista previa, foto y vídeo); el contrato `TakePicture` de la cámara del sistema;
  - **audio y vídeo:** `SoundPool`, `MediaPlayer`, `MediaRecorder` y **Media3** (ExoPlayer), la librería actual de Google para reproducir;
  - **galería compartida:** **`MediaStore`**, donde se guarda lo que otras apps pueden ver (la galería de fotos).
- **Dónde va cada archivo en el proyecto:** las imágenes, en `res/drawable`; los sonidos y vídeos cortos, en **`res/raw`** (se usan con `R.raw.nombre`); los archivos que se leen con su nombre y carpetas, en `assets`.

**Compara: formatos de imagen**

| Formato | Compresión | Transparencia | Para qué |
|---|---|---|---|
| **JPEG** | Con pérdida; se elige la **calidad** (0–100) | No | Fotos |
| **PNG** | Sin pérdida | Sí | Iconos, capturas, dibujos con bordes nítidos |
| **WebP** | Con o sin pérdida | Sí | Lo recomendado en Android: ocupa menos que los dos anteriores |

---

## 2. Imágenes: capturar, procesar y convertir

**En una frase:** una imagen en memoria es un `Bitmap`, una cuadrícula de píxeles; se puede reducir, girar o recortar y guardar en otro formato.

**Lo que tienes que saber**

- **Capturar:** con la cámara del sistema (`TakePicturePreview` devuelve una foto pequeña; `TakePicture` guarda la foto entera en un archivo) o con **CameraX**, que da vista previa dentro de la app.
- **El tamaño importa:** una foto de 12 megapíxeles ocupa en memoria unos 48 MB (4 bytes por píxel). Mostrarla tal cual en una tarjeta pequeña gasta memoria y puede cerrar la app (`OutOfMemoryError`). Por eso se **reduce** antes de guardarla o mostrarla.
- **Procesar** (cambiar el contenido): `Bitmap.createScaledBitmap(foto, ancho, alto, true)` la **escala**; con una `Matrix` se gira; con `Canvas` y `Paint` se dibuja encima (un texto, un marco).
- **Convertir** (cambiar de formato): **`bitmap.compress(Bitmap.CompressFormat.JPEG, 80, salida)`** escribe la imagen como JPEG con calidad 80 en un flujo de salida (un archivo). Con `PNG` o `WEBP_LOSSY` se convierte a otro formato. Menos calidad, archivo más pequeño y más pérdida.
- **Cargar:** `BitmapFactory.decodeFile(ruta)` (o `decodeResource`, `decodeStream`) lee un archivo y devuelve el `Bitmap`; en Compose se muestra con `Image(bitmap = foto.asImageBitmap(), …)`.
- **Dónde guardarla:** en los archivos **propios de la app** (`filesDir`: solo ella los ve y se borran al desinstalarla) o en **`MediaStore`** (la galería: los ven otras apps).

**Compara**

| | Procesar | Convertir |
|---|---|---|
| Qué cambia | El contenido: tamaño, giro, recorte, filtros | El formato del archivo |
| Ejemplo | Reducir la foto a 800 píxeles de ancho | Guardarla como JPEG de calidad 80 |

---

## 3. Audio

**En una frase:** los sonidos cortos se reproducen con `SoundPool`; la música y el audio largo, con `MediaPlayer` o Media3.

**Lo que tienes que saber**

- **`SoundPool`:** para **efectos cortos** (un clic, un aviso) que tienen que sonar **al instante**, varias veces y a la vez. Se cargan antes en memoria (`load`) y se lanzan con `play`. Archivos pequeños (hasta 1 MB).
- **`MediaPlayer`:** para **audio largo** (música, locuciones) y vídeo. Tiene un **ciclo de vida**: se crea con su fuente (`MediaPlayer.create(contexto, R.raw.ladrido)`), se prepara, `start()`, `pause()`, `stop()` y, al terminar, **`release()`** para liberar el reproductor. Si no se libera, sigue ocupando memoria y el audio del sistema.
- **Media3 (ExoPlayer):** la librería actual para reproducir audio y vídeo, también por *streaming* (desde Internet, mientras se descarga). Es la recomendada para reproductores completos.
- **Grabar:** `MediaRecorder` graba del micrófono; necesita el permiso `RECORD_AUDIO`.
- **Eventos y errores:** al reproducir se escuchan eventos, como **terminado** (`setOnCompletionListener`, para liberar o pasar a la siguiente) y **error** (`setOnErrorListener`, para avisar sin que la app se cierre).

**Compara**

| | `SoundPool` | `MediaPlayer` / Media3 |
|---|---|---|
| Para qué | Efectos cortos | Música, audio largo, vídeo |
| Carga | Todo en memoria, antes | Se va leyendo mientras suena |
| Varios a la vez | Sí | Uno por reproductor |
| Ejemplo | El ladrido al adoptar | La música de fondo de un vídeo del centro |

---

## 4. Vídeo

**En una frase:** el vídeo se reproduce con Media3 (ExoPlayer) y se graba con CameraX; necesita más cuidado con la memoria, la batería y los formatos.

**Lo que tienes que saber**

- **Reproducir:** con **Media3**, un `ExoPlayer` y su vista `PlayerView`, que trae los controles (reproducir, pausa, barra de tiempo). En Compose se inserta con `AndroidView`. En el sistema de vistas antiguo había `VideoView` con `MediaController`.
- **La fuente:** un archivo de la app (`res/raw`), un archivo del móvil o una URL (*streaming*, con el permiso `INTERNET`).
- **El ciclo de vida:** se pausa cuando la pantalla deja de verse y se **libera** al salir; si no, sigue sonando o gastando batería.
- **Grabar:** con **CameraX** (caso de uso `VideoCapture`), con los permisos `CAMERA` y `RECORD_AUDIO`.
- **Convertir vídeo** (otro formato, recortar, bajar la resolución): con **Media3 Transformer**.

---

## 5. Animaciones

**En una frase:** una animación cambia una propiedad (tamaño, color, posición, transparencia) poco a poco, en lugar de a saltos; en Compose basta con decir el valor final.

**Lo que tienes que saber**

- **`animate*AsState`** (`animateFloatAsState`, `animateColorAsState`, `animateDpAsState`…): cuando cambia el valor de destino, Compose lo lleva hasta él animando. Ejemplo: la tarjeta de un animal recién adoptado crece un poco (`escala` de 1 a 1,1) y vuelve.
- **`AnimatedVisibility`:** hace aparecer y desaparecer un elemento con una transición (fundido, deslizamiento).
- **`Crossfade`:** cambia de un contenido a otro con un fundido.
- **`rememberInfiniteTransition`:** una animación que se repite sin fin (un latido, un indicador de carga).
- Se controla la **duración** y la **curva** con `animationSpec` (`tween(300)`: 300 ms; `spring()`: con rebote).
- **Con criterio:** una animación tiene que **ayudar** (indicar qué ha cambiado, dar respuesta a un toque), ser corta (entre 150 y 400 ms) y no distraer. Android permite a la persona **quitar las animaciones** en la accesibilidad del sistema.

---

## 6. Depurar, empaquetar y documentar

**En una frase:** antes de entregar una app se busca y se corrige lo que falla, se empaqueta en un archivo instalable y se explica cómo se usa y cómo está hecha.

**Lo que tienes que saber**

- **Logcat:** el registro de mensajes del dispositivo, en Android Studio. La app escribe con `Log.d("CentroAcogida", "Animales cargados: ${lista.size}")`. Cada mensaje tiene una **etiqueta** (para filtrarlos) y un **nivel**: `Log.v` (detalle), `Log.d` (depuración), `Log.i` (información), `Log.w` (aviso), `Log.e` (error). Cuando la app se cierra sola, Logcat muestra la **excepción** y la **línea** donde ocurrió.
- **El depurador:** un **punto de interrupción** (*breakpoint*, clic junto al número de línea) para la app en esa línea; con *Debug* se ve el valor de cada variable y se avanza paso a paso.
- **Empaquetar:**
  - **APK** (*Android Package*): el archivo que se instala en un móvil. *Build → Build App Bundle(s) / APK(s) → Build APK(s)* lo genera en `app/build/outputs/apk/debug/`;
  - **AAB** (*Android App Bundle*): el formato para publicar en Google Play, que genera un APK a medida de cada móvil;
  - **debug** (para probar, firmado con una clave de prueba) y **release** (para publicar, **firmado** con la clave propia, que hay que guardar: sin ella no se puede actualizar la app).
- **Instalar en un móvil real:** activar las **opciones de desarrollador** y la **depuración USB**, conectarlo y ejecutar desde Android Studio; o copiar el APK al móvil y abrirlo, permitiendo instalar apps de esa fuente.
- **Documentar:**
  - para quien **usa** la app: qué hace, cómo se instala y cómo se hace cada tarea;
  - para quien la **mantiene**: un `README` con cómo se compila y su estructura, y comentarios **KDoc** (`/** … */`) en las clases y funciones que usan otros;
  - las **pruebas** hechas: qué se probó, en qué dispositivos y con qué resultado.
