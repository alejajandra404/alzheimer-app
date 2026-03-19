package mx.edu.itson.estanciaalzheimer

object MockData {

    val pacientes = arrayListOf(
        Paciente(1, "Beatriz Adriana Castañeda",    82, "Alzheimer Tipo 2",        "204-A", "NEEDS_EVAL"),
        Paciente(2, "Martha Elena Solórzano",    79, "Demencia Vascular",        "112-B", "STABLE"),
        Paciente(3, "Hortensia Figueroa", 75, "Alzheimer Leve",           "308-C", "OBSERVATION"),
        Paciente(4, "Gregorio Téllez Girón",   88, "Alzheimer Avanzado",       "101-A", "NEEDS_EVAL"),
        Paciente(5, "Salvador Becerra Montes", 71, "Deterioro Cognitivo Leve", "215-B", "STABLE"),
        Paciente(6, "Filemón Quintero Delgado",   84, "Demencia Mixta",           "307-A", "RESTING")
    )

    val instrumentos = arrayListOf(
        Instrumento(1, "MMSE",    "Mini-Mental State Examination. Evalúa orientación, memoria, atención, lenguaje y habilidades visuoespaciales.", 30),
        Instrumento(2, "Tinetti", "Evalúa equilibrio y marcha para determinar riesgo de caídas en adultos mayores.", 28)
    )

    val evaluaciones = arrayListOf(
        Evaluacion(1,  1, "MMSE",    "2025-11-10", 18, 30, "Dra. García"),
        Evaluacion(2,  1, "Tinetti", "2025-11-10", 14, 28, "Dra. García"),
        Evaluacion(3,  2, "MMSE",    "2025-11-12", 24, 30, "Dr. Ramírez"),
        Evaluacion(4,  2, "Tinetti", "2025-11-12", 22, 28, "Dr. Ramírez"),
        Evaluacion(5,  3, "MMSE",    "2025-10-05", 21, 30, "Dra. García"),
        Evaluacion(6,  4, "MMSE",    "2025-09-20", 12, 30, "Dr. Ramírez"),
        Evaluacion(7,  4, "Tinetti", "2025-09-20",  9, 28, "Dr. Ramírez"),
        Evaluacion(8,  5, "MMSE",    "2025-11-01", 26, 30, "Dra. García"),
        Evaluacion(9,  1, "MMSE",    "2025-07-15", 21, 30, "Dr. Ramírez"),
        Evaluacion(10, 1, "MMSE",    "2025-03-20", 23, 30, "Dra. García")
    )
}
