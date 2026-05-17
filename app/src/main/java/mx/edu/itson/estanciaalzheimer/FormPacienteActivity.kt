package mx.edu.itson.estanciaalzheimer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class FormPacienteActivity : AppCompatActivity() {

    private val estados = arrayOf("NEEDS_EVAL", "STABLE", "OBSERVATION", "RESTING")
    private var fechaNacimientoSeleccionada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_paciente)

        val modoEdicion = intent.getBooleanExtra("modoEdicion", false)
        val pacienteId = intent.getIntExtra("id", 0)

        val tvTitulo = findViewById<TextView>(R.id.tvTituloForm)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val btnFecha = findViewById<Button>(R.id.btnFechaNacimiento)
        val etDiagnostico = findViewById<EditText>(R.id.etDiagnostico)
        val etCuarto = findViewById<EditText>(R.id.etCuarto)
        val spinnerEstado = findViewById<Spinner>(R.id.spinnerEstado)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarPaciente)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter

        btnFecha.setOnClickListener { mostrarDatePicker(btnFecha) }

        if (modoEdicion) {
            tvTitulo.text = "Editar paciente"
            btnGuardar.text = "Guardar cambios"
            findViewById<View>(R.id.layoutNombre).visibility = View.GONE
            findViewById<View>(R.id.layoutFecha).visibility = View.GONE
            etNombre.setText(intent.getStringExtra("nombre"))
            etDiagnostico.setText(intent.getStringExtra("diagnostico"))
            etCuarto.setText(intent.getStringExtra("cuarto"))
            val fn = intent.getStringExtra("fechaNacimiento") ?: ""
            if (fn.isNotEmpty()) {
                fechaNacimientoSeleccionada = fn
                btnFecha.text = fn
            }
            val estadoActual = intent.getStringExtra("estado") ?: "NEEDS_EVAL"
            val idx = estados.indexOf(estadoActual).takeIf { it >= 0 } ?: 0
            spinnerEstado.setSelection(idx)
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val diagnostico = etDiagnostico.text.toString().trim()
            val cuarto = etCuarto.text.toString().trim()
            val estado = spinnerEstado.selectedItem.toString()

            if (nombre.isEmpty() || diagnostico.isEmpty() || cuarto.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (fechaNacimientoSeleccionada.isEmpty()) {
                Toast.makeText(this, "Selecciona la fecha de nacimiento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (modoEdicion) {
                editarPaciente(pacienteId, nombre, fechaNacimientoSeleccionada, diagnostico, cuarto, estado)
            } else {
                agregarPaciente(nombre, fechaNacimientoSeleccionada, diagnostico, cuarto, estado)
            }
        }
    }

    private fun mostrarDatePicker(btnFecha: Button) {
        val cal = Calendar.getInstance()
        val anioInicial = if (fechaNacimientoSeleccionada.isNotEmpty())
            fechaNacimientoSeleccionada.split("-")[0].toIntOrNull() ?: 1950 else 1950

        DatePickerDialog(this, { _, anio, mes, dia ->
            val fecha = "%04d-%02d-%02d".format(anio, mes + 1, dia)
            fechaNacimientoSeleccionada = fecha
            btnFecha.text = fecha
        }, anioInicial, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun agregarPaciente(nombre: String, fechaNacimiento: String, diagnostico: String, cuarto: String, estado: String) {
        val ref = FirebaseDatabase.getInstance().getReference("pacientes")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var maxId = 0
                for (child in snapshot.children) {
                    val id = child.key?.toIntOrNull() ?: 0
                    if (id > maxId) maxId = id
                }
                val newId = maxId + 1
                val paciente = Paciente(newId, nombre, fechaNacimiento, diagnostico, cuarto, estado)
                ref.child(newId.toString()).setValue(paciente)
                    .addOnSuccessListener {
                        Toast.makeText(this@FormPacienteActivity, "Paciente registrado", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@FormPacienteActivity, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FormPacienteActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editarPaciente(id: Int, nombre: String, fechaNacimiento: String, diagnostico: String, cuarto: String, estado: String) {
        val paciente = Paciente(id, nombre, fechaNacimiento, diagnostico, cuarto, estado)
        FirebaseDatabase.getInstance()
            .getReference("pacientes")
            .child(id.toString())
            .setValue(paciente)
            .addOnSuccessListener {
                Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
    }
}
