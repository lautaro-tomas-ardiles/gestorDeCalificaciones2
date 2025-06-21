package sql.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MateriaData(
    val materia: String,
    @SerialName("dni_p")
    val dniP: String,
    @SerialName("materia_id")
    val materiaId: Int? = null
)