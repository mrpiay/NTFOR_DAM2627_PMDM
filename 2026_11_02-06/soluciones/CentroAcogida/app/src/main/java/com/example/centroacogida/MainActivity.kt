package com.example.centroacogida

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.centroacogida.ui.theme.CentroAcogidaTheme

class MainActivity : ComponentActivity() {
    private val TAG = "Ciclo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            CentroAcogidaTheme {
                AcogidaApp()
            }
        }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}
