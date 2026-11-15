# UD3 · Conectividad, sensores y segundo plano

La UD3 se completa esta semana. Cada apartado tiene la idea clave, lo que tienes que saber y, cuando hay conceptos que se confunden, una tabla para compararlos. Los ejemplos son de la app del Centro de acogida del proyecto. Las preguntas para comprobar que lo entiendes están en el cuestionario de la semana, en «Cuestiones sobre los apuntes».

Los apartados 1, 3 y 4 se practican en el proyecto (pasos 11, 12 y 13). Los apartados 2 y 5 se estudian solo aquí: en el examen, las preguntas sobre ellos son de conceptos y criterio, no de código.

Continúan los [apuntes de la UD3 de la semana del 9 al 13](../../2026_11_09-13/apuntes/UD3-persistencia-y-servicios.md) (guardar datos, Room, corrutinas y permisos).

---

## 1. Datos desde Internet: HTTP y Retrofit

**En una frase:** la app pide los datos a un servidor con **HTTP** (*HyperText Transfer Protocol*); el servidor responde, normalmente en **JSON**, y la app convierte esa respuesta en objetos de Kotlin.

**Lo que tienes que saber**

- **La petición:** la app pide una dirección (URL) con un método. **GET** recupera datos (la lista de animales) y **POST** envía datos (una solicitud de adopción). El servidor responde con un **código** (200, todo bien; 404, no existe; 500, error del servidor) y un contenido.
- **Una API REST** (*Representational State Transfer*) es un servicio web que ofrece sus datos así: cada URL es un recurso y se usa con los métodos de HTTP.
- **HTTPS** es HTTP **cifrado** con TLS (*Transport Layer Security*): nadie puede leer ni cambiar los datos por el camino. El servidor demuestra quién es con un **certificado** firmado por una autoridad de certificación en la que confía Android. Desde Android 9, las apps **no pueden usar HTTP sin cifrar** salvo que se configure expresamente: siempre HTTPS.
- **Lo que pide Android:**
  - el permiso **`INTERNET`** en el manifiesto (es normal: se concede al instalar);
  - la petición **fuera del hilo principal**: si se hace en él, la app se congela y Android lanza `NetworkOnMainThreadException`. Con Retrofit, la función es `suspend` y se llama desde una corrutina (`viewModelScope.launch`);
  - tratar el **error**: sin conexión, servidor caído o respuesta inesperada, la app avisa y no se cierra (`try`/`catch`).
- **Retrofit** convierte una API web en una **interfaz de Kotlin**: se declara la función con una anotación y Retrofit escribe el código que hace la petición. Sus piezas:
  - la **interfaz**: `@GET("animales.json") suspend fun animales(): List<Animal>`;
  - el objeto **`Retrofit`**, con la **URL base** (la parte común de todas las direcciones) y un **conversor**, que pasa el JSON a objetos (`Animal`) y al revés. Hay conversores para Gson, Moshi o kotlinx.serialization;
  - la llamada, desde el ViewModel, dentro de una corrutina.
- **JSON** (*JavaScript Object Notation*) es texto con objetos entre `{ }` y listas entre `[ ]`: `{"nombre": "Luna", "especie": "Perro", "edad": 3}`. Los nombres de los campos tienen que coincidir con los de la clase de Kotlin (o indicarse con una anotación).

**Compara: con qué se hace la petición**

| Opción | Qué es | Cuándo se usa |
|---|---|---|
| `HttpURLConnection` | La clase básica de Java (`java.net`): se abre la conexión, se lee el flujo de bytes y se convierte a mano | Para entender qué pasa por debajo. Es lo que traen los libros; mucho código para poca cosa |
| OkHttp | Cliente HTTP de bajo nivel | Retrofit lo usa por debajo; directamente, cuando hace falta controlar cada petición |
| **Retrofit** | Una capa sobre OkHttp que convierte la API en una interfaz | **Lo más habitual en Android nativo**. Es el que se usa en el proyecto |
| Ktor Client | Cliente de JetBrains, todo en Kotlin | En proyectos multiplataforma (Kotlin Multiplatform) |
| Volley | Librería antigua de Google | Solo en código heredado |

**Compara: en qué formato llegan los datos**

| Formato | Cómo es | Dónde se usa |
|---|---|---|
| **JSON** | Texto con objetos y listas; ligero y fácil de leer | La mayoría de las API REST. El del proyecto |
| XML (*eXtensible Markup Language*) | Texto con etiquetas, como HTML; más largo | Servicios antiguos, RSS |
| Protocol Buffers | **Binario**, compacto y rápido; necesita definir el esquema | gRPC y servicios de Google |
| GraphQL | No es un formato, sino una forma de pedir: el cliente dice exactamente qué campos quiere | API que sirven a muchas apps distintas |
| CSV o texto plano | Valores separados por comas o líneas | Datos muy sencillos |

