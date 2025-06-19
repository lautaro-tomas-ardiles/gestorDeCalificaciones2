package screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import colors.orange
import colors.red
import sql.SqlViewModel
import sql.data.AlumnoData
import utilitis.boxOfData
import utilitis.button
import utilitis.search

@Composable
fun ListOfAlumnosOutPut(
    data: List<AlumnoData>,
    sql: SqlViewModel,
    onRefresh: () -> Unit
) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(start = 30.dp)
            .verticalScroll(scroll)
            .fillMaxSize()
    ) {
        var headerVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { headerVisible = true }

        AnimatedVisibility(
            visible = headerVisible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            boxOfData(
                data = listOf("Nombre Del alumno", "DNI del alumno"),
                color = red
            )
        }

        Spacer(modifier = Modifier.padding(20.dp))

        if (data.isEmpty()) {
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            ) {
                boxOfData(
                    data = listOf("no hay datos"),
                    color = orange
                )
            }
        }

        data.forEach { item ->
            // Estado para controlar visibilidad de animación
            var visible by remember { mutableStateOf(false) }
            // Disparar la animación al componer el ítem
            LaunchedEffect(item.dni) { visible = true }

            // AnimatedVisibility con transición horizontal suave
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    initialOffsetX = { -it }, // Desde la izquierda
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { it }, // Hacia la derecha
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                Column {
                    Row {
                        boxOfData(
                            listOf(item.nombre, item.dni),
                            orange
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        button("×") {
                            visible = false
                            sql.eliminarAlumnoPorDNI(item.dni)
                            onRefresh()
                        }
                    }
                    // Espaciado después de la fila
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}


@Composable
fun mainListOfAlumnos(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }
    LaunchedEffect(Unit) {
        sql.buscarAlumnosPorDNI("")
    }
    var estudiantes by sql.alumnos

    LaunchedEffect(sql.mensaje) {
        sql.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            sql.limpiarMensaje()
        }
    }

    var lastQuery by remember { mutableStateOf("") }

    fun refreshList(query: String, isDNI: Boolean = true) {
        lastQuery = query
        if (isDNI) {
            sql.buscarAlumnosPorDNI(query)
        } else {
            sql.buscarAlumnosPorNombre(query)
        }
    }

    Column {
        search(
            onSearchByName = { search ->
                refreshList(search, false)
            },
            onSearchByDNI = { search ->
                refreshList(search)
            }
        )
        ListOfAlumnosOutPut(
            data = estudiantes,
            sql = sql,
            onRefresh = { refreshList(lastQuery) }
        )
    }
}
