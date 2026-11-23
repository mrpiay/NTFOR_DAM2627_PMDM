# App Android - ValoraPMDM

## Introducción

En esta guía aprenderás a diseñar y desarrollar **ValoraPMDM**, una aplicación Android que permite consultar las valoraciones anónimas del módulo de PMDM (Programación Multimedia y Dispositivos Móviles) en sus distintas evaluaciones.

La aplicación muestra la nota media, su representación en estrellas, el número total de valoraciones y la distribución de las puntuaciones recibidas. Los datos se obtienen de archivos JSON publicados en GitHub y se almacenan en una base de datos local, de manera que pueden consultarse incluso cuando el dispositivo no tiene conexión a Internet.

El objetivo de esta guía no es únicamente conseguir que la aplicación funcione, sino también comprender cómo se construye una aplicación Android actual y cómo se comunican sus diferentes componentes. Para ello, se usa **Android Studio**, el entorno de desarrollo oficial de Android, donde se escribe el código, se compila y se prueba la aplicación en un emulador o en un móvil. Además, se usan **Kotlin y Jetpack Compose** para crear la interfaz, **ViewModel y corrutinas** para gestionar el estado y las operaciones en segundo plano, **Retrofit** para obtener los datos de Internet y **Room** para almacenarlos localmente.

El desarrollo se divide en dos partes:

- **Parte 1. Diseño y funcionamiento:** qué hace la aplicación, cómo se organizan los datos y qué arquitectura sigue, antes de empezar a programar.
- **Parte 2. Implementación paso a paso:** se crea el proyecto desde cero, se configuran las herramientas y librerías, se escribe el código de cada archivo, se prueba la aplicación y se genera el APK para instalarla en un dispositivo Android.

No es necesario tener experiencia previa con Android Studio. La guía explica desde la preparación del entorno hasta la obtención de la aplicación terminada, incluyendo comprobaciones y ejemplos para facilitar el seguimiento.

Al final, un paso más **anima las barras** del recuento con Compose, para que se vea qué cambia de una evaluación a otra.

Al finalizar, tendrás una aplicación funcional y una visión general de cómo se integran la interfaz, la lógica de la aplicación, el acceso a Internet y el almacenamiento local en un proyecto Android.

## Índice

