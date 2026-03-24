package mx.edu.itson.estanciaalzheimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PacientesActivity : AppCompatActivity() {

    var pacientes: ArrayList<Paciente> = ArrayList<Paciente>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pacientes)

        agregarPacientes()

        var listview: ListView = findViewById(R.id.lv_pacientes) as ListView

        var adaptador: PacienteAdapter = PacienteAdapter(this, pacientes)
        listview.adapter = adaptador


        listview.setOnItemClickListener { parent, view, position, id ->
            val pacienteSeleccionado = pacientes[position]

            val intent = Intent(this, DetallePacienteActivity::class.java)

            intent.putExtra("nombre", pacienteSeleccionado.nombre)
            intent.putExtra("edad", pacienteSeleccionado.edad)
            intent.putExtra("diagnostico", pacienteSeleccionado.diagnostico)
            intent.putExtra("cuarto", pacienteSeleccionado.cuarto)
            intent.putExtra("estado", pacienteSeleccionado.estado)

            startActivity(intent)
        }
    }


    fun agregarPacientes(){
        pacientes.add(Paciente(1, "Juan", 21, "Alzheimer", "Cuarto 1", "NEEDS_EVAL"))
        pacientes.add(Paciente(2, "Pedro", 20, "Alzheimer", "Cuarto 4", "OBSERVATION"))
        pacientes.add(Paciente(3, "Alejandra", 21, "Alzheimer", "Cuarto 2", "STABLE"))
        pacientes.add(Paciente(4, "Christopher", 20, "Alzheimer", "Cuarto 3", "RESTING"))

    }

    private class PacienteAdapter: BaseAdapter {
    var paciente = ArrayList<Paciente>()
    var contexto: Context?=null


    constructor(contexto: Context, paciente: ArrayList<Paciente>){
        this.paciente = paciente
        this.contexto = contexto
    }

    override fun getCount(): Int {
        return paciente.size
    }

    override fun getItem(position: Int): Any? {
        return paciente[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var p = paciente[position]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.item_paciente, null)

        var nombre = vista.findViewById(R.id.tv_nombre) as TextView
        var edad = vista.findViewById(R.id.tv_edad) as TextView
        var diagnostico = vista.findViewById(R.id.tv_diagnostico) as TextView
        var cuarto = vista.findViewById(R.id.tv_cuarto) as TextView
        var estado = vista.findViewById(R.id.tv_estado) as TextView

        nombre.setText(p.nombre)
        edad.setText(p.edad.toString())
        diagnostico.setText(p.diagnostico)
        cuarto.setText(p.cuarto)
        estado.setText(p.estado)
        when (p.estado) {
            "NEEDS_EVAL" -> estado.setTextColor(ContextCompat.getColor(contexto!!, R.color.status_needs_eval))
            "STABLE" -> estado.setTextColor(ContextCompat.getColor(contexto!!, R.color.status_stable))
            "OBSERVATION" -> estado.setTextColor(ContextCompat.getColor(contexto!!, R.color.status_observation))
            "RESTING" -> estado.setTextColor(ContextCompat.getColor(contexto!!, R.color.text_secondary))
        }

        return vista
    }


}
}



