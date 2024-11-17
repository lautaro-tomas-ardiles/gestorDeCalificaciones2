@file:OptIn(ExperimentalComposeUiApi::class)

package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.*
import sql.SQLiteCRUD

@Composable
fun subOutputTopBar(onScreenChange: (Int) -> Unit) {
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    val boxWidth = screenWidth / 8
    val superBoxWidth = screenWidth / 2

    Row{
        //alumno
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(50.16.dp)
                .width(boxWidth)
                .background(color = blue)
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()

                    // Borde superior
                    drawLine(
                        color = red,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )

                    // Borde izquierdo
                    drawLine(
                        color = red,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(1)
                }
        ) {
            Text(
                text = "Alumno",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        //profesor
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(50.16.dp)
                .width(boxWidth)
                .background(color = blue)
                .drawBehind {
                    val strokeWidth = 4f

                    // Borde superior
                    drawLine(
                        color = red,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(2)
                }
        ) {
            Text(
                text = "Profesor",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        //materia
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(50.16.dp)
                .width(boxWidth)
                .background(color = blue)
                .drawBehind {
                    val strokeWidth = 4f

                    // Borde superior
                    drawLine(
                        color = red,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(3)
                }
        ) {
            Text(
                text = "Materia",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        //nota
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(50.16.dp)
                .width(boxWidth)
                .background(color = blue)
                .drawBehind {
                    val strokeWidth = 4f

                    // Borde superior
                    drawLine(
                        color = red,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(4)
                }
        ) {
            Text(
                text = "Nota",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        //búsqueda de datos
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(50.16.dp)
                .width(superBoxWidth)
                .background(color = orange)
                .drawBehind {
                    val strokeWidth = 4f

                    // Borde izquierdo
                    drawLine(
                        color = red,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )

                    // Borde inferior
                    drawLine(
                        color = red,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )

                    // Borde derecho
                    drawLine(
                        color = red,
                        start = Offset(size.width - strokeWidth / 2, 0f),
                        end = Offset(size.width - strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(5)
                }
        ) {
            Text(
                text = "Búsqueda de datos",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun search(
    onSearchByName: (String) -> Unit,
    onSearchByDNI: (String) -> Unit
) {
    var search by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(top = 30.dp, start = 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = {search = it},
            label = {
                Text(
                    text = "Nombre o D.N.I del alumno...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.6f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = blue,
                unfocusedBorderColor = blue,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray
            ),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.White
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.padding(20.dp))

        Button(
            onClick = {
                onSearchByName(search)
            },
            modifier = Modifier
                .height(50.dp)
                .align(Alignment.CenterVertically),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = black
            ),
            shape = RoundedCornerShape(100),
            border = BorderStroke(3.dp, red)
        ){
            Text(
                text = "Buscar por nombre",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))

        Button(
            onClick = {
                onSearchByDNI(search)
            },
            modifier = Modifier
                .height(50.dp)
                .align(Alignment.CenterVertically),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = black
            ),
            shape = RoundedCornerShape(100),
            border = BorderStroke(3.dp, red)
        ){
            Text(
                text = "Buscar por dni",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun textsOutPut(data: List<List<Any>>) {
    Column(
        modifier = Modifier
            .padding(
                top = 40.dp,
                start = 30.dp
            )
            .fillMaxSize()
    ) {
        //indica como se disponen los datos
        Box(
            modifier = Modifier
                .border(
                    width = 3.dp,
                    color = red,
                    shape = RoundedCornerShape(100)
                )
                .padding(
                    top = 10.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                )
            ){
            Text(
                text = "Nombre del alumno | nota | Nombre del profesor | materia",
                fontSize = 27.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn{
            items(data) { data ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 4.dp,
                            color = blue,
                            shape = RoundedCornerShape(100)
                        )
                        .padding(
                            top = 10.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 10.dp
                        )
                ) {
                    Text(
                        text = "${data[0]} | ${data[1]} | ${data[2]} | ${data[3]}",
                        fontSize = 27.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Composable
fun mainOutput(onScreenChange: (Int) -> Unit) {
    val crud = SQLiteCRUD()
    var estudiantes by remember { mutableStateOf(emptyList<List<Any>>()) }

    Scaffold(
        topBar = { inputTopAppBar() },
        backgroundColor = black
    ){
        Column{
            subOutputTopBar(onScreenChange)
            search(
                onSearchByName = { search ->
                    estudiantes = crud.selectAlumnosN(search)
                },
                onSearchByDNI = { search ->
                    estudiantes = crud.selectAlumnosD(search)
                }
            )
            textsOutPut(estudiantes)
        }
    }
}