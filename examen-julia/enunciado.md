# PROGRAMACIÓN MULTIMEDIA Y DISPOSITIVOS MÓVILES

---

## CUESTIONARIO

*Cada respuesta acertada vale 0.25, cada error resta 0.1.*

### 1. Una aplicación utiliza continuamente GPS, cámara y conexión de datos. ¿Qué problema propio de los dispositivos móviles debería tenerse especialmente en cuenta?

a) Estas funciones solo pueden utilizarse desde un emulador.\
b) El uso continuado puede aumentar el consumo de batería y recursos del dispositivo.\
c) Android impide utilizar cámara y GPS simultáneamente.\
d) Las aplicaciones Android disponen siempre de la misma cantidad de memoria.

### 2. Observa la configuración de una aplicación:

```kotlin
defaultConfig {
    minSdk = 24
    targetSdk = 36
}
```

Se prueba en tres dispositivos:

- Dispositivo A: API 23
- Dispositivo B: API 28
- Dispositivo C: API 36

¿Cuál de las siguientes afirmaciones es correcta?

a) Solo podrá instalarse en el dispositivo C, porque *targetSdk* = 36.\
b) Podrá instalarse en los tres dispositivos, pero en A Android desactivará las funciones no compatibles.\
c) Podrá instalarse en B y C; *minSdk* establece la API mínima admitida y *targetSdk* indica la versión de Android para la que la aplicación declara estar adaptada en cuanto a comportamiento.\
d) Podrá instalarse en B y C únicamente si *minSdk* y *targetSdk* tienen el mismo valor.

### 3. Tienes que realizar los siguientes cambios en una aplicación Android:

- añade una nueva imagen llamada logo.png;
- modifica el texto que aparece como nombre de la aplicación;
- declara una nueva Activity;
- cambia *versionName*.

¿Cuál de las siguientes asociaciones es correcta?

a) res/drawable — strings.xml — AndroidManifest.xml — build.gradle.kts\
b) res/mipmap — MainActivity.kt — strings.xml — AndroidManifest.xml\
c) res/drawable — AndroidManifest.xml — MainActivity.kt — strings.xml\
d) res/values — strings.xml — build.gradle.kts — AndroidManifest.xml

### 4. ¿Por qué *mostrarSaludo()* provoca un problema?

```kotlin
fun mostrarSaludo() {
    Mensaje("Ana")
}

@Composable
fun Mensaje(nombre: String) {
    Text("Hola $nombre")
}
```

a) Porque *Mensaje()* recibe un parámetro.\
b) Porque *Mensaje()* debería devolver un String.\
c) Porque *Text()* solo puede utilizarse dentro de *MainActivity*.\
d) Porque una función *@Composable* debe llamarse desde un contexto composable.

### 5. Observa el siguiente código:

```kotlin
Column(
    modifier = Modifier.padding(16.dp)
) {
    Text("Título")

    Row {
        Text("A")
        Text("B")
    }
}
```

¿Qué afirmación describe mejor el efecto del `Modifier.padding(16.dp)`?

a) Añade 16 dp de separación únicamente entre "A" y "B".\
b) Aplica el espacio solo al Text("Título").\
c) Añade espacio alrededor del contenido completo de la Column.\
d) Hace que la Column ocupe obligatoriamente toda la pantalla.

### 6. *R.drawable.logo* representa:

```kotlin
Image(
    painter = painterResource(R.drawable.logo),
    contentDescription = "Logo"
)
```

a) El nombre de una variable que contiene una imagen.\
b) Una imagen que Android descarga automáticamente.\
c) Una ruta absoluta dentro del ordenador.\
d) El identificador generado para un recurso de la aplicación.

### 7. ¿Qué efecto produce este Modifier sobre el botón?

```kotlin
Button(
    onClick = { },
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
) {
    Text("Aceptar")
}
```

a) El botón ocupa todo el alto disponible y el texto se separa 16 dp de sus bordes.\
b) El botón mide exactamente 16 dp de ancho y ocupa todo el alto disponible.\
c) El botón se centra automáticamente y el texto aumenta su tamaño.\
d) El botón intenta ocupar todo el ancho disponible y se aplica un espacio de 16 dp alrededor mediante padding.

### 8. ¿Qué valor tendrá *texto*?

```kotlin
val nombre: String? = "Ana"
val texto = nombre?.uppercase() ?: "SIN NOMBRE"
```

a) "Ana"\
b) "SIN NOMBRE"\
c) "ANA"\
d) Se produce una excepción.

### 9. ¿Cuál de las siguientes expresiones puede evaluarse sin provocar una excepción por el valor *null*?

```kotlin
val nombre: String? = null
```

a) `nombre!!.length`\
b) `nombre.length`\
c) `nombre?.length`\
d) `nombre.uppercase()`

### 10. ¿Qué valor tendrá mensaje?

```kotlin
val mensaje = when {
    puntos >= 5 -> "Nivel basico"
    puntos >= 8 -> "Nivel medio"
    else -> "Nivel bajo"
}
```

a) "Nivel alto"\
b) "Nivel medio"\
c) "Nivel basico"\
d) "Nivel bajo"

### 11. ¿Cuál de las siguientes afirmaciones es correcta?

```kotlin
val operacion: (Int) -> Int = { numero ->
    numero * 2
}

val resultado = operacion(4)
```

a) operacion almacena el resultado de multiplicar un número por 2.\
b) operacion almacena una función que recibe un Int y devuelve un Int.\
c) `{ numero -> numero * 2 }` se ejecuta únicamente al declarar la variable.\
d) *resultado* contiene la propia función almacenada en *operacion*.

### 12. ¿Cuál de las siguientes afirmaciones es correcta?

