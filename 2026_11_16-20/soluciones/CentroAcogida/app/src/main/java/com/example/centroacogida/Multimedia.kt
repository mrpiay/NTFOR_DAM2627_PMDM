package com.example.centroacogida

// Multimedia.kt: procesar y guardar la foto, y reproducir un sonido
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.annotation.RawRes
import java.io.File

// Lado en píxeles de la foto guardada
private const val LADO = 300

// Calidad del JPEG, de 0 a 100: menos calidad, archivo más pequeño
private const val CALIDAD = 80

// El archivo de la foto de un animal, en la carpeta privada de la app
fun archivoFoto(context: Context, animalId: Int): File =
    File(context.filesDir, "foto_$animalId.jpg")

// PROCESAR: recorta el centro de la foto en un cuadrado y lo escala a LADO x LADO
fun recortarCuadrado(foto: Bitmap): Bitmap {
    val lado = minOf(foto.width, foto.height)
    val x = (foto.width - lado) / 2
    val y = (foto.height - lado) / 2
    val cuadrada = Bitmap.createBitmap(foto, x, y, lado, lado)
    return Bitmap.createScaledBitmap(cuadrada, LADO, LADO, true)
}

// CONVERTIR: guarda la foto como JPEG y devuelve el archivo
fun guardarComoJpeg(context: Context, foto: Bitmap, animalId: Int): File {
    val archivo = archivoFoto(context, animalId)
    archivo.outputStream().use { salida ->
        foto.compress(Bitmap.CompressFormat.JPEG, CALIDAD, salida)
    }
    return archivo
}

// Lee la foto guardada de un animal; null si todavía no tiene
fun cargarFoto(context: Context, animalId: Int): Bitmap? {
    val archivo = archivoFoto(context, animalId)
    return if (archivo.exists()) BitmapFactory.decodeFile(archivo.path) else null
}

// Reproduce un sonido de res/raw y libera el reproductor al terminar
fun reproducirSonido(context: Context, @RawRes sonido: Int) {
    val reproductor = MediaPlayer.create(context, sonido) ?: return
    reproductor.setOnCompletionListener { it.release() }
    reproductor.start()
}
