package com.example.centroacogida

// Ubicacion.kt: dónde está el dispositivo y a qué distancia del centro
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// Dónde está el centro de acogida (la plaza del Obradoiro, en Santiago de Compostela)
const val CENTRO_LATITUD = 42.8806
const val CENTRO_LONGITUD = -8.5446

// Pide la ubicación actual. Devuelve null si no se puede (localización desactivada).
// Antes de llamarla hay que tener el permiso de ubicación: por eso @SuppressLint
@SuppressLint("MissingPermission")
suspend fun obtenerUbicacion(context: Context): Location? = suspendCancellableCoroutine { continuacion ->
    val gestor = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    // GPS si está activado; si no, la red (antenas y Wi-Fi)
    val proveedor = when {
        gestor.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
        gestor.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> null
    }
    if (proveedor == null) {
        continuacion.resume(null)
    } else {
        val cancelar = CancellationSignal()
        continuacion.invokeOnCancellation { cancelar.cancel() }
        LocationManagerCompat.getCurrentLocation(
            gestor, proveedor, cancelar, ContextCompat.getMainExecutor(context)
        ) { ubicacion -> continuacion.resume(ubicacion) }
    }
}

// Distancia en kilómetros desde una ubicación hasta el centro
fun distanciaAlCentroKm(ubicacion: Location): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        ubicacion.latitude, ubicacion.longitude, CENTRO_LATITUD, CENTRO_LONGITUD, resultado
    )
    return resultado[0] / 1000f
}
