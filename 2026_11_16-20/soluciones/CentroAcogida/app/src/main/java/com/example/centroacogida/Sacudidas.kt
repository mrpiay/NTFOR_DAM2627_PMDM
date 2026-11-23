package com.example.centroacogida

// Sacudidas.kt: avisa cuando se agita el móvil, con el acelerómetro
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlin.math.sqrt

// Cuántas veces la gravedad tiene que medir la aceleración para contar como sacudida
private const val UMBRAL = 2.7f

// Tiempo mínimo entre dos sacudidas, para no contar varias por una sola
private const val ESPERA_MS = 1000L

@Composable
fun AlAgitar(onSacudida: () -> Unit) {
    val context = LocalContext.current
    // Siempre la última versión de la acción, aunque la pantalla se vuelva a dibujar
    val accion by rememberUpdatedState(onSacudida)

    // DisposableEffect: registra el sensor al mostrar la pantalla y lo quita al salir
    DisposableEffect(Unit) {
        val gestor = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val acelerometro = gestor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var ultima = 0L

        val oyente = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val (x, y, z) = event.values
                // Módulo de la aceleración, en «gravedades»: en reposo vale 1
                val g = sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH
                val ahora = System.currentTimeMillis()
                if (g > UMBRAL && ahora - ultima > ESPERA_MS) {
                    ultima = ahora
                    accion()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Si el dispositivo no tiene acelerómetro, no se registra nada
        if (acelerometro != null) {
            gestor.registerListener(oyente, acelerometro, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { gestor.unregisterListener(oyente) }
    }
}
