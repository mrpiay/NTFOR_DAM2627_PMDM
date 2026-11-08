package com.example.centroacogida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// PantallaSolicitud.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSolicitud(
    animal: Animal?,
    form: FormularioUiState,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onAceptaChange: (Boolean) -> Unit,
    onEnviar: () -> Unit,
    onVolver: () -> Unit,
    onTerminar: () -> Unit
) {
    // Los errores solo se muestran después de pulsar «Enviar»
    val errorNombre = if (form.intentadoEnviar) form.errorNombre else null
    val errorEmail = if (form.intentadoEnviar) form.errorEmail else null
    val errorTelefono = if (form.intentadoEnviar) form.errorTelefono else null
    val errorCondiciones = if (form.intentadoEnviar) form.errorCondiciones else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adoptar a ${animal?.nombre ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (form.enviado) {
                Text(
                    "Solicitud enviada. El centro se pondrá en contacto contigo.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = onTerminar, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver a la lista")
                }
            } else {
                OutlinedTextField(
                    value = form.nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre y apellidos") },
                    singleLine = true,
                    isError = errorNombre != null,
                    supportingText = { if (errorNombre != null) Text(errorNombre) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = form.email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = errorEmail != null,
                    supportingText = { if (errorEmail != null) Text(errorEmail) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = form.telefono,
                    onValueChange = onTelefonoChange,
                    label = { Text("Teléfono") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = errorTelefono != null,
                    supportingText = { if (errorTelefono != null) Text(errorTelefono) },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = form.aceptaCondiciones, onCheckedChange = onAceptaChange)
                    Text("Acepto las condiciones de adopción")
                }
                if (errorCondiciones != null) {
                    Text(errorCondiciones, color = MaterialTheme.colorScheme.error)
                }
                Button(onClick = onEnviar, modifier = Modifier.fillMaxWidth()) {
                    Text("Enviar")
                }
            }
        }
    }
}
