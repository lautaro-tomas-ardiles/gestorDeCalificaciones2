package screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import colors.black
import utilitis.menuBar

@Composable
fun mainListOfProfesores(onScreenChange: (Int) -> Unit) {
    Scaffold(
        backgroundColor = black ,
        snackbarHost = {
//            SnackbarHost(
//                snackbarHostState ,
//                snackbar = { data -> customSnackbar(data) }
//            )
        }
    ) {
        menuBar(onScreenChange , 6)
    }
}