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

class AplicarInstrumentoActivity : AppCompatActivity() {

    private lateinit var q1Options: List<TextView>
    private lateinit var q2Options: List<TextView>
    private lateinit var regOptions: List<TextView>
    private lateinit var attOptions: List<TextView>
    private lateinit var recallOptions: List<TextView>
    private lateinit var langNamingOptions: List<TextView>
    private lateinit var langRepeatOptions: List<TextView>
    private lateinit var langCommandOptions: List<TextView>
    private lateinit var langReadOptions: List<TextView>
    private lateinit var langWriteOptions: List<TextView>
    private lateinit var figureOptions: List<TextView>

    private lateinit var tvTotalScore: TextView
    private val selectedScores = HashMap<String, Int>()
    private var pacienteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicar_instrumento)

        pacienteId = intent.getIntExtra("pacienteId", 0)
        val nombre = intent.getStringExtra("nombre")

        tvTotalScore = findViewById(R.id.tvTotalScore)
        findViewById<TextView>(R.id.tvNombrePacienteInstrumento).text = nombre

        initOptionGroups()
        setupAllSelectionGroups()

        findViewById<Button>(R.id.btnFinalizarEvaluacion).setOnClickListener {
            guardarEvaluacion()
        }
    }

    private fun initOptionGroups() {
        q1Options = listOf(
            findViewById(R.id.btnQ1_0), findViewById(R.id.btnQ1_1),
            findViewById(R.id.btnQ1_2), findViewById(R.id.btnQ1_3),
            findViewById(R.id.btnQ1_4), findViewById(R.id.btnQ1_5)
        )
        q2Options = listOf(
            findViewById(R.id.btnQ2_0), findViewById(R.id.btnQ2_1),
            findViewById(R.id.btnQ2_2), findViewById(R.id.btnQ2_3),
            findViewById(R.id.btnQ2_4), findViewById(R.id.btnQ2_5)
        )
        regOptions = listOf(
            findViewById(R.id.btnReg_0), findViewById(R.id.btnReg_1),
            findViewById(R.id.btnReg_2), findViewById(R.id.btnReg_3)
        )
        attOptions = listOf(
            findViewById(R.id.btnAtt_0), findViewById(R.id.btnAtt_1),
            findViewById(R.id.btnAtt_2), findViewById(R.id.btnAtt_3),
            findViewById(R.id.btnAtt_4), findViewById(R.id.btnAtt_5)
        )
        recallOptions = listOf(
            findViewById(R.id.btnRecall_0), findViewById(R.id.btnRecall_1),
            findViewById(R.id.btnRecall_2), findViewById(R.id.btnRecall_3)
        )
        langNamingOptions = listOf(
            findViewById(R.id.btnLangNaming_0), findViewById(R.id.btnLangNaming_1),
            findViewById(R.id.btnLangNaming_2)
        )
        langRepeatOptions = listOf(
            findViewById(R.id.btnLangRepeat_0), findViewById(R.id.btnLangRepeat_1)
        )
        langCommandOptions = listOf(
            findViewById(R.id.btnLangCommand_0), findViewById(R.id.btnLangCommand_1),
            findViewById(R.id.btnLangCommand_2), findViewById(R.id.btnLangCommand_3)
        )
        langReadOptions = listOf(
            findViewById(R.id.btnLangRead_0), findViewById(R.id.btnLangRead_1)
        )
        langWriteOptions = listOf(
            findViewById(R.id.btnLangWrite_0), findViewById(R.id.btnLangWrite_1)
        )
        figureOptions = listOf(
            findViewById(R.id.btnFigure_0), findViewById(R.id.btnFigure_1)
        )
    }

    private fun setupAllSelectionGroups() {
        setupGroup(q1Options, "q1")
        setupGroup(q2Options, "q2")
        setupGroup(regOptions, "reg")
        setupGroup(attOptions, "att")
        setupGroup(recallOptions, "recall")
        setupGroup(langNamingOptions, "naming")
        setupGroup(langRepeatOptions, "repeat")
        setupGroup(langCommandOptions, "command")
        setupGroup(langReadOptions, "read")
        setupGroup(langWriteOptions, "write")
        setupGroup(figureOptions, "figure")
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
            instrumento = "MMSE",
            fecha = fecha,
            puntaje = total,
            puntajeTotal = 30,
            evaluador = evaluador
        )

        val ref = FirebaseDatabase.getInstance()
            .getReference("evaluaciones")
            .child(pacienteId.toString())

        ref.push().setValue(evaluacion).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Evaluación guardada", Toast.LENGTH_SHORT).show()
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
