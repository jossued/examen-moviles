package com.example.examenapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_listar_estudiante.*

import android.widget.PopupMenu
//import android.R




class ListarEstudianteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_estudiante)

        EstudianteHttp().obtenerTodos()
        for (estudiante in BDD.estudiantes) {
            Log.i("bdd-", estudiante.nombres)
        }

        val layoutManager = LinearLayoutManager(this)
        val rv = rview_estudiantes

        val adaptador = PersonasAdaptador(BDD.estudiantes, this)

        rview_estudiantes.layoutManager = layoutManager
        rview_estudiantes.itemAnimator = DefaultItemAnimator()
        rview_estudiantes.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.estudiantes_menu, menu)
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

                val estudiante = intent.getParcelableExtra<Estudiante?>("estudiante_actualizar")


                if (estudiante != null) {
                    actualizarUsuario(estudiante)
                }


            }

            RESULT_CANCELED -> {
                Log.i("error", "Error")
            }
        }

    }

    fun actualizarUsuario(estudiante: Estudiante) {
        val estudianteActualizar = EstudianteHttp(estudiante.nombres, estudiante.apellidos, estudiante.fechaNacimiento, estudiante.semestreActual, estudiante.graduado)
        estudianteActualizar.actualizar(estudiante.id)
    }

    companion object {
        val requestCodeActualizar = 101
    }

}


class PersonasAdaptador(val listaPersonas: ArrayList<EstudianteHttp>, private val contexto: ListarEstudianteActivity) :
    RecyclerView.Adapter<PersonasAdaptador.MyViewHolder>() {


    // val intentEditar = intent

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        var nombreTextView: TextView
        var apellidoTextView: TextView
//        var fechaNacimientoTextView: TextView
        var boton_opciones: Button

        init {
            nombreTextView = view.findViewById(R.id.textView_nombre) as TextView
            apellidoTextView = view.findViewById(R.id.textView_apellido) as TextView
//            fechaNacimientoTextView = view.findViewById(R.id.textView_fechaNacimiento) as TextView

            boton_opciones = view.findViewById(R.id.button_opciones) as Button

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

                }*/


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
        val persona = listaPersonas[position]

        holder.nombreTextView.setText(persona.nombres)
        holder.apellidoTextView.setText(persona.apellidos)
//        holder.fechaNacimientoTextView.setText(persona.fechaNacimiento)

        holder.boton_opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.boton_opciones)
            //inflating menu from xml resource
            popup.inflate(R.menu.estudiantes_menu)

            popup.setOnMenuItemClickListener {
                item ->
                when (item.getItemId()) {
                    R.id.editar_estudiante -> {
                        val intentEditar = Intent(contexto, FormularioEstudianteActivity::class.java)
                        Log.i("paso", "${persona.nombres}, ${persona.apellidos}, ${persona.fechaNacimiento}, ${persona.id}")
//                        intentEditar.putExtra("id_usuario", persona.id)

                        val estudianteActualizar = Estudiante(persona.id!!, persona.nombres, persona.apellidos, persona.semestreActual, persona.fechaNacimiento, persona.graduado)

                        intentEditar.putExtra("estudiante_pasar",estudianteActualizar)
                        contexto.startActivityForResult(intentEditar, ListarEstudianteActivity.requestCodeActualizar)
                        true
                    }
                    R.id.eliminar_estudiante -> {
                        EstudianteHttp().eliminar(persona.id)
                        listaPersonas.removeAt(position)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.compartir_estudiante ->
                         true
                    R.id.listar_materias ->
                         true
                    else ->  false
                }
            }

            popup.show()
        }

    }

    override fun getItemCount(): Int {
        return listaPersonas.size
    }

    companion object {
        val requestCodeActualizar = 101
    }

}