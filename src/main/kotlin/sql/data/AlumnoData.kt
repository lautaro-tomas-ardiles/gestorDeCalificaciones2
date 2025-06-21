package sql.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlumnoData(
    @SerialName("nombre_a")
    val nombreA: String,
    @SerialName("dni_a")
    val dniA: String
)