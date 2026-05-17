package mx.edu.itson.estanciaalzheimer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DetallePacienteActivity : AppCompatActivity() {

    private val editarLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_paciente)

        val bundle = intent.extras
        val pacienteId = bundle?.getInt("id") ?: 0
        val nombre = bundle?.getString("nombre")
        val fechaNacimiento = bundle?.getString("fechaNacimiento") ?: ""
        val diagnostico = bundle?.getString("diagnostico")
        val cuarto = bundle?.getString("cuarto")
        val estado = bundle?.getString("estado")

        findViewById<TextView>(R.id.tv_nombre_detalle).text = nombre
        findViewById<TextView>(R.id.tv_edad_detalle).text = "Edad: ${calcularEdad(fechaNacimiento)} años"
        findViewById<TextView>(R.id.tv_diagnostico_detalle).text = diagnostico
        findViewById<TextView>(R.id.tv_cuarto_detalle).text = cuarto
        findViewById<TextView>(R.id.tv_estado_detalle).text = estado

        findViewById<Button>(R.id.btn_historial).setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_editar_paciente).setOnClickListener {
            val intent = Intent(this, FormPacienteActivity::class.java)
            intent.putExtra("modoEdicion", true)
            intent.putExtra("id", pacienteId)
            intent.putExtra("nombre", nombre)
            intent.putExtra("fechaNacimiento", fechaNacimiento)
            intent.putExtra("diagnostico", diagnostico)
            intent.putExtra("cuarto", cuarto)
            intent.putExtra("estado", estado)
            editarLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btn_aplicar).setOnClickListener {
            val intent = Intent(this, SeleccionarInstrumentoActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_eliminar_paciente).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar paciente")
                .setMessage("¿Eliminar a $nombre del sistema? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    FirebaseDatabase.getInstance()
                        .getReference("pacientes")
                        .child(pacienteId.toString())
                        .removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun calcularEdad(fechaNacimiento: String): Int {
        return try {
            val partes = fechaNacimiento.split("-")
            val anio = partes[0].toInt()
            val mes = partes[1].toInt()
            val dia = partes[2].toInt()
            val hoy = java.util.Calendar.getInstance()
            var edad = hoy.get(java.util.Calendar.YEAR) - anio
            val mesHoy = hoy.get(java.util.Calendar.MONTH) + 1
            val diaHoy = hoy.get(java.util.Calendar.DAY_OF_MONTH)
            if (mesHoy < mes || (mesHoy == mes && diaHoy < dia)) edad--
            edad
        } catch (e: Exception) { 0 }
    }
}
