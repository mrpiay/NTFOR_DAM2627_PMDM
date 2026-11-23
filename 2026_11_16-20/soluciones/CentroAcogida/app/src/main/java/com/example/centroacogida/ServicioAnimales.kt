package com.example.centroacogida

// ServicioAnimales.kt: la lista de animales desde Internet, con Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

// La interfaz: cada función es una petición. Retrofit escribe el código que la hace.
interface ServicioAnimales {
    // GET a URL_BASE + "animales.json". suspend: se llama desde una corrutina
    @GET("animales.json")
    suspend fun animales(): List<Animal>
}

// Un solo objeto Retrofit para toda la app
object Red {
    // La parte común de todas las direcciones (termina en /)
    private const val URL_BASE =
        "https://raw.githubusercontent.com/mrpiay/NTFOR_DAM2627_PMDM/main/2026_11_16-20/actividades/"

    // ignoreUnknownKeys: si el JSON trae campos que Animal no tiene, se ignoran
    private val json = Json { ignoreUnknownKeys = true }

    val servicio: ServicioAnimales = Retrofit.Builder()
        .baseUrl(URL_BASE)
        // El conversor: pasa el JSON de la respuesta a objetos Animal
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ServicioAnimales::class.java)
}
