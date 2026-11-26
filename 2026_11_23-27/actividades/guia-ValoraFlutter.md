# App Flutter - ValoraFlutter

## Introducción

En esta guía aprenderás a desarrollar **ValoraFlutter**: la app **ValoraPMDM**, hecha otra vez, ahora con **Flutter**. Muestra las valoraciones anónimas del módulo de PMDM de cada evaluación: la nota media, sus estrellas, el número de valoraciones y cuántas veces se ha dado cada nota, con una pestaña por evaluación. Al tocar una nota, se abre una **segunda pantalla** con las valoraciones que la tienen.

Los datos son los mismos archivos JSON de GitHub, y las clases y la descarga son las de **ValoraDart**, sin cambiar ni una línea: un programa de Dart y una app de Flutter comparten el código que no es interfaz. Lo nuevo es la pantalla, que en Flutter se construye con **widgets**.

El objetivo no es solo que la app funcione, sino entender cómo se construye una interfaz con widgets, cómo se maneja el estado y la espera de una descarga, cómo se navega entre pantallas, y **en qué se parece y en qué se diferencia de la versión nativa** con Kotlin y Compose. Es la comparación entre Android nativo y Flutter que pide la programación del módulo.

El desarrollo se divide en dos partes:

- **Parte 1. Diseño y funcionamiento:** qué hace la app, los widgets, cómo se organiza el código y cómo se mueve la información, y la comparación con ValoraPMDM.
- **Parte 2. Implementación paso a paso:** se crea el proyecto, se reutiliza el código de ValoraDart, se escribe la pantalla, se prueba, se genera el APK y, al final, la misma app se ejecuta en el navegador.

La teoría sale del libro de McGraw-Hill, *Programación multimedia y dispositivos móviles*, **unidad 4, apartados 3 «Widgets» y 4 «Navegación, listas y bibliotecas»**. Su caso práctico 3, «Mejorando la app del tiempo», es una app con la misma estructura que esta: un widget con estado, un `FutureBuilder` que espera a la descarga y un botón o un gesto para actualizar. La segunda pantalla usa las rutas, `ListView.builder` y `ListTile` de los ejemplos del apartado 4 (`rutas.dart`, `listas.dart` y `listas2.dart`).

Antes, hay que haber hecho **«Hola Flutter»** (Flutter instalado y el contador funcionando) y **«ValoraDart»** (el lenguaje Dart y las dos clases que aquí se reutilizan).

## Índice

