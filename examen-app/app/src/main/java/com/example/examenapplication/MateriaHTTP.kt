package com.example.examenapplication

import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result

class MateriaHTTP(
    var codigoBarra: Int,
    var nombre: String,
    var codigo: Int,
    var descripcion: String,
    var activo: Boolean,
    var fechaCreacion: String,
    var numeroHorasPorSemana: Int,
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var id: Int? = null
) {
    constructor(): this (0, "", 0, "", false, "", 0, 0)


    val url = "http://192.168.100.206:1337/Materia"

    fun crearMateria(idEstudiante: Int?) {

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



    fun eliminar(id:Int?){
        val urlParam = url+'/'+id
        urlParam.httpDelete()
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
    fun obtenerPorId(idEstudiante: Int?){
//        val urlParam = url+'/'+id
        val parametros = listOf(
            "idEstudiante" to idEstudiante

        )
        url.httpGet(parametros)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("httpres", "Error: ${ex}")
                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.i("data", "Data: ${data}")

                        val materiaArray = Klaxon().parseArray<MateriaHTTP>(data)
                        Log.i("httpres", "Datos: ${materiaArray?.toString()}")
                        if (materiaArray != null) {
                            BDD.materias.clear()
                            for ( materia in materiaArray.iterator()){
                                Log.i("httpres", "Estudiante: ${materia.nombre}")
                                Log.i("httpres", "Estudiante: ${materia}")

                                val materiaInsert = MateriaHTTP(materia.codigoBarra, materia.nombre, materia.codigo,
                                    materia.descripcion, materia.activo, materia.fechaCreacion, materia.numeroHorasPorSemana,
                                    materia.createdAt, materia.updatedAt, materia.id)
                                BDD.materias.add(materiaInsert)
                            }
                        }

                    }
                }
            }
    }

    fun actualizar(id:Int?){
        val urlParam = url+'/'+id

        val parametros = listOf(
            "codigoBarra" to codigoBarra,
            "nombre" to nombre,
            "codigo" to codigo,
            "descripcion" to descripcion,
            "activo" to activo,
            "fechaCreacion" to fechaCreacion,
            "numeroHorasPorSemana" to numeroHorasPorSemana
//            "idEstudiante" to idEstudiante

        )

        urlParam.httpPut(parametros)
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