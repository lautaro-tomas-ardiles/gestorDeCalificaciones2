package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.black
import sql.SqlViewModel
import utilitis.*

@Composable
fun materiaInputInsert(sql: SqlViewModel) {
    var nombreDeLaMateria by remember { mutableStateOf("") }

    var selectedProfesor by remember { mutableStateOf("") }
    var profesorNombre by remember { mutableStateOf("") }
    var dniProfesor by remember { mutableStateOf("") }

    var expandedProfesor by remember { mutableStateOf(false) }
    val profesores = remember { sql.obtenerProfesores() }
    val profesoresFiltrados = profesores.filter {
        val nombre = (it[0] as? String)?.lowercase() ?: ""
        nombre.contains(profesorNombre.lowercase())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 35.dp) ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nombreDeLaMateria ,
            onValueChange = { nombreDeLaMateria = it } ,
            label = "Nombre de la materia"
        )
        Spacer(modifier = Modifier.padding(31.dp))
        // PROFESOR
        selectorBox(
            label = "Seleccione profesor..." ,
            expanded = expandedProfesor ,
            onExpandedChange = { expandedProfesor = it } ,
            inputText = profesorNombre ,
            onInputChange = { profesorNombre = it } ,
            options = profesoresFiltrados ,
            displayText = { "nombre: ${it[0]} | dni: ${it[1]}" } ,
            onSelect = {
                dniProfesor = it[1] as String
                profesorNombre = "nombre: ${it[0]} | dni: ${it[1]}"
                expandedProfesor = false
            }
        )
        Spacer(modifier = Modifier.padding(31.dp))

        button(
            label = "Añadir"
        ) {
            sql.agregarMateria(dniProfesor , nombreDeLaMateria)
            nombreDeLaMateria = ""
            dniProfesor = ""
            selectedProfesor = ""
            profesorNombre = ""
        }
    }
}

@Composable
fun mainInputMateria(onScreenChange: (Int) -> Unit) {
    val sql = remember { SqlViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    Scaffold(
        backgroundColor = black ,
        snackbarHost = {
            SnackbarHost(
                snackbarHostState ,
                snackbar = { data -> customSnackbar(data) }
            )
        }
    ) {
        menuBar(onScreenChange , 3)
        materiaInputInsert(sql)
    }
}