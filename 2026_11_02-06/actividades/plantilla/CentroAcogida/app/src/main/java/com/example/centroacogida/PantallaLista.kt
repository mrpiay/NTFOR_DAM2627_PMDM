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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// PantallaLista.kt
// TODO Paso 5: la pantalla recibirá el estado (uiState) y el evento onCambiarUrgentes
//              en lugar de guardar el estado con remember.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(onAnimalClick: (Animal) -> Unit) {
    // Estado de la pantalla: se pierde al girar el móvil (lo verás en el paso 4)
    var soloUrgentes by remember { mutableStateOf(false) }
    val visibles = if (soloUrgentes) {
        DatosAnimales.animales.filter { it.urgente }
    } else {
        DatosAnimales.animales
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Centro de acogida") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Adopciones urgentes", modifier = Modifier.weight(1f))
                Switch(checked = soloUrgentes, onCheckedChange = { soloUrgentes = it })
            }
            // TODO Paso 8: cambiar la LazyColumn por una LazyVerticalGrid adaptativa.
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(visibles, key = { it.id }) { animal ->
                    TarjetaAnimal(animal = animal, onClick = { onAnimalClick(animal) })
                }
            }
        }
    }
}

// TODO Paso 8: añadir una vista previa con ancho de tableta (opcional).
