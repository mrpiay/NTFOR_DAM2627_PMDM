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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

// PantallaLista.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    uiState: AcogidaUiState,
    onCambiarUrgentes: (Boolean) -> Unit,
    onAnimalClick: (Animal) -> Unit,
    onVerSolicitudes: () -> Unit = {},
    onBuscar: (String) -> Unit = {},
    onRecargar: () -> Unit = {}
) {
    // Agitar el móvil recarga la lista
    AlAgitar(onSacudida = onRecargar)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de acogida") },
                actions = {
                    // El mismo efecto que agitar, para quien no puede o no quiere agitar el móvil
                    IconButton(onClick = onRecargar) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Recargar la lista")
                    }
                    TextButton(onClick = onVerSolicitudes) { Text("Mis solicitudes") }
                }
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
            if (uiState.cargando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            uiState.aviso?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            DistanciaAlCentro()
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

// Pide el permiso de ubicación y muestra a qué distancia está el centro
@Composable
fun DistanciaAlCentro() {
    val context = LocalContext.current
    val alcance = rememberCoroutineScope()
    var texto by rememberSaveable { mutableStateOf<String?>(null) }

    fun calcular() {
        alcance.launch {
            texto = "Buscando tu ubicación…"
            val ubicacion = obtenerUbicacion(context)
            texto = if (ubicacion == null) {
                "No se ha podido saber dónde estás: activa la ubicación del dispositivo."
            } else {
                "Estás a %.1f km del centro.".format(distanciaAlCentroKm(ubicacion))
            }
        }
    }

    // Pide a la vez la ubicación precisa y la aproximada: la persona elige cuál da
    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { respuesta ->
        if (respuesta.values.any { it }) calcular()
        else texto = "Sin permiso de ubicación no se puede calcular la distancia."
    }

    OutlinedButton(
        onClick = {
            val tengoPermiso = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (tengoPermiso) calcular()
            else pedirPermiso.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("¿A qué distancia está el centro?")
    }
    texto?.let { Text(it) }
}

@Preview(name = "Tableta", widthDp = 840, heightDp = 1200, showBackground = true)
@Composable
fun ListaTabletaPreview() {
    CentroAcogidaTheme {
        PantallaLista(uiState = AcogidaUiState(), onCambiarUrgentes = {}, onAnimalClick = {})
    }
}
