package mx.edu.itson.estanciaalzheimer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aplicar_instrumento)

        val nombre = intent.getStringExtra("nombre")
        val tvNombre = findViewById<TextView>(R.id.tvNombrePacienteInstrumento)
        tvNombre.text = nombre

        initOptionGroups()
        setupAllSelectionGroups()
    }

    private fun initOptionGroups() {
        q1Options = listOf(
            findViewById(R.id.btnQ1_0),
            findViewById(R.id.btnQ1_1),
            findViewById(R.id.btnQ1_2),
            findViewById(R.id.btnQ1_3),
            findViewById(R.id.btnQ1_4),
            findViewById(R.id.btnQ1_5)
        )

        q2Options = listOf(
            findViewById(R.id.btnQ2_0),
            findViewById(R.id.btnQ2_1),
            findViewById(R.id.btnQ2_2),
            findViewById(R.id.btnQ2_3),
            findViewById(R.id.btnQ2_4),
            findViewById(R.id.btnQ2_5)
        )

        regOptions = listOf(
            findViewById(R.id.btnReg_0),
            findViewById(R.id.btnReg_1),
            findViewById(R.id.btnReg_2),
            findViewById(R.id.btnReg_3)
        )

        attOptions = listOf(
            findViewById(R.id.btnAtt_0),
            findViewById(R.id.btnAtt_1),
            findViewById(R.id.btnAtt_2),
            findViewById(R.id.btnAtt_3),
            findViewById(R.id.btnAtt_4),
            findViewById(R.id.btnAtt_5)
        )

        recallOptions = listOf(
            findViewById(R.id.btnRecall_0),
            findViewById(R.id.btnRecall_1),
            findViewById(R.id.btnRecall_2),
            findViewById(R.id.btnRecall_3)
        )

        langNamingOptions = listOf(
            findViewById(R.id.btnLangNaming_0),
            findViewById(R.id.btnLangNaming_1),
            findViewById(R.id.btnLangNaming_2)
        )

        langRepeatOptions = listOf(
            findViewById(R.id.btnLangRepeat_0),
            findViewById(R.id.btnLangRepeat_1)
        )

        langCommandOptions = listOf(
            findViewById(R.id.btnLangCommand_0),
            findViewById(R.id.btnLangCommand_1),
            findViewById(R.id.btnLangCommand_2),
            findViewById(R.id.btnLangCommand_3)
        )

        langReadOptions = listOf(
            findViewById(R.id.btnLangRead_0),
            findViewById(R.id.btnLangRead_1)
        )

        langWriteOptions = listOf(
            findViewById(R.id.btnLangWrite_0),
            findViewById(R.id.btnLangWrite_1)
        )

        figureOptions = listOf(
            findViewById(R.id.btnFigure_0),
            findViewById(R.id.btnFigure_1)
        )
    }

    private fun setupAllSelectionGroups() {
        setupSingleSelectionGroup(q1Options)
        setupSingleSelectionGroup(q2Options)
        setupSingleSelectionGroup(regOptions)
        setupSingleSelectionGroup(attOptions)
        setupSingleSelectionGroup(recallOptions)

        setupSingleSelectionGroup(langNamingOptions)
        setupSingleSelectionGroup(langRepeatOptions)
        setupSingleSelectionGroup(langCommandOptions)
        setupSingleSelectionGroup(langReadOptions)
        setupSingleSelectionGroup(langWriteOptions)

        setupSingleSelectionGroup(figureOptions)
    }

    private fun setupSingleSelectionGroup(options: List<TextView>) {
        options.forEach { selectedView ->
            selectedView.setOnClickListener {
                options.forEach { option ->
                    setOptionUnselected(option)
                }
                setOptionSelected(selectedView)
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