package sql

import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object SQLiteConnection {
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