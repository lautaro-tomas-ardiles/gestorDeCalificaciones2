package sql

import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object SQLiteconnection {
    private val dbPath = System.getProperty("user.home") + "/Escuela.db"
    private val url = "jdbc:sqlite:$dbPath"

    init {
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

    fun insertAlumnos(nombreCompletoA: String, dniA: String){
        val sql = "INSERT INTO alumnos (dniA, nombreCompletoA) VALUES (?, ?)"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1,dniA)
                pstmt.setString(2,nombreCompletoA )
                pstmt.executeUpdate()
            }
        }
    }
    fun insertProfesores(nombreCompletoP: String, dniP: String){
        val sql = "INSERT INTO profesores (nombreCompletoP, dniP) VALUES (?, ?)"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, nombreCompletoP)
                pstmt.setString(2, dniP)
                pstmt.executeUpdate()
            }
        }
    }
    fun insertMaterias(dniP: String, materia: String){
        val sql = "INSERT INTO materias (dniP, materia) VALUES (?, ?)"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, dniP)
                pstmt.setString(2, materia)
                pstmt.executeUpdate()
            }
        }
    }
    fun insertNotas(dniP: String, dniA: String, nota: Double){
        val sql = "INSERT INTO notas (dniP, dniA, nota) VALUES (?, ?, ?)"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, dniP)
                pstmt.setString(2, dniA)
                pstmt.setDouble(3, nota)
                pstmt.executeUpdate()
            }
        }
    }

    fun selectAlumnosN(nombreCompletoA: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = """
            SELECT 
                nombreCompletoA AS 'Alumno',
                nota AS 'Nota',
                nombreCompletoP AS 'Profesor',
                materia AS 'Materia' 
            FROM 
                notas 
            JOIN 
                profesores ON notas.dniP = profesores.dniP
            JOIN 
                alumnos ON notas.dniA = alumnos.dniA 
            JOIN 
                materias ON materias.dniP = profesores.dniP 
            WHERE 
                alumnos.nombreCompletoA LIKE ?;
            """

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, "%$nombreCompletoA%")
                val resultSet = pstmt.executeQuery()
                while (resultSet.next()) {
                    val row = listOf<Any>(
                        resultSet.getString("alumno"),
                        resultSet.getDouble("nota"),
                        resultSet.getString("profesor"),
                        resultSet.getString("materia")
                    )
                    data.add(row)
                }
            }
        }
        return data
    }
    fun selectAlumnosD(dniA: String): List<List<Any>> {
        val data = mutableListOf<List<Any>>()
        val sql = """
            SELECT 
                nombreCompletoA AS 'Alumno',
                nota AS 'Nota',
                nombreCompletoP AS 'Profesor',
                materia AS 'Materia' 
            FROM 
                notas 
            JOIN 
                profesores ON notas.dniP = profesores.dniP
            JOIN 
                alumnos ON notas.dniA = alumnos.dniA 
            JOIN 
                materias ON materias.dniP = profesores.dniP 
            WHERE 
                alumnos.dniA LIKE ?;
            """

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, "%$dniA%")
                val resultSet = pstmt.executeQuery()
                while (resultSet.next()) {
                    val row = listOf<Any>(
                        resultSet.getString("alumno"),
                        resultSet.getDouble("nota"),
                        resultSet.getString("profesor"),
                        resultSet.getString("materia")
                    )
                    data.add(row)
                }
            }
        }
        return data
    }
    fun selectAlumnos(): List<List<Any>>{
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM alumnos;"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                val resultSet = pstmt.executeQuery()
                while (resultSet.next()){
                    val row = listOf<Any>(
                        resultSet.getString("nombreCompletoA"),
                        resultSet.getString("dniA")
                    )
                    data.add(row)
                }
            }
        }

        return data
    }

    fun selectProfesores(): List<List<Any>>{
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM profesores;"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                val resultSet = pstmt.executeQuery()
                while (resultSet.next()){
                    val row = listOf<Any>(
                        resultSet.getString("nombreCompletoP"),
                        resultSet.getString("dniP")
                    )
                    data.add(row)
                }
            }
        }

        return data
    }

    fun selectMaterias(): List<List<Any>>{
        val data = mutableListOf<List<Any>>()
        val sql = "SELECT * FROM materias;"

        SQLiteconnection.connect()?.use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                val resultSet = pstmt.executeQuery()
                while (resultSet.next()){
                    val row = listOf<Any>(
                        resultSet.getString("materia"),
                        resultSet.getString("dniP")
                    )
                    data.add(row)
                }
            }
        }

        return data
    }

    fun clearAllTables() {
        val tables = listOf("alumnos", "profesores", "materias", "notas")

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