# Kotlin, Dart y Java: los tipos de datos

Una comparación de los tipos de datos de los tres lenguajes del ciclo:

- **Java**, el de 1º, el de ValoraPROG;
- **Kotlin**, el de Android, el de ValoraPMDM;
- **Dart**, el de Flutter, el de ValoraDart.

Cada tabla muestra lo mismo en los tres lenguajes, también cuando coinciden: así se ve qué se puede dar por sabido y dónde hay que tener cuidado. Las salidas y los errores que aparecen son los que dan los tres lenguajes al ejecutar el código.

---

## 1. Los tipos básicos

| | Java | Kotlin | Dart |
|---|---|---|---|
| Número entero | `int` | `Int` | `int` |
| Número decimal | `double` | `Double` | `double` |
| Verdadero o falso | `boolean` | `Boolean` | `bool` |
| Texto | `String` | `String` | `String` |
| Un carácter | `char` | `Char` | No tiene: se usa un `String` de una letra |

- En **Java**, `int`, `double`, `boolean` y `char` son **tipos primitivos**: no son objetos y no tienen métodos. Para meterlos en una lista se usa su clase, `Integer`, `Double`…, y por eso se escribe `List<Integer>` y no `List<int>`.
- En **Kotlin** y en **Dart**, **todo es un objeto**, también los números: `7.toDouble()` en Kotlin y `media.round()` en Dart son métodos del número. En una lista se escribe el mismo tipo: `List<Int>` y `List<int>`.

### La división

| | Java | Kotlin | Dart |
|---|---|---|---|
| `7 / 2` | `3` | `3` | **`3.5`** |
| División con decimales | `(double) 7 / 2` → `3.5` | `7.toDouble() / 2` o `7.0 / 2` → `3.5` | `7 / 2` → `3.5` |
| División entera | `7 / 2` → `3` | `7 / 2` → `3` | `7 ~/ 2` → `3` |

En Java y en Kotlin, dos enteros dan una división entera. En Dart, `/` siempre da un `double`, y la división entera tiene su propio operador, `~/`.

Por eso ValoraPROG necesita `(double) suma / total` para calcular la media, y ValoraDart escribe solo `suma / total`.

---

## 2. Variables

| | Java | Kotlin | Dart |
|---|---|---|---|
| Con el tipo escrito | `int total = 3;` | `var total: Int = 3` | `int total = 3;` |
| Con el tipo deducido | `var total = 3;` | `var total = 3` | `var total = 3;` |
| No se puede reasignar | `final int total = 3;` | `val total = 3` | `final total = 3;` |
| Fija desde que se compila | `static final` (una constante) | `const val` | `const total = 3;` |

- **Dónde va el tipo:** en Java y en Dart, **delante** del nombre (`int total`); en Kotlin, **detrás**, con dos puntos (`total: Int`).
- **El `;`:** en Java es obligatorio y en Dart también; en Kotlin no se pone.

---

## 3. Los valores nulos

| | Java | Kotlin | Dart |
|---|---|---|---|
| Qué puede valer `null` | Cualquier objeto | Solo un tipo con `?`: `String?` | Solo un tipo con `?`: `String?` |
| Llamada segura: `null` si es `null` | No tiene | `nombre?.length` | `nombre?.length` |
| Valor por si es `null` | `if (nombre == null) …` | `nombre?.length ?: 0` (operador Elvis) | `nombre?.length ?? 0` |
| «No es `null`, seguro» | No hace falta decirlo | `nombre!!.length` | `nombre!.length` |
| Si al final era `null` | `NullPointerException` | `NullPointerException` | `Null check operator used on a null value` |

En Java, el compilador deja llamar a un método sobre algo que puede ser `null`, y el error salta al ejecutar el programa. En Kotlin y en Dart, el compilador **no deja** usar un `String?` como si fuera un `String`: obliga a decir qué hacer con el `null` (*null safety*). Por eso el `media` de ValoraDart es `double?` y el de ValoraPMDM, `Double?`: sin valoraciones no hay media.

La única diferencia entre los dos es la escritura: `?:` y `!!` en Kotlin, `??` y `!` en Dart.

---

## 4. Listas

Una lista guarda elementos **en orden** y se accede a ellos por su posición, empezando en 0.

