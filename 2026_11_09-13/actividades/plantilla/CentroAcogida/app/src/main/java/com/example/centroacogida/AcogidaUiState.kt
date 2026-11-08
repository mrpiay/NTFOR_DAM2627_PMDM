package com.example.centroacogida

// AcogidaUiState.kt
data class AcogidaUiState(
    val animales: List<Animal> = DatosAnimales.animales,
    val soloUrgentes: Boolean = false
) {
    val animalesVisibles: List<Animal>
        get() = if (soloUrgentes) animales.filter { it.urgente } else animales
}
