package com.example.examenapplication

import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class MateriaHTTP(
    var codigoBarra: Int,
    var nombre: String,
    var codigo: Int,
    var descripcion: String,
    var activo: Boolean,
    var fechaCreacion: String,
    var numeroHorasPorSemana: Int,
    var idEstudiante: Int,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var id: Int? = null
) {

    val url = "http://192.168.100.178:1337/Materia"

    fun crearMateria() {

        val parametros = listOf(
            "codigoBarra" to codigoBarra,
            "nombre" to nombre,
            "codigo" to codigo,
            "descripcion" to descripcion,
            "activo" to activo,
            "fechaCreacion" to fechaCreacion,
            "numeroHorasPorSemana" to numeroHorasPorSemana,
            "idEstudiante" to idEstudiante


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

                        val materiaClase: MateriaHTTP? = Klaxon()
                            .parse<MateriaHTTP>(usuarioString)

                        Log.i("httpres", "Datos: ${materiaClase?.nombre}")

                    }
                }
            }
    }
}