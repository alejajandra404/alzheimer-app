package mx.edu.itson.estanciaalzheimer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AplicarTinettiActivity : AppCompatActivity() {

    private lateinit var eq1Options: List<TextView>
    private lateinit var eq2Options: List<TextView>
    private lateinit var eq3Options: List<TextView>
    private lateinit var eq4Options: List<TextView>
    private lateinit var eq5Options: List<TextView>
    private lateinit var eq6Options: List<TextView>
    private lateinit var eq7Options: List<TextView>
    private lateinit var eq8Options: List<TextView>
    private lateinit var eq9Options: List<TextView>
    private lateinit var mar1Options: List<TextView>
    private lateinit var mar2Options: List<TextView>
    private lateinit var mar3Options: List<TextView>
    private lateinit var mar4Options: List<TextView>
    private lateinit var mar5Options: List<TextView>
    private lateinit var mar6Options: List<TextView>
    private lateinit var mar7Options: List<TextView>
    private lateinit var mar8Options: List<TextView>

    private lateinit var tvTotalScore: TextView
    private val selectedScores = HashMap<String, Int>()
    private var pacienteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicar_tinetti)

        pacienteId = intent.getIntExtra("pacienteId", 0)
        val nombre = intent.getStringExtra("nombre")

        tvTotalScore = findViewById(R.id.tvTotalScoreTinetti)
        findViewById<TextView>(R.id.tvNombrePacienteTinetti).text = nombre

        initOptionGroups()
        setupAllGroups()

        findViewById<Button>(R.id.btnFinalizarTinetti).setOnClickListener {
            guardarEvaluacion()
        }
    }

    private fun initOptionGroups() {
        eq1Options = listOf(findViewById(R.id.btnEq1_0), findViewById(R.id.btnEq1_1))
        eq2Options = listOf(findViewById(R.id.btnEq2_0), findViewById(R.id.btnEq2_1), findViewById(R.id.btnEq2_2))
        eq3Options = listOf(findViewById(R.id.btnEq3_0), findViewById(R.id.btnEq3_1), findViewById(R.id.btnEq3_2))
        eq4Options = listOf(findViewById(R.id.btnEq4_0), findViewById(R.id.btnEq4_1), findViewById(R.id.btnEq4_2))
        eq5Options = listOf(findViewById(R.id.btnEq5_0), findViewById(R.id.btnEq5_1), findViewById(R.id.btnEq5_2))
        eq6Options = listOf(findViewById(R.id.btnEq6_0), findViewById(R.id.btnEq6_1), findViewById(R.id.btnEq6_2))
        eq7Options = listOf(findViewById(R.id.btnEq7_0), findViewById(R.id.btnEq7_1))
        eq8Options = listOf(findViewById(R.id.btnEq8_0), findViewById(R.id.btnEq8_1), findViewById(R.id.btnEq8_2))
        eq9Options = listOf(findViewById(R.id.btnEq9_0), findViewById(R.id.btnEq9_1), findViewById(R.id.btnEq9_2))
        mar1Options = listOf(findViewById(R.id.btnMar1_0), findViewById(R.id.btnMar1_1))
        mar2Options = listOf(findViewById(R.id.btnMar2_0), findViewById(R.id.btnMar2_1))
        mar3Options = listOf(findViewById(R.id.btnMar3_0), findViewById(R.id.btnMar3_1))
        mar4Options = listOf(findViewById(R.id.btnMar4_0), findViewById(R.id.btnMar4_1))
        mar5Options = listOf(findViewById(R.id.btnMar5_0), findViewById(R.id.btnMar5_1))
        mar6Options = listOf(findViewById(R.id.btnMar6_0), findViewById(R.id.btnMar6_1), findViewById(R.id.btnMar6_2))
        mar7Options = listOf(findViewById(R.id.btnMar7_0), findViewById(R.id.btnMar7_1), findViewById(R.id.btnMar7_2))
        mar8Options = listOf(findViewById(R.id.btnMar8_0), findViewById(R.id.btnMar8_1))
    }

    private fun setupAllGroups() {
        setupGroup(eq1Options, "eq1")
        setupGroup(eq2Options, "eq2")
        setupGroup(eq3Options, "eq3")
        setupGroup(eq4Options, "eq4")
        setupGroup(eq5Options, "eq5")
        setupGroup(eq6Options, "eq6")
        setupGroup(eq7Options, "eq7")
        setupGroup(eq8Options, "eq8")
        setupGroup(eq9Options, "eq9")
        setupGroup(mar1Options, "mar1")
        setupGroup(mar2Options, "mar2")
        setupGroup(mar3Options, "mar3")
        setupGroup(mar4Options, "mar4")
        setupGroup(mar5Options, "mar5")
        setupGroup(mar6Options, "mar6")
        setupGroup(mar7Options, "mar7")
        setupGroup(mar8Options, "mar8")
    }

    private fun setupGroup(options: List<TextView>, key: String) {
        options.forEach { view ->
            view.setOnClickListener {
                options.forEach { setOptionUnselected(it) }
                setOptionSelected(view)
                selectedScores[key] = view.text.toString().toIntOrNull() ?: 0
                updateTotalScore()
            }
        }
    }

    private fun updateTotalScore() {
        tvTotalScore.text = selectedScores.values.sum().toString()
    }

    private fun guardarEvaluacion() {
        if (pacienteId == 0) {
            Toast.makeText(this, "Error: paciente no identificado", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedScores.isEmpty()) {
            Toast.makeText(this, "Debes responder al menos una pregunta antes de finalizar", Toast.LENGTH_SHORT).show()
            return
        }

        val total = selectedScores.values.sum()
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val evaluador = FirebaseAuth.getInstance().currentUser?.email ?: "Evaluador"

        val evaluacion = Evaluacion(
            pacienteId = pacienteId,
            instrumento = "Tinetti",
            fecha = fecha,
            puntaje = total,
            puntajeTotal = 28,
            evaluador = evaluador
        )

        val ref = FirebaseDatabase.getInstance()
            .getReference("evaluaciones")
            .child(pacienteId.toString())

        ref.push().setValue(evaluacion).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Evaluación Tinetti guardada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar evaluación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setOptionSelected(textView: TextView) {
        textView.setBackgroundResource(R.drawable.bg_option_selected)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun setOptionUnselected(textView: TextView) {
        textView.setBackgroundResource(R.drawable.bg_option_unselected)
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
    }
}
