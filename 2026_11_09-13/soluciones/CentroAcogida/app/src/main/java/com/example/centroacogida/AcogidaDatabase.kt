package com.example.centroacogida

// AcogidaDatabase.kt · La base de datos (un archivo SQLite dentro de la app)
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Solicitud::class], version = 1, exportSchema = false)
abstract class AcogidaDatabase : RoomDatabase() {
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        @Volatile private var instancia: AcogidaDatabase? = null

        // Una sola base de datos para toda la app
        fun obtener(context: Context): AcogidaDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(context, AcogidaDatabase::class.java, "acogida.db")
                    .build()
                    .also { instancia = it }
            }
    }
}
