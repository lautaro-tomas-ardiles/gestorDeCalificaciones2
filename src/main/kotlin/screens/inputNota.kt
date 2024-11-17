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
import androidx.compose.ui.res.painterResource
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
fun notaInputSubAppBar(onScreenChange: (Int) -> Unit) {
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
                fontSize = 24.sp
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
                fontSize = 24.sp
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
                fontSize = 24.sp
            )
        }
        //nota
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
                    onScreenChange(4)
                }
        ) {
            Text(
                text = "Nota",
                fontSize = 24.sp
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
                fontSize = 24.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun notaInputInsert() {
    var nota by remember { mutableStateOf("") }
    var selectedProfesor by remember { mutableStateOf("") }
    var selectedAlumno by remember { mutableStateOf("") }
    var selectedMateria by remember { mutableStateOf("") }

    var expandedProfesor by remember { mutableStateOf(false) }
    var expandedAlumno by remember { mutableStateOf(false) }
    var expandedMateria by remember { mutableStateOf(false) }

    val crud = SQLiteCRUD()
    val profesores = crud.selectProfesores()
    val alumno = crud.selectAlumnos()
    val materias = crud.selectMaterias()

    var dniProfesor = ""
    var dniAlumno = ""

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nota,
            onValueChange = { nota = it },
            label = {
                Text(
                    text = "Nombre de la materia...",
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

        ExposedDropdownMenuBox(//caja del drop menu
            expanded = expandedAlumno,
            onExpandedChange = {
                expandedAlumno = !expandedAlumno
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ){
            OutlinedTextField(//text field
                value = selectedAlumno,
                onValueChange = {  },
                label = {
                    Text(
                        text = "Seleccione alumno...",
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
                    .fillMaxWidth(),
                trailingIcon = {
                    if (expandedAlumno){
                        Icon(
                            painter = painterResource("Chevron up.svg"),
                            contentDescription = "flecha",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }else {
                        Icon(
                            painter = painterResource("Chevron down.svg"),
                            contentDescription = "flecha",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }

                    ExposedDropdownMenuDefaults.TrailingIcon(//action de abrir o cerrar el menu
                        expanded = expandedAlumno
                    )
                }
            )
            ExposedDropdownMenu(//el menu en si
                expanded = expandedAlumno,
                onDismissRequest = {
                    expandedAlumno = false
                },
                modifier = Modifier
                    .background(black)
            ){
                alumno.forEach { alumno ->
                    val nombre = alumno[0] as String
                    val dni = alumno[1] as String
                    DropdownMenuItem(//los items
                        onClick = {
                            selectedAlumno = "nombre: $nombre | dni: $dni"
                            expandedAlumno = false
                            dniAlumno = dni
                        }
                    ) {
                        Text(
                            text = "nombre: $nombre | dni: $dni",
                            color = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(31.5.dp))

        ExposedDropdownMenuBox(//caja del drop menu
            expanded = expandedProfesor,
            onExpandedChange = {
                expandedProfesor = !expandedProfesor
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ){
            OutlinedTextField(//text field
                value = selectedProfesor,
                onValueChange = {  },
                label = {
                    Text(
                        text = "Seleccione profesor...",
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
                    .fillMaxWidth(),
                trailingIcon = {
                    if (expandedProfesor){
                        Icon(
                            painter = painterResource("Chevron up.svg"),
                            contentDescription = "flecha",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }else {
                        Icon(
                            painter = painterResource("Chevron down.svg"),
                            contentDescription = "flecha",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }

                    ExposedDropdownMenuDefaults.TrailingIcon(//action de abrir o cerrar el menu
                        expanded = expandedProfesor
                    )
                }
            )
            ExposedDropdownMenu(//el menu en si
                expanded = expandedProfesor,
                onDismissRequest = {
                    expandedProfesor = false
                },
                modifier = Modifier
                    .background(black)
            ){
                profesores.forEach { profesor ->
                    val nombre = profesor[0] as String
                    val dni = profesor[1] as String
                    DropdownMenuItem(//los items
                        onClick = {
                            selectedProfesor = "nombre: $nombre | dni: $dni"
                            expandedProfesor = false
                            dniProfesor = dni
                        }
                    ) {
                        Text(
                            text = "nombre: $nombre | dni: $dni",
                            color = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(31.5.dp))

        ExposedDropdownMenuBox(//caja del drop menu
            expanded = expandedMateria,
            onExpandedChange = {
                expandedMateria = !expandedMateria
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ){
            OutlinedTextField(//text field
                value = selectedMateria,
                onValueChange = {  },
                label = {
                    Text(
                        text = "Seleccione materia...",
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
                    .fillMaxWidth(),
                trailingIcon = {
                    if (expandedMateria){
                        Icon(
                            painter = painterResource("Chevron up.svg"),
                            contentDescription = "flecha",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }else {
                        Icon(
                            painter = painterResource("Chevron down.svg"),
                            contentDescription = "flecha",
                            tint = Color.White,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }

                    ExposedDropdownMenuDefaults.TrailingIcon(//action de abrir o cerrar el menu
                        expanded = expandedMateria
                    )
                }
            )
            ExposedDropdownMenu(//el menu en si
                expanded = expandedMateria,
                onDismissRequest = {
                    expandedMateria = false
                },
                modifier = Modifier
                    .background(black)
            ){
                materias.forEach { materias ->
                    val nombreM = materias[0] as String
                    val dni = materias[1] as String
                    DropdownMenuItem(//los items
                        onClick = {
                            selectedMateria = "nombre de la materia: $nombreM |  dni del profesor: $dni"
                            expandedMateria = false
                            dniProfesor = dni
                        }
                    ) {
                        Text(
                            text = "nombre: $nombreM | dni: $dni",
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(31.5.dp))

        OutlinedButton(
            onClick = {
                try {
                    crud.insertNotas(
                        dniP = dniProfesor,
                        dniA = dniAlumno,
                        nota = nota.toDoubleOrNull()?: 0.0
                    )
                    selectedAlumno = ""
                    selectedMateria = ""
                    selectedProfesor = ""

                }catch (e: Exception){
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
fun mainInputNota(onScreenChange: (Int) -> Unit) {
    Scaffold(
        topBar = {
            inputTopAppBar()
        },
        backgroundColor = black
    ) {
        Column {
            notaInputSubAppBar(onScreenChange)
            notaInputInsert()
        }
    }    
}