package com.example.centroacogida

// FormularioUiState.kt
data class FormularioUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val aceptaCondiciones: Boolean = false,
    val intentadoEnviar: Boolean = false,
    val enviado: Boolean = false
) {
    val errorNombre: String?
        get() = if (nombre.isBlank()) "Escribe tu nombre" else null

    val errorEmail: String?
        get() = if (!email.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) "Correo no válido" else null

    val errorTelefono: String?
        get() = if (telefono.length != 9) "El teléfono debe tener 9 cifras" else null

    val errorCondiciones: String?
        get() = if (!aceptaCondiciones) "Debes aceptar las condiciones" else null

    val esValido: Boolean
        get() = errorNombre == null && errorEmail == null &&
                errorTelefono == null && errorCondiciones == null
}
