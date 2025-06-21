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
import sql.DBViewModel
import sql.data.OutPutData
import utilitis.boxOfData
import utilitis.button
import utilitis.search

@Composable
fun textsOutPut(
    data: List<OutPutData>,
    sql: DBViewModel,
    onRefresh: () -> Unit
) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(start = 30.dp)
            .fillMaxWidth()
            .verticalScroll(scroll)
    ) {
        //indica como se disponen los datos
        boxOfData(
            data = listOf("Nombre Del alumno", "Nota", "Nombre del profesor", "Materia"),
            color = red
        )
        Spacer(modifier = Modifier.padding(20.dp))
        if (data.isEmpty()) {
            AnimatedVisibility(
                true,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            ) {
                boxOfData(
                    data = listOf("no hay datos con esos nombres o dni"),
                    color = orange
                )
            }
        }

        data.forEach { item ->
            // Estado para controlar visibilidad de animación
            var visible by remember { mutableStateOf(false) }
            // Disparar la animación al componer el ítem
            LaunchedEffect(item.notaId) { visible = true }

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
                            listOf(
                                item.dniA.nombreA, item.nota,
                                item.dniP.nombreP, item.materiaId.materia,
                                item.notaId
                            ),
                            orange
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        button("×") {
                            visible = false
                            sql.eliminarNotaPorId(item.notaId)
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
fun mainOutput(snackbarHostState: SnackbarHostState) {
    val sql = remember { DBViewModel() }
    LaunchedEffect(Unit) {
        sql.buscarNotaDeAlumnoPorNombre("")
    }

    val estudiantes by sql.outPut  // Observa directamente el estado

    var lastQuery by remember { mutableStateOf("") }

    fun refreshList(query: String, isDNI: Boolean = true) {
        lastQuery = query
        if (isDNI) {
            sql.buscarNotaDelAlumnoPorDNI(query)
        } else {
            sql.buscarNotaDeAlumnoPorNombre(query)
        }
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
                refreshList(search, false)
            },
            onSearchByDNI = { search ->
                refreshList(search)
            }
        )
        textsOutPut(estudiantes, sql) {
            refreshList(lastQuery)
        }
    }
}