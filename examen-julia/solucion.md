# PROGRAMACIÓN MULTIMEDIA Y DISPOSITIVOS MÓVILES — SOLUCIONES

---

## CUESTIONARIO

**1. b) El uso continuado puede aumentar el consumo de batería y recursos del dispositivo.** GPS, cámara y datos móviles son de lo que más batería gasta; en un móvil, la batería, la memoria y la red son limitaciones propias. Las otras opciones son falsas.

**2. c) Podrá instalarse en B y C; *minSdk* establece la API mínima admitida y *targetSdk* indica la versión de Android para la que la aplicación declara estar adaptada en cuanto a comportamiento.** El dispositivo A (API 23) está por debajo de `minSdk = 24` y no puede instalarla. `targetSdk` no impide instalar en versiones más antiguas.

**3. a) res/drawable — strings.xml — AndroidManifest.xml — build.gradle.kts.** Las imágenes van en `res/drawable`, los textos (también el nombre de la app) en `strings.xml`, las actividades se declaran en el manifiesto y `versionName` está en `build.gradle.kts`.

**4. d) Porque una función *@Composable* debe llamarse desde un contexto composable.** `mostrarSaludo()` es una función normal, así que no puede llamar a `Mensaje()`: da un error de compilación.

**5. c) Añade espacio alrededor del contenido completo de la Column.** El `Modifier` va en la `Column`, así que el margen rodea todo lo que hay dentro, no solo un elemento ni el espacio entre "A" y "B".

**6. d) El identificador generado para un recurso de la aplicación.** Android genera un identificador (un número) para cada recurso de `res/`; `R.drawable.logo` es el de `res/drawable/logo.png`.

**7. d) El botón intenta ocupar todo el ancho disponible y se aplica un espacio de 16 dp alrededor mediante padding.** `fillMaxWidth()` es el ancho, no el alto, y `padding(16.dp)` es un margen, no el tamaño.

**8. c) "ANA".** `nombre` no es `null`, así que `?.uppercase()` se ejecuta y el operador Elvis (`?:`) no actúa.

**9. c) `nombre?.length`.** La llamada segura `?.` devuelve `null` en lugar de fallar. `nombre!!.length` lanza una excepción con `null`, y `nombre.length` y `nombre.uppercase()` ni siquiera compilan con un tipo anulable.

**10. c) "Nivel basico".** El código no declara `puntos`, pero con cualquier valor ≥ 5 (por ejemplo, 8) el `when` se queda con la primera rama que se cumple, `puntos >= 5`. La rama "Nivel medio" nunca llega a evaluarse.

**11. b) operacion almacena una función que recibe un Int y devuelve un Int.** La lambda no se ejecuta al declararla: se ejecuta en `operacion(4)`, y `resultado` vale 8, no la función.

**12. c) accion es una función que recibe un Int y no devuelve ningún valor; al ejecutarse con 5, resultado pasa a valer 15.** `(Int) -> Unit` es el tipo de `accion`, no de `ejecutarAccion()`, y la lambda se ejecuta dentro de `ejecutarAccion()`, no antes.

**13. b) *mutableStateOf* crea un estado observable y *remember* permite conservarlo durante las recomposiciones.** Hacen cosas distintas, y ninguno conserva el valor si se cierra la app.

**14. c) redondear pasa a true y el Text puede actualizarse mostrando "Resultado redondeado".** `it` es el valor nuevo del `Switch` (`true`); al cambiar el estado, Compose recompone y el `Text` muestra el otro texto.

**15. a) El estado de cantidad se mantiene en Pantalla, mientras que SelectorCantidad recibe el valor y una función para comunicar los cambios.** Es elevar el estado (*state hoisting*): el hijo no tiene estado propio, y `onCantidadChange` es una función, no un número.

**16. c) Porque al cambiar resultado, Compose puede recomponer el composable y volver a ejecutar la expresión que calcula imagen.** `imagen` no se modifica: en cada recomposición se crea otra vez con el valor actual de `resultado`.

| Pregunta | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|:--------:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| **Respuesta** | b | c | a | d | c | d | d | c |

| Pregunta | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 |
|:--------:|:-:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|
| **Respuesta** | c | c | b | c | b | c | a | c |

---

## EJERCICIOS

### Ejercicio 1 (1.5 puntos)

```kotlin
fun obtenerNombre(nombre: String?): String {

    val usuario = nombre ?: "Invitado"

    return usuario.uppercase()
}
```

a) ¿Qué significa que nombre sea de tipo `String?`?

**Que es un tipo anulable: `nombre` puede contener un String o el valor `null`. El compilador obliga a tratar el posible `null` antes de usarlo.**

b) Explica qué hace: `nombre ?: "Invitado"`

**Es el operador Elvis. Si `nombre` no es `null`, se usa su valor; si es `null`, se usa "Invitado".**

*También vale explicarlo con su equivalente: `if (nombre != null) nombre else "Invitado"`.*

c) ¿Qué devuelve exactamente esta llamada? `obtenerNombre(null)`

**"INVITADO". Al ser `null` se usa "Invitado" y después se pasa a mayúsculas con `uppercase()`.**

d) ¿Qué devuelve exactamente esta llamada? `obtenerNombre("Laura")`

**"LAURA".**

e) Modifica únicamente la expresión necesaria para que, cuando nombre sea null, se utilice "Desconocido" en lugar de "Invitado".

**Solución:**

