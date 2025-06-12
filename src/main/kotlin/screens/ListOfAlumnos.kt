package screens

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import colors.black
import utilitis.menuBar

@Composable
fun mainListOfAlumnos(onScreenChange: (Int) -> Unit) {
    Scaffold(
        backgroundColor = black ,
        snackbarHost = {
//            SnackbarHost(
//                snackbarHostState ,
//                snackbar = { data -> customSnackbar(data) }
//            )
        }
    ) {
        menuBar(onScreenChange , 5)
    }
}