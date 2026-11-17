package com.example.centroacogida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.tooling.preview.Preview
import com.example.centroacogida.ui.theme.CentroAcogidaTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    onAnimalClick: (Animal) -> Unit,
    onVerSolicitudes: () -> Unit = {},
    onBuscar: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de acogida") },
                actions = { TextButton(onClick = onVerSolicitudes) { Text("Mis solicitudes") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = uiState.textoBusqueda,
                onValueChange = onBuscar,
                label = { Text("Buscar por nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Adopciones urgentes", modifier = Modifier.weight(1f))
                Switch(checked = uiState.soloUrgentes, onCheckedChange = onCambiarUrgentes)
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.animalesVisibles, key = { it.id }) { animal ->
                    TarjetaAnimal(animal = animal, onClick = { onAnimalClick(animal) })
                }
            }
        }
    }
}

@Preview(name = "Tableta", widthDp = 840, heightDp = 1200, showBackground = true)
@Composable
fun ListaTabletaPreview() {
    CentroAcogidaTheme {
        PantallaLista(uiState = AcogidaUiState(), onCambiarUrgentes = {}, onAnimalClick = {})
    }
}
