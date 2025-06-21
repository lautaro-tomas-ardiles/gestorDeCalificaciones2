package sql.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OutPutData(
    @SerialName("dni_p")
    val dniP: NestedProfesor,
    val nota: Double,
    @SerialName("dni_a")
    val dniA: NestedName,
    @SerialName("materia_id")
    val materiaId: NestedMateria,
    @SerialName("nota_id")
    val notaId: Int
)

@Serializable
data class NestedName(
    @SerialName("nombre_a")
    val nombreA: String
)

@Serializable
data class NestedProfesor(
    @SerialName("nombre_p")
    val nombreP: String
)

@Serializable
data class NestedMateria(
    @SerialName("materia")
    val materia: String
)