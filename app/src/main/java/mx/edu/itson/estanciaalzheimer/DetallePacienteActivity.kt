package mx.edu.itson.estanciaalzheimer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallePacienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_paciente)

        val nombre = intent.getStringExtra("nombre")
        val edad = intent.getIntExtra("edad", 0)
        val diagnostico = intent.getStringExtra("diagnostico")
        val cuarto = intent.getStringExtra("cuarto")
        val estado = intent.getStringExtra("estado")

        val tvNombre = findViewById(R.id.tv_nombre_detalle) as TextView
        val tvEdad = findViewById<TextView>(R.id.tv_edad_detalle)
        val tvDiagnostico = findViewById<TextView>(R.id.tv_diagnostico_detalle)
        val tvCuarto = findViewById<TextView>(R.id.tv_cuarto_detalle)
        val tvEstado = findViewById<TextView>(R.id.tv_estado_detalle)

        tvNombre.text = nombre
        tvEdad.text = "Edad: $edad"
        tvDiagnostico.text = diagnostico
        tvCuarto.text = cuarto
        tvEstado.text = estado

    }
}
