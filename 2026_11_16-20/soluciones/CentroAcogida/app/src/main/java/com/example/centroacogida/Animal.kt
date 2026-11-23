// Animal.kt
package com.example.centroacogida

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// @Serializable: kotlinx.serialization sabe pasar un Animal a JSON y al revés
@Serializable
data class Animal(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    // @Transient: no viene en el JSON; se usa el valor por defecto
    @Transient @DrawableRes val imagen: Int = R.drawable.ic_launcher_foreground,
    val urgente: Boolean,
    val descripcion: String = ""
)
