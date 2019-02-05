package com.example.examenapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_listar_materia.*

class ListarMateriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_materia)

        val estudianteMostrar = intent.getParcelableExtra<Estudiante?>("estudiante_pasar")
        MateriaHTTP().obtenerPorId(estudianteMostrar!!.id)

        if (estudianteMostrar != null) {
            Log.i("intent-mostar", estudianteMostrar.nombres)
            Log.i("intent-mostrar", "${estudianteMostrar.id}")
            mostrarDatos(estudianteMostrar)
        }

        for (materia in BDD.materias) {
            Log.i("bdd-m", materia.nombre)
        }

        button_crear_materia.setOnClickListener {
            irACrearMateria(estudianteMostrar)
        }

        val layoutManager = LinearLayoutManager(this)
        val rv = rview_materias

        val adaptador = MateriasAdaptador(BDD.materias, this, estudianteMostrar)

        rview_materias.layoutManager = layoutManager
        rview_materias.itemAnimator = DefaultItemAnimator()
        rview_materias.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }

    fun mostrarDatos(estudiante: Estudiante) {
        textView_nombresE.setText(estudiante.nombres)
        textView_apellidosE.setText(estudiante.apellidos)
        textView_fechaNacimiento.setText(estudiante.fechaNacimiento)
        textView_semestreE.setText(estudiante.semestreActual.toString())
    }

    fun irACrearMateria(estudiante: Estudiante){
        val intent = Intent(this, FormularioMateriaActivity::class.java)
        intent.putExtra("estudiante", estudiante)
        this.startActivity(intent)
        this.finish()
    }
}

class MateriasAdaptador(val listaMaterias: ArrayList<MateriaHTTP>, private val contexto: ListarMateriaActivity, val estudiante: Estudiante) :
    RecyclerView.Adapter<MateriasAdaptador.MyViewHolder2>() {

    inner class MyViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

        var nombreTextView: TextView
        var codigoTextView: TextView
        var boton_opciones: Button

        init {
            Log.i("debug", "Entro en Holder")
            nombreTextView = view.findViewById(R.id.textView_nombreMateria) as TextView
            codigoTextView = view.findViewById(R.id.textView_codigoMateria) as TextView
            boton_opciones = view.findViewById(R.id.button_opcionesM) as Button

            val layout = view.findViewById(R.id.relative_layout_materias) as RelativeLayout


        }


    }

    // Definimos el layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder2 {
        Log.i("debug", "Entro en CreateH")

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.lista_materias_rvlayout,
                parent,
                false
            )

        return MyViewHolder2(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        val persona = listaMaterias[position]
        Log.i("debug", "Entro en BindH")


        Log.i("materias", persona.nombre)
        Log.i("materias", persona.codigo.toString())
        holder.nombreTextView.setText(persona.nombre)
        holder.codigoTextView.setText(persona.codigo.toString())

        holder.boton_opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.boton_opciones)
            //inflating menu from xml resource
            popup.inflate(R.menu.materias_menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.editar_materia -> {
                        val intentEditar = Intent(contexto, FormularioMateriaActivity::class.java)
                        Log.i(
                            "paso",
                            "${persona.nombre}, ${persona.codigo}, ${persona.descripcion}, ${persona.id}"
                        )
//                        intentEditar.putExtra("id_usuario", persona.id)

                        val materiaActualizar = Materia(
                            persona.id!!,
                            persona.codigoBarra,
                            persona.nombre,
                            persona.codigo,
                            persona.descripcion,
                            persona.activo,
                            persona.fechaCreacion,
                            persona.numeroHorasPorSemana
                        )

                        intentEditar.putExtra("materia_pasar", materiaActualizar)
                        intentEditar.putExtra("estudiante", estudiante)
                        //contexto.startActivityForResult(intentEditar, ListarEstudianteActivity.requestCodeActualizar)
                        contexto.startActivity(intentEditar)
                        contexto.finish()
                        notifyDataSetChanged()
                        true
                    }
                    R.id.eliminar_materia -> {
                        val builder = AlertDialog.Builder(contexto)
                        builder
                            .setMessage("Estas seguro de eliminar el estudiante?")
                            .setPositiveButton(
                                "Si, de una"
                            ) { dialog, which ->
                                MateriaHTTP().eliminar(persona.id)
                                Alerter.create(contexto)
                                    .setText("Materia ${persona.nombre} eliminada")
                                    .show()
                                listaMaterias.removeAt(position)
                                notifyDataSetChanged()

                            }
                            .setNegativeButton(
                                "No"
                            ) { dialog, which ->
                                Alerter.create(contexto)
                                    .setText("Selecciono que NO")
                                    .show()
                            }


                        val dialogo = builder.create()
                        dialogo.show()


                        true
                    }
                    R.id.compartir_materia -> {
                        val texto = persona.nombre

                        val intent = Intent(Intent.ACTION_SEND)

                        intent.type = "text/html"

                        intent.putExtra(Intent.EXTRA_TEXT, texto)

                        contexto.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

    }
    override fun getItemCount(): Int {
        Log.i("debug", "Entro en Size")

        return listaMaterias.size
    }


}