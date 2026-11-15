package com.example.centroacogida

// AcogidaUiState.kt
data class AcogidaUiState(
    val animales: List<Animal> = DatosAnimales.animales,
    val soloUrgentes: Boolean = false,
    val textoBusqueda: String = ""
    // TODO Paso 11.5: cargando y aviso
) {
    val animalesVisibles: List<Animal>
        get() = animales
            .filter { !soloUrgentes || it.urgente }
            .filter { it.nombre.contains(textoBusqueda.trim(), ignoreCase = true) }
}
