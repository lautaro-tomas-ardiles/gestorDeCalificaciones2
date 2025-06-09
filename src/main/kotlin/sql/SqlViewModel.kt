package sql

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SqlViewModel : ViewModel() {
    private val crud = SQLiteCRUD()

    var mensaje by mutableStateOf<String?>(null)
        private set

    fun agregarAlumno(nombreAlumno: String , dniAlumno: String) {
        if (nombreAlumno.isBlank() || dniAlumno.isBlank()) {
            mensaje = "Nombre y DNI del alumno no pueden estar vacíos."
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                crud.insertAlumnos(nombreAlumno , dniAlumno)
                mensaje = "Alumno agregado correctamente."
            } catch (e: Exception) {
                mensaje = "Error: ${e.message}"
            }
        }
    }

    fun agregarProfesor(nombreProfesor: String , dniProfesor: String) {
        if (nombreProfesor.isBlank() || dniProfesor.isBlank()) {
            mensaje = "Nombre y DNI del profesor no pueden estar vacíos."
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                crud.insertProfesores(nombreProfesor , dniProfesor)
                mensaje = "Profesor agregado correctamente."
            } catch (e: Exception) {
                mensaje = "Error: ${e.message}"
            }
        }
    }

    fun agregarMateria(dniProfeMateria: String , materia: String) {
        if (dniProfeMateria.isBlank() || materia.isBlank()) {
            mensaje = "DNI del profesor y nombre de la materia no pueden estar vacíos."
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                crud.insertMaterias(dniProfeMateria , materia)
                mensaje = "Materia agregada correctamente."
            } catch (e: Exception) {
                mensaje = "Error: ${e.message}"
            }
        }
    }

    fun agregarNota(dniNotaP: String , dniNotaA: String , materiaNota: String , nota: String) {
        if (dniNotaP.isBlank() || dniNotaA.isBlank() || materiaNota.isBlank() || nota.isBlank()) {
            mensaje = "Todos los campos son obligatorios para registrar una nota."
            return
        }

        val notaDouble = nota.toDoubleOrNull()
        if (notaDouble == null) {
            mensaje = "La nota debe ser un número válido."
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                crud.insertNotas(dniNotaP , dniNotaA , materiaNota , notaDouble)
                mensaje = "Nota agregada correctamente."
            } catch (e: Exception) {
                mensaje = "Error: ${e.message}"
            }
        }
    }

    fun obtenerAlumnos(): List<List<Any>> {
        return try {
            crud.selectAlumnos()
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun obtenerProfesores(): List<List<Any>> {
        return try {
            crud.selectProfesores()
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun obtenerMaterias(): List<List<Any>> {
        return try {
            crud.selectMaterias()
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun buscarAlumnoPorNombre(nombre: String): List<List<Any>> {
        return try {
            crud.selectAlumnosByNameAndNotas(nombre)
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun buscarAlumnoPorDNI(dni: String): List<List<Any>> {
        return try {
            crud.selectAlumnosByDNIAndNotas(dni)
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun limpiarMensaje() {
        mensaje = null
    }
}
