package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.black
import sql.SqlViewModel
import utilitis.addButton
import utilitis.customSnackbar
import utilitis.menuBar
import utilitis.textBar

@Composable
fun alumnoInputText(sql: SqlViewModel) {
    var nombreDelAlumno by remember { mutableStateOf("") }
    var dniDelAlumno by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 35.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nombreDelAlumno,
            onValueChange = { nombreDelAlumno = it },
            label = "Nombre del alumno..."
        )
        Spacer(modifier = Modifier.padding(31.dp))

        textBar(
            value = dniDelAlumno,
            onValueChange = { dniDelAlumno = it },
            label = "D.N.I del alumno..."
        )
        Spacer(modifier = Modifier.padding(31.dp))

        addButton(
            label = "Añadir",
            onClick = {
                sql.agregarAlumno(nombreDelAlumno, dniDelAlumno)
                nombreDelAlumno = ""
                dniDelAlumno = ""
            }
        )
    }
}

@Composable
fun mainInputAlumno(onScreenChange: (Int) -> Unit) {
    val sql = remember { SqlViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensaje cuando cambie
    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }
    Scaffold(
        backgroundColor = black,
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                snackbar = { data -> customSnackbar(data) }
            )
        }
    ) {
        menuBar(onScreenChange, 1)
        alumnoInputText(sql)
    }

}