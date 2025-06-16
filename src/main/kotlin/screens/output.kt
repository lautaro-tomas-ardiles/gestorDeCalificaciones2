package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.orange
import colors.red
import sql.data.OutPutData
import sql.SqlViewModel
import utilitis.boxOfData
import utilitis.button
import utilitis.search

@Composable
fun textsOutPut(
    data: List<OutPutData>,
    sql: SqlViewModel,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 30.dp)
            .fillMaxSize()
    ) {
        //indica como se disponen los datos
        boxOfData(
            data = listOf("Nombre Del alumno","Nota","Nombre del profesor","Materia"),
            color = red
        )
        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn {
            items(data) {it ->
                Row {
                    boxOfData(
                        listOf(it.nombreDelAlumno, it.nota, it.nombreDelProfesor, it.nombreDeLaMateria),
                        orange
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    button("×") {
                        sql.eliminarNotaPorDNI(it.dniDelAlumno)
                        onRefresh()
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun mainOutput(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }
    var estudiantes by remember { mutableStateOf(emptyList<OutPutData>()) }

    fun refreshList(query: String, isDNI: Boolean = true) {
        estudiantes = if (isDNI) {
            sql.buscarAlumnoPorDNI(query)
        }else {
            sql.buscarAlumnoPorNombre(query)
        }
    }

    var lastQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        estudiantes = sql.buscarAlumnoPorNombre("")
    }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    Column {
        search(
            onSearchByName = { search ->
                lastQuery = search
                refreshList(lastQuery, false)
            },
            onSearchByDNI = { search ->
                lastQuery = search
                refreshList(lastQuery)
            }
        )
        textsOutPut(estudiantes, sql) {
            refreshList(lastQuery)
        }
    }
}