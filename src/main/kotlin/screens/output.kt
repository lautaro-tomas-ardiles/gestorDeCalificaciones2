package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.black
import colors.orange
import colors.red
import sql.SqlViewModel
import utilitis.boxOfData
import utilitis.customSnackbar
import utilitis.menuBar
import utilitis.search

@Composable
fun textsOutPut(data: List<List<Any>>) {
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
            items(data) { data ->
                boxOfData(data,orange)
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun mainOutput(onScreenChange: (Int) -> Unit) {
    val sql = remember { SqlViewModel() }
    var estudiantes by remember { mutableStateOf(emptyList<List<Any>>()) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    Scaffold(
        backgroundColor = black,
        snackbarHost = {
            SnackbarHost(
                snackbarHostState ,
                snackbar = { data -> customSnackbar(data) }
            )
        }
    ) {
        Column {
            menuBar(onScreenChange , 7)
            search(
                onSearchByName = { search ->
                    estudiantes = sql.buscarAlumnoPorNombre(search)
                } ,
                onSearchByDNI = { search ->
                    estudiantes = sql.buscarAlumnoPorDNI(search)
                }
            )
            textsOutPut(estudiantes)
        }
    }
}