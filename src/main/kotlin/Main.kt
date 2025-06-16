import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import colors.black
import screens.*
import utilitis.customSnackbar
import utilitis.menuBar

@Composable
fun app() {
    var currentScreen by remember { mutableStateOf(1) }
    var previousScreen by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        backgroundColor = black,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                customSnackbar(data)
            }
        },
        topBar = {
            menuBar(currentScreen) {
                currentScreen = it
            }
        }
    ) {
        val isForward = currentScreen > previousScreen
        Column {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (isForward) {
                        (slideInHorizontally { width -> width } + fadeIn())
                            .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn())
                            .togetherWith(slideOutHorizontally { width -> width } + fadeOut())
                    }
                },
                label = "screenTransition"
            ) { screen ->
                when (screen) {
                    1 -> {
                        previousScreen = screen
                        mainInputAlumno(snackbarHostState)
                    }

                    2 -> {
                        previousScreen = screen
                        mainInputProfesor(snackbarHostState)
                    }

                    3 -> {
                        previousScreen = screen
                        mainInputMateria(snackbarHostState)
                    }

                    4 -> {
                        previousScreen = screen
                        mainInputNota(snackbarHostState)
                    }

                    5 -> {
                        previousScreen = screen
                        mainListOfAlumnos(snackbarHostState)
                    }

                    6 -> {
                        previousScreen = screen
                        mainListOfProfesores(snackbarHostState)
                    }

                    7 -> {
                        previousScreen = screen
                        mainOutput(snackbarHostState)
                    }
                }
            }
        }
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication ,
        title = "Gestor de calificaciones"
    ) {
        app()
    }
}
