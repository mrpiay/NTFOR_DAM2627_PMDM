# Repaso · UD1 y UD2

Repaso de lo que entró en la P1 (Julia), el examen de octubre: el proyecto Android, Kotlin y Compose. Lo que se explica en los apuntes de la semana del 2 al 6 solo se menciona aquí, con su enlace.

---

## 1. `minSdk` y `targetSdk`

**En una frase:** `minSdk` dice en qué versiones de Android se puede instalar la app; `targetSdk`, para qué versión está preparada.

**Lo que tienes que saber**

- Los dos están en `defaultConfig`, dentro de `build.gradle.kts` del módulo `app`, como **niveles de API** (cada versión de Android tiene el suyo: Android 7 es la API 24; Android 16, la API 36).
- **`minSdk`**: la API **mínima**. En un dispositivo con una API menor, la app **no se instala**.
- **`targetSdk`**: la API para la que la app **declara estar adaptada**. Android aplica los comportamientos de esa versión (por ejemplo, cómo se piden los permisos). No impide instalarla en versiones más antiguas.

**Compara** (con `minSdk = 26` y `targetSdk = 35`)

| Dispositivo | ¿Se instala? | Por qué |
|---|---|---|
| API 24 | No | Es menor que `minSdk` |
| API 30 | Sí | Está entre `minSdk` y `targetSdk` |
| API 36 | Sí | Es mayor que `targetSdk`: se instala y Android la trata como adaptada a la 35 |

---

## 2. Dónde va cada cosa del proyecto

**En una frase:** cada tipo de cambio tiene su sitio en el proyecto.

**Lo que tienes que saber**

| Cambio | Dónde |
|---|---|
| Una imagen nueva (`foto.png`) | `res/drawable/` |
| El icono de la app | `res/mipmap/` |
| Un texto, también el nombre de la app | `res/values/strings.xml` |
| Declarar una actividad o un permiso | `AndroidManifest.xml` |
| La versión que ve el usuario (`versionName`), `minSdk`, `targetSdk` y las librerías | `build.gradle.kts` del módulo `app` |

- **`R.drawable.foto`** no es la imagen ni una ruta: es el **identificador** (un número) que Android genera para el recurso `res/drawable/foto.png`. Se usa con `painterResource(R.drawable.foto)`.
- Lo mismo con los textos: `stringResource(R.string.titulo)`.

