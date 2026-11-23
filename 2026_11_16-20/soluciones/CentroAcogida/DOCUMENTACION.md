# Centro de acogida

## Qué hace

App Android para consultar los animales de un centro de acogida y solicitar su adopción. Es para las personas que quieren adoptar: ven los animales, su ficha y a qué distancia está el centro, y envían su solicitud desde el móvil.

## Cómo se usa

- **Ver los animales:** al abrir la app sale la lista, descargada de Internet.
- **Buscar:** escribir parte del nombre en «Buscar por nombre».
- **Ver solo los urgentes:** activar «Adopciones urgentes».
- **Recargar la lista:** agitar el móvil o pulsar la flecha circular de arriba.
- **Ver la distancia al centro:** pulsar «¿A qué distancia está el centro?» y permitir la ubicación.
- **Hacer la foto de un animal:** en su ficha, «Hacer una foto de…» y permitir la cámara.
- **Solicitar una adopción:** en su ficha, «Solicitar adopción», rellenar el formulario y «Enviar».
- **Ver mis solicitudes:** «Mis solicitudes», arriba a la derecha.

## Cómo se compila e instala

Se abre la carpeta `CentroAcogida` en Android Studio (*File > Open*), se espera al *Sync* de Gradle y se ejecuta con **Run** en un emulador o en un móvil con la depuración USB activada. El APK de prueba se genera con *Build > Generate App Bundles or APKs > Generate APKs* y queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Permisos que pide y para qué

| Permiso | Para qué | Si se deniega |
|---|---|---|
| Internet | Descargar la lista de animales | No se pide: se concede al instalar. Sin conexión, la app avisa y muestra los animales que trae |
| Cámara | Hacer la foto de un animal | La app avisa y no hace la foto; lo demás funciona |
| Ubicación | Calcular la distancia al centro (basta con la aproximada) | La app avisa y no calcula la distancia; lo demás funciona |

## Pruebas

En el emulador Pixel 9 (Android 17).

| Prueba | Cómo | Resultado esperado | Resultado |
|---|---|---|---|
| Carga desde Internet | Abrir la app con conexión | «Lista actualizada: 10 animales», con Bruno y Lola | Correcto |
| Sin conexión | Modo avión y recargar | «No se ha podido actualizar la lista: se muestran los animales guardados en la app» y 8 animales; la app no se cierra | Correcto |
| Agitar | Línea de la terminal (`emu sensor set acceleration`) | Se recarga la lista; en Logcat, otra línea «Animales cargados de Internet: 10» | Correcto |
| Distancia | Con la ubicación por defecto del emulador (California) | Unos 8.785 km | Correcto |
| Permiso denegado | *Don't allow* en la ubicación | «Sin permiso de ubicación no se puede calcular la distancia.» | Correcto |
| Foto | Hacer la foto, cerrar la app y volver | «Foto guardada: foto_1.jpg, 4 KB»; la foto sigue al volver | Correcto |
| Sonido | Enviar una solicitud | Suena una vez | Correcto |
| Giro de pantalla | Girar con la lista cargada y el aviso de la distancia | Se mantienen la lista y los avisos | Correcto |
