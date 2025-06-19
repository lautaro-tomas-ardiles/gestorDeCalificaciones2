package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sql.data.NotaData
import sql.SqlViewModel
import utilitis.button
import utilitis.selectorBox
import utilitis.textBar

@Composable
fun notaInputInsert(sql: SqlViewModel) {
    val scroll = rememberScrollState()
    var nota by remember { mutableStateOf("") }

    var selectedAlumno by remember { mutableStateOf("") }
    var selectedProfesor by remember { mutableStateOf("") }
    var selectedMateria by remember { mutableStateOf("") }

    var materiaNombre by remember { mutableStateOf("") }
    var materiaId by remember { mutableStateOf(-1) }

    var dniAlumno by remember { mutableStateOf("") }
    var dniProfesor by remember { mutableStateOf("") }

    var expandedAlumno by remember { mutableStateOf(false) }
    var expandedProfesor by remember { mutableStateOf(false) }
    var expandedMateria by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // se pide a db las listas correspondientes
        sql.cargarProfesores()
        sql.cargarAlumnos()
        sql.cargarMaterias()
        //se filtran las listas
        sql.filtrarProfesores("")
        sql.filtrarAlumnos("")
        sql.filtrarMaterias("","")
    }

    LaunchedEffect(dniProfesor) {
        sql.filtrarMaterias(dniProfesor, materiaNombre)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(vertical = 35.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textBar(
            value = nota,
            onValueChange = { nota = it },
            label = "Nota..."
        )
        Spacer(modifier = Modifier.padding(31.dp))

        // ALUMNO
        selectorBox(
            label = "Seleccione alumno...",
            expanded = expandedAlumno,
            onExpandedChange = { expandedAlumno = it },
            inputText = selectedAlumno,
            onInputChange = {
                selectedAlumno = it
                sql.filtrarAlumnos(it)
            },
            options = sql.alumnos.value,
            displayText = { "nombre: ${it.nombre} | dni: ${it.dni}" },
            onSelect = {
                dniAlumno = it.dni
                selectedAlumno = "nombre: ${it.nombre} | dni: ${it.dni}"
                expandedAlumno = false
            }
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
            },
            options = sql.profesores.value,
            displayText = { "nombre: ${it.nombre} | dni: ${it.dni}" },
            onSelect = {
                dniProfesor = it.dni
                selectedProfesor = "nombre: ${it.nombre} | dni: ${it.dni}"
                expandedProfesor = false
            }
        )

        Spacer(modifier = Modifier.padding(31.dp))

        // MATERIA
        selectorBox(
            label = "Seleccione materia...",
            expanded = expandedMateria,
            onExpandedChange = { expandedMateria = it },
            inputText = selectedMateria,
            onInputChange = {
                selectedMateria = it
                sql.filtrarMaterias(dniProfesor, materiaNombre)
            },
            options = sql.materias.value,
            displayText = { "nombre: ${it.nombre} | dni del profesor: ${it.dniDelProfesor}" },
            onSelect = {
                selectedMateria = "nombre: ${it.nombre} | dni del profesor: ${it.dniDelProfesor}"
                materiaNombre = it.nombre
                materiaId = it.materiaId
                expandedMateria = false
            }
        )

        Spacer(modifier = Modifier.padding(31.dp))

        button(label = "Añadir") {
            sql.agregarNota(
                NotaData(
                    dniProfesor,
                    nota.toDoubleOrNull(),
                    dniAlumno,
                    id = materiaId
                )
            )
            if (sql.mensaje != null) {
                return@button
            }
            // Reset
            nota = ""
            selectedAlumno = ""
            selectedProfesor = ""
            selectedMateria = ""
            materiaNombre = ""
            dniAlumno = ""
            dniProfesor = ""
            materiaId = -1
        }
    }
}

@Composable
fun mainInputNota(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    notaInputInsert(sql)
}