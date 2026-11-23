# App Flutter - ValoraFlutter

## Introducción

En esta guía aprenderás a desarrollar **ValoraFlutter**: la app **ValoraPMDM**, hecha otra vez, ahora con **Flutter**. Muestra las valoraciones anónimas del módulo de PMDM de cada evaluación: la nota media, sus estrellas, el número de valoraciones y cuántas veces se ha dado cada nota, con una pestaña por evaluación.

Los datos son los mismos archivos JSON de GitHub, y las clases y la descarga son las de **ValoraDart**, sin cambiar ni una línea: un programa de Dart y una app de Flutter comparten el código que no es interfaz. Lo nuevo es la pantalla, que en Flutter se construye con **widgets**.

El objetivo no es solo que la app funcione, sino entender cómo se construye una interfaz con widgets, cómo se maneja el estado y la espera de una descarga, y **en qué se parece y en qué se diferencia de la versión nativa** con Kotlin y Compose. Es la comparación entre Android nativo y Flutter que pide la programación del módulo.

El desarrollo se divide en dos partes:

- **Parte 1. Diseño y funcionamiento:** qué hace la app, los widgets, cómo se organiza el código y cómo se mueve la información, y la comparación con ValoraPMDM.
- **Parte 2. Implementación paso a paso:** se crea el proyecto, se reutiliza el código de ValoraDart, se escribe la pantalla, se prueba y se genera el APK.

La teoría sale del libro de McGraw-Hill, *Programación multimedia y dispositivos móviles*, **unidad 4, apartado 3 «Widgets»**. Su caso práctico 3, «Mejorando la app del tiempo», es una app con la misma estructura que esta: un widget con estado, un `FutureBuilder` que espera a la descarga y un botón o un gesto para actualizar.

Antes, hay que haber hecho **«Hola Flutter»** (Flutter instalado y el contador funcionando) y **«ValoraDart»** (el lenguaje Dart y las dos clases que aquí se reutilizan).

## Índice

