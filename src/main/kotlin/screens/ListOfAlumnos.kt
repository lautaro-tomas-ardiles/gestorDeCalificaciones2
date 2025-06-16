package screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.orange
import colors.red
import sql.data.AlumnoData
import sql.SqlViewModel
import utilitis.boxOfData
import utilitis.button
import utilitis.search

@Composable
fun ListOfAlumnosOutPut(
    data: List<AlumnoData>,
    sql: SqlViewModel,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 30.dp)
            .fillMaxSize()
    ) {
        boxOfData(
            data = listOf("Nombre Del alumno", "DNI del alumno"),
            color = red
        )
        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn {
            items(data) { it ->
                Row {
                    boxOfData(
                        listOf(it.nombre, it.dni),
                        orange
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    button("×") {
                        sql.eliminarAlumnoPorDNI(it.dni)
                        onRefresh()
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun mainListOfAlumnos(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }
    var estudiantes by remember { mutableStateOf(emptyList<AlumnoData>()) }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    fun refreshList(query: String, isDNI: Boolean = true) {
        estudiantes = if (isDNI) {
            sql.listaDeAlumnosPorDNI(query)
        }else {
            sql.listaDeAlumnosPorNombre(query)
        }
    }

    var lastQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        estudiantes = sql.listaDeAlumnosPorDNI("")
    }
    Column {
        search(
            onSearchByDNI = { str ->
                lastQuery = str
                refreshList(str, true)
            },
            onSearchByName = { str ->
                lastQuery = str
                refreshList(str, false)
            }
        )
        ListOfAlumnosOutPut(
            data = estudiantes,
            sql = sql,
            onRefresh = { refreshList(lastQuery) }
        )
    }
}