- [Parte 1 · Diseño](#parte-1--diseño)
  - [1. Qué hace la app](#1-qué-hace-la-app)
  - [2. Los widgets](#2-los-widgets)
  - [3. Navegación y listas](#3-navegación-y-listas)
  - [4. Cómo se organiza el código](#4-cómo-se-organiza-el-código)
  - [5. Cómo se mueve la información](#5-cómo-se-mueve-la-información)
  - [6. Decisiones de diseño](#6-decisiones-de-diseño)
  - [7. ValoraPMDM frente a ValoraFlutter](#7-valorapmdm-frente-a-valoraflutter)
- [Parte 2 · Implementación](#parte-2--implementación)
  - [Paso 1 · Crear el proyecto](#paso-1--crear-el-proyecto)
  - [Paso 2 · La librería http y el permiso de Internet](#paso-2--la-librería-http-y-el-permiso-de-internet)
  - [Paso 3 · Las clases y la descarga de ValoraDart](#paso-3--las-clases-y-la-descarga-de-valoradart)
  - [Cómo se construye la pantalla: pasos 4 a 10](#cómo-se-construye-la-pantalla-pasos-4-a-10)
    - [Paso 4 · La app más pequeña](#paso-4--la-app-más-pequeña)
    - [Paso 5 · La descarga](#paso-5--la-descarga)
    - [Paso 6 · El botón ↻ y los avisos](#paso-6--el-botón--y-los-avisos)
    - [Paso 7 · Las pestañas](#paso-7--las-pestañas)
    - [Paso 8 · El resumen de cada pestaña](#paso-8--el-resumen-de-cada-pestaña)
    - [Paso 9 · La barra animada](#paso-9--la-barra-animada)
    - [Paso 10 · La segunda pantalla](#paso-10--la-segunda-pantalla)
  - [Paso 11 · Generar el APK](#paso-11--generar-el-apk)
  - [Paso 12 · La misma app en el navegador](#paso-12--la-misma-app-en-el-navegador)
- [Si algo falla](#si-algo-falla)
- [Los archivos del proyecto](#los-archivos-del-proyecto)

---

# Parte 1 · Diseño

## 1. Qué hace la app

```
┌──────────────────────────────────────┐
│ Valoraciones de PMDM     [recargar]  │
│  1ª evaluación  │  2ª evaluación     │
│  ═════════════                       │
│                                      │
│                3.8                   │
│             * * * * ·                │
│           13 valoraciones            │
│  ──────────────────────────────────  │
│  Toca una fila para ver las …        │
│  5 * ███████                    3    │
│  4 * ██████████████             6    │
│  3 * ███████                    3    │
│  2 * ██                         1    │
│  1 *                            0    │
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
| R8 | Al **tocar una fila del recuento**, se abre una segunda pantalla con la **lista de las valoraciones con esa nota** (su número y sus estrellas). Las filas de una nota que no ha puesto nadie no responden. Se vuelve con la flecha de la barra de arriba o con el botón «atrás» del móvil. |

La pantalla de una nota, al tocar la fila del 5 en la 1ª evaluación:

```
┌──────────────────────────────────────┐
│ ←  Nota 5 · 1ª evaluación            │
│                                      │
│  •  Valoración 2                     │
│      5 de 5                          │
│  •  Valoración 6                     │
│      5 de 5                          │
│  •  Valoración 10                    │
│      5 de 5                          │
└──────────────────────────────────────┘
```

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
                │   ├── actions: IconButton (recargar)
                │   └── bottom: TabBar ── Tab, Tab
                └── body: TabBarView
                    └── ResumenEvaluacion        (sin estado)
                        └── ListView
                            ├── Text "3.8"
                            ├── Estrellas ── Row ── Icon ×5
                            ├── Text "13 valoraciones"
                            ├── Divider
                            ├── Text "Toca una fila…"
                            └── FilaRecuento ×5 ── Row
                                ├── Text "5", Icon (estrella)
                                ├── Expanded ── TweenAnimationBuilder ── LinearProgressIndicator
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

## 3. Navegación y listas

Esta sección resume el apartado 4 del libro (MGH, U4, 4.1 y 4.2).

### 3.1 · Las rutas y el `Navigator` (MGH 4.1)

En Flutter, cada pantalla tiene una **ruta**: un nombre que empieza por `/`. Las rutas se declaran en `MaterialApp`, en un mapa `routes` que dice qué pantalla corresponde a cada nombre, y la primera pantalla la da **`initialRoute`** (por eso ya no se usa `home`). Para cambiar de pantalla se usa el **`Navigator`**, que guarda las pantallas en una **pila**:

| Pieza | Qué hace | En ValoraFlutter |
|---|---|---|
| `routes: {'/': …, '/nota': …}` | Las pantallas de la app, con su ruta | `PantallaValora` en `/` y `PantallaNota` en `/nota` |
| `initialRoute: '/'` | La pantalla con la que empieza la app | La principal |
| `Navigator.pushNamed(context, '/nota')` | Pone encima de la pila la pantalla de esa ruta: es la que se ve | Al tocar una fila del recuento |
| `arguments: …` | Los datos que se le pasan a la pantalla nueva | La evaluación y la nota, en un objeto `DatosNota` |
| `ModalRoute.of(context)!.settings.arguments` | En la pantalla nueva, recoge esos datos | Al principio del `build` de `PantallaNota` |
| `Navigator.pop(context)` | Quita la pantalla de encima de la pila: se vuelve a la anterior | Lo hace la flecha que el `AppBar` pone sola en la segunda pantalla, y el botón «atrás» del móvil |

Es la misma idea que en Android con Navigation Compose: un `NavHost` con las rutas, `navigate` para ir y `popBackStack` para volver.

### 3.2 · Las listas (MGH 4.2)

| Pieza | Qué hace | En ValoraFlutter |
|---|---|---|
| `ListView(children: [...])` | Una lista con sus widgets ya escritos. Para pocos elementos | El resumen de una pestaña |
| `ListView.builder(itemCount: …, itemBuilder: …)` | Una lista que **crea cada fila cuando hace falta**, a partir de su número: `itemBuilder` recibe la posición y devuelve el widget de esa fila. Para listas largas o que dependen de los datos | Las valoraciones de una nota |
| `ListTile` | Una fila estándar de Material: icono a la izquierda (`leading`), título (`title`), subtítulo (`subtitle`) y algo a la derecha (`trailing`) | Cada valoración |
| `InkWell(onTap: …)` | Hace que cualquier widget responda al toque, con el efecto de onda de Material. Con `onTap: null`, no responde | Cada fila del recuento |

## 4. Cómo se organiza el código

```
┌──────────────────────────────────────────────────────────────┐
│ lib/main.dart                                    pasos 4-10  │
│   ValoraApp            la app: tema y pantalla de inicio     │
│   PantallaValora       con estado: la descarga (_carga)      │
│   Aviso                un texto centrado: error o vacío      │
│   ResumenEvaluacion    una pestaña: media, estrellas, filas  │
│   Estrellas            cinco estrellas                       │
│   FilaRecuento         una nota, su barra y sus veces        │
│   DatosNota            lo que se pasa a la pantalla de nota  │
│   PantallaNota         la lista de valoraciones de una nota  │
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
| 4 a 10 | `lib/main.dart` | La interfaz: la app, sus dos pantallas y sus widgets |

## 5. Cómo se mueve la información

**Al abrir (R1, R2)**

1. `main` arranca la app con `runApp(const ValoraApp())`, y `ValoraApp` muestra la pantalla de su `initialRoute`, `/`: `PantallaValora`.
2. Flutter crea el estado de `PantallaValora` (`createState`) y ejecuta su `initState`, que empieza la descarga: `_carga = cargarEvaluaciones()`. `_carga` es un `Future`, todavía sin resolver.
3. `build` dibuja un `FutureBuilder` con ese `Future`. Como aún no ha terminado (`waiting`), su `builder` devuelve la pantalla con el círculo de progreso.
4. Mientras, `cargarEvaluaciones` llama a `descargar` con 1, 2 y 3, una detrás de otra, y crea una `Evaluacion` con cada una que tiene archivo.
5. Al terminar, el `FutureBuilder` se vuelve a construir solo: ahora `done` y con los datos. Devuelve una pestaña por evaluación, y en cada una, un `ResumenEvaluacion`.

**Al cambiar de pestaña:** `DefaultTabController` cambia la pestaña elegida y `TabBarView` muestra su contenido, deslizándolo hacia un lado; también se puede cambiar deslizando el dedo sobre el contenido. El deslizamiento no está programado: `TabBarView` lo hace solo. No se descarga nada.

**Al pulsar ↻ (R4):** `onPressed` llama a `recargar`, que hace `setState(() { _carga = cargarEvaluaciones(); })`. El estado tiene un `Future` nuevo, `build` se vuelve a ejecutar y el `FutureBuilder` vuelve a esperar: círculo y, después, los datos.

**Al tocar una fila del recuento (R8):** su `InkWell` llama a `Navigator.pushNamed(context, '/nota', arguments: DatosNota(evaluacion, nota))`. El `Navigator` pone encima la pantalla de la ruta `/nota`, `PantallaNota`, que recoge los datos con `ModalRoute.of(context)!.settings.arguments`, filtra las valoraciones con esa nota (`where`) y las muestra con `ListView.builder`. No se descarga nada: las valoraciones ya estaban en la evaluación. Con la flecha o el botón «atrás», `Navigator.pop` la quita y se vuelve a la principal, en la misma pestaña.

**Si la descarga falla (R5):** `descargar` lanza una excepción y el `Future` termina con error. El `FutureBuilder` lo ve en `snapshot.hasError` y muestra un `Aviso` con el motivo. La app no se cierra, y con ↻ se reintenta.

**Al girar el móvil (R6):** en Flutter, la ventana de Android no se destruye al girar (el proyecto lo declara en el manifiesto, con `android:configChanges`). Flutter solo vuelve a ejecutar `build` con el nuevo tamaño: el estado sigue, y con él `_carga`, ya resuelto. No se descarga nada.

## 6. Decisiones de diseño

| Decisión | Por qué |
|---|---|
| **Reutilizar** `valoracion.dart` y `servicio_valora.dart` tal cual | Los datos y la descarga no dependen de cómo se muestran: el mismo código sirve para la consola y para la app. Es la ventaja de separar la interfaz de lo demás. |
| **Un solo** widget con estado, `PantallaValora` | Lo único que cambia es la descarga. El resto de widgets solo muestran lo que reciben: sin estado, son más sencillos y se pueden reutilizar. |
| Un widget propio por cada parte (`ResumenEvaluacion`, `Estrellas`, `FilaRecuento`) | Cada `build` es corto y se lee solo. Es lo que recomienda el libro (MGH 3.3) para que el código no quede todo anidado. |
| La descarga, en **`initState`**, y no en `build` | `build` se ejecuta muchas veces (al girar, al cambiar de pestaña…); `initState`, una. Si la descarga estuviera en `build`, se repetiría en cada redibujado. |
| **`FutureBuilder`** para la espera, los errores y los datos | Los tres casos quedan en un solo sitio, el `builder`, y la pantalla siempre corresponde al estado de la descarga. |
| Un método **`pantalla`** para la estructura | La barra de arriba con ↻ sale igual en los tres casos (esperando, error, datos): se escribe una vez. |
| **Rutas con nombre**, como el libro | Todas las pantallas están en un solo sitio, el mapa `routes` de `MaterialApp`, y se va a ellas por su nombre (MGH 4.1, `rutas.dart`). |
| Los datos de la pantalla de nota, en **`arguments`**, dentro de un objeto `DatosNota` | La pantalla de nota no descarga nada: recibe la evaluación ya descargada. Una clase con nombre deja claro qué recibe, mejor que un mapa o una lista sueltos. |
| Las filas sin valoraciones **no responden** | Abrir una lista vacía no dice nada: con `onTap: null`, `InkWell` no reacciona. |
| **Sin base de datos** | ValoraPMDM guarda las valoraciones con Room para verlas sin conexión. En Flutter haría falta un paquete como `sqflite` o `drift`: se deja fuera para centrarse en los widgets. Sin conexión, la app avisa. |

## 7. ValoraPMDM frente a ValoraFlutter

La versión nativa terminada está en [`ValoraPMDM.zip`](ValoraPMDM.zip), en esta misma carpeta (cómo se abre, al final de su [guía](guia-ValoraPMDM.md#el-proyecto-terminado)): ábrela en Android Studio para comparar las dos pieza a pieza.

| | ValoraPMDM (Android nativo) | ValoraFlutter (Flutter) |
|---|---|---|
| Lenguaje | Kotlin | Dart |
| Interfaz | Funciones `@Composable` | Widgets: clases con `build` |
| Estructura de la pantalla | `Scaffold`, `TopAppBar`, `PrimaryTabRow` y `Tab` | `Scaffold`, `AppBar`, `TabBar` y `Tab` |
| Colores de la app | **Dinámicos**: el tema que crea Android Studio tiene `dynamicColor = true` (en `ui/theme/Theme.kt`), y desde Android 12 la app toma los colores del fondo de pantalla del móvil (*Material You*). En el emulador sale azul; en otro móvil, de otro color; antes de Android 12, morado (`Purple40`) | **Fijos**: `ColorScheme.fromSeed(seedColor: Colors.teal)` crea todos los colores a partir del verde azulado, iguales en cualquier móvil |
| Animar las barras | `animateFloatAsState`, con `tween(1200, easing = FastOutSlowInEasing)`, y un `LaunchedEffect` para que también crezcan al abrir la app | `TweenAnimationBuilder`, con `duration` y `curve: Curves.fastOutSlowIn`: empieza siempre en `begin`, así que crecen también al abrir la app. |
| Cambiar de pestaña | El contenido cambia de golpe, y no se puede pasar a otra pestaña deslizando el dedo: `PrimaryTabRow` solo dibuja las pestañas. Para que se deslice haría falta un `HorizontalPager` | El contenido se desliza hacia un lado, y también se cambia deslizando el dedo: `TabBarView` lo trae de serie. Aunque se espere lo mismo, cada tecnología da por defecto un comportamiento distinto |
| Columnas, filas y espacio | `Column`, `Row`, `Modifier.weight`, `Modifier.padding` | `Column`, `Row`, `Expanded`, `Padding` |
| Navegar a otra pantalla | Navigation Compose: `NavHost` con las rutas, `navigate("nota/5")` y `popBackStack()` | `MaterialApp` con `routes`, `Navigator.pushNamed(…, arguments: …)` y `Navigator.pop` |
| Pasar datos a la pantalla nueva | En la ruta (`nota/{nota}`); los demás datos, en el mismo ViewModel | En `arguments`, con un objeto (`DatosNota`) |
| Una lista larga | `LazyColumn` con `items` y `ListItem` | `ListView.builder` con `ListTile` |
| Tocar una fila | `Modifier.clickable` | `InkWell(onTap: …)` |
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
New-Item -ItemType Directory -Force C:\dam2627\pmdm
cd C:\dam2627\pmdm
flutter create --platforms android valora_flutter
code valora_flutter
```

Como en «Hola Flutter», se crea solo para Android y se abre en VS Code. `New-Item` crea la carpeta `pmdm` si no existe.

Como en «Hola Flutter» (paso 5), crea `.vscode/settings.json` para que la extensión de Java no importe la carpeta `android`, y recarga VS Code (`Ctrl + Mayús + P` → **Developer: Reload Window**):

```json
{
  "java.import.gradle.enabled": false,
  "java.gradle.buildServer.enabled": "off",
  "gradle.autoDetect": "off"
}
```

**Comprueba que…** VS Code abre la carpeta `VALORA_FLUTTER`, con `lib/main.dart` (el contador de ejemplo), `pubspec.yaml`, `android`, `test` y `.vscode/settings.json`, y abajo no sale nada de «Java» ni de «Gradle».

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

   Mientras se programa (modo *debug*), Flutter ya da ese permiso. Pero el APK final (paso 11) no podría descargar nada sin él.

**Comprueba que…** en `pubspec.yaml` está `http` en `dependencies` y en el manifiesto está la línea `uses-permission`.

## Paso 3 · Las clases y la descarga de ValoraDart

Copia los dos archivos de `lib` de ValoraDart en `lib` de ValoraFlutter. En PowerShell, desde `C:\dam2627\pmdm`:

```powershell
Copy-Item valora_dart\lib\valoracion.dart, valora_dart\lib\servicio_valora.dart valora_flutter\lib\
```

No se cambia nada: son exactamente los mismos archivos (paso 3 y paso 4 de ValoraDart).

**Comprueba que…** en `lib` de ValoraFlutter hay tres archivos: `main.dart`, `servicio_valora.dart` y `valoracion.dart`.

## Cómo se construye la pantalla: pasos 4 a 10

Toda la interfaz va en `lib/main.dart`. No se escribe de una vez: se construye en siete pasos, y cada uno añade **una idea**, se ejecuta y se comprueba en el emulador antes de seguir:

| Paso | Qué se añade | Qué se aprende |
|---|---|---|
| 4 | La app más pequeña: una barra y un texto | `runApp`, `MaterialApp`, `Scaffold`, el árbol de widgets y `build` |
| 5 | La descarga, con una línea por evaluación | Un widget con estado, `initState` y `FutureBuilder` |
| 6 | El botón ↻ y los avisos | `setState`, `onPressed` y un widget propio |
| 7 | Las pestañas | `DefaultTabController`, el `for` dentro de una lista y por qué el `FutureBuilder` pasa a envolver toda la pantalla |
| 8 | El resumen de cada pestaña | Widgets propios con datos, `Theme.of(context)`, `Row` y `Expanded` |
| 9 | La barra animada | `TweenAnimationBuilder` |
| 10 | La segunda pantalla | Rutas, `Navigator`, `InkWell` y `ListView.builder` |

En cada paso se separa lo que **ya se sabe de Dart** (ValoraDart) de lo que es **nuevo de Flutter**. Al final de cada paso está `lib/main.dart` completo, desplegable, para compararlo con el propio.

Para ver cada cambio, la app se deja en marcha en el emulador (**Run**, encima de `void main()`) y se guarda el archivo: el *hot reload* la actualiza al momento, como en «Hola Flutter». Cuando un paso necesita reiniciar la app, lo dice.

[`ValoraFlutter-pasos.zip`](ValoraFlutter-pasos.zip) trae el proyecto con los pasos 1 a 3 hechos y, en la carpeta `pasos`, el `main.dart` de cada paso, del 4 al 10. Sirve para ver cualquier paso en el emulador, o para seguir desde un paso concreto. Su `README.md` explica cómo se usa.

## Paso 4 · La app más pequeña

Abre `lib/main.dart` y **sustituye todo** su contenido (el contador) por este:

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y la pantalla de inicio ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
      // La pantalla de inicio: una barra arriba y un texto en el centro
      home: Scaffold(
        appBar: AppBar(title: const Text('Valoraciones de PMDM')),
        body: const Center(child: Text('Aquí irán las valoraciones')),
      ),
    );
  }
}
```

Es una app que solo muestra una barra con el título y un texto en el centro. Pero ya tiene la forma de toda app de Flutter.

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `void main() { runApp(…); }` | El punto de entrada, como en ValoraDart |
| `class ValoraApp extends StatelessWidget` | Una clase que hereda de otra |
| `const ValoraApp({super.key});` | Un constructor `const` con un argumento con nombre, `key`, que se pasa a la clase de la que hereda (`super.key`) |
| `@override Widget build(…)` | Sobrescribir un método de la clase padre, como `toString` en `Valoracion` |
| `MaterialApp(title: …, theme: …, home: …)` | Crear un objeto con argumentos con nombre, como `Valoracion(id: 1, nota: 4)` |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `runApp(const ValoraApp())` | Pone el widget en la pantalla: es la raíz del árbol |
| `StatelessWidget` | Un widget **sin estado**: solo muestra lo que recibe |
| `build(BuildContext context)` | El método que **describe** qué hay que dibujar. Flutter lo llama cuando necesita dibujar el widget; nunca se llama a mano |
| `MaterialApp` | La app de Material Design: el título, los colores (`theme`) y la pantalla de inicio (`home`) |
| `Scaffold` | La estructura de una pantalla de Material: la barra de arriba (`appBar`) y el contenido (`body`) |
| `AppBar`, `Center`, `Text` | La barra de arriba, un widget que centra a su hijo (`child`) y un texto |

**Por qué se escribe así**

- **La interfaz es un árbol de objetos.** Cada widget es un objeto de una clase de Dart, y se meten unos dentro de otros con los argumentos `child`, `body`, `appBar`…: `MaterialApp` → `Scaffold` → `AppBar` y `Center` → `Text`. No hay un archivo aparte con el diseño, como el XML del Android tradicional: el diseño **es** código Dart.
- **`build` no dibuja: describe.** Devuelve el árbol de widgets que tiene que haber, y Flutter se encarga de pintarlo. Si algo cambia, Flutter vuelve a llamar a `build` y compara el árbol nuevo con el anterior. Es la forma **declarativa** de Compose: en ValoraPMDM, las funciones `@Composable` hacen lo mismo.
- **`const`** delante de un widget dice que no cambia nunca: Flutter lo crea una sola vez y no lo vuelve a construir al redibujar. VS Code avisa cuando se puede poner y no está.
- **`super.key`**: todos los widgets reciben una `key` opcional, que Flutter usa para distinguir widgets iguales. Aquí no se usa, pero se pasa siempre, por convenio.

`flutter create` también trae una prueba automática del contador, `test/widget_test.dart`, que busca la clase `MyApp`. Como ya no existe, daría un error: en el Explorador, **borra** `test/widget_test.dart` (clic derecho → *Delete*).

**Comprueba que…** al ejecutar la app (**Run**), se ve la barra de arriba con «Valoraciones de PMDM» y, en el centro, «Aquí irán las valoraciones». `flutter analyze` dice `No issues found!`.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Quién llama a `build`?
2. ¿Qué pasaría al guardar si cambiaras `'Aquí irán las valoraciones'` por otro texto, con la app en marcha?

<details>
<summary>Respuestas</summary>

1. Flutter, cuando necesita dibujar el widget: al empezar y cada vez que hay que redibujarlo. El código nunca lo llama.
2. El *hot reload* vuelve a ejecutar `build` con el texto nuevo, y la pantalla cambia al momento, sin reiniciar la app.

</details>

## Paso 5 · La descarga

Ahora la pantalla descarga las valoraciones con `descargar`, de ValoraDart, y muestra una línea por evaluación. Para eso hace falta un widget **con estado**: la descarga empieza, tarda y termina, y la pantalla tiene que cambiar mientras tanto.

**Los cambios:**

1. Debajo de la primera línea `import`, los dos archivos de ValoraDart:

   ```dart
   import 'servicio_valora.dart';
   import 'valoracion.dart';
   ```

2. En `ValoraApp`, la pantalla de inicio deja de ser el `Scaffold` escrito ahí y pasa a ser un widget propio, `PantallaValora`:

   ```dart
   home: const PantallaValora(),
   ```

3. Al final del archivo, la pantalla. Son **dos clases**: el widget y su estado.

   ```dart
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

     @override
     Widget build(BuildContext context) {
       return Scaffold(
         appBar: AppBar(title: const Text('Valoraciones de PMDM')),
         // FutureBuilder dibuja una cosa u otra según cómo vaya el Future
         body: FutureBuilder<List<Evaluacion>>(
           future: _carga,
           builder: (context, snapshot) {
             // 1. Aún no ha terminado: un círculo de progreso
             if (snapshot.connectionState != ConnectionState.done) {
               return const Center(child: CircularProgressIndicator());
             }
             // 2. Ha fallado (sin conexión, error del servidor): el motivo
             if (snapshot.hasError) {
               return Center(
                 child: Text(
                   'No se han podido descargar las valoraciones.\n${snapshot.error}',
                 ),
               );
             }
             // 3. Ha terminado bien: de momento, una línea por evaluación
             final evaluaciones = snapshot.data!;
             return Column(
               children: [
                 for (final e in evaluaciones)
                   Text(
                     '${e.numero}ª evaluación: ${e.total} valoraciones, '
                     'media ${e.media!.toStringAsFixed(1)}',
                   ),
               ],
             );
           },
         ),
       );
     }
   }
   ```

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `cargarEvaluaciones() async` y `await descargar(numero)` | Una función asíncrona que espera cada descarga, como el `main` de ValoraDart |
| `Future<List<Evaluacion>>` | Lo que devuelve una función `async`: una lista de evaluaciones que todavía no ha llegado |
| `final evaluaciones = <Evaluacion>[];` | Una lista vacía; `<Evaluacion>` dice de qué tipo son sus elementos |
| `_carga`, `_PantallaValoraState` | Un nombre que empieza por `_` es **privado** del archivo |
| `late Future<…> _carga;` | `late`: la variable no tiene valor al declararla, pero lo tendrá antes de usarla. Aquí se le da en `initState` |
| `(context, snapshot) { … }` | Una función sin nombre que se pasa como argumento, como `(v) => v.nota` en el `map` de ValoraDart, pero con llaves porque tiene varias líneas |
| `snapshot.data!` | `data` es `List<Evaluacion>?`, y `!` dice que aquí no es `null` |
| `for (final e in evaluaciones) Text(…)` dentro de `[…]` | Un `for` dentro de una lista: crea un elemento por cada evaluación (*collection for*) |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `StatefulWidget` y `State` | Un widget **con estado** se escribe con dos clases: el widget, que solo crea su estado (`createState`), y el estado, que guarda los datos que cambian y tiene el `build` |
| `initState()` | Se ejecuta **una sola vez**, cuando se crea el estado. Es donde se empieza lo que viene de fuera, como la descarga. Lo primero, siempre, `super.initState()` |
| `FutureBuilder` | Un widget que **espera un `Future`** y se vuelve a construir cuando cambia: al empezar a esperar y al terminar |
| `builder` | La función que dice **qué se dibuja** en cada momento. La llama Flutter, cada vez que el `FutureBuilder` se construye |
| `snapshot` | Una «foto» del `Future` en ese momento (un `AsyncSnapshot`): `connectionState` (`waiting`, esperando, o `done`, terminado), `hasError` y `error` (si ha fallado, y por qué) y `data` (el resultado) |
| `CircularProgressIndicator` | El círculo que gira mientras se espera |
| `Column` | Pone sus hijos (`children`) uno debajo de otro |

**Por qué se escribe así**

**Por qué no se usa `await` en `build`.** En ValoraDart, `main` hacía `await descargar(…)`, esperaba y después escribía. `build` no puede esperar: tiene que devolver **ya** lo que se dibuja, porque la pantalla tiene que mostrar algo mientras llega la descarga. `FutureBuilder` resuelve eso sin `await`:

| Momento | `snapshot` | Lo que devuelve `builder` |
|---|---|---|
| 1. Se abre la pantalla: `build` crea el `FutureBuilder` | `connectionState` = `waiting` | El círculo de progreso |
| 2. Llega la descarga (en ese tiempo, la app no se congela) | Flutter vuelve a construir el `FutureBuilder`: `done`, con `data` | Una línea por evaluación |
| 2 bis. O la descarga falla | `done`, con `hasError` = `true` | El texto del error |

El código no llama a `builder` ni le dice cuándo volver a dibujar: lo hace Flutter, cada vez que cambia el `Future`.

**Por qué la descarga se empieza en `initState` y no en `build`.** Si se escribiera `future: cargarEvaluaciones()` dentro de `build`, cada vez que Flutter redibujara la pantalla (al girar el móvil, por ejemplo) se crearía un `Future` nuevo y empezaría otra descarga, y el `FutureBuilder` volvería a esperar: la app no pararía de descargar. Por eso el `Future` se crea **una vez**, en `initState`, y se guarda en el estado, en `_carga`.

**Por qué dos clases.** Flutter crea y tira los widgets constantemente: cada vez que se redibuja, se construyen objetos nuevos. Si los datos estuvieran en el widget, se perderían. El objeto `State` es el que **Flutter conserva** entre un dibujo y el siguiente: `createState` solo se llama la primera vez. Según el libro, es como guardar los datos en un **ViewModel** en Android: en ValoraPMDM, lo que aquí hace `_PantallaValoraState` lo hacen el `ValoraViewModel` y el `ValoraUiState`.

**Comprueba que…** al guardar, la app muestra un momento el círculo y, después, dos líneas arriba: «1ª evaluación: 13 valoraciones, media 3.8» y «2ª evaluación: 13 valoraciones, media 4.4». No hay 3ª: su archivo no existe (404), y `cargarEvaluaciones` no la añade. Con el modo avión del emulador y la app reiniciada, sale «No se han podido descargar las valoraciones.» y el motivo.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Qué pasaría si la descarga se hiciera dentro de `build`, con `future: cargarEvaluaciones()`?
2. ¿Cuántas veces se ejecuta `builder` al abrir la app, si la descarga va bien?
3. ¿Dónde está en ValoraPMDM lo que aquí hace `_PantallaValoraState`?

<details>
<summary>Respuestas</summary>

1. Cada vez que se redibujara la pantalla se crearía un `Future` nuevo y empezaría otra descarga, y el `FutureBuilder` volvería a esperar: la app no pararía de descargar. Por eso el `Future` se crea en `initState` y se guarda en el estado.
2. Al menos dos: una con `waiting`, que dibuja el círculo, y otra con `done`, que dibuja las líneas. Si algo más obliga a redibujar, como girar el móvil, se vuelve a ejecutar, pero con el `Future` ya terminado: sin descargar.
3. En el `ValoraViewModel` (que guarda el estado y descarga) y en `ValoraUiState` (el estado de la pantalla). En Flutter, para una app pequeña, el `State` del widget hace las dos cosas.

</details>

<details>
<summary><code>lib/main.dart</code> completo, tras el paso 5</summary>

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y la pantalla de inicio ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Valoraciones de PMDM')),
      // FutureBuilder dibuja una cosa u otra según cómo vaya el Future
      body: FutureBuilder<List<Evaluacion>>(
        future: _carga,
        builder: (context, snapshot) {
          // 1. Aún no ha terminado: un círculo de progreso
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          // 2. Ha fallado (sin conexión, error del servidor): el motivo
          if (snapshot.hasError) {
            return Center(
              child: Text(
                'No se han podido descargar las valoraciones.\n${snapshot.error}',
              ),
            );
          }
          // 3. Ha terminado bien: de momento, una línea por evaluación
          final evaluaciones = snapshot.data!;
          return Column(
            children: [
              for (final e in evaluaciones)
                Text(
                  '${e.numero}ª evaluación: ${e.total} valoraciones, '
                  'media ${e.media!.toStringAsFixed(1)}',
                ),
            ],
          );
        },
      ),
    );
  }
}
```

</details>

## Paso 6 · El botón ↻ y los avisos

Un botón en la barra de arriba para volver a descargar, y un widget propio, `Aviso`, para los mensajes: el error y el caso en que no hay ninguna evaluación publicada.

**Los cambios:**

1. En `_PantallaValoraState`, debajo de `cargarEvaluaciones`, el método del botón:

   ```dart
   // El botón de recargar: una descarga nueva. setState avisa a Flutter para que redibuje
   void recargar() {
     setState(() {
       _carga = cargarEvaluaciones();
     });
   }
   ```

2. En el `AppBar`, el botón, en `actions` (los botones de la derecha de la barra):

   ```dart
   appBar: AppBar(
     title: const Text('Valoraciones de PMDM'),
     actions: [
       IconButton(
         icon: const Icon(Icons.refresh),
         tooltip: 'Recargar las valoraciones',
         onPressed: recargar,
       ),
     ],
   ),
   ```

3. En el `builder`, el error pasa a ser un `Aviso`, y se añade el caso de la lista vacía:

   ```dart
   // 2. Ha fallado (sin conexión, error del servidor): el motivo
   if (snapshot.hasError) {
     return Aviso(
       'No se han podido descargar las valoraciones.\n${snapshot.error}',
     );
   }
   // 3. Ha terminado bien: las evaluaciones con archivo
   final evaluaciones = snapshot.data!;
   if (evaluaciones.isEmpty) {
     return const Aviso('No hay valoraciones publicadas.');
   }
   ```

4. Al final del archivo, el widget `Aviso`:

   ```dart
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
   ```

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `onPressed: recargar` | Pasar una **función** como argumento, sin paréntesis: se ejecutará al pulsar. Con paréntesis, `recargar()`, se ejecutaría ahora, al dibujar |
| `setState(() { … })` | Una función que recibe otra función, sin argumentos (`() { … }`) |
| `const Aviso(this.texto, {super.key});` | Un constructor con un argumento posicional, `texto`, que va directo a la propiedad, y uno con nombre, `key`. Como el `Evaluacion(this.numero, this.valoraciones)` de ValoraDart |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `setState` | Ejecuta el cambio que recibe y **avisa a Flutter** de que el estado ha cambiado: Flutter vuelve a llamar a `build` |
| `IconButton` | Un botón con un icono. `tooltip` es el texto que sale al mantenerlo pulsado y el que lee un lector de pantalla |
| `Icons.refresh` | La flecha circular de los iconos de Material |
| `Padding` y `EdgeInsets.all(24)` | Deja 24 píxeles de margen alrededor de su hijo |
| Un `StatelessWidget` propio | Un trozo de interfaz con nombre, que se reutiliza: `Aviso` se usa en dos sitios con textos distintos |

**Por qué se escribe así**

**Qué hace `setState`.** Al pulsar ↻:

1. `recargar` llama a `setState`, que ejecuta `_carga = cargarEvaluaciones()`: el estado guarda un `Future` nuevo, el de una descarga nueva.
2. `setState` avisa a Flutter, que vuelve a llamar a `build`.
3. `build` crea un `FutureBuilder` con el `Future` nuevo, que aún no ha terminado: el círculo. Al terminar, las líneas.

Sin `setState`, `_carga` cambiaría igual, pero Flutter no se enteraría: no volvería a llamar a `build`, y se seguirían viendo los datos de antes. **En Flutter, la pantalla solo cambia cuando cambia el estado y se avisa con `setState`.** Es la misma idea que el estado de Compose en ValoraPMDM.

**Por qué un widget propio.** El aviso se usa dos veces, y en el paso 7 se usará más. Escribirlo como clase evita repetir el `Center`, el `Padding` y el `Text`, y da un nombre a lo que es: un aviso. Es como sacar una función en lugar de copiar código.

**Comprueba que…**

- Arriba a la derecha está el botón ↻. Al pulsarlo, vuelve a salir el círculo y, después, las mismas dos líneas.
- Con el modo avión y ↻, sale el aviso centrado: «No se han podido descargar las valoraciones.» y el motivo. Al quitar el modo avión y pulsar ↻, vuelven los datos.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Qué pasaría sin `setState`, con solo `_carga = cargarEvaluaciones();` dentro de `recargar`?
2. ¿Qué pasaría con `onPressed: recargar()`?
3. Si no hay conexión, ¿qué se ve? Recorre el camino desde `descargar` hasta la pantalla.

<details>
<summary>Respuestas</summary>

1. La descarga nueva empezaría, pero la pantalla no se redibujaría: se seguirían viendo los datos de antes, y el círculo no saldría.
2. No compila. Con paréntesis, `recargar()` se ejecutaría al construir la pantalla, y `onPressed` recibiría lo que devuelve, que es `void`, y no una función: `This expression has a type of 'void' so its value can't be used`.
3. `http.get` lanza una excepción en `descargar`, que sale de `cargarEvaluaciones`, y el `Future` `_carga` termina con error. El `FutureBuilder` se vuelve a construir, `snapshot.hasError` es `true` y el `builder` devuelve un `Aviso` con el motivo. La app no se cierra.

</details>

<details>
<summary><code>lib/main.dart</code> completo, tras el paso 6</summary>

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y la pantalla de inicio ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
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
      ),
      // FutureBuilder dibuja una cosa u otra según cómo vaya el Future
      body: FutureBuilder<List<Evaluacion>>(
        future: _carga,
        builder: (context, snapshot) {
          // 1. Aún no ha terminado: un círculo de progreso
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }
          // 2. Ha fallado (sin conexión, error del servidor): el motivo
          if (snapshot.hasError) {
            return Aviso(
              'No se han podido descargar las valoraciones.\n${snapshot.error}',
            );
          }
          // 3. Ha terminado bien: las evaluaciones con archivo
          final evaluaciones = snapshot.data!;
          if (evaluaciones.isEmpty) {
            return const Aviso('No hay valoraciones publicadas.');
          }
          // De momento, una línea por evaluación
          return Column(
            children: [
              for (final e in evaluaciones)
                Text(
                  '${e.numero}ª evaluación: ${e.total} valoraciones, '
                  'media ${e.media!.toStringAsFixed(1)}',
                ),
            ],
          );
        },
      ),
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
```

</details>

## Paso 7 · Las pestañas

Una pestaña por evaluación, debajo del título, como en ValoraPMDM. De momento, cada pestaña muestra solo su media en el centro. Este paso cambia **dónde va** el `FutureBuilder`.

**Los cambios:** el `build` de `_PantallaValoraState` se sustituye por este, y debajo se añade el método `pantalla`:

```dart
@override
Widget build(BuildContext context) {
  // FutureBuilder dibuja una cosa u otra según cómo vaya el Future
  return FutureBuilder<List<Evaluacion>>(
    future: _carga,
    builder: (context, snapshot) {
      // 1. Aún no ha terminado: un círculo de progreso
      if (snapshot.connectionState != ConnectionState.done) {
        return pantalla(
          cuerpo: const Center(child: CircularProgressIndicator()),
        );
      }
      // 2. Ha fallado (sin conexión, error del servidor): el motivo
      if (snapshot.hasError) {
        return pantalla(
          cuerpo: Aviso(
            'No se han podido descargar las valoraciones.\n${snapshot.error}',
          ),
        );
      }
      // 3. Ha terminado bien: las evaluaciones con archivo
      final evaluaciones = snapshot.data!;
      if (evaluaciones.isEmpty) {
        return pantalla(
          cuerpo: const Aviso('No hay valoraciones publicadas.'),
        );
      }
      // Una pestaña por evaluación. DefaultTabController une las pestañas con su contenido
      return DefaultTabController(
        length: evaluaciones.length,
        child: pantalla(
          pestanas: TabBar(
            tabs: [
              for (final e in evaluaciones)
                Tab(text: '${e.numero}ª evaluación'),
            ],
          ),
          cuerpo: TabBarView(
            // De momento, la media en el centro de cada pestaña
            children: [
              for (final e in evaluaciones)
                Center(child: Text('Media: ${e.media!.toStringAsFixed(1)}')),
            ],
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
```

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `Widget pantalla({PreferredSizeWidget? pestanas, required Widget cuerpo})` | Un método con dos argumentos con nombre: `pestanas` es **opcional** y puede ser `null` (`?`); `cuerpo` es **obligatorio** (`required`) |
| `pantalla(cuerpo: …)` | La llamada sin `pestanas`: vale `null` |
| `[for (final e in evaluaciones) Tab(…)]` | El mismo `for` dentro de una lista del paso 5: una pestaña por evaluación |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `DefaultTabController(length: …, child: …)` | Sabe cuántas pestañas hay y cuál está elegida |
| `TabBar` y `Tab` | La fila de pestañas. Va en `bottom` del `AppBar`: debajo del título |
| `TabBarView` | El contenido de cada pestaña, uno por cada `Tab` y en el mismo orden. Se cambia tocando una pestaña o deslizando el dedo |
| `PreferredSizeWidget` | Un widget que dice qué alto quiere tener. `bottom` del `AppBar` solo admite estos: `TabBar` lo es |

**Por qué se escribe así**

**Cómo se entienden `TabBar` y `TabBarView` sin pasarse nada.** Ninguno de los dos recibe el `DefaultTabController`. Cada uno lo busca **hacia arriba en el árbol**, a partir de su `context`: el primer `DefaultTabController` que encuentran por encima es el suyo. Para eso sirve el `BuildContext` que recibe `build`: es **la posición del widget en el árbol**. Por eso el `DefaultTabController` tiene que estar **por encima** de los dos, y como `TabBar` va en el `AppBar` y `TabBarView` en el `body`, tiene que envolver todo el `Scaffold`.

**Por qué el `FutureBuilder` pasa a envolver el `Scaffold`.** Hasta el paso 6, el `FutureBuilder` estaba dentro, en el `body`, y solo decidía el contenido. Ahora la descarga decide también **la barra**: con datos, lleva pestañas; esperando o con error, no. Así que el `FutureBuilder` sube a lo más alto, y en cada caso devuelve una pantalla completa.

**Por qué el método `pantalla`.** Los cuatro casos (esperando, error, vacío y con datos) necesitan el mismo `Scaffold`, con el mismo título y el mismo botón; solo cambian las pestañas y el contenido. Sin `pantalla`, el `Scaffold` estaría escrito cuatro veces.

**Comprueba que…** al guardar, debajo del título salen las pestañas **1ª evaluación** y **2ª evaluación**, cada una con su media en el centro (3.8 y 4.4). Se cambia de pestaña tocándola o deslizando el contenido hacia un lado. El deslizamiento no está programado: lo hace `TabBarView`.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Qué pasaría si el `DefaultTabController` envolviera solo el `TabBarView`?
2. Mientras se descarga, ¿hay pestañas? ¿Por qué?

<details>
<summary>Respuestas</summary>

1. El `TabBar`, que está en el `AppBar`, buscaría hacia arriba un `DefaultTabController` y no lo encontraría: Flutter da el error `No TabController for TabBar`, porque no sabe cuántas pestañas hay ni cuál está elegida.
2. No. Mientras se espera, el `builder` llama a `pantalla` sin `pestanas`, así que `bottom` es `null`. Las pestañas dependen de las evaluaciones, y todavía no han llegado.

</details>

<details>
<summary><code>lib/main.dart</code> completo, tras el paso 7</summary>

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y la pantalla de inicio ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
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
          return pantalla(
            cuerpo: const Center(child: CircularProgressIndicator()),
          );
        }
        // 2. Ha fallado (sin conexión, error del servidor): el motivo
        if (snapshot.hasError) {
          return pantalla(
            cuerpo: Aviso(
              'No se han podido descargar las valoraciones.\n${snapshot.error}',
            ),
          );
        }
        // 3. Ha terminado bien: las evaluaciones con archivo
        final evaluaciones = snapshot.data!;
        if (evaluaciones.isEmpty) {
          return pantalla(
            cuerpo: const Aviso('No hay valoraciones publicadas.'),
          );
        }
        // Una pestaña por evaluación. DefaultTabController une las pestañas con su contenido
        return DefaultTabController(
          length: evaluaciones.length,
          child: pantalla(
            pestanas: TabBar(
              tabs: [
                for (final e in evaluaciones)
                  Tab(text: '${e.numero}ª evaluación'),
              ],
            ),
            cuerpo: TabBarView(
              // De momento, la media en el centro de cada pestaña
              children: [
                for (final e in evaluaciones)
                  Center(child: Text('Media: ${e.media!.toStringAsFixed(1)}')),
              ],
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
```

</details>

## Paso 8 · El resumen de cada pestaña

Cada pestaña muestra la media en grande, sus estrellas, el total de valoraciones y el recuento de cada nota con una barra, como en ValoraPMDM. Son tres widgets propios, cada uno en su clase.

**Los cambios:**

1. En el `TabBarView`, el texto provisional se sustituye por el resumen:

   ```dart
   cuerpo: TabBarView(
     children: [for (final e in evaluaciones) ResumenEvaluacion(e)],
   ),
   ```

2. Al final del archivo, los tres widgets:

   ```dart
   // ── El contenido de una pestaña: la media, sus estrellas y el recuento ─
   class ResumenEvaluacion extends StatelessWidget {
     final Evaluacion evaluacion;

     const ResumenEvaluacion(this.evaluacion, {super.key});

     @override
     Widget build(BuildContext context) {
       final media = evaluacion.media!; // en una pestaña siempre hay valoraciones
       final total = evaluacion.total;
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
           Center(
             child: Text(total == 1 ? '1 valoración' : '$total valoraciones'),
           ),
           const Divider(height: 32),
           // Una fila por nota, de la 5 a la 1: el for dentro de la lista crea un widget por cada una
           for (final nota in evaluacion.recuento.keys)
             FilaRecuento(evaluacion: evaluacion, nota: nota),
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
             Icon(
               Icons.star,
               color: i <= llenas ? colores.primary : colores.outlineVariant,
             ),
         ],
       );
     }
   }

   // ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
   class FilaRecuento extends StatelessWidget {
     final Evaluacion evaluacion;
     final int nota;

     const FilaRecuento({super.key, required this.evaluacion, required this.nota});

     @override
     Widget build(BuildContext context) {
       final veces = evaluacion.recuento[nota]!;
       final total = evaluacion.total;
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
                 // La parte llena de la barra: la proporción de esa nota, de 0 a 1
                 child: LinearProgressIndicator(
                   value: veces / total,
                   minHeight: 12,
                 ),
               ),
             ),
             SizedBox(width: 28, child: Text('$veces')),
           ],
         ),
       );
     }
   }
   ```

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `evaluacion.media!`, `evaluacion.total`, `evaluacion.recuento` | Los getters de `Evaluacion`, de ValoraDart, sin cambios |
| `media.toStringAsFixed(1)` y `media.round()` | La media con un decimal (3.8) y redondeada a un entero (4) |
| `total == 1 ? '1 valoración' : '$total valoraciones'` | El operador ternario: el singular o el plural |
| `evaluacion.recuento[nota]!` | Leer de un mapa da `int?`; `!` dice que la nota está |
| `veces / total` | `/` da un `double`: la proporción, de 0 a 1 |
| `for (var i = 1; i <= 5; i++)` dentro de `[…]` | Un `for` clásico dentro de una lista: cinco iconos |
| `FilaRecuento({super.key, required this.evaluacion, required this.nota})` | Un constructor con argumentos con nombre y obligatorios, como el de `Valoracion` |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `ListView` | Como `Column`, pero si no cabe en la pantalla se puede desplazar |
| `Row` | Pone sus hijos uno al lado de otro. `mainAxisSize: MainAxisSize.min` hace que ocupe solo lo que necesita, y así `Center` la puede centrar |
| `Expanded` | Dentro de una `Row`, hace que un hijo ocupe **todo el espacio que sobra**: aquí, la barra |
| `SizedBox(width: 20, …)` | Un ancho fijo: así los números de todas las filas quedan alineados |
| `Theme.of(context)` | El tema de la app (los colores y los estilos de letra que se dieron en `MaterialApp`) |
| `LinearProgressIndicator(value: …)` | Una barra; `value`, de 0 a 1, es la parte llena |
| `Divider` | Una línea de separación |

**Por qué se escribe así**

**Por qué tres widgets y no uno.** Todo el resumen podría ir dentro del `TabBarView`, pero quedaría un árbol enorme y anidado, difícil de leer y de cambiar. Cada widget propio es una pieza con nombre y con sus datos en el constructor: `Estrellas(4)` dibuja cuatro estrellas llenas, sin saber nada de evaluaciones. Es lo mismo que sacar funciones: en ValoraPMDM, cada parte de la pantalla es una función `@Composable`.

**Por qué no tienen estado.** Solo muestran lo que reciben: si la evaluación cambia, Flutter crea otro `ResumenEvaluacion` con la nueva. El único dato que cambia en toda la app, la descarga, está en `_PantallaValoraState`.

**Cómo llega el color.** `Theme.of(context)` busca hacia arriba en el árbol, como el `TabBar` buscaba su `DefaultTabController` en el paso 7, y encuentra el tema de `MaterialApp`. Por eso los widgets no reciben el color: lo piden. Si se cambia `Colors.teal` en `ValoraApp`, cambian todos.

**Comprueba que…** la pestaña de la 1ª evaluación muestra **3.8**, cuatro estrellas de color, «13 valoraciones» y el recuento: 5 → 3, 4 → 6, 3 → 3, 2 → 1 y 1 → 0, con barras de distinto largo. La 2ª: **4.4**, «13 valoraciones», 5 → 6, 4 → 6, 3 → 1 y el resto, 0. Al girar el emulador, el resumen se puede desplazar hasta la fila de la nota 1.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `PantallaValora` tiene estado y `ResumenEvaluacion` no?
2. ¿Qué pasaría si se quitara el `Expanded` de la barra?

<details>
<summary>Respuestas</summary>

1. `PantallaValora` tiene un dato que cambia, la descarga (empieza, termina, se repite al recargar). `ResumenEvaluacion` solo muestra la evaluación que recibe en su constructor: si cambia la evaluación, Flutter crea otro `ResumenEvaluacion`.
2. Una `Row` deja que cada hijo ocupe el ancho que quiera, y una `LinearProgressIndicator` quiere todo el que le den, sin límite: Flutter da el error `BoxConstraints forces an infinite width` y no dibuja la fila. `Expanded` le da un ancho concreto: el que sobra.

</details>

<details>
<summary><code>lib/main.dart</code> completo, tras el paso 8</summary>

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y la pantalla de inicio ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
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
          return pantalla(
            cuerpo: const Center(child: CircularProgressIndicator()),
          );
        }
        // 2. Ha fallado (sin conexión, error del servidor): el motivo
        if (snapshot.hasError) {
          return pantalla(
            cuerpo: Aviso(
              'No se han podido descargar las valoraciones.\n${snapshot.error}',
            ),
          );
        }
        // 3. Ha terminado bien: las evaluaciones con archivo
        final evaluaciones = snapshot.data!;
        if (evaluaciones.isEmpty) {
          return pantalla(
            cuerpo: const Aviso('No hay valoraciones publicadas.'),
          );
        }
        // Una pestaña por evaluación. DefaultTabController une las pestañas con su contenido
        return DefaultTabController(
          length: evaluaciones.length,
          child: pantalla(
            pestanas: TabBar(
              tabs: [
                for (final e in evaluaciones)
                  Tab(text: '${e.numero}ª evaluación'),
              ],
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
        Center(
          child: Text(total == 1 ? '1 valoración' : '$total valoraciones'),
        ),
        const Divider(height: 32),
        // Una fila por nota, de la 5 a la 1: el for dentro de la lista crea un widget por cada una
        for (final nota in evaluacion.recuento.keys)
          FilaRecuento(evaluacion: evaluacion, nota: nota),
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
          Icon(
            Icons.star,
            color: i <= llenas ? colores.primary : colores.outlineVariant,
          ),
      ],
    );
  }
}

// ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
class FilaRecuento extends StatelessWidget {
  final Evaluacion evaluacion;
  final int nota;

  const FilaRecuento({super.key, required this.evaluacion, required this.nota});

  @override
  Widget build(BuildContext context) {
    final veces = evaluacion.recuento[nota]!;
    final total = evaluacion.total;
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
              // La parte llena de la barra: la proporción de esa nota, de 0 a 1
              child: LinearProgressIndicator(
                value: veces / total,
                minHeight: 12,
              ),
            ),
          ),
          SizedBox(width: 28, child: Text('$veces')),
        ],
      ),
    );
  }
}
```

</details>

## Paso 9 · La barra animada

Las barras del recuento crecen desde vacías hasta su largo, al abrir la app y al cambiar de pestaña, como en ValoraPMDM (paso 13 de su guía).

**El cambio:** en `FilaRecuento`, la `LinearProgressIndicator` y su comentario se sustituyen por esto:

```dart
// La barra crece desde vacía hasta su proporción, en 1200 ms,
// como en ValoraPMDM. TweenAnimationBuilder va cambiando
// valor de begin a end, y con cada valor vuelve a llamar a builder
child: TweenAnimationBuilder<double>(
  tween: Tween(begin: 0, end: veces / total),
  duration: const Duration(milliseconds: 1200),
  curve: Curves.fastOutSlowIn,
  builder: (context, valor, _) =>
      LinearProgressIndicator(value: valor, minHeight: 12),
),
```

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `TweenAnimationBuilder<double>` | Un tipo genérico: lo que se anima es un `double` |
| `(context, valor, _) => …` | Una función flecha con tres parámetros. `_` es el nombre que se da a un parámetro que no se usa |
| `const Duration(milliseconds: 1200)` | Un objeto de la librería de Dart: una duración |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `Tween(begin: 0, end: veces / total)` | De dónde a dónde va el valor: de 0 (vacía) a la proporción de esa nota |
| `TweenAnimationBuilder` | Va cambiando el valor de `begin` a `end` durante `duration` y, con cada valor, vuelve a llamar a `builder` |
| `curve: Curves.fastOutSlowIn` | Cómo avanza: deprisa al principio y despacio al final. Es la misma curva que `FastOutSlowInEasing` en Compose |

**Por qué se escribe así.** Una animación en Flutter es lo mismo que todo lo demás: **un `build` que se repite**. `TweenAnimationBuilder` llama a `builder` en cada fotograma, unas sesenta veces por segundo, cada vez con un `valor` un poco mayor, y en cada llamada se devuelve una barra un poco más llena. No hay que mover nada a mano: solo decir cómo se dibuja la barra con un valor dado.

La barra vuelve a crecer al cambiar de pestaña porque `TabBarView` construye la página nueva, y con ella un `TweenAnimationBuilder` nuevo, que empieza otra vez en 0. Los 1200 ms son los de ValoraPMDM: la página nueva entra deslizándose, y la barra empieza a crecer mientras se desliza. Con menos tiempo, cuando la página termina de llegar, la barra casi ha terminado y apenas se ve.

**Comprueba que…** al abrir la app y al cambiar de pestaña, las barras crecen desde la izquierda, deprisa al principio y despacio al final.

<details>
<summary><code>lib/main.dart</code> completo, tras el paso 9</summary>

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y la pantalla de inicio ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
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
          return pantalla(
            cuerpo: const Center(child: CircularProgressIndicator()),
          );
        }
        // 2. Ha fallado (sin conexión, error del servidor): el motivo
        if (snapshot.hasError) {
          return pantalla(
            cuerpo: Aviso(
              'No se han podido descargar las valoraciones.\n${snapshot.error}',
            ),
          );
        }
        // 3. Ha terminado bien: las evaluaciones con archivo
        final evaluaciones = snapshot.data!;
        if (evaluaciones.isEmpty) {
          return pantalla(
            cuerpo: const Aviso('No hay valoraciones publicadas.'),
          );
        }
        // Una pestaña por evaluación. DefaultTabController une las pestañas con su contenido
        return DefaultTabController(
          length: evaluaciones.length,
          child: pantalla(
            pestanas: TabBar(
              tabs: [
                for (final e in evaluaciones)
                  Tab(text: '${e.numero}ª evaluación'),
              ],
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
        Center(
          child: Text(total == 1 ? '1 valoración' : '$total valoraciones'),
        ),
        const Divider(height: 32),
        // Una fila por nota, de la 5 a la 1: el for dentro de la lista crea un widget por cada una
        for (final nota in evaluacion.recuento.keys)
          FilaRecuento(evaluacion: evaluacion, nota: nota),
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
          Icon(
            Icons.star,
            color: i <= llenas ? colores.primary : colores.outlineVariant,
          ),
      ],
    );
  }
}

// ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
class FilaRecuento extends StatelessWidget {
  final Evaluacion evaluacion;
  final int nota;

  const FilaRecuento({super.key, required this.evaluacion, required this.nota});

  @override
  Widget build(BuildContext context) {
    final veces = evaluacion.recuento[nota]!;
    final total = evaluacion.total;
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
              // La barra crece desde vacía hasta su proporción, en 1200 ms,
              // como en ValoraPMDM. TweenAnimationBuilder va cambiando
              // valor de begin a end, y con cada valor vuelve a llamar a builder
              child: TweenAnimationBuilder<double>(
                tween: Tween(begin: 0, end: veces / total),
                duration: const Duration(milliseconds: 1200),
                curve: Curves.fastOutSlowIn,
                builder: (context, valor, _) =>
                    LinearProgressIndicator(value: valor, minHeight: 12),
              ),
            ),
          ),
          SizedBox(width: 28, child: Text('$veces')),
        ],
      ),
    );
  }
}
```

</details>

## Paso 10 · La segunda pantalla

Al tocar una fila del recuento (por ejemplo, la del 4), se abre una pantalla con **las valoraciones que tienen esa nota**. Con la flecha de arriba o el botón «atrás» del móvil, se vuelve. Es lo que hace ValoraPMDM en el paso 14 de su guía.

Una app con varias pantallas las organiza en **rutas**: cada pantalla tiene un nombre que empieza por `/`, y el **`Navigator`** las apila. La que está encima es la que se ve.

```
Al abrir:            Al tocar la fila del 4:        Al volver (pop):
┌──────────────┐     ┌──────────────┐               ┌──────────────┐
│ /            │     │ /nota        │  ← se ve      │ /            │
└──────────────┘     ├──────────────┤               └──────────────┘
                     │ /            │
                     └──────────────┘
```

**Los cambios:**

1. En `ValoraApp`, `home` se sustituye por las rutas (y el comentario de la clase pasa a decir «las pantallas (rutas)»):

   ```dart
   // Las pantallas de la app, cada una con su ruta: un nombre que empieza por /.
   // Sin home: la primera pantalla es la de initialRoute
   initialRoute: '/',
   routes: {
     '/': (context) => const PantallaValora(),
     '/nota': (context) => const PantallaNota(),
   },
   ```

2. En `ResumenEvaluacion`, debajo del `Divider`, un texto pequeño para que se sepa que las filas se pueden tocar:

   ```dart
   // Para que se sepa que las filas se pueden tocar
   Padding(
     padding: const EdgeInsets.only(bottom: 12),
     child: Text(
       'Toca una fila para ver las valoraciones con esa nota',
       style: Theme.of(context).textTheme.bodySmall?.copyWith(
         color: Theme.of(context).colorScheme.onSurfaceVariant,
       ),
     ),
   ),
   ```

3. En `FilaRecuento`, el `Padding` que devuelve `build` queda dentro de un `InkWell`, que responde al toque:

   ```dart
   // InkWell: la fila responde al toque (onTap), con el efecto de onda de Material.
   // Con onTap a null, no responde: no hay ninguna valoración que mostrar
   return InkWell(
     onTap: veces == 0
         ? null
         : () {
             // Va a la ruta /nota, y le pasa los datos que necesita en arguments
             Navigator.pushNamed(
               context,
               '/nota',
               arguments: DatosNota(evaluacion, nota),
             );
           },
     child: Padding(
       // … la fila, igual que antes
     ),
   );
   ```

4. Al final del archivo, los datos que se pasan y la pantalla nueva:

   ```dart
   // ── Lo que necesita la pantalla de una nota: la evaluación y la nota ───
   class DatosNota {
     final Evaluacion evaluacion;
     final int nota;

     DatosNota(this.evaluacion, this.nota);
   }

   // ── La pantalla de una nota: las valoraciones que la tienen, en una lista ──
   class PantallaNota extends StatelessWidget {
     const PantallaNota({super.key});

     @override
     Widget build(BuildContext context) {
       // Los datos que se pasaron en arguments, al navegar a esta ruta
       final datos = ModalRoute.of(context)!.settings.arguments as DatosNota;
       // Solo las valoraciones con esa nota: where filtra, toList las pasa a lista
       final valoraciones = datos.evaluacion.valoraciones
           .where((v) => v.nota == datos.nota)
           .toList();

       return Scaffold(
         // La flecha para volver la pone el AppBar sola: hace Navigator.pop(context)
         appBar: AppBar(
           title: Text(
             'Nota ${datos.nota} · ${datos.evaluacion.numero}ª evaluación',
           ),
         ),
         // ListView.builder crea las filas a medida que se ven, a partir de su número
         body: ListView.builder(
           itemCount: valoraciones.length,
           itemBuilder: (context, indice) {
             final valoracion = valoraciones[indice];
             // ListTile: una fila estándar de Material, con icono, título y subtítulo
             return ListTile(
               leading: const Icon(Icons.person_outline),
               title: Text('Valoración ${valoracion.id}'),
               subtitle: Text('${valoracion.nota} de 5'),
             );
           },
         ),
       );
     }
   }
   ```

**Lo que ya se sabe de Dart**

| Pieza | Qué es en Dart |
|---|---|
| `routes: { '/': …, '/nota': … }` | Un mapa: de cada nombre de ruta, a la función que crea su pantalla |
| `(context) => const PantallaValora()` | Una función flecha que crea la pantalla. No se crea ahora: se creará cuando se vaya a esa ruta |
| `class DatosNota` | Una clase de datos sencilla, como `Evaluacion` |
| `… as DatosNota` | Decir de qué tipo es un valor que llega como `Object?` |
| `.where((v) => v.nota == datos.nota).toList()` | Filtrar una lista: `where` deja las que cumplen la condición y `toList` lo pasa a lista (miniguía Kotlin, Dart y Java, apartado 7) |
| `veces == 0 ? null : () { … }` | El ternario: sin función, o con una función sin nombre |
| `…bodySmall?.copyWith(…)` | `?.` porque el estilo podría ser `null`; `copyWith` copia el estilo cambiando solo el color |

**Lo nuevo de Flutter**

| Pieza | Qué es en Flutter |
|---|---|
| `initialRoute` y `routes` | Las pantallas de la app y cuál se ve al empezar. Sustituyen a `home` |
| `Navigator.pushNamed(context, '/nota', arguments: …)` | Pone encima de la pila la pantalla de esa ruta, y le pasa unos datos |
| `ModalRoute.of(context)!.settings.arguments` | En la pantalla nueva, los datos que se pasaron al navegar. Se buscan con el `context`, como el tema |
| `Navigator.pop(context)` | Quita la pantalla de encima. No hace falta escribirlo: lo hacen la flecha que el `AppBar` pone sola en la segunda pantalla y el botón «atrás» del móvil |
| `InkWell(onTap: …)` | Hace que cualquier widget responda al toque, con el efecto de onda de Material. Con `onTap: null`, no responde |
| `ListView.builder(itemCount: …, itemBuilder: …)` | Una lista que **crea cada fila cuando va a verse**, a partir de su posición. Para listas que dependen de los datos o que pueden ser largas |
| `ListTile` | Una fila estándar de Material: icono a la izquierda (`leading`), título y subtítulo. No lleva estrellas: todas las valoraciones de la lista tienen la misma nota, que ya está en el título de la pantalla. Igual que el `ListItem` de ValoraPMDM |

**Por qué se escribe así**

**Por qué se pasan los datos y no se vuelven a descargar.** Las valoraciones ya están dentro de la evaluación que tiene la fila. `FilaRecuento` mete la evaluación y la nota en un objeto `DatosNota`, lo pasa en `arguments`, y `PantallaNota` lo recoge y filtra. Un solo objeto, en lugar de varios datos sueltos, porque `arguments` solo admite un valor.

**Por qué `ListView.builder` aquí y `ListView` en el resumen.** El resumen tiene siempre los mismos elementos, escritos uno a uno. La lista de una nota tiene tantas filas como valoraciones, y pueden ser muchas: `ListView.builder` solo construye las que se ven, y va creando las demás al desplazarse.

**Por qué la pantalla principal sigue igual al volver.** `pushNamed` pone la pantalla nueva **encima**, sin quitar la principal: su estado, con `_carga` ya terminado, sigue ahí. Al volver, `pop` quita la de encima y se ve la principal tal como estaba, en la misma pestaña, sin descargar.

**Comprueba que…** este paso cambia la pantalla de inicio, que solo se lee al arrancar: **reinicia la app** con *hot restart* (la flecha circular verde de la barra de depuración, o `Ctrl + Mayús + F5`). Después:

- Debajo de la línea sale «Toca una fila para ver las valoraciones con esa nota».
- En la 1ª evaluación, al tocar la fila del **4**, se abre «Nota 4 · 1ª evaluación» con **6** valoraciones, cada una con «4 de 5».
- Con la flecha de arriba, o con el botón «atrás» del móvil, se vuelve a la principal, en la misma pestaña y sin círculo de progreso.
- Al tocar la fila del **1** (0 veces), no pasa nada.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Cómo sabe `PantallaNota` de qué evaluación y de qué nota tiene que mostrar las valoraciones?
2. Al volver de la pantalla de una nota, ¿se vuelven a descargar las valoraciones? ¿Por qué?
3. ¿Por qué hay que reiniciar la app en este paso, y no basta con el *hot reload*?

<details>
<summary>Respuestas</summary>

1. Por los `arguments` de `pushNamed`: `FilaRecuento` le pasa un `DatosNota` con la evaluación y la nota, y `PantallaNota` lo recoge con `ModalRoute.of(context)!.settings.arguments`. Las valoraciones ya vienen dentro de la evaluación: no descarga nada.
2. No. `Navigator.pop` solo quita la pantalla de encima: la principal seguía en la pila, con su estado, y `_carga` ya está resuelto. `initState` no se vuelve a ejecutar.
3. El *hot reload* vuelve a ejecutar los `build`, pero conserva lo que ya existe, como el `Navigator` con su pantalla. `initialRoute` solo se usa al crear el `Navigator`, al arrancar. Con el *hot restart*, la app empieza de cero.

</details>

<details>
<summary><code>lib/main.dart</code> completo, tras el paso 10 (el definitivo)</summary>

```dart
// ValoraFlutter: las valoraciones de PMDM de cada evaluación, en una app de Flutter.
// Es ValoraPMDM hecha con Flutter: los mismos datos y la misma pantalla, y una segunda
// pantalla con las valoraciones de una nota. Las clases y la descarga son las de ValoraDart, sin cambios.
import 'package:material_ui/material_ui.dart';

import 'servicio_valora.dart';
import 'valoracion.dart';

void main() {
  runApp(const ValoraApp());
}

// ── La app: sin estado, el tema y las pantallas (rutas) ──────────────
class ValoraApp extends StatelessWidget {
  const ValoraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ValoraFlutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
      ),
      // Las pantallas de la app, cada una con su ruta: un nombre que empieza por /.
      // Sin home: la primera pantalla es la de initialRoute
      initialRoute: '/',
      routes: {
        '/': (context) => const PantallaValora(),
        '/nota': (context) => const PantallaNota(),
      },
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
          return pantalla(
            cuerpo: const Center(child: CircularProgressIndicator()),
          );
        }
        // 2. Ha fallado (sin conexión, error del servidor): el motivo
        if (snapshot.hasError) {
          return pantalla(
            cuerpo: Aviso(
              'No se han podido descargar las valoraciones.\n${snapshot.error}',
            ),
          );
        }
        // 3. Ha terminado bien: las evaluaciones con archivo
        final evaluaciones = snapshot.data!;
        if (evaluaciones.isEmpty) {
          return pantalla(
            cuerpo: const Aviso('No hay valoraciones publicadas.'),
          );
        }
        // Una pestaña por evaluación. DefaultTabController une las pestañas con su contenido
        return DefaultTabController(
          length: evaluaciones.length,
          child: pantalla(
            pestanas: TabBar(
              tabs: [
                for (final e in evaluaciones)
                  Tab(text: '${e.numero}ª evaluación'),
              ],
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
        Center(
          child: Text(total == 1 ? '1 valoración' : '$total valoraciones'),
        ),
        const Divider(height: 32),
        // Para que se sepa que las filas se pueden tocar
        Padding(
          padding: const EdgeInsets.only(bottom: 12),
          child: Text(
            'Toca una fila para ver las valoraciones con esa nota',
            style: Theme.of(context).textTheme.bodySmall?.copyWith(
              color: Theme.of(context).colorScheme.onSurfaceVariant,
            ),
          ),
        ),
        // Una fila por nota, de la 5 a la 1: el for dentro de la lista crea un widget por cada una
        for (final nota in evaluacion.recuento.keys)
          FilaRecuento(evaluacion: evaluacion, nota: nota),
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
          Icon(
            Icons.star,
            color: i <= llenas ? colores.primary : colores.outlineVariant,
          ),
      ],
    );
  }
}

// ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
// Al tocarla, si alguien ha puesto esa nota, se abre la pantalla con sus valoraciones
class FilaRecuento extends StatelessWidget {
  final Evaluacion evaluacion;
  final int nota;

  const FilaRecuento({super.key, required this.evaluacion, required this.nota});

  @override
  Widget build(BuildContext context) {
    final veces = evaluacion.recuento[nota]!;
    final total = evaluacion.total;
    // InkWell: la fila responde al toque (onTap), con el efecto de onda de Material.
    // Con onTap a null, no responde: no hay ninguna valoración que mostrar
    return InkWell(
      onTap: veces == 0
          ? null
          : () {
              // Va a la ruta /nota, y le pasa los datos que necesita en arguments
              Navigator.pushNamed(
                context,
                '/nota',
                arguments: DatosNota(evaluacion, nota),
              );
            },
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 6),
        child: Row(
          children: [
            SizedBox(width: 20, child: Text('$nota')),
            Icon(Icons.star, color: Theme.of(context).colorScheme.primary),
            // Expanded: la barra ocupa todo el ancho que dejan el número y la estrella
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 8),
                // La barra crece desde vacía hasta su proporción, en 1200 ms,
                // como en ValoraPMDM. TweenAnimationBuilder va cambiando
                // valor de begin a end, y con cada valor vuelve a llamar a builder
                child: TweenAnimationBuilder<double>(
                  tween: Tween(begin: 0, end: veces / total),
                  duration: const Duration(milliseconds: 1200),
                  curve: Curves.fastOutSlowIn,
                  builder: (context, valor, _) =>
                      LinearProgressIndicator(value: valor, minHeight: 12),
                ),
              ),
            ),
            SizedBox(width: 28, child: Text('$veces')),
          ],
        ),
      ),
    );
  }
}

// ── Lo que necesita la pantalla de una nota: la evaluación y la nota ───
class DatosNota {
  final Evaluacion evaluacion;
  final int nota;

  DatosNota(this.evaluacion, this.nota);
}

// ── La pantalla de una nota: las valoraciones que la tienen, en una lista ──
class PantallaNota extends StatelessWidget {
  const PantallaNota({super.key});

  @override
  Widget build(BuildContext context) {
    // Los datos que se pasaron en arguments, al navegar a esta ruta
    final datos = ModalRoute.of(context)!.settings.arguments as DatosNota;
    // Solo las valoraciones con esa nota: where filtra, toList las pasa a lista
    final valoraciones = datos.evaluacion.valoraciones
        .where((v) => v.nota == datos.nota)
        .toList();

    return Scaffold(
      // La flecha para volver la pone el AppBar sola: hace Navigator.pop(context)
      appBar: AppBar(
        title: Text(
          'Nota ${datos.nota} · ${datos.evaluacion.numero}ª evaluación',
        ),
      ),
      // ListView.builder crea las filas a medida que se ven, a partir de su número
      body: ListView.builder(
        itemCount: valoraciones.length,
        itemBuilder: (context, indice) {
          final valoracion = valoraciones[indice];
          // ListTile: una fila estándar de Material, con icono, título y subtítulo
          return ListTile(
            leading: const Icon(Icons.person_outline),
            title: Text('Valoración ${valoracion.id}'),
            subtitle: Text('${valoracion.nota} de 5'),
          );
        },
      ),
    );
  }
}
```

</details>

## Paso 11 · Generar el APK

En la terminal:

```powershell
flutter build apk
```

Genera la versión final de la app (*release*), compilada con AOT. Tarda unos minutos y deja el archivo en `build\app\outputs\flutter-apk\app-release.apk`. Se instala como el de ValoraPMDM: en el emulador, arrastrándolo a su ventana; en un móvil, copiándolo y abriéndolo.

**Comprueba que…** la app instalada desde el APK abre sin la etiqueta **DEBUG** y descarga las valoraciones.

## Paso 12 · La misma app en el navegador

En «Hola Flutter» (paso 9), el contador se ejecutó también en el navegador sin cambiar el código. Aquí se hace lo mismo con una app de verdad, que descarga datos de Internet y tiene dos pantallas, para comprobar que **el código de `lib` sirve igual para Android y para la web**.

1. Para la app del emulador: en la barra de depuración de VS Code, el cuadrado rojo (**Stop**).
2. En la terminal de VS Code, en la carpeta del proyecto:

   ```powershell
   flutter create --platforms web .
   flutter run -d chrome
   ```

   Como en «Hola Flutter»: la primera orden añade la plataforma web al proyecto (la carpeta `web`) sin tocar `lib` ni `android`, y la segunda compila la app para la web y la abre en Chrome (con Edge, `-d edge`).
3. Para terminar, pulsa `q` en la terminal.

**Comprueba que…** en Chrome se ven las pestañas de la 1ª y la 2ª evaluación con las mismas medias y el mismo recuento que en el emulador (3.8 y 4.4), las barras crecen, ↻ vuelve a descargar, al tocar la fila del **4** se abre «Nota 4 · 1ª evaluación» con 6 valoraciones, y la flecha de arriba vuelve a la principal.

**La misma app, en Android y en la web.** El código de `lib` es el mismo; lo que cambia es todo lo de alrededor:

| | En Android | En la web |
|---|---|---|
| Qué hace falta para ejecutarla | El SDK de Android (el que instala Android Studio) y un emulador o un móvil | Un navegador |
| En qué se convierte el código de Dart | En código nativo del procesador del móvil (AOT) | En JavaScript (o WebAssembly), que es lo que entiende el navegador |
| La versión final | `flutter build apk`: un archivo, `app-release.apk` (paso 11) | `flutter build web`: una carpeta, `build\web`, con `index.html` y los archivos de la app («Hola Flutter», paso 9) |
| Cómo llega a quien la usa | Se instala el APK en el móvil, o se publica en una tienda como Google Play | Se sube la carpeta `build\web` a un servidor web y se abre con su dirección. No se instala nada |
| Permiso para usar Internet | Hay que pedirlo en `AndroidManifest.xml` (paso 2) | No existe: ese archivo es solo de Android. En cambio, quien decide si la página puede leer los datos es el **servidor** de los datos (CORS, debajo) |
| Lo que hay que cambiar en `lib` | Nada | Nada |

**CORS.** Por seguridad, cuando una página web pide datos a **otro sitio** (aquí, la app en `localhost` pide los JSON a `raw.githubusercontent.com`), el navegador solo se los entrega si ese servidor lo permite, con una cabecera de su respuesta: `Access-Control-Allow-Origin`. GitHub responde `Access-Control-Allow-Origin: *`, es decir, «cualquier página puede leerlos», y por eso la app funciona en el navegador. Una app de Android no pasa por esa comprobación: es una regla de los navegadores.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué en la web no hace falta el permiso de Internet del paso 2?
2. Si los JSON estuvieran en un servidor que no enviase `Access-Control-Allow-Origin`, ¿qué pasaría en el emulador y qué en Chrome?
3. ¿Qué se le da a alguien para que use la app en su móvil Android, y qué para que la use en el navegador?

<details>
<summary>Respuestas</summary>

1. Porque `AndroidManifest.xml` es solo de Android: en la web no se usa. En el navegador, una página siempre puede hacer peticiones; lo que se comprueba es si el servidor de los datos las permite (CORS).
2. En el emulador, nada cambiaría: las apps de Android no comprueban CORS. En Chrome, el navegador no entregaría los datos a la app, la descarga fallaría y saldría el aviso «No se han podido descargar las valoraciones.».
3. Para Android, el APK (o la app publicada en Google Play), que se instala. Para la web, solo una dirección: la del servidor donde está la carpeta `build\web`.

</details>

---

## Si algo falla

| Qué pasa | Qué hacer |
|---|---|
| `Target of URI doesn't exist: 'package:http/http.dart'` | Falta `flutter pub add http` (paso 2) |
| `Target of URI doesn't exist: 'servicio_valora.dart'` | Faltan los dos archivos de ValoraDart en `lib` (paso 3) |
| Error en `test/widget_test.dart` con `MyApp` | Es la prueba del contador de `flutter create`, que esta app no usa: bórrala (paso 4) |
| La app se queda siempre en el círculo | La descarga no termina: comprueba la conexión del emulador (abre el navegador del emulador) |
| Sale siempre «No se han podido descargar…», con conexión | La URL base de `servicio_valora.dart` está mal: ábrela en el navegador con `valora1.json` al final |
| El APK no descarga nada, y en modo *debug* sí | Falta el permiso de Internet en el manifiesto (paso 2) |
| En Chrome sale «No se han podido descargar…», y en el emulador funciona | El servidor de los datos no permite que una página web los lea (CORS, paso 12). Con los archivos de GitHub no pasa: comprueba la URL base de `servicio_valora.dart` |
| `The getter '…' isn't defined` o algo en rojo | Un nombre mal escrito: compara con el código de la guía. Dart distingue mayúsculas de minúsculas |
| Al tocar una fila, `Could not find a generator for route RouteSettings("/nota", …)` | Falta la ruta `/nota` en el mapa `routes` de `MaterialApp`, o está escrita de otra forma |
| `Null check operator used on a null value` en `PantallaNota` | Se ha llegado a `/nota` sin `arguments`: el `pushNamed` de `FilaRecuento` tiene que llevar `arguments: DatosNota(…)` |
| El *hot reload* no muestra un cambio en `initState` | `initState` solo se ejecuta al crear el estado: para eso hace falta el *hot restart* (`Ctrl + Mayús + F5`) |
| Errores de Gradle, del JDK o de rutas | Mira «Si algo falla» de «Hola Flutter» |

## Los archivos del proyecto

```
C:\dam2627\pmdm\valora_flutter
├── android
│   └── app\src\main
│       └── AndroidManifest.xml   el permiso de Internet (paso 2)
├── web                           la página que carga la app en el navegador (paso 12)
├── lib
│   ├── main.dart                 la interfaz: la app, sus dos pantallas y sus widgets (pasos 4 a 10)
│   ├── servicio_valora.dart      la descarga, de ValoraDart (paso 3)
│   └── valoracion.dart           las clases, de ValoraDart (paso 3)
├── pubspec.yaml                  las dependencias: material_ui y http (paso 2)
└── build                         lo compilado: el APK (paso 11) y, con `flutter build web`, la versión web
```
