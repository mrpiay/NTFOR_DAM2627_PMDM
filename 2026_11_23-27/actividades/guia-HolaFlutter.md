# App Flutter - Hola Flutter

## Introducción

En esta guía vas a instalar **Flutter** y a crear, ejecutar y entender tu primera app: el **contador** que trae Flutter de ejemplo, una pantalla con un número y un botón **+** que lo aumenta. Es la misma idea que el primer contador de la unidad 1 con Android, pero hecha con otra tecnología.

**Flutter** es la tecnología de Google para crear apps **multiplataforma**: con el mismo código se obtienen apps para Android, iOS, web y escritorio. Se programa en **Dart**, y la interfaz se construye con **widgets**, unos dentro de otros, igual que en Compose con funciones `@Composable`. Hasta ahora todo se ha hecho en Android **nativo**, con Kotlin y Compose; con esta guía se ve la otra opción, para poder compararlas.

El objetivo no es solo que la app funcione, sino entender qué es Flutter, cómo está hecho por dentro y cómo se organiza un proyecto. Para ello se usan el **SDK de Flutter**, **VS Code** con la extensión de Flutter y el **emulador de Android** que ya tienes de ValoraPMDM.

El desarrollo se divide en dos partes:

- **Parte 1. Qué es Flutter:** qué es, su arquitectura, las herramientas del SDK y cómo es un proyecto, antes de instalar nada.
- **Parte 2. Instalación y primera app paso a paso:** se instala el SDK, se comprueba con `flutter doctor`, se crea el proyecto, se ejecuta en el emulador, se prueba el *hot reload*, se lee el código y, al final, la misma app se ejecuta en el navegador.

La teoría sale del libro de McGraw-Hill, *Programación multimedia y dispositivos móviles*, **unidad 4, apartado 1 «Introducción a Flutter»**, y sus casos prácticos «Instalación del plugin de Flutter en Visual Studio Code» y «Creación de proyectos, ejecución y Hot Reload», que conviene tener a mano. El libro está hecho con una versión anterior de Flutter: donde cambia algo, la guía lo dice.

Al finalizar, tendrás Flutter instalado y funcionando, y la base para la guía siguiente, «ValoraDart», con el lenguaje Dart.

## Índice

