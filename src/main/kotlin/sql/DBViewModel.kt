package sql

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sql.data.*

class DBViewModel : ViewModel() {
    private val crud = SupabaseCRUD()

    /**
     * Actualmente, hay un problema porque se usa viewModelScope.launch(Dispatchers.IO)
     * en funciones que se ejecutan en una misma pantalla (antes no pasaba por qué usaba
     * returns de listas y no variables internas en view model) haci que se va a usar
     * mutex para evitar esto
     */

    private val mutex = Mutex()

    var mensaje by mutableStateOf<String?>(null)
        private set

    private val _alumnos = mutableStateOf<List<AlumnoData>>(emptyList())
    val alumnos = _alumnos

    private val _profesores = mutableStateOf<List<ProfesorData>>(emptyList())
    val profesores = _profesores

    private val _materias = mutableStateOf<List<MateriaData>>(emptyList())
    val materias = _materias

    private val _outPut = mutableStateOf<List<OutPutData>>(emptyList())
    val outPut = _outPut

    /**
     * Empty campos: revisa si un número indefinido de variable de tipo
     * string están vacíos
     *
     * @param campos todos los string que se comprueban
     * @return retorna un booleano, verdadero si alguna esta vació
     * y false si todos tiene algo
     */
    private fun emptyCampos(vararg campos: String): Boolean {
        return campos.any { it.isBlank() }
    }

    /**
     * Agregar alumno: se agrega un alumno a la db
     *
     * @param nombreAlumno nombre del alumno
     * @param dniAlumno dni del alumno
     */
    fun agregarAlumno(nombreAlumno: String, dniAlumno: String) {
        if (emptyCampos(nombreAlumno, dniAlumno)) {
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

    /**
     * Agregar profesor: se agrega un profesor a la db
     *
     * @param nombreProfesor nombre del profesor
     * @param dniProfesor dni del profesor
     */
    fun agregarProfesor(nombreProfesor: String, dniProfesor: String) {
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

    /**
     * Agrega una nueva materia al sistema con el DNI del profesor asociado.
     * materiaId es -1 porque es auto incremental
     *
     * @param dniProfeMateria DNI del profesor que dicta la materia.
     * @param materia Nombre de la materia a agregar.
     */
    fun agregarMateria(dniProfeMateria: String, materia: String) {
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

    /**
     * Agrega una nota para un alumno en una materia, validando los campos requeridos.
     *
     * @param data Objeto [NotaData] que contiene la información de la nota, alumno, profesor y materia.
     */
    fun agregarNota(data: NotaData) {
        if (emptyCampos(data.dniP, data.dniA)) {
            mensaje = "Todos los campos son obligatorios para registrar una nota."
            return
        }

        if (data.nota == null || data.nota >= 10 || data.nota <= 0) {
            mensaje = "La nota tiene que ser un numero(menor a 10 y positivo)."
            return
        }

        if (data.materiaId == null) {
            mensaje = "tiene que seleccionar una materia"
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

    /**
     * Carga la lista completa de alumnos desde la base de datos y la guarda en el estado.
     */
    fun cargarAlumnos() {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _alumnos.value = crud.selectAlumnos()
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    /**
     * Carga la lista completa de profesores desde la base de datos y la guarda en el estado.
     */
    fun cargarProfesores() {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _profesores.value = crud.selectProfesores()
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    /**
     * Carga la lista completa de materias desde la base de datos y la guarda en el estado.
     */
    fun cargarMaterias() {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _materias.value = crud.selectMaterias()
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    /** Filtra la lista de alumnos por nombre o DNI, ignorando mayúsculas/minúsculas.
     *
     * @param nombreAndDni Cadena a buscar en el nombre o DNI del alumno.
     */
    fun filtrarAlumnos(nombreAndDni: String) {
        if (emptyCampos(nombreAndDni)) {
            cargarAlumnos()
        }

        viewModelScope.launch(Dispatchers.Default) {
            mutex.withLock {
                _alumnos.value = _alumnos.value.filter {
                    it.nombreA.contains(nombreAndDni, ignoreCase = true) ||
                            it.dniA.contains(nombreAndDni, ignoreCase = true)
                }
            }
        }
    }

    /**
     * Filtra la lista de profesores por nombre o DNI, ignorando mayúsculas/minúsculas.
     *
     * @param nombreAndDni Cadena a buscar en el nombre o DNI del profesor.
     */
    fun filtrarProfesores(nombreAndDni: String) {
        if (emptyCampos(nombreAndDni)) {
            cargarProfesores()
        }
        viewModelScope.launch(Dispatchers.Default) {
            mutex.withLock {
                _profesores.value = _profesores.value.filter {
                    it.nombreP.contains(nombreAndDni, ignoreCase = true) ||
                            it.dniP.contains(nombreAndDni, ignoreCase = true)
                }
            }
        }
    }

    /**
     * Filtra la lista de materias por nombre de la materia y DNI del profesor.
     *
     * @param dni DNI del profesor al que están asignadas las materias.
     * @param nombre Nombre parcial o completo de la materia a buscar.
     */
    fun filtrarMaterias(dni: String, nombre: String) {
        if (emptyCampos(dni)) {
            cargarMaterias()
        }

        viewModelScope.launch(Dispatchers.Default) {
            mutex.withLock {
                _materias.value = _materias.value.filter {
                    it.materia.contains(nombre, ignoreCase = true) &&
                            it.dniP.contains(dni, ignoreCase = true)
                }
            }
        }
    }

    fun buscarNotaDeAlumnoPorNombre(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _outPut.value = crud.selectAlumnosAndNotasByName(nombre)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    println(e.message)
                }
            }
        }
    }

    fun buscarNotaDelAlumnoPorDNI(dni: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _outPut.value = crud.selectAlumnosAndNotasByDNI(dni)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    fun eliminarNotaPorId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    crud.deleteNotaById(id)
                    mensaje = "Nota eliminado correctamente"
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    fun buscarAlumnosPorDNI(dni: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _alumnos.value = crud.listOfAlumnosByDNI(dni)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    _alumnos.value = emptyList()
                }
            }
        }
    }

    fun eliminarAlumnoPorDNI(dni: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    crud.deleteAlumnoByDNI(dni)
                    mensaje = "Alumno eliminado correctamente"
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    fun buscarAlumnosPorNombre(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _alumnos.value = crud.listOfAlumnosByNombre(nombre)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    _alumnos.value = emptyList()
                }
            }
        }
    }

    fun buscarProfesoresPorNombre(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _profesores.value = crud.listOfProfesoresByNombre(nombre)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    _profesores.value = emptyList()
                }
            }
        }
    }

    fun eliminarProfesorPorDNI(dni: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    crud.deleteProfesorByDNI(dni)
                    mensaje = "profesor eliminado correctamente"
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    fun buscarProfesoresPorDNI(dni: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _profesores.value = crud.listOfProfesoresByDNI(dni)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    _profesores.value = emptyList()
                }
            }
        }
    }

    fun buscarMateriasPorDNI(dni: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _materias.value = crud.listOfMateriasByDNI(dni)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    _materias.value = emptyList()
                }
            }
        }
    }

    fun buscarMateriasPorNombre(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    _materias.value = crud.listOfMateriasByNombre(nombre)
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                    _materias.value = emptyList()
                }
            }
        }
    }

    fun eliminarMateriaPorId(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                try {
                    if (id != null) {
                        crud.deleteMateriasById(id)
                        mensaje = "materia eliminada correctamente"
                    }
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            }
        }
    }

    fun limpiarMensaje() {
        mensaje = null
    }
}
