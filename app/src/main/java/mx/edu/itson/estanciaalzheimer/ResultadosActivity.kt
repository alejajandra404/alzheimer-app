package mx.edu.itson.estanciaalzheimer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ResultadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val btnVolver = findViewById<Button>(R.id.btnVolverHistorial)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
