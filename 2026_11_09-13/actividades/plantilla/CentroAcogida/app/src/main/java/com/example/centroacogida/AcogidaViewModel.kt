package com.example.centroacogida

// AcogidaViewModel.kt
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AcogidaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AcogidaUiState())
    val uiState: StateFlow<AcogidaUiState> = _uiState.asStateFlow()

    fun cambiarSoloUrgentes(valor: Boolean) {
        _uiState.update { it.copy(soloUrgentes = valor) }
    }

    // TODO Paso 10: cambiarBusqueda

    fun buscarAnimal(id: Int): Animal? =
        _uiState.value.animales.find { it.id == id }

    private val _formulario = MutableStateFlow(FormularioUiState())
    val formulario: StateFlow<FormularioUiState> = _formulario.asStateFlow()

    fun cambiarNombre(valor: String) = _formulario.update { it.copy(nombre = valor) }

    fun cambiarEmail(valor: String) = _formulario.update { it.copy(email = valor.trim()) }

    fun cambiarTelefono(valor: String) {
        // Solo se aceptan cifras y como mucho 9
        if (valor.all { it.isDigit() } && valor.length <= 9) {
            _formulario.update { it.copy(telefono = valor) }
        }
    }

    fun cambiarAcepta(valor: Boolean) = _formulario.update { it.copy(aceptaCondiciones = valor) }

    fun enviarSolicitud() {
        _formulario.update { it.copy(intentadoEnviar = true, enviado = it.esValido) }
    }

    fun reiniciarFormulario() {
        _formulario.value = FormularioUiState()
    }
}
