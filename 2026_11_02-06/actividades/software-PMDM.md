# Software · PMDM · semana del 2 al 6 de noviembre

Lo que hace falta esta semana para el proyecto «Centro de acogida 2.0». Todo se instala en **Windows**.

| Programa | Para qué | Ocupa |
|---|---|---|
| **Android Studio 2026.2** (trae su JDK, el SDK de Android, el emulador y Device Manager) | Abrir, compilar y ejecutar la app | unos 1,5 GB, más unos 4 GB del SDK |
| Un **emulador de móvil** (AVD *Pixel*) | Ejecutar la app | unos 2 GB |

Las bibliotecas del proyecto (ViewModel, Navigation…) no se instalan aparte: Android Studio las descarga en el *Sync*.

## Android Studio

1. Descárgalo de **developer.android.com/studio** (la última versión estable). Si ya lo tienes, comprueba en *Help → About* que es la 2026.2 o posterior; si no, *Help → Check for Updates*.
2. La primera vez que lo abras, el asistente descarga el **SDK de Android**: elige la instalación *Standard*, acepta las licencias y espera, que tarda.

## El emulador

1. *View → Tool Windows → Device Manager* → **+** → *Create Virtual Device*.
2. Elige un **Pixel** (categoría *Phone*) con la última imagen de sistema estable.
3. **Comprueba que** arranca y muestra la pantalla de inicio de Android.

## Si algo falla

| Qué pasa | Qué hacer |
|---|---|
| El *Sync* falla con un error de versión del plugin de Android (AGP) | Actualiza Android Studio: *Help → Check for Updates* |
| El *Sync* falla y el error habla de `jlink` o del JDK | *File → Settings → Build, Execution, Deployment → Build Tools → Gradle*: como **Gradle JDK**, el que trae Android Studio (`jbr`) |
| El emulador da un error de virtualización (VT-x / AMD-V) o no arranca | La virtualización tiene que estar activada en la BIOS/UEFI: compruébalo en *Administrador de tareas → Rendimiento → CPU → Virtualización* |
