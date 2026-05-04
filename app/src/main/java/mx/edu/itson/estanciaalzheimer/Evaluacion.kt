package mx.edu.itson.estanciaalzheimer

data class Evaluacion(
    var id: Int = 0,
    var pacienteId: Int = 0,
    var instrumento: String = "",
    var fecha: String = "",
    var puntaje: Int = 0,
    var puntajeTotal: Int = 0,
    var evaluador: String = ""
)
