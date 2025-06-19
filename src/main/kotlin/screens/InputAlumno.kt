package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sql.SqlViewModel
import utilitis.button
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

        button(
            label = "Añadir",
            onClick = {
                sql.agregarAlumno(nombreDelAlumno, dniDelAlumno)
                if (sql.mensaje != null) {
                    return@button
                }
                nombreDelAlumno = ""
                dniDelAlumno = ""
            }
        )
    }
}

@Composable
fun mainInputAlumno(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }

    // Mostrar mensaje cuando cambie
    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }
    alumnoInputText(sql)
}