- [Parte 1 · Diseño](#parte-1--diseño)
  - [1. Qué hace la app](#1-qué-hace-la-app)
  - [2. De dónde salen los datos](#2-de-dónde-salen-los-datos)
  - [3. Cómo se organiza el código](#3-cómo-se-organiza-el-código)
  - [4. Cómo se mueve la información](#4-cómo-se-mueve-la-información)
  - [5. Decisiones de diseño](#5-decisiones-de-diseño)
  - [6. Configuración de librerías y dependencias](#6-configuración-de-librerías-y-dependencias)
- [Parte 2 · Implementación](#parte-2--implementación)
  - [Paso 0 · Preparar Android Studio](#paso-0--preparar-android-studio)
  - [Paso 1 · Crear el proyecto](#paso-1--crear-el-proyecto)
  - [Paso 2 · Las librerías](#paso-2--las-librerías)
  - [Paso 3 · El permiso de Internet](#paso-3--el-permiso-de-internet)
  - [Paso 4 · El modelo de datos](#paso-4--el-modelo-de-datos)
  - [Paso 5 · La base de datos con Room](#paso-5--la-base-de-datos-con-room)
  - [Paso 6 · Internet con Retrofit](#paso-6--internet-con-retrofit)
  - [Paso 7 · El estado de la pantalla](#paso-7--el-estado-de-la-pantalla)
  - [Paso 8 · El ViewModel](#paso-8--el-viewmodel)
  - [Paso 9 · La pantalla](#paso-9--la-pantalla)
  - [Paso 10 · MainActivity](#paso-10--mainactivity)
  - [Paso 11 · Probar la app](#paso-11--probar-la-app)
  - [Paso 12 · Generar e instalar el APK](#paso-12--generar-e-instalar-el-apk)
  - [Paso 13 · Las valoraciones reales](#paso-13--las-valoraciones-reales)
  - [Paso 14 · Animar las barras](#paso-14--animar-las-barras)
  - [Paso 15 · El detalle de una nota](#paso-15--el-detalle-de-una-nota)
- [Si algo falla](#si-algo-falla)
- [Los archivos del proyecto](#los-archivos-del-proyecto)
- [El proyecto terminado](#el-proyecto-terminado)

---

# Parte 1 · Diseño

## 1. Qué hace la app

```
┌──────────────────────────────────────┐
│ Valoraciones de PMDM     [recargar]  │  ← botón de recargar
│ [ 1ª evaluación ] [ 2ª evaluación ]  │  ← una pestaña por evaluación guardada
│                                      │
│                 3.8                  │  ← media, con un decimal
│              * * * * ·               │  ← la media redondeada, en estrellas
│           13 valoraciones            │
│ ──────────────────────────────────── │
│  5 *  ██████          3              │
│  4 *  ████████████    6              │  ← cuántas veces se ha dado cada nota
│  3 *  ██████          3              │
│  2 *  ██              1              │
│  1 *                  0              │
└──────────────────────────────────────┘
```

| Nº | Requisito |
|---|---|
| R1 | Al abrir, la app muestra las valoraciones **guardadas en el móvil**. No descarga nada. |
| R2 | La primera vez, sin nada guardado, dice «No hay valoraciones guardadas» y que se pulse el botón de recargar. |
| R3 | Al pulsar **↻**, descarga las valoraciones de la 1ª, 2ª y 3ª evaluación, las que existan, y las guarda en el móvil. Mientras descarga, se ve una barra de progreso y el botón está desactivado, para no empezar dos descargas a la vez. |
| R4 | Hay una **pestaña por cada evaluación** con valoraciones guardadas. Las evaluaciones que aún no tienen valoraciones no aparecen, aunque las anteriores tampoco estén (por ejemplo, si solo hay de la 2ª). Si el archivo de una evaluación deja de existir, sus valoraciones guardadas se borran. |
| R5 | En cada pestaña: la **media** con un decimal, las **estrellas** (la media redondeada), el **número de valoraciones** y, de la nota 5 a la 1, **cuántas veces se ha dado cada una** con una barra proporcional. |
| R6 | Si la descarga falla, la app avisa del motivo y sigue con lo guardado. No se cierra. Sin conexión, el aviso es «Sin conexión: se muestran las valoraciones guardadas»; si el servidor da un error, «Error del servidor (código)…»; si el archivo no tiene el formato esperado, «Los datos descargados no tienen el formato esperado…». |
| R7 | Al girar la pantalla no se pierde nada ni se vuelve a descargar. Si en horizontal no cabe todo, se puede desplazar hacia abajo. |
| R8 | Las valoraciones son **anónimas**: solo un número para distinguirlas y la nota. |
| R9 | Las barras del recuento están **animadas**: al aparecer crecen desde 0 y, al cambiar de pestaña, pasan poco a poco de su tamaño anterior al nuevo. Si el sistema tiene las animaciones desactivadas, cambian de golpe (paso 14). |
| R10 | Al **tocar una fila del recuento**, se abre una segunda pantalla con la **lista de las valoraciones con esa nota**. Las filas de una nota que no ha puesto nadie no responden. Se vuelve con la flecha de arriba o con el botón «atrás» del móvil (paso 15). |

## 2. De dónde salen los datos

Las valoraciones se recogen de forma anónima y se publican en el repositorio del módulo, en GitHub, como archivos **JSON**: uno por evaluación.

```
Valoración anónima de cada evaluación
        │
        ▼
Repositorio NTFOR_DAM2627_PMDM, carpeta valora/
   valora1.json   ← 1ª evaluación
   valora2.json   ← 2ª evaluación
   valora3.json   ← 3ª evaluación (cuando exista)
        │  HTTP GET (Retrofit), al pulsar ↻
        ▼
ValoraPMDM ──guarda──► Room (base de datos en el móvil) ──muestra──► pantalla
```

**El formato de cada archivo.** Una lista de objetos, uno por valoración, con un número (`id`) y la nota de 1 a 5 estrellas (`nota`). El `id` no dice nada de quién valoró: solo sirve para distinguir unas valoraciones de otras.

```json
[
  { "id": 1, "nota": 4 },
  { "id": 2, "nota": 5 },
  { "id": 3, "nota": 3 }
]
```

**Las direcciones.** Los archivos se leen en su versión «en bruto» (*raw*), sin la página de GitHub alrededor. La dirección tiene una parte común, la **URL base**, y el nombre del archivo:

```
https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/  ← URL base
                                                                                      valora1.json
```

Para construir y probar la app se usan las **valoraciones de ejemplo**, en `valora/ejemplo/`: hay de la 1ª y la 2ª evaluación, 13 en cada una, con medias distintas (3.8 y 4.4). No hay archivo de la 3ª, a propósito: así se prueba que la app se salta las evaluaciones que no existen. Puedes abrir [valora1.json](https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/valora1.json) en el navegador para ver cómo es.

**Lo que puede pasar al pedir un archivo.** Cada respuesta de un servidor lleva un **código** de tres cifras que dice cómo ha ido. La app distingue estos casos:

| Qué pasa | Qué hace la app |
|---|---|
| El archivo existe (código **200**, *correcto*) | Lo guarda en el móvil, en lugar de lo que hubiera de esa evaluación |
| El archivo no existe (código **404**, *no encontrado*) | Esa evaluación no tiene valoraciones publicadas: borra las que hubiera guardadas y pasa a la siguiente |
| El servidor responde con otro error (por ejemplo, **500**, *error interno*) | Avisa «Error del servidor» y deja lo guardado |
| No hay respuesta (sin conexión) | Avisa «Sin conexión» y deja lo guardado |
| Llega el archivo, pero no es el JSON esperado | Avisa de que los datos no tienen el formato esperado y deja lo guardado |

## 3. Cómo se organiza el código

### 3.1 · Las palabras que hay que conocer

Una app Android se escribe en **Kotlin**, un lenguaje de programación. El código se guarda en archivos con la extensión **`.kt`** (de *Kotlin*), igual que un texto acaba en `.txt` o una foto en `.jpg`. Cada archivo `.kt` contiene una pieza de la app.

Dentro de esos archivos hay, sobre todo, dos cosas:

- **Clases.** Una clase es un molde que describe un tipo de cosa y qué datos tiene. Por ejemplo, la clase `Valora` dice que una valoración tiene un número de evaluación, un número para distinguirla (`id`) y una nota. Con ese molde se crean tantas valoraciones como hagan falta; cada una es un **objeto**.
- **Funciones.** Una función es una acción con nombre. Se escribe con paréntesis: `cargar()` descarga las valoraciones; `elegir(2)` elige la 2ª evaluación. Lo que va entre los paréntesis es el dato que necesita la acción.

Estas son las piezas que usa la app:

| Palabra | Qué es | En esta app |
|---|---|---|
| **JSON** | Un formato de texto para guardar datos ordenados, con llaves `{ }` y corchetes `[ ]`. Lo entienden todos los lenguajes de programación. | Los archivos `valora1.json`, `valora2.json`… con las notas |
| **Librería** | Código ya hecho, escrito por otros, que se añade al proyecto para no tener que programarlo desde cero. | Retrofit, Room y Compose son librerías |
| **Retrofit** | Una librería para pedir cosas a un servidor de Internet. | Descarga los archivos JSON y convierte su texto en objetos de Kotlin |
| **Room** | Una librería para guardar datos en una **base de datos** dentro del móvil: tablas con filas y columnas, como una hoja de cálculo. Lo guardado sigue ahí aunque se cierre la app o se apague el móvil. | Guarda las valoraciones para verlas sin conexión |
| **Entidad** | La clase que Room convierte en tabla: cada dato de la clase es una columna y cada objeto, una fila. | `Valora` |
| **DAO** | *Data Access Object* (objeto de acceso a datos): la lista de operaciones que se pueden hacer con una tabla, como leer, borrar o insertar. | `ValoraDao` |
| **Compose** | La forma de dibujar pantallas en Android escribiendo funciones de Kotlin. Cada función dibuja un trozo (un texto, una fila, una barra…) y unas se meten dentro de otras. | `PantallaValora` |
| **Estado** | Todos los datos que necesita la pantalla para dibujarse en un momento dado. La pantalla no guarda nada por su cuenta: mira el estado y lo dibuja. Cuando el estado cambia, se vuelve a dibujar sola. | Qué pestaña está elegida, si está descargando, el aviso y las valoraciones |
| **UiState** | El nombre que se suele dar a la clase que contiene el estado. *UI* son las siglas de *User Interface* (interfaz de usuario), es decir, la pantalla. | `ValoraUiState`: «el estado de la pantalla de Valora» |
| **ViewModel** | La pieza que hace de intermediaria entre la pantalla y los datos. La pantalla le avisa cuando se pulsa algo; el ViewModel hace el trabajo (descargar, guardar) y prepara el estado. Además, **sobrevive al girar el móvil**: al girarlo, Android destruye la pantalla y la vuelve a crear, pero el ViewModel sigue, y con él los datos. | `ValoraViewModel` |
| **Corrutina** | Un trabajo que se hace aparte, sin detener la app. Descargar tarda: si la app se quedara esperando, la pantalla se congelaría. Con una corrutina, la descarga avanza y la pantalla sigue respondiendo. | La descarga que hace `cargar()` |
| **Flow** y **StateFlow** | Un **Flow** es un dato que avisa: quien lo usa no lo lee una sola vez, sino que recibe cada valor nuevo cuando cambia. Un **StateFlow** es un Flow que siempre tiene un valor actual. | Room entrega las valoraciones como Flow; el ViewModel entrega el estado como StateFlow (`uiState`) |
| **Activity** | La ventana de una app Android: lo primero que abre Android al pulsar el icono. | `MainActivity`, que solo muestra `PantallaValora` |
| **Logcat** | Una ventana de Android Studio donde se ven los mensajes que escribe la app mientras funciona. | Dice cuántas valoraciones ha descargado de cada evaluación |
| **Animación** | Un valor que, en lugar de saltar, va cambiando poco a poco hasta su objetivo, y la pantalla se dibuja con cada paso. En Compose, con funciones `animate…AsState` | Las barras del recuento (paso 14) |

### 3.2 · Las capas

Una app pequeña se podría escribir en un solo archivo, pero sería difícil de leer y de cambiar. Por eso el código se reparte en **capas**, cada una con un solo trabajo:

- **Pantalla:** dibuja el estado y recoge los toques. No calcula nada ni sabe de dónde salen los datos.
- **ViewModel:** decide qué hacer cuando se pulsa algo (descargar, guardar, avisar) y entrega el estado. El estado (`ValoraUiState`) hace todos los cálculos: la media y su texto, las estrellas, las barras del recuento y qué pestaña está marcada.
- **Room y Retrofit:** guardan y traen los datos. Room, en el móvil; Retrofit, desde Internet.
- **Modelo:** la forma de los datos, es decir, qué es una valoración, y cómo se convierte la que llega en el JSON en la que se guarda. Lo usan las demás capas.

En resumen: **el modelo convierte, el estado calcula y la pantalla dibuja**. Cada capa solo habla con la de al lado. La pantalla, por ejemplo, nunca descarga ni guarda nada: se lo pide al ViewModel. Así, si un día los datos llegaran de otro sitio, la pantalla no tendría que cambiar.

**Cómo se lee el esquema.** Cada caja es una capa, con sus archivos `.kt` y lo que hace cada uno; a la derecha, el paso de la Parte 2 en el que se escribe. Las flechas dicen quién pide qué a quién. Por ejemplo, «recargar → cargar()» quiere decir que, al pulsar el botón de recargar, la pantalla llama a la función `cargar()` del ViewModel. La flecha que sube, «el estado», es la respuesta: el ViewModel entrega el estado y la pantalla lo dibuja.

El esquema se lee de arriba abajo, desde lo que se ve hasta los datos. La app se construye al revés, de abajo arriba: del paso 4 al 10.

```
┌──────────────────────────────────────────────────────────────┐
│ PANTALLA                                       pasos 9 y 10  │
│   MainActivity.kt          abre la pantalla                  │
│   PantallaValora.kt        dibuja el estado; no calcula      │
└─────────────┬──────────────────────────────────▲─────────────┘
              │ recargar → cargar()              │ el estado
              │ pestaña → elegir(n)              │ (uiState)
┌─────────────▼──────────────────────────────────┴─────────────┐
│ VIEWMODEL                                      pasos 7 y 8   │
│   ValoraUiState.kt         el estado y todos sus cálculos    │
│   ValoraViewModel.kt       descarga, guarda y avisa          │
└──────────┬──────────────────────────────────────┬────────────┘
           │ guarda y lee                         │ descarga
┌──────────▼───────────────────┐   ┌──────────────▼────────────┐
│ ROOM (en el móvil)    paso 5 │   │ RETROFIT (Internet) paso 6│
│   ValoraDao.kt               │   │   ServicioValora.kt       │
│   ValoraDatabase.kt          │   │                           │
└──────────┬───────────────────┘   └──────────────┬────────────┘
           │ usa Valora                           │ usa ValoraJson
┌──────────▼──────────────────────────────────────▼────────────┐
│ MODELO                                         paso 4        │
│   Valora.kt                ValoraJson, Valora y aValora      │
└──────────────────────────────────────────────────────────────┘
```

| Paso | Capa | Archivo | Qué contiene |
|---|---|---|---|
| 4 | Modelo | `Valora.kt` | Las dos clases de datos, `ValoraJson` (una valoración tal como llega en el JSON) y `Valora` (una fila de la tabla de Room), y `aValora`, que convierte una en la otra |
| 5 | Room | `ValoraDao.kt` | Las consultas: leer todas, sustituir las de una evaluación |
| 5 | Room | `ValoraDatabase.kt` | La base de datos, una sola para toda la app |
| 6 | Retrofit | `ServicioValora.kt` | La petición HTTP que descarga un archivo y el objeto `Red`, que la prepara |
| 7 | ViewModel | `ValoraUiState.kt` | Lo que muestra la pantalla, ya calculado: las pestañas, la media y su texto, las estrellas y las filas del recuento |
| 8 | ViewModel | `ValoraViewModel.kt` | Descarga, guarda en Room, avisa de los errores y junta todo en el estado |
| 9 | Pantalla | `PantallaValora.kt` | La pantalla con Compose: dibuja el estado, sin calcular nada |
| 10 | Pantalla | `MainActivity.kt` | El punto de entrada: muestra la pantalla |

## 4. Cómo se mueve la información

Esto es lo que pasa por dentro en cada situación, siguiendo el esquema del punto 3. Entre paréntesis va el nombre que tiene en el código.

**Al abrir la app (R1, R2)**

1. Android abre la ventana de la app (`MainActivity`) y esta muestra la pantalla (`PantallaValora`).
2. La pantalla pide su ViewModel (`ValoraViewModel`). Como es la primera vez, se crea.
3. El ViewModel pide a Room todas las valoraciones guardadas (`dao.todas()`). Room las entrega en un Flow, así que no responde solo una vez: avisará cada vez que la tabla cambie.
4. El ViewModel agrupa las valoraciones por evaluación y las pone en el estado. El estado calcula el resto: qué evaluaciones hay (las pestañas) y, de la pestaña elegida, la media, las estrellas y las filas del recuento.
5. La pantalla dibuja el estado. Si no hay nada guardado, sale el mensaje «No hay valoraciones guardadas»; si hay, las pestañas, la media y las barras.

Nada de esto usa Internet.

**Al pulsar el botón de recargar (R3, R4)**

1. La pantalla avisa al ViewModel: llama a `cargar()`.
2. El ViewModel marca en el estado que está descargando (`cargando = true`). La pantalla muestra la barra de progreso y desactiva el botón; si aun así llegara otra llamada a `cargar()`, el ViewModel no hace nada mientras dure la descarga.
3. En una corrutina, para que la pantalla no se congele, el ViewModel pide a Retrofit los tres archivos, uno detrás de otro: `valora1.json`, `valora2.json` y `valora3.json`.
4. Por cada archivo que llega, Retrofit convierte el texto JSON en una lista de objetos `ValoraJson`. El ViewModel los convierte en objetos `Valora` con `aValora`, que les añade el número de evaluación, y pide a Room que **sustituya** las valoraciones guardadas de esa evaluación por las nuevas (`sustituir`).
5. Si un archivo no existe (404), el ViewModel borra de Room las valoraciones de esa evaluación, si las había, lo escribe en Logcat y pasa al siguiente.
6. Cada vez que la tabla cambia, Room avisa por su Flow: el ViewModel vuelve a preparar el estado y la pantalla se vuelve a dibujar sola. No hace falta decirle que se redibuje.
7. Al terminar, haya ido bien o mal, el ViewModel marca que ya no está descargando (`cargando = false`) y la barra desaparece.

**Al elegir una pestaña:** la pantalla llama a `elegir(n)`, donde `n` es el número de la evaluación. El ViewModel cambia en el estado la evaluación elegida y la pantalla dibuja la media y el recuento de esa evaluación. No se descarga nada. Si la evaluación elegida deja de tener valoraciones, el estado muestra la primera que tenga (`mostrada`).

**Si la descarga falla (R6):** Kotlin lanza una **excepción**, que es su forma de avisar de un error. Hay un tipo de excepción para cada problema: `IOException` si no hay conexión, `HttpException` si el servidor responde con un error y `SerializationException` si el JSON no es como se esperaba. El ViewModel las recoge (`catch`) para que la app no se cierre, pone en el estado el aviso que corresponde y no toca Room: lo guardado sigue en pantalla.

**Al tocar una fila del recuento (R10):** la fila avisa de su nota (`onNota`) y la navegación va a la ruta `"nota/5"` (`navController.navigate`). `PantallaNota` recibe el mismo ViewModel, pide al estado las valoraciones con esa nota (`conNota`) y las muestra en una lista. Con la flecha o el botón «atrás», `popBackStack` la quita y se vuelve a la principal. No se descarga nada.

**Al cambiar las barras (R9):** cuando el estado trae otra proporción para una barra (otra pestaña, otros datos), la barra no la dibuja de golpe: `animateFloatAsState` va del valor que tenía al nuevo en 600 milisegundos, y la fila se dibuja en cada paso. El estado no sabe nada de la animación.

**Al girar el móvil (R7):** Android destruye la ventana y la vuelve a crear con la nueva orientación. La pantalla se dibuja otra vez, pero el ViewModel no se ha destruido: tiene el mismo estado, así que se ve lo mismo y no se descarga nada.

## 5. Decisiones de diseño

| Decisión | Por qué |
|---|---|
| Descargar **solo al pulsar ↻**, no al abrir | Las valoraciones cambian pocas veces (una por evaluación). Al abrir basta con lo guardado, y así la app funciona igual con o sin conexión. |
| Guardar en **Room** | Para consultarlas sin conexión y no depender de Internet cada vez (R1, R6). |
| **Dos clases**: `ValoraJson` y `Valora` | El JSON no dice de qué evaluación es cada valoración: lo dice el nombre del archivo. La tabla de Room sí necesita esa columna. Cada clase representa una cosa. |
| **Clave primaria** de dos columnas (`evaluacion`, `id`) | La valoración 1 de la 1ª evaluación y la valoración 1 de la 2ª son distintas: el `id` se repite en cada archivo. |
| **Sustituir** las valoraciones de una evaluación (borrar e insertar) | Si el archivo cambia, en el móvil queda exactamente lo que hay en el archivo, sin restos de la versión anterior. Con `@Transaction`, o se hacen las dos cosas o ninguna. |
| **Una sola función** de Retrofit con `@Path` | `valora{evaluacion}.json` sirve para los tres archivos: cambia solo el número. |
| La **media** y el **recuento** se calculan, no se guardan | Se obtienen de la lista en cada momento: no pueden quedar desactualizados. |
| El modelo **convierte**, el estado **calcula** y la pantalla **dibuja** | La conversión del JSON a la tabla está junto a las dos clases (`aValora`). La media llega a la pantalla ya escrita (`mediaTexto`) y cada barra ya calculada (`FilaNota`). Si cambia un cálculo, no se toca la pantalla; si cambia el dibujo, no se toca el cálculo. |
| Las **pestañas** salen de los datos | Cuando se publica una evaluación nueva, aparece su pestaña sin tocar el código (R4). |
| Un **404** borra la evaluación guardada | En el móvil queda lo mismo que está publicado: si un archivo se retira, sus valoraciones dejan de mostrarse (R4). |
| Un **aviso distinto** para cada error | Sin conexión, error del servidor y formato incorrecto se arreglan de forma distinta: el aviso y Logcat dicen cuál ha sido (R6). |
| Escala de **1 a 5** estrellas | Como en cualquier valoración con estrellas, la mínima es una. Por eso el recuento tiene cinco filas, una por estrella. |
| Valoraciones **anónimas** | Solo un número y la nota (R8): nada en los datos dice quién valoró. |
| Una **segunda pantalla**, con Navigation Compose | Practica la navegación de la UD2 con la misma app, y permite comparar con ValoraFlutter, que hace lo mismo con el `Navigator` de Flutter. |
| **Un solo ViewModel** para las dos pantallas | La pantalla de una nota usa las valoraciones que ya están cargadas: no descarga ni lee nada. |
| **Animar** las barras | Al cambiar de pestaña, el movimiento enseña qué notas suben y cuáles bajan entre una evaluación y otra; un salto de golpe no se ve. |
| La animación, **solo en la pantalla** | Es cómo se dibuja, no qué datos hay: el estado y el ViewModel no cambian (R9). |
| **600 ms** y con curva que frena | Lo bastante para verse, sin que la app parezca lenta. Frenar al final se ve más natural que una velocidad constante. |
| Se **respeta** «Quitar animaciones» del sistema | A algunas personas el movimiento en pantalla les marea o les distrae. Compose lo hace solo: si el sistema las desactiva, las barras cambian de golpe. |

## 6. Configuración de librerías y dependencias

Antes de programar, cada requisito del punto 1 se convierte en lo que necesita el proyecto. Quien empieza una app sabe **qué quiere ver y qué quiere que pase**, pero no qué librerías, plugins o permisos hacen falta. Esto se averigua siempre de la misma forma.

### 6.1 · El método, en cuatro preguntas

Para cada cosa que tiene que hacer la app:

1. **¿Qué necesita, en palabras técnicas?** «Que no se pierda al girar» es *guardar el estado fuera de la pantalla*; «descargar las notas» es *una petición HTTP*; «verlas sin conexión» es *una base de datos en el móvil*. La [guía de arquitectura de apps de Android](https://developer.android.com/topic/architecture) explica las necesidades habituales y qué se usa para cada una.
2. **¿Lo trae ya el proyecto, o hace falta una librería?** Lo que crea Android Studio con *Empty Activity* ya trae Kotlin, Compose con Material 3 y lo básico de Android. Para lo demás se busca la librería recomendada, primero en la documentación oficial de Android (developer.android.com) y, si no está, en la de Kotlin (kotlinlang.org).
3. **¿Qué pide esa librería?** Cada una tiene en su documentación un apartado de instalación: *Declaring dependencies*, *Setup* o *Download*. Ahí dice exactamente qué hay que añadir y la última versión estable.
4. **¿Qué tipo de pieza es cada cosa que pide?** Según el tipo, va en un archivo o en otro (6.3).

### 6.2 · Las preguntas aplicadas a esta app

| Lo que se quiere (requisito) | Qué necesita | Con qué se hace | ¿Lo trae el proyecto? | Dónde se mira qué añadir |
|---|---|---|---|---|
| Ver la pantalla con pestañas, estrellas y barras (R5) | Una interfaz con componentes de Material 3 | Compose y Material 3 | **Sí** | — |
| El icono de recargar y el de estrella (R3, R5) | Iconos de Material | `material-icons-core` | No | En la [página de Compose](https://developer.android.com/develop/ui/compose), apartado de iconos. Sin versión: la pone el BOM de Compose |
| Que no se pierda nada al girar (R7) | Guardar el estado fuera de la pantalla | ViewModel (`lifecycle-viewmodel-compose`) | No | [Lifecycle: Declaring dependencies](https://developer.android.com/jetpack/androidx/releases/lifecycle) |
| Descargar sin congelar la pantalla (R3) | Trabajo en segundo plano | Corrutinas | **Sí**: llegan con Lifecycle y Room | — |
| Descargar los archivos de Internet (R3) | Peticiones HTTP | Retrofit | No | [Retrofit en GitHub](https://github.com/square/retrofit), apartado *Download* |
| … y que Android lo permita | Acceso a Internet | Permiso `INTERNET` | No | [Conectarse a la red](https://developer.android.com/develop/connectivity/network-ops/connecting) |
| Pasar el texto JSON a objetos de Kotlin (R3) | Conversión de JSON | kotlinx.serialization y el conversor de Retrofit | No | [kotlinx.serialization: Setup](https://github.com/Kotlin/kotlinx.serialization) y [Retrofit en GitHub](https://github.com/square/retrofit), apartado de conversores |
| Verlas sin conexión (R1, R6) | Una base de datos en el móvil | Room | No | [Room: Declaring dependencies](https://developer.android.com/jetpack/androidx/releases/room) |
| … y que Room escriba el código de las consultas | Generar código al compilar | KSP | No | Lo pide la misma página de Room; versiones en [KSP: releases](https://github.com/google/ksp/releases) |

Con esta tabla hecha, ya se sabe todo lo que hay que añadir en el paso 2, y por qué.

### 6.3 · Qué tipo de pieza es y dónde va

Lo que pide cada librería es siempre de uno de estos cuatro tipos:

| Si la librería… | Es un… | Dónde va | Ejemplo en esta app |
|---|---|---|---|
| tiene clases y funciones que usa tu código | **dependencia** (`implementation`) | `build.gradle.kts (Module :app)`, en `dependencies` | Retrofit, Room (`room-runtime`, `room-ktx`), ViewModel |
| genera código a partir de anotaciones, al compilar | **procesador** (`ksp`) y su **plugin** | `ksp(…)` en `dependencies`, y el plugin KSP en los dos `build.gradle.kts` | `room-compiler`, que escribe el código de `@Dao` y `@Database` |
| cambia cómo se compila Kotlin | **plugin** | En los dos `build.gradle.kts`, en `plugins` | `kotlin-serialization`, que hace funcionar `@Serializable` |
| usa algo del móvil (Internet, cámara, ubicación…) | **permiso** | `AndroidManifest.xml` | `INTERNET` |

Y **todas las versiones**, de dependencias y de plugins, se apuntan una sola vez en el catálogo `libs.versions.toml`. Los demás archivos las nombran por su alias (`libs.retrofit`, `libs.plugins.ksp`).

### 6.4 · Qué versión poner

- **La última estable** que diga la página de la librería. Las versiones *alpha*, *beta* o *rc* son de prueba: no se usan en una app que tiene que funcionar.
- **Hay versiones que van juntas**, y no se eligen por separado:
  - el **BOM de Compose** (`composeBom`) fija la versión de todas las librerías de Compose, también la de los iconos: por eso no llevan versión propia;
  - los **plugins de Kotlin** (`kotlin-compose` y `kotlin-serialization`) llevan la misma versión que Kotlin;
  - las librerías de una misma familia comparten versión: las tres de Room, las dos de Retrofit;
  - el **plugin de Android** (AGP) necesita un Android Studio igual de nuevo o más.
- **Android Studio ayuda.** En `libs.versions.toml`, una versión que tiene otra más nueva sale marcada en amarillo, y con **Alt + Intro** propone cambiarla. *File > Project Structure > Suggestions* las muestra todas.
- **Se comprueba sincronizando** (*Sync Now*) y compilando. Si falta algo, el error lo dice:

| Error | Lo que falta |
|---|---|
| `Unresolved reference` en un `import` o en una clase de la librería | La **dependencia** (`implementation`) o el *Sync Now* |
| `Serializer for class '…' is not found` | El **plugin** `kotlin-serialization` |
| `Cannot find implementation for … ValoraDatabase` | El **procesador** `ksp(room-compiler)` o el plugin KSP |
| `SecurityException` o `Permission denied` al usar Internet | El **permiso** en el manifiesto |
| «This version of the Android Gradle plugin requires a newer version of Android Studio» | Actualizar Android Studio (*Help > Check for Updates*) o usar una versión de AGP más antigua |

---

# Parte 2 · Implementación

Cada paso dice **qué se hace**, trae el **código completo** del archivo y termina con **Comprueba que…**: no pases al siguiente hasta que se cumpla.

## Paso 0 · Preparar Android Studio

### 0.1 · Instalar Android Studio

Esta guía está probada con **Android Studio 2026.2.2**, en Windows 11, y con las versiones de librerías del paso 2. En otras versiones de Android Studio, algunos menús y botones pueden tener otro nombre o estar en otro sitio.

Si ya está instalado en tu ordenador, pasa al 0.2.

1. Descárgalo de [developer.android.com/studio](https://developer.android.com/studio) (botón **Download Android Studio**).
2. Ejecuta el instalador y deja todas las opciones como vienen (**Next** hasta **Finish**).
3. Al abrirlo por primera vez sale el asistente de configuración (*Setup Wizard*): elige **Standard**, acepta las licencias (*Accept* en cada una) y pulsa **Finish**. Descarga el SDK de Android: tarda unos minutos.

### 0.2 · Las partes de la ventana

Con un proyecto abierto, la ventana de Android Studio tiene estas zonas. Todas se abren también desde *View > Tool Windows*.

| Zona | Dónde | Para qué |
|---|---|---|
| **Project** | Panel de la izquierda | Los archivos del proyecto. Arriba del panel, la vista **Android** agrupa lo importante. |
| **Editor** | En el centro | Donde se escribe el código. Cada archivo abierto es una pestaña. |
| **Barra de ejecución** | Arriba a la derecha | El dispositivo (por ejemplo, **Pixel 9**), la configuración **app**, ▶ (*Run*) y el bichito (*Debug*). |
| **Running Devices** | Panel de la derecha | La pantalla del emulador, dentro de Android Studio. |
| **Logcat** | Abajo | Los mensajes que escribe la app y los errores. |
| **Terminal** | Abajo | Una línea de comandos en la carpeta del proyecto. |
| **Device Manager** | Barra de la derecha (icono de un móvil) | Los emuladores: crearlos, encenderlos, apagarlos. |

### 0.3 · Crear el emulador

El **emulador** es un móvil Android que funciona dentro del ordenador.

1. Abre el **Device Manager** (*View > Tool Windows > Device Manager*).
2. Pulsa **+** → **Create Virtual Device**.
3. Elige **Pixel 9** (categoría *Phone*) y pulsa **Next**.
4. En *System Image*, elige la versión más reciente de Android que aparezca (si tiene un icono de descarga ⤓, púlsalo y espera) y pulsa **Finish**.
5. En la lista del Device Manager, pulsa ▶ junto al Pixel 9. La primera vez tarda uno o dos minutos en arrancar.

**Comprueba que…** el emulador muestra la pantalla de inicio de Android, en el panel *Running Devices*.

## Paso 1 · Crear el proyecto

1. En la pantalla de bienvenida, **New Project** (o, con otro proyecto abierto, *File > New > New Project*).
2. A la izquierda, **Phone and Tablet**; en el centro, **Empty Activity** (la del logo de Compose). **Next**.
3. Rellena exactamente así:

   | Campo | Valor |
   |---|---|
   | Name | `ValoraPMDM` |
   | Package name | `com.example.valorapmdm` |
   | Save location | `C:\dam2627\pmdm\ValoraPMDM` |
   | Minimum SDK | **API 24 ("Nougat"; Android 7.0)** |
   | Build configuration language | **Kotlin DSL (build.gradle.kts)** |

   El **Minimum SDK** es la versión de Android más antigua en la que se puede instalar la app. Android Studio la muestra con su nivel de API y su versión de Android, y debajo dice en qué porcentaje de móviles funcionará. Con API 24, la app funciona en Android 7.0 y en todas las versiones posteriores; por ejemplo, en Android 8 (API 26). El enlace **Help me choose** muestra la tabla completa de versiones.

   El **Package name** (nombre del paquete) es el identificador de la app: Android lo usa para distinguirla de todas las demás, así que no puede haber dos apps instaladas con el mismo. Se escribe en minúsculas, con palabras separadas por puntos, y suele ser una dirección web al revés seguida del nombre de la app: una empresa con la web `miempresa.com` usaría `com.miempresa.nombredelaapp`. `com.example` es la parte que se usa cuando la app no es de ninguna web, como en las pruebas y las prácticas. Android Studio lo usa también en dos sitios más:
   - Es la **carpeta** donde van los archivos `.kt`: en el panel *Project*, `app > kotlin+java > com.example.valorapmdm`.
   - Es la **primera línea** de cada archivo de Kotlin: `package com.example.valorapmdm`. Dice a qué paquete pertenece el archivo y tiene que coincidir con la carpeta. Android Studio la escribe solo al crear el archivo; el código de la guía también la trae.

4. **Finish**. Android Studio crea el proyecto y lo prepara (*Gradle sync*, en la barra de abajo). La primera vez descarga librerías y tarda unos minutos: espera a que la barra de abajo termine.
5. Arriba a la derecha, elige el **Pixel 9** y pulsa ▶ (*Run*).

**Comprueba que…** en el emulador se abre una app que dice «Hello Android!».

**El árbol de archivos.** El panel *Project*, a la izquierda, muestra los archivos del proyecto. Arriba del panel hay un desplegable: elige **Android**. Esta vista agrupa los archivos por tipo y esconde los que no hacen falta. Pulsa la flecha ▸ de cada carpeta para abrirla.

Así queda el proyecto al terminar la guía. Lo marcado como **NUEVO** lo creas tú en el paso que se indica; lo demás lo ha creado Android Studio. Android Studio ordena los archivos por orden alfabético, no por el orden de los pasos.

```
app
├── manifests
│   └── AndroidManifest.xml                   ya existe · se cambia en el paso 3
├── kotlin+java
│   ├── com.example.valorapmdm                aquí se crean los archivos de Kotlin
│   │   ├── ui.theme                          ya existe · no se toca
│   │   │   ├── Color.kt
│   │   │   ├── Theme.kt
│   │   │   └── Type.kt
│   │   ├── MainActivity.kt                   ya existe · se cambia en el paso 10
│   │   ├── PantallaValora.kt                 NUEVO · paso 9
│   │   ├── ServicioValora.kt                 NUEVO · paso 6
│   │   ├── Valora.kt                         NUEVO · paso 4
│   │   ├── ValoraDao.kt                      NUEVO · paso 5
│   │   ├── ValoraDatabase.kt                 NUEVO · paso 5
│   │   ├── ValoraUiState.kt                  NUEVO · paso 7
│   │   └── ValoraViewModel.kt                NUEVO · paso 8
│   ├── com.example.valorapmdm (androidTest)  pruebas automáticas · no se toca
│   └── com.example.valorapmdm (test)         pruebas automáticas · no se toca
└── res                                       iconos, colores y textos · no se toca
Gradle Scripts
├── build.gradle.kts (Project: ValoraPMDM)    se cambia en el paso 2
├── build.gradle.kts (Module :app)            se cambia en el paso 2
├── proguard-rules.pro                        no se toca
├── gradle.properties                         no se toca
├── gradle-daemon-jvm.properties              no se toca
├── gradle-wrapper.properties                 no se toca
├── libs.versions.toml                        se cambia en el paso 2
├── local.properties                          no se toca
└── settings.gradle.kts                       no se toca
```

`com.example.valorapmdm` sale tres veces dentro de `kotlin+java`. Los archivos se crean **siempre en la primera**, la que no lleva nada entre paréntesis. Las otras dos, `(androidTest)` y `(test)`, son para pruebas automáticas y en esta guía no se usan.

**Cómo se crea un archivo de Kotlin** (pasos 4 a 9):

1. En el panel *Project*, haz clic derecho sobre `com.example.valorapmdm`: la primera, sin paréntesis. No sobre `ui.theme`.
2. *New > Kotlin Class/File*.
3. En la ventana que se abre, elige **File** en la lista y escribe el nombre **sin** `.kt` (por ejemplo, `Valora`). Pulsa **Intro**.
4. El archivo aparece en el árbol, dentro de `com.example.valorapmdm`, y se abre a la derecha, vacío o solo con la línea `package com.example.valorapmdm`. Sustituye todo su contenido por el código de la guía.

Si lo creas en otra carpeta por error, arrástralo en el árbol hasta `com.example.valorapmdm` y, en la ventana que sale, pulsa **Refactor**.

## Paso 2 · Las librerías

**Antes de empezar: qué es Gradle.** Android Studio no convierte el código en una app por sí solo: lo hace **Gradle**, la herramienta de compilación. Gradle lee unos archivos de configuración (los de *Gradle Scripts*, en el panel *Project*), descarga las librerías que se le piden y compila el proyecto. En esos archivos se declaran dos tipos de cosas:

| Qué | Qué es | Ejemplo |
|---|---|---|
| **Dependencia** | Una librería que usa el código de la app. Va dentro del APK. | Room, Retrofit |
| **Plugin** | Una herramienta que usa Gradle para compilar. No va dentro del APK. | KSP, que genera el código de Room; el de kotlinx.serialization |
| **Catálogo de versiones** | El archivo `libs.versions.toml`: la lista de todas las librerías y plugins, con su versión, en un solo sitio. Los otros archivos los nombran por su alias (`libs.room.runtime`). | |

Cada vez que se cambia uno de estos archivos, hay que **sincronizar** (*Sync Now*): Gradle vuelve a leerlos y descarga lo nuevo. Hasta entonces, Android Studio no conoce las librerías y marca en rojo todo lo que las usa.

La app necesita librerías que el proyecto vacío no trae. Cómo se averigua cuáles, y qué tipo de pieza es cada una, está en el punto 6 de la Parte 1:

| Librería | Para qué |
|---|---|
| **Room** (`room-runtime`, `room-ktx`, `room-compiler`) | La base de datos en el móvil |
| **KSP** | Genera, al compilar, el código de las consultas de Room |
| **Retrofit** y su conversor | Las peticiones HTTP |
| **kotlinx.serialization** | Convertir el JSON en objetos de Kotlin |
| **material-icons-core** | Los iconos ↻ y ★ |
| **lifecycle-viewmodel-compose** | Usar el ViewModel desde Compose |

Las librerías se declaran en tres archivos de *Gradle Scripts*. Sustituye el contenido **completo** de cada uno.

**2.1 · `libs.versions.toml`**: el catálogo con el nombre y la versión de cada librería.

```toml
[versions]
agp = "9.5.0"
coreKtx = "1.19.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
lifecycleRuntimeKtx = "2.11.0"
activityCompose = "1.13.0"
kotlin = "2.2.10"
composeBom = "2026.02.01"
# Room, KSP, Retrofit y kotlinx.serialization
room = "2.8.4"
ksp = "2.3.4"
retrofit = "3.0.0"
kotlinxSerialization = "1.9.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
# Room
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
# Retrofit y kotlinx.serialization
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-converter-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

**2.2 · `build.gradle.kts (Project: ValoraPMDM)`**: los *plugins* (herramientas de compilación) que puede usar el proyecto.

```kotlin
// Archivo de compilación del proyecto: los plugins que pueden usar los módulos
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false                  // KSP, para Room
    alias(libs.plugins.kotlin.serialization) apply false // para convertir JSON
}
```

**2.3 · `build.gradle.kts (Module :app)`**: cómo se compila la app y qué librerías usa.

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.valorapmdm"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.valorapmdm"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose y lo básico de Android
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Room (persistencia). KSP genera el código de las consultas al compilar
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    // Retrofit (HTTP) y kotlinx.serialization (convierte el JSON en objetos de Kotlin)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    // Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
```

Al cambiar estos archivos, arriba del editor sale una barra amarilla: pulsa **Sync Now** y espera a que termine (barra de abajo).

**Comprueba que…** el *sync* termina sin errores (en la pestaña **Build**, abajo, sale «BUILD SUCCESSFUL» o ningún mensaje en rojo) y ▶ sigue abriendo «Hello Android!».

## Paso 3 · El permiso de Internet

La app descarga archivos de Internet, y Android exige declararlo. **Internet** es un permiso **normal**: basta con declararlo en el manifiesto y se concede al instalar, sin preguntar.

Abre `app > manifests > AndroidManifest.xml` y añade la línea `<uses-permission … INTERNET />` justo antes de `<application`. El archivo queda así:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Permiso normal: se concede al instalar -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.ValoraPMDM">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.ValoraPMDM">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

Si tu manifiesto tiene alguna línea más dentro de `<activity>` (por ejemplo, `android:label`), déjala: lo único que se añade es el permiso.

**Comprueba que…** la línea del permiso está fuera de `<application>`, justo antes.

## Paso 4 · El modelo de datos

Es la capa **Modelo**, la de abajo del esquema del punto 3. Crea el archivo **`Valora`** (ver «Cómo se crea un archivo de Kotlin», en el paso 1). Tiene las dos clases de datos de la app:

- `ValoraJson`: una valoración tal como llega en el JSON.
- `Valora`: una valoración tal como se guarda en Room, con su evaluación.

Y la función `aValora`, que convierte la primera en la segunda. Va en este archivo porque solo trata de estas dos clases.

Lo que empieza por **`@`** es una **anotación**: una etiqueta que se pone encima de una clase o de una función para que una librería haga algo con ella. Las anotaciones no hacen nada por sí solas; las lee la librería al compilar o al ejecutar.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `data class` | Una clase pensada solo para guardar datos. Kotlin le añade, sin escribirlas, funciones para compararlas, copiarlas (`copy`) y mostrarlas como texto. |
| `val id: Int` | Una **propiedad**: un dato de la clase. `val` quiere decir que no cambia después de crear el objeto; `Int`, que es un número entero. |
| `@Serializable` | Para kotlinx.serialization: permite convertir el texto JSON en objetos de esta clase. Los nombres de las propiedades (`id`, `nota`) tienen que ser los del JSON. |
| `@Entity(tableName = "valora", …)` | Para Room: convierte la clase en la tabla `valora`. Cada propiedad es una columna y cada objeto, una fila. |
| `primaryKeys = ["evaluacion", "id"]` | La **clave primaria**: las columnas que identifican cada fila. No puede haber dos filas con la misma evaluación y el mismo `id`. |
| `fun ValoraJson.aValora(evaluacion: Int) = …` | Una **función de extensión**: se escribe fuera de la clase `ValoraJson`, pero se usa como si fuera suya: `json.aValora(1)`. Dentro, `id` y `nota` son los del objeto con el que se llama. El `=` sin llaves devuelve directamente lo que va detrás. |

```kotlin
package com.example.valorapmdm

import androidx.room.Entity
import kotlinx.serialization.Serializable

// ── Lo que llega en el JSON ──────────────────────────────────────────
// Cada objeto del archivo: { "id": 1, "nota": 4 }
// Los nombres de las propiedades tienen que ser los mismos que en el JSON
@Serializable
data class ValoraJson(
    val id: Int,
    val nota: Int
)

// ── Lo que se guarda en Room ─────────────────────────────────────────
// Lo mismo y, además, de qué evaluación es (en el JSON lo dice el nombre del archivo).
// La clave primaria son dos columnas: la valoración 1 de la 1ª evaluación
// no es la misma que la valoración 1 de la 2ª
@Entity(tableName = "valora", primaryKeys = ["evaluacion", "id"])
data class Valora(
    val evaluacion: Int,
    val id: Int,
    val nota: Int
)

// ── De lo que llega a lo que se guarda ───────────────────────────────
// Convierte una valoración del JSON en una fila de la tabla, añadiéndole su evaluación
fun ValoraJson.aValora(evaluacion: Int) = Valora(evaluacion = evaluacion, id = id, nota = nota)
```

**Comprueba que…** el archivo no tiene nada en rojo. Si algo sale en rojo, revisa el paso 2: falta alguna librería o el *Sync Now*.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué `ValoraJson` no tiene la propiedad `evaluacion`?
2. ¿Podría haber en la tabla dos filas con `id` 1?
3. ¿Qué devuelve `ValoraJson(id = 7, nota = 5).aValora(2)`?

<details>
<summary>Respuestas</summary>

1. Porque el JSON no la trae: de qué evaluación es cada valoración lo dice el nombre del archivo (`valora1.json`, `valora2.json`…).
2. Sí, si son de evaluaciones distintas: la clave primaria es la pareja (`evaluacion`, `id`). Lo que no puede haber son dos filas con la misma pareja.
3. `Valora(evaluacion = 2, id = 7, nota = 5)`: la misma valoración, con su evaluación añadida.

</details>

## Paso 5 · La base de datos con Room

**Room** guarda los datos en una base de datos **SQLite** dentro del móvil. Se usa con tres piezas: la **entidad** (la tabla, ya hecha en el paso 4), el **DAO** (las consultas) y la **base de datos**.

**5.1 · El DAO.** Crea el archivo **`ValoraDao`**. Un **DAO** (*Data Access Object*) es una interfaz con las operaciones sobre la tabla; Room escribe el código de cada una al compilar.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `@Dao interface` | Una **interfaz** dice qué funciones hay, pero no lo que hacen por dentro. Con `@Dao`, Room escribe ese código al compilar (con KSP). |
| `@Query("SELECT * FROM valora ORDER BY evaluacion, id")` | La consulta en **SQL**, el lenguaje de las bases de datos: todas las columnas (`*`) de todas las filas de `valora`, ordenadas por evaluación y por `id`. |
| `:evaluacion` | Dentro de una `@Query`, se sustituye por el parámetro de la función que se llama igual. |
| `Flow<List<Valora>>` | La lista de valoraciones, pero «que avisa»: cada vez que la tabla cambia, Room envía la lista nueva. |
| `suspend` | Una función que tarda (leer o escribir en el disco) y que, por eso, se llama desde una corrutina, sin bloquear la pantalla. |
| `@Insert` | Room escribe el código que guarda cada objeto como una fila. |
| `@Transaction` | Todo lo que hace la función se hace entero o no se hace: si fallara la inserción, no se quedaría la evaluación borrada. |

```kotlin
package com.example.valorapmdm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ValoraDao {

    // ── Leer ─────────────────────────────────────────────────────────
    // Todas las valoraciones guardadas, ordenadas.
    // Devuelve un Flow: cada vez que la tabla cambia, envía la lista nueva
    // y la pantalla se actualiza sola
    @Query("SELECT * FROM valora ORDER BY evaluacion, id")
    fun todas(): Flow<List<Valora>>

    // ── Escribir ─────────────────────────────────────────────────────
    // suspend: tarda, así que se llama desde una corrutina, fuera del hilo principal
    @Query("DELETE FROM valora WHERE evaluacion = :evaluacion")
    suspend fun borrarEvaluacion(evaluacion: Int)

    @Insert
    suspend fun insertar(lista: List<Valora>)

    // Sustituye las valoraciones de una evaluación por las recién descargadas.
    // @Transaction: o se hacen las dos cosas, o ninguna. Si fallara la inserción,
    // no se quedaría la evaluación borrada
    @Transaction
    suspend fun sustituir(evaluacion: Int, lista: List<Valora>) {
        borrarEvaluacion(evaluacion)
        insertar(lista)
    }
}
```

**5.2 · La base de datos.** Crea el archivo **`ValoraDatabase`**. Declara qué tablas tiene (`entities`) y da acceso al DAO. Se crea **una sola vez** para toda la app: abrir la base de datos cuesta, y dos copias abiertas a la vez podrían pisarse.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `@Database(entities = [Valora::class], version = 1)` | Declara la base de datos: sus tablas (una, `Valora`) y la versión de su estructura. |
| `abstract class … : RoomDatabase()` | `abstract`: a la clase le falta código, que escribe Room al compilar. `: RoomDatabase()`: la clase **hereda** de la de Room, es decir, tiene todo lo que tiene esa y algo más. |
| `abstract fun valoraDao()` | La función que da el DAO. Room escribe su código. |
| `companion object` | Lo que pertenece a la clase y no a cada objeto: aquí, la única base de datos (`instancia`) y la función para obtenerla. Se usa sin crear objetos: `ValoraDatabase.obtener(…)`. |
| `?:` | «Si lo de la izquierda es `null`, usa lo de la derecha». Si ya hay base de datos, la devuelve; si no, la crea. |
| `@Volatile` y `synchronized` | Evitan que dos partes de la app, a la vez, creen dos bases de datos. |

```kotlin
package com.example.valorapmdm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Las tablas de la base de datos y su versión (se sube si cambia la estructura)
@Database(entities = [Valora::class], version = 1)
abstract class ValoraDatabase : RoomDatabase() {

    // Room escribe el código de esta función: devuelve el DAO
    abstract fun valoraDao(): ValoraDao

    // ── Una sola base de datos para toda la app ──────────────────────
    companion object {
        @Volatile
        private var instancia: ValoraDatabase? = null

        // Si ya existe, la devuelve; si no, la crea (archivo "valora.db")
        fun obtener(context: Context): ValoraDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(context, ValoraDatabase::class.java, "valora.db")
                    .build()
                    .also { instancia = it }
            }
    }
}
```

**Comprueba que…** *Build > Make Project* (o el martillo de la barra de arriba) termina sin errores. Room revisa las consultas al compilar: si una tabla o columna está mal escrita en `@Query`, el error sale aquí.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. Si ya hay guardadas 13 valoraciones de la 1ª y 13 de la 2ª, y se llama a `sustituir(1, …)` con 10 valoraciones nuevas, ¿cuántas filas quedan en la tabla?
2. ¿Por qué `todas()` no lleva `suspend` y las otras funciones del DAO sí?

<details>
<summary>Respuestas</summary>

1. 23: se borran las 13 de la 1ª, se insertan las 10 nuevas y las 13 de la 2ª no se tocan.
2. Porque devuelve un `Flow`: no espera a tener la respuesta, sino que la va enviando cada vez que la tabla cambia. Las otras tienen que esperar a que termine la operación en el disco.

</details>

## Paso 6 · Internet con Retrofit

**Retrofit** hace las peticiones HTTP. Se describe cada petición en una **interfaz** y Retrofit escribe el código que la hace. El **conversor** de kotlinx.serialization pasa la respuesta JSON a objetos `ValoraJson`.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `interface ServicioValora` | Describe las peticiones, sin escribir cómo se hacen: Retrofit escribe ese código. |
| `@GET("valora{evaluacion}.json")` | Una petición **GET** (pedir un archivo) a la dirección `URL_BASE` + ese texto. |
| `@Path("evaluacion")` | Sustituye `{evaluacion}` por el valor del parámetro: con `valora(2)`, pide `valora2.json`. |
| `List<ValoraJson>` | Lo que devuelve la función: el conversor transforma el JSON de la respuesta en esta lista. |
| `object Red` | Un **objeto único** para toda la app: Kotlin crea uno solo y no se pueden crear más. Construir Retrofit cuesta, y con uno basta. |
| `Json { ignoreUnknownKeys = true }` | Cómo se convierte el JSON: si trae campos que `ValoraJson` no tiene, se ignoran en lugar de dar error. |
| `Retrofit.Builder() … .build()` | Construye Retrofit pieza a pieza: la dirección base, el conversor y, al final, `build()`. |
| `.create(ServicioValora::class.java)` | Crea el servicio: el objeto que hace de verdad las peticiones de la interfaz. |

Crea el archivo **`ServicioValora`**:

```kotlin
package com.example.valorapmdm

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// ── La petición ──────────────────────────────────────────────────────
// Cada función de la interfaz es una petición; Retrofit escribe el código.
// {evaluacion} se sustituye por el número que se pasa en @Path:
//   valora(1) → GET URL_BASE + "valora1.json"
//   valora(2) → GET URL_BASE + "valora2.json"
// suspend: la respuesta tarda, así que se llama desde una corrutina
interface ServicioValora {
    @GET("valora{evaluacion}.json")
    suspend fun valora(@Path("evaluacion") evaluacion: Int): List<ValoraJson>
}

// ── Un solo objeto Retrofit para toda la app ─────────────────────────
object Red {
    // La parte común de todas las direcciones. Tiene que terminar en /
    private const val URL_BASE =
        "https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/"

    // ignoreUnknownKeys: si el JSON trae campos que ValoraJson no tiene, se ignoran
    // en lugar de dar error
    private val json = Json { ignoreUnknownKeys = true }

    val servicio: ServicioValora = Retrofit.Builder()
        .baseUrl(URL_BASE)
        // El conversor: pasa el JSON de la respuesta a objetos ValoraJson
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ServicioValora::class.java)
}
```

**Comprueba que…** el archivo no tiene nada en rojo y que, en el navegador, `URL_BASE` + `valora1.json` muestra la lista de valoraciones, y `URL_BASE` + `valora3.json` dice «404: Not Found».

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Qué dirección completa pide `valora(2)`?
2. ¿Por qué la `URL_BASE` tiene que terminar en `/`?

<details>
<summary>Respuestas</summary>

1. `https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/ejemplo/valora2.json`: la `URL_BASE` y, detrás, `valora2.json`.
2. Porque Retrofit pega el nombre del archivo detrás de ella. Retrofit lo exige: si no termina en `/`, da un error al crearse y la app se cierra.

</details>

## Paso 7 · El estado de la pantalla

El **estado** es todo lo que la pantalla necesita para dibujarse. Se guarda en una `data class` y la pantalla lo pinta tal cual. Lo que se puede **calcular** a partir de otros datos (las pestañas, la evaluación que se muestra, el total, la media, las estrellas y las filas del recuento) no se guarda: se calcula con propiedades `get()`, así nunca queda desactualizado.

Todos los cálculos están aquí, y ninguno en la pantalla:

- cada barra del recuento llega a la pantalla como una `FilaNota`, con la nota, las veces y la **proporción** (la parte llena de la barra, de 0 a 1) ya calculadas;
- la media llega también **escrita** (`mediaTexto`, «3.8»), con un decimal y con punto;
- la posición de la pestaña marcada llega como un número (`indicePestana`: 0 la primera, 1 la segunda…).

La pantalla solo lo dibuja.

`seleccionada` es la pestaña que se ha tocado; `mostrada`, la que de verdad se dibuja. Casi siempre son la misma. Son distintas cuando la evaluación seleccionada no tiene valoraciones, por ejemplo, al abrir la app con valoraciones solo de la 2ª: `seleccionada` vale 1, pero `mostrada` vale 2, la primera que hay.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `= emptyMap()`, `= 1`, `= false`, `= null` | **Valores por defecto**: `ValoraUiState()`, sin pasar nada, crea el estado inicial. |
| `Map<Int, List<Valora>>` | Un **mapa**: a cada clave (el número de evaluación) le corresponde un valor (su lista de valoraciones). |
| `String?`, `Int?`, `Double?` | El `?` quiere decir que puede valer `null`, es decir, «no hay». |
| `val media: Double? get() = …` | Una **propiedad calculada**: no guarda nada; cada vez que se lee, se calcula con lo que va detrás de `get() =`. |
| `keys.sorted()` | Las claves del mapa (las evaluaciones), ordenadas. |
| `firstOrNull()` | El primer elemento de la lista o, si está vacía, `null`. |
| `mostrada?.let { porEvaluacion[it] }.orEmpty()` | Si `mostrada` no es `null`, busca su lista en el mapa (`it` es el valor de `mostrada`). Si no hay, una lista vacía. |
| `data class FilaNota(…)` | Una clase pequeña con los tres datos de una fila del recuento. Se declara en el mismo archivo porque solo la usa el estado. |
| `(5 downTo 1).map { nota -> … }` | Recorre las notas de la 5 a la 1 y, con cada una, crea una `FilaNota`: el resultado es una lista de cinco filas, en el orden en que se dibujan. |
| `count { … }`, `map`, `average()` | Funciones de Kotlin para listas: contar los que cumplen una condición, transformar cada elemento y calcular la media. |
| `media?.roundToInt() ?: 0` | Si hay media, la redondea (3.8 → 4); si es `null`, 0 estrellas. |
| `String.format(Locale.US, "%.1f", it)` | Escribe un número con un decimal (`%.1f`). `Locale.US` hace que el separador sea el punto, sea cual sea el idioma del móvil. |
| `indexOf(mostrada)` | La posición de `mostrada` en la lista de evaluaciones, empezando en 0. Si no está, -1. |
| `if (total == 0) 0f else veces.toFloat() / total` | La proporción de la barra. `toFloat()` pasa el número a decimal para que la división no dé un entero; si no hay valoraciones, 0, para no dividir entre 0. |

Crea el archivo **`ValoraUiState`**:

```kotlin
package com.example.valorapmdm

import java.util.Locale
import kotlin.math.roundToInt

// ── Una fila del recuento, ya calculada ──────────────────────────────
// La pantalla solo la dibuja: no tiene que contar ni dividir nada
data class FilaNota(
    val nota: Int,          // de 1 a 5
    val veces: Int,         // cuántas veces se ha dado
    val proporcion: Float   // veces / total, de 0 a 1: la parte llena de la barra
)

// Todo lo que muestra la pantalla
data class ValoraUiState(
    // ── Datos guardados ──────────────────────────────────────────────
    // Las valoraciones de Room, agrupadas por evaluación: 1 → [...], 2 → [...]
    val porEvaluacion: Map<Int, List<Valora>> = emptyMap(),

    // ── Estado de la interfaz ────────────────────────────────────────
    val seleccionada: Int = 1,      // la pestaña elegida
    val cargando: Boolean = false,  // si se está descargando (barra de progreso)
    val aviso: String? = null       // el mensaje de error, si lo hay
) {
    // ── Lo que se calcula ────────────────────────────────────────────
    // Las evaluaciones que tienen valoraciones, en orden: son las pestañas
    val evaluaciones: List<Int>
        get() = porEvaluacion.keys.sorted()

    // La evaluación que se muestra: la elegida, si tiene valoraciones; si no,
    // la primera que tenga (por ejemplo, si solo hay de la 2ª). null si no hay ninguna
    val mostrada: Int?
        get() = if (seleccionada in evaluaciones) seleccionada else evaluaciones.firstOrNull()

    // La posición de la pestaña que se muestra (0, 1, 2…). -1 si no hay pestañas
    val indicePestana: Int
        get() = evaluaciones.indexOf(mostrada)

    // Las valoraciones de la evaluación que se muestra
    val lista: List<Valora>
        get() = mostrada?.let { porEvaluacion[it] }.orEmpty()

    // Cuántas valoraciones hay
    val total: Int
        get() = lista.size

    // La media de las notas. Sin valoraciones no hay media: null
    val media: Double?
        get() = if (lista.isEmpty()) null else lista.map { it.nota }.average()

    // La media como se escribe en la pantalla: con un decimal y punto decimal (3.8),
    // sea cual sea el idioma del móvil (Locale.US). null si no hay media
    val mediaTexto: String?
        get() = media?.let { String.format(Locale.US, "%.1f", it) }

    // Cuántas estrellas van de color: la media redondeada (3.8 → 4)
    val estrellas: Int
        get() = media?.roundToInt() ?: 0

    // El recuento, de la nota 5 a la 1: una fila por nota, también las que no ha dado nadie
    val filas: List<FilaNota>
        get() = (5 downTo 1).map { nota ->
            val veces = lista.count { it.nota == nota }
            FilaNota(
                nota = nota,
                veces = veces,
                proporcion = if (total == 0) 0f else veces.toFloat() / total
            )
        }
}
```

**Comprueba que…** el archivo no tiene nada en rojo.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. Si la evaluación mostrada tiene las notas 4, 5 y 3, ¿cuánto valen `media`, `estrellas` y la fila de la nota 4?
2. ¿Por qué `media` es `Double?` y no `Double`?

<details>
<summary>Respuestas</summary>

1. `media` = 4.0 (12 / 3) y `estrellas` = 4. La fila de la nota 4 es `FilaNota(nota = 4, veces = 1, proporcion = 0.33)`: una de las tres valoraciones, así que su barra está llena en un tercio.
2. Porque sin valoraciones no hay media: vale `null`, y `mediaTexto` también. La pantalla, al ver `mediaTexto` en `null`, muestra «No hay valoraciones guardadas».

</details>

## Paso 8 · El ViewModel

El **ViewModel** es el que decide: lee de Room, descarga con Retrofit, guarda y prepara el estado. **Sobrevive al giro** de pantalla, así que no se pierde nada ni se repite la descarga. Es un `AndroidViewModel` porque necesita el contexto de la aplicación para abrir la base de datos.

El estado junta dos fuentes con `combine`: lo guardado en **Room** (que cambia cuando se descarga) y lo de la **interfaz** (pestaña, carga, aviso). Cuando cualquiera de las dos cambia, el estado se recalcula y la pantalla se redibuja.

En `cargar()` hay dos `try`, uno dentro de otro:

- El de **dentro** está en cada vuelta del bucle. Recoge solo el 404, el archivo que no existe: borra esa evaluación y deja que el bucle siga con la siguiente. Cualquier otro código lo vuelve a lanzar (`throw e`) hacia fuera.
- El de **fuera** recoge lo que detiene la descarga: sin conexión (`IOException`), un error del servidor (`HttpException`) o un JSON con otro formato (`SerializationException`). Cada uno llama a `avisar` con su motivo.
- `finally` se ejecuta siempre, haya ido bien o mal: quita la barra de progreso.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `: AndroidViewModel(application)` | La clase hereda de `AndroidViewModel`: es un ViewModel con acceso a la aplicación, que hace falta para abrir la base de datos. |
| `private` | Solo se usa dentro de esta clase. La pantalla no puede tocar `dao` ni `pantalla`: solo lee `uiState` y llama a `elegir` y `cargar`. |
| `MutableStateFlow` y `StateFlow` | Un StateFlow que se puede cambiar (`pantalla`) y otro que solo se puede leer (`uiState`, el que ve la pantalla). |
| `combine(a, b) { … }` | Junta dos Flow. Cada vez que cambia cualquiera de los dos, ejecuta el bloque con el último valor de cada uno. |
| `groupBy { it.evaluacion }` | Agrupa la lista por evaluación y da un mapa: `1 → [...]`, `2 → [...]`. `it` es cada elemento de la lista. |
| `stateIn(viewModelScope, …, ValoraUiState())` | Convierte el Flow en un StateFlow, con el estado inicial vacío mientras llegan los datos de Room. `WhileSubscribed(5_000)` lo mantiene activo 5 segundos después de que la pantalla deje de mirarlo, lo que dura un giro. |
| `pantalla.update { it.copy(…) }` | Cambia el estado: copia el actual (`it`) cambiando solo lo que va entre paréntesis. |
| `viewModelScope.launch { … }` | Empieza una **corrutina** que dura lo que dure el ViewModel. |
| `try`, `catch`, `finally`, `throw` | `try` intenta el código; si lanza una excepción, la recoge el `catch` de su tipo; `finally` se ejecuta siempre; `throw` vuelve a lanzar una excepción. |
| `Log.d` y `Log.e` | Escriben en Logcat un mensaje de depuración (`D`) o de error (`E`), con la etiqueta `ValoraPMDM`. |

Crea el archivo **`ValoraViewModel`**:

```kotlin
package com.example.valorapmdm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ValoraViewModel(application: Application) : AndroidViewModel(application) {

    // ── Las fuentes de datos ─────────────────────────────────────────
    private val dao = ValoraDatabase.obtener(application).valoraDao()

    // Lo que no viene de Room: la pestaña elegida, si está cargando y el aviso
    private val pantalla = MutableStateFlow(ValoraUiState())

    // ── El estado que ve la pantalla ─────────────────────────────────
    // Junta lo guardado en Room con lo de la interfaz. Cuando cambia cualquiera
    // de los dos, se vuelve a calcular
    val uiState: StateFlow<ValoraUiState> =
        combine(dao.todas(), pantalla) { guardadas, p ->
            p.copy(porEvaluacion = guardadas.groupBy { it.evaluacion })
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ValoraUiState())

    // ── Acciones de la pantalla ──────────────────────────────────────
    // Al tocar una pestaña
    fun elegir(evaluacion: Int) {
        pantalla.update { it.copy(seleccionada = evaluacion) }
    }

    // Al pulsar el botón de recargar: pide las tres evaluaciones y deja en Room
    // lo que hay publicado. Al abrir la app no se llama: se muestra lo guardado
    fun cargar() {
        // Si ya se está descargando, no se empieza otra descarga
        if (pantalla.value.cargando) return
        pantalla.update { it.copy(cargando = true, aviso = null) }

        // Una corrutina: las peticiones tardan y no pueden bloquear el hilo principal
        viewModelScope.launch {
            try {
                for (evaluacion in 1..3) {
                    try {
                        val lista = Red.servicio.valora(evaluacion)
                        // Del JSON a la tabla (aValora, en Valora.kt) y se guarda
                        dao.sustituir(evaluacion, lista.map { it.aValora(evaluacion) })
                        Log.d("ValoraPMDM", "Evaluación $evaluacion: ${lista.size} valoraciones")
                    } catch (e: HttpException) {
                        // Solo el 404 (el archivo no existe) quiere decir «sin valoraciones publicadas».
                        // Cualquier otro código es un error del servidor: se trata más abajo
                        if (e.code() != 404) throw e
                        // Si había valoraciones guardadas de esa evaluación, ya no están publicadas
                        dao.borrarEvaluacion(evaluacion)
                        Log.d("ValoraPMDM", "Evaluación $evaluacion: sin archivo (404)")
                    }
                }
            } catch (e: IOException) {
                // No hubo respuesta: no hay conexión o no se encuentra el servidor
                avisar("Sin conexión", e)
            } catch (e: HttpException) {
                // El servidor respondió con un error que no es 404 (403, 500…)
                avisar("Error del servidor (${e.code()})", e)
            } catch (e: SerializationException) {
                // Llegó el archivo, pero su contenido no es el JSON esperado
                avisar("Los datos descargados no tienen el formato esperado", e)
            } finally {
                // Siempre, haya ido bien o mal: se quita la barra de progreso
                pantalla.update { it.copy(cargando = false) }
            }
        }
    }

    // Escribe el error en Logcat y lo avisa en la pantalla. Lo guardado en Room no se toca
    private fun avisar(motivo: String, e: Exception) {
        Log.e("ValoraPMDM", motivo, e)
        pantalla.update { it.copy(aviso = "$motivo: se muestran las valoraciones guardadas") }
    }
}
```

**Comprueba que…** *Build > Make Project* termina sin errores.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. Sin conexión, al pulsar recargar, ¿qué `catch` recoge el error y qué pasa con lo guardado en Room?
2. Si `valora3.json` da 404, ¿qué hace `cargar()`?
3. ¿Por qué al girar el móvil no se vuelve a descargar?

<details>
<summary>Respuestas</summary>

1. El de `IOException`: llama a `avisar("Sin conexión", e)`, que lo escribe en Logcat y pone el aviso en la pantalla. Room no se toca.
2. El `catch` de dentro ve que el código es 404: borra las valoraciones guardadas de la 3ª (si las había), escribe «Evaluación 3: sin archivo (404)» en Logcat y el bucle sigue. Como es la última, termina y `finally` quita la barra.
3. Porque la descarga solo empieza al llamar a `cargar()`, y el ViewModel, con su estado, sobrevive al giro.

</details>

## Paso 9 · La pantalla

La pantalla está hecha con **Compose**: cada función `@Composable` dibuja una parte, y se vuelve a dibujar sola cuando cambia el estado. La pantalla no decide ni calcula nada: muestra el estado tal como llega y, cuando se pulsa algo, avisa al ViewModel.

| Función | Qué dibuja |
|---|---|
| `PantallaValora` | Todo: la barra de arriba con ↻ (desactivado mientras descarga), las pestañas, el aviso, el resumen y el recuento |
| `Resumen` | La media ya escrita (`mediaTexto`), las estrellas y el número de valoraciones, en singular si solo hay una; si no hay media, el mensaje |
| `Estrellas` | Cinco estrellas: las primeras de color y el resto en gris |
| `FilaRecuento` | Una fila del recuento, que recibe ya calculada (`FilaNota`): la nota, su barra y cuántas veces se ha dado |

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `@Composable` | Marca una función que dibuja con Compose. Solo se puede llamar desde otra `@Composable`. |
| `viewModel()` | Da el ViewModel de la pantalla: lo crea la primera vez y, después de un giro, devuelve el mismo. |
| `val estado by … collectAsStateWithLifecycle()` | Lee el `uiState` del ViewModel. Cuando cambia, Compose vuelve a ejecutar las funciones que lo usan y la pantalla se redibuja. Deja de leerlo cuando la app no se ve. |
| `Scaffold` y `TopAppBar` | La estructura de la pantalla: una barra arriba, con el título y el botón, y el contenido debajo. |
| `Column` y `Row` | Colocan lo que tienen dentro uno debajo de otro (`Column`) o uno al lado de otro (`Row`). |
| `Modifier` | Ajusta un elemento: `padding` (margen), `fillMaxWidth` (todo el ancho), `width` y `height` (tamaño fijo), `weight(1f)` (el espacio que sobra), `verticalScroll(rememberScrollState())` (desplazar hacia abajo lo que no cabe; `rememberScrollState` recuerda hasta dónde se ha desplazado). |
| `PrimaryTabRow` y `Tab` | La fila de pestañas y cada pestaña. |
| `LinearProgressIndicator` | Una barra horizontal. Sin `progress`, se mueve sola (la de carga); con `progress`, está llena hasta ese punto, de 0 a 1 (las del recuento). Material 3 dibuja, por defecto, un hueco entre la parte llena y el resto y un punto al final; `gapSize = 0.dp` y `drawStopIndicator = {}` los quitan y dejan la barra lisa. |
| `onClick = { viewModel.cargar() }` | Lo que va entre llaves es una **lambda**: un trozo de código que se ejecuta después, cuando se pulsa. |
| `if`, `for`, `?.let` | Dentro de una función `@Composable`, deciden qué se dibuja: solo si se cumple algo, una vez por cada elemento o solo si no es `null`. |

Crea el archivo **`PantallaValora`**:

```kotlin
package com.example.valorapmdm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// ── La pantalla completa ─────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaValora(viewModel: ValoraViewModel = viewModel()) {
    // El estado del ViewModel. Cuando cambia, esta función se vuelve a ejecutar
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        // La barra de arriba: el título y el botón de recargar
        topBar = {
            TopAppBar(
                title = { Text("Valoraciones de PMDM") },
                actions = {
                    // Mientras descarga, el botón está desactivado
                    IconButton(onClick = { viewModel.cargar() }, enabled = !estado.cargando) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Recargar las valoraciones")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Las pestañas: una por cada evaluación con valoraciones ──
            if (estado.evaluaciones.isNotEmpty()) {
                PrimaryTabRow(selectedTabIndex = estado.indicePestana) {
                    estado.evaluaciones.forEach { evaluacion ->
                        Tab(
                            selected = evaluacion == estado.mostrada,
                            onClick = { viewModel.elegir(evaluacion) },
                            text = { Text("${evaluacion}ª evaluación") }
                        )
                    }
                }
            }

            // verticalScroll: si no cabe (por ejemplo, con el móvil girado), se desplaza hacia abajo
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // ── La barra de progreso y el aviso ─────────────────────
                if (estado.cargando) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                estado.aviso?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
                }

                // ── La media y el recuento ──────────────────────────────
                Resumen(mediaTexto = estado.mediaTexto, estrellas = estado.estrellas, total = estado.total)
                if (estado.total > 0) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    // Las filas vienen ya calculadas en el estado: aquí solo se dibujan
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        for (fila in estado.filas) {
                            FilaRecuento(fila)
                        }
                    }
                }
            }
        }
    }
}

// ── La media con un decimal, sus estrellas y cuántas valoraciones hay ──
@Composable
fun Resumen(mediaTexto: String?, estrellas: Int, total: Int) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (mediaTexto == null) {
            Text("No hay valoraciones guardadas", style = MaterialTheme.typography.titleMedium)
            Text("Pulsa el botón de recargar para descargarlas", style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(mediaTexto, style = MaterialTheme.typography.displayMedium)
            Estrellas(estrellas)
            // En singular si solo hay una
            Text(if (total == 1) "1 valoración" else "$total valoraciones", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ── Cinco estrellas: las primeras, del color del tema; el resto, en gris ──
@Composable
fun Estrellas(llenas: Int) {
    Row {
        repeat(5) { i ->
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = if (i < llenas) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

// ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
@Composable
fun FilaRecuento(fila: FilaNota) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("${fila.nota}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(20.dp))
        Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        // La misma barra que la de carga, pero con la parte llena fija (de 0 a 1)
        LinearProgressIndicator(
            progress = { fila.proporcion },
            modifier = Modifier
                .weight(1f)              // ocupa todo el ancho que dejan el número y la estrella
                .height(12.dp)
                .padding(horizontal = 8.dp),
            // Barra lisa: sin hueco entre la parte llena y el resto, ni punto al final
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
        Text("${fila.veces}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(28.dp))
    }
}
```

**Comprueba que…** el archivo no tiene nada en rojo.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Qué parte del código pone el botón de recargar en gris mientras descarga?
2. Cuando llegan valoraciones nuevas, nadie llama a ninguna función para redibujar. ¿Por qué se actualiza la pantalla?

<details>
<summary>Respuestas</summary>

1. `enabled = !estado.cargando`, en el `IconButton`: el botón solo está activo cuando no se está descargando.
2. Porque `estado` se lee con `collectAsStateWithLifecycle()`: cuando el `uiState` cambia, Compose vuelve a ejecutar `PantallaValora` y las funciones que usan el estado.

</details>

## Paso 10 · MainActivity

`MainActivity` es lo primero que se ejecuta al abrir la app. Solo aplica el tema y muestra la pantalla.

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `class MainActivity : ComponentActivity()` | La ventana de la app. Hereda de `ComponentActivity`, la ventana de Android que sabe mostrar Compose. |
| `override fun onCreate(…)` | La función que Android llama al crear la ventana. `override`: sustituye a la que trae `ComponentActivity`; `super.onCreate(…)` ejecuta antes la original. |
| `setContent { … }` | Lo que se dibuja en la ventana, con Compose. |
| `ValoraPMDMTheme { … }` | Aplica los colores y las letras del tema a todo lo que tiene dentro. |

Abre `MainActivity.kt` (ya existe) y sustituye **todo** su contenido. Se quitan `Greeting` y su vista previa, que eran el «Hello Android!» del proyecto vacío.

```kotlin
package com.example.valorapmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.valorapmdm.ui.theme.ValoraPMDMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // la app ocupa toda la pantalla, también detrás de las barras del sistema
        setContent {
            // El tema (colores y letras) que creó Android Studio, en ui.theme
            ValoraPMDMTheme {
                PantallaValora()
            }
        }
    }
}
```

**Comprueba que…** ▶ abre la app con el título «Valoraciones de PMDM», el botón ↻ y el mensaje «No hay valoraciones guardadas».

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿En qué línea se decide qué pantalla se muestra al abrir la app?

<details>
<summary>Respuestas</summary>

1. En `setContent`, dentro del tema: `PantallaValora()`.

</details>

## Paso 11 · Probar la app

**11.1 · Las pruebas.** Hazlas en orden:

| Nº | Prueba | Cómo | Resultado esperado |
|---|---|---|---|
| 1 | Primera apertura | Abrir la app | «No hay valoraciones guardadas · Pulsa el botón de recargar para descargarlas». Sin pestañas. |
| 2 | Descargar | Pulsar ↻ | Un momento la barra de progreso y el botón ↻ en gris; después, las pestañas **1ª evaluación** y **2ª evaluación**. No hay 3ª. |
| 3 | 1ª evaluación | Pestaña 1ª | **3.8**, cuatro estrellas de color, «13 valoraciones» y el recuento: 5 → 3, 4 → 6, 3 → 3, 2 → 1 y 1 → 0 |
| 4 | 2ª evaluación | Pestaña 2ª | **4.4** y «13 valoraciones»: 5 → 6, 4 → 6, 3 → 1 y el resto, 0 |
| 5 | Sin conexión | Modo avión en el emulador (desliza desde arriba) y pulsar ↻ | El aviso «Sin conexión: se muestran las valoraciones guardadas» y los datos siguen |
| 6 | Guardado | Con el modo avión, cerrar la app (ver abajo) y abrirla desde su icono | Salen las valoraciones, sin conexión: vienen de Room |
| 7 | Giro | Quitar el modo avión, elegir la 2ª y girar el emulador (botones de girar de su barra) | Sigue en la 2ª, sin barra de progreso: no se ha descargado nada. Deslizando hacia arriba se llega hasta la fila de la nota 1 |

**Cómo se cierra la app en el emulador:** arrastra desde la barra blanca de abajo hacia arriba, hasta la mitad, y espera un momento antes de soltar (o pulsa el botón cuadrado de la barra lateral del emulador). Salen las apps abiertas: arrastra la de ValoraPMDM hacia arriba.

**11.2 · Logcat.** Abre **Logcat** (abajo) y escribe en el filtro:

```
tag~:^ValoraPMDM$
```

Pulsa ↻: tienen que salir tres líneas `D`, una por evaluación:

```
Evaluación 1: 13 valoraciones
Evaluación 2: 13 valoraciones
Evaluación 3: sin archivo (404)
```

Con el modo avión, una línea `E` que empieza por «Sin conexión», con la excepción debajo: explica por qué falló.

`tag~:` busca con una expresión regular: `^` es el principio de la etiqueta y `$` el final, así que solo salen los mensajes con la etiqueta exacta `ValoraPMDM`.

**11.3 · Ver la base de datos.** Con la app abierta en el emulador: *View > Tool Windows > App Inspection* → pestaña **Database Inspector**. A la izquierda sale `valora.db` con la tabla `valora`: haz doble clic para ver sus filas, con las columnas `evaluacion`, `id` y `nota`. Son 26: 13 de cada evaluación.

**Comprueba que…** las siete pruebas dan el resultado esperado, Logcat muestra las líneas de la descarga y la tabla tiene 26 filas.

## Paso 12 · Generar e instalar el APK

El **APK** (*Android Package*) es el archivo que se instala en un móvil.

1. *Build > Generate App Bundles or APKs > Generate APKs* (en algunas versiones, *Build > Build App Bundle(s) / APK(s) > Build APK(s)*).
2. Al terminar, abajo a la derecha sale un aviso con el enlace **locate**: abre la carpeta `app\build\outputs\apk\debug\`, con `app-debug.apk`.
3. En el emulador, mantén pulsado el icono de ValoraPMDM → *App info* → **Uninstall**.
4. Arrastra `app-debug.apk` desde la carpeta hasta la ventana del emulador.
5. Desliza hacia arriba en la pantalla de inicio del emulador para ver todas las apps y abre ValoraPMDM desde ahí. No pulses ▶ en Android Studio: volvería a instalar la app y ya no estarías probando el APK.

**Si además tienes un móvil Android:** copia `app-debug.apk` al móvil (por cable USB, a la carpeta `Download`, o enviándotelo por correo), ábrelo con la app **Archivos**, permite instalar apps de esa fuente si lo pide y toca **Instalar** (si Play Protect avisa, **Instalar de todas formas**).

**Comprueba que…** la app instalada desde el APK se abre desde la lista de apps y, al pulsar ↻, descarga las valoraciones.

## Paso 13 · Las valoraciones reales

Las valoraciones reales de cada evaluación están en `valora/`, sin `ejemplo/`. Para que la app las use, solo cambia la URL base, en `ServicioValora.kt`:

```kotlin
    private const val URL_BASE =
        "https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/valora/"
```

El resto del código no cambia: los nombres de los archivos y su formato son los mismos. Las evaluaciones que aún no tengan archivo dan 404 y no aparecen.

Lo que ya estaba guardado en Room de los ejemplos se sustituye al pulsar ↻ en las evaluaciones que tengan archivo real. Para empezar de cero, desinstala la app antes de volver a pulsar ▶: al desinstalar se borra su base de datos.

**Comprueba que…** tras pulsar ↻, Logcat muestra las evaluaciones que se han descargado de `valora/`.

## Paso 14 · Animar las barras

Hasta aquí, al cambiar de pestaña o al recargar, las barras del recuento saltan de golpe a su nuevo tamaño. En este paso se **animan**: al aparecer crecen desde 0 y, al cambiar de pestaña, pasan poco a poco del tamaño que tenían al nuevo. Así se ve qué ha cambiado entre una evaluación y otra.

**Cómo anima Compose.** Una animación es un valor que no salta, sino que **va cambiando poco a poco** hasta llegar a su objetivo. En Compose se hace con **`animateFloatAsState`**: se le da el valor objetivo (la proporción de la barra, de 0 a 1) y devuelve el valor que hay que dibujar en cada momento. Cuando el objetivo cambia, ese valor va del que tenía al nuevo, y cada vez que avanza, la fila **se vuelve a dibujar**, unas 60 veces por segundo, durante el tiempo que dure. La pantalla sigue sin calcular nada: el objetivo llega del estado, como antes; solo cambia cómo se llega a él.

**Que crezcan al aparecer.** `animateFloatAsState` solo anima cuando el objetivo **cambia**. La primera vez que se dibuja una fila, empieza ya en su valor, sin animación. Para que crezca desde 0, la fila recuerda si ya se ha mostrado (`mostrada`, que empieza en `false`): mientras no, el objetivo es 0; en cuanto se dibuja por primera vez, `LaunchedEffect` pone `mostrada` a `true` y el objetivo pasa a ser su proporción.

**Un ejemplo, paso a paso.** En la 1ª evaluación, la nota 5 tiene 3 de 13 valoraciones (proporción 0.23); en la 2ª, 6 de 13 (0.46). Al pasar de la pestaña 1ª a la 2ª, el estado cambia el objetivo de esa barra de 0.23 a 0.46, y el valor que se dibuja va así:

```
tiempo (ms)    0      100    200    300    400    500    600
se dibuja    0.23   0.25   0.34   0.41   0.44   0.46   0.46
                    └ arranca ┘└ rápido ┘└─ frena al final ─┘
```

Entre una columna y otra hay unos seis dibujos más: la barra se dibuja unas 60 veces por segundo. Del 100 al 300 avanza mucho, y del 400 al 600 casi nada: así frena.

En `PantallaValora.kt`, cambia la función **`FilaRecuento`** por esta, con la constante de encima:

**Las piezas del código:**

| Pieza | Qué hace |
|---|---|
| `private const val DURACION_MS = 600` | Lo que tarda la animación: 600 milisegundos, poco más de medio segundo. Con menos, apenas se ve; con más, la app parece lenta |
| `remember { mutableStateOf(false) }` | Un estado que la fila **recuerda** entre un dibujo y el siguiente: si no, volvería a `false` cada vez. Al cambiar, la fila se vuelve a dibujar |
| `var mostrada by …` | Con `by`, `mostrada` se usa como una variable normal (`mostrada = true`), aunque por dentro sea un estado |
| `LaunchedEffect(Unit) { mostrada = true }` | Ejecuta su bloque una sola vez, justo después de que la fila se dibuje por primera vez. Con `Unit` no se repite nunca más mientras la fila exista |
| `animateFloatAsState(targetValue = …)` | Devuelve el valor animado. `targetValue` es el objetivo: 0 hasta que la fila se muestra, y después `fila.proporcion` |
| `tween(durationMillis = …, easing = FastOutSlowInEasing)` | Cómo se anima: en un tiempo fijo (`tween`) y con una **curva** (`easing`). `FastOutSlowInEasing` arranca suave, va rápido y frena al final, como un objeto real que se pone en marcha y se detiene: se ve más natural que a velocidad constante |
| `label = "barra de la nota ${fila.nota}"` | Un nombre para la animación. No se ve en la app; sirve para reconocerla en las herramientas de Android Studio |
| `progress = { proporcion }` | La barra dibuja el valor animado, no `fila.proporcion` directamente |

Añade también estos `import` al principio del archivo, con los demás (o escribe el código y pulsa **Alt + Intro** sobre lo que salga en rojo):

```kotlin
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
```

```kotlin
// Cuánto tarda una barra en llegar a su tamaño, en milisegundos
private const val DURACION_MS = 600

// ── Una fila del recuento: la nota, su barra y cuántas veces se ha dado ──
// La barra está animada (paso 14): al aparecer, crece desde 0; al cambiar de pestaña
// o al recargar, pasa poco a poco de su tamaño de antes al nuevo, en lugar de saltar
@Composable
fun FilaRecuento(fila: FilaNota) {
    // Al principio, la barra vacía; en cuanto la fila se dibuja por primera vez, su valor
    var mostrada by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mostrada = true }

    // El valor que se dibuja: va del que tenía al objetivo en DURACION_MS,
    // rápido al principio y más despacio al final (FastOutSlowInEasing)
    val proporcion by animateFloatAsState(
        targetValue = if (mostrada) fila.proporcion else 0f,
        animationSpec = tween(durationMillis = DURACION_MS, easing = FastOutSlowInEasing),
        label = "barra de la nota ${fila.nota}"
    )

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("${fila.nota}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(20.dp))
        Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        // La misma barra que la de carga; la parte llena (de 0 a 1) es la animada
        LinearProgressIndicator(
            progress = { proporcion },
            modifier = Modifier
                .weight(1f)              // ocupa todo el ancho que dejan el número y la estrella
                .height(12.dp)
                .padding(horizontal = 8.dp),
            // Barra lisa: sin hueco entre la parte llena y el resto, ni punto al final
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
        Text("${fila.veces}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.width(28.dp))
    }
}
```

No hace falta añadir ninguna librería: las animaciones son parte de Compose, que ya está en el proyecto desde el paso 2.

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. En la pestaña 1ª, la nota 4 tiene 6 de 13 valoraciones; en la 2ª, también 6 de 13. Al pasar de una a otra, ¿se mueve su barra? ¿Y la de la nota 5?
2. ¿Qué pasaría si `mostrada` se declarara como `var mostrada = false`, sin `remember` ni `mutableStateOf`?
3. ¿Por qué hace falta `mostrada`, si `animateFloatAsState` ya anima?
4. ¿Por qué el ViewModel y el estado no cambian para animar las barras?

<details>
<summary>Respuestas</summary>

1. La de la nota 4, no: su objetivo es el mismo (6 / 13), así que no hay nada que animar. La de la nota 5 sí: pasa de 3 / 13 a 6 / 13, y crece hasta el doble.
2. Cada vez que la fila se volviera a dibujar, `mostrada` volvería a ser `false` y el objetivo, 0: la barra no llegaría a su tamaño. Además, cambiarla no avisaría a Compose para volver a dibujar. `remember` la conserva entre dibujos y `mutableStateOf` avisa cuando cambia.
3. Porque `animateFloatAsState` solo anima cuando el objetivo cambia. La primera vez, la barra empezaría ya en su tamaño. Con `mostrada`, el primer objetivo es 0 y, justo después, la proporción: el objetivo cambia, y la barra crece.
4. Porque la animación es cómo se dibuja, no qué datos hay: el estado sigue diciendo cuánto tiene que medir cada barra, y la pantalla decide cómo llegar ahí. Es la misma regla de siempre: el estado calcula y la pantalla dibuja.

</details>

**Probar las animaciones.** Pulsa ▶ y hazlas en orden:

| Nº | Prueba | Cómo | Resultado esperado |
|---|---|---|---|
| 1 | Al abrir | Abrir la app con las valoraciones ya guardadas | Las barras crecen desde la izquierda hasta su tamaño, en algo más de medio segundo, más despacio al final |
| 2 | Cambiar de pestaña | De la 1ª a la 2ª y otra vez a la 1ª | Las barras que cambian crecen o encogen hasta su nuevo tamaño: la de la nota 5 crece (de 3 a 6); la de la 3, encoge (de 3 a 1); la de la 4 no se mueve (6 en las dos) |
| 3 | Recargar | Pulsar ↻ | Si los datos no han cambiado, las barras no se mueven: su objetivo es el mismo |
| 4 | Girar | Girar el emulador | Las barras vuelven a crecer desde 0: al girar, la pantalla se dibuja de nuevo. Los datos no se pierden ni se descargan (R7) |
| 5 | A cámara lenta | Cambiar `DURACION_MS` a `3000`, pulsar ▶ y cambiar de pestaña | Se ve despacio cómo cada barra va de su tamaño al nuevo: arranca suave, acelera y frena al final, como en la tabla del ejemplo. Vuelve a dejarlo en `600` |
| 6 | Sin animaciones | En el emulador, **Settings → Accessibility** y activar **Remove animations** (en algunas versiones está dentro de **Color and motion**). Volver a la app y cambiar de pestaña | Las barras cambian de golpe, sin animar: Compose respeta el ajuste del sistema. Desactívalo después |

**Comprueba que…** las seis pruebas dan el resultado esperado.

## Paso 15 · El detalle de una nota

Hasta aquí, la app tiene una sola pantalla. En este paso se añade una **segunda**: al tocar una fila del recuento (por ejemplo, la del 5), se abre una pantalla con **la lista de las valoraciones que tienen esa nota**, y con la flecha de arriba o el botón «atrás» del móvil se vuelve. Es lo mismo que hace ValoraFlutter, para poder comparar cómo se navega en las dos.

**Cómo se navega en Compose.** Se usa **Navigation Compose**, como en el proyecto del Centro de Acogida (UD2, apartado 5 de los apuntes, «Navegación»):

| Pieza | Qué hace |
|---|---|
| `rememberNavController()` | El **controlador de navegación**: sabe en qué pantalla se está y guarda en una **pila** las anteriores, para poder volver |
| `NavHost(navController, startDestination = "resumen") { … }` | Las pantallas de la app. Cada una, con `composable("ruta") { … }`. La primera es la de `startDestination` |
| `composable("nota/{nota}")` | Una ruta con un **argumento**: `{nota}` es la parte que cambia. `"nota/5"` es la pantalla de la nota 5 |
| `navController.navigate("nota/5")` | Va a esa ruta: pone la pantalla encima de la pila |
| `navController.popBackStack()` | Quita la pantalla de encima: se vuelve a la anterior. El botón «atrás» del móvil hace lo mismo sin programarlo |

**Un solo ViewModel para las dos pantallas.** El ViewModel se crea **fuera** del `NavHost` y se pasa a las dos pantallas, como en el Centro de Acogida. Así, la pantalla de una nota ve las mismas valoraciones y la misma pestaña elegida que la principal, sin volver a descargar ni a leer de Room.

**15.1 · La librería.** En `gradle/libs.versions.toml`, añade la versión al final de `[versions]` y la librería al final de `[libraries]`:

```toml
# Navegación entre pantallas (paso 15)
navigationCompose = "2.10.2"
```

```toml
# Navegación entre pantallas (paso 15)
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
```

Y en `app/build.gradle.kts`, en `dependencies`, debajo de la línea de `kotlinx.serialization.json`:

```kotlin
    // Navegación entre pantallas (paso 15)
    implementation(libs.androidx.navigation.compose)
```

Pulsa **Sync Now** en la barra amarilla, como en el paso 2.

**15.2 · Las valoraciones de una nota, en el estado.** En `ValoraUiState.kt`, dentro de la clase, encima de `filas`, añade:

```kotlin
    // Las valoraciones de la evaluación que se muestra con una nota (paso 15)
    fun conNota(nota: Int): List<Valora> = lista.filter { it.nota == nota }
```

`lista` son las valoraciones de la evaluación que se muestra, y `filter` se queda con las que tienen esa nota. Como siempre, el estado calcula y la pantalla solo dibuja.

**15.3 · La fila del recuento, que se pueda tocar.** En `PantallaValora.kt`:

1. Añade el `import` de `clickable`, con los demás: `import androidx.compose.foundation.clickable`.
2. `PantallaValora` recibe qué hacer al tocar una fila. Cambia su cabecera por:

   ```kotlin
   // onNota: lo que se hace al tocar una fila del recuento (paso 15). Lo decide quien la usa: la navegación
   fun PantallaValora(viewModel: ValoraViewModel = viewModel(), onNota: (Int) -> Unit = {}) {
   ```

3. Donde se dibujan las filas, cambia `FilaRecuento(fila)` por:

   ```kotlin
                               FilaRecuento(fila, onClick = { onNota(fila.nota) })
   ```

4. `FilaRecuento` recibe qué hacer al tocarla. Cambia su cabecera y el principio de su `Row`:

   ```kotlin
   // onClick: al tocar la fila. Solo responde si alguien ha puesto esa nota (paso 15)
   fun FilaRecuento(fila: FilaNota, onClick: () -> Unit = {}) {
       …
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .clickable(enabled = fila.veces > 0, onClick = onClick),  // con el efecto de onda de Material
           verticalAlignment = Alignment.CenterVertically
       ) {
   ```

`PantallaValora` no sabe nada de navegación: solo avisa de qué nota se ha tocado (`onNota`). Quién decide adónde se va es la navegación, en el paso siguiente. Así la pantalla se puede usar y probar sola, como hasta ahora.

**15.4 · La pantalla de una nota.** Crea `PantallaNota.kt`, en el mismo paquete:

| Pieza | Qué hace |
|---|---|
| `PantallaNota(viewModel, nota, onVolver)` | Recibe el ViewModel (el mismo que la principal), la nota y qué hacer al volver |
| `estado.conNota(nota)` | Las valoraciones de esa nota, calculadas por el estado |
| `TopAppBar(…, navigationIcon = { IconButton(onClick = onVolver) { … } })` | La barra de arriba, con la flecha de volver a la izquierda del título |
| `Icons.AutoMirrored.Filled.ArrowBack` | La flecha de volver. *AutoMirrored*: en idiomas que se escriben de derecha a izquierda, se da la vuelta |
| `LazyColumn { items(valoraciones) { … } }` | Una lista que solo dibuja las filas que se ven: para cada valoración, una fila |
| `ListItem(leadingContent = …, headlineContent = …, supportingContent = …, trailingContent = …)` | Una fila estándar de Material: icono, título, subtítulo y algo a la derecha (aquí, las estrellas, con el `Estrellas` del paso 9) |

```kotlin
package com.example.valorapmdm

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// ── La pantalla de una nota: las valoraciones que la tienen (paso 15) ──
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNota(viewModel: ValoraViewModel, nota: Int, onVolver: () -> Unit) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val valoraciones = estado.conNota(nota)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nota $nota · ${estado.mostrada}ª evaluación") },
                // La flecha de volver, a la izquierda del título
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        // LazyColumn: una lista que solo dibuja las filas que se ven
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(valoraciones) { valoracion ->
                // ListItem: una fila estándar de Material, con icono, título y subtítulo
                ListItem(
                    leadingContent = { Icon(Icons.Filled.Person, contentDescription = null) },
                    headlineContent = { Text("Valoración ${valoracion.id}") },
                    supportingContent = { Text("${valoracion.nota} de 5") },
                    trailingContent = { Estrellas(valoracion.nota) }
                )
            }
        }
    }
}
```

**15.5 · La navegación.** Crea `Navegacion.kt`, en el mismo paquete:

```kotlin
package com.example.valorapmdm

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ── Las pantallas de la app y cómo se pasa de una a otra (paso 15) ─────
@Composable
fun NavegacionValora() {
    // El controlador de navegación: sabe en qué pantalla se está y guarda la pila de las anteriores
    val navController = rememberNavController()

    // Un solo ViewModel para las dos pantallas: se crea fuera del NavHost, y así
    // la pantalla de una nota ve las mismas valoraciones y la misma pestaña elegida
    val viewModel: ValoraViewModel = viewModel()

    // NavHost: cada composable(ruta) es una pantalla. La primera es "resumen"
    NavHost(navController = navController, startDestination = "resumen") {
        composable("resumen") {
            PantallaValora(
                viewModel = viewModel,
                // Al tocar una fila, se va a la ruta de esa nota: "nota/5"
                onNota = { nota -> navController.navigate("nota/$nota") }
            )
        }
        // {nota} es un argumento: la parte de la ruta que cambia
        composable("nota/{nota}") { entrada ->
            val nota = entrada.arguments?.getString("nota")?.toIntOrNull() ?: 0
            PantallaNota(
                viewModel = viewModel,
                nota = nota,
                // Volver: quita esta pantalla de la pila y se ve la anterior
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
```

**15.6 · MainActivity.** Ahora la app empieza por la navegación, que muestra la pantalla principal. En `MainActivity.kt`, cambia `PantallaValora()` por:

```kotlin
                // Las dos pantallas y cómo se pasa de una a otra (Navegacion.kt, paso 15)
                NavegacionValora()
```

**Comprueba que lo entiendes.** Contesta antes de abrir las respuestas:

1. ¿Por qué el ViewModel se crea en `NavegacionValora` y no dentro de cada pantalla?
2. ¿Cómo sabe `PantallaNota` qué nota mostrar?
3. ¿Por qué `PantallaValora` recibe `onNota` en lugar de llamar ella a `navController.navigate`?
4. Al volver de la pantalla de una nota, ¿se vuelven a descargar las valoraciones?

<details>
<summary>Respuestas</summary>

1. Para que las dos pantallas usen **el mismo**: la de una nota ve las valoraciones y la pestaña elegida de la principal. Si cada pantalla pidiera el suyo con `viewModel()` dentro del `NavHost`, cada destino tendría uno distinto, y el de la nota no sabría qué evaluación se está mostrando.
2. Por la ruta: `navigate("nota/5")` lleva el 5 en el texto, y `composable("nota/{nota}")` lo recoge en `entrada.arguments` y se lo pasa a `PantallaNota`.
3. Para que la pantalla no dependa de la navegación: solo avisa de qué nota se ha tocado, y quien la usa decide qué hacer. Así se puede seguir usando y probando sola, por ejemplo, en una vista previa.
4. No. `popBackStack` solo quita la pantalla de encima de la pila; la principal y el ViewModel siguen ahí, con sus datos.

</details>

**Probar el detalle.** Pulsa ▶:

| Nº | Prueba | Cómo | Resultado esperado |
|---|---|---|---|
| 1 | Una nota | En la 1ª evaluación, tocar la fila del **5** | Se abre «Nota 5 · 1ª evaluación» con **3** valoraciones (la 2, la 6 y la 10), cada una con «5 de 5» y cinco estrellas |
| 2 | Otra pestaña | Volver, pasar a la 2ª evaluación y tocar la fila del **5** | «Nota 5 · 2ª evaluación», con **6** valoraciones |
| 3 | Volver | La flecha de arriba, y después lo mismo con el botón «atrás» del móvil | Se vuelve a la principal, en la misma pestaña, sin descargar nada |
| 4 | Una nota sin valoraciones | En la 1ª evaluación, tocar la fila del **1** (0 veces) | No pasa nada |
| 5 | Girar | En la pantalla de una nota, girar el emulador | Sigue en la misma pantalla, con la misma lista |

**Comprueba que…** las cinco pruebas dan el resultado esperado, y que ValoraFlutter hace lo mismo con la misma nota (en ValoraFlutter, paso 6, pruebas 8 a 10).

---

## Si algo falla

| Qué pasa | Qué hacer |
|---|---|
| En Android Studio, al hacer el *sync* sale un error de `jlink` o del JDK | *File > Settings > Build, Execution, Deployment > Build Tools > Gradle* y, en **Gradle JDK**, elige el de Android Studio (`jbr`). Vuelve a hacer el *sync*. |
| En Android Studio, al compilar sale «jlink executable …`\.vscode\extensions\redhat.java-…\jlink.exe` does not exist» | El Gradle que compila no usa el Java de Android Studio, sino el de la extensión de Java de **VS Code**, que es un **JRE** (solo ejecuta programas, no trae `jlink`). Pasa si VS Code tiene abierta una carpeta que contiene proyectos Android y en ella aparecen o se abren archivos de Java (`.java`), por ejemplo, un proyecto de PROG. La extensión de Java de VS Code se activa entonces del todo e importa también los proyectos de Gradle de la carpeta, los Android: para eso deja un Gradle abierto en segundo plano con su JRE, y Android Studio, al compilar, reutiliza ese Gradle en lugar de abrir el suyo. Mientras en la carpeta no hay archivos de Java, no pasa nada, aunque VS Code lleve abierto todo el día. Para arreglarlo, en la carpeta que tienes abierta en VS Code crea el archivo `.vscode\settings.json` con `{ "java.import.gradle.enabled": false, "java.gradle.buildServer.enabled": "off", "gradle.autoDetect": "off" }` y recarga VS Code (`Ctrl + Mayús + P` → «Developer: Reload Window»): VS Code cierra su Gradle y ya no abre ninguno en esa carpeta. Después, vuelve a compilar en Android Studio. Si el error sigue, cierra VS Code y Android Studio, finaliza los `java.exe` que queden (Administrador de tareas, pestaña *Detalles*) y abre Android Studio otra vez. |
| Muchas cosas en rojo después del paso 2 | Falta el **Sync Now**. Si no aparece la barra amarilla: *File > Sync Project with Gradle Files*. |
| Algo en rojo en un archivo de Kotlin | Pon el cursor encima: el mensaje dice qué falta. Si es un `import`, **Alt + Intro** lo añade. Revisa también que la primera línea es `package com.example.valorapmdm`. |
| Error al compilar en `@Query` | El nombre de la tabla o de una columna no coincide con `Valora` (`valora`, `evaluacion`, `id`, `nota`). |
| Al pulsar ↻ sale siempre el aviso de «Sin conexión», también con conexión | Falta el permiso de Internet en el manifiesto (paso 3), o el nombre del servidor de la `URL_BASE` está mal escrito. Logcat (línea `E`) dice el motivo. |
| Sale el aviso «Error del servidor (…)» | El servidor ha respondido con un error que no es 404. El número dice cuál: 403, sin permiso; 429, demasiadas peticiones seguidas; 500 o más, un fallo del servidor. Espera unos minutos y vuelve a pulsar ↻. |
| Sale el aviso «Los datos descargados no tienen el formato esperado» | El archivo existe, pero no es una lista de `{ "id": …, "nota": … }`. Ábrelo en el navegador y compáralo con el formato del punto 2. |
| Al pulsar ↻ no aparece ninguna pestaña | Las tres evaluaciones han dado 404: la `URL_BASE` apunta a una carpeta sin archivos. Compruébala en el navegador (paso 6). |
| La app se cierra al abrirla | En Logcat, sin filtro, busca la línea roja `FATAL EXCEPTION`: debajo está el error y el archivo y la línea donde ocurrió. |
| El botón ▶ no hace nada y a su izquierda ya no pone «app» | Se ha borrado la configuración de ejecución (pasa al elegir **Delete** en el menú de los tres puntos de «app»). *Run > Edit Configurations…* → **+** → **Android App**; en *Name*, `app`; en *Module*, `ValoraPMDM.app`; **OK**. |
| Al instalar sale «Can't find service: package» o «Activity class … does not exist» | El emulador no ha terminado de arrancar. Espera a ver la pantalla de inicio y pulsa ▶ otra vez. Si se repite: *Device Manager* → los tres puntos del Pixel 9 → **Cold Boot Now**. |
| Las barras no se animan (paso 14) | Comprueba que la barra usa `progress = { proporcion }`, el valor animado, y no `fila.proporcion`. Si aun así cambian de golpe, mira que no esté activado **Remove animations** en *Settings → Accessibility*, ni a 0 las escalas de animación de las *Developer options* |
| Al tocar una fila no pasa nada, y tiene valoraciones | En `PantallaValora`, falta pasar `onClick = { onNota(fila.nota) }` a `FilaRecuento`, o `MainActivity` sigue mostrando `PantallaValora()` en lugar de `NavegacionValora()` |
| `Unresolved reference: NavHost` o `rememberNavController` | Falta la librería de navegación (paso 15.1) o el **Sync Now** |
| La pantalla de una nota sale vacía | Cada pantalla tiene un ViewModel distinto: el ViewModel tiene que crearse en `NavegacionValora`, fuera del `NavHost`, y pasarse a las dos |
| Las barras no crecen al abrir la app, pero sí al cambiar de pestaña | Falta `LaunchedEffect(Unit) { mostrada = true }`, o `targetValue` no usa `mostrada` |
| Después de cambiar un archivo en GitHub, la app sigue mostrando lo anterior | GitHub guarda los archivos en caché unos minutos. Espera y vuelve a pulsar ↻. |

## Los archivos del proyecto

Al terminar, así están los archivos en el disco, en la carpeta del proyecto (solo los principales). Es lo mismo que muestra el panel *Project* con la vista **Android**, pero con las carpetas reales: el paquete `com.example.valorapmdm` es, en el disco, la carpeta `java\com\example\valorapmdm`. Para verlo así en Android Studio, elige **Project** en el desplegable de arriba del panel, en lugar de **Android**.

```
C:\dam2627\pmdm\ValoraPMDM
├── app
│   ├── build.gradle.kts                                  las librerías de la app (paso 2)
│   └── src
│       ├── androidTest                                   pruebas automáticas (no se usan)
│       ├── main
│       │   ├── AndroidManifest.xml                       el permiso de Internet (paso 3)
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── valorapmdm                    los archivos de Kotlin
│       │   │               ├── ui
│       │   │               │   └── theme                 colores y letras (no se tocan)
│       │   │               ├── MainActivity.kt           el punto de entrada (pasos 10 y 15)
│   │   │               ├── Navegacion.kt             las dos pantallas y las rutas (paso 15)
│       │   │               ├── PantallaNota.kt           la lista de valoraciones de una nota (paso 15)
│   │   │               ├── PantallaValora.kt         la pantalla, solo dibuja (pasos 9, 14 y 15)
│       │   │               ├── ServicioValora.kt         Retrofit y la URL base (paso 6)
│       │   │               ├── Valora.kt                 ValoraJson, Valora y aValora (paso 4)
│       │   │               ├── ValoraDao.kt              las consultas de Room (paso 5)
│       │   │               ├── ValoraDatabase.kt         la base de datos (paso 5)
│       │   │               ├── ValoraUiState.kt          el estado y sus cálculos (pasos 7 y 15)
│       │   │               └── ValoraViewModel.kt        el ViewModel (paso 8)
│       │   └── res                                       iconos, colores y textos
│       └── test                                          pruebas automáticas (no se usan)
├── gradle
│   └── libs.versions.toml                                el catálogo de librerías (paso 2)
├── build.gradle.kts                                      los plugins del proyecto (paso 2)
└── settings.gradle.kts                                   el nombre del proyecto y sus módulos
```

## El proyecto terminado

El proyecto terminado, con los pasos 1 a 15, está en [`ValoraPMDM.zip`](ValoraPMDM.zip): sirve para comparar con el propio y para repasar.

1. Descárgalo y descomprímelo en `C:\dam2627\pmdm`: clic derecho → **Extraer todo…** → escribe `C:\dam2627\pmdm` → **Extraer**. Queda la carpeta `C:\dam2627\pmdm\ValoraPMDM`. Si ya tienes una carpeta con ese nombre, la tuya, cámbiale antes el nombre.
2. En Android Studio, **File → Open**, elige la carpeta `ValoraPMDM` y **OK**. Si pregunta si confías en el proyecto, **Trust Project**.
3. Espera a que termine el *sync* de Gradle (abajo a la derecha). La primera vez descarga las librerías y tarda unos minutos.
4. Arranca el emulador y pulsa ▶.

El zip no trae las carpetas que genera Android Studio al compilar (`build`, `.gradle`) ni `local.properties`, que dice dónde está el SDK de Android en cada ordenador: las crea él solo al abrir el proyecto.
