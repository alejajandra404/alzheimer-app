package mx.edu.itson.estanciaalzheimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistorialActivity : AppCompatActivity() {

    private val evaluaciones = ArrayList<Evaluacion>()
    private lateinit var adaptador: EvaluacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val pacienteId = intent.getIntExtra("pacienteId", 0)
        val nombre = intent.getStringExtra("nombre")

        findViewById<TextView>(R.id.tvPacienteHistorial).text = "Paciente: $nombre"

        val listview = findViewById<ListView>(R.id.lv_evaluaciones)
        adaptador = EvaluacionAdapter(this, evaluaciones)
        listview.adapter = adaptador

        val ref = FirebaseDatabase.getInstance()
            .getReference("evaluaciones")
            .child(pacienteId.toString())

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                evaluaciones.clear()
                for (child in snapshot.children) {
                    val evaluacion = child.getValue(Evaluacion::class.java)
                    if (evaluacion != null) evaluaciones.add(evaluacion)
                }
                evaluaciones.sortByDescending { it.fecha }
                adaptador.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HistorialActivity, "Error al cargar historial", Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<Button>(R.id.btnVerResultados).setOnClickListener {
            val intent = Intent(this, ResultadosActivity::class.java)
            intent.putExtra("pacienteId", pacienteId)
            intent.putExtra("nombre", nombre)
            startActivity(intent)
        }
    }

    private inner class EvaluacionAdapter(
        private val contexto: Context,
        private val lista: ArrayList<Evaluacion>
    ) : BaseAdapter() {

        override fun getCount() = lista.size
        override fun getItem(position: Int): Any = lista[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val e = lista[position]
            val vista = LayoutInflater.from(contexto).inflate(R.layout.item_evaluacion, null)

            vista.findViewById<TextView>(R.id.tv_instrumento_item).text = e.instrumento
            vista.findViewById<TextView>(R.id.tv_fecha_item).text = "Fecha: ${e.fecha}"
            vista.findViewById<TextView>(R.id.tv_evaluador_item).text = "Evaluador: ${e.evaluador}"
            vista.findViewById<TextView>(R.id.tv_puntaje_item).text = "Puntaje: ${e.puntaje}/${e.puntajeTotal}"

            return vista
        }
    }
}
