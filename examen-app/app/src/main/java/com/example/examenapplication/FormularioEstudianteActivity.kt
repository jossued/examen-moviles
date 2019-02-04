package com.example.examenapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

import kotlinx.android.synthetic.main.activity_formulario_estudiante.*

class FormularioEstudianteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_estudiante)

        val url = "http://192.168.100.206:1337/Estudiante"

        intent = Intent(this, MainActivity::class.java)

        button_estudiante_cancelar.setOnClickListener {

            this.startActivity(intent)
            this.finish()
        }

        button_estudiante_guardar.setOnClickListener {
            crearEstudiante(url)
            this.startActivity(intent)
            this.finish()
        }

    }

    fun obtenerParametros(): EstudianteHttp {

        val nombres = editText_estudiante_nombres.text.toString()
        val apellidos = editText_estudiante_apellidos.text.toString()
        val fechaNacimiento = editText_estudiante_fechaNacimiento.text.toString()
        val semestreActual = editText_estudiante_semestre.text.toString().toInt()
        val graduado = graduado.isChecked()

        return EstudianteHttp(nombres, apellidos, fechaNacimiento, semestreActual, graduado)
    }

    fun crearEstudiante(url: String) {

        val parametrosEstudiante = obtenerParametros()

        val parametros = listOf(
            "nombres" to parametrosEstudiante.nombres,
            "apellidos" to parametrosEstudiante.apellidos,
            "fechaNacimiento" to parametrosEstudiante.fechaNacimiento,
            "semestreActual" to parametrosEstudiante.semestreActual,
            "graduado" to parametrosEstudiante.graduado

        )

        Log.i("httpres", parametros.toString())

        url
            .httpPost(parametros)
            .responseString { request, response, result ->

                when (result) {
                    is Result.Failure -> {
                        val exepcion = result.getException()
                        Log.i("httpres", "Error: ${exepcion}")
                        Log.i("httpres", "Error: ${response}")

                    }
                    is Result.Success -> {

                        val usuarioString = result.get()

                        val estudianteClase: EstudianteHttp? = Klaxon()
                            .parse<EstudianteHttp>(usuarioString)

                        Log.i("httpres", "Datos: ${estudianteClase?.nombres}")

                    }
                }
            }
    }
}

class EstudianteHttp(
    var nombres: String,
    var apellidos: String,
    var fechaNacimiento: String,
    var semestreActual: Int,
    var graduado: Boolean,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var id: Int? = null
) {}
