package mx.edu.itson.estanciaalzheimer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val pacienteId = intent.getIntExtra("pacienteId", 0)
        val nombre = intent.getStringExtra("nombre")

        val tvPaciente = findViewById<TextView>(R.id.tvPacienteResultados)
        val tvMmse = findViewById<TextView>(R.id.tvUltimoMmse)
        val tvTinetti = findViewById<TextView>(R.id.tvUltimoTinetti)
        val tvCambio = findViewById<TextView>(R.id.tvCambio)
        val tvEstado = findViewById<TextView>(R.id.tvEstadoGeneral)

        tvPaciente.text = "Paciente: $nombre"

        val ref = FirebaseDatabase.getInstance()
            .getReference("evaluaciones")
            .child(pacienteId.toString())

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = ArrayList<Evaluacion>()
                for (child in snapshot.children) {
                    val e = child.getValue(Evaluacion::class.java)
                    if (e != null) lista.add(e)
                }

                val mmse = lista.filter { it.instrumento == "MMSE" }.sortedByDescending { it.fecha }
                val tinetti = lista.filter { it.instrumento == "Tinetti" }.sortedByDescending { it.fecha }

                if (mmse.isNotEmpty()) {
                    val ultimo = mmse[0]
                    tvMmse.text = "${ultimo.puntaje}/${ultimo.puntajeTotal}"
                    tvEstado.text = interpretarMmse(ultimo.puntaje)

                    if (mmse.size >= 2) {
                        val diff = ultimo.puntaje - mmse[1].puntaje
                        tvCambio.text = when {
                            diff > 0 -> "+$diff puntos en MMSE"
                            diff < 0 -> "$diff puntos en MMSE"
                            else -> "Sin cambio en MMSE"
                        }
                    } else {
                        tvCambio.text = "Primera evaluación"
                    }
                } else {
                    tvMmse.text = "Sin datos"
                    tvCambio.text = "—"
                    tvEstado.text = "Sin evaluaciones registradas"
                }

                if (tinetti.isNotEmpty()) {
                    tvTinetti.text = "${tinetti[0].puntaje}/${tinetti[0].puntajeTotal}"
                } else {
                    tvTinetti.text = "Sin datos"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ResultadosActivity, "Error al cargar resultados", Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<Button>(R.id.btnVolverHistorial).setOnClickListener {
            finish()
        }
    }

    private fun interpretarMmse(puntaje: Int): String {
        return when {
            puntaje >= 27 -> "Función cognitiva normal"
            puntaje >= 21 -> "Deterioro cognitivo leve"
            puntaje >= 11 -> "Deterioro cognitivo moderado"
            else -> "Deterioro cognitivo severo"
        }
    }
}
