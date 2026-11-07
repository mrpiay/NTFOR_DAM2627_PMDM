package com.example.centroacogida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.centroacogida.ui.theme.CentroAcogidaTheme

class MainActivity : ComponentActivity() {
    // TODO Paso 4: añadir la constante TAG y mensajes Log.d en los métodos del ciclo de vida.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroAcogidaTheme {
                // TODO Paso 5: crear el ViewModel y pasar su estado a PantallaLista.
                // TODO Paso 6: sustituir todo esto por AcogidaApp() (navegación).
                PantallaLista(onAnimalClick = { })
            }
        }
    }

    // TODO Paso 4: sobrescribir onStart, onResume, onPause, onStop y onDestroy.
}