Más sobre la estructura: [UD1, apartado 5](../../2026_11_02-06/apuntes/UD1-tecnologias-y-entornos.md#5-la-estructura-de-un-proyecto-android).

---

## 3. Funciones `@Composable` y `Modifier`

**En una frase:** una función `@Composable` dibuja interfaz, solo se puede llamar desde otra `@Composable`, y su aspecto se ajusta con un `Modifier`.

**Lo que tienes que saber**

- Una función marcada con **`@Composable` solo se puede llamar desde otra función `@Composable`** (o desde `setContent`). Llamarla desde una función normal da un error de compilación.
- **`Modifier`** encadena ajustes con puntos, **en orden**, y cada uno se aplica sobre el resultado del anterior.
- **`padding(16.dp)`** deja ese espacio **alrededor de todo el contenido** del elemento al que se aplica. En una `Column`, alrededor de toda la columna, no entre sus elementos.
- **`fillMaxWidth()`** hace que el elemento intente ocupar **todo el ancho disponible**.
- El orden importa: `Modifier.background(Color.Yellow).padding(16.dp)` pinta también el margen; `Modifier.padding(16.dp).background(Color.Yellow)` deja el margen sin pintar.

```kotlin
@Composable
fun Ficha(nombre: String) {
    Column(modifier = Modifier.padding(16.dp)) {   // 16 dp alrededor de toda la columna
        Text(nombre)
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {   // todo el ancho
            Text("Adoptar")
        }
    }
}
```

Más sobre Compose: [UD2, apartado 2](../../2026_11_02-06/apuntes/UD2-kotlin-y-compose.md#2-jetpack-compose).

---

## 4. `when`

**En una frase:** `when` elige un valor según la **primera** condición que se cumple.

**Lo que tienes que saber**

- Se comprueban las ramas **de arriba abajo**, y **gana la primera** que se cumple; las siguientes ya no se miran.
- Por eso el orden importa: las condiciones más exigentes van primero.
- `else` cubre todo lo demás. Cuando `when` devuelve un valor, `else` es obligatorio.

**Compara** (con `val temp = 30`)

| Código | Resultado | Por qué |
|---|---|---|
| `when { temp >= 20 -> "Templado"; temp >= 28 -> "Calor"; else -> "Frío" }` | `"Templado"` | `temp >= 20` se cumple primero y la rama de `"Calor"` nunca llega a mirarse |
| `when { temp >= 28 -> "Calor"; temp >= 20 -> "Templado"; else -> "Frío" }` | `"Calor"` | La condición más exigente va primero |

---

## 5. Tipos función y lambdas

**En una frase:** en Kotlin una función se puede guardar en una variable o pasar como parámetro; su tipo dice qué recibe y qué devuelve.

**Lo que tienes que saber**

- **`(Int) -> Int`**: una función que recibe un `Int` y devuelve un `Int`. **`(String) -> Unit`**: recibe un `String` y no devuelve nada (`Unit`).
- Una **lambda** es una función sin nombre entre llaves: `{ n -> n + 1 }`. Si tiene un solo parámetro, se puede llamar **`it`**: `{ it + 1 }`.
- Guardar una lambda **no la ejecuta**: se ejecuta cuando se **llama**, con paréntesis.
- Si el **último parámetro** de una función es otra función, la lambda puede ir **fuera de los paréntesis**: `repetir(3) { println("Hola") }`.

```kotlin
val triple: (Int) -> Int = { n -> n * 3 }      // guarda la función, no la ejecuta
val r = triple(5)                              // ahora sí: r vale 15

fun aplicar(valor: Int, f: (Int) -> Int): Int = f(valor)
val s = aplicar(4) { it - 1 }                  // la lambda se ejecuta dentro de aplicar: s vale 3
```

Más sobre lambdas: [UD2, apartado 1](../../2026_11_02-06/apuntes/UD2-kotlin-y-compose.md#1-kotlin-lo-básico).

---

## 6. El estado: `remember`, recomposición y elevar el estado

**En una frase:** el estado es lo que la pantalla muestra y puede cambiar; vive en un sitio y baja como valor, y los cambios suben como eventos.

**Lo que tienes que saber**

- **`mutableStateOf`** crea un estado **observable**: cuando cambia, Compose vuelve a ejecutar (**recompone**) los composables que lo leen.
- **`remember`** **conserva** ese estado entre recomposiciones. Sin `remember`, en cada recomposición se volvería a crear con el valor inicial.
- Un `var x = 0` normal **no es observable**: cambiarlo no actualiza la pantalla, y en cada recomposición vuelve a valer `0`.
- En cada recomposición se vuelve a ejecutar **toda la función**. Por eso un `val` calculado a partir del estado (`val mensaje = if (likes > 10) "Popular" else "Nuevo"`) tiene el valor nuevo: no se modifica, se crea otra vez.
- **Elevar el estado** (*state hoisting*): el estado lo guarda el composable de **arriba**; el de abajo recibe el **valor** y una **función** para avisar de los cambios. Así el de abajo es reutilizable y no tiene estado propio.

```kotlin
@Composable
fun Pantalla() {
    var likes by remember { mutableStateOf(0) }            // el estado vive arriba
    BotonLike(likes = likes, onLike = { likes = it })      // baja el valor y una función
}

@Composable
fun BotonLike(likes: Int, onLike: (Int) -> Unit) {        // sin estado propio
    Button(onClick = { onLike(likes + 1) }) {              // avisa del cambio hacia arriba
        Text("♥ $likes")
    }
}
```

Más sobre el estado: [UD2, apartado 3](../../2026_11_02-06/apuntes/UD2-kotlin-y-compose.md#3-el-estado-y-la-recomposición).

---

## 7. Lo demás, en los apuntes de la semana del 2 al 6

| Tema | Dónde |
|---|---|
| Limitaciones de un dispositivo móvil (batería, memoria, red…) | [UD1, apartado 1](../../2026_11_02-06/apuntes/UD1-tecnologias-y-entornos.md#1-las-limitaciones-de-un-dispositivo-móvil) |
| Versiones de Android y niveles de API | [UD1, apartado 3](../../2026_11_02-06/apuntes/UD1-tecnologias-y-entornos.md#3-android-y-sus-versiones) |
| `val` y `var`, null safety (`?`, `?.`, `?:`, `!!`), `data class` y listas | [UD2, apartado 1](../../2026_11_02-06/apuntes/UD2-kotlin-y-compose.md#1-kotlin-lo-básico) |
