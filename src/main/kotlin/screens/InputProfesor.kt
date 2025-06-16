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
fun profesorInputInsert(sql: SqlViewModel) {
    var nombreDelProfesor by remember { mutableStateOf("") }
    var dniDelProfesor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 35.dp) ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nombreDelProfesor ,
            onValueChange = { nombreDelProfesor = it } ,
            label = "Nombre del profesor..."
        )
        Spacer(modifier = Modifier.padding(31.dp))

        textBar(
            value = dniDelProfesor ,
            onValueChange = { dniDelProfesor = it } ,
            label = "D.N.I del profesor"
        )
        Spacer(modifier = Modifier.padding(31.dp))

        button(label = "agregar") {
            sql.agregarProfesor(
                nombreDelProfesor ,
                dniDelProfesor
            )
            nombreDelProfesor = ""
            dniDelProfesor = ""
        }
    }
}

@Composable
fun mainInputProfesor(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }

    // Mostrar mensaje cuando cambie
    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    profesorInputInsert(sql)
}