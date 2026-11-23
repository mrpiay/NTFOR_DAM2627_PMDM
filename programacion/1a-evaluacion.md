# 1ª evaluación · PMDM

[← Programación](README.md)

Cómo se calcula la nota de la 1ª evaluación, qué contenidos entran en los exámenes y planificación semanal. La planificación empieza el 2 de noviembre; lo que ya evaluó Julia (exámenes y tareas de Teams) está en la fila «Antes del 2 nov».

**En este documento:** [Cómo se evalúa](#cómo-se-evalúa-de-dónde-sale-la-nota) · [Qué entra en los exámenes](#qué-entra-en-los-exámenes) · [Rúbricas](#rúbricas) · [Planificación semanal](#planificación-semanal) · [Notas](#notas)

## Cómo se evalúa: de dónde sale la nota

Porcentajes sobre la nota de la evaluación: entre todas las partes suman 100 %.

| Parte | Peso | Qué incluye |
|---|---|---|
| **P** · exámenes parciales | 40 % | Lo evaluado por Julia: P1 (Julia) 12,7 %, «Tarea 1» (Julia) 2,5 %<br>P2 · vie 4 dic: 24,8 % |
| **A** · actividades guiadas | 20 % | 3 actividades, 6,7 % cada una. Nivel de implementación (50 %) y de comprensión (50 %) |
| **F** · examen final | 30 % | vie 11 dic |
| **R** · autoevaluación | 10 % | Una sola, la semana del examen final: una tarea de Teams con la [plantilla de autoevaluación](autoevaluacion-1a.xlsx) |
| **C** · cuestionarios | — | 3 cuestionarios. No tienen nota propia, pero su entrega es obligatoria: sin todos ellos entregados, la R es un 0 |

## Qué entra en los exámenes

Los porcentajes de esta sección no son partes de la nota: dicen cuánto pesa cada unidad y cada contenido **dentro de los exámenes**. Con ellos se reparte el 40 % de los exámenes parciales (P) entre lo que evaluó Julia y el examen parcial, y valen igual para el examen final (F), que pregunta todas las unidades: los contenidos con más porcentaje tienen preguntas que valen más. Cada porcentaje es la suma de los pesos de sus criterios de evaluación en la programación oficial. Las actividades guiadas (A) y la autoevaluación (R) cuentan aparte, como se indica en «Cómo se evalúa».

### UD1 · Tecnologías y entornos de desarrollo para dispositivos móviles

Pesa un 15,9 % en los exámenes (10 % del módulo).

| Contenido | Peso en la unidad |
|---|---|
| Limitaciones, configuraciones, perfiles y estructura de una app | 50 % |
| Entorno, modificar una app y emuladores | 40 % |
| Tecnologías de desarrollo: nativas y multiplataforma (Flutter) | 10 % |

### UD2 · Desarrollo de aplicaciones móviles e interfaces de usuario

Pesa un 31,8 % en los exámenes (20 % del módulo).

| Contenido | Peso en la unidad |
|---|---|
| Kotlin y Compose: estructura de clases e interfaz | 75 % |
| Pruebas en emuladores y documentar | 25 % |

### UD3 · Persistencia, conectividad y servicios del dispositivo móvil

Pesa un 28,6 % en los exámenes (18 % del módulo).

| Contenido | Peso en la unidad |
|---|---|
| Persistencia: Room y preferencias | 20 % |
| Permisos, tareas en segundo plano y localización | 20 % |
| Conexiones HTTP y sensores | 40 % |
| Bluetooth y desplegar en un móvil real | 20 % |

### UD4 · Desarrollo de aplicaciones multimedia

Pesa un 23,8 % en los exámenes (22 % del módulo; en la 1ª evaluación cuenta un 15 %, lo que se da).

| Contenido | Peso en la unidad |
|---|---|
| Captura, procesado y conversión de imágenes | 43 % |
| Reproducción de audio y vídeo, eventos y animaciones | 28 % |
| Desplegar, depurar y documentar | 29 % |

## Rúbricas

Cómo se califica cada parte de la evaluación.

### Exámenes parciales (P) y examen final (F)

En papel. Cada pregunta indica los puntos que vale: los contenidos con más porcentaje tienen preguntas que valen más. La rúbrica de cada examen se publica aquí antes del examen.

### Actividades guiadas (A)

Se califican una vez terminadas, por su **nivel de implementación y comprensión**, y cómo depende del tipo de actividad:

- **Proyecto de progresión continuada** (un mismo proyecto que avanza de una actividad a la siguiente): se califica **hasta dónde se ha implementado la solución final del proyecto**, no cada actividad por separado. Todas sus actividades tienen la misma nota.
- **Actividad independiente:** se califica su propio resultado.

La nota tiene dos mitades:

- **Implementación (50 %):** con el proyecto propio, en el ordenador, se muestran, por orden, las comprobaciones de las tablas de abajo: 12 por proyecto o actividad, 3 en cada nivel (del 1, lo más básico, al 4). Cada comprobación que sale bien suma por sí misma, aunque falle alguna de un nivel anterior: la nota de implementación es las que salen bien entre 12, por 10.
- **Comprensión (50 %):** cuánto se sabe explicar. Se hacen 4 preguntas sobre el diseño y la arquitectura, una por nivel, sobre lo que se muestra en ese nivel; se hacen las cuatro aunque no se haya alcanzado el nivel. Cada respuesta bien dada vale 2,5. De qué trata cada pregunta, en las tablas de abajo.

#### A1–A3 · Proyecto «Centro de acogida 2.0»

Proyecto de progresión continuada: A1, A2 y A3 tienen la misma nota, la del proyecto entero.

**Implementación** (12 comprobaciones; cada una que sale bien suma 10/12)

| Nivel | Muestra que… |
|---|---|
| **1** | • el interruptor «Adopciones urgentes» filtra la lista y no pierde su valor al girar<br>• al pulsar una tarjeta se abre el detalle de ese animal<br>• el formulario «Solicitar adopción» marca los errores y confirma la solicitud |
| **2** | • la app se adapta al móvil en vertical (una columna) y a la tableta o en horizontal (varias)<br>• una solicitud enviada aparece en «Mis solicitudes»<br>• sigue ahí al cerrar y volver a abrir la app |
| **3** | • al abrir, la lista se carga de Internet: «Lista actualizada: 10 animales», con Bruno y Lola<br>• sin conexión, sale el aviso y la app no se cierra<br>• agitar el móvil, o el botón de recargar, vuelve a cargar la lista |
| **4** | • la foto de un animal se ve y sigue ahí al volver a entrar<br>• suena el sonido al enviar una solicitud<br>• «Adopción urgente» se anima en la lista |

**Comprensión** (una pregunta por nivel; se hacen las cuatro)

| Nivel | La pregunta trata sobre… |
|---|---|
| 1 | Cómo se conserva el estado al girar |
| 2 | Cómo guarda Room las solicitudes |
| 3 | Por qué la descarga va en una corrutina y qué pasa sin conexión |
| 4 | Dónde se guarda la foto y por qué no se pierde |

| Comprensión | Nota |
|---|---|
| Las 4 preguntas, bien respondidas | 10 |
| 3 preguntas | 7,5 |
| 2 preguntas | 5 |
| 1 pregunta | 2,5 |
| Ninguna | 0 |

La nota es la media de las dos. El mínimo de 4 se aplica a la media de las actividades de la evaluación.

### Cuestionarios (C) y autoevaluación (R)

Hay un cuestionario cada semana en la que se facilitan apuntes o actividades guiadas nuevas, con preguntas sobre ellos. Su entrega es obligatoria, hasta el lunes siguiente a su semana, salvo que se indique otra fecha (ver las notas de la [1ª evaluación](#cómo-se-evalúa-de-dónde-sale-la-nota)). No tienen nota propia, pero son un requisito de la autoevaluación: sin todos los cuestionarios entregados, la autoevaluación (R) es un 0.

La autoevaluación (R) se hace una sola vez, la semana del examen final, con la [plantilla de autoevaluación](autoevaluacion-1a.xlsx) en una tarea de Teams. Forma el 10 % de la nota. La mitad es la hoja completa: un nivel en cada tema, un tipo de fallo donde corresponde y una frecuencia en cada hábito de estudio. La otra mitad es la reflexión final, si es concreta: nombra los temas o los hábitos que más fallan y qué se va a hacer, en concreto, para mejorarlos. Requisito: tener entregados todos los cuestionarios (C); si falta alguno, la R es un 0. Lo marcado en cada tema y en cada hábito no cambia la nota.

## Planificación semanal

| Semana | Qué se trabaja | Cómo se evalúa | Exámenes |
|---|---|---|---|
| Antes del 2 nov<br>[repaso Julia](../repaso-julia/apuntes)<br>[examen de octubre](../examen-julia/enunciado.md) | Lo evaluado por Julia | «Tarea 1» (Julia) (2,5 %) | **P1 (Julia)** (12,7 %) |
| [2–6 nov](../2026_11_02-06)<br>[apuntes](../2026_11_02-06/apuntes) · [actividades](../2026_11_02-06/actividades) | UD1 · Limitaciones, configuraciones, perfiles y estructura de una app<br>UD1 · Entorno, modificar una app y emuladores<br>UD1 · Tecnologías de desarrollo: nativas y multiplataforma (Flutter)<br>UD2 · Kotlin y Compose: estructura de clases e interfaz<br>UD2 · Pruebas en emuladores y documentar | A1 (6,7 %), C1 | — |
| [9–13 nov](../2026_11_09-13)<br>[apuntes](../2026_11_09-13/apuntes) · [actividades](../2026_11_09-13/actividades) | UD3 · Persistencia: Room y preferencias<br>UD3 · Permisos, tareas en segundo plano y localización<br>UD3 · Conexiones HTTP y sensores: la teoría<br>UD3 · Bluetooth y desplegar en un móvil real: la teoría<br>UD4 · Un permiso y la cámara | A2 (6,7 %), C2 | — |
| [16–20 nov](../2026_11_16-20)<br>[apuntes](../2026_11_16-20/apuntes) · [actividades](../2026_11_16-20/actividades) | UD1 · Multiplataforma (Flutter): la teoría<br>UD2 · Probar la app y documentarla<br>UD3 · La ubicación, con su permiso; el segundo plano (WorkManager) y los mapas, en teoría<br>UD3 · Cargar los animales desde Internet (Retrofit) y un sensor: agitar para refrescar<br>UD3 · Generar el APK de la app; Bluetooth, en teoría<br>UD4 · La foto del animal: reducirla a JPEG y mostrarla; los formatos, en teoría<br>UD4 · Un sonido al adoptar y una animación en la tarjeta; el vídeo, en teoría<br>UD4 · Generar el APK, depurar con Logcat, probar y documentar | A3 (6,7 %), C3 | — |
| [23–27 nov](../2026_11_23-27)<br>[actividades](../2026_11_23-27/actividades) | UD1 · Flutter en la práctica: instalarlo, el lenguaje Dart y una app (ValoraFlutter) | — | — |
| 30 nov–4 dic | Repaso (mar 1 y mié 2) | — | **P2** (24,8 %), vie 4 dic |
| 7–11 dic | Repaso (mié 9) | R (10 %) | **F** (30 %), vie 11 dic |
| 14–18 dic | UD5 · Arquitectura y motores de juegos | Cierre de la 1ª evaluación · jue 17 | — |

## Notas

- **Cuestionarios (C).** Hay uno cada semana en la que se facilitan apuntes o actividades guiadas nuevas, con preguntas sobre ellos. Su entrega es obligatoria, hasta el lunes siguiente a su semana, salvo que se indique otra fecha (ver las notas). No tienen nota propia, pero sin todos los cuestionarios entregados, la autoevaluación (R) es un 0.
- **Autoevaluación (R).** La R es de reflexión. Una sola, la semana del examen final, en una tarea de Teams: la [plantilla de autoevaluación](autoevaluacion-1a.xlsx), que cada estudiante rellena en su propia copia. Por cada tema de cada unidad, el nivel de comprensión y el tipo de fallo principal; la frecuencia de 15 hábitos de estudio, y una reflexión general sobre qué hacer para mejorar en la 2ª evaluación. Forma el 10 % de autoevaluación del apartado 6.1 de la programación oficial. La mitad de su nota es la hoja completa; la otra mitad, la reflexión, si es concreta. Requisito: tener entregados todos los cuestionarios (C); si falta alguno, la R es un 0. Lo marcado en cada tema y en cada hábito no cambia la nota.
- **Exámenes escritos** (parciales, **P**, y final, **F**). Son en papel e incluyen preguntas similares a las de los cuestionarios. Cada pregunta indica los puntos que vale: los contenidos con más porcentaje tienen preguntas que valen más.
- **Actividades guiadas (A).** Se califican, una vez terminadas, por su nivel de implementación y comprensión, al 50 %. Si son partes de un proyecto de progresión continuada, se califica hasta dónde se ha implementado la solución final del proyecto, con la misma nota para todas; si son independientes, cada una por su resultado. Implementación: 12 comprobaciones ordenadas en 4 niveles; cada una que sale bien suma 10/12. Comprensión: 4 preguntas sobre el diseño y la arquitectura, una por nivel (2,5 cada una); qué pide cada nivel, en las [rúbricas](#rúbricas). Detalle, en las [rúbricas](#rúbricas). El mínimo de 4 se aplica a la media de las actividades de la evaluación.
- El C1 se entrega junto con el C2, el lunes 16 de noviembre.
- En la 1ª evaluación, la semana del 16 al 20 se toca cada tema con un caso práctico corto en la app del centro de acogida (Internet, un sensor, la ubicación, la foto, un sonido y una animación, generar el APK y depurarla), con el código técnico ya hecho; el segundo plano, Bluetooth, los mapas y el vídeo se dan en teoría, con preguntas de conceptos y criterio en el examen. La app integrada de la UD4 se aplaza al final del curso; la UD4 cuenta en la 1ª evaluación con el 68 % de su peso.
- «App Centro Acogida» no se entregó: sus contenidos se evalúan con la P1 (Julia) y la P2.
- La semana del 23 al 27 se dedica a Flutter (UD2), con tres proyectos guiados, en orden: Hola Flutter, ValoraDart y ValoraFlutter. No son actividades guiadas (A): no tienen nota ni cuestionario. Lo que se trabaja en ellos (tecnologías de desarrollo nativas y multiplataforma, CA1.2) entra en el examen parcial P2 y en el final. La guía de ValoraPMDM, con el detalle de una nota (paso 15), queda para repasar la evaluación.
- El examen parcial pregunta todo lo de la 1ª evaluación que no evaluó Julia, aunque sea de forma breve. Lo que ya evaluó Julia (fila «Antes del 2 nov» de la planificación semanal) cuenta con esa nota; si no se hizo ese examen o no se entregó esa tarea, en esa parte se obtiene un 5. El examen final pregunta toda la evaluación.
- Mínimo de 4 en cada examen parcial, en la media de las actividades guiadas y en el final para aplicar la media (apartado 6.1 de la programación oficial).
