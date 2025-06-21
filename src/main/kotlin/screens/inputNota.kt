package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sql.DBViewModel
import sql.data.NotaData
import utilitis.button
import utilitis.selectorBox
import utilitis.textBar

@Composable
fun notaInputInsert(sql: DBViewModel) {
    val scroll = rememberScrollState()
    var nota by remember { mutableStateOf("") }

    var selectedAlumno by remember { mutableStateOf("") }
    var selectedProfesor by remember { mutableStateOf("") }
    var selectedMateria by remember { mutableStateOf("") }

    var materiaNombre by remember { mutableStateOf("") }
    var materiaId: Int? by remember { mutableStateOf(null) }

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
                if (it.isBlank()) {
                    dniAlumno = ""
                }
            },
            options = sql.alumnos.value,
            displayText = { "nombre: ${it.nombreA} | dni: ${it.dniA}" },
            onSelect = {
                dniAlumno = it.dniA
                selectedAlumno = "nombre: ${it.nombreA} | dni: ${it.dniA}"
                expandedAlumno = false
            }
        )

        Spacer(modifier = Modifier.padding(31.dp))
        Text(selectedAlumno)
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
                    dniProfesor = ""
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
            displayText = { "nombre: ${it.materia} | dni del profesor: ${it.dniP}" },
            onSelect = {
                selectedMateria = "nombre: ${it.materia} | dni del profesor: ${it.dniP}"
                materiaNombre = it.materia
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
                    materiaId
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
            materiaId = null
        }
    }
}

@Composable
fun mainInputNota(snackbarHostState: SnackbarHostState) {
    val sql = remember { DBViewModel() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    notaInputInsert(sql)
}