package sql

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
        val sql = "INSERT INTO notas (dniP, dniA, materia, nota) VALUES (?, ?, ?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , notaData.dniDelProfesor)
                    stmt.setString(2 , notaData.dniDelAlumno)
                    stmt.setString(3 , notaData.nombreDeLaMateria)
                    stmt.setDouble(4 , notaData.nota)
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
                notas.materia,
                notas.dniA,
                notas.dniP
            FROM
                alumnos
            JOIN
                notas ON alumnos.dniA = notas.dniA
            JOIN
                profesores ON profesores.dniP = notas.dniP
            JOIN
                materias ON materias.dniP = profesores.dniP
                AND materias.materia = notas.materia
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
                            nombreDelAlumno = resultSet.getString(1) ,
                            nota = resultSet.getDouble(2) ,
                            nombreDelProfesor = resultSet.getString(3) ,
                            nombreDeLaMateria = resultSet.getString(4),
                            dniDelAlumno = resultSet.getString(5),
                            dniDelProfesor = resultSet.getString(6)
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
                notas.materia,
                notas.dniA,
                notas.dniP
            FROM
                alumnos
            JOIN
                notas ON alumnos.dniA = notas.dniA
            JOIN
                profesores ON profesores.dniP = notas.dniP
            JOIN
                materias ON materias.dniP = profesores.dniP
                AND materias.materia = notas.materia
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
                            nombreDelAlumno = resultSet.getString(1) ,
                            nota = resultSet.getDouble(2) ,
                            nombreDelProfesor = resultSet.getString(3) ,
                            nombreDeLaMateria = resultSet.getString(4),
                            dniDelAlumno = resultSet.getString(5),
                            dniDelProfesor = resultSet.getString(6)
                        )
                    )
                }
            }
        }
        return data
    }

    //delete
    fun deleteNotaByDni(dniA: String) {
        val query = """
            DELETE 
                FROM
                    notas
                WHERE
                    notas.dniA = ?
            ;
        """.trimIndent()

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, dniA)
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
        val sql = "SELECT materia, dniP FROM materias;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    data.add(
                        MateriaData(
                            nombre = resultSet.getString(1),
                            dniDelProfesor = resultSet.getString(2)
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
        val query = """
            DELETE 
            FROM
                profesores
            WHERE
                dniP = ?
            ;
        """.trimIndent()

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, dniP)
                stmt.executeUpdate()
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

    //para limpiar la db
    fun clearAllTables() {
        val tables = listOf("alumnos" , "profesores" , "materias" , "notas")

        SQLiteConnection.connect()?.use { conn ->
            tables.forEach { table ->
                val sql = "DELETE FROM $table"
                conn.prepareStatement(sql).use { stmt ->
                    stmt.executeUpdate()
                }
            }
            println("Se han eliminado todos los datos de las tablas.")
        }
    }
}