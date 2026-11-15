# UD1 · Desarrollo multiplataforma: Flutter y otras opciones

Amplía el apartado 2 de los [apuntes de la UD1 de la semana del 2 al 6](../../2026_11_02-06/apuntes/UD1-tecnologias-y-entornos.md) (las tecnologías de desarrollo). Se estudia solo aquí: en el examen, las preguntas son de conceptos y criterio, no de código. Las preguntas para comprobar que lo entiendes están en el [cuestionario](../cuestionario.md) de la semana.

---

## 1. Nativo o multiplataforma

**En una frase:** una app **nativa** se escribe para un sistema (Android con Kotlin, iOS con Swift); una **multiplataforma** se escribe una vez y funciona en varios.

**Lo que tienes que saber**

- **Nativo:** acceso directo a todo lo del sistema (sensores, cámara, notificaciones) en cuanto sale, el mejor rendimiento y el aspecto propio de cada sistema. Pero dos sistemas son dos apps: dos lenguajes, dos equipos, el doble de mantenimiento.
- **Multiplataforma:** un solo código (o casi) para Android, iOS y a veces web y escritorio. Menos coste; a cambio, lo más nuevo o específico de cada sistema llega más tarde o hay que programarlo aparte.
- **Las opciones más usadas:**
  - **Flutter** (Google): lenguaje **Dart**; la interfaz se construye con **widgets** y Flutter **la dibuja él mismo**, igual en todos los sistemas;
  - **Kotlin Multiplatform** (JetBrains): se comparte la **lógica** (datos, red, reglas) en Kotlin y cada sistema puede tener su interfaz nativa, o compartirla también con **Compose Multiplatform**;
  - **React Native** (Meta): JavaScript o TypeScript con React; usa los controles nativos de cada sistema.

---

## 2. Flutter

**En una frase:** en Flutter todo es un **widget**, y la interfaz se describe como una función del estado, igual que en Compose.

**Lo que tienes que saber**

- **Widgets:** cada elemento (texto, botón, fila, columna, la pantalla entera) es un widget, y se anidan unos dentro de otros. `Text`, `ElevatedButton`, `Row`, `Column`, `ListView`, `Scaffold`.
- **Sin estado y con estado:** un `StatelessWidget` solo muestra lo que recibe; un `StatefulWidget` guarda datos que cambian y, al cambiarlos con **`setState`**, se vuelve a dibujar. Es la misma idea que el estado y la recomposición de Compose.
- **Hot reload:** al guardar el código, el cambio aparece en la app en marcha en un segundo, sin perder el estado.
- **Paquetes:** las librerías se añaden en `pubspec.yaml`, desde **pub.dev** (por ejemplo, `http` para Internet, `camera`, `geolocator`).
- **Para probarlo sin instalar nada:** **DartPad** (dartpad.dev) ejecuta Dart y Flutter en el navegador.

**Compara**

| | Jetpack Compose | Flutter |
|---|---|---|
| Lenguaje | Kotlin | Dart |
| Sistemas | Android (y otros con Compose Multiplatform) | Android, iOS, web y escritorio |
| La interfaz | Funciones `@Composable` | Widgets |
| Estado | `remember`, `mutableStateOf`, ViewModel | `StatefulWidget` y `setState` (o gestores como Provider) |
| Quién dibuja | Android | El motor de Flutter |

---

## 3. Cuándo elegir cada una

| Situación | Lo razonable |
|---|---|
| Solo Android, y se necesita lo último del sistema o el mejor rendimiento | Nativo (Kotlin y Compose) |
| Android e iOS con un equipo pequeño y una interfaz igual en los dos | Flutter |
| Ya hay una app Android en Kotlin y se quiere llevar a iOS sin rehacerla | Kotlin Multiplatform |
| El equipo sabe web (JavaScript y React) | React Native |
