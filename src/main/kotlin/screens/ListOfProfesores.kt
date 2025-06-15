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
import sql.data.ProfesorData
import sql.SqlViewModel
import utilitis.boxOfData
import utilitis.button
import utilitis.customSnackbar
import utilitis.menuBar
import utilitis.search

@Composable
fun ListOfProfesoresOutPut(
    data: List<ProfesorData>,
    sql: SqlViewModel,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 30.dp)
            .fillMaxSize()
    ) {
        boxOfData(
            data = listOf("Nombre Del profesor", "DNI del profesor"),
            color = red
        )
        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn {
            items(data) { it ->
                Row {
                    boxOfData(
                        listOf(it.nombre,it.dni),
                        orange
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    button("×") {
                        sql.eliminarProfesorPorDNI(it.dni)
                        onRefresh()
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun mainListOfProfesores(onScreenChange: (Int) -> Unit) {
    val sql = remember { SqlViewModel() }
    var profesores by remember { mutableStateOf(emptyList<ProfesorData>()) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    fun refreshList(query: String, isDNI: Boolean = true) {
        profesores = if (isDNI) {
            sql.listaDeProfesoresPorDNI(query)
        }else {
            sql.listaDeProfesoresPorNombre(query)
        }
    }

    var lastQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        profesores = sql.listaDeProfesoresPorDNI("")
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
            menuBar(onScreenChange, 6)
            search(
                onSearchByDNI = { str ->
                    lastQuery = str
                    refreshList(str, true)
                },
                onSearchByName = { str ->
                    lastQuery = str
                    refreshList(str, false)
                },
                isAlumno = false
            )
            ListOfProfesoresOutPut(
                data = profesores,
                sql = sql,
                onRefresh = { refreshList(lastQuery) }
            )
        }
    }
}