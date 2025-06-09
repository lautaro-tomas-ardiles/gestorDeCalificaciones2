import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import screens.*

@Composable
fun app() {
    var currentScreen by remember { mutableStateOf(1) }

    // Cuando currentScreen cambia, cambiamos la pantalla
    when (currentScreen) {
        1 -> mainInputAlumno(onScreenChange = { currentScreen = it })

        2 -> mainInputProfesor(onScreenChange = { currentScreen = it })

        3 -> mainInputMateria(onScreenChange = { currentScreen = it })

        4 -> mainInputNota(onScreenChange = { currentScreen = it })

        5 -> mainOutput(onScreenChange = { currentScreen = it })
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
