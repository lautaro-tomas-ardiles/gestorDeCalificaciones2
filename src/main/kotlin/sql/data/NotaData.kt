package sql.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotaData(
    @SerialName("dni_p")
    val dniP: String,
    val nota: Double?,
    @SerialName("dni_a")
    val dniA: String,
    @SerialName("materia_id")
    val materiaId: Int?,
    @SerialName("nota_id")
    val notaId: Int? = null
)