package mx.edu.itson.estanciaalzheimer

data class Paciente(
    var id: Int = 0,
    var nombre: String = "",
    var fechaNacimiento: String = "",
    var diagnostico: String = "",
    var cuarto: String = "",
    var estado: String = ""
)
