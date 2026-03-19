package mx.edu.itson.estanciaalzheimer

data class Evaluacion(
    var id: Int,
    var pacienteId: Int,
    var instrumento: String,
    var fecha: String,
    var puntaje: Int,
    var puntajeTotal: Int,
    var evaluador: String
)
