package com.example.centroacogida

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// PantallaDetalle.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    animal: Animal?,
    onVolver: () -> Unit,
    onSolicitar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(animal?.nombre ?: "Animal no encontrado") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (animal == null) {
            Text(
                "No se ha encontrado el animal.",
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(animal.imagen),
                    contentDescription = animal.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(MaterialTheme.shapes.large)
                )
                Text(animal.nombre, style = MaterialTheme.typography.headlineMedium)
                val anios = if (animal.edad == 1) "año" else "años"
                Text("${animal.especie} · ${animal.edad} $anios", style = MaterialTheme.typography.titleMedium)
                if (animal.urgente) {
                    Text("Necesita adopción urgente", color = MaterialTheme.colorScheme.error)
                }
                Text(animal.descripcion, style = MaterialTheme.typography.bodyLarge)
                Button(onClick = onSolicitar, modifier = Modifier.fillMaxWidth()) {
                    Text("Solicitar adopción")
                }
            }
        }
    }
}
