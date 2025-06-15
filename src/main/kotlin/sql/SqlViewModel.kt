package sql

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sql.data.AlumnoData
import sql.data.MateriaData
import sql.data.NotaData
import sql.data.OutPutData
import sql.data.ProfesorData

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
                crud.insertAlumnos(AlumnoData(nombreAlumno, dniAlumno))
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
                crud.insertProfesores(ProfesorData(nombreProfesor, dniProfesor))
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
                crud.insertMaterias(MateriaData(materia, dniProfeMateria))
                mensaje = "Materia agregada correctamente."
            } catch (e: Exception) {
                mensaje = "Error: ${e.message}"
            }
        }
    }

    fun agregarNota(data: NotaData) {
        if (
            data.dniDelProfesor.isBlank() ||
            data.dniDelAlumno.isBlank() ||
            data.nombreDeLaMateria.isBlank()
        ) {
            mensaje = "Todos los campos son obligatorios para registrar una nota."
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                crud.insertNotas(data)
                mensaje = "Nota agregada correctamente."
            } catch (e: Exception) {
                mensaje = "Error: ${e.message}"
            }
        }
    }

    fun obtenerAlumnos(): List<AlumnoData> {
        return try {
            crud.selectAlumnos()
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun obtenerProfesores(): List<ProfesorData> {
        return try {
            crud.selectProfesores()
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun obtenerMaterias(): List<MateriaData> {
        return try {
            crud.selectMaterias()
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun buscarAlumnoPorNombre(nombre: String): List<OutPutData> {
        return try {
            crud.selectAlumnosByNameAndNotas(nombre)
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun buscarAlumnoPorDNI(dni: String): List<OutPutData> {
        return try {
            crud.selectAlumnosByDNIAndNotas(dni)
        } catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun eliminarNotaPorDNI(dni: String) {
        try {
            crud.deleteNotaByDni(dni)
            mensaje = "Nota eliminado correctamente"
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
        }
    }

    fun listaDeAlumnosPorDNI(dni: String): List<AlumnoData> {
        return try {
            crud.listOfAlumnosByDNI(dni)
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun eliminarAlumnoPorDNI(dni: String) {
        try {
            crud.deleteAlumnoByDNI(dni)
            mensaje = "Alumno eliminado correctamente"
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
        }
    }

    fun listaDeAlumnosPorNombre(nombre: String): List<AlumnoData> {
        return try {
            crud.listOfAlumnosByNombre(nombre)
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun listaDeProfesoresPorDNI(dni: String): List<ProfesorData> {
        return try {
            crud.listOfProfesoresByDNI(dni)
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun eliminarProfesorPorDNI(dni: String) {
        try {
            crud.deleteProfesorByDNI(dni)
            mensaje = "Alumno eliminado correctamente"
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
        }
    }

    fun listaDeProfesoresPorNombre(nombre: String): List<ProfesorData> {
        return try {
            crud.listOfProfesoresByNombre(nombre)
        }catch (e: Exception) {
            mensaje = "Error: ${e.message}"
            emptyList()
        }
    }

    fun limpiarMensaje() {
        mensaje = null
    }
}