Con Retrofit, cambiar de formato es cambiar el **conversor**: la interfaz no cambia.

---

## 2. Trabajo en segundo plano: hilos, servicios y WorkManager

**En una frase:** lo que tarda va fuera del hilo principal; lo que tiene que seguir aunque se cierre la pantalla o la app se encarga a un servicio o a WorkManager.

**Lo que tienes que saber**

- El **hilo principal** (o hilo de la interfaz) dibuja la pantalla y atiende los toques. Solo él puede tocar la interfaz. Si se bloquea unos segundos, Android muestra el aviso «La aplicación no responde» (ANR, *Application Not Responding*).
- **Cómo se ha hecho a lo largo del tiempo:**
  - **`Thread`**: un hilo nuevo con la tarea larga; para volver a la interfaz, `runOnUiThread` o un `Handler`. Funciona, pero hay que gestionarlo todo a mano.
  - **`AsyncTask`**: lo que traen muchos libros (`doInBackground`, `onPostExecute`). **Está obsoleta** desde Android 11.
  - **Corrutinas**: lo actual en Kotlin (apuntes de la semana anterior, apartado 3). Se cancelan solas con el ViewModel o la pantalla.
- **Servicio** (`Service`): un componente **sin interfaz** que hace un trabajo largo, como reproducir música o sincronizar. Puede ser **iniciado** (`startService`: sigue hasta que se para) o **enlazado** (`bindService`: vive mientras alguien está conectado a él). Ojo: se ejecuta en el hilo principal si no se crea otro. Desde Android 8, un servicio que sigue cuando la app no está a la vista tiene que ser **de primer plano**, con una **notificación** visible (el reproductor de música).
- **WorkManager**: para el trabajo que tiene que hacerse **aunque se cierre la app o se reinicie el móvil**, cuando el sistema pueda: subir una copia de seguridad, sincronizar de noche, recordar una cita. Se le pueden poner **condiciones** (solo con Wi-Fi, solo cargando) y lo **reintenta** si falla. Ejemplo: recordar a la persona, al día siguiente, que tiene una solicitud de adopción pendiente.
- **Notificaciones:** desde Android 13 son un permiso peligroso, **`POST_NOTIFICATIONS`**, que hay que pedir mientras se usa la app.
- **Receptor de difusión** (`BroadcastReceiver`): responde a avisos del sistema o de otras apps (batería baja, se ha conectado el cargador, ha cambiado la red).

**Compara**

| | Corrutina | Servicio de primer plano | WorkManager |
|---|---|---|---|
| Para qué | Lo que tarda mientras se usa la pantalla | Algo largo que la persona **ve** que sigue | Algo que **tiene que hacerse**, aunque sea más tarde |
| Si se cierra la app | Se cancela | Sigue, con su notificación | Se hace igualmente, cuando se cumplan sus condiciones |
| Ejemplo | Cargar los animales | Reproducir música | El recordatorio de la solicitud |

---

## 3. Los sensores

**En una frase:** los sensores miden el mundo físico (movimiento, posición, luz…) y la app recibe sus valores cada vez que cambian.

**Lo que tienes que saber**

- **Tipos:**
  - **de movimiento:** acelerómetro (aceleración en m/s² en los ejes x, y, z), giroscopio (giro), gravedad;
  - **de posición:** magnetómetro (campo magnético, la brújula), proximidad (si hay algo cerca de la pantalla, como la oreja en una llamada);
  - **de entorno:** luz, presión, temperatura, humedad.
  No todos los móviles tienen todos: la app comprueba si el sensor existe antes de usarlo.
- **Cómo se usan:**
  - el **`SensorManager`** da acceso a los sensores (`getDefaultSensor(Sensor.TYPE_ACCELEROMETER)`; `getSensorList` dice cuáles hay);
  - un **`SensorEventListener`** recibe los valores en **`onSensorChanged`** (`event.values[0]`, `[1]`, `[2]` son x, y, z);
  - el listener se **registra** al mostrar la pantalla y se **quita** al salir: un sensor escuchando sin necesidad **gasta batería**. En Compose se hace con `DisposableEffect`, que registra al entrar y quita al salir (`onDispose`);
  - la **frecuencia** se elige al registrar (`SENSOR_DELAY_UI` para la interfaz, `SENSOR_DELAY_GAME` para juegos): más rápida, más batería.
