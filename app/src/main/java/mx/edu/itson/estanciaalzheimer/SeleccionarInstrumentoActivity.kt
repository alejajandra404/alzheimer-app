package mx.edu.itson.estanciaalzheimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SeleccionarInstrumentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_instrumento)

        val pacienteId = intent.getIntExtra("pacienteId", 0)
        val nombre = intent.getStringExtra("nombre")

        findViewById<TextView>(R.id.tvNombrePacienteSeleccion).text = nombre

        findViewById<Button>(R.id.btnAplicarMmse).setOnClickListener {
            val intent = Intent(this, AplicarInstrumentoActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAplicarTinetti).setOnClickListener {
            val intent = Intent(this, AplicarTinettiActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }
    }
}
