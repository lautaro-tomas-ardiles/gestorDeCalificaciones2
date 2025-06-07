@file:OptIn(ExperimentalComposeUiApi::class)

package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.black
import colors.blue
import colors.orange
import colors.red
import sql.SQLiteCRUD
import utilitis.menuBar

@Composable
fun search(
    onSearchByName: (String) -> Unit,
    onSearchByDNI: (String) -> Unit
) {
    var searchByName by remember { mutableStateOf("") }
    var searchByDNI by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(vertical = 30.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = searchByName,
            onValueChange = { searchByName = it },
            label = {
                Text(
                    text = "Nombre del alumno...",
                    fontSize = MaterialTheme.typography.h6.fontSize
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
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(14.5.dp))

        OutlinedButton(
            onClick = {
                onSearchByName(searchByName)
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
                    text = "Buscar por nombre",
                    color = Color.White,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.padding(14.5.dp))

        OutlinedTextField(
            value = searchByDNI,
            onValueChange = { searchByDNI = it },
            label = {
                Text(
                    text = "D.N.I del alumno...",
                    fontSize = MaterialTheme.typography.h6.fontSize
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
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
                //.width(470.dp)
        )
        Spacer(modifier = Modifier.padding(14.5.dp))

        OutlinedButton(
            onClick = {
                onSearchByDNI(searchByDNI)
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
                    text = "Buscar por dni",
                    color = Color.White,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        )
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
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = red,
                    shape = RoundedCornerShape(10)
                )
                .padding(
                    vertical = 10.dp,
                    horizontal = 20.dp
                )
            ){
            Text(
                text = "Nombre del alumno | nota | Nombre del profesor | materia",
                fontSize = MaterialTheme.typography.h5.fontSize,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))

        LazyColumn{
            items(data) { data ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = orange,
                            shape = RoundedCornerShape(10)
                        )
                        .padding(
                            vertical = 10.dp,
                            horizontal = 20.dp
                        )
                ) {
                    Text(
                        text = "${data[0]} | ${data[1]} | ${data[2]} | ${data[3]}",
                        fontSize = MaterialTheme.typography.h5.fontSize,
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
        backgroundColor = black
    ){
        Column{
            menuBar(onScreenChange, 5)
            search(
                onSearchByName = { search ->
                    estudiantes = crud.selectAlumnosByNameAndNotas(search)
                },
                onSearchByDNI = { search ->
                    estudiantes = crud.selectAlumnosByDNIAndNotas(search)
                }
            )
            textsOutPut(estudiantes)
        }
    }
}