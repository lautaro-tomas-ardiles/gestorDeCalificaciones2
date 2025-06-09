package sql

import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object SQLiteconnection {
    private val dbDirectory = Paths.get("data")
    private val dbPath = dbDirectory.resolve("Escuela.db").toString()
    private val url = "jdbc:sqlite:$dbPath"

    init {
        if (!Files.exists(dbDirectory)) {
            Files.createDirectories(dbDirectory)
        }
        if (!Files.exists(Paths.get(dbPath))) {
            createDatabase()
        }
    }

    fun connect(): Connection? {
        return try {
            DriverManager.getConnection(url).also {
                println("Conexión a SQLite establecida.")
            }
        } catch (e: SQLException) {
            println("Error al conectar a la base de datos: ${e.message}")
            null
        }
    }

    private fun createDatabase() {
        connect()?.use { conn ->
            val stmt = conn.createStatement()
            // Aquí iría el código SQL para crear las tablas en la base de datos
            val sql = """
                CREATE TABLE IF NOT EXISTS alumnos (
                    dniA TEXT PRIMARY KEY,
                    nombreCompletoA TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS profesores (
                    dniP TEXT PRIMARY KEY,
                    nombreCompletoP TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS materias (
                    dniP TEXT NOT NULL,
                    materia TEXT NOT NULL,
                    FOREIGN KEY(dniP) REFERENCES profesores(dniP)
                );
                CREATE TABLE IF NOT EXISTS notas (
                    dniP TEXT NOT NULL,
                    dniA TEXT NOT NULL,
                    nota REAL NOT NULL,
                    materia TEXT NOT NULL,
                    FOREIGN KEY(dniP) REFERENCES profesores(dniP),
                    FOREIGN KEY(dniA) REFERENCES alumnos(dniA)
                );
            """
            stmt.executeUpdate(sql)
            println("Base de datos creada con éxito.")
        }
    }
}

class SQLiteCRUD {

    fun insertAlumnos(nombreCompletoA: String , dniA: String) {
        val sql = "INSERT INTO alumnos (dniA, nombreCompletoA) VALUES (?, ?)"

        try {
            SQLiteconnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1 , dniA)
                    pstmt.setString(2 , nombreCompletoA)
                    pstmt.executeUpdate()
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
            SQLiteconnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1 , nombreCompletoP)
                    pstmt.setString(2 , dniP)
                    pstmt.executeUpdate()
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
            SQLiteconnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1 , dniP)
                    pstmt.setString(2 , materia)
                    pstmt.executeUpdate()
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
            SQLiteconnection.connect()?.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1 , dniP)
                    pstmt.setString(2 , dniA)
                    pstmt.setString(3 , materia)
                    pstmt.setDouble(4 , nota)
                    pstmt.executeUpdate()
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

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1 , "%$nombreCompletoA%")
                val resultSet = pstmt.executeQuery()
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

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1 , "%$dniA%")
                val resultSet = pstmt.executeQuery()
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

    fun selectAlumnos(): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM alumnos;"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                val resultSet = pstmt.executeQuery()
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

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                val resultSet = pstmt.executeQuery()
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

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                val resultSet = pstmt.executeQuery()
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

    fun clearAllTables() {
        val tables = listOf("alumnos" , "profesores" , "materias" , "notas")

        SQLiteconnection.connect()?.use { conn ->
            tables.forEach { table ->
                val sql = "DELETE FROM $table"
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.executeUpdate()
                }
            }
            println("Se han eliminado todos los datos de las tablas.")
        }
    }
}