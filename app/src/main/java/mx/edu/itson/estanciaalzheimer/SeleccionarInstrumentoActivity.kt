package mx.edu.itson.estanciaalzheimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SeleccionarInstrumentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_instrumento)

        val pacienteId = intent.getIntExtra("pacienteId", 0)
        val nombre = intent.getStringExtra("nombre")

        findViewById<TextView>(R.id.tvNombrePacienteSeleccion).text = nombre

        cargarInstrumentos()

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

    private fun cargarInstrumentos() {
        val tvNombreMmse = findViewById<TextView>(R.id.tvNombreMmse)
        val tvSubtituloMmse = findViewById<TextView>(R.id.tvSubtituloMmse)
        val tvDescripcionMmse = findViewById<TextView>(R.id.tvDescripcionMmse)
        val tvNombreTinetti = findViewById<TextView>(R.id.tvNombreTinetti)
        val tvSubtituloTinetti = findViewById<TextView>(R.id.tvSubtituloTinetti)
        val tvDescripcionTinetti = findViewById<TextView>(R.id.tvDescripcionTinetti)
        val btnMmse = findViewById<Button>(R.id.btnAplicarMmse)
        val btnTinetti = findViewById<Button>(R.id.btnAplicarTinetti)

        val ref = FirebaseDatabase.getInstance().getReference("instrumentos")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mmse = snapshot.child("mmse").getValue(Instrumento::class.java)
                val tinetti = snapshot.child("tinetti").getValue(Instrumento::class.java)

                if (mmse != null) {
                    tvNombreMmse.text = mmse.nombre
                    tvSubtituloMmse.text = "${mmse.nombreCompleto}  •  ${mmse.puntajeMaximo} pts"
                    tvDescripcionMmse.text = mmse.descripcion
                    btnMmse.text = "Aplicar ${mmse.nombre}"
                }

                if (tinetti != null) {
                    tvNombreTinetti.text = tinetti.nombre
                    tvSubtituloTinetti.text = "${tinetti.nombreCompleto}  •  ${tinetti.puntajeMaximo} pts"
                    tvDescripcionTinetti.text = tinetti.descripcion
                    btnTinetti.text = "Aplicar ${tinetti.nombre}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SeleccionarInstrumentoActivity, "Error al cargar instrumentos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
