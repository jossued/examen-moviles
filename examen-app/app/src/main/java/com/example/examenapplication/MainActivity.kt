package com.example.examenapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_crear_estudiante.setOnClickListener {
            this.irACrearEstudiante()
        }
        button_listar_estudiante.setOnClickListener {
            this.irAListarEstudiante()
        }
    }

    fun irACrearEstudiante(){
        intent = Intent(this, FormularioEstudianteActivity::class.java)
        this.startActivity(intent)
    }

    fun irAListarEstudiante(){
        intent = Intent(this, ListarEstudianteActivity::class.java)
        this.startActivity(intent)
    }
}
