@file:OptIn(ExperimentalComposeUiApi::class)

package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.black
import colors.blue
import colors.orange
import colors.red
import sql.SQLiteCRUD

@Composable
fun profesorInputSubAppBarr(onScreenChange: (Int) -> Unit) {
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

                    // Borde izquierdo
                    drawLine(
                        color = red,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )

                    // Borde superior
                    drawLine(
                        color = red,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
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
                    onScreenChange(2)
                }
        ) {
            Text(
                text = "Profesor",
                fontSize = 20.sp,
                color = Color.Black
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
                color = Color.White
            )
        }
    }
}

@Composable
fun profesorInputInsert() {
    var nombreDelProfesor by remember { mutableStateOf("") }
    var dniDelProfesor by remember { mutableStateOf("") }

    val crud = SQLiteCRUD()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nombreDelProfesor,
            onValueChange = { nombreDelProfesor = it },
            label = {
                Text(
                    text = "Nombre del profesor...",
                    fontSize = 20.sp
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = blue,
                unfocusedBorderColor = blue,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray
            ),
            textStyle = TextStyle(
                fontSize = 17.sp,
                color = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
        )
        Spacer(modifier = Modifier.padding(31.5.dp))

        OutlinedTextField(
            value = dniDelProfesor,
            onValueChange = { dniDelProfesor = it },
            label = {
                Text(
                    text = "D.N.I del profesor...",
                    fontSize = 20.sp
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = blue,
                unfocusedBorderColor = blue,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray
            ),
            textStyle = TextStyle(
                fontSize = 17.sp,
                color = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
        )
        Spacer(modifier = Modifier.padding(31.5.dp))

        OutlinedButton(
            onClick = {
                try {
                    crud.insertProfesores(
                        nombreCompletoP = nombreDelProfesor,
                        dniP = dniDelProfesor
                    )
                    nombreDelProfesor = ""
                    dniDelProfesor = ""
                } catch (e: Exception) {
                    println(e.message)
                    println(e.localizedMessage)
                    print(e.cause)
                }
            },
            shape = RoundedCornerShape(40.dp),
            border = BorderStroke(
                width = 2.dp,
                color = blue
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Transparent,
            ),
            content = {
                Text(
                    text = "Añadir",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        )
    }
}

@Composable
fun mainInputProfesor(onScreenChange: (Int) -> Unit) {
    Scaffold(
        topBar = {
            inputTopAppBar()
        },
        backgroundColor = black
    ) {
        Column{
            profesorInputSubAppBarr(onScreenChange)
            profesorInputInsert()
        }
    }
}