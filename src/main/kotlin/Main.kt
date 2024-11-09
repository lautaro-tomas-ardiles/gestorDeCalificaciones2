
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import screens.OutputMain
import screens.inputMain

@Composable
@Preview
fun App() {
    var primeraScreen by remember { mutableStateOf(true) }

    if (primeraScreen) {
        inputMain(onScreenChange = { primeraScreen = false })
    } else {
        OutputMain(onScreenChange = { primeraScreen = true })
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
