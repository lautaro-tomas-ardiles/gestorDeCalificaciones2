package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sql.DBViewModel
import utilitis.button
import utilitis.selectorBox
import utilitis.textBar

@Composable
fun materiaInputInsert(sql: DBViewModel) {

    var nombreDeLaMateria by remember { mutableStateOf("") }
    var selectedProfesor by remember { mutableStateOf("") }
    var dniProfesor by remember { mutableStateOf("") }
    var expandedProfesor by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        sql.cargarProfesores()
        sql.filtrarProfesores("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 35.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nombreDeLaMateria,
            onValueChange = { nombreDeLaMateria = it },
            label = "Nombre de la materia"
        )
        Spacer(modifier = Modifier.padding(31.dp))

        // PROFESOR
        selectorBox(
            label = "Seleccione profesor...",
            expanded = expandedProfesor,
            onExpandedChange = { expandedProfesor = it },
            inputText = selectedProfesor,
            onInputChange = {
                selectedProfesor = it
                sql.filtrarProfesores(it)
                if (it.isBlank()) {
                    dniProfesor = it
                }
            },
            options = sql.profesores.value,
            displayText = { "nombre: ${it.nombreP} | dni: ${it.dniP}" },
            onSelect = {
                dniProfesor = it.dniP
                selectedProfesor = "nombre: ${it.nombreP} | dni: ${it.dniP}"
                expandedProfesor = false
            }
        )
        Spacer(modifier = Modifier.padding(31.dp))

        button(label = "Añadir") {
            sql.agregarMateria(dniProfesor, nombreDeLaMateria)
            if (sql.mensaje != null) {
                return@button
            }
            nombreDeLaMateria = ""
            dniProfesor = ""
            selectedProfesor = ""
        }
    }
}

@Composable
fun mainInputMateria(snackbarHostState: SnackbarHostState) {
    val sql = remember { DBViewModel() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }
    materiaInputInsert(sql)
}