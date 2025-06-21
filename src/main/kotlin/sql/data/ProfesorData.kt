package sql.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfesorData(
    @SerialName("nombre_p")
    val nombreP: String,
    @SerialName("dni_p")
    val dniP: String
)