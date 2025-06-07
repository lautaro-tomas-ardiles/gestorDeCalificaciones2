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
fun profesorInputInsert(sql: SqlViewModel) {
    var nombreDelProfesor by remember { mutableStateOf("") }
    var dniDelProfesor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 35.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nombreDelProfesor,
            onValueChange = { nombreDelProfesor = it },
            label = "Nombre del profesor..."
        )
        Spacer(modifier = Modifier.padding(31.dp))

        textBar(
            value = dniDelProfesor,
            onValueChange = { dniDelProfesor = it },
            label = "D.N.I del profesor"
        )
        Spacer(modifier = Modifier.padding(31.dp))

        addButton(label = "agregar") {
            sql.agregarProfesor(
                nombreDelProfesor,
                dniDelProfesor
            )
            nombreDelProfesor = ""
            dniDelProfesor = ""
        }
    }
}

@Composable
fun mainInputProfesor(onScreenChange: (Int) -> Unit) {
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
        menuBar(onScreenChange, 2)
        profesorInputInsert(sql)
    }
}