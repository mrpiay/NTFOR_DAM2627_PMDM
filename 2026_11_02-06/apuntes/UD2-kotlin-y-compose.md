# UD2 · Aplicaciones móviles e interfaces de usuario

Lo básico que hay que saber de la UD2. Cada apartado tiene la idea clave, lo que tienes que saber y, cuando hay conceptos que se confunden, una tabla para compararlos. Los ejemplos son de la app del Centro de acogida del proyecto. Las preguntas para comprobar que lo entiendes están en los cuestionarios de autoevaluación.

---

## 1. Kotlin: lo básico

**En una frase:** Kotlin es el lenguaje de Android; es conciso y evita los errores con `null` desde el compilador.

**Lo que tienes que saber**

- **`val`** declara un valor que no cambia; **`var`**, una variable que sí cambia.
- **Null safety:** un tipo con `?` puede ser `null` (`Animal?`). Para usarlo:
  - `?.` (llamada segura): `animal?.nombre` da `null` en lugar de fallar;
  - `?:` (Elvis): `animal?.nombre ?: "Desconocido"` da un valor por si es `null`;
  - `!!` obliga a que no sea `null` y, si lo es, la app se cierra: mejor evitarlo.
- **`data class`:** una clase para guardar datos. Trae `equals` (compara los datos), `toString` y **`copy`** (una copia cambiando algún campo): `animal.copy(edad = 4)`.
- **Colecciones:** `listOf(…)` crea una lista. Con lambdas: `filter { it.urgente }` (los que cumplen), `find { it.id == 2 }` (el primero, o `null`), `map { it.nombre }` (transformar).
- **Lambda:** una función sin nombre, entre llaves; `it` es su parámetro cuando solo hay uno.

**Compara**

| | `filter` | `find` |
|---|---|---|
| Devuelve | Una lista con todos los que cumplen | El primero que cumple, o `null` |
| Tipo | `List<Animal>` | `Animal?` |

---

## 2. Jetpack Compose

**En una frase:** en Compose la interfaz se escribe con funciones (composables) que describen cómo se ve la pantalla a partir de los datos.

**Lo que tienes que saber**

- Un **composable** es una función marcada con `@Composable`. Recibe datos y avisa de los eventos con lambdas (`onClick`).
- **`Modifier`** cambia el tamaño, los márgenes o el comportamiento: `Modifier.padding(16.dp).fillMaxWidth()`. Un composable reutilizable recibe un parámetro `modifier`.
- **Disposición:** `Column` (en vertical), `Row` (en horizontal) y `Box` (superpuestos).
- **Listas:** `LazyColumn` y `LazyVerticalGrid` solo dibujan lo que se ve. Con `key = { it.id }` cada elemento se identifica por su id, que tiene que ser único.
- **Material 3:** `Scaffold` (estructura de la pantalla, con su `TopAppBar`), `Card`, `Button`, `Switch`, `OutlinedTextField`, `Checkbox`; y **`AlertDialog`** para las alertas. Los colores, tipos de letra y formas salen de **`MaterialTheme`**.
- **`@Preview`** dibuja un composable en Android Studio sin ejecutar la app.

---

## 3. El estado y la recomposición

**En una frase:** cuando cambia un estado, Compose vuelve a ejecutar los composables que lo leen y la pantalla se actualiza sola.

**Lo que tienes que saber**

- Un **estado** es un valor observado: `var soloUrgentes by remember { mutableStateOf(false) }`.
- **Recomposición:** al cambiar el estado, Compose vuelve a ejecutar las funciones que lo leen.
- **`remember`** conserva el valor entre recomposiciones, pero **se pierde al girar** el móvil. **`rememberSaveable`** lo conserva también al girar.
- Un campo de texto no guarda nada: muestra `value` y avisa con `onValueChange`; es el estado quien guarda el texto.

**Compara**

| | `remember` | `rememberSaveable` | ViewModel |
|---|---|---|---|
| ¿Sobrevive a la recomposición? | Sí | Sí | Sí |
| ¿Sobrevive al giro? | No | Sí | Sí |
| Para qué | Estado pequeño de un composable | Estado de un composable que no debe perderse | El estado de la pantalla y su lógica |

---

## 4. ViewModel, estado de la UI y flujo unidireccional

**En una frase:** el ViewModel guarda el estado de la pantalla y su lógica, y sobrevive al giro; la pantalla solo muestra el estado y avisa de los eventos.

**Lo que tienes que saber**

- El **estado de la UI** se agrupa en una `data class`, como `AcogidaUiState` (los animales, el filtro, los visibles).
- El ViewModel lo guarda en un **`MutableStateFlow` privado** y lo ofrece como **`StateFlow` público**, de solo lectura: solo el ViewModel lo cambia, con `_uiState.update { it.copy(…) }`.
- La pantalla lo lee con `collectAsState()` y se recompone cuando cambia.
- **Flujo unidireccional de datos:** el **estado baja** (del ViewModel a las pantallas) y los **eventos suben** (las pantallas llaman a funciones del ViewModel mediante lambdas).
- Las pantallas no conocen el ViewModel ni la navegación: reciben el estado y lambdas. Así se pueden previsualizar y probar por separado.

---

## 5. Navegación

**En una frase:** cada pantalla es un destino con una ruta, y el `NavController` apila y desapila pantallas.

**Lo que tienes que saber**

- El **`NavHost`** reúne los destinos; cada uno tiene una **ruta**: `"lista"`, `"detalle/{animalId}"`.
- **`navigate("detalle/2")`** abre una pantalla y la pone encima de la pila; **`popBackStack()`** vuelve a la anterior. El botón Atrás del sistema hace lo mismo.
- Los **argumentos** van en la ruta (`{animalId}`) y se leen en el destino (`getInt("animalId")`).
- Si el ViewModel se crea fuera del `NavHost`, todas las pantallas comparten el mismo.

---

## 6. Formularios y validación

**En una frase:** el formulario guarda lo que se escribe en el estado, y al enviar se comprueba que todo es correcto antes de aceptarlo.

**Lo que tienes que saber**

- Cada campo lee su valor del estado y lo cambia con una función del ViewModel (`cambiarNombre`, `cambiarTelefono`…).
- El ViewModel puede **filtrar** lo que entra (en el teléfono, solo cifras y como mucho 9).
- La **validación** está en el estado: cada campo tiene su error (o `null`) y `esValido` dice si se puede enviar.
- Los errores se muestran **después** de intentar enviar, junto al campo (`isError`, `supportingText`), con mensajes claros.
- El teclado se adapta al dato con `KeyboardOptions` (correo, teléfono).

---

## 7. Probar en el emulador y documentar

**En una frase:** la app se prueba en el emulador como la usaría una persona, en varios dispositivos, y el proceso se documenta.

**Lo que tienes que saber**

- Se prueban las tareas reales: navegar, rellenar el formulario con datos buenos y malos, girar el móvil, volver atrás.
- Se prueba en **varios tamaños** (móvil y tableta) y con el teclado en pantalla.
- **Logcat** muestra los errores; un `FATAL EXCEPTION` es que la app se ha cerrado, y su primera línea dice por qué.
- **Documentar** el desarrollo: qué hace cada archivo, cómo se ejecuta y qué se ha probado.