- **Detectar una sacudida:** con el acelerómetro se calcula el módulo de la aceleración, `√(x² + y² + z²)`. En reposo vale unos 9,8 (la gravedad); si pasa de un **umbral** (por ejemplo, 2,7 veces la gravedad), es una sacudida. Para no contar varias, se ignoran las que llegan muy seguidas.
- **En el emulador:** *Extended controls* (los tres puntos de la barra del emulador) → **Virtual sensors**: se mueve el móvil virtual y cambian los valores.

---

## 4. La ubicación y los mapas

**En una frase:** el móvil sabe dónde está combinando GPS, redes móviles y Wi-Fi; la app pide permiso y recibe latitud y longitud.

**Lo que tienes que saber**

- **De dónde sale la posición:**
  - **GPS** (*Global Positioning System*): satélites; muy preciso, pero no funciona bien dentro de los edificios y gasta más batería;
  - **red**: antenas de telefonía y redes Wi-Fi cercanas; menos preciso, funciona dentro y gasta menos.
  - El **proveedor de ubicación combinada** (*Fused Location Provider*, de Google Play Services) elige y mezcla las fuentes según la precisión pedida: es lo recomendado. La API básica de Android es `LocationManager`.
- **Permisos** (peligrosos, se piden mientras se usa la app):
  - `ACCESS_COARSE_LOCATION`: ubicación **aproximada** (unos cientos de metros);
  - `ACCESS_FINE_LOCATION`: **precisa**. Desde Android 12, la persona puede dar solo la aproximada aunque la app pida la precisa: la app tiene que funcionar igual;
  - `ACCESS_BACKGROUND_LOCATION`: para usarla con la app cerrada. Se pide aparte y solo si de verdad hace falta.
- **Distancia entre dos puntos:** `Location.distanceBetween(lat1, lon1, lat2, lon2, resultado)` o `ubicacion.distanceTo(otra)`, en metros.
- **En el emulador:** *Extended controls* → **Location**: se elige un punto en el mapa y se pulsa *Set location*.
- **Mapas:** con el **Maps SDK for Android** (y la librería Maps Compose) se muestra un mapa de Google con marcadores y rutas. Necesita una **clave de API** de Google Cloud, asociada a la app, y Google cobra a partir de cierto número de usos. Hay alternativas libres, como OpenStreetMap.
- **Privacidad:** pedir solo la precisión necesaria, explicar para qué se usa y no guardarla ni enviarla si no hace falta.

**Compara**

| | Aproximada (`COARSE`) | Precisa (`FINE`) |
|---|---|---|
| Precisión | Cientos de metros | Unos metros |
| Para qué basta | El tiempo, la ciudad, la distancia a un centro | Navegación, rutas a pie |

---

## 5. Comunicación inalámbrica: red, Wi-Fi, Bluetooth y NFC

**En una frase:** además de Internet, el móvil se comunica con lo que tiene cerca; cada tecnología tiene su alcance, sus clases y sus permisos.

**Lo que tienes que saber**

- **Saber si hay conexión:** el **`ConnectivityManager`** dice si hay red y de qué tipo (Wi-Fi, datos móviles), y avisa cuando cambia. Necesita el permiso `ACCESS_NETWORK_STATE`. Sirve para avisar «Sin conexión» antes de intentar cargar, o para descargar lo pesado solo con Wi-Fi.
- **Wi-Fi:** el **`WifiManager`** consulta el estado y busca redes. Buscar redes necesita también el permiso de ubicación, porque las redes cercanas revelan dónde está la persona.
- **Bluetooth:** radio de corto alcance (unos 10 m) para intercambiar datos con otros dispositivos (auriculares, relojes, sensores, otro móvil).
  - Hay dos tipos: el **clásico**, para mucho tráfico continuo (audio), y el de **baja energía** (BLE, *Bluetooth Low Energy*), para pequeños datos con poco consumo (pulseras, sensores).
  - Los dos dispositivos se **vinculan** (emparejan) una vez; después se buscan, se conectan y abren un canal para enviar y recibir datos.
  - Permisos: desde Android 12, **`BLUETOOTH_SCAN`** (buscar) y **`BLUETOOTH_CONNECT`** (conectar), que se piden mientras se usa la app.
  - Para probarlo hacen falta dos dispositivos reales.
- **NFC** (*Near Field Communication*): alcance de unos **4 cm** y sin emparejar: basta con acercar. Sirve para pagar con el móvil, leer **etiquetas** (una etiqueta en la jaula que abre la ficha del animal) o compartir datos. La etiqueta no necesita batería.

**Compara**

| | Wi-Fi | Bluetooth | NFC |
|---|---|---|---|
| Alcance | Decenas de metros | Unos 10 m | Unos 4 cm |
| Emparejar | No (contraseña de la red) | Sí, una vez | No |
| Para qué | Internet, red local | Auriculares, relojes, sensores | Pagar, etiquetas |