- [Parte 1 · Diseño](#parte-1--diseño)
  - [1. Qué hace la app](#1-qué-hace-la-app)
  - [2. Los widgets](#2-los-widgets)
  - [3. Cómo se organiza el código](#3-cómo-se-organiza-el-código)
  - [4. Cómo se mueve la información](#4-cómo-se-mueve-la-información)
  - [5. Decisiones de diseño](#5-decisiones-de-diseño)
  - [6. ValoraPMDM frente a ValoraFlutter](#6-valorapmdm-frente-a-valoraflutter)
- [Parte 2 · Implementación](#parte-2--implementación)
  - [Paso 1 · Crear el proyecto](#paso-1--crear-el-proyecto)
  - [Paso 2 · La librería http y el permiso de Internet](#paso-2--la-librería-http-y-el-permiso-de-internet)
  - [Paso 3 · Las clases y la descarga de ValoraDart](#paso-3--las-clases-y-la-descarga-de-valoradart)
  - [Paso 4 · La pantalla](#paso-4--la-pantalla)
  - [Paso 5 · Una prueba automática](#paso-5--una-prueba-automática)
  - [Paso 6 · Probar la app](#paso-6--probar-la-app)
  - [Paso 7 · Generar el APK](#paso-7--generar-el-apk)
- [Si algo falla](#si-algo-falla)
- [Los archivos del proyecto](#los-archivos-del-proyecto)

---

# Parte 1 · Diseño

## 1. Qué hace la app

```
┌──────────────────────────────────────┐
│ Valoraciones de PMDM              ↻  │
│  1ª evaluación  │  2ª evaluación     │
│  ═════════════                       │
│                                      │
│                3.8                   │
│             ★ ★ ★ ★ ☆                │
│           13 valoraciones            │
│  ──────────────────────────────────  │
│  5 ★ ███████░░░░░░░░░░░░░░░░░░  3    │
│  4 ★ ██████████████░░░░░░░░░░░  6    │
│  3 ★ ███████░░░░░░░░░░░░░░░░░░  3    │
│  2 ★ ██░░░░░░░░░░░░░░░░░░░░░░░  1    │
│  1 ★ ░░░░░░░░░░░░░░░░░░░░░░░░░  0    │
└──────────────────────────────────────┘
```

| Nº | Requisito |
|---|---|
| R1 | Al abrir, la app descarga las valoraciones de la 1ª, la 2ª y la 3ª evaluación, las que existan. Mientras descarga, se ve un círculo de progreso. |
| R2 | Hay una **pestaña por cada evaluación** con valoraciones. Las que no tienen archivo (404) no aparecen. |
| R3 | En cada pestaña: la **media** con un decimal, las **estrellas** (la media redondeada), el **número de valoraciones** y, de la nota 5 a la 1, **cuántas veces se ha dado cada una**, con una barra proporcional. |
| R4 | El botón **↻** vuelve a descargarlo todo. |
| R5 | Si la descarga falla (sin conexión, error del servidor), la app dice el motivo y no se cierra. Con **↻** se puede volver a intentar. |
| R6 | Al girar el móvil no se pierde nada ni se vuelve a descargar. Si en horizontal no cabe todo, se puede desplazar hacia abajo. |
| R7 | Las valoraciones son **anónimas**: solo un número para distinguirlas y la nota. |

Los datos son los mismos que en ValoraPMDM y ValoraDart: los archivos `valora1.json`, `valora2.json` y `valora3.json` de `valora/ejemplo/`, con medias 3.8 y 4.4, y sin archivo de la 3ª evaluación.

## 2. Los widgets

Esta sección resume el apartado 3 del libro (MGH, U4, 3.1 a 3.7). En la columna de la derecha está dónde aparece cada cosa en ValoraFlutter.

### 2.1 · Todo es un widget (MGH 3.1 a 3.3)

En Flutter, **todo es un widget**: un texto, un botón, una fila, el espacio alrededor de algo, la pantalla entera. Los widgets son **clases de Dart**, con argumentos posicionales y con nombre en su constructor, y se meten unos dentro de otros formando un **árbol**. Un widget lleva dentro otro con el argumento `child`, o varios con `children`.

> **Flutter es declarativo**, como Compose. La interfaz se describe a partir de unos datos, el **estado**, y cuando los datos cambian, la interfaz se vuelve a construir sola. Lo contrario es lo **imperativo**, el Android tradicional con XML: la vista se crea una vez y se cambia desde el código, elemento a elemento (`texto.setText(…)`).

El árbol de la pantalla de ValoraFlutter, con una pestaña abierta:

```
MaterialApp
└── PantallaValora                       (con estado)
    └── FutureBuilder
        └── DefaultTabController
            └── Scaffold
                ├── appBar: AppBar
                │   ├── title: Text "Valoraciones de PMDM"
                │   ├── actions: IconButton ↻
                │   └── bottom: TabBar ── Tab, Tab
                └── body: TabBarView
                    └── ResumenEvaluacion        (sin estado)
                        └── ListView
                            ├── Text "3.8"
                            ├── Estrellas ── Row ── Icon ×5
                            ├── Text "13 valoraciones"
                            ├── Divider
                            └── FilaRecuento ×5 ── Row
                                ├── Text "5", Icon ★
                                ├── Expanded ── LinearProgressIndicator
                                └── Text "3"
```

Al programar en Flutter se crean **widgets propios** a partir de los que trae Flutter: `ResumenEvaluacion`, `Estrellas`, `FilaRecuento`… Cada uno, en su clase, y con su método **`build`**, que devuelve el árbol que lo forma. Así el código no queda todo anidado en un solo sitio.

### 2.2 · Widgets sin estado y con estado (MGH 3.3 y 3.6)

| | `StatelessWidget` (sin estado) | `StatefulWidget` (con estado) |
|---|---|---|
| Qué es | Un widget que **solo muestra lo que recibe** en su constructor | Un widget con **datos que cambian** mientras se usa: su estado |
| Cómo se escribe | Una clase que hereda de `StatelessWidget`, con su método `build` | **Dos clases**: el widget, que hereda de `StatefulWidget` y crea su estado con `createState`, y el estado, que hereda de `State` y tiene los datos y el `build` |
| Su ciclo de vida | Constructor → `build`. Después, `build` cada vez que hay que redibujarlo | Constructor del widget → `createState` → constructor del estado → **`initState`** (una sola vez) → `build`. Después, **`setState`** → `build` otra vez. Al final, `dispose` |
| En ValoraFlutter | `ValoraApp`, `ResumenEvaluacion`, `Estrellas`, `FilaRecuento`, `Aviso` | `PantallaValora`, con su estado `_PantallaValoraState` |

Tres métodos del estado que se usan en ValoraFlutter:

| Método | Cuándo se ejecuta | En ValoraFlutter |
|---|---|---|
| `initState` | Una sola vez, al crear el estado: es donde se empieza a cargar lo que viene de fuera (la red, una base de datos). Lo primero, `super.initState()` | Empieza la primera descarga |
| `build` | Cada vez que hay que dibujar el widget | Dibuja la pantalla según cómo vaya la descarga |
| `setState(() { … })` | Cuando se llama: cambia el estado y marca el widget para reconstruirlo (vuelve a ejecutar `build`) | El botón ↻: una descarga nueva |

**El estado no se pierde al redibujar.** `createState` solo se llama la primera vez que el widget entra en el árbol; las siguientes, Flutter reutiliza el mismo estado. Según el libro, es el equivalente a guardar los datos en un **ViewModel** en Android.

### 2.3 · Los widgets que se usan (MGH 3.4)

| Widget | Qué hace |
|---|---|
| `MaterialApp` | La app de Material Design: título, colores (`theme`) y pantalla de inicio (`home`) |
| `Scaffold` | La estructura de una pantalla de Material: barra de arriba (`appBar`), contenido (`body`)… |
| `AppBar` | La barra de arriba: título, botones (`actions`) y, debajo, las pestañas (`bottom`) |
| `DefaultTabController`, `TabBar`, `Tab` y `TabBarView` | Las pestañas: el controlador sabe cuántas hay y cuál está elegida, `TabBar` dibuja las pestañas y `TabBarView`, el contenido de cada una |
| `ListView` | Una lista de widgets que se desplaza si no cabe |
| `Row` y `Column` | Widgets en fila (horizontal) o en columna (vertical) |
| `Expanded` | Dentro de una `Row`, hace que un hijo ocupe todo el espacio que sobra |
| `Center`, `Padding` y `SizedBox` | Centrar, dejar espacio alrededor y dar un tamaño fijo |
| `Text`, `Icon`, `Divider` | Un texto, un icono y una línea de separación |
| `IconButton` | Un botón con un icono. Al pulsarlo, ejecuta su `onPressed` |
| `CircularProgressIndicator` y `LinearProgressIndicator` | El círculo de espera, y una barra: aquí, la del recuento, con su parte llena en `value` (de 0 a 1) |

### 2.4 · La interacción: `onPressed` (MGH 3.5)

Los widgets preparados para la interacción, como los botones, reciben en el argumento con nombre **`onPressed`** la función que se ejecuta al pulsarlos (el gesto *tap*). Para otros gestos, como deslizar, está `GestureDetector`, que es lo que usa el caso práctico 3 del libro para actualizar al deslizar hacia abajo. ValoraFlutter usa un botón, como ValoraPMDM.

### 2.5 · `FutureBuilder` (MGH 3.7)

Una descarga devuelve un `Future`: el resultado aún no está. `FutureBuilder` es un widget que **se construye según cómo vaya un `Future`**:

```dart
FutureBuilder<List<Evaluacion>>(
  future: _carga,                    // el Future que se espera
  builder: (context, snapshot) {     // se ejecuta cada vez que cambia
    // snapshot.connectionState: waiting (esperando) o done (terminado)
    // snapshot.hasError y snapshot.error: si ha fallado, y por qué
    // snapshot.data: el resultado, cuando ha terminado bien
  },
)
```

El `Future` se guarda **en el estado** y se crea en `initState`, no dentro de `build`. Si se creara en `build`, cada vez que se redibujara la pantalla empezaría una descarga nueva. Es lo que hace el ejemplo `ejemploFutureBuilder.zip` del libro.

## 3. Cómo se organiza el código

```
┌──────────────────────────────────────────────────────────────┐
│ lib/main.dart                                    paso 4      │
│   ValoraApp            la app: tema y pantalla de inicio     │
│   PantallaValora       con estado: la descarga (_carga)      │
│   Aviso                un texto centrado: error o vacío      │
│   ResumenEvaluacion    una pestaña: media, estrellas, filas  │
│   Estrellas            cinco estrellas                       │
│   FilaRecuento         una nota, su barra y sus veces        │
└─────────────┬──────────────────────────────────▲─────────────┘
              │ descargar(numero)                │ List<Valoracion>?
┌─────────────▼──────────────────────────────────┴─────────────┐
│ lib/servicio_valora.dart · de ValoraDart, igual  paso 3      │
│   descargar   pide el JSON con http y lo convierte           │
└─────────────┬────────────────────────────────────────────────┘
              │ Valoracion.fromJson(mapa)
┌─────────────▼────────────────────────────────────────────────┐
│ lib/valoracion.dart · de ValoraDart, igual       paso 3      │
│   Valoracion, Evaluacion (total, media, recuento), estrellas │
└──────────────────────────────────────────────────────────────┘
```

| Paso | Archivo | Qué contiene |
|---|---|---|
| 3 | `lib/valoracion.dart` | Los datos y los cálculos. **El mismo archivo de ValoraDart** |
| 3 | `lib/servicio_valora.dart` | La descarga, con `http`. **El mismo archivo de ValoraDart** |
| 4 | `lib/main.dart` | La interfaz: la app y sus widgets |
| 5 | `test/widget_test.dart` | Una prueba automática de la pantalla |

## 4. Cómo se mueve la información

**Al abrir (R1, R2)**

1. `main` arranca la app con `runApp(const ValoraApp())`, y `ValoraApp` muestra `PantallaValora`.
2. Flutter crea el estado de `PantallaValora` (`createState`) y ejecuta su `initState`, que empieza la descarga: `_carga = cargarEvaluaciones()`. `_carga` es un `Future`, todavía sin resolver.
3. `build` dibuja un `FutureBuilder` con ese `Future`. Como aún no ha terminado (`waiting`), su `builder` devuelve la pantalla con el círculo de progreso.
4. Mientras, `cargarEvaluaciones` llama a `descargar` con 1, 2 y 3, una detrás de otra, y crea una `Evaluacion` con cada una que tiene archivo.
5. Al terminar, el `FutureBuilder` se vuelve a construir solo: ahora `done` y con los datos. Devuelve una pestaña por evaluación, y en cada una, un `ResumenEvaluacion`.

**Al cambiar de pestaña:** `DefaultTabController` cambia la pestaña elegida y `TabBarView` muestra su contenido. No se descarga nada.

**Al pulsar ↻ (R4):** `onPressed` llama a `recargar`, que hace `setState(() { _carga = cargarEvaluaciones(); })`. El estado tiene un `Future` nuevo, `build` se vuelve a ejecutar y el `FutureBuilder` vuelve a esperar: círculo y, después, los datos.

**Si la descarga falla (R5):** `descargar` lanza una excepción y el `Future` termina con error. El `FutureBuilder` lo ve en `snapshot.hasError` y muestra un `Aviso` con el motivo. La app no se cierra, y con ↻ se reintenta.

**Al girar el móvil (R6):** en Flutter, la ventana de Android no se destruye al girar (el proyecto lo declara en el manifiesto, con `android:configChanges`). Flutter solo vuelve a ejecutar `build` con el nuevo tamaño: el estado sigue, y con él `_carga`, ya resuelto. No se descarga nada.

## 5. Decisiones de diseño

| Decisión | Por qué |
|---|---|
| **Reutilizar** `valoracion.dart` y `servicio_valora.dart` tal cual | Los datos y la descarga no dependen de cómo se muestran: el mismo código sirve para la consola y para la app. Es la ventaja de separar la interfaz de lo demás. |
| **Un solo** widget con estado, `PantallaValora` | Lo único que cambia es la descarga. El resto de widgets solo muestran lo que reciben: sin estado, son más sencillos y se pueden reutilizar. |
| Un widget propio por cada parte (`ResumenEvaluacion`, `Estrellas`, `FilaRecuento`) | Cada `build` es corto y se lee solo. Es lo que recomienda el libro (MGH 3.3) para que el código no quede todo anidado. |
| La descarga, en **`initState`**, y no en `build` | `build` se ejecuta muchas veces (al girar, al cambiar de pestaña…); `initState`, una. Si la descarga estuviera en `build`, se repetiría en cada redibujado. |
| **`FutureBuilder`** para la espera, los errores y los datos | Los tres casos quedan en un solo sitio, el `builder`, y la pantalla siempre corresponde al estado de la descarga. |
| Un método **`pantalla`** para la estructura | La barra de arriba con ↻ sale igual en los tres casos (esperando, error, datos): se escribe una vez. |
| **Sin base de datos** | ValoraPMDM guarda las valoraciones con Room para verlas sin conexión. En Flutter haría falta un paquete como `sqflite` o `drift`: se deja fuera para centrarse en los widgets. Sin conexión, la app avisa. |

## 6. ValoraPMDM frente a ValoraFlutter

| | ValoraPMDM (Android nativo) | ValoraFlutter (Flutter) |
|---|---|---|
| Lenguaje | Kotlin | Dart |
| Interfaz | Funciones `@Composable` | Widgets: clases con `build` |
| Estructura de la pantalla | `Scaffold`, `TopAppBar`, `PrimaryTabRow` y `Tab` | `Scaffold`, `AppBar`, `TabBar` y `Tab` |
| Columnas, filas y espacio | `Column`, `Row`, `Modifier.weight`, `Modifier.padding` | `Column`, `Row`, `Expanded`, `Padding` |
| Estado | `ViewModel` con un `StateFlow` | Un `StatefulWidget` con su `State` |
| Redibujar al cambiar el estado | Automático: Compose observa el `StateFlow` | `setState` |
| La espera de la descarga | Corrutinas y un estado `cargando` | `Future`, `async`/`await` y `FutureBuilder` |
| HTTP y JSON | Retrofit y Kotlinx Serialization | `http` y `jsonDecode` con `fromJson` |
| Guardar en el móvil | Room (funciona sin conexión) | Nada (sin conexión, avisa) |
| Al girar | La actividad se recrea; el `ViewModel` sobrevive | La ventana no se recrea; el estado sigue |
| Librerías | Gradle y `libs.versions.toml` | `pubspec.yaml` y pub.dev |
| Ver un cambio | Volver a compilar e instalar | Hot reload, al guardar |
| Otras plataformas | Solo Android | Android, iOS, web y escritorio con el mismo código |

---

# Parte 2 · Implementación

Cada paso dice **qué se hace**, trae el **código completo** y termina con **Comprueba que…**: no pases al siguiente hasta que se cumpla.

## Paso 1 · Crear el proyecto

En PowerShell:

```powershell
cd C:\dam2627\pmdm
flutter create --platforms android valora_flutter
code valora_flutter
```

Como en «Hola Flutter», se crea solo para Android y se abre en VS Code.

**Comprueba que…** VS Code abre la carpeta `VALORA_FLUTTER`, con `lib/main.dart` (el contador de ejemplo), `pubspec.yaml`, `android` y `test`.

## Paso 2 · La librería http y el permiso de Internet

1. En la terminal de VS Code (`Ctrl + ñ`):

   ```powershell
   flutter pub add http
   ```

   Apunta el paquete `http` en `pubspec.yaml` y lo descarga, como `dart pub add` en ValoraDart.
2. Abre `android/app/src/main/AndroidManifest.xml` y añade el permiso de Internet justo debajo de la primera línea, `<manifest …>`, como en ValoraPMDM:

   ```xml
   <manifest xmlns:android="http://schemas.android.com/apk/res/android">
       <!-- Para descargar las valoraciones de Internet -->
       <uses-permission android:name="android.permission.INTERNET" />
       <application
   ```

   Mientras se programa (modo *debug*), Flutter ya da ese permiso. Pero el APK final (paso 7) no podría descargar nada sin él.

**Comprueba que…** en `pubspec.yaml` está `http` en `dependencies` y en el manifiesto está la línea `uses-permission`.

## Paso 3 · Las clases y la descarga de ValoraDart

Copia los dos archivos de `lib` de ValoraDart en `lib` de ValoraFlutter. En PowerShell, desde `C:\dam2627\pmdm`:

```powershell
Copy-Item valora_dart\lib\valoracion.dart, valora_dart\lib\servicio_valora.dart valora_flutter\lib\
```

No se cambia nada: son exactamente los mismos archivos (paso 3 y paso 4 de ValoraDart).

**Comprueba que…** en `lib` de ValoraFlutter hay tres archivos: `main.dart`, `servicio_valora.dart` y `valoracion.dart`.

## Paso 4 · La pantalla

`lib/main.dart` es toda la interfaz. Abre el archivo y **sustituye todo** su contenido (el contador) por este:

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `import 'package:material_ui/material_ui.dart';` | Los widgets de Material, como en «Hola Flutter» |
| `import 'servicio_valora.dart';` y `import 'valoracion.dart';` | Los dos archivos de ValoraDart, que están en la misma carpeta |
| `class ValoraApp extends StatelessWidget` | La app, sin estado: el tema y la pantalla de inicio |
| `ColorScheme.fromSeed(seedColor: Colors.teal)` | Los colores de la app, a partir del verde azulado |
| `class PantallaValora extends StatefulWidget` y `createState()` | La pantalla, con estado. Su estado es `_PantallaValoraState` |
| `late Future<List<Evaluacion>> _carga;` | El estado: la descarga. `late` quiere decir «se le da valor más tarde, antes de usarla»: en `initState` |
| `@override void initState() { super.initState(); … }` | Se ejecuta una sola vez, al crear el estado. Primero, siempre, `super.initState()` |
| `Future<List<Evaluacion>> cargarEvaluaciones() async` | Descarga las tres evaluaciones con `descargar`, de ValoraDart, y se queda con las que tienen archivo |
| `final evaluaciones = <Evaluacion>[];` | Una lista vacía de evaluaciones. `<Evaluacion>` dice de qué tipo son sus elementos |
| `setState(() { _carga = cargarEvaluaciones(); });` | Cambia el estado (una descarga nueva) y avisa a Flutter para que vuelva a ejecutar `build` |
| `FutureBuilder<List<Evaluacion>>(future: _carga, builder: …)` | Construye la pantalla según cómo vaya `_carga` |
| `snapshot.connectionState != ConnectionState.done` | Aún no ha terminado: el círculo de progreso |
| `snapshot.hasError` y `snapshot.error` | Ha terminado con error, y cuál |
| `snapshot.data!` | Los datos. Es `List<Evaluacion>?` (antes de terminar no hay datos); aquí ya ha terminado bien, y `!` lo dice |
| `DefaultTabController(length: …, child: …)` | Las pestañas: cuántas hay. `TabBar` y `TabBarView` lo usan sin pasárselo |
| `[for (final e in evaluaciones) Tab(…)]` | Un `for` dentro de una lista: crea un elemento por cada evaluación. Se llama *collection for* |
| `Widget pantalla({PreferredSizeWidget? pestanas, required Widget cuerpo})` | Un método que devuelve el `Scaffold` con la barra de arriba. Las pestañas son opcionales (`?`): sin datos, no hay |
| `IconButton(icon: …, tooltip: …, onPressed: recargar)` | El botón ↻. `tooltip` es el texto que lee un lector de pantalla. `onPressed` recibe la función, sin paréntesis |
| `const Aviso(this.texto, {super.key});` | Constructor con un argumento posicional (el texto) y uno con nombre (`key`) |
| `evaluacion.media!` | En una pestaña siempre hay valoraciones, así que la media no es `null` |
| `Theme.of(context).textTheme.displayMedium` | Un estilo de letra grande, del tema de la app |
| `for (final nota in recuento.keys) FilaRecuento(…)` | Una fila por cada nota del recuento, de la 5 a la 1 |
| `mainAxisSize: MainAxisSize.min` | La fila de estrellas ocupa solo lo que necesita, y así `Center` la centra |
| `LinearProgressIndicator(value: veces / total, minHeight: 12)` | La barra: llena en la proporción de esa nota (`/` en Dart da decimal) |

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla.
// Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, solo el tema y la pantalla de inicio ──────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal)),
      home: const PantallaValora(),
    );
  }
}

// ── La pantalla: con estado, porque las valoraciones se descargan ─────
class PantallaValora extends StatefulWidget {
  const PantallaValora({super.key});

  @override
  State<PantallaValora> createState() => _PantallaValoraState();
}

class _PantallaValoraState extends State<PantallaValora> {
  // El estado: la descarga en curso o terminada. Es un Future: aún puede no haber llegado
  late Future<List<Evaluacion>> _carga;

  // initState se ejecuta una sola vez, al crear la pantalla: empieza la primera descarga
  @override
  void initState() {
    super.initState();
    _carga = cargarEvaluaciones();
  }

  // Descarga las tres evaluaciones y se queda con las que tienen archivo
  Future<List<Evaluacion>> cargarEvaluaciones() async {
    final evaluaciones = <Evaluacion>[];
    for (var numero = 1; numero <= 3; numero++) {
      final valoraciones = await descargar(numero);
      if (valoraciones != null) {
        evaluaciones.add(Evaluacion(numero, valoraciones));
      }
    }
    return evaluaciones;
  }

  // El botón de recargar: una descarga nueva. setState avisa a Flutter para que redibuje
  void recargar() {
    setState(() {
      _carga = cargarEvaluaciones();
    });
  }

  @override
  Widget build(BuildContext context) {
    // FutureBuilder dibuja una cosa u otra según cómo vaya el Future
    return FutureBuilder<List<Evaluacion>>(
      future: _carga,
      builder: (context, snapshot) {
        // 1. Aún no ha terminado: un círculo de progreso
        if (snapshot.connectionState != ConnectionState.done) {
          return pantalla(cuerpo: const Center(child: CircularProgressIndicator()));
        }
        // 2. Ha fallado (sin conexión, error del servidor): el motivo
        if (snapshot.hasError) {
          return pantalla(cuerpo: Aviso('No se han podido descargar las valoraciones.\n${snapshot.error}'));
        }
        // 3. Ha terminado bien: las evaluaciones con archivo
        final evaluaciones = snapshot.data!;
        if (evaluaciones.isEmpty) {
          return pantalla(cuerpo: const Aviso('No hay valoraciones publicadas.'));
        }
        // Una pestaña por evaluación. DefaultTabController une las pestañas con su contenido
        return DefaultTabController(
          length: evaluaciones.length,
          child: pantalla(
            pestanas: TabBar(
              tabs: [for (final e in evaluaciones) Tab(text: '${e.numero}ª evaluación')],
            ),
            cuerpo: TabBarView(
              children: [for (final e in evaluaciones) ResumenEvaluacion(e)],
            ),
          ),
        );
      },
    );
  }

  // La estructura de la pantalla, igual en todos los casos: la barra de arriba, con
  // el título y el botón de recargar, las pestañas (si las hay) y el contenido
  Widget pantalla({PreferredSizeWidget? pestanas, required Widget cuerpo}) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Valoraciones de PMDM'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'Recargar las valoraciones',
            onPressed: recargar,
          ),
        ],
        bottom: pestanas,
      ),
      body: cuerpo,
    );
  }
}

// ── Un aviso centrado: sin valoraciones, o un error ────────────────────
class Aviso extends StatelessWidget {
  final String texto;

  const Aviso(this.texto, {super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Text(texto, textAlign: TextAlign.center),
      ),
    );
  }
}

// ── El contenido de una pestaña: la media, sus estrellas y el recuento ─
class ResumenEvaluacion extends StatelessWidget {
  final Evaluacion evaluacion;

  const ResumenEvaluacion(this.evaluacion, {super.key});

  @override
  Widget build(BuildContext context) {
    final media = evaluacion.media!; // en una pestaña siempre hay valoraciones
    final total = evaluacion.total;
    final recuento = evaluacion.recuento;
    // ListView: si no cabe (por ejemplo, con el móvil girado), se desplaza
    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        Center(
          child: Text(
            media.toStringAsFixed(1),
            style: Theme.of(context).textTheme.displayMedium,
          ),
        ),
        Center(child: Estrellas(media.round())),
        Center(child: Text(total == 1 ? '1 valoración' : '$total valoraciones')),
        const Divider(height: 32),
        // Una fila por nota, de la 5 a la 1: el for dentro de la lista crea un widget por cada una
        for (final nota in recuento.keys)
          FilaRecuento(nota: nota, veces: recuento[nota]!, total: total),
      ],
    );
  }
}

// ── Cinco estrellas: las primeras, del color del tema; el resto, en gris ──
class Estrellas extends StatelessWidget {
  final int llenas;

  const Estrellas(this.llenas, {super.key});

  @override
  Widget build(BuildContext context) {
    final colores = Theme.of(context).colorScheme;
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        for (var i = 1; i <= 5; i++)
          Icon(Icons.star, color: i <= llenas ? colores.primary : colores.outlineVariant),
      ],
    );
  }
}

// ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
class FilaRecuento extends StatelessWidget {
  final int nota;
  final int veces;
  final int total;

  const FilaRecuento({super.key, required this.nota, required this.veces, required this.total});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Row(
        children: [
          SizedBox(width: 20, child: Text('$nota')),
          Icon(Icons.star, color: Theme.of(context).colorScheme.primary),
          // Expanded: la barra ocupa todo el ancho que dejan el número y la estrella
          Expanded(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8),
              child: LinearProgressIndicator(value: veces / total, minHeight: 12),
            ),
          ),
          SizedBox(width: 28, child: Text('$veces')),
        ],
      ),
    );
  }
}
```

**Comprueba que…** `flutter analyze` dice `No issues found!`. Si sale un error en `test/widget_test.dart` diciendo que no encuentra `MyApp`, es la prueba del contador: se cambia en el paso 5.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `PantallaValora` tiene estado y `ResumenEvaluacion` no?
2. ¿Qué pasaría si la descarga se hiciera dentro de `build`, con `future: cargarEvaluaciones()`?
3. ¿Qué hace `setState` en `recargar`, y qué pasaría sin él?
4. Si no hay conexión, ¿qué se ve? Recorre el camino desde `descargar` hasta la pantalla.
5. ¿Dónde está en ValoraPMDM lo que aquí hace `_PantallaValoraState`?

<details>
<summary>Respuestas</summary>

1. `PantallaValora` tiene un dato que cambia, la descarga (empieza, termina, se repite al recargar). `ResumenEvaluacion` solo muestra la evaluación que recibe en su constructor: si cambia la evaluación, Flutter crea otro `ResumenEvaluacion`.
2. Cada vez que se redibujara la pantalla (al girar, al recargar…) se crearía un `Future` nuevo y empezaría otra descarga, y el `FutureBuilder` volvería a esperar: la app no pararía de descargar. Por eso el `Future` se crea en `initState` y se guarda en el estado.
3. Cambia `_carga` por un `Future` nuevo y avisa a Flutter de que el estado ha cambiado, así que se vuelve a ejecutar `build` y el `FutureBuilder` espera a la nueva descarga. Sin `setState`, `_carga` cambiaría, pero la pantalla no se redibujaría: se seguirían viendo los datos de antes.
4. `http.get` lanza una excepción en `descargar`, que sale de `cargarEvaluaciones`, y el `Future` `_carga` termina con error. El `FutureBuilder` vuelve a construirse, `snapshot.hasError` es `true` y devuelve un `Aviso` con «No se han podido descargar las valoraciones» y el motivo.
5. En el `ValoraViewModel` (que guarda el estado y descarga) y en `ValoraUiState` (el estado de la pantalla). En Flutter, para una app pequeña, el `State` del widget hace las dos cosas.

</details>

## Paso 5 · Una prueba automática

`flutter create` trae una prueba del contador, que ya no sirve. Abre `test/widget_test.dart` y **sustituye todo** su contenido por esta prueba, que dibuja el resumen de una evaluación con datos de ejemplo, sin Internet, y comprueba lo que sale en la pantalla:

| Pieza | Qué hace |
|---|---|
| `testWidgets('…', (tester) async { … })` | Una prueba de widgets. `tester` es la herramienta para dibujar y buscar |
| `await tester.pumpWidget(…)` | Dibuja el widget, dentro de una app y una pantalla, como en la app de verdad |
| `find.text('4.0')` | Busca un texto en la pantalla |
| `expect(…, findsOneWidget)` | Comprueba que hay exactamente uno. `findsNWidgets(5)`, que hay cinco |

```dart
// Prueba del resumen de una evaluación, sin Internet: se dibuja con datos de ejemplo
// y se comprueba lo que sale en la pantalla.
import 'package:flutter_test/flutter_test.dart';
import 'package:material_ui/material_ui.dart';
import 'package:valora_flutter/main.dart';
import 'package:valora_flutter/valoracion.dart';

