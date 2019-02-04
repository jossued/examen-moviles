package com.example.examenapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_listar_estudiante.*

class ListarEstudianteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_estudiante)

        EstudianteHttp().obtenerTodos()

        val layoutManager = LinearLayoutManager(this)
        val rv = rview_estudiantes

        for (estudiante in BDD.estudiantes) {
            Log.i("bdd", estudiante.nombres)
        }
        val adaptador = PersonasAdaptador(this)

        rview_estudiantes.layoutManager = layoutManager
        rview_estudiantes.itemAnimator = DefaultItemAnimator()
        rview_estudiantes.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("int", "$resultCode")

        when (resultCode) {
            Activity.RESULT_OK -> {

                Log.i("int", "$requestCode")
                Log.i("int", "$resultCode")
                Log.i("int", "$data")


                Log.i("intent-nombre-apellido", "LLEGOOOO ${data!!.getIntExtra("id", -1)}")
                Log.i("intent-nombre-apellido", "LLEGOOOO ${data!!.getStringExtra("textoNombre")}")
                Log.i("intent-nombre-apellido", "LLEGOOOO ${data!!.getStringExtra("textoApellido")}")
                Log.i("intent-nombre-apellido", "LLEGOOOO ${data!!.getStringExtra("textoEmail")}")

                actualizarUsuario(
                    data!!.getIntExtra("id", -1), data!!.getStringExtra("textoNombre"),
                    data!!.getStringExtra("textoApellido"), data!!.getStringExtra("textoEmail")
                )


            }

            RESULT_CANCELED -> {
                Log.i("error", "Error")
            }
        }

    }

    fun actualizarUsuario(id: Int, textoNombre: String, textoApellido: String, textoEmail: String) {

    }

    companion object {
        val requestCodeActualizar = 101
    }
}


class PersonasAdaptador(private val contexto: ListarEstudianteActivity) :
    RecyclerView.Adapter<PersonasAdaptador.MyViewHolder>() {


    // val intentEditar = intent

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTextView: TextView
        var apellidoTextView: TextView
        var fechaNacimientoTextView: TextView


        init {
            nombreTextView = view.findViewById(R.id.textView_nombre) as TextView
            apellidoTextView = view.findViewById(R.id.textView_apellido) as TextView
            fechaNacimientoTextView = view.findViewById(R.id.textView_fechaNacimiento) as TextView

            val layout = view.findViewById(R.id.relative_layout_estudiantes) as RelativeLayout
            /*
            layout
                .setOnClickListener {
                    val estudiante = BDD.estudiantes[position]
                    val intentEditar = Intent(contexto, EditarUsuarioActivity::class.java)
                    Log.i("paso", "${usuario.nombre}, ${usuario.apellido}, ${usuario.email}, ${usuario.id}")
                    intentEditar.putExtra("id_usuario", usuario.id)

                    val usuario_pasar = Usuario(usuario.id, usuario.nombre, usuario.apellido, usuario.email)
                    intentEditar.putExtra("usuario_pasar",usuario_pasar)
                    // contexto.startActivity(intentEditar)
                    contexto.startActivityForResult(intentEditar, UsuariosActivity.requestCodeActualizar)
                    // contexto.finish()
//                    contexto.recreate()

                }
*/

        }


    }


    // Definimos el layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.lista_estudiantes_rvlayout,
                parent,
                false
            )

        return MyViewHolder(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val persona = BDD.estudiantes[position]

        holder.nombreTextView.setText(persona.nombres)
        holder.apellidoTextView.setText(persona.apellidos)
        holder.fechaNacimientoTextView.setText(persona.apellidos)


    }

    override fun getItemCount(): Int {
        return BDD.estudiantes.size
    }

    companion object {
        val requestCodeActualizar = 101
    }

}