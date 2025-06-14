package sql

class SQLiteCRUD {

    //ingreso de datos a db
    fun insertAlumnos(nombreCompletoA: String , dniA: String) {
        val sql = "INSERT INTO alumnos (dniA, nombreCompletoA) VALUES (?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , dniA)
                    stmt.setString(2 , nombreCompletoA)
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

    fun insertProfesores(nombreCompletoP: String , dniP: String) {
        val sql = "INSERT INTO profesores (nombreCompletoP, dniP) VALUES (?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , nombreCompletoP)
                    stmt.setString(2 , dniP)
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

    fun insertMaterias(dniP: String , materia: String) {
        val sql = "INSERT INTO materias (dniP, materia) VALUES (?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , dniP)
                    stmt.setString(2 , materia)
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

    fun insertNotas(dniP: String , dniA: String , materia: String , nota: Double) {
        val sql = "INSERT INTO notas (dniP, dniA, materia, nota) VALUES (?, ?, ?, ?)"

        try {
            SQLiteConnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1 , dniP)
                    stmt.setString(2 , dniA)
                    stmt.setString(3 , materia)
                    stmt.setDouble(4 , nota)
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
    fun selectAlumnosByNameAndNotas(nombreCompletoA: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = """
            SELECT
                alumnos.nombreCompletoA AS 'alumno',
                notas.nota AS 'nota',
                profesores.nombreCompletoP AS 'profesor',
                notas.materia AS 'materia'
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
                    val row = listOf<Any>(
                        resultSet.getString("alumno") ,
                        resultSet.getDouble("nota") ,
                        resultSet.getString("profesor") ,
                        resultSet.getString("materia")
                    )
                    data.add(row)
                }
            }
        }
        return data
    }

    fun selectAlumnosByDNIAndNotas(dniA: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = """
            SELECT
                alumnos.nombreCompletoA AS 'alumno',
                notas.nota AS 'nota',
                profesores.nombreCompletoP AS 'profesor',
                notas.materia AS 'materia'
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
                    val row = listOf<Any>(
                        resultSet.getString("alumno") ,
                        resultSet.getDouble("nota") ,
                        resultSet.getString("profesor") ,
                        resultSet.getString("materia")
                    )
                    data.add(row)
                }
            }
        }
        return data
    }

    //para los select box
    fun selectAlumnos(): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM alumnos;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    val row = listOf<Any>(
                        resultSet.getString("nombreCompletoA") ,
                        resultSet.getString("dniA")
                    )
                    data.add(row)
                }
            }
        }

        return data
    }

    fun selectProfesores(): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM profesores;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    val row = listOf<Any>(
                        resultSet.getString("nombreCompletoP") ,
                        resultSet.getString("dniP")
                    )
                    data.add(row)
                }
            }
        }

        return data
    }

    fun selectMaterias(): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM materias;"

        SQLiteConnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    val row = listOf<Any>(
                        resultSet.getString("materia") ,
                        resultSet.getString("dniP")
                    )
                    data.add(row)
                }
            }
        }

        return data
    }

    //para la lista de alumnos
    //por dni
    fun listOfAlumnosByDNI(dniA: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
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
                    val row = listOf<Any>(
                        resultSet.getString(1),
                        resultSet.getString(2)
                    )
                    data.add(row)
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
    fun listOfAlumnosByNombre(name: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
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
                    val row = listOf<Any>(
                        resultSet.getString(1),
                        resultSet.getString(2)
                    )
                    data.add(row)
                }
            }
        }
        return data
    }

    //lista de profesores
    fun listOfProfesoresByDNI(dniP: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
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
                    val row = listOf<Any>(
                        resultSet.getString(1),
                        resultSet.getString(2)
                    )
                    data.add(row)
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
    fun listOfProfesoresByNombre(name: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
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
                    val row = listOf<Any>(
                        resultSet.getString(1),
                        resultSet.getString(2)
                    )
                    data.add(row)
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