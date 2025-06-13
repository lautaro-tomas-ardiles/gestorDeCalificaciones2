package screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.black
import colors.orange
import colors.red
import sql.SqlViewModel
import utilitis.boxOfData
import utilitis.button
import utilitis.customSnackbar
import utilitis.menuBar

@Composable
fun ListOutPut(
    data: List<List<Any>>,
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
            items(data) { row ->
                Row {
                    boxOfData(row, orange)
                    Spacer(modifier = Modifier.padding(5.dp))
                    button("x") {
                        val dni = row[1] as String
                        sql.eliminarAlumnoPorDNI(dni)
                        onRefresh()
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}


@Composable
fun mainListOfAlumnos(onScreenChange: (Int) -> Unit) {
    val sql = remember { SqlViewModel() }
    var estudiantes by remember { mutableStateOf(emptyList<List<Any>>()) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    fun refreshList(query: String, isDNI : Boolean = true) {
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

    Scaffold(
        backgroundColor = black,
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                snackbar = { data -> customSnackbar(data) }
            )
        }
    ) {
        Column {
            menuBar(onScreenChange, 5)
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
            ListOutPut(
                data = estudiantes,
                sql = sql,
                onRefresh = { refreshList(lastQuery) }
            )
        }
    }
}
