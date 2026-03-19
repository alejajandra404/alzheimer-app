package mx.edu.itson.estanciaalzheimer

data class Paciente(
    var id: Int,
    var nombre: String,
    var edad: Int,
    var diagnostico: String,
    var cuarto: String,
    var estado: String // "NEEDS_EVAL", "STABLE", "OBSERVATION", "RESTING"
)
