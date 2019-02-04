package com.example.examenapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener


import kotlinx.android.synthetic.main.activity_formulario_estudiante.*

class FormularioEstudianteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_estudiante)

        intent = Intent(this, MainActivity::class.java)

        button_estudiante_cancelar.setOnClickListener {
            regresar()

        }

        button_estudiante_guardar.setOnClickListener {
            val estudianteFormulario = obtenerParametros()


            estudianteFormulario.crearEstudiante()
            Alerter.create(this@FormularioEstudianteActivity)
                .setTitle("Estudiante creado")
                .setText("Nombre:  ${estudianteFormulario.nombres}")
                .setDuration(10000)
                .setOnHideListener({
                    regresar()
                })
                .show()

        }

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


}


