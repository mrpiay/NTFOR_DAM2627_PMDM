package com.example.centroacogida

// Solicitud.kt · Una tabla de Room (cada solicitud enviada es una fila)
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val animalId: Int,
    val animalNombre: String,
    val nombre: String,
    val email: String,
    val telefono: String,
    val fecha: Long = System.currentTimeMillis()
)
