# Software · 2º DAM · PMDM · semana del 3 al 6 de noviembre

Qué hay que tener instalado para hacer el proyecto de esta semana, cómo instalarlo en Windows y cómo comprobar que funciona. Si algo no se puede instalar, cada apartado dice qué hacer mientras tanto: **nadie se queda parado por el software**.

**Índice**

- [Antes de empezar](#antes-de-empezar)
- [Base común: WSL2 con Ubuntu, Java y Python](#base-común-wsl2-con-ubuntu-java-y-python)
- [2º DAM · PMDM](#2º-dam--pmdm)
- [Problemas frecuentes](#problemas-frecuentes)

## Antes de empezar

- **Descarga siempre de la web oficial** de cada programa (las direcciones están en cada apartado). No uses páginas de descargas de terceros.
- **Permisos de administrador.** Casi todas las instalaciones los piden. Si tu ordenador no te los da, trabaja en pareja o en un equipo del aula y avisa al docente.
- **Espacio en disco.** Mira cuánto tienes libre (Explorador de archivos → *Este equipo*). Lo que ocupa cada cosa va indicado; son valores aproximados.
- **Virtualización activada.** WSL2 y el emulador de Android de este módulo necesitan que esté activada en la BIOS/UEFI. Para comprobarlo: *Administrador de tareas* (`Ctrl + Mayús + Esc`) → *Rendimiento* → *CPU* → **Virtualización: Habilitado**. Si pone *Deshabilitado*, avisa al docente antes de tocar nada en la BIOS.
- **Instalar desde la terminal (opcional).** Windows 10 y 11 traen `winget`. Donde se indica, puedes instalar con una sola orden en PowerShell en lugar de descargar el instalador.

## Base común: WSL2 con Ubuntu, Java y Python

**2º DAM (DI y PMDM).** WSL2 es un Ubuntu completo dentro de Windows. Lo usaremos como entorno de trabajo común para todo lo que se hace en la terminal: comandos de Linux, programas de consola en Java y scripts en Python, editados con Visual Studio Code. Lo que tiene ventanas, como Android Studio, sigue en Windows.

| Qué | Para qué | Ocupa |
|---|---|---|
| **WSL2 con Ubuntu 24.04** | La terminal de Linux de todos los módulos | unos 2 GB |
| **Java (OpenJDK 21) dentro de Ubuntu** | Compilar y ejecutar Java en la terminal | unos 300 MB |
| **Python 3 dentro de Ubuntu** (ya viene) y `python3-venv` | Ejecutar scripts y crear entornos virtuales | unos 50 MB |
| **Visual Studio Code** (en Windows) con la extensión **WSL** de Microsoft | Editar los archivos de Ubuntu desde Windows | unos 400 MB |

### Instalar WSL2 y Ubuntu

1. Abre **PowerShell como administrador** (clic derecho en el botón Inicio → *Terminal (Administrador)*) y escribe:

    ```
    wsl --install -d Ubuntu-24.04
    ```

2. Reinicia el ordenador si te lo pide. Al volver (o al abrir *Ubuntu 24.04* desde el menú Inicio), se abre Ubuntu y te pide un **nombre de usuario** y una **contraseña** (es la de Ubuntu, no la de Windows; al escribirla no se ve nada: es normal). Apúntala: la necesitarás para `sudo`.
3. **Comprueba que**, en PowerShell, `wsl -l -v` muestra `Ubuntu-24.04` con **VERSION 2**.

### Instalar Java y lo necesario de Python dentro de Ubuntu

En la terminal de Ubuntu (menú Inicio → *Ubuntu*):

```
sudo apt update
sudo apt install -y openjdk-21-jdk python3-venv python3-pip unzip
```

**Comprueba que** `java -version` muestra 21 y `python3 --version` muestra 3.12 o superior. `unzip` sirve para descomprimir archivos zip: en Ubuntu no viene instalado.

### Visual Studio Code con WSL

1. Instala **Visual Studio Code** en Windows (**code.visualstudio.com**) si no lo tienes.
2. En VS Code, *Extensiones* (`Ctrl + Mayús + X`): instala **WSL** (de Microsoft).
3. En la terminal de Ubuntu, crea una carpeta de trabajo y ábrela en VS Code:

    ```
    mkdir -p ~/dam && cd ~/dam
    code .
    ```

4. **Comprueba que** VS Code se abre y abajo a la izquierda pone **WSL: Ubuntu-24.04**. La primera vez tarda un poco: VS Code instala su parte dentro de Ubuntu. La terminal de VS Code (`Ctrl + Ñ`) es ya la de Ubuntu.

### Dónde guardar el trabajo

- Guarda tus proyectos **dentro de Ubuntu** (por ejemplo, en `~/dam`), no en `/mnt/c/...`: es mucho más rápido.
- Para ver esos archivos desde Windows (para entregarlos, por ejemplo), en el Explorador de archivos escribe `\\wsl$` y entra en `Ubuntu-24.04` → `home` → tu usuario.
- Para copiar una plantilla de Windows a Ubuntu: `cp /mnt/c/Users/<tu-usuario>/Downloads/<archivo>.zip ~/dam/` y después `unzip`.

### Si no puedes instalar

- **«WSL 2 requiere una actualización del kernel» o error de virtualización:** comprueba la virtualización (ver *Antes de empezar*) y ejecuta `wsl --update` en PowerShell como administrador.
- **WSL no arranca y dice que falta una característica de Windows** (por ejemplo, «Plataforma de máquina virtual»): `wsl --install` las activa solo, pero a veces hay que hacerlo a mano. Busca en el menú Inicio **«Activar o desactivar las características de Windows»**, marca **Plataforma de máquina virtual** y **Subsistema de Windows para Linux**, pulsa *Aceptar* y **reinicia**. No hace falta marcar Hyper-V.
- **Sin permisos de administrador:** trabaja en pareja o en un equipo del aula y avisa al docente.
- **No hay tiempo para instalarlo esta semana:** el proyecto de cada módulo se puede hacer también desde Windows, como indica su apartado. WSL se usará cada vez más en las próximas semanas.

## 2º DAM · PMDM

Proyecto «Centro de acogida 2.0»: ampliar con Kotlin y Jetpack Compose tu app del centro de acogida (la tarea «App centro de acogida») o la de la carpeta `plantilla` de `clase`, con ViewModel, navegación, un formulario, dos tamaños de pantalla y una base de datos con Room. Room no se instala aparte: son dependencias que el proyecto añade (paso 9.1) y Android Studio descarga en el *Sync*.

La plantilla está probada con **Android Studio 2026.2**. Si tienes una versión anterior, actualízala (*Help → Check for Updates*): con una antigua, el *Sync* de la plantilla puede fallar.

**En Windows:** Android Studio y el emulador funcionan en Windows; WSL no se usa en este proyecto.

| Programa | Para qué | Ocupa |
|---|---|---|
| **Android Studio** (incluye el SDK de Android, el emulador y Device Manager) | Crear, compilar y ejecutar la app | unos 1,5 GB, más unos 4 GB del SDK |
| **Dos dispositivos virtuales (AVD):** un móvil Pixel y una tableta, con la misma imagen de sistema | Probar la app en dos tamaños de pantalla | unos 2–4 GB |
| Alternativa: tu **móvil Android** con la depuración USB activada | Probar la app sin emulador | — |

### Android Studio

1. Descárgalo de **developer.android.com/studio** (la última versión estable). O: `winget install Google.AndroidStudio`
2. La primera vez que lo abras, el asistente descarga el **SDK de Android**: elige la instalación *Standard*, acepta las licencias y espera (puede tardar bastante: hazlo antes de clase si puedes).
3. **Comprueba que** puedes crear un proyecto *Empty Activity* (Kotlin y Compose) y que compila sin errores (*Build → Make Project*).

### Emuladores (AVD)

1. *View → Tool Windows → Device Manager* → **+** → *Create Virtual Device*.
2. Crea un **Pixel** (categoría *Phone*) con la última imagen de sistema estable.
3. Crea una **tableta** (categoría *Tablet*) con **la misma imagen**, para no descargar otra.
4. **Comprueba que** los dos arrancan y muestran la pantalla de inicio de Android.

### Si no puedes instalar

- **El emulador no arranca o va muy lento:** comprueba que la virtualización está habilitada (ver *Antes de empezar*) y cierra programas pesados. Mientras, usa tu móvil con la **depuración USB** (*Ajustes → Información del teléfono →* pulsa 7 veces *Número de compilación* → *Opciones de desarrollador → Depuración USB*) o las vistas previas `@Preview` de Android Studio.
- **Error de Gradle al hacer el Sync** (habla de `jlink` o del JDK): en *File → Settings → Build, Execution, Deployment → Build Tools → Gradle* elige como **Gradle JDK** el que trae Android Studio (`jbr`), como en el Paso 0.
- **Sin espacio para el SDK y los emuladores:** usa una sola imagen de sistema para los dos AVD y borra los AVD que no uses.
- **Sin AVD de tableta:** en el Paso 8 usa la vista previa con otro ancho que trae el proyecto.
- **Sin Android Studio:** trabaja en pareja y repasa el código del proyecto con tu compañero.

## Problemas frecuentes

| Problema | Qué hacer |
|---|---|
| Una orden (`java`, `python`, `winget`…) «no se reconoce» | Cierra y vuelve a abrir la terminal después de instalar. Si sigue, el programa no está en el PATH: reinstálalo marcando la opción de añadir al PATH. |
| El instalador pide permisos de administrador y no los tienes | Avisa al docente y trabaja en pareja o en un equipo del aula. |
| La descarga es muy lenta | Empieza por Android Studio, que es lo más grande, y mientras haz la parte del proyecto que no lo necesita. |
| El emulador da un error de virtualización (VT-x / AMD-V) | La virtualización está desactivada en la BIOS/UEFI. Avisa al docente; no cambies otras opciones de la BIOS. |
| Te falta espacio en disco | Borra descargas que ya no necesites. |
