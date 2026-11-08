package com.example.centroacogida

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.centroacogida.ui.theme.CentroAcogidaTheme

// TarjetaAnimal.kt
@Composable
fun TarjetaAnimal(
    animal: Animal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(animal.imagen),
                contentDescription = animal.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(animal.nombre, style = MaterialTheme.typography.titleMedium)
                val anios = if (animal.edad == 1) "año" else "años"
                Text(
                    "${animal.especie} · ${animal.edad} $anios",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (animal.urgente) {
                    Text(
                        "Adopción urgente",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TarjetaAnimalPreview() {
    CentroAcogidaTheme {   // el tema de tu proyecto (está en ui/theme/Theme.kt)
        TarjetaAnimal(animal = DatosAnimales.animales[0], onClick = {})
    }
}