| | Java | Kotlin | Dart |
|---|---|---|---|
| Crear una lista que no cambia | `List.of(4, 5, 3)` | `listOf(4, 5, 3)` | `const [4, 5, 3]` |
| Crear una lista que cambia | `new ArrayList<>(List.of(4, 5, 3))` | `mutableListOf(4, 5, 3)` | `[4, 5, 3]` |
| Tipo | `List<Integer>` | `List<Int>` y `MutableList<Int>` | `List<int>` |
| Cuántos elementos | `notas.size()` | `notas.size` | `notas.length` |
| El primero | `notas.get(0)` | `notas[0]` | `notas[0]` |
| Añadir | `notas.add(2)` | `notas.add(2)` | `notas.add(2)` |
| Posición que no existe | `IndexOutOfBoundsException` | `IndexOutOfBoundsException` (o `notas.getOrNull(10)`, que da `null`) | `RangeError` |

**Lo que más cambia es qué lista se puede modificar:**

- En **Dart**, una lista entre corchetes, `[4, 5, 3]`, **se puede cambiar**. Para que no cambie, se escribe `const`.
- En **Kotlin** es al revés: `listOf` da una lista que **no se puede cambiar**, y para cambiarla hace falta `mutableListOf`.
- En **Java**, `List.of` tampoco se puede cambiar, y la lista que cambia es un `ArrayList`.

Y el error salta en momentos distintos al intentar añadir a una lista que no cambia:

| Java | Kotlin | Dart |
|---|---|---|
| Al ejecutar: `UnsupportedOperationException` | **Al compilar:** `unresolved reference 'add'`. Una `List` de Kotlin no tiene `add` | Al ejecutar: `Unsupported operation: Cannot add to an unmodifiable list` |

---

## 5. Mapas

Un mapa guarda **parejas clave → valor**, y se busca un valor por su clave. Es lo que usan ValoraDart y ValoraPMDM para el recuento: la nota es la clave, y las veces que se ha dado, el valor.

| | Java | Kotlin | Dart |
|---|---|---|---|
| Crear | `new LinkedHashMap<>()` y un `put` por pareja | `mutableMapOf(5 to 3, 4 to 6)` | `{5: 3, 4: 6}` |
| Tipo | `Map<Integer, Integer>` | `Map<Int, Int>` y `MutableMap<Int, Int>` | `Map<int, int>` |
| Leer un valor | `recuento.get(5)` | `recuento[5]` | `recuento[5]` |
| Guardar un valor | `recuento.put(5, 4)` | `recuento[5] = 4` | `recuento[5] = 4` |
| Leer una clave que no está | `null` | `null` | `null` |
| Con un valor por defecto | `recuento.getOrDefault(1, 0)` | `recuento[1] ?: 0` | `recuento[1] ?? 0` |
| Recorrerlo | `for (Map.Entry<Integer, Integer> e : recuento.entrySet())` | `for ((nota, veces) in recuento)` | `recuento.forEach((nota, veces) { … })` |
| Se imprime | `{5=3, 4=6, 3=3}` | `{5=3, 4=6, 3=3}` | `{5: 3, 4: 6, 3: 3}` |

- **Leer una clave da un tipo que puede ser `null`.** En Kotlin y en Dart, `recuento[5]` es `Int?` o `int?`, aunque la clave esté. Por eso ValoraDart escribe `veces[valoracion.nota]! + 1`: el `!` dice que la nota siempre está en el mapa.
- **El orden.** Los mapas de Kotlin (`mapOf`, `mutableMapOf`) y de Dart (`{…}`) guardan las parejas **en el orden en que se añadieron**. En Java depende de la clase: un `LinkedHashMap` también las guarda en orden, pero un `HashMap` no. Con las mismas parejas, `5, 4, 3`, un `HashMap` imprime `{3=3, 4=6, 5=3}`.

---

## 6. Conjuntos

Un conjunto guarda elementos **sin repetir**: si se añade uno que ya está, no cambia nada.

| | Java | Kotlin | Dart |
|---|---|---|---|
| Crear | `new HashSet<>(List.of(4, 5, 4, 3))` | `setOf(4, 5, 4, 3)` | `{4, 5, 4, 3}` |
| Resultado | `[3, 4, 5]`, 3 elementos | `[4, 5, 3]`, 3 elementos | `{4, 5, 3}`, 3 elementos |

