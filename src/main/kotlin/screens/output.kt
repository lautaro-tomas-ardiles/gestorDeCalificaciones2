package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import utilitis.textBarForSearch

@Composable
fun search(
    onSearchByName: (String) -> Unit ,
    onSearchByDNI: (String) -> Unit
) {
    var searchByName by remember { mutableStateOf("") }
    var searchByDNI by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(vertical = 30.dp , horizontal = 20.dp)
            .fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Center
    ) {
        textBarForSearch(
            value = searchByName,
            onValueChange = { searchByName = it },
            label = "Nombre del alumno",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(14.dp))

        button(
            label = "Búsqueda por nombre"
        ) {
            onSearchByName(searchByName)
        }
        Spacer(modifier = Modifier.padding(14.dp))

        textBarForSearch(
            value = searchByDNI,
            onValueChange = { searchByDNI = it },
            label = "D.N.I del alumno...",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(14.dp))

        button(
            label = "Búsqueda por D.N.I"
        ) {
            onSearchByDNI(searchByDNI)
        }
    }
}

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