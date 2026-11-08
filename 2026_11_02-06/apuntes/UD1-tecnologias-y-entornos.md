# UD1 · Tecnologías y entornos de desarrollo para dispositivos móviles

Lo básico que hay que saber de la UD1. Cada apartado tiene la idea clave, lo que tienes que saber y, cuando hay conceptos que se confunden, una tabla para compararlos. Los ejemplos son de la app del Centro de acogida del proyecto. Las preguntas para comprobar que lo entiendes están en el [cuestionario](../cuestionario.md) de la semana, en «Cuestiones sobre los apuntes».

---

## 1. Las limitaciones de un dispositivo móvil

**En una frase:** una app móvil funciona en un aparato pequeño, con batería, con poca memoria y con la red que haya, y eso condiciona cómo se programa.

**Lo que tienes que saber**

- **Batería:** cada operación consume; hay que evitar trabajo innecesario en segundo plano.
- **Pantalla:** pequeña y de muchos tamaños y densidades; la interfaz se adapta (UD2).
- **Memoria y procesador:** más limitados que en un ordenador; el sistema puede cerrar una app en segundo plano para liberar memoria.
- **Red:** puede ser lenta, cara o no estar; la app tiene que funcionar sin conexión o avisar.
- **Interacción:** se usa con el dedo, a menudo con una mano y con interrupciones (llamadas, notificaciones). Al girar el móvil, la pantalla se vuelve a crear.
- **Fragmentación:** hay miles de modelos y muchas versiones de Android a la vez.

---

## 2. Las tecnologías de desarrollo

**En una frase:** una app se puede hacer nativa para cada sistema, multiplataforma con un solo código o como web.

**Lo que tienes que saber**

- **Nativa:** con las herramientas de cada sistema. Android, con **Kotlin** (antes Java) y **Jetpack Compose**; iOS, con Swift y SwiftUI. Da el mejor rendimiento y acceso a todo el dispositivo.
- **Multiplataforma:** un solo código para Android e iOS: **Flutter** (Dart), React Native (JavaScript), **Kotlin Multiplatform** y .NET MAUI (C#).
- **Web y PWA:** una web adaptada al móvil, que se puede instalar como una app; acceso limitado al dispositivo.
- En el módulo, Android nativo con Kotlin y Compose.

**Compara**

| | Nativa | Multiplataforma | Web / PWA |
|---|---|---|---|
| Código | Uno por sistema | Uno para todos | Uno para todos |
| Rendimiento y acceso al dispositivo | El mejor | Muy bueno | Limitado |
| Ejemplos | Kotlin + Compose, Swift + SwiftUI | Flutter, React Native, Kotlin Multiplatform | HTML, CSS y JavaScript |

---

## 3. Android y sus versiones

**En una frase:** Android es un sistema basado en el kernel Linux, con muchas versiones en uso, y cada app dice desde qué versión funciona.

**Lo que tienes que saber**

- Capas: el **kernel Linux** (hardware, memoria, procesos), las librerías y el entorno de ejecución **ART**, el **framework** de Android (las clases que usa una app) y las apps.
- Cada versión tiene un **nivel de API** (un número, como 24 o 36).
- En `app/build.gradle.kts`:
  - **`minSdk`:** la API mínima en la que se puede instalar la app (24 en el proyecto, Android 7.0);
  - **`targetSdk`:** la versión para la que está pensada y probada;
  - **`compileSdk`:** la versión con la que se compila.
- Cuanto más bajo el `minSdk`, en más móviles funciona, pero con menos funciones nuevas disponibles.

---

## 4. El entorno: Android Studio, Gradle, el emulador y Logcat

**En una frase:** en Android Studio se escribe y se ejecuta la app, Gradle la construye, el emulador la prueba y Logcat muestra lo que pasa.

**Lo que tienes que saber**

- **Android Studio:** el entorno oficial. Trae el **SDK** de Android y un **JDK**.
- **Gradle:** construye la app y descarga las librerías. Se configura en **`gradle/libs.versions.toml`** (el catálogo de versiones) y **`app/build.gradle.kts`** (las dependencias que usa la app). Tras cambiarlos: **Sync Now**.
- **Emulador** (AVD, *Android Virtual Device*): un móvil simulado, que se crea en el *Device Manager*. Necesita la virtualización activada en el ordenador. También se puede probar en un **móvil real** con la depuración USB activada.
- **Logcat:** los mensajes de la app mientras funciona (`Log.d(TAG, "…")`) y sus errores. Se filtra con `tag:`.
- **Vista previa** (`@Preview`): dibuja un composable sin ejecutar la app.

---

## 5. La estructura de un proyecto Android

**En una frase:** un proyecto tiene el código, los recursos, el manifiesto y la configuración de Gradle, cada cosa en su sitio.

**Lo que tienes que saber**

| Dónde | Qué hay |
|---|---|
| `app/src/main/java/…` | El código Kotlin: actividades, pantallas, ViewModel, modelos |
| `app/src/main/res/` | Los recursos: imágenes (`drawable`), textos y colores (`values`), iconos (`mipmap`) |
| `AndroidManifest.xml` | La identidad de la app: su nombre, su actividad principal y los **permisos** |
| `build.gradle.kts` y `libs.versions.toml` | La configuración: versiones, `minSdk` y dependencias |

- Desde el código, los recursos se usan con **`R`**: `R.drawable.foto`, `R.string.titulo`.
- La **actividad** (`MainActivity`) es el punto de entrada: en `onCreate` llama a `setContent { … }`, que muestra los composables.
- Para modificar una app existente, primero se localiza cada pieza: qué archivo dibuja cada pantalla, dónde están los datos y qué se toca para cambiar algo.

---

## 6. El ciclo de vida de una actividad

**En una frase:** Android crea, pausa, para y destruye las actividades según lo que hace la persona, y la app tiene que estar preparada.

**Lo que tienes que saber**

- Los métodos, en orden: **`onCreate`** → `onStart` → `onResume` (la app está en primer plano) … `onPause` → `onStop` → **`onDestroy`**.
- **Al girar el móvil**, la actividad se destruye y se vuelve a crear: `onPause`, `onStop`, `onDestroy` y otra vez `onCreate`, `onStart`, `onResume`.
- Por eso lo guardado solo con `remember` se pierde al girar. Para que el estado sobreviva se usa un **ViewModel** (UD2) o `rememberSaveable`.

---

## 7. Configuraciones y perfiles de dispositivo

**En una frase:** los dispositivos se distinguen por su tamaño de pantalla, su densidad, su orientación y su versión, y la app se adapta a cada uno.

**Lo que tienes que saber**

- **Tamaño** (móvil, plegable, tableta) y **orientación** (vertical u horizontal).
- **Densidad:** los píxeles por pulgada. Por eso los tamaños se dan en **dp** (píxeles independientes de la densidad) y los textos en **sp**: miden lo mismo a la vista en cualquier pantalla. Los **sp**, además, crecen o encogen con el tamaño de letra que la persona elige en los ajustes del móvil (por accesibilidad).
- **Recursos alternativos:** carpetas con sufijo (`values-es`, `drawable-xxhdpi`, `values-night`) que Android elige según el idioma, la densidad o el modo oscuro.
- En el emulador se crean perfiles (*Pixel*, *Pixel Tablet*…) para probar la app en distintos dispositivos.
