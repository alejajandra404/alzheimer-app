package mx.edu.itson.estanciaalzheimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallePacienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_paciente)

        val bundle = intent.extras
        val pacienteId = bundle?.getInt("id") ?: 0
        val nombre = bundle?.getString("nombre")
        val edad = bundle?.getInt("edad") ?: 0
        val diagnostico = bundle?.getString("diagnostico")
        val cuarto = bundle?.getString("cuarto")
        val estado = bundle?.getString("estado")

        findViewById<TextView>(R.id.tv_nombre_detalle).text = nombre
        findViewById<TextView>(R.id.tv_edad_detalle).text = "Edad: $edad"
        findViewById<TextView>(R.id.tv_diagnostico_detalle).text = diagnostico
        findViewById<TextView>(R.id.tv_cuarto_detalle).text = cuarto
        findViewById<TextView>(R.id.tv_estado_detalle).text = estado

        findViewById<Button>(R.id.btn_historial).setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_aplicar).setOnClickListener {
            val intent = Intent(this, AplicarInstrumentoActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }
    }
}
