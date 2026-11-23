package com.example.centroacogida

// SolicitudDao.kt · Las consultas, escritas en SQL; Room genera el código
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {
    @Insert
    suspend fun insertar(solicitud: Solicitud)

    // Un Flow: la pantalla se actualiza sola cada vez que cambia la tabla
    @Query("SELECT * FROM solicitudes ORDER BY fecha DESC")
    fun todas(): Flow<List<Solicitud>>
}
