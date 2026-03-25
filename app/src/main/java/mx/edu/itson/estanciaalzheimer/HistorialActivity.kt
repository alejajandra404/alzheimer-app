package mx.edu.itson.estanciaalzheimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val nombre = intent.getStringExtra("nombre")
        val tvPaciente = findViewById<TextView>(R.id.tvPacienteHistorial)
        tvPaciente.text = "Paciente: $nombre"

        val botonVerResultados = findViewById<Button>(R.id.btnVerResultados)
        botonVerResultados.setOnClickListener {
            val intent = Intent(this, ResultadosActivity::class.java)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }
    }
}