void main() {
  testWidgets('El resumen muestra la media, el total y el recuento', (tester) async {
    final evaluacion = Evaluacion(1, [
      Valoracion(id: 1, nota: 4),
      Valoracion(id: 2, nota: 5),
      Valoracion(id: 3, nota: 3),
    ]);

    // pumpWidget dibuja el widget, dentro de una app y una pantalla, como en la app real
    await tester.pumpWidget(MaterialApp(home: Scaffold(body: ResumenEvaluacion(evaluacion))));

    expect(find.text('4.0'), findsOneWidget); // la media: (4 + 5 + 3) / 3
    expect(find.text('3 valoraciones'), findsOneWidget);
    expect(find.byType(FilaRecuento), findsNWidgets(5)); // una fila por nota, de la 5 a la 1
  });
}
```

En la terminal:

```powershell
flutter test
```

**Comprueba que…** sale `All tests passed!` y `flutter analyze` dice `No issues found!`.

## Paso 6 · Probar la app

Arranca el emulador (`flutter emulators --launch Pixel_9`), abre `lib/main.dart` y pulsa **F5**, como en «Hola Flutter».

| Nº | Prueba | Cómo | Resultado esperado |
|---|---|---|---|
| 1 | Abrir | Al arrancar | Un momento el círculo de progreso; después, las pestañas **1ª evaluación** y **2ª evaluación**. No hay 3ª |
| 2 | 1ª evaluación | Pestaña 1ª | **3.8**, cuatro estrellas de color, «13 valoraciones» y el recuento: 5 → 3, 4 → 6, 3 → 3, 2 → 1 y 1 → 0. Lo mismo que en ValoraPMDM |
| 3 | 2ª evaluación | Pestaña 2ª (o deslizando hacia la izquierda) | **4.4** y «13 valoraciones»: 5 → 6, 4 → 6, 3 → 1 y el resto, 0 |
| 4 | Recargar | ↻ | Otra vez el círculo y, después, los mismos datos |
| 5 | Sin conexión | Modo avión en el emulador (desliza desde arriba) y ↻ | «No se han podido descargar las valoraciones.» y el motivo (`ClientException…`). Quita el modo avión y ↻: vuelven los datos |
| 6 | Girar | Elegir la 2ª y girar el emulador | Sigue en la 2ª, sin círculo de progreso: no se ha descargado nada. Se puede desplazar hasta la fila de la nota 1 |
| 7 | Hot reload | Cambia `Colors.teal` por `Colors.orange` en `ValoraApp` y guarda | La app cambia de color al momento y sigue en la misma pestaña |

**Comprueba que…** las siete pruebas dan el resultado esperado. Las medias son las mismas que en ValoraPMDM y en ValoraDart: los tres usan los mismos datos.

## Paso 7 · Generar el APK

En la terminal:

```powershell
flutter build apk
```

Genera la versión final de la app (*release*), compilada con AOT. Tarda unos minutos y deja el archivo en `build\app\outputs\flutter-apk\app-release.apk`. Se instala como el de ValoraPMDM: en el emulador, arrastrándolo a su ventana; en un móvil, copiándolo y abriéndolo.

**Comprueba que…** la app instalada desde el APK abre sin la etiqueta **DEBUG** y descarga las valoraciones.

---

## Si algo falla

| Qué pasa | Qué hacer |
|---|---|
| `Target of URI doesn't exist: 'package:http/http.dart'` | Falta `flutter pub add http` (paso 2) |
| `Target of URI doesn't exist: 'servicio_valora.dart'` | Faltan los dos archivos de ValoraDart en `lib` (paso 3) |
| Error en `test/widget_test.dart` con `MyApp` | Es la prueba del contador: cámbiala por la del paso 5 |
| La app se queda siempre en el círculo | La descarga no termina: comprueba la conexión del emulador (abre el navegador del emulador) |
| Sale siempre «No se han podido descargar…», con conexión | La URL base de `servicio_valora.dart` está mal: ábrela en el navegador con `valora1.json` al final |
| El APK no descarga nada, y en modo *debug* sí | Falta el permiso de Internet en el manifiesto (paso 2) |
| `The getter '…' isn't defined` o algo en rojo | Un nombre mal escrito: compara con el código de la guía. Dart distingue mayúsculas de minúsculas |
| El *hot reload* no muestra un cambio en `initState` | `initState` solo se ejecuta al crear el estado: para eso hace falta el *hot restart* (`Ctrl + Mayús + F5`) |
| Errores de Gradle, del JDK o de rutas | Mira «Si algo falla» de «Hola Flutter» |

## Los archivos del proyecto

```
C:\dam2627\pmdm\valora_flutter
├── android
│   └── app\src\main
│       └── AndroidManifest.xml   el permiso de Internet (paso 2)
├── lib
│   ├── main.dart                 la interfaz: la app y sus widgets (paso 4)
│   ├── servicio_valora.dart      la descarga, de ValoraDart (paso 3)
│   └── valoracion.dart           las clases, de ValoraDart (paso 3)
├── test
│   └── widget_test.dart          la prueba del resumen (paso 5)
├── pubspec.yaml                  las dependencias: material_ui y http (paso 2)
└── build                         lo compilado: el APK (paso 7)
```
