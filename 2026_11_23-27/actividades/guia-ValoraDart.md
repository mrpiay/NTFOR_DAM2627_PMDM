# Programa Dart - ValoraDart

## Introducción

En esta guía aprenderás a diseñar y desarrollar **ValoraDart**, un programa en **Dart**, para la consola, que descarga las valoraciones anónimas del módulo de PMDM de cada evaluación y muestra su media, sus estrellas y cuántas veces se ha dado cada nota.

Son las mismas valoraciones que muestra la app **ValoraPMDM**, en los mismos archivos JSON de GitHub. La diferencia es el lenguaje: aquí todo se escribe en **Dart**, el lenguaje de **Flutter**. Antes de hacer una app con Flutter conviene conocer el lenguaje, y este programa recorre lo esencial: variables y tipos, *null safety*, listas y mapas, funciones, clases, y la programación asíncrona con `Future`, `async` y `await` para descargar de Internet.

El objetivo de esta guía no es únicamente conseguir que el programa funcione, sino también comprender cómo se escribe un programa en Dart y en qué se parece y en qué se diferencia de Kotlin y de Java. Para ello, se usa **Visual Studio Code** (VS Code), con la extensión de Flutter, y el **SDK de Flutter**, que trae Dart, tal como se instaló en la guía «Hola Flutter».

El desarrollo se divide en dos partes:

- **Parte 1. Diseño y funcionamiento:** qué hace el programa, de dónde salen los datos, el lenguaje Dart y cómo se organiza el código, antes de empezar a programar.
- **Parte 2. Implementación paso a paso:** se crea el proyecto, se añade la librería `http`, se escribe y se comprueba cada archivo y se prueba el programa.

La teoría de Dart sale del libro de McGraw-Hill, *Programación multimedia y dispositivos móviles*, **unidad 4, apartado 2 «El lenguaje de programación Dart»**, que conviene tener a mano. Sus ejemplos descargables (`listas.dart`, `maps.dart`, `funciones_asincronas.dart`…) se pueden ejecutar con `dart nombre.dart`.

Al finalizar, tendrás un programa funcional y la base de Dart que se usa en todas las apps de Flutter.

## Índice

