package com.example.centroacogida

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
                    .verticalScroll(rememberScrollState())
                    .widthIn(max = 600.dp),
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
                FotoDelAnimal(animal)
            }
        }
    }
}

// Pedir un permiso en tiempo de ejecución y capturar una imagen con la cámara
@Composable
fun FotoDelAnimal(animal: Animal) {
    val context = LocalContext.current
    // rememberSaveable: la foto y el aviso no se pierden al girar la pantalla
    // TODO Paso 14.2: cargar la foto guardada del animal
    var foto by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    var aviso by rememberSaveable { mutableStateOf<String?>(null) }

    // Abre la cámara del sistema y devuelve una foto pequeña (o null si se cancela)
    val hacerFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // TODO Paso 14.2: recortar, guardar como JPEG y avisar del tamaño
        foto = bitmap
    }
    // Muestra el diálogo del sistema «¿Permitir que la app haga fotos?»
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) hacerFoto.launch(null)
        else aviso = "Sin permiso de cámara no se puede hacer la foto."
    }

    OutlinedButton(
        onClick = {
            aviso = null
            val tengoPermiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
            if (tengoPermiso) hacerFoto.launch(null) else pedirPermiso.launch(Manifest.permission.CAMERA)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Hacer una foto de ${animal.nombre}")
    }
    aviso?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    foto?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Foto de ${animal.nombre}",
            modifier = Modifier.fillMaxWidth().height(200.dp).clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
    }
}
