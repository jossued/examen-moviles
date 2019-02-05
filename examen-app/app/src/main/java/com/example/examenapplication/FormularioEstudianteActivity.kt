package com.example.examenapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapadoo.alerter.Alerter


import kotlinx.android.synthetic.main.activity_formulario_estudiante.*
//import android.support.test.espresso.matcher.ViewMatchers.isChecked


class FormularioEstudianteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_estudiante)

        val estudianteActualizar = intent.getParcelableExtra<Estudiante?>("estudiante_pasar")

        if (estudianteActualizar != null) {
            Log.i("intent-editar", estudianteActualizar.nombres)
            Log.i("intent-editar", "${estudianteActualizar.semestreActual}")
            mostrarDatos(estudianteActualizar)
        }

        intent = Intent(this, MainActivity::class.java)

        button_estudiante_cancelar.setOnClickListener {
            regresar()

        }

        button_estudiante_guardar.setOnClickListener {
            val estudianteFormulario = obtenerParametros()


            if (estudianteActualizar == null) {
                estudianteFormulario.crearEstudiante()
                Alerter.create(this@FormularioEstudianteActivity)
                    .setTitle("Estudiante creado")
                    .setText("Nombre:  ${estudianteFormulario.nombres}")
                    .setDuration(10000)
                    .setOnHideListener({
                        regresar()
                    })
                    .show()
            } else if (estudianteActualizar != null) {

                /*
                val estudianteActualizado = obtenerParametros()

                devolverActualizar(estudianteActualizar, estudianteActualizado)
                */

                estudianteFormulario.actualizar(estudianteActualizar.id)
                Alerter.create(this@FormularioEstudianteActivity)
                    .setTitle("Estudiante actualizado")
                    .setText("Nombre:  ${estudianteFormulario.nombres}")
                    .setDuration(10000)
                    .setOnHideListener {
                        this.startActivity(Intent(this, ListarEstudianteActivity::class.java))
                        this.finish()
                    }
                    .show()

            }


        }

    }

    fun devolverActualizar(estudiantePorActualizar: Estudiante, estudianteActual: EstudianteHttp) {

        estudiantePorActualizar.nombres = estudianteActual.nombres
        estudiantePorActualizar.semestreActual = estudianteActual.semestreActual
        estudiantePorActualizar.fechaNacimiento = estudianteActual.fechaNacimiento
        estudiantePorActualizar.semestreActual = estudianteActual.semestreActual
        estudiantePorActualizar.graduado = estudianteActual.graduado

        val intentRespuesta = Intent()

        Log.i("estudiante_pasar", "${estudiantePorActualizar.nombres}")
        intentRespuesta.putExtra("estudiante_pasar", estudiantePorActualizar)

        this.setResult(
            Activity.RESULT_OK,
            intentRespuesta
        )

        this.finish()

    }
    fun regresar(){
        this.startActivity(intent)
        this.finish()
    }

    fun obtenerParametros(): EstudianteHttp {

        val nombres = editText_estudiante_nombres.text.toString()
        val apellidos = editText_estudiante_apellidos.text.toString()
        val fechaNacimiento = editText_estudiante_fechaNacimiento.text.toString()
        val semestreActual = editText_estudiante_semestre.text.toString().toInt()
        val graduado = graduado.isChecked()

        return EstudianteHttp(nombres, apellidos, fechaNacimiento, semestreActual, graduado)
    }

    fun mostrarDatos(estudiante: Estudiante) {
        editText_estudiante_nombres.setText(estudiante.nombres)
        editText_estudiante_apellidos.setText(estudiante.apellidos)
        editText_estudiante_fechaNacimiento.setText(estudiante.fechaNacimiento)
        editText_estudiante_semestre.setText(estudiante.semestreActual.toString())
        graduado.isChecked = estudiante.graduado
    }

}