- [Parte 1 · Diseño](#parte-1--diseño)
  - [1. Qué hace el programa](#1-qué-hace-el-programa)
  - [2. De dónde salen los datos](#2-de-dónde-salen-los-datos)
  - [3. El lenguaje Dart](#3-el-lenguaje-dart)
  - [4. Cómo se organiza el código](#4-cómo-se-organiza-el-código)
  - [5. Cómo se mueve la información](#5-cómo-se-mueve-la-información)
  - [6. Decisiones de diseño](#6-decisiones-de-diseño)
  - [7. Librerías y dependencias](#7-librerías-y-dependencias)
- [Parte 2 · Implementación](#parte-2--implementación)
  - [Paso 0 · Comprobar Dart](#paso-0--comprobar-dart)
  - [Paso 1 · Crear el proyecto](#paso-1--crear-el-proyecto)
  - [Paso 2 · La librería http](#paso-2--la-librería-http)
  - [Paso 3 · Las clases](#paso-3--las-clases)
  - [Paso 4 · La descarga](#paso-4--la-descarga)
  - [Paso 5 · El programa principal](#paso-5--el-programa-principal)
  - [Paso 6 · Probar el programa](#paso-6--probar-el-programa)
- [Si algo falla](#si-algo-falla)
- [Los archivos del proyecto](#los-archivos-del-proyecto)

---

# Parte 1 · Diseño

## 1. Qué hace el programa

Así se ve al ejecutarlo, en el terminal:

```
=== ValoraDart ===

Evaluación 1: 13 valoraciones
  Media: 3.8  ****-
  5 | ###           3
  4 | ######        6
  3 | ###           3
  2 | #             1
  1 |               0

Evaluación 2: 13 valoraciones
  Media: 4.4  ****-
  5 | ######        6
  4 | ######        6
  3 | #             1
  2 |               0
  1 |               0

Evaluación 3: sin archivo (404)
```

| Nº | Requisito |
|---|---|
| R1 | Descarga de Internet las valoraciones de la 1ª, la 2ª y la 3ª evaluación, una detrás de otra. |
| R2 | De cada evaluación con valoraciones, muestra el **número de valoraciones**, la **media** con un decimal y sus **estrellas**: 5 caracteres, un `*` por estrella y un `-` por cada una que falta. Las estrellas son la media redondeada: 3.8 da `****-`. |
| R3 | Debajo, de la nota 5 a la 1, **cuántas veces se ha dado** cada una, con una barra de `#` tan larga como las veces. |
| R4 | Si el archivo de una evaluación no existe (404), lo dice y pasa a la siguiente. |
| R5 | Si la descarga falla (sin conexión, error del servidor), avisa del motivo y sigue con la siguiente evaluación: el programa no se cierra. |
| R6 | Las valoraciones son **anónimas**: solo un número para distinguirlas y la nota. |

## 2. De dónde salen los datos

Las valoraciones están en archivos **JSON** publicados en GitHub, uno por evaluación: `valora1.json`, `valora2.json` y `valora3.json`. Son los mismos que usa ValoraPMDM. Cada archivo es una **lista** de valoraciones, y cada valoración, un **objeto** con dos datos:

```json
[
  { "id": 1, "nota": 4 },
  { "id": 2, "nota": 5 },
  { "id": 3, "nota": 3 }
]
```

La dirección de cada archivo es una **URL base**, la carpeta, más el nombre del archivo:

```
https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/  ← URL base
                                                                     valora1.json  ← archivo
```

En `valora/ejemplo/` hay valoraciones de la 1ª y la 2ª evaluación, 13 en cada una, con medias distintas (3.8 y 4.4). No hay archivo de la 3ª, a propósito: así se prueba qué pasa cuando no existe (R4). Puedes abrir [valora1.json](https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/valora1.json) en el navegador para ver cómo es.

Al pedir un archivo, el servidor responde con un **código de estado**: **200** si todo va bien (y el contenido del archivo), **404** si no existe, y otros números (403, 500…) si hay un error.

## 3. El lenguaje Dart

Dart es un lenguaje creado por Google en 2011, que **Flutter** usa para todo. Según el libro (MGH, U4, apartado 2), permite una programación estructurada flexible y **familiar para quien ya conoce Java, Kotlin, C# o JavaScript**, es **orientado a objetos** y se puede **compilar de dos formas**: **JIT** (*just-in-time*, mientras se ejecuta en la máquina virtual de Dart, lo que permite el *hot reload* de Flutter) y **AOT** (*ahead-of-time*, antes de ejecutar, a código nativo, lo que da un rendimiento casi nativo).

Esta sección resume lo que usa ValoraDart, apartado por apartado del libro. En la columna de la derecha está dónde aparece en el programa.

### 3.1 · Trabajar con Dart (MGH 2.1 y 2.2)

| Concepto | Qué es | En ValoraDart |
|---|---|---|
| `dart archivo.dart` | Ejecuta un archivo de Dart. El SDK de Flutter trae la máquina virtual de Dart | `dart run`, que ejecuta el proyecto entero |
| **DartPad** ([dartpad.dev](https://dartpad.dev)) | Un editor en el navegador que ejecuta Dart (y Flutter) sin instalar nada | Para probar trozos sueltos |
| `main` | La función por la que empieza el programa, como en Java | En `bin/valora_dart.dart` |
| `print(…)` | Escribe una línea en la consola | Todo lo que se ve |
| **Interpolación**: `$variable` y `${expresión}` | Mete el valor de una variable dentro de un texto. Con llaves, si es algo más que un nombre: `${evaluacion.total}` | `'Evaluación $numero: ${evaluacion.total} valoraciones'` |
| `;` al final, `//` y `/* */` | Como en Java: cada sentencia acaba en punto y coma; los comentarios, igual | Todo el código |

### 3.2 · Variables y tipos (MGH 2.3)

| Concepto | Qué es | En ValoraDart |
|---|---|---|
| Tipos | `int`, `double`, `String`, `bool`, y las colecciones `List`, `Set` y `Map` | `int numero`, `double? media`, `List<Valoracion>`, `Map<int, int>` |
| `var` | Declara una variable y **deduce** su tipo del valor. Una vez asignado, el tipo ya no cambia | `for (var numero = 1; …)` |
| `final` | Una variable que **no se puede reasignar**. El objeto que guarda sí puede cambiar por dentro (a una lista `final` se le pueden añadir elementos) | `final respuesta = await http.get(url);` |
| `const` | Un valor **fijo desde que se compila**: ni se reasigna ni cambia por dentro | `const urlBase = '…';` |

**Null safety** (MGH 2.3.A). Desde Dart 2.12, una variable **no puede valer `null`**, salvo que se diga con `?`. Así el compilador avisa antes de ejecutar de los errores por valores nulos, que en Java saltan como `NullPointerException`:

| Operador | Qué hace | En ValoraDart |
|---|---|---|
| `Tipo?` | Declara que puede valer `null` | `double? get media`: sin valoraciones, no hay media. `Future<List<Valoracion>?>`: si es 404, no hay lista |
| `!` | «Sé que aquí no es `null`»: convierte un `Tipo?` en `Tipo`. Si al final fuera `null`, el programa fallaría | `veces[valoracion.nota]! + 1`: leer de un mapa da `int?`, pero la nota siempre está |
| `??` | El valor de la izquierda, o el de la derecha si el de la izquierda es `null` | (no se usa) `nombre ?? 'Anónimo'` |
| `??=` | Asigna solo si la variable vale `null` | (no se usa) |
| `?.` | Accede a una propiedad solo si el objeto no es `null`; si lo es, da `null` | (no se usa) `texto?.length` |

**Colecciones** (MGH 2.3.B a D):

| Colección | Qué es | En ValoraDart |
|---|---|---|
| `List` | Una lista **ordenada**, con posiciones desde 0: `[1, 2, 3]`. En Dart no hay *arrays*: se usan listas | `List<Valoracion> valoraciones` |
| `Set` | Un conjunto **sin orden ni repetidos**: `{1, 2}` | (no se usa) |
| `Map` | Pares **clave: valor**, como un JSON: `{'PMDM': 8, 'DI': 7}` | `recuento`: `{5: 3, 4: 6, …}`, nota → veces; y cada valoración del JSON, `{'id': 1, 'nota': 4}` |
| `.map(función)` | Transforma cada elemento de una colección con una función y da otra colección | `valoraciones.map((v) => v.nota)`: las notas; `lista.map(… Valoracion.fromJson …)`: los objetos |

### 3.3 · Funciones (MGH 2.4 y 2.5)

| Concepto | Qué es | En ValoraDart |
|---|---|---|
| Función | `TipoRetorno nombre(argumentos) { … }`. Puede estar fuera de cualquier clase | `String estrellas(double media)`, `void mostrar(Evaluacion evaluacion)` |
| **Función anónima** | Una función sin nombre, que se pasa como argumento a otra: `(a, b) { … }` | `forEach((nota, veces) { … })` |
| **Función flecha** `=>` | Abrevia una función de una sola expresión: sin llaves ni `return` | `int get total => valoraciones.length;`, `(v) => v.nota` |
| Argumentos **opcionales** `[ ]` | Se pueden omitir; si no se pasan, valen `null` o su valor por defecto | (no se usa) |
| Argumentos **con nombre** `{ }` | Se pasan con su nombre: `Valoracion(id: 1, nota: 4)`. Con `required`, son obligatorios. Son los que más se usan en Flutter | `Valoracion({required this.id, required this.nota})` |
| Estructuras de control | `if`/`else`, `switch`, `for`, `forEach`, `while` y el ternario `condición ? a : b`, como en Java | `for` de 1 a 3, `if (valoraciones == null)`, `media == null ? '-' : …` |

### 3.4 · Clases (MGH 2.6)

| Concepto | Qué es | En ValoraDart |
|---|---|---|
| Clase y objeto | Como en Java: `class Nombre { propiedades; constructor; métodos }`. Para crear un objeto, el constructor, sin `new` | `Evaluacion(numero, valoraciones)` |
| **Constructor simplificado** | `Nombre(this.a, this.b);`: los argumentos van directos a las propiedades | `Evaluacion(this.numero, this.valoraciones);` |
| Constructor con **argumentos con nombre** | `Nombre({required this.a, required this.b});` | `Valoracion({required this.id, required this.nota});` |
| **Constructor con nombre** | Dart no tiene sobrecarga de constructores: en su lugar, constructores con nombre, `Nombre.otro(…)` | `Valoracion.fromJson(…)` |
| **Inicialización con diccionario** (`fromJson`) | Un constructor que crea el objeto a partir de un mapa (un JSON leído). Las propiedades se inicializan **después de los dos puntos**, separadas por comas | `: id = json['id'] as int, nota = json['nota'] as int` |
| `toString` | Lo que se escribe al hacer `print(objeto)`. Se sobrescribe con `@override` | `'Valoración $id: $nota'` |
| **Getter** `get` | Un método que se usa como una propiedad, sin paréntesis: sirve para **propiedades calculadas** | `total`, `media` y `recuento` |
| Privado: `_nombre` | En Dart no hay `private`: lo que empieza por `_` es privado de su archivo | (no se usa) |
| Herencia, abstractas, interfaces, *mixins* | `extends`, `abstract class`, `implements` y `with`: se verán con los widgets de Flutter, que se crean heredando de `StatelessWidget` o `StatefulWidget` | (no se usa) |

### 3.5 · Programación asíncrona (MGH 2.7)

Pedir un archivo a un servidor tarda, y no se sabe cuánto. Si el programa se quedara parado esperando, en una app la pantalla se congelaría. Dart lo resuelve con **`Future`**, como Kotlin con las corrutinas:

| Concepto | Qué es | En ValoraDart |
|---|---|---|
| `Future<Tipo>` | Un valor que **llegará más adelante**. `Future<String>` es «un `String`, cuando esté» | `Future<List<Valoracion>?> descargar(…)` |
| `async` | Marca una función que usa `await`. Una función `async` siempre devuelve un `Future` | `descargar` y `main` |
| `await` | **Espera** a que el `Future` se resuelva y da su valor. Solo se puede usar dentro de una función `async` | `await http.get(url)` y `await descargar(numero)` |
| `.then(…)` | La otra forma de usar el valor de un `Future`, sin esperar: una función que se ejecuta cuando llega | (no se usa: con `await` se lee mejor) |

Sin `await`, `print(descargar(1))` no escribiría la lista, sino `Instance of 'Future<List<Valoracion>?>'`: el `Future`, todavía sin resolver. Es lo que muestra el ejemplo `funciones_asincronas.dart` del libro.

## 4. Cómo se organiza el código

El programa se reparte en **tres archivos**, cada uno con un trabajo, como las capas de ValoraPMDM:

```
┌──────────────────────────────────────────────────────────────┐
│ bin/valora_dart.dart                             paso 5      │
│   main      descarga las tres evaluaciones, una a una        │
│   mostrar   escribe el resumen de una evaluación             │
└─────────────┬──────────────────────────────────▲─────────────┘
              │ descargar(numero)                │ List<Valoracion>?
┌─────────────▼──────────────────────────────────┴─────────────┐
│ lib/servicio_valora.dart                         paso 4      │
│   descargar  pide el JSON con http y lo convierte            │
└─────────────┬────────────────────────────────────────────────┘
              │ Valoracion.fromJson(mapa)
┌─────────────▼────────────────────────────────────────────────┐
│ lib/valoracion.dart                              paso 3      │
│   Valoracion   una valoración: id y nota                     │
│   Evaluacion   total, media y recuento                       │
│   estrellas    la media en estrellas: "****-"                │
└──────────────────────────────────────────────────────────────┘
```

| Paso | Archivo | Qué contiene |
|---|---|---|
| 3 | `lib/valoracion.dart` | Los **datos y los cálculos**: las clases `Valoracion` y `Evaluacion` y la función `estrellas`. No descarga ni escribe nada |
| 4 | `lib/servicio_valora.dart` | La **descarga**: la URL base y la función `descargar`, con `http` |
| 5 | `bin/valora_dart.dart` | El **programa**: `main`, que descarga y escribe, y `mostrar` |

En un proyecto de Dart, `bin` guarda lo que se ejecuta y `lib`, la **biblioteca**: el código que se usa desde otros archivos con `import 'package:valora_dart/…';`.

## 5. Cómo se mueve la información

1. `main` recorre las evaluaciones 1, 2 y 3 con un `for`.
2. Para cada una, llama a `descargar(numero)` y **espera** (`await`) su resultado.
3. `descargar` arma la URL (`urlBase` + `valora1.json`), pide el archivo con `http.get` y espera la respuesta.
4. Si el código es **404**, devuelve `null`. Si no es **200**, lanza una excepción con el código.
5. Si es 200, `jsonDecode` convierte el texto en una **lista de mapas**, y `map` convierte cada mapa en una `Valoracion` con el constructor `fromJson`. Devuelve la lista.
6. De vuelta en `main`: si es `null`, escribe «sin archivo (404)». Si no, crea una `Evaluacion` con la lista y llama a `mostrar`.
7. `mostrar` pide a la evaluación su `total`, su `media` y su `recuento` (los getters, que calculan en ese momento) y los escribe.
8. Si algo lanza una excepción (sin conexión, error del servidor), el `catch` de `main` la recoge, escribe el motivo y el `for` sigue con la siguiente evaluación (R5).

## 6. Decisiones de diseño

| Decisión | Por qué |
|---|---|
| Los **mismos JSON** que ValoraPMDM | Se comparan las dos versiones con los mismos datos y deben salir las mismas medias: 3.8 y 4.4. |
| **Tres archivos**: datos, descarga y programa | Cada uno se entiende y se cambia por separado, como las capas de ValoraPMDM. Las clases no saben de dónde vienen los datos ni cómo se muestran. |
| Un constructor **`fromJson`** | Es la forma habitual en Dart de convertir un JSON en objetos (MGH 2.6.F), y la que usan las apps de Flutter. |
| La media y el recuento, como **getters** | Se calculan cuando se piden, con los datos de ese momento: no pueden quedar desactualizados. |
| `media` es **`double?`** | Sin valoraciones no hay media: con *null safety*, el tipo lo dice, y quien la usa tiene que tenerlo en cuenta (`media == null ? '-' : …`). |
| **`await`** en lugar de `.then` | El código se lee de arriba abajo, en orden, como si no fuera asíncrono. |
| Un **404** no es un error | Que la 3ª evaluación aún no tenga archivo es normal: se dice y se sigue. |
| Estrellas con **`*` y `-`** | Se ven igual en cualquier terminal, como en ValoraPROG. |

## 7. Librerías y dependencias

Lo que no trae Dart se busca en **[pub.dev](https://pub.dev)**, el catálogo de paquetes de Dart y Flutter, y se apunta en el archivo **`pubspec.yaml`** del proyecto, en `dependencies`. `dart pub add nombre` lo apunta y lo descarga en un solo paso.

| Lo que se quiere | Con qué | ¿Lo trae Dart? |
|---|---|---|
| Pedir un archivo a un servidor (R1) | El paquete **`http`**: `http.get(url)` | No: `dart pub add http` |
| Convertir el texto JSON en objetos de Dart | `jsonDecode`, de `dart:convert` | **Sí**: `import 'dart:convert';` |
| Esperar a la descarga sin bloquear | `Future`, `async` y `await` | **Sí**: son parte del lenguaje |

---

# Parte 2 · Implementación

Cada paso dice **qué se hace**, trae el **código completo** del archivo y termina con **Comprueba que…**: no pases al siguiente hasta que se cumpla.

## Paso 0 · Comprobar Dart

ValoraDart usa el **SDK de Flutter**, que trae Dart, instalado en `C:\dam2627\flutter` en la guía «Hola Flutter», y **VS Code** con la extensión **Flutter** (que instala también la de **Dart**).

En una terminal (PowerShell):

```powershell
dart --version
```

**Comprueba que…** sale `Dart SDK version: 3.14.0` (o una más nueva). Si sale que `dart` no se reconoce, falta añadir `C:\dam2627\flutter\bin` al `Path` (guía «Hola Flutter», paso 2).

## Paso 1 · Crear el proyecto

En la terminal, una línea cada vez:

```powershell
New-Item -ItemType Directory -Force C:\dam2627\pmdm
cd C:\dam2627\pmdm
dart create -t console valora_dart
code valora_dart
```

| Comando | Qué hace |
|---|---|
| `New-Item -ItemType Directory -Force C:\dam2627\pmdm` | Crea la carpeta de los proyectos, si no existe (con `-Force`, si ya existe, no da error ni borra nada) |
| `dart create -t console valora_dart` | Crea un proyecto de Dart para la consola (`-t console`, la plantilla), en la carpeta `valora_dart`. Los nombres de proyecto de Dart van en minúsculas y con `_` |
| `code valora_dart` | Abre la carpeta del proyecto en VS Code |

La primera vez que se usa `dart`, antes de crear el proyecto sale un aviso largo sobre el envío de estadísticas de uso (*telemetry*, Google Analytics): es informativo, no hay que hacer nada. Al final, `dart create` escribe `Created project valora_dart…` y sugiere `cd valora_dart` y `dart run`: no hace falta, el proyecto se abre con `code`.

Este proyecto no tiene carpeta `android`, así que no necesita el `.vscode/settings.json` de «Hola Flutter».

La plantilla trae un ejemplo que no se usa: en el Explorador de VS Code, **borra** `lib/valora_dart.dart` y `test/valora_dart_test.dart` (clic derecho → *Delete*).

**Comprueba que…** en VS Code está la carpeta `VALORA_DART`, con `bin/valora_dart.dart`, `lib` (vacía) y `pubspec.yaml`.

## Paso 2 · La librería http

En la terminal de VS Code (`Ctrl + ñ`):

```powershell
dart pub add http
dart pub remove path
```

`dart pub add http` apunta el paquete en `pubspec.yaml` y lo descarga. `dart pub remove path` quita un paquete que la plantilla añade y que este programa no usa. El archivo queda así:

```yaml
name: valora_dart
description: A sample command-line application.
version: 1.0.0
# repository: https://github.com/my_org/my_repo

environment:
  sdk: ^3.14.0

# Add regular dependencies here.
dependencies:
  http: ^1.6.0
dev_dependencies:
  lints: ^6.0.0
  test: ^1.25.6
```

| Parte | Qué es |
|---|---|
| `name` | El nombre del paquete. Es el que va en los `import 'package:valora_dart/…'` |
| `environment: sdk:` | Las versiones de Dart con las que funciona. `^3.14.0` quiere decir «3.14.0 o más nueva, pero 3.x» |
| `dependencies` | Las librerías que usa el programa: `http` |
| `dev_dependencies` | Las que solo se usan al programar: `lints` (avisos de estilo) y `test` (pruebas) |

**Comprueba que…** en `pubspec.yaml` está `http` en `dependencies`, y en la terminal no salen errores.

## Paso 3 · Las clases

`lib/valoracion.dart` tiene los **datos y los cálculos**: qué es una valoración, las valoraciones de una evaluación con su media y su recuento, y las estrellas. No descarga ni escribe nada.

En el Explorador, clic derecho sobre `lib` → **New File…**, `valoracion.dart`:

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `final int id;` | Una propiedad que no cambia después de crear el objeto |
| `Valoracion({required this.id, required this.nota});` | Constructor con argumentos con nombre y obligatorios: se crea con `Valoracion(id: 1, nota: 4)` |
| `Valoracion.fromJson(Map<String, dynamic> json)` | Un constructor con nombre que recibe un mapa: el objeto del JSON ya leído. `dynamic` quiere decir «de cualquier tipo»: en un JSON, los valores pueden ser números, textos… |
| `: id = json['id'] as int, nota = json['nota'] as int` | La inicialización, después de los dos puntos. `json['id']` lee del mapa el valor de la clave `id`, y `as int` dice que es un entero |
| `@override String toString() => …` | Sobrescribe lo que se escribe con `print(valoracion)`. Con `=>`, función flecha |
| `Evaluacion(this.numero, this.valoraciones);` | Constructor simplificado |
| `int get total => valoraciones.length;` | Un getter: se usa como `evaluacion.total`, sin paréntesis |
| `double? get media` | Devuelve `double?`: `null` si no hay valoraciones |
| `valoraciones.map((v) => v.nota).reduce((a, b) => a + b)` | `map` saca la nota de cada valoración y `reduce` las va sumando de dos en dos: la suma de todas |
| `suma / total` | En Dart, `/` siempre da un `double`, aunque los dos sean `int` (para la división entera está `~/`) |
| `final veces = {5: 0, 4: 0, 3: 0, 2: 0, 1: 0};` | Un mapa de nota a veces, empezando todas a 0. El orden de las claves es el de escritura: de la 5 a la 1 |
| `veces[valoracion.nota] = veces[valoracion.nota]! + 1;` | Suma 1 a esa nota. Leer de un mapa da `int?` (la clave podría no estar), y `!` dice que aquí está seguro |
| `media.round()` | Redondea al entero más cercano: 3.8 → 4 |
| `'*' * llenas + '-' * (5 - llenas)` | En Dart, un texto por un número lo repite: `'*' * 4` es `'****'` |

```dart
// Las clases de ValoraDart: una valoración y las valoraciones de una evaluación.
// Aquí no se descarga ni se escribe nada: solo datos y cálculos.

/// Una valoración: un número para distinguirla y una nota del 1 al 5.
/// Es lo que hay en cada elemento de los archivos JSON: { "id": 1, "nota": 4 }
class Valoracion {
  final int id;
  final int nota;

  // Constructor con argumentos con nombre y obligatorios (required)
  Valoracion({required this.id, required this.nota});

  // Constructor con nombre: crea la valoración a partir de un mapa, el JSON ya leído.
  // Las propiedades se inicializan después de los dos puntos, separadas por comas
  Valoracion.fromJson(Map<String, dynamic> json)
      : id = json['id'] as int,
        nota = json['nota'] as int;

  // Lo que se escribe al hacer print(valoracion)
  @override
  String toString() => 'Valoración $id: $nota';
}

/// Las valoraciones de una evaluación y lo que se calcula con ellas
class Evaluacion {
  final int numero;
  final List<Valoracion> valoraciones;

  // Constructor simplificado: los argumentos van directamente a las propiedades
  Evaluacion(this.numero, this.valoraciones);

  // Getter: se usa como una propiedad (evaluacion.total), pero se calcula
  int get total => valoraciones.length;

  // La media, o null si no hay valoraciones: por eso el tipo es double? (nullable)
  double? get media {
    if (valoraciones.isEmpty) {
      return null;
    }
    // map convierte cada valoración en su nota; reduce las suma de dos en dos
    final suma = valoraciones.map((v) => v.nota).reduce((a, b) => a + b);
    return suma / total;
  }

  // Cuántas veces se ha dado cada nota, de la 5 a la 1: {5: 3, 4: 6, 3: 3, 2: 1, 1: 0}
  Map<int, int> get recuento {
    final veces = {5: 0, 4: 0, 3: 0, 2: 0, 1: 0};
    for (final valoracion in valoraciones) {
      veces[valoracion.nota] = veces[valoracion.nota]! + 1;
    }
    return veces;
  }
}

/// Cinco caracteres: un * por cada estrella y un - por cada una que falta.
/// Las estrellas son la media redondeada a un entero: 3.8 → "****-"
String estrellas(double media) {
  final llenas = media.round();
  return '*' * llenas + '-' * (5 - llenas);
}
```

**Comprueba que…** VS Code no marca nada en rojo en el archivo. Con la terminal en la carpeta del proyecto, `dart analyze` dice `No issues found!`.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `media` es `double?` y `total` es `int`, sin `?`?
2. Con las notas 4, 5 y 3, ¿qué devuelven `total`, `media`, `recuento` y `estrellas(media!)`?
3. ¿Qué diferencia hay entre `Valoracion(id: 1, nota: 4)` y `Valoracion.fromJson({'id': 1, 'nota': 4})`?
4. ¿Qué pasaría si en `recuento` se quitara el `!`?

<details>
<summary>Respuestas</summary>

1. Una lista siempre tiene un número de elementos, aunque sea 0. La media, en cambio, no existe si no hay valoraciones (sería dividir entre 0): el `?` lo dice, y el compilador obliga a quien la use a contar con el `null`.
2. `total` = 3; `media` = 4.0 (12 / 3); `recuento` = `{5: 1, 4: 1, 3: 1, 2: 0, 1: 0}`; `estrellas(4.0)` = `"****-"`.
3. Ninguna en el resultado: las dos crean la misma valoración. La primera recibe los datos sueltos, con nombre; la segunda, un mapa, que es como llegan del JSON.
4. No compilaría: `veces[valoracion.nota]` es `int?`, y a un `int?` no se le puede sumar 1. *Null safety* obliga a decir qué hacer con el posible `null`.

</details>

## Paso 4 · La descarga

`lib/servicio_valora.dart` pide cada archivo JSON al servidor y lo convierte en una lista de `Valoracion`.

Crea `lib/servicio_valora.dart`:

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `import 'dart:convert';` | La biblioteca de Dart con `jsonDecode`. Las que empiezan por `dart:` vienen con Dart |
| `import 'package:http/http.dart' as http;` | El paquete `http`, con el prefijo `http`: sus funciones se escriben `http.get(…)` |
| `import 'valoracion.dart';` | Las clases del paso 3, que están en la misma carpeta |
| `const urlBase = '…';` | La URL base: una constante |
| `Future<List<Valoracion>?> descargar(int evaluacion) async` | Una función asíncrona: devuelve un `Future` que, cuando se resuelva, tendrá la lista o `null` |
| `Uri.parse('${urlBase}valora$evaluacion.json')` | Convierte el texto de la URL en un `Uri`, que es lo que pide `http.get` |
| `await http.get(url)` | Pide el archivo y espera la respuesta |
| `respuesta.statusCode` | El código de estado: 200, 404… |
| `throw Exception('…')` | Lanza una excepción: la función termina ahí, y quien la llamó la recibe en su `catch` |
| `jsonDecode(respuesta.body) as List` | `respuesta.body` es el texto del archivo; `jsonDecode` lo convierte en objetos de Dart: aquí, una lista |
| `.map((elemento) => Valoracion.fromJson(…)).toList()` | Convierte cada elemento de la lista en una `Valoracion`, y el resultado, otra vez en lista (`map` devuelve una colección que hay que pasar a lista) |

```dart
// La descarga de las valoraciones, con el paquete http.
import 'dart:convert';

import 'package:http/http.dart' as http;

import 'valoracion.dart';

/// La carpeta de GitHub con los archivos valora1.json, valora2.json…
/// Son las mismas valoraciones de ejemplo que usa ValoraPMDM
const urlBase =
    'https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/';

/// Descarga las valoraciones de una evaluación.
/// Es asíncrona: devuelve un Future, que se resuelve cuando llega la respuesta.
/// El resultado es la lista de valoraciones, o null si el archivo no existe (404)
Future<List<Valoracion>?> descargar(int evaluacion) async {
  final url = Uri.parse('${urlBase}valora$evaluacion.json');
  // await: espera a que llegue la respuesta del servidor antes de seguir
  final respuesta = await http.get(url);

  if (respuesta.statusCode == 404) {
    return null; // esa evaluación aún no tiene archivo
  }
  if (respuesta.statusCode != 200) {
    throw Exception('error del servidor (${respuesta.statusCode})');
  }

  // jsonDecode convierte el texto JSON en objetos de Dart: aquí, una lista de mapas
  final lista = jsonDecode(respuesta.body) as List;
  // Cada mapa se convierte en una Valoracion con el constructor fromJson
  return lista
      .map((elemento) => Valoracion.fromJson(elemento as Map<String, dynamic>))
      .toList();
}
```

**Comprueba que…** `dart analyze` sigue diciendo `No issues found!`.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `descargar` devuelve `Future<List<Valoracion>?>` y no `List<Valoracion>`?
2. ¿Qué devuelve `descargar(3)` con las valoraciones de ejemplo, y por qué no es un error?
3. ¿Qué pasaría si se quitara el `await` de `http.get(url)`?

<details>
<summary>Respuestas</summary>

1. `Future`, porque la lista no está al momento: llega cuando responde el servidor. Y el `?` del final, porque puede no haber lista: si el archivo no existe, devuelve `null`.
2. `null`: `valora3.json` no existe, el servidor responde 404 y la función devuelve `null` sin lanzar nada. Que una evaluación aún no tenga valoraciones es normal.
3. `respuesta` sería un `Future<Response>`, no la respuesta, y `respuesta.statusCode` no compilaría: un `Future` no tiene `statusCode`. Hay que esperar a que llegue.

</details>

## Paso 5 · El programa principal

`bin/valora_dart.dart` es lo que se ejecuta: descarga las tres evaluaciones y escribe el resumen de cada una. Abre el archivo que creó la plantilla y **sustituye todo** su contenido por este:

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `import 'package:valora_dart/servicio_valora.dart';` | Las funciones de `lib`, con el nombre del paquete (el `name` de `pubspec.yaml`) |
| `Future<void> main() async` | `main` también puede ser asíncrona: así puede esperar a cada descarga |
| `for (var numero = 1; numero <= 3; numero++)` | Las tres evaluaciones, una detrás de otra |
| `try { … } catch (e) { … }` | Si algo lanza una excepción dentro del `try`, el `catch` la recoge en `e`: el programa avisa y sigue |
| `final media = evaluacion.media;` | Se pide la media una sola vez y se guarda: el getter calcula cada vez que se usa |
| `media == null ? '-' : media.toStringAsFixed(1)` | El ternario: si no hay media, una raya; si la hay, con un decimal. Dentro de la segunda parte, Dart ya sabe que `media` no es `null` |
| `evaluacion.recuento.forEach((nota, veces) { … })` | Recorre el mapa: para cada par, una función anónima recibe la clave (`nota`) y el valor (`veces`) |
| `barra.padRight(evaluacion.total)` | Rellena la barra con espacios por la derecha hasta ese ancho: así los números de la derecha quedan en columna |

```dart
// ValoraDart: descarga las valoraciones de cada evaluación y muestra
// su media, sus estrellas y el recuento de cada nota, en la consola.
import 'package:valora_dart/servicio_valora.dart';
import 'package:valora_dart/valoracion.dart';

// main es async porque espera (await) a cada descarga
Future<void> main() async {
  print('=== ValoraDart ===');

  for (var numero = 1; numero <= 3; numero++) {
    try {
      final valoraciones = await descargar(numero);
      if (valoraciones == null) {
        print('\nEvaluación $numero: sin archivo (404)');
      } else {
        mostrar(Evaluacion(numero, valoraciones));
      }
    } catch (e) {
      // Sin conexión, o con un error del servidor, el programa avisa y sigue
      print('\nEvaluación $numero: no se ha podido descargar ($e)');
    }
  }
}

/// Escribe el resumen de una evaluación
void mostrar(Evaluacion evaluacion) {
  final media = evaluacion.media;
  print('\nEvaluación ${evaluacion.numero}: ${evaluacion.total} valoraciones');

  // El operador ternario: si no hay media, una raya
  final textoMedia = media == null ? '-' : media.toStringAsFixed(1);
  final textoEstrellas = media == null ? '' : estrellas(media);
  print('  Media: $textoMedia  $textoEstrellas');

  // Una barra de # por cada nota, tan larga como las veces que se ha dado
  evaluacion.recuento.forEach((nota, veces) {
    final barra = '#' * veces;
    print('  $nota | ${barra.padRight(evaluacion.total)} $veces');
  });
}
```

**Comprueba que…** `dart analyze` dice `No issues found!`.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `main` es `async`?
2. Si se corta la conexión, ¿qué escribe el programa, y por qué no se cierra?
3. ¿Por qué `mostrar` puede escribir `media.toStringAsFixed(1)` sin `!`, si `media` es `double?`?

<details>
<summary>Respuestas</summary>

1. Porque usa `await` para esperar cada descarga, y `await` solo se puede usar dentro de una función `async`.
2. Para cada evaluación, «no se ha podido descargar» con el motivo. `http.get` lanza una excepción, sale de `descargar` y llega al `catch` de `main`, que la escribe; después, el `for` sigue con la siguiente.
3. Porque está en la parte del ternario que solo se ejecuta si `media == null` es falso. Dart lo deduce (se llama *promoción de tipos*): ahí, `media` ya es `double`.

</details>

## Paso 6 · Probar el programa

En la terminal de VS Code, en la carpeta del proyecto:

```powershell
dart run
```

`dart run` ejecuta el programa del proyecto, `bin/valora_dart.dart`. La primera vez lo compila, y tarda unos segundos.

**Las pruebas:**

| Nº | Prueba | Cómo | Resultado esperado |
|---|---|---|---|
| 1 | Normal | `dart run` | Lo del punto 1: la 1ª evaluación con 13 valoraciones, media **3.8**, `****-`, y 5 → 3, 4 → 6, 3 → 3, 2 → 1, 1 → 0 |
| 2 | Otra evaluación | Mirar la 2ª | Media **4.4**, `****-`, y 5 → 6, 4 → 6, 3 → 1. Son las mismas medias que en ValoraPMDM |
| 3 | No existe | Mirar la 3ª | «Evaluación 3: sin archivo (404)» |
| 4 | Sin conexión | Desconectar el Wi-Fi (o el cable) del ordenador y `dart run` | Para cada evaluación, «no se ha podido descargar (ClientException…)». El programa termina normalmente. Vuelve a conectarte |
| 5 | Un `print` | En `main`, justo después de `final valoraciones = await descargar(numero);`, añade `print(valoraciones);` y `dart run` | Sale la lista, con cada valoración escrita por `toString`: `[Valoración 1: 4, Valoración 2: 5, …]` y, en la 3ª, `null`. Después, quita la línea |

**Comprueba que…** las cinco pruebas dan el resultado esperado.

---

## Si algo falla

| Qué pasa | Qué hacer |
|---|---|
| `dart` no se reconoce | Falta `C:\dam2627\flutter\bin` en el `Path`, o la terminal se abrió antes de añadirlo: ciérrala y abre otra (guía «Hola Flutter», paso 2) |
| `Target of URI doesn't exist: 'package:http/http.dart'` | Falta el paquete: `dart pub add http` en la carpeta del proyecto |
| `Target of URI doesn't exist: 'package:valora_dart/…'` | El `name` de `pubspec.yaml` no es `valora_dart`, o el archivo no está en `lib` |
| Error en `test/valora_dart_test.dart` | Es la prueba de la plantilla, que usa el ejemplo borrado: borra también ese archivo (paso 1) |
| `The method '…' isn't defined` o algo en rojo | Un nombre mal escrito: Dart distingue mayúsculas de minúsculas. Compara con el código de la guía |
| `A value of type 'int?' can't be assigned…` o `can't be unconditionally invoked because the receiver can be 'null'` | Es *null safety*: falta un `!` o comprobar el `null`. Compara con el código de la guía |
| Sale «no se ha podido descargar» con conexión | La URL base está mal escrita: ábrela en el navegador con `valora1.json` al final |
| Las tildes salen mal («EvaluaciÃ³n») | La terminal no está en UTF-8. En la terminal de VS Code se ven bien; en otra, ejecuta antes `chcp 65001` |

## Los archivos del proyecto

```
C:\dam2627\pmdm\valora_dart
├── bin
│   └── valora_dart.dart        el programa: main y mostrar (paso 5)
├── lib
│   ├── servicio_valora.dart    la descarga, con http (paso 4)
│   └── valoracion.dart         las clases y los cálculos (paso 3)
├── pubspec.yaml                el paquete y sus dependencias (paso 2)
├── pubspec.lock                las versiones exactas instaladas (lo crea dart pub)
└── .dart_tool                  lo que usa Dart al compilar (no se toca)
```
