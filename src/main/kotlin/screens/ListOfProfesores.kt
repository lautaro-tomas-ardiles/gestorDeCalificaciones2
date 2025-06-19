package screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import sql.data.ProfesorData
import sql.SqlViewModel
import utilitis.boxOfData
import utilitis.button
import utilitis.search

@Composable
fun ListOfProfesoresOutPut(
    data: List<ProfesorData>,
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
                data = listOf("Nombre Del profesor", "DNI del profesor"),
                color = red
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))

        if (data.isEmpty()) {
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
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
                            sql.eliminarProfesorPorDNI(item.dni)
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
fun mainListOfProfesores(snackbarHostState: SnackbarHostState) {
    val sql = remember { SqlViewModel() }
    LaunchedEffect(Unit) {
        sql.buscarProfesoresPorDNI("")
    }
    var profesores by sql.profesores

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
            sql.buscarProfesoresPorDNI(query)
        } else {
            sql.buscarProfesoresPorNombre(query)
        }
    }

    Column {
        search(
            onSearchByName = { search ->
                refreshList(search, false)
            },
            onSearchByDNI = { search ->
                refreshList(search)
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