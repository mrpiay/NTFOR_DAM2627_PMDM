package com.example.centroacogida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// PantallaLista.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    uiState: AcogidaUiState,
    onCambiarUrgentes: (Boolean) -> Unit,
    onAnimalClick: (Animal) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Centro de acogida") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // TODO Paso 10: el campo «Buscar por nombre»
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Adopciones urgentes", modifier = Modifier.weight(1f))
                Switch(checked = uiState.soloUrgentes, onCheckedChange = onCambiarUrgentes)
            }
            // TODO Paso 8: cambiar la LazyColumn por una LazyVerticalGrid adaptativa.
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.animalesVisibles, key = { it.id }) { animal ->
                    TarjetaAnimal(animal = animal, onClick = { onAnimalClick(animal) })
                }
            }
        }
    }
}


// TODO Paso 8: añadir una vista previa con ancho de tableta.