```kotlin
val usuario = nombre ?: "Desconocido"
```

*También vale `val usuario = if (nombre == null) "Desconocido" else nombre`, aunque el enunciado pide cambiar solo la expresión necesaria.*

f) ¿Qué ocurriría si sustituyésemos `val usuario = nombre ?: "Invitado"` por `val usuario = nombre!!` y llamásemos a `obtenerNombre(null)`?

**El código compila, pero al ejecutarse lanza una `NullPointerException`. El operador `!!` afirma que el valor no es `null`, y si lo es, el programa falla en tiempo de ejecución.**

### Ejercicio 2 (1.5 puntos)

```kotlin
fun aplicarCambio(
    valor: Int,
    cambio: (Int) -> Int
): Int {
    return cambio(valor)
}

val resultado1 = aplicarCambio(4) { numero ->
    numero * 2
}

val resultado2 = aplicarCambio(resultado1) {
    it + 3
}
```

a) ¿Qué significa el tipo `(Int) -> Int` en el parámetro cambio?

**Que `cambio` es una función que recibe un Int y devuelve un Int.**

b) ¿Qué valor tendrá resultado1? Explica brevemente cómo se obtiene.

**8. Se llama a `aplicarCambio` con `valor = 4` y la lambda `numero -> numero * 2`. La función ejecuta `cambio(4)`, que devuelve 4 * 2 = 8.**

c) ¿Qué representa `it` en la segunda llamada?

**Es el nombre implícito del único parámetro de la lambda. Representa el valor que recibe la lambda, en este caso `resultado1` (8).**

d) ¿Qué valor tendrá finalmente resultado2?

**11 (8 + 3).**

e) Explica en qué momento se ejecuta la lambda que se pasa a aplicarCambio.

**No se ejecuta al escribirla ni al pasarla como argumento. Se ejecuta cuando, dentro de `aplicarCambio`, se invoca `cambio(valor)` en la línea del `return`.**

*Otras formas equivalentes de escribir las llamadas, que dan los mismos resultados: `aplicarCambio(4) { it * 2 }` y `aplicarCambio(resultado1, { n -> n + 3 })`.*

### Ejercicio 3 (1.5 puntos)

```kotlin
@Composable
fun Contador() {

    var contador by remember {
        mutableStateOf(0)
    }

    val mensaje = if (contador >= 3) {
        "Objetivo alcanzado"
    } else {
        "Sigue intentándolo"
    }

    Column {

        Text("Valor: $contador")
        Text(mensaje)

        Button(
            onClick = {
                contador++
            }
        ) {
            Text("Sumar")
        }
    }
}
```

a) ¿Qué valores mostrarán los dos Text después de pulsar tres veces el botón?

**"Valor: 3" y "Objetivo alcanzado".**

b) Explica por qué, al modificar contador, puede actualizarse la interfaz.

**Porque `contador` es un estado observable creado con `mutableStateOf`. Cuando su valor cambia, Compose recompone los composables que lo leen y la interfaz muestra el nuevo valor. `remember` conserva ese estado entre recomposiciones.**

c) mensaje está declarado con val. ¿Por qué puede mostrarse un mensaje diferente después de cambiar contador si una variable val no puede modificarse?

**Porque `mensaje` no se modifica: en cada recomposición se vuelve a ejecutar la función `Contador()` y se crea un `val mensaje` nuevo, calculado con el valor actual de `contador`.**

d) ¿Qué problema tendría este código si sustituyésemos la declaración de contador por:

```kotlin
var contador = 0
```

Explica qué ocurriría con la actualización de la interfaz y con el valor de contador durante una recomposición.

**Habría dos problemas. `contador` ya no sería un estado observable, así que al incrementarlo Compose no se enteraría y la interfaz no se actualizaría. Además, al no usar `remember`, en cada recomposición se volvería a ejecutar `var contador = 0` y el valor se reiniciaría a 0.**

*Una declaración correcta alternativa: `var contador by remember { mutableIntStateOf(0) }`, la versión de `mutableStateOf` para enteros.*

### Ejercicio 4 (1.5 puntos)

Completa el siguiente código para que:

- numero sea un estado inicializado a 1;
- imagen seleccione el recurso correspondiente mediante when;
- Image muestre el recurso calculado;
- al pulsar el botón se genere aleatoriamente un número entre 1 y 3.

**Solución (los nombres de los recursos `imagen1`, `imagen2` e `imagen3` son de ejemplo):**

```kotlin
var numero by remember { mutableStateOf(1) }

val imagen = when (numero) {
    1 -> R.drawable.imagen1
    2 -> R.drawable.imagen2
    else -> R.drawable.imagen3
}

Image(
    painter = painterResource(imagen),
    contentDescription = null
)

Button(
    onClick = {
        numero = (1..3).random()
    }
) {
    Text("Cambiar imagen")
}
```

**Otras respuestas válidas para cada hueco:**

| Hueco | También vale |
|---|---|
| El estado | `remember { mutableIntStateOf(1) }` |
| Los recursos del `when` | Cualquier nombre de `R.drawable` que exista, siempre con `else` en la última rama |
| `painter` | `painterResource(id = imagen)` |
| El número aleatorio | `Random.nextInt(1, 4)` (con `import kotlin.random.Random`; el 4 no se incluye) o `(1..3).random()` |

Sin `by`, con `val numero = remember { mutableStateOf(1) }`, también funciona, pero entonces se usa `numero.value` en el `when` y en el botón.
