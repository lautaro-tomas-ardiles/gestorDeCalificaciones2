package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import colors.*
import sql.SQLiteCRUD

@Composable
fun inputTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Gestor de notas",
                fontSize = 30.sp,
                color = Color.White
            )
        },
        backgroundColor = green,
        modifier = Modifier.height(70.dp),
        elevation = 900.dp
    )
}

@Composable
fun subInputTopBar(onScreenChange: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = blue,
                )
                .background(red)
                .fillMaxWidth(0.5f)
                .height(60.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Ingreso de datos",
                fontSize = 26.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = red,
                )
                .fillMaxWidth(1f)
                .height(60.dp)
                .clickable { onScreenChange() },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Salida de datos",
                fontSize = 26.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun textsField() {
    val scroll = rememberScrollState()
    val crud = SQLiteCRUD()

    var alumno by remember { mutableStateOf("") }
    var dniAlumno by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var profesor by remember { mutableStateOf("") }
    var dniProfesor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = alumno,
            onValueChange = {alumno = it},
            label = {
                Text(
                    text = "Alumno...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
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
        Spacer(modifier = Modifier.size(25.dp))

        OutlinedTextField(
            value = dniAlumno,
            onValueChange = {dniAlumno = it},
            label = {
                Text(
                    text = "D.N.I del alumno...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
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

        Spacer(modifier = Modifier.size(25.dp))

        OutlinedTextField(
            value = nota,
            onValueChange = {nota = it},
            label = {
                Text(
                    text = "Nota...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
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
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.size(25.dp))

        OutlinedTextField(
            value = profesor,
            onValueChange = {profesor = it},
            label = {
                Text(
                    text = "Profesor...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
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
        Spacer(modifier = Modifier.size(25.dp))

        OutlinedTextField(
            value = dniProfesor,
            onValueChange = {dniProfesor = it},
            label = {
                Text(
                    text = "dni del profesor...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
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
        Spacer(modifier = Modifier.size(25.dp))

        OutlinedTextField(
            value = materia,
            onValueChange = {materia = it},
            label = {
                Text(
                    text = "Materia...",
                    fontSize = 24.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
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
        Spacer(modifier = Modifier.size(25.dp))

        Button(
            onClick = {
                try {
                    val notaDouble = nota.toDoubleOrNull() ?: 0.0
                    crud.insertAlumnos(
                        nombreCompletoA =  alumno,
                        dniA =  dniAlumno
                    )
                    crud.insertProfesores(
                        nombreCompletoP = profesor,
                        dniP = dniProfesor
                    )
                    crud.insertMaterias(
                        dniP = dniProfesor,
                        materia = materia
                    )
                    crud.insertNotas(
                        nota =  notaDouble,
                        dniA = dniAlumno,
                        dniP = dniProfesor
                    )
                }catch (e: Exception){
                    println(e.message)
                }
            },
            modifier = Modifier
                .height(50.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = black
            ),
            shape = RoundedCornerShape(100),
            border = BorderStroke(3.dp, red)
        ){
            Text(
                text = "Añadir",
                fontSize = 24.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.size(25.dp))

    }
}

@Composable
fun inputMain(onScreenChange: () -> Unit) {
    Scaffold(
        topBar = { inputTopAppBar() },
        backgroundColor = black
    ){
        Column{
            subInputTopBar(onScreenChange)
            textsField()
        }
    }
}
