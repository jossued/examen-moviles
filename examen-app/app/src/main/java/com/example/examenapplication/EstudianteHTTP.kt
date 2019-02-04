package com.example.examenapplication

import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class EstudianteHttp(
    var nombres: String,
    var apellidos: String,
    var fechaNacimiento: String,
    var semestreActual: Int,
    var graduado: Boolean,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var id: Int? = null
) {

    constructor(): this ("", "", "", 0, false)

    val url = "http://192.168.100.178:1337/Estudiante"

    fun crearEstudiante() {

        val parametros = listOf(
            "nombres" to nombres,
            "apellidos" to apellidos,
            "fechaNacimiento" to fechaNacimiento,
            "semestreActual" to semestreActual,
            "graduado" to graduado

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

    fun obtenerTodos(){
        url.httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("httpres", "Error: ${ex}")
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val estudianteArray = Klaxon().parse<EstudianteHttp>(data)
                        Log.i("httpres", "Datos: ${estudianteArray?.toString()}")
                        if (estudianteArray != null) {
                            for ( estudiante in estudianteArray.iterator()){
                                Log.i("httpres", "Estudiante: ${estudiante}")
                                BDD.estudiantes.add(estudiante)
                            }
                        }

                    }
                }
            }
    }
}