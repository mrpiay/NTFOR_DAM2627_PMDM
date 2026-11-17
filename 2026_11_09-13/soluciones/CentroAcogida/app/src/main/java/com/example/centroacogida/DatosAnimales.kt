// DatosAnimales.kt
package com.example.centroacogida

object DatosAnimales {
    val animales: List<Animal> = listOf(
        Animal(1, "Luna", "Perro", 3, R.drawable.ic_launcher_foreground, urgente = true,
            descripcion = "Muy cariñosa. Se lleva bien con niños."),
        Animal(2, "Misi", "Gato", 1, R.drawable.ic_launcher_foreground, urgente = false,
            descripcion = "Tranquila y curiosa."),
        Animal(3, "Toby", "Perro", 5, R.drawable.ic_launcher_foreground, urgente = false),
        Animal(4, "Nala", "Gato", 2, R.drawable.ic_launcher_foreground, urgente = true),
        Animal(5, "Rocky", "Perro", 8, R.drawable.ic_launcher_foreground, urgente = false),
        Animal(6, "Coco", "Conejo", 1, R.drawable.ic_launcher_foreground, urgente = false),
        Animal(7, "Kira", "Perro", 4, R.drawable.ic_launcher_foreground, urgente = true),
        Animal(8, "Simba", "Gato", 6, R.drawable.ic_launcher_foreground, urgente = false),
    )
}
