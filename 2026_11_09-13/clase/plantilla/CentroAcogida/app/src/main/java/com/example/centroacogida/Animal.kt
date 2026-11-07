// Animal.kt
package com.example.centroacogida

import androidx.annotation.DrawableRes

data class Animal(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    @DrawableRes val imagen: Int,
    val urgente: Boolean,
    val descripcion: String = ""
)
