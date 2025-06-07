@file:OptIn(ExperimentalComposeUiApi::class)

package utilitis

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.*

@Composable
fun menuBar(
    onScreenChange: (Int) -> Unit,
    selectedScreen: Int
) {
    // Obtener el ancho de la pantalla
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp
    val boxWidth = screenWidth / 8 //ancho de las cajas
    val superBoxWidth = screenWidth / 2 //la ultima
    Row {
        //alumno
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(boxWidth)
                .background(color = if (selectedScreen == 1) orange else blue)
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()
                    // Borde izquierdo
                    drawLine(
                        color = red,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth * 2
                    )
                    // Borde derecho
                    drawLine(
                        color = if (selectedScreen == 1) red else Color.Transparent,
                        start = Offset(size.width - strokeWidth / 2, 0f),
                        end = Offset(size.width - strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde inferior
                    drawLine(
                        color = if (selectedScreen == 1) red else Color.Transparent,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                    // Borde superior
                    drawLine(
                        color = if (selectedScreen != 1) red else Color.Transparent,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(1)
                }
                .padding(7.dp)
        ) {
            Text(
                text = "Alumno",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = if (selectedScreen == 1) black else Color.White
            )
        }
        //profesor
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(boxWidth)
                .background(color = if (selectedScreen == 2) orange else blue)
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()

                    // Borde izquierdo
                    drawLine(
                        color = if (selectedScreen == 2) red else Color.Transparent,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde derecho
                    drawLine(
                        color = if (selectedScreen == 2) red else Color.Transparent,
                        start = Offset(size.width - strokeWidth / 2, 0f),
                        end = Offset(size.width - strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde inferior
                    drawLine(
                        color = if (selectedScreen == 2) red else Color.Transparent,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                    // Borde superior
                    drawLine(
                        color = if (selectedScreen != 2) red else Color.Transparent,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(2)
                }
                .padding(7.dp)
        ) {
            Text(
                text = "Profesor",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = if (selectedScreen == 2) black else Color.White
            )
        }
        //materia
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(boxWidth)
                .background(color = if (selectedScreen == 3) orange else blue)
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()

                    // Borde izquierdo
                    drawLine(
                        color = if (selectedScreen == 3) red else Color.Transparent,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde derecho
                    drawLine(
                        color = if (selectedScreen == 3) red else Color.Transparent,
                        start = Offset(size.width - strokeWidth / 2, 0f),
                        end = Offset(size.width - strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde inferior
                    drawLine(
                        color = if (selectedScreen == 3) red else Color.Transparent,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                    // Borde superior
                    drawLine(
                        color = if (selectedScreen != 3) red else Color.Transparent,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(3)
                }
                .padding(7.dp)
        ) {
            Text(
                text = "Materia",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = if (selectedScreen == 3) black else Color.White
            )
        }
        //nota
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(boxWidth)
                .background(color = if (selectedScreen == 4) orange else blue)
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()

                    // Borde izquierdo
                    drawLine(
                        color = if (selectedScreen == 4) red else Color.Transparent,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde derecho
                    drawLine(
                        color = if (selectedScreen == 4) red else Color.Transparent,
                        start = Offset(size.width - strokeWidth / 2, 0f),
                        end = Offset(size.width - strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde inferior
                    drawLine(
                        color = if (selectedScreen == 4) red else Color.Transparent,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                    // Borde superior
                    drawLine(
                        color = if (selectedScreen != 4) red else Color.Transparent,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(4)
                }
                .padding(7.dp)
        ) {
            Text(
                text = "Nota",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = if (selectedScreen == 4) black else Color.White
            )
        }
        //búsqueda
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(superBoxWidth)
                .background(color = if (selectedScreen == 5) orange else blue)
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()

                    // Borde superior
                    drawLine(
                        color = if (selectedScreen != 5) red else Color.Transparent,
                        start = Offset(0f, strokeWidth / 2),
                        end = Offset(size.width, strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )

                    // Borde derecho
                    drawLine(
                        color = if (selectedScreen != 5) red else Color.Transparent,
                        start = Offset(size.width - strokeWidth / 2, 0f),
                        end = Offset(size.width - strokeWidth / 2, size.height),
                        strokeWidth = strokeWidth * 2
                    )
                    // Borde izquierdo
                    drawLine(
                        color = if (selectedScreen == 5) red else Color.Transparent,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Borde inferior
                    drawLine(
                        color = if (selectedScreen == 5) red else Color.Transparent,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                }
                .clickable {
                    onScreenChange(5)
                }
                .padding(7.dp)
        ) {
            Text(
                text = "Búsqueda de datos",
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = if (selectedScreen == 5) black else Color.White
            )
        }

    }
}

@Composable
fun textBar(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: (@Composable (() -> Unit))? = null,
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
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

@Composable
fun dropdownSelector(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    opciones: List<List<Any>>,
    onItemSelected: (String, String) -> Unit,
    getLabel: (List<Any>) -> String
) {
    Box {
        textBar(
            value = value,
            onValueChange = {
                onValueChange(it)
                onExpandedChange(true)
            },
            label = label,
            trailingIcon = {
                IconButton(onClick = { onExpandedChange(!expanded) }) {
                    Icon(
                        painter = painterResource(if (expanded) "close.svg" else "search.svg"),
                        contentDescription = "search",
                        tint = orange,
                        modifier = Modifier.size(30.dp)
                    )
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
            opciones.forEach {
                val nombre = it[0] as String
                val id = it[1] as String

                DropdownMenuItem(onClick = {
                    onItemSelected(nombre, id)
                    onExpandedChange(false)
                }) {
                    Text(text = getLabel(it), color = orange)
                }
            }
        }
    }
}


@Composable
fun addButton(
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
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

@Composable
fun selectorBox(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    inputText: String,
    onInputChange: (String) -> Unit,
    options: List<List<Any>>,
    displayText: (List<Any>) -> String,
    onSelect: (List<Any>) -> Unit
) {
    Box {
        textBar(
            value = inputText,
            onValueChange = onInputChange,
            label = label,
            trailingIcon = {
                IconButton(
                    onClick = { onExpandedChange(!expanded) },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = if (expanded) painterResource("close.svg") else painterResource("search.svg"),
                        contentDescription = "search",
                        tint = orange,
                        modifier = Modifier.size(30.dp)
                    )
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
                    onClick = { onSelect(item) }
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
