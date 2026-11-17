package com.example.centroacogida

// AcogidaViewModel.kt
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// AndroidViewModel para tener el contexto de la aplicación y abrir la base de datos
class AcogidaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AcogidaDatabase.obtener(application).solicitudDao()

    // Las solicitudes guardadas en Room, como estado para la interfaz
    val solicitudes: StateFlow<List<Solicitud>> = dao.todas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiState = MutableStateFlow(AcogidaUiState())
    val uiState: StateFlow<AcogidaUiState> = _uiState.asStateFlow()

    fun cambiarSoloUrgentes(valor: Boolean) {
        _uiState.update { it.copy(soloUrgentes = valor) }
    }

    fun cambiarBusqueda(texto: String) {
        _uiState.update { it.copy(textoBusqueda = texto) }
    }

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

    fun enviarSolicitud(animal: Animal?) {
        val f = _formulario.value
        _formulario.update { it.copy(intentadoEnviar = true, enviado = it.esValido) }
        // Si es válida, se guarda en Room, en segundo plano (una corrutina)
        if (f.esValido && animal != null) {
            viewModelScope.launch {
                dao.insertar(
                    Solicitud(
                        animalId = animal.id, animalNombre = animal.nombre,
                        nombre = f.nombre, email = f.email, telefono = f.telefono
                    )
                )
            }
        }
    }

    fun reiniciarFormulario() {
        _formulario.value = FormularioUiState()
    }
}
