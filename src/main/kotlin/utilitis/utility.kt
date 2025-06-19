@file:OptIn(ExperimentalComposeUiApi::class)

package utilitis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.black
import colors.blue
import colors.orange
import colors.red

/**
 * Componente que representa un ítem del menú de navegación.
 *
 * @param isSelected Indica si este ítem está actualmente seleccionado.
 * @param title Texto que se muestra dentro del ítem.
 * @param modifier Modificador de estilo y comportamiento.
 * @param onClick Acción a ejecutar al hacer clic sobre el ítem.
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

/**
 * Barra de navegación horizontal que permite cambiar entre distintas pantallas.
 *
 * @param selectedScreen Índice de la pantalla actualmente seleccionada.
 * @param onScreenChange Callback que se ejecuta al seleccionar una nueva pantalla.
 */
@Composable
fun menuBar(
    selectedScreen: Int,
    onScreenChange: (Int) -> Unit
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
        //Lista de materias
        menuItem(
            isSelected = selectedScreen == 7,
            title = "Lista de materias",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(7)
        }
        //búsqueda
        menuItem(
            isSelected = selectedScreen == 8,
            title = "Búsqueda",
            modifier = Modifier.weight(1f)
        ) {
            onScreenChange(8)
        }
    }
}

/**
 * Campo de entrada de texto reutilizable con estilo personalizado.
 *
 * @param value Texto actual del campo.
 * @param onValueChange Callback que se ejecuta cuando el texto cambia.
 * @param label Etiqueta visible dentro del campo.
 * @param trailingIcon Ícono opcional que aparece al final del campo.
 * @param modifier Modificador de estilo y tamaño.
 */
@Composable
fun textBar(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    trailingIcon: (@Composable (() -> Unit))? = null,
    modifier: Modifier = Modifier.fillMaxWidth(0.5f)
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
        modifier = modifier
            .padding(4.dp),
        maxLines = 1,
        trailingIcon = trailingIcon
    )
}

/**
 * Botón estilizado reutilizable en la aplicación.
 *
 * @param label Texto del botón.
 * @param onClick Acción a ejecutar al hacer clic en el botón.
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
 * Snackbar personalizado para mostrar mensajes de notificación.
 *
 * @param snackbarData Datos del snackbar, como el mensaje y duración.
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
 * Componente que permite buscar y seleccionar elementos de una lista mediante un campo de texto
 * y un menú desplegable (DropdownMenu).
 *
 * @param T Tipo genérico de los elementos en la lista.
 * @param label Etiqueta que se muestra en el campo de texto.
 * @param expanded Indica si el menú desplegable está visible.
 * @param onExpandedChange Callback que actualiza el estado de expansión del menú.
 * @param inputText Texto actual ingresado por el usuario.
 * @param onInputChange Callback que se ejecuta cuando el texto de entrada cambia.
 * @param options Lista de elementos a mostrar en el menú desplegable.
 * @param displayText Función que convierte cada elemento en una cadena para mostrar.
 * @param onSelect Callback que se ejecuta al seleccionar un elemento del menú.
 */
@Composable
fun <T> selectorBox(
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    inputText: String,
    onInputChange: (String) -> Unit,
    options: List<T>,
    displayText: (T) -> String,
    onSelect: (T) -> Unit
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
                    AnimatedVisibility(
                        visible = expanded,
                        enter = scaleIn(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            initialScale = 0.0f,
                            transformOrigin = TransformOrigin.Center
                        ),
                        exit = scaleOut(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            targetScale = 0.0f,
                            transformOrigin = TransformOrigin.Center
                        )
                    ) {
                        Icon(
                            painter = painterResource("close_24dp.svg"),
                            contentDescription = "close",
                            tint = orange,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = !expanded,
                        enter = scaleIn(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            initialScale = 0.0f,
                            transformOrigin = TransformOrigin.Center
                        ),
                        exit = scaleOut(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            targetScale = 0.0f,
                            transformOrigin = TransformOrigin.Center
                        )
                    ) {
                        Icon(
                            painter = painterResource("search_24dp.svg"),
                            contentDescription = "search",
                            tint = orange,
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
                AnimatedVisibility(
                    visible = true, // visible siempre, pero animado al aparecer
                    enter = scaleIn(
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        initialScale = 0.0f,
                        transformOrigin = TransformOrigin.Center
                    ),
                    exit = scaleOut(
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        targetScale = 0.0f,
                        transformOrigin = TransformOrigin.Center
                    )
                ) {
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
}

/**
 * Componente para mostrar una lista de datos como texto en una caja decorada.
 *
 * @param T Tipo de datos en la lista.
 * @param data Lista de datos a mostrar.
 * @param color Color del borde de la caja.
 */
@Composable
fun <T> boxOfData(data: List<T>, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
 * Componente con dos campos de búsqueda y sus respectivos botones.
 * Permite buscar por nombre o por DNI.
 *
 * @param onSearchByName Acción que se ejecuta con el texto del nombre.
 * @param onSearchByDNI Acción que se ejecuta con el texto del DNI.
 * @param isAlumno Indica si se está buscando un alumno o un profesor.
 * @param isMateria Indica si se esta buscando materia
 */
@Composable
fun search(
    onSearchByName: (String) -> Unit,
    onSearchByDNI: (String) -> Unit,
    isAlumno: Boolean = true,
    isMateria: Boolean = false
) {
    var searchByName by remember { mutableStateOf("") }
    var searchByDNI by remember { mutableStateOf("") }

    val labelForNombre =
        if (isAlumno) {
            "Nombre del alumno"
        } else if (isMateria) {
            "Nombre de la materia"
        } else {
            "Nombre del profesor"
        }

    val labelForDNI =
        if (isAlumno) {
            "D.N.I del alumno..."
        } else if (isMateria) {
            "D.N.I del profesor..."
        } else {
            "D.N.I del profesor..."
        }

    Row(
        modifier = Modifier
            .padding(vertical = 30.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        textBar(
            value = searchByName,
            onValueChange = { searchByName = it },
            label = labelForNombre,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(14.dp))

        button(
            label = "Búsqueda por nombre"
        ) {
            onSearchByName(searchByName)
        }
        Spacer(modifier = Modifier.padding(14.dp))

        textBar(
            value = searchByDNI,
            onValueChange = { searchByDNI = it },
            label = labelForDNI,
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