```kotlin
fun ejecutarAccion(
    valor: Int,
    accion: (Int) -> Unit
) {
    accion(valor)
}

var resultado = 10

ejecutarAccion(5) { numero ->
    resultado += numero
}
```

a) accion contiene un número entero y resultado termina valiendo 10.\
b) La lambda se ejecuta antes de llamar a *ejecutarAccion()* y devuelve 15.\
c) accion es una función que recibe un Int y no devuelve ningún valor; al ejecutarse con 5, resultado pasa a valer 15.\
d) (Int) -> Unit significa que *ejecutarAccion()* recibe un Int y devuelve un objeto de tipo Unit.

### 13. ¿Cuál de las siguientes afirmaciones es correcta?

```kotlin
var nombre by remember {
    mutableStateOf("")
}
```

a) *remember* crea el estado observable y *mutableStateOf* evita que se pierda durante las recomposiciones.\
b) *mutableStateOf* crea un estado observable y *remember* permite conservarlo durante las recomposiciones.\
c) *remember* y *mutableStateOf* realizan exactamente la misma función.\
d) *mutableStateOf* guarda el valor aunque se cierre y vuelva a abrir la aplicación.

### 14. Inicialmente redondear vale false. El usuario activa el *Switch*. ¿Qué sucede?

```kotlin
var redondear by remember {
    mutableStateOf(false)
}

Switch(
    checked = redondear,
    onCheckedChange = { redondear = it }
)

Text(
    if (redondear) "Resultado redondeado"
    else "Resultado exacto"
)
```

a) it recibe el valor anterior de redondear, por lo que sigue siendo false.\
b) Solo cambia visualmente el Switch; la variable redondear no se modifica.\
c) redondear pasa a true y el Text puede actualizarse mostrando "Resultado redondeado".\
d) Se produce un error porque un Text y un Switch no pueden utilizar el mismo estado.

### 15. ¿Cuál de las siguientes afirmaciones describe correctamente el código?

```kotlin
@Composable
fun Pantalla() {
    var cantidad by remember {
        mutableStateOf(1)
    }

    SelectorCantidad(
        cantidad = cantidad,
        onCantidadChange = { cantidad = it }
    )
}

@Composable
fun SelectorCantidad(
    cantidad: Int,
    onCantidadChange: (Int) -> Unit
) {
    Button(
        onClick = {
            onCantidadChange(cantidad + 1)
        }
    ) {
        Text("$cantidad")
    }
}
```

a) El estado de cantidad se mantiene en Pantalla, mientras que SelectorCantidad recibe el valor y una función para comunicar los cambios.\
b) SelectorCantidad crea automáticamente su propio estado porque recibe un parámetro cantidad.\
c) onCantidadChange contiene un número entero que se incrementa al pulsar el botón.\
d) El parámetro cantidad de SelectorCantidad se modifica directamente mediante cantidad + 1.

### 16. ¿Cuál de estas afirmaciones explica correctamente por qué puede cambiar la imagen aunque imagen esté declarada con val?

```kotlin
var resultado by remember {
    mutableStateOf(1)
}

val imagen = when (resultado) {
    1 -> R.drawable.dado_1
    2 -> R.drawable.dado_2
    3 -> R.drawable.dado_3
    else -> R.drawable.dado_4
}

Button(
    onClick = { resultado = 3 }
) {
    Text("Cambiar")
}

Image(
    painter = painterResource(imagen),
    contentDescription = null
)
```

a) Porque val permite modificar su valor durante la ejecución si está dentro de un *composable*.\
b) Porque *painterResource()* cambia automáticamente imagen al pulsar el botón.\
c) Porque al cambiar resultado, Compose puede recomponer el composable y volver a ejecutar la expresión que calcula imagen.\
d) Porque *mutableStateOf* convierte automáticamente todas las variables del composable en mutables.

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

b) Explica qué hace: `nombre ?: "Invitado"`

c) ¿Qué devuelve exactamente esta llamada? `obtenerNombre(null)`

d) ¿Qué devuelve exactamente esta llamada? `obtenerNombre("Laura")`

e) Modifica únicamente la expresión necesaria para que, cuando nombre sea null, se utilice "Desconocido" en lugar de "Invitado".

f) ¿Qué ocurriría si sustituyésemos `val usuario = nombre ?: "Invitado"` por `val usuario = nombre!!` y llamásemos a `obtenerNombre(null)`?

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

b) ¿Qué valor tendrá resultado1? Explica brevemente cómo se obtiene.

c) ¿Qué representa `it` en la segunda llamada?

d) ¿Qué valor tendrá finalmente resultado2?

e) Explica en qué momento se ejecuta la lambda que se pasa a aplicarCambio.

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

b) Explica por qué, al modificar contador, puede actualizarse la interfaz.

c) mensaje está declarado con val. ¿Por qué puede mostrarse un mensaje diferente después de cambiar contador si una variable val no puede modificarse?

d) ¿Qué problema tendría este código si sustituyésemos la declaración de contador por:

```kotlin
var contador = 0
```

Explica qué ocurriría con la actualización de la interfaz y con el valor de contador durante una recomposición.

### Ejercicio 4 (1.5 puntos)

```kotlin
var numero by ______________________

val imagen = when (numero) {
    1 -> ______________________
    2 -> ______________________
    else -> ______________________
}

Image(
    painter = ______________________
    contentDescription = null
)

Button(
    onClick = {
        numero = ______________________
    }
) {
    Text("Cambiar imagen")
}
```

Completa el siguiente código para que:

- numero sea un estado inicializado a 1;
- imagen seleccione el recurso correspondiente mediante when;
- Image muestre el recurso calculado;
- al pulsar el botón se genere aleatoriamente un número entre 1 y 3.
