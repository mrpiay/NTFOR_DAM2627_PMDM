# UD3 · Persistencia, conectividad y servicios del dispositivo

Lo básico que hay que saber de la UD3. Cada apartado tiene la idea clave, lo que tienes que saber y, cuando hay conceptos que se confunden, una tabla para compararlos. Los ejemplos son de la app del Centro de acogida del proyecto. Las preguntas para comprobar que lo entiendes están en el [cuestionario](../cuestionario.md) de la semana, en «Cuestiones sobre los apuntes».

Los apartados 1 a 3 se practican en el proyecto. Los apartados 4 y 5 se trabajan a fondo en el proyecto integrado de la UD4; aquí está lo básico.

---

## 1. Guardar datos en el dispositivo

**En una frase:** lo que tiene que seguir ahí al cerrar la app se guarda en el dispositivo: en preferencias, en una base de datos o en archivos.

**Lo que tienes que saber**

- Lo que está solo en memoria (el ViewModel, `remember`) se pierde al cerrar la app.
- **DataStore:** para datos pequeños de clave y valor, como las preferencias (modo oscuro, último filtro).
- **Room:** para datos con estructura, en una base de datos **SQLite** dentro del dispositivo (las solicitudes de adopción).
- **Archivos:** para fotos, documentos y datos sin estructura.

**Compara**

| | DataStore | Room |
|---|---|---|
| Qué guarda | Unos pocos valores sueltos (clave y valor) | Tablas con muchos registros |
| Ejemplo | El último filtro elegido | Las solicitudes enviadas |
| Consultas | No | Sí, con SQL |

---

## 2. Room

**En una frase:** Room usa SQLite con tres piezas: la entidad, el DAO y la base de datos.

**Lo que tienes que saber**

- **Entidad** (`@Entity`): una tabla. Cada objeto es una fila. `Solicitud`, con `@PrimaryKey(autoGenerate = true)` para que la base de datos ponga el id.
- **DAO** (`@Dao`): las operaciones, con anotaciones (`@Insert`, `@Query("SELECT …")`).
- **Base de datos** (`@Database`): une las entidades y los DAO. Hay **una sola** instancia para toda la app.
- Room genera el código al compilar, con **KSP** (un plugin de Gradle).
- Una consulta que devuelve un **`Flow`** avisa cada vez que cambian los datos, y la pantalla se actualiza sola.
- Con el **Database Inspector** de Android Studio se ve la base de datos de la app en el emulador.

---

## 3. Trabajo en segundo plano: corrutinas

**En una frase:** lo que puede tardar (base de datos, red) no se hace en el hilo de la interfaz, sino en una corrutina, para que la app no se congele.

**Lo que tienes que saber**

- El **hilo principal** dibuja la interfaz y atiende los toques; si se bloquea, la app se congela.
- Una función **`suspend`** puede pausarse mientras espera, sin bloquear el hilo.
- Desde el ViewModel se lanza con **`viewModelScope.launch { … }`**: la corrutina se cancela sola si el ViewModel desaparece. Desde una actividad, lo mismo con **`lifecycleScope.launch { … }`**.
- `launch` empieza la corrutina en ese momento; cuando llega a una función `suspend` que tiene que esperar (como guardar en Room), se suspende, y el código que va después de `launch` sigue. Cuando termina la espera, la corrutina continúa donde se quedó.
- Room no deja acceder a la base de datos desde el hilo principal: da el error *Cannot access database on the main thread*.

---

## 4. Permisos

**En una frase:** para usar lo delicado del dispositivo (cámara, ubicación, micrófono, contactos) la app declara el permiso y, además, lo pide a la persona mientras se usa.

**Lo que tienes que saber**

- Todos los permisos se declaran en **`AndroidManifest.xml`** (`<uses-permission …/>`).
- Los **normales** (como Internet) se conceden solos al instalar.
- Los **peligrosos** (cámara, ubicación, micrófono…) se piden **mientras se usa la app**, con el diálogo del sistema, justo cuando hacen falta. La persona puede concederlos, concederlos solo esta vez o denegarlos, y la app tiene que funcionar en los tres casos.
- En Compose se piden con `rememberLauncherForActivityResult` y `RequestPermission`.

**Compara**

| | Permiso normal | Permiso peligroso |
|---|---|---|
| Ejemplo | `INTERNET` | `CAMERA`, `ACCESS_FINE_LOCATION` |
| Se declara en el manifiesto | Sí | Sí |
| Se pide mientras se usa la app | No | Sí |

---

## 5. Conectividad, sensores y despliegue

**En una frase:** una app puede conectarse a servicios web, usar los sensores y comunicarse con otros dispositivos; y para usarla en un móvil real hay que empaquetarla.

**Lo que tienes que saber**

- **HTTP y HTTPS:** para hablar con un servicio web (una API REST que devuelve JSON). En Android se suele usar **Retrofit**. Siempre HTTPS, que va cifrado. Hace falta el permiso `INTERNET`.
- **Sensores:** acelerómetro, giroscopio, proximidad, luz… se leen con el `SensorManager`. La **ubicación**, con el servicio de localización y su permiso.
- **Comunicación inalámbrica:** Bluetooth, NFC y Wi-Fi, cada una con sus clases y permisos.
- **Empaquetar y desplegar:**
  - la app se empaqueta en un **APK**, o en un **AAB** para publicarla en Google Play;
  - la versión de **depuración** (*debug*) es para probar, y la de **publicación** (*release*) va **firmada** con una clave;
  - se instala en un móvil real conectándolo por USB con la **depuración USB** activada, o instalando el APK.