**La trampa de Dart:** las llaves sirven para los mapas y para los conjuntos. Con elementos sueltos (`{4, 5, 3}`) es un conjunto; con parejas (`{5: 3}`), un mapa. Y vacías, `{}`, son **un mapa**, no un conjunto. Un conjunto vacío se escribe con el tipo delante: `<int>{}`.

---

## 7. Operaciones con colecciones

Los tres lenguajes permiten transformar una lista con una **función sin nombre** (lambda), que se escribe de forma distinta:

| | Java | Kotlin | Dart |
|---|---|---|---|
| Lambda que dobla un número | `n -> n * 2` | `{ n -> n * 2 }` o `{ it * 2 }` | `(n) => n * 2` |
| Las notas de 4 o más | `notas.stream().filter(n -> n >= 4).toList()` | `notas.filter { it >= 4 }` | `notas.where((n) => n >= 4).toList()` |
| Cada nota, doblada | `notas.stream().map(n -> n * 2).toList()` | `notas.map { it * 2 }` | `notas.map((n) => n * 2).toList()` |
| Cuántas notas son 4 | `notas.stream().filter(n -> n == 4).count()` | `notas.count { it == 4 }` | `notas.where((n) => n == 4).length` |
| La suma | `notas.stream().mapToInt(n -> n).sum()` | `notas.sum()` | `notas.reduce((a, b) => a + b)` |
| La media | `notas.stream().mapToInt(n -> n).average()` | `notas.average()` | No tiene: la suma entre `notas.length` |

- En **Kotlin**, `it` es el parámetro de una lambda que solo tiene uno.
- En **Java**, las operaciones van sobre un *stream* (`notas.stream()`), no sobre la lista.
- En **Dart**, `filter` se llama **`where`**. Además, `where` y `map` no devuelven una lista, sino un `Iterable`: `notas.where((n) => n >= 4)` se imprime `(4, 5)`, entre paréntesis. Para tener una lista hace falta `.toList()`. Kotlin devuelve directamente una lista: `[4, 5]`.

---

## 8. El mismo cálculo en los tres proyectos Valora

La media de las notas, tal como la calcula cada proyecto.

**ValoraPROG (Java):** las notas están en una matriz, `notas[fila][categoria]`, y se suman con un bucle.

```java
public static double media(int[][] notas, int total, int categoria) {
    int suma = 0;
    for (int fila = 0; fila < total; fila++) {
        suma += notas[fila][categoria];
    }
    return (double) suma / total;
}
```

**ValoraPMDM (Kotlin):** las valoraciones están en una lista. `map` saca la nota de cada una y `average` hace la media.

```kotlin
val media: Double?
    get() = if (lista.isEmpty()) null else lista.map { it.nota }.average()
```

**ValoraDart (Dart):** también una lista. `map` saca las notas, `reduce` las suma y `/` ya da un `double`.

```dart
double? get media {
  if (valoraciones.isEmpty) {
    return null;
  }
  final suma = valoraciones.map((v) => v.nota).reduce((a, b) => a + b);
  return suma / total;
}
```

Las diferencias de esta guía, juntas en un solo cálculo:

- La media puede no existir en Kotlin y en Dart: `Double?` y `double?` (apartado 3). En Java, un `double` no puede ser `null`: con `total` 0, `(double) 0 / 0` da `NaN` (*not a number*), así que ValoraPROG comprueba `total == 0` en el menú antes de calcularla.
- Java necesita `(double)`; Dart no (apartado 1).
- Kotlin tiene `average`; Dart no, y suma con `reduce` (apartado 7).

---

## Dónde consultar

- **Kotlin:** los [apuntes de Kotlin y Compose](../../2026_11_02-06/apuntes/UD2-kotlin-y-compose.md), apartado 1; y el libro de McGraw Hill, unidad 1, «El lenguaje Kotlin».
- **Dart:** la [guía de ValoraDart](guia-ValoraDart.md), apartado 3, «El lenguaje Dart»; y el libro de McGraw Hill, unidad 4, «El lenguaje de programación Dart».
- **La documentación oficial**, en inglés: [colecciones de Kotlin](https://kotlinlang.org/docs/collections-overview.html) y [colecciones de Dart](https://dart.dev/language/collections).
