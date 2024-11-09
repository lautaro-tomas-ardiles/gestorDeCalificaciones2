package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.*
import sql.SQLiteCRUD

@Composable
fun SubOutputTopBar(onScreenChange: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = red,
                )
                .fillMaxWidth(0.5f)
                .height(60.dp)
                .clickable { onScreenChange() },
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
                    color = blue,
                )
                .background(red)
                .fillMaxWidth(1f)
                .height(60.dp),
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
fun Search(
    onSearchByName: (String) -> Unit,
    onSearchByDNI: (String) -> Unit
) {
    var busqueda by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(top = 30.dp, start = 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = {busqueda = it},
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
                onSearchByName(busqueda)
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
                onSearchByDNI(busqueda)
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
fun TextsOutPut(data: List<List<Any>>) {
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
fun OutputMain(onScreenChange: () -> Unit) {
    val crud = SQLiteCRUD()
    var estudiantes by remember { mutableStateOf(emptyList<List<Any>>()) }

    Scaffold(
        topBar = { inputTopAppBar() },
        backgroundColor = black
    ){
        Column{
            SubOutputTopBar(onScreenChange)
            Search(
                onSearchByName = { busqueda ->
                    estudiantes = crud.selectAlumnosN(busqueda)
                },
                onSearchByDNI = { busqueda ->
                    estudiantes = crud.selectAlumnosD(busqueda)
                }
            )
            TextsOutPut(estudiantes)
        }
    }
}