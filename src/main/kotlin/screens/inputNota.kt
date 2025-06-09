package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.black
import sql.SqlViewModel
import utilitis.addButton
import utilitis.menuBar
import utilitis.selectorBox
import utilitis.textBar

@Composable
fun notaInputInsert(sql: SqlViewModel) {
    val scroll = rememberScrollState()
    var nota by remember { mutableStateOf("") }

    var alumnoNombre by remember { mutableStateOf("") }
    var profesorNombre by remember { mutableStateOf("") }
    var materiaNombre by remember { mutableStateOf("") }

    var dniAlumno by remember { mutableStateOf("") }
    var dniProfesor by remember { mutableStateOf("") }

    var expandedAlumno by remember { mutableStateOf(false) }
    var expandedProfesor by remember { mutableStateOf(false) }
    var expandedMateria by remember { mutableStateOf(false) }

    val alumnos = remember { sql.obtenerAlumnos() }
    val profesores = remember { sql.obtenerProfesores() }
    val materias = remember { sql.obtenerMaterias() }

    val alumnosFiltrados = alumnos.filter {
        val nombre = (it[0] as? String)?.lowercase() ?: ""
        nombre.contains(alumnoNombre.lowercase())
    }
    val profesoresFiltrados = profesores.filter {
        val nombre = (it[0] as? String)?.lowercase() ?: ""
        nombre.contains(profesorNombre.lowercase())
    }
    val materiasFiltradas = materias.filter {
        val nombre = (it[0] as? String)?.lowercase() ?: ""
        nombre.contains(materiaNombre.lowercase())
        val profesorDni = (it[1] as? String)?.lowercase() ?: ""
        profesorDni.contains(dniProfesor.lowercase())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(vertical = 35.dp) ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nota ,
            onValueChange = { nota = it } ,
            label = "Nota..."
        )
        Spacer(modifier = Modifier.padding(31.dp))
        // ALUMNO
        selectorBox(
            label = "Seleccione alumno..." ,
            expanded = expandedAlumno ,
            onExpandedChange = { expandedAlumno = it } ,
            inputText = alumnoNombre ,
            onInputChange = { alumnoNombre = it } ,
            options = alumnosFiltrados ,
            displayText = { "nombre: ${it[0]} | dni: ${it[1]}" } ,
            onSelect = {
                dniAlumno = it[1] as String
                alumnoNombre = "nombre: ${it[0]} | dni: ${it[1]}"
                expandedAlumno = false
            }
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
        // MATERIA
        selectorBox(
            label = "Seleccione materia..." ,
            expanded = expandedMateria ,
            onExpandedChange = { expandedMateria = it } ,
            inputText = materiaNombre ,
            onInputChange = { materiaNombre = it } ,
            options = materiasFiltradas ,
            displayText = { "nombre: ${it[0]} | dni: ${it[1]}" } ,
            onSelect = {
                dniProfesor = it[1] as String
                materiaNombre = it[0] as String
                expandedMateria = false
            }
        )
        Spacer(modifier = Modifier.padding(31.dp))

        addButton(label = "Añadir") {
            sql.agregarNota(dniProfesor , dniAlumno , materiaNombre , nota)
            nota = ""
            alumnoNombre = ""
            profesorNombre = ""
            materiaNombre = ""
        }
    }
}

@Composable
fun mainInputNota(onScreenChange: (Int) -> Unit) {
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
                snackbar = { data -> utilitis.customSnackbar(data) }
            )
        }
    ) {
        Column {
            menuBar(onScreenChange , 4)
            notaInputInsert(sql)
        }
    }
}