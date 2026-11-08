package com.example.centroacogida

// AcogidaApp.kt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AcogidaApp(viewModel: AcogidaViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val formulario by viewModel.formulario.collectAsState()

    NavHost(navController = navController, startDestination = "lista") {

        composable("lista") {
            PantallaLista(
                uiState = uiState,
                onCambiarUrgentes = viewModel::cambiarSoloUrgentes,
                onAnimalClick = { animal -> navController.navigate("detalle/${animal.id}") }
                // TODO Paso 10: onBuscar
            )
        }

        composable(
            route = "detalle/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("animalId") ?: -1
            PantallaDetalle(
                animal = viewModel.buscarAnimal(id),
                onVolver = { navController.popBackStack() },
                onSolicitar = {
                    viewModel.reiniciarFormulario()
                    navController.navigate("solicitud/$id")
                }
            )
        }

        composable(
            route = "solicitud/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("animalId") ?: -1
            PantallaSolicitud(
                animal = viewModel.buscarAnimal(id),
                form = formulario,
                onNombreChange = viewModel::cambiarNombre,
                onEmailChange = viewModel::cambiarEmail,
                onTelefonoChange = viewModel::cambiarTelefono,
                onAceptaChange = viewModel::cambiarAcepta,
                onEnviar = viewModel::enviarSolicitud,
                onVolver = { navController.popBackStack() },
                onTerminar = { navController.popBackStack("lista", inclusive = false) }
            )
        }
    }
}
