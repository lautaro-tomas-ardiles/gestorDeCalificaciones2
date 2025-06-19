package sql

import sql.data.AlumnoData
import sql.data.MateriaData
import sql.data.NotaData
import sql.data.OutPutData
import sql.data.ProfesorData

class SQLiteCRUD {

    //ingreso de datos a db
    fun insertAlumnos(alumnoData: AlumnoData) {
        val sql = "INSERT INTO alumnos (dniA, nombreCompletoA) VALUES (?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , alumnoData.dni)
                    stmt.setString(2 , alumnoData.nombre)
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            if (e.message?.contains("UNIQUE constraint failed: alumnos.dniA") == true) {
                throw Exception("el D.N.I del alumno ya existe")
            } else {
                throw e
            }
        }
    }

    fun insertProfesores(profesorData: ProfesorData) {
        val sql = "INSERT INTO profesores (nombreCompletoP, dniP) VALUES (?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , profesorData.nombre)
                    stmt.setString(2 , profesorData.dni)
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            if (e.message?.contains("UNIQUE constraint failed: profesores.dniP") == true) {
                throw Exception("Ya existe un profesor con ese DNI.")
            } else {
                throw e
            }
        }
    }

    fun insertMaterias(materiaData: MateriaData) {
        val sql = "INSERT INTO materias (dniP, materia) VALUES (?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , materiaData.dniDelProfesor)
                    stmt.setString(2 , materiaData.nombre)
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            if (e.message?.contains("UNIQUE constraint failed: materias.dniP, materias.materia") == true) {
                throw Exception("Esa materia ya fue registrada para ese profesor.")
            } else {
                throw e
            }
        }
    }

    fun insertNotas(notaData: NotaData) {
        val sql = "INSERT INTO notas (dniP, dniA, materiaId, nota) VALUES (?, ?, ?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , notaData.dniDelProfesor)
                    stmt.setString(2 , notaData.dniDelAlumno)
                    stmt.setInt(3 , notaData.id)
                    stmt.setDouble(4 , notaData.nota ?: 0.0)
                    stmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            if (e.message?.contains("UNIQUE constraint failed: notas") == true) {
                throw Exception("Ya se ha registrado una nota para ese alumno, materia y profesor.")
            } else {
                throw e
            }
        }
    }

    //pedido para output
    fun selectAlumnosByNameAndNotas(nombreCompletoA: String): List<OutPutData> {
        val data = mutableListOf<OutPutData>()
        val sql = """
            SELECT
                alumnos.nombreCompletoA,
                notas.nota,
                profesores.nombreCompletoP,
                materias.materia,
                notas.notaId
            FROM
                alumnos
            JOIN
                notas ON alumnos.dniA = notas.dniA
            JOIN
                profesores ON profesores.dniP = notas.dniP
            JOIN
                materias ON materias.materiaId = notas.materiaId
            WHERE 
                alumnos.nombreCompletoA LIKE ?;
            """

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1 , "%$nombreCompletoA%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        OutPutData(
                            nombreDelAlumno = resultSet.getString(1),
                            nota = resultSet.getDouble(2),
                            nombreDelProfesor = resultSet.getString(3),
                            nombreDeLaMateria = resultSet.getString(4),
                            notaId = resultSet.getInt(5)
                        )
                    )
                }
            }
        }
        return data
    }

    fun selectAlumnosByDNIAndNotas(dniA: String): List<OutPutData> {
        val data = mutableListOf<OutPutData>()
        val sql = """
            SELECT
                alumnos.nombreCompletoA,
                notas.nota,
                profesores.nombreCompletoP,
                materias.materia,
                notas.notaId
            FROM
                alumnos
            JOIN
                notas ON alumnos.dniA = notas.dniA
            JOIN
                profesores ON profesores.dniP = notas.dniP
            JOIN
                materias ON materias.materiaId = notas.materiaId
            WHERE 
                alumnos.dniA LIKE ?;     
            """

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1 , "%$dniA%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        OutPutData(
                            nombreDelAlumno = resultSet.getString(1),
                            nota = resultSet.getDouble(2),
                            nombreDelProfesor = resultSet.getString(3),
                            nombreDeLaMateria = resultSet.getString(4),
                            notaId = resultSet.getInt(5)
                        )
                    )
                }
            }
        }
        return data
    }

    //delete
    fun deleteNotaById(id: Int) {
        val query = """
            DELETE 
                FROM
                    notas
                WHERE
                    notas.notaId = ?
            ;
        """.trimIndent()

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }

    //para los select box
    fun selectAlumnos(): List<AlumnoData> {
        val data = mutableListOf<AlumnoData>()
        val sql = "SELECT nombreCompletoA, dniA FROM alumnos;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        AlumnoData(
                            nombre = resultSet.getString(1),
                            dni = resultSet.getString(2)
                        )
                    )
                }
            }
        }

        return data
    }

    fun selectProfesores(): List<ProfesorData> {
        val data = mutableListOf<ProfesorData>()
        val sql = "SELECT nombreCompletoP, dniP FROM profesores;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        ProfesorData(
                            nombre = resultSet.getString(1),
                            dni = resultSet.getString(2)
                        )
                    )
                }
            }
        }

        return data
    }

    fun selectMaterias(): List<MateriaData> {
        val data = mutableListOf<MateriaData>()
        val sql = "SELECT materia, dniP, materiaId FROM materias;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        MateriaData(
                            nombre = resultSet.getString(1),
                            dniDelProfesor = resultSet.getString(2),
                            materiaId = resultSet.getInt(3)
                        )
                    )
                }
            }
        }

        return data
    }

    //para la lista de alumnos
    //por dni
    fun listOfAlumnosByDNI(dniA: String): List<AlumnoData> {
        val data = mutableListOf<AlumnoData>()
        val query = """
            SELECT 
                alumnos.nombreCompletoA,
                alumnos.dniA
            FROM
                alumnos
            WHERE
                alumnos.dniA LIKE ?
            ;
        """.trimIndent()
        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$dniA%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        AlumnoData(
                            nombre = resultSet.getString(1),
                            dni = resultSet.getString(2)
                        )
                    )
                }
            }
        }
        return data
    }

    fun deleteAlumnoByDNI(dniA: String) {
        val query = """
            DELETE 
            FROM
                alumnos
            WHERE
                dniA = ?
            ;
        """.trimIndent()

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, dniA)
                stmt.executeUpdate()
            }
        }
    }
    //por nombre
    fun listOfAlumnosByNombre(name: String): List<AlumnoData> {
        val data = mutableListOf<AlumnoData>()
        val query = """
            SELECT 
                alumnos.nombreCompletoA,
                alumnos.dniA
            FROM
                alumnos
            WHERE
                alumnos.nombreCompletoA LIKE ?
            ;
        """.trimIndent()
        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$name%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        AlumnoData(
                            nombre = resultSet.getString(1),
                            dni = resultSet.getString(2)
                        )
                    )
                }
            }
        }
        return data
    }

    //lista de profesores
    fun listOfProfesoresByDNI(dniP: String): List<ProfesorData> {
        val data = mutableListOf<ProfesorData>()
        val query = """
            SELECT 
                profesores.nombreCompletoP,
                profesores.dniP
            FROM
                profesores
            WHERE
                profesores.dniP LIKE ?
            ;
        """.trimIndent()

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$dniP%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        ProfesorData(
                            nombre = resultSet.getString(1),
                            dni = resultSet.getString(2)
                        )
                    )
                }
            }
        }
        return data
    }

    fun deleteProfesorByDNI(dniP: String) {
        val deleteNotasQuery = """
            DELETE FROM notas
            WHERE dniP = ?
        """.trimIndent()

        val deleteMateriasQuery = """
            DELETE FROM materias
            WHERE dniP = ?
        """.trimIndent()

        val deleteProfesorQuery = """
            DELETE FROM profesores
            WHERE dniP = ?
        """.trimIndent()

        SQLiteConnection.connect()?.use { conn ->
            conn.autoCommit = false // iniciamos la transacción
            try {
                conn.prepareStatement(deleteNotasQuery).use { stmt ->
                    stmt.setString(1, dniP)
                    stmt.executeUpdate()
                }


                conn.prepareStatement(deleteMateriasQuery).use { stmt ->
                    stmt.setString(1, dniP)
                    stmt.executeUpdate()
                }


                conn.prepareStatement(deleteProfesorQuery).use { stmt ->
                    stmt.setString(1, dniP)
                    stmt.executeUpdate()
                }

                conn.commit()
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    }
    //por nombre
    fun listOfProfesoresByNombre(name: String): List<ProfesorData> {
        val data = mutableListOf<ProfesorData>()
        val query = """
            SELECT 
                profesores.nombreCompletoP,
                profesores.dniP
            FROM
                profesores
            WHERE
                profesores.nombreCompletoP LIKE ?
            ;
        """.trimIndent()
        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$name%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        ProfesorData(
                            nombre = resultSet.getString(1),
                            dni = resultSet.getString(2)
                        )
                    )
                }
            }
        }
        return data
    }

    fun listOfMateriasByDNI(dniP: String): List<MateriaData> {
        val data = mutableListOf<MateriaData>()
        val query = """
            SELECT 
                materias.materia,
                materias.dniP,
                materias.materiaId
            FROM
                materias
            WHERE
                materias.dniP LIKE ?
            ;
        """.trimIndent()
        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$dniP%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        MateriaData(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)
                        )
                    )
                }
            }
        }
        return data
    }

    fun listOfMateriasByNombre(name: String): List<MateriaData> {
        val data = mutableListOf<MateriaData>()
        val query = """
            SELECT 
                materias.materia,
                materias.dniP,
                materias.materiaId
            FROM
                materias
            WHERE
                materias.materia LIKE ?
            ;
        """.trimIndent()
        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, "%$name%")
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        MateriaData(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)
                        )
                    )
                }
            }
        }
        return data
    }

    fun deleteMateriasById(id: Int) {
        val deleteMaterias = """
            DELETE FROM materias
            WHERE materiaId = ?
        """.trimIndent()
        val deleteNotas = """
            DELETE FROM notas
            WHERE materiaId = ?
        """.trimIndent()


        SQLiteConnection.connect()?.use { conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(deleteNotas).use { statement ->
                    statement.setInt(1, id)
                    statement.executeUpdate()
                }
                conn.prepareStatement(deleteMaterias).use { statement ->
                    statement.setInt(1, id)
                    statement.executeUpdate()
                }
                conn.commit()
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    }

}