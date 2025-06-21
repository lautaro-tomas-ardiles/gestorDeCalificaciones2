@file:OptIn(SupabaseInternal::class)

package sql

import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import sql.data.*

object DBConstants {
    // Tablas
    const val TABLE_ALUMNOS = "alumnos"
    const val TABLE_PROFESORES = "profesores"
    const val TABLE_MATERIAS = "materias"
    const val TABLE_NOTAS = "notas"

    // alumnos
    const val COLUMN_DNI_A = "dni_a"
    const val COLUMN_NOMBRE_A = "nombre_a"

    // profesores
    const val COLUMN_DNI_P = "dni_p"
    const val COLUMN_NOMBRE_P = "nombre_p"

    // materias
    const val COLUMN_MATERIA_ID = "materia_id"
    const val COLUMN_MATERIA = "materia"

    // notas
    const val COLUMN_NOTA_ID = "nota_id"
    const val COLUMN_NOTA = "nota"
}

class SupabaseCRUD {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://uuqekpukehirlggauzvm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InV1cWVrcHVrZWhpcmxnZ2F1enZtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTAzNTI5MjgsImV4cCI6MjA2NTkyODkyOH0.lKcr0VOLUvpZ0Kd-Mq6X370tet9_mqflHhhOymERPlM"
    ) {
        install(Postgrest) {
            defaultSchema = "public"
            propertyConversionMethod = PropertyConversionMethod.SERIAL_NAME
        }
    }

    // Las funciones insert ya están con try-catch
    // Reutilizadas sin cambios

    suspend fun insertAlumnos(data: AlumnoData) {
        try {
            supabase.from(DBConstants.TABLE_ALUMNOS).insert(data)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun insertProfesores(data: ProfesorData) {
        try {
            supabase.from(DBConstants.TABLE_PROFESORES).insert(data)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun insertMaterias(data: MateriaData) {
        try {
            supabase.from(DBConstants.TABLE_MATERIAS).insert(data)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun insertNotas(data: NotaData) {
        try {
            supabase.from(DBConstants.TABLE_NOTAS).insert(data)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun selectAlumnos(): List<AlumnoData> {
        try {
            val result = supabase.from(DBConstants.TABLE_ALUMNOS)
                .select(
                    Columns.raw(
                        """
                            ${DBConstants.COLUMN_NOMBRE_A},
                            ${DBConstants.COLUMN_DNI_A}
                        """.trimIndent()
                    )
                )
                .decodeList<AlumnoData>()
            return result
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun selectProfesores(): List<ProfesorData> {
        try {
            val result = supabase.from(DBConstants.TABLE_PROFESORES)
                .select(
                    Columns.raw(
                        """
                            ${DBConstants.COLUMN_NOMBRE_P},
                            ${DBConstants.COLUMN_DNI_P}
                        """.trimIndent()
                    )
                )
                .decodeList<ProfesorData>()
            return result
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun selectMaterias(): List<MateriaData> {
        try {
            val result = supabase.from(DBConstants.TABLE_MATERIAS)
                .select(
                    Columns.raw(
                        """
                            ${DBConstants.COLUMN_MATERIA},
                            ${DBConstants.COLUMN_DNI_P},
                            ${DBConstants.COLUMN_MATERIA_ID}
                        """.trimIndent()
                    )
                )
                .decodeList<MateriaData>()
            return result
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun selectAlumnosAndNotasByName(nombreCompletoA: String): List<OutPutData> {
        val filtro = "%$nombreCompletoA%"
        try {
            return supabase
                .from(DBConstants.TABLE_NOTAS)
                .select(
                    Columns.raw(
                        """
                        ${DBConstants.COLUMN_DNI_P} (
                            ${DBConstants.COLUMN_NOMBRE_P}
                        ),
                        ${DBConstants.COLUMN_NOTA},
                        ${DBConstants.COLUMN_DNI_A} (
                            ${DBConstants.COLUMN_NOMBRE_A}
                        ),
                        ${DBConstants.COLUMN_MATERIA_ID} (
                            ${DBConstants.COLUMN_MATERIA}
                        ),
                        ${DBConstants.COLUMN_NOTA_ID}
                """.trimIndent()
                    )
                ) {
                    filter {
                        like("${DBConstants.COLUMN_DNI_A}.${DBConstants.COLUMN_NOMBRE_A}", filtro)
                    }
                }.decodeList<OutPutData>()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun selectAlumnosAndNotasByDNI(dni: String): List<OutPutData> {
        val filtro = "%$dni%"
        try {
            return supabase
                .from(DBConstants.TABLE_NOTAS)
                .select(
                    Columns.raw(
                        """
                        ${DBConstants.COLUMN_DNI_P} (
                            ${DBConstants.COLUMN_NOMBRE_P}
                        ),
                        ${DBConstants.COLUMN_NOTA},
                        ${DBConstants.COLUMN_DNI_A} (
                            ${DBConstants.COLUMN_NOMBRE_A}
                        ),
                        ${DBConstants.COLUMN_MATERIA_ID} (
                            ${DBConstants.COLUMN_MATERIA}
                        ),
                        ${DBConstants.COLUMN_NOTA_ID}
                """.trimIndent()
                    )
                ) {
                    filter {
                        like(DBConstants.COLUMN_DNI_A, filtro)
                    }
                }.decodeList<OutPutData>()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }


    suspend fun deleteAlumnoByDNI(dniA: String) {
        try {
            supabase.from(DBConstants.TABLE_ALUMNOS).delete {
                filter {
                    eq(DBConstants.COLUMN_DNI_A, dniA)
                }
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun deleteProfesorByDNI(dniP: String) {
        try {
            supabase.from(DBConstants.TABLE_NOTAS).delete {
                filter { eq(DBConstants.COLUMN_DNI_P, dniP) }
            }
            supabase.from(DBConstants.TABLE_MATERIAS).delete {
                filter { eq(DBConstants.COLUMN_DNI_P, dniP) }
            }
            supabase.from(DBConstants.TABLE_PROFESORES).delete {
                filter { eq(DBConstants.COLUMN_DNI_P, dniP) }
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun deleteMateriasById(materiaId: Int) {
        try {
            supabase.from(DBConstants.TABLE_NOTAS).delete {
                filter { eq(DBConstants.COLUMN_MATERIA_ID, materiaId) }
            }
            supabase.from(DBConstants.TABLE_MATERIAS).delete {
                filter { eq(DBConstants.COLUMN_MATERIA_ID, materiaId) }
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun deleteNotaById(notaId: Int) {
        try {
            supabase.from(DBConstants.TABLE_NOTAS).delete {
                filter { eq(DBConstants.COLUMN_NOTA_ID, notaId) }
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun listOfAlumnosByDNI(dniA: String): List<AlumnoData> {
        try {
            val filtro = "%$dniA%"
            return supabase.from(DBConstants.TABLE_ALUMNOS)
                .select(Columns.list("${DBConstants.COLUMN_NOMBRE_A},${DBConstants.COLUMN_DNI_A}")) {
                    filter { like(DBConstants.COLUMN_DNI_A, filtro) }
                }.decodeList()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun listOfAlumnosByNombre(name: String): List<AlumnoData> {
        try {
            val filtro = "%$name%"
            return supabase.from(DBConstants.TABLE_ALUMNOS)
                .select(Columns.list("${DBConstants.COLUMN_NOMBRE_A},${DBConstants.COLUMN_DNI_A}")) {
                    filter { like(DBConstants.COLUMN_NOMBRE_A, filtro) }
                }.decodeList()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun listOfProfesoresByDNI(dniP: String): List<ProfesorData> {
        try {
            val filtro = "%$dniP%"
            return supabase.from(DBConstants.TABLE_PROFESORES)
                .select(Columns.list("${DBConstants.COLUMN_NOMBRE_P},${DBConstants.COLUMN_DNI_P}")) {
                    filter { like(DBConstants.COLUMN_DNI_P, filtro) }
                }.decodeList()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun listOfProfesoresByNombre(nombreP: String): List<ProfesorData> {
        try {
            val filtro = "%$nombreP%"
            return supabase.from(DBConstants.TABLE_PROFESORES)
                .select(Columns.list("${DBConstants.COLUMN_NOMBRE_P}, ${DBConstants.COLUMN_DNI_P}")) {
                    filter { like(DBConstants.COLUMN_NOMBRE_P, filtro) }
                }.decodeList()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun listOfMateriasByDNI(dniP: String): List<MateriaData> {
        try {
            val filtro = "%$dniP%"
            return supabase.from(DBConstants.TABLE_MATERIAS)
                .select(
                    Columns.raw(
                        """
                    ${DBConstants.COLUMN_MATERIA},
                    ${DBConstants.COLUMN_DNI_P},
                    ${DBConstants.COLUMN_MATERIA_ID}
                """.trimIndent()
                    )
                ) {
                    filter { like(DBConstants.COLUMN_DNI_P, filtro) }
                }.decodeList()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    suspend fun listOfMateriasByNombre(name: String): List<MateriaData> {
        try {
            val filtro = "%$name%"
            return supabase.from(DBConstants.TABLE_MATERIAS)
                .select(Columns.list("${DBConstants.COLUMN_MATERIA},${DBConstants.COLUMN_DNI_P},${DBConstants.COLUMN_MATERIA_ID}")) {
                    filter { like(DBConstants.COLUMN_MATERIA, filtro) }
                }.decodeList()
        } catch (e: Exception) {
            handleError(e)
            return emptyList()
        }
    }

    // Manejo centralizado de errores
    private fun handleError(e: Exception) {
        when {
            e.message?.contains("HTTP request") == true -> throw Exception("Active el internet")
            e.message?.contains("duplicate key") == true -> throw Exception("uno de los elementos ya existe")
            else -> throw e
        }
    }
}
