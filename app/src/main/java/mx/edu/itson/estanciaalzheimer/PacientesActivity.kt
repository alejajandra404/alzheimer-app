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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PacientesActivity : AppCompatActivity() {

    private val pacientes = ArrayList<Paciente>()
    private lateinit var adaptador: PacienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pacientes)

        val listview: ListView = findViewById(R.id.lv_pacientes)
        adaptador = PacienteAdapter(this, pacientes)
        listview.adapter = adaptador

        listview.setOnItemClickListener { _, _, position, _ ->
            val p = pacientes[position]
            val intent = Intent(this, DetallePacienteActivity::class.java)
            intent.putExtra("id", p.id)
            intent.putExtra("nombre", p.nombre)
            intent.putExtra("edad", p.edad)
            intent.putExtra("diagnostico", p.diagnostico)
            intent.putExtra("cuarto", p.cuarto)
            intent.putExtra("estado", p.estado)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@PacientesActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })

        val ref = FirebaseDatabase.getInstance().getReference("pacientes")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pacientes.clear()
                for (child in snapshot.children) {
                    val paciente = child.getValue(Paciente::class.java)
                    if (paciente != null) pacientes.add(paciente)
                }
                adaptador.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PacientesActivity, "Error al cargar pacientes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private inner class PacienteAdapter(
        private val contexto: Context,
        private val lista: ArrayList<Paciente>
    ) : BaseAdapter() {

        override fun getCount() = lista.size
        override fun getItem(position: Int): Any = lista[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val p = lista[position]
            val vista = LayoutInflater.from(contexto).inflate(R.layout.item_paciente, null)

            val nombre = vista.findViewById<TextView>(R.id.tv_nombre)
            val edad = vista.findViewById<TextView>(R.id.tv_edad)
            val diagnostico = vista.findViewById<TextView>(R.id.tv_diagnostico)
            val cuarto = vista.findViewById<TextView>(R.id.tv_cuarto)
            val estado = vista.findViewById<TextView>(R.id.tv_estado)

            nombre.text = p.nombre
            edad.text = p.edad.toString()
            diagnostico.text = p.diagnostico
            cuarto.text = p.cuarto
            estado.text = p.estado

            when (p.estado) {
                "NEEDS_EVAL" -> estado.setTextColor(ContextCompat.getColor(contexto, R.color.status_needs_eval))
                "STABLE" -> estado.setTextColor(ContextCompat.getColor(contexto, R.color.status_stable))
                "OBSERVATION" -> estado.setTextColor(ContextCompat.getColor(contexto, R.color.status_observation))
                "RESTING" -> estado.setTextColor(ContextCompat.getColor(contexto, R.color.text_secondary))
            }

            return vista
        }
    }
}
