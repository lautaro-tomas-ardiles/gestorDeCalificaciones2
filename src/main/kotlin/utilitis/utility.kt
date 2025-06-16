@file:OptIn(ExperimentalComposeUiApi::class)

package utilitis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.black
import colors.blue
import colors.orange
import colors.red

/**
 * Las funciones menuBar y menuItem se encargan se la barra que permite el cambio
 * de pantallas. Menu item tiene los items con su borde texto y aspecto, menu bar
 * contiene los items
 */
@Composable
fun menuItem(
    isSelected: Boolean,
    title: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = if (isSelected) orange else blue)
            .drawBehind {
                val strokeWidth = 5.dp.toPx()
                // Borde izquierdo
                drawLine(
                    color = if (isSelected) red else Color.Transparent,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
                // Borde derecho
                drawLine(
                    color = if (isSelected) red else Color.Transparent,
                    start = Offset(size.width - strokeWidth / 2, 0f),
                    end = Offset(size.width - strokeWidth / 2, size.height),
                    strokeWidth = strokeWidth
                )
                // Borde inferior
                drawLine(
                    color = if (isSelected) red else Color.Transparent,
                    start = Offset(0f, size.height - strokeWidth / 2),
                    end = Offset(size.width, size.height - strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
                // Borde superior
                drawLine(
                    color = if (!isSelected) red else Color.Transparent,
                    start = Offset(0f, strokeWidth / 2),
                    end = Offset(size.width, strokeWidth / 2),
                    strokeWidth = strokeWidth
                )
            }
            .clickable {
                onClick()
            }
            .padding(7.dp)
    ) {
        Text(
            text = title,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            color = if (isSelected) black else Color.White
        )
    }
}

@Composable
fun menuBar(
    onScreenChange: (Int) -> Unit,
    selectedScreen: Int
) {
    Row {
        //Alumno
        menuItem(
            isSelected = selectedScreen == 1,
            title = "Alumno",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(1)
        }
        //Profesor
        menuItem(
            isSelected = selectedScreen == 2,
            title = "Profesor",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(2)
        }
        //materia
        menuItem(
            isSelected = selectedScreen == 3,
            title = "Materia",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(3)
        }
        //nota
        menuItem(
            isSelected = selectedScreen == 4,
            title = "Nota",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(4)
        }
        //Lista de alumno
        menuItem(
            isSelected = selectedScreen == 5,
            title = "Lista de alumno",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(5)
        }
        //Lista de profesores
        menuItem(
            isSelected = selectedScreen == 6,
            title = "Lista de profesores",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(6)
        }
        //búsqueda
        menuItem(
            isSelected = selectedScreen == 7,
            title = "Búsqueda",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(7)
        }
    }
}

/**
 * La function textBar permite la entrada de texto permitiendo un aspecto consistent en las entradas de texto
 * y la function textBarForSearch es igual excepto que usa un modifier para las modificaciones
 */
@Composable
fun textBar(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontSize = MaterialTheme.typography.subtitle2.fontSize
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = blue,
            unfocusedBorderColor = blue,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            cursorColor = Color.White
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color.White
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(4.dp),
        maxLines = 1,
        trailingIcon = trailingIcon
    )
}

@Composable
fun textBarForSearch(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontSize = MaterialTheme.typography.subtitle2.fontSize
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = blue,
            unfocusedBorderColor = blue,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            cursorColor = Color.White
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color.White
        ),
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}

/**
 * Es el botón para us general dentro de la aplicación
 */
@Composable
fun button(
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        border = BorderStroke(
            width = 2.dp,
            color = blue
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent,
        ),
        content = {
            Text(
                text = label,
                color = Color.White,
                fontSize = MaterialTheme.typography.h6.fontSize
            )
        }
    )
}

/**
 * Es la barra emergente con mensajes de error y de cumplimiento?, no sé escribir
 */
@Composable
fun customSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        snackbarData = snackbarData,
        contentColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = blue,
        elevation = 15.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(0.5f)
    )
}

/**
 * Box con text bar para encontrar elementos de una lista
 */
@Composable
fun <T> selectorBox(
    label: String, // label del text bar
    expanded: Boolean, // si se expende o no en dropMenu
    onExpandedChange: (Boolean) -> Unit,// el cambio de estado(cambia si esta expandido)
    inputText: String,// texto que se ingresa
    onInputChange: (String) -> Unit,// su cambio cuando se selection un elemento
    options: List<T>,// lista de objetos del dropMenu (el tipo <T> permite que se especifique su tipo al usarlo)
    displayText: (T) -> String,// como es el texto donde se muestran las options
    onSelect: (T) -> Unit // el que se selecciona
) {
    Box {
        textBar(
            value = inputText,
            onValueChange = onInputChange,
            label = label,
            trailingIcon = {
                IconButton(
                    onClick = { onExpandedChange(!expanded) },
                    modifier = Modifier.size(35.dp)
                ) {
                    AnimatedVisibility(expanded){
                        Icon(
                            painter = painterResource("close_24dp.svg"),
                            contentDescription = "close",
                            tint = blue,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    AnimatedVisibility(!expanded){
                        Icon(
                            painter = painterResource("search_24dp.svg"),
                            contentDescription = "close",
                            tint = blue,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .background(black)
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onSelect(item)
                        onExpandedChange(false)
                    }
                ) {
                    Text(
                        text = displayText(item),
                        color = orange
                    )
                }
            }
        }
    }
}

/**
 * Box que muestra los datos de una lista
 */
@Composable
fun <T> boxOfData(data: List<T>, color: Color) {
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(10)
            )
            .padding(
                vertical = 10.dp,
                horizontal = 20.dp
            )
    ) {
        Text(
            text = data.joinToString(separator = " | ") { it.toString() },
            fontSize = MaterialTheme.typography.h6.fontSize,
            color = Color.White
        )
    }
}

/**
 * Tiene dos barras de búsqueda con sus respectivos botones uno para dni y otro para nombre
 */
@Composable
fun search(
    onSearchByName: (String) -> Unit,
    onSearchByDNI: (String) -> Unit,
    isAlumno: Boolean = true
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
        textBarForSearch(
            value = searchByName,
            onValueChange = { searchByName = it },
            label = if (isAlumno) "Nombre del alumno" else "Nombre del profesor",
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
            label = if (isAlumno) "D.N.I del alumno..." else "D.N.I del profesor...",
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