- [Parte 1 · Qué es Flutter](#parte-1--qué-es-flutter)
  - [1. Flutter, en pocas palabras](#1-flutter-en-pocas-palabras)
  - [2. La arquitectura](#2-la-arquitectura)
  - [3. Las herramientas del SDK](#3-las-herramientas-del-sdk)
  - [4. Un proyecto de Flutter](#4-un-proyecto-de-flutter)
  - [5. Las palabras que hay que conocer](#5-las-palabras-que-hay-que-conocer)
  - [6. Flutter frente a Android nativo](#6-flutter-frente-a-android-nativo)
- [Parte 2 · Instalación y primera app](#parte-2--instalación-y-primera-app)
  - [Paso 0 · Lo que hace falta](#paso-0--lo-que-hace-falta)
  - [Paso 1 · Descargar el SDK](#paso-1--descargar-el-sdk)
  - [Paso 2 · Añadir Flutter al Path](#paso-2--añadir-flutter-al-path)
  - [Paso 3 · flutter doctor](#paso-3--flutter-doctor)
  - [Paso 4 · La extensión de VS Code](#paso-4--la-extensión-de-vs-code)
  - [Paso 5 · Crear el proyecto](#paso-5--crear-el-proyecto)
  - [Paso 6 · Ejecutar en el emulador](#paso-6--ejecutar-en-el-emulador)
  - [Paso 7 · El hot reload](#paso-7--el-hot-reload)
  - [Paso 8 · Leer el código](#paso-8--leer-el-código)
  - [Paso 9 · La misma app en el navegador](#paso-9--la-misma-app-en-el-navegador)
- [Si algo falla](#si-algo-falla)
- [Los archivos del proyecto](#los-archivos-del-proyecto)

---

# Parte 1 · Qué es Flutter

## 1. Flutter, en pocas palabras

Según el libro (MGH, U4, 1.1), **Flutter es un SDK y un *framework* de código abierto**, creado por Google en 2017, para desarrollar apps **multiplataforma** (Android, iOS, escritorio y web): con el mismo código se obtienen apps compiladas al código nativo de cada plataforma.

- **El lenguaje es Dart**, también de Google (2011). Dart se ejecuta en su máquina virtual, la **DartVM**, y se puede compilar de dos formas:
  - **JIT** (*just-in-time*): mientras se ejecuta, en la máquina virtual. Es lo que se usa al programar, y lo que permite el *hot reload*.
  - **AOT** (*ahead-of-time*): antes de ejecutar, al código nativo de cada plataforma. Es lo que se usa en la app final, y le da un rendimiento casi nativo.
- **Flutter dibuja su propia interfaz**: no usa los controles de Android ni los de iOS, sino los suyos, y los dibuja con su motor gráfico (Skia, en el libro; las versiones actuales usan **Impeller**). Por eso una app se ve igual en todas las plataformas.

De ahí salen sus **ventajas**:

- **Stateful hot reload** (recarga en caliente con estado): al guardar un cambio en el código, se ve en la app al momento, sin reiniciarla y **sin perder el estado** (lo que se había escrito, el número del contador…).
- **Rendimiento casi nativo**, por la compilación AOT.
- **Interfaces vistosas y animaciones fluidas**, hasta 60 imágenes por segundo.

Y sus **inconvenientes**, por no ser nativo:

- Las **novedades de cada sistema** llegan antes a las herramientas nativas.
- Las apps **ocupan más**, porque llevan dentro lo necesario para cada sistema.
- El **rendimiento es algo menor** que el de una app nativa bien optimizada.

## 2. La arquitectura

Flutter se organiza en **capas**: cada una se apoya en la de debajo (MGH, U4, 1.2, con el diagrama de la documentación oficial):

```
┌──────────────────────────────────────────────────────────────┐
│ FRAMEWORK · en Dart                lo que usa quien programa │
│   Material y Cupertino   los controles de Android y de iOS   │
│   Widgets                los controles y cómo se combinan    │
│   Rendering              el árbol de componentes y su dibujo │
│   Animación, dibujo, gestos y clases básicas (Foundation)    │
├──────────────────────────────────────────────────────────────┤
│ ENGINE (motor) · en C y C++                                  │
│   dibuja cada imagen (frame), el texto, la entrada y la      │
│   salida, la accesibilidad…                                  │
├──────────────────────────────────────────────────────────────┤
│ EMBEDDER (integrador) · propio de cada plataforma            │
│   empaqueta la app en el formato nativo y la integra en el   │
│   sistema operativo (Android, iOS, Windows…)                 │
└──────────────────────────────────────────────────────────────┘
```

Al programar en Flutter solo se toca la capa de arriba, el **framework**: los widgets de **Material** (el diseño de Android, el mismo que en Compose) o de **Cupertino** (el de iOS).

## 3. Las herramientas del SDK

El SDK se descarga de la web de Flutter y se organiza en **canales**: el **estable** (*stable*), con lo ya probado, que es el que se usa, y el **beta**, con las novedades para probarlas (MGH, U4, 1.3). Todo se hace con un solo comando, **`flutter`**, desde el terminal:

| Comando | Qué hace |
|---|---|
| `flutter help` | La ayuda, con todos los subcomandos |
| `flutter doctor` | Revisa el equipo y dice qué falta para usar Flutter (el SDK de Android, las licencias, un emulador…) y cómo arreglarlo |
| `flutter create nombre` | Crea un proyecto nuevo |
| `flutter run` | Compila y ejecuta la app en un dispositivo |
| `flutter build apk` | Genera el instalable (APK) |
| `flutter clean` | Borra lo compilado, para empezar de cero |
| `flutter devices` | Los dispositivos conectados donde se puede ejecutar la app |
| `flutter emulators` | Los emuladores disponibles, y con `--launch` arranca uno |
| `flutter create --platforms web .` | Añade otra plataforma (aquí, la web) a un proyecto que ya existe (paso 9) |

Para programar, Flutter se integra con **Android Studio**, **VS Code** y Emacs mediante extensiones (*plugins*) (MGH, U4, 1.4). En VS Code hacen falta dos: **Flutter** y **Dart**; al instalar la de Flutter, se instala también la de Dart. Esta guía usa VS Code, como los casos prácticos del libro. Android Studio sigue haciendo falta por el SDK de Android y el emulador.

## 4. Un proyecto de Flutter

Un proyecto se crea con `flutter create` (MGH, U4, 1.5). Lo más importante de lo que genera:

| Carpeta o archivo | Qué es |
|---|---|
| **`lib`** | El **código de la app, en Dart**. `lib/main.dart` es el punto de entrada. Es lo único que se programa en esta guía |
| **`pubspec.yaml`** | La **configuración** del proyecto: su nombre, las librerías que usa (dependencias) y los recursos (imágenes, fuentes, sonidos) |
| **`android`** | Un **proyecto de Android completo** (con su módulo `app`, Gradle, el manifiesto…). Al compilar, el código de Flutter se mete en él |
| `ios` | Lo mismo para iOS. Para compilarlo hace falta un Mac con Xcode. En esta guía no se crea |
| `web` | Lo necesario para la web: la página `index.html` que carga la app. Se añade en el paso 9 |
| `test` | Las pruebas automáticas |
| `build` | Lo compilado: no se toca |

## 5. Las palabras que hay que conocer

| Palabra | Qué es | En el contador |
|---|---|---|
| **Widget** | Cada pieza de la interfaz: un texto, un botón, una columna, la pantalla entera. Se meten unos dentro de otros, formando un **árbol** | `Text`, `Column`, `Scaffold`, `FloatingActionButton` |
| **`StatelessWidget`** | Un widget **sin estado**: solo muestra lo que recibe | `MyApp` |
| **`StatefulWidget`** | Un widget **con estado**: tiene datos que cambian. El estado está en una clase aparte, `State` | `MyHomePage`, con su `_MyHomePageState` |
| **`build`** | El método que dice cómo se dibuja el widget: devuelve su árbol de widgets | En `MyApp` y en `_MyHomePageState` |
| **`setState`** | Cambia el estado y avisa a Flutter para que vuelva a ejecutar `build` y redibuje. Es como cambiar un estado de Compose | `_counter++`, dentro de `setState` |
| **Hot reload** | Al guardar un cambio, se aplica a la app en marcha **sin perder el estado**. En el terminal, tecla `r` | El número del contador sigue igual |
| **Hot restart** | Vuelve a arrancar la app con el código nuevo, **perdiendo el estado**. En el terminal, tecla `R` | El contador vuelve a 0 |
| **Modo *debug*** | Compilación JIT, con *hot reload*. La app lleva la etiqueta **DEBUG** arriba a la derecha | Lo que se usa en esta guía |

## 6. Flutter frente a Android nativo

| | Android nativo (ValoraPMDM) | Flutter |
|---|---|---|
| Lenguaje | Kotlin | Dart |
| Sistemas | Android | Android, iOS, web y escritorio, con el mismo código |
| La interfaz | Funciones `@Composable` | Widgets, clases de Dart |
| El estado | `remember`, `mutableStateOf`, ViewModel | `StatefulWidget` y `setState` (o gestores de estado como Provider) |
| Quién dibuja | Android | El motor de Flutter |
| Ver un cambio | Volver a compilar e instalar (o la vista previa) | Hot reload: al guardar |
| Librerías | Gradle (`libs.versions.toml`) | `pubspec.yaml`, de [pub.dev](https://pub.dev) |

---

# Parte 2 · Instalación y primera app

Cada paso dice **qué se hace** y termina con **Comprueba que…**: no pases al siguiente hasta que se cumpla.

## Paso 0 · Lo que hace falta

- **Android Studio**, con el SDK de Android y el emulador **Pixel 9** de ValoraPMDM: Flutter los usa para compilar y ejecutar la app de Android.
- **VS Code**.
- **Git**, que Flutter necesita para funcionar. En PowerShell, `git --version` tiene que mostrar una versión. Si no, instálalo desde [git-scm.com](https://git-scm.com/download/win) con las opciones por defecto.
- Unos **5 GB libres**: unos 2 GB el SDK de Flutter, y otros tantos lo que descarga la primera vez que compila para Android.

**Comprueba que…** Android Studio abre, el Pixel 9 arranca desde su *Device Manager* y `git --version` responde.

## Paso 1 · Descargar el SDK

**Dónde se instala: en una ruta corta y sin caracteres raros.** Flutter y las herramientas de Android fallan en Windows con rutas largas y con rutas que llevan **ñ, tildes, espacios u otros caracteres que no sean letras inglesas, números, `-` o `_`**: por ejemplo, `C:\Users\usuario\OneDrive\Escritorio` o `C:\Program Files`. Por eso todo va en `C:\dam2627`: el SDK en `C:\dam2627\flutter` y los proyectos en `C:\dam2627\pmdm`.

1. Descarga el **zip del SDK** para Windows, canal **stable**, desde la página de instalación de Flutter: [docs.flutter.dev/install/archive](https://docs.flutter.dev/install/archive). Esta guía está probada con **Flutter 3.50.1**. Ocupa casi 2 GB.
2. Descomprímelo **en `C:\dam2627`**: clic derecho sobre el zip → **Extraer todo…** → escribe `C:\dam2627` → **Extraer**. Dentro del zip ya hay una carpeta `flutter`, así que queda `C:\dam2627\flutter`.
3. Borra el zip.

**Comprueba que…** existe `C:\dam2627\flutter\bin\flutter.bat`. Si la ruta es `C:\dam2627\flutter\flutter\bin`, lo has descomprimido una carpeta más dentro: mueve la carpeta interior a `C:\dam2627`.

## Paso 2 · Añadir Flutter al Path

Para escribir `flutter` en cualquier terminal, su carpeta `bin` tiene que estar en el **`Path`**, la lista de carpetas donde Windows busca los programas. En PowerShell, en una sola línea:

```powershell
[Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path', 'User') + ';C:\dam2627\flutter\bin', 'User')
```

| Parte | Qué hace |
|---|---|
| `GetEnvironmentVariable('Path', 'User')` | Lee el `Path` de tu usuario |
| `+ ';C:\dam2627\flutter\bin'` | Le añade la carpeta de Flutter al final. El `;` separa una carpeta de otra |
| `SetEnvironmentVariable(…, 'User')` | Lo guarda, para tu usuario. No hace falta ser administrador |

El cambio solo lo ven los terminales y programas que se abran **después**: **cierra PowerShell y VS Code** y vuelve a abrirlos.

**Comprueba que…** en un PowerShell nuevo, `flutter --version` muestra `Flutter 3.50.1 • channel stable` (la primera vez tarda un poco y muestra un aviso sobre el envío de estadísticas).

## Paso 3 · flutter doctor

```powershell
flutter doctor
```

Revisa todo lo que necesita Flutter y lo marca con `[√]` (bien), `[!]` (falta algo) o `[X]` (no está). En un equipo con Android Studio, lo normal la primera vez es esto:

```
[√] Flutter (Channel stable, 3.50.1, on Microsoft Windows …)
[√] Windows Version (Windows 11 or higher, …)
[!] Android toolchain - develop for Android devices (Android SDK version 36.0.0)
    X cmdline-tools component is missing.
    X Android license status unknown.
[√] Chrome - develop for the web
[X] Visual Studio - develop Windows apps
[√] Connected device (3 available)
[√] Network resources
```

| Línea | Qué hacer |
|---|---|
| **Android toolchain · cmdline-tools** | Faltan las herramientas de línea de comandos del SDK de Android. En Android Studio: **File → Settings → Languages & Frameworks → Android SDK**, pestaña **SDK Tools**, marca **Android SDK Command-line Tools (latest)** y **Apply** → **OK** |
| **Android toolchain · licenses** | Hay que aceptar las licencias del SDK: `flutter doctor --android-licenses`, y a cada pregunta, `y` e **Intro** |
| **Visual Studio** | Solo hace falta para crear apps de **escritorio de Windows**. No se usan: **se ignora** |
| **Chrome** | Para apps web. Tampoco se usan, pero no molesta |

Vuelve a ejecutar `flutter doctor`.

**Comprueba que…** **Flutter** y **Android toolchain** salen con `[√]`. Visual Studio puede seguir con `[X]`.

## Paso 4 · La extensión de VS Code

En VS Code, panel de **Extensiones** (`Ctrl + Mayús + X`), busca **Flutter** (de *Dart Code*) e **Instalar**. Instala también la extensión **Dart**, que necesita (MGH, U4, 1.4.A y caso práctico «Instalación del plugin de Flutter en Visual Studio Code»).

**Comprueba que…** con `Ctrl + Mayús + P` y escribiendo `flutter`, salen órdenes como **Flutter: New Project** y **Flutter: Run Flutter Doctor**. Si VS Code dice *Could not find a Flutter SDK*, ciérralo y ábrelo otra vez (paso 2); si sigue, pulsa **Locate SDK** y elige `C:\dam2627\flutter`.

## Paso 5 · Crear el proyecto

En PowerShell:

```powershell
cd C:\dam2627\pmdm
flutter create --platforms android hola_flutter
code hola_flutter
```

| Comando | Qué hace |
|---|---|
| `flutter create --platforms android hola_flutter` | Crea el proyecto `hola_flutter`, solo para Android (`--platforms android`). Sin esa opción crearía también las carpetas de iOS, web, Windows… Los nombres van en minúsculas y con `_` |
| `code hola_flutter` | Abre la carpeta del proyecto en VS Code |

Si VS Code pregunta si confías en los autores de la carpeta, **Yes, I trust the authors**. Abajo a la derecha puede salir que está descargando dependencias: espera a que termine.

**Comprueba que…** en el Explorador de VS Code están `lib/main.dart`, `pubspec.yaml`, `android` y `test` (punto 4 de la Parte 1).

## Paso 6 · Ejecutar en el emulador

1. **Arranca el emulador.** En la terminal de VS Code (`Ctrl + ñ`):

   ```powershell
   flutter emulators
   flutter emulators --launch Pixel_9
   ```

   `flutter emulators` muestra los emuladores (el `Id` del Pixel 9 es `Pixel_9`) y `--launch` lo arranca. Espera a ver la pantalla de inicio del móvil.
2. **Comprueba que Flutter lo ve:**

   ```powershell
   flutter devices
   ```

   Tiene que salir una línea con **sdk gphone** o **Pixel 9** y **android**, además de Chrome y Windows.
3. **Ejecuta la app:** abre `lib/main.dart` y pulsa **F5** (*Run → Start Debugging*). Abajo a la derecha, en la barra de estado de VS Code, tiene que poner el emulador como dispositivo; si pone **Chrome** o **Windows**, haz clic ahí y elige el emulador.

**La primera vez tarda mucho, entre 5 y 10 minutos:** Gradle compila el proyecto de Android y descarga lo que le falta del SDK (por ejemplo, el NDK y CMake). En la consola de depuración de VS Code se ve el progreso. Las siguientes veces tarda segundos.

**Comprueba que…** en el emulador se abre **Flutter Demo Home Page**, con la etiqueta **DEBUG** arriba a la derecha, el texto *You have pushed the button this many times:*, un **0** y el botón **+**. Al pulsar **+**, el número aumenta.

## Paso 7 · El hot reload

Con la app en marcha y el número en **4** (pulsa **+** cuatro veces), cambia dos textos en `lib/main.dart`, como en el caso práctico del libro:

- `'Flutter Demo Home Page'` por `'Ejemplo de aplicación Flutter'`.
- `'You have pushed the button this many times:'` por `'Cantidad de pulsaciones del botón +:'`.

Guarda con **Ctrl + S**.

**Comprueba que…** los dos textos cambian en el emulador al momento, sin reiniciar la app, y **el número sigue en 4**: es el *stateful hot reload*. Después, prueba el *hot restart*: en la barra de depuración de VS Code, la flecha circular verde (**Restart**, `Ctrl + Mayús + F5`). La app vuelve a empezar y el número vuelve a **0**.

Una prueba más, la que propone el propio código (`TRY THIS`): cambia `Colors.deepPurple` por `Colors.green` y guarda. Toda la app cambia de color, y el número no se pierde.

## Paso 8 · Leer el código

Es el código que ha creado `flutter create`, con sus comentarios en inglés. El libro trae el mismo contador comentado en español, en su ejemplo descargable `contador.dart` (MGH, U4, apartado 3); conviene tenerlo al lado.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `import 'package:material_ui/material_ui.dart';` | Trae los widgets de Material Design, del paquete `material_ui`, que `flutter create` apunta en `pubspec.yaml`. El libro usa `package:flutter/material.dart`: todavía funciona, pero desde Flutter 3.47 está obsoleto (*deprecated*) y `flutter analyze` avisa de que se cambie por el nuevo |
| `void main() { runApp(const MyApp()); }` | El punto de entrada: arranca la app con el widget raíz, `MyApp` |
| `class MyApp extends StatelessWidget` | La app es un widget sin estado. **Hereda** (`extends`) de `StatelessWidget` |
| `const MyApp({super.key});` | El constructor, con un argumento con nombre, `key`, que pasa a la clase madre. El libro escribe `({Key? key}) : super(key: key)`, que es lo mismo, de forma más larga |
| `@override Widget build(BuildContext context)` | Sobrescribe `build`: devuelve el árbol de widgets de este widget |
| `MaterialApp(title: …, theme: …, home: …)` | La app de Material Design: su título, sus colores (`theme`) y la pantalla de inicio (`home`). Todos los argumentos son **con nombre** |
| `colorScheme: .fromSeed(seedColor: Colors.deepPurple)` | Los colores de la app, generados a partir de uno. `.fromSeed` es la forma corta de `ColorScheme.fromSeed`: Dart deduce el tipo (en el libro, siempre con el nombre completo) |
| `class MyHomePage extends StatefulWidget` | La pantalla de inicio: un widget **con estado** |
| `final String title;` | Su configuración: el título, que no cambia |
| `State<MyHomePage> createState() => _MyHomePageState();` | Crea su estado, en la clase `_MyHomePageState`. El `_` la hace privada de este archivo |
| `int _counter = 0;` | El estado: el número del contador |
| `setState(() { _counter++; });` | Aumenta el número **y avisa** a Flutter: se vuelve a ejecutar `build` y se redibuja. Sin `setState`, el número cambiaría, pero la pantalla no |
| `Scaffold(appBar: …, body: …, floatingActionButton: …)` | La estructura de una pantalla de Material: la barra de arriba, el contenido y el botón flotante. Como `Scaffold` en Compose |
| `Center` y `Column(children: [ … ])` | Widgets de distribución (*layout*): `Center` centra a su hijo (`child`); `Column` pone sus hijos (`children`) uno debajo de otro |
| `mainAxisAlignment: .center` | Centra los hijos en vertical. Forma corta de `MainAxisAlignment.center` |
| `Text('$_counter', style: …headlineMedium)` | El número, con un estilo de letra grande. El libro usa `headline4`, el nombre antiguo |
| `FloatingActionButton(onPressed: _incrementCounter, …)` | El botón **+**: al pulsarlo, llama a `_incrementCounter`. Se pasa la función, sin paréntesis |
| `const` delante de un widget | El widget no cambia nunca: Flutter lo crea una vez y no lo vuelve a construir |

```dart
import 'package:material_ui/material_ui.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a purple toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: .fromSeed(seedColor: Colors.deepPurple),
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // TRY THIS: Try changing the color here to a specific color (to
        // Colors.amber, perhaps?) and trigger a hot reload to see the AppBar
        // change color while the other colors stay the same.
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          //
          // TRY THIS: Invoke "debug painting" (choose the "Toggle Debug Paint"
          // action in the IDE, or press "p" in the console), to see the
          // wireframe for each widget.
          mainAxisAlignment: .center,
          children: [
            const Text('You have pushed the button this many times:'),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
```

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `MyApp` es un `StatelessWidget` y `MyHomePage` un `StatefulWidget`?
2. ¿Qué pasaría si `_incrementCounter` hiciera `_counter++` sin `setState`?
3. ¿Por qué con el *hot reload* el número no vuelve a 0, y con el *hot restart* sí?
4. En `onPressed: _incrementCounter`, ¿por qué no hay paréntesis después de `_incrementCounter`?
5. ¿Qué carpeta del proyecto habría que llevarse a otro ordenador para tener el código de la app, y cuál no hace falta?

<details>
<summary>Respuestas</summary>

1. `MyApp` solo monta la app (título, colores, pantalla de inicio), y eso no cambia. `MyHomePage` tiene un dato que cambia mientras se usa, el número del contador: ese dato es su estado.
2. El número aumentaría en memoria, pero la pantalla no cambiaría: `build` no se volvería a ejecutar, porque nadie ha avisado a Flutter.
3. El *hot reload* cambia el código de la app que ya está en marcha y conserva el estado: `_counter` sigue valiendo lo mismo. El *hot restart* vuelve a crearlo todo desde `main`, y `_counter` vuelve a su valor inicial, 0.
4. Porque se le pasa **la función**, para que el botón la llame cuando se pulse. Con paréntesis, se llamaría al construir la pantalla y se pasaría su resultado (nada).
5. `lib` (el código) y `pubspec.yaml` (la configuración), además de `android`. La carpeta `build` no: es lo compilado, y se vuelve a generar.

</details>

## Paso 9 · La misma app en el navegador

Flutter es **multiplataforma**: el mismo código sirve para Android, iOS, la web y el escritorio. En el paso 5 el proyecto se creó solo para Android (`--platforms android`). Para que funcione también en el navegador, basta con **añadirle la plataforma web**. El código de `lib` no se toca.

1. Para la app del emulador: en la barra de depuración de VS Code, el cuadrado rojo (**Stop**, `Mayús + F5`).
2. En la terminal de VS Code, en la carpeta del proyecto:

   ```powershell
   flutter create --platforms web .
   flutter devices
   flutter run -d chrome
   ```

   | Orden | Qué hace |
   |---|---|
   | `flutter create --platforms web .` | Añade la plataforma web al proyecto que ya existe: el punto (`.`) es la carpeta actual. Crea la carpeta `web`, con la página `index.html` que carga la app, y no cambia `lib` ni `android` |
   | `flutter devices` | Ahora, además del emulador (si está encendido), salen **Chrome (web)** y **Edge (web)** |
   | `flutter run -d chrome` | Compila la app para la web y la abre en Chrome. `-d` elige el dispositivo. En VS Code, lo mismo: elegir **Chrome** en la barra de estado, abajo a la derecha, y **F5** |

3. Para terminar, en la terminal donde se ejecuta, pulsa `q`.

**Qué ha cambiado al compilar.** Para Android, Dart se compila a **código nativo** del procesador del móvil (AOT, MGH 1.1). Para la web, se compila a **JavaScript** (o WebAssembly), que es lo que entiende el navegador. El código de la app es el mismo: lo que cambia es la capa de abajo, el *embedder* de cada plataforma (Parte 1, punto 2).

**La versión final para la web.** `flutter build web` genera en `build\web` los archivos de una página web (`index.html`, `main.dart.js`…), listos para subirlos a cualquier servidor web (por ejemplo, GitHub Pages).

**Por qué no se abre con doble clic.** Abierto desde el disco (`file:///C:/…/index.html`), el navegador muestra la página en blanco. Una página normal sí carga desde el disco sus imágenes, estilos y scripts, pero una app de Flutter, al arrancar, pide con JavaScript (`fetch`) su motor gráfico (un archivo WebAssembly), sus fuentes y la lista de sus recursos, y registra un *service worker*. Por seguridad, los navegadores no permiten nada de eso en páginas abiertas desde el disco: solo en las que llegan de un **servidor** (`http://` o `https://`). `flutter run -d chrome` ya arranca uno pequeño en el propio ordenador: por eso la app se ve.

**Probar la versión final con un servidor de prueba.** Python, que ya está instalado para DI, trae uno:

```powershell
flutter build web
cd build\web
python -m http.server 8000
```

En el navegador, `http://localhost:8000`: es la app final, sin la etiqueta **DEBUG**. Para parar el servidor, `Ctrl + C` en la terminal, y `cd ..\..` para volver a la carpeta del proyecto.

**¿Y iOS?** `flutter create --platforms ios .` crea igual la carpeta `ios`, con el proyecto de Xcode, y el código de `lib` también es el mismo. Pero para compilar una app de iOS hace falta un **Mac con Xcode**: Apple no permite hacerlo en Windows ni en Linux. Lo mismo pasa con las apps de escritorio de Windows, que necesitan Visual Studio: es la línea con `[X]` de `flutter doctor` (paso 3).

**Comprueba que…** en Chrome se abre la misma app, con la etiqueta **DEBUG**, y el botón **+** aumenta el número; en el proyecto ha aparecido la carpeta `web`; y en `http://localhost:8000` se abre la versión final, sin **DEBUG**.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Qué hubo que cambiar en `lib/main.dart` para que la app funcione en Chrome?
2. ¿Por qué se puede compilar la misma app para Android y para la web, y para iOS no, desde este ordenador?
3. En la web, ¿la app usa los controles HTML del navegador (botones, textos)?

<details>
<summary>Respuestas</summary>

1. Nada. Solo se añadió la plataforma web al proyecto (`flutter create --platforms web .`): el código de la app es el mismo para todas las plataformas. Eso es lo que significa multiplataforma.
2. Para Android y la web, las herramientas están en Windows: el SDK de Android y el compilador de Dart a JavaScript. Para iOS hace falta Xcode, que solo funciona en un Mac.
3. No: Flutter dibuja sus propios widgets con su motor (Parte 1, punto 1), también en el navegador. Por eso la app se ve igual en el móvil y en Chrome.

</details>

---

## Si algo falla

| Qué pasa | Qué hacer |
|---|---|
| `flutter` no se reconoce | Falta `C:\dam2627\flutter\bin` en el `Path` (paso 2), o el terminal se abrió antes: ciérralo y abre otro. Comprueba también que existe `C:\dam2627\flutter\bin\flutter.bat` (paso 1) |
| `Unable to find git in your PATH` | Falta Git: instálalo (paso 0) y abre un terminal nuevo |
| Errores raros al instalar o compilar, con rutas que llevan ñ, tildes, espacios u OneDrive | El SDK o el proyecto no están en `C:\dam2627`. Muévelos ahí (paso 1) |
| `flutter doctor` sigue diciendo *cmdline-tools component is missing* | En el SDK Manager de Android Studio, marca **Android SDK Command-line Tools (latest)** y **Apply** (paso 3) |
| `Android license status unknown` | `flutter doctor --android-licenses` y acepta todas con `y` |
| VS Code dice *Could not find a Flutter SDK* | Cierra y abre VS Code. Si sigue, **Locate SDK** → `C:\dam2627\flutter` |
| Al pulsar F5 la app se abre en Chrome | El dispositivo de la barra de estado de VS Code (abajo a la derecha) es Chrome: elige el emulador. Si no sale, arráncalo con `flutter emulators --launch Pixel_9` |
| Al abrir `build\web\index.html` con doble clic, la página sale en blanco | Es normal: necesita un servidor. Usa `python -m http.server 8000` en `build\web` y abre `http://localhost:8000` (paso 9) |
| `python` no se reconoce al arrancar el servidor | Python no está instalado o no está en el `Path`: es el mismo que se usa en DI |
| `flutter run -d chrome` dice que no encuentra el dispositivo | Falta Chrome, o Flutter no lo encuentra: prueba con Edge, `flutter run -d edge` |
| La primera ejecución parece no avanzar | Es normal: la primera compilación tarda entre 5 y 10 minutos. En la consola de depuración se ve `Running Gradle task 'assembleDebug'…` |
| El *hot reload* no hace nada | El archivo no se ha guardado, o la app no se ejecutó con F5 (en modo *debug*). Con `flutter run` en el terminal, pulsa `r` |
| Error al compilar, de Gradle o de Java (`jlink`…) | Es el mismo problema que en ValoraPMDM, con un Gradle abierto por VS Code: mira esa guía, apartado «Si algo falla» |

## Los archivos del proyecto

```
C:\dam2627
├── flutter                   el SDK de Flutter (paso 1)
└── pmdm
    └── hola_flutter          el proyecto (paso 5)
        ├── android           el proyecto de Android, generado
        ├── web               la página que carga la app en el navegador (paso 9)
        ├── lib
        │   └── main.dart     el código de la app (pasos 7 y 8)
        ├── test
        │   └── widget_test.dart   una prueba automática del contador
        ├── pubspec.yaml      la configuración y las dependencias
        └── build             lo compilado (no se toca)
```
