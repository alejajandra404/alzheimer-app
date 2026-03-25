package mx.edu.itson.estanciaalzheimer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val nombre = intent.getStringExtra("nombre")
        val tvPaciente = findViewById<TextView>(R.id.tvPacienteResultados)
        tvPaciente.text = "Paciente: $nombre"

        val btnVolver = findViewById<Button>(R.id.btnVolverHistorial)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
