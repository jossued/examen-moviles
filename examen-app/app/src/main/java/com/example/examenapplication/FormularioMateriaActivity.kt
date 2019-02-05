package com.example.examenapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_formulario_materia.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FormularioMateriaActivity : AppCompatActivity() {

    var pathActualFoto = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_materia)
        val materiaActualizar = intent.getParcelableExtra<Materia?>("materia_pasar")

        val estudiante = intent.getParcelableExtra<Estudiante?>("estudiante")


        if (materiaActualizar != null) {
            Log.i("intent-editar", materiaActualizar.nombre)
            Log.i("intent-editar", "${materiaActualizar.codigo}")
            mostrarDatos(materiaActualizar)
        }

        button_guardarm.setOnClickListener {
            val materiaFormulario = obtenerParametros()


            if (materiaActualizar == null) {
                materiaFormulario.crearMateria(estudiante?.id)
                Alerter.create(this@FormularioMateriaActivity)
                    .setTitle("Materia creada")
                    .setText("Nombre:  ${materiaFormulario.nombre}")
                    .setDuration(10000)
                    .setOnHideListener({
                        regresar(estudiante)
                    })
                    .show()
            } else if (materiaActualizar != null) {


                materiaFormulario.actualizar(materiaActualizar.id)
                Alerter.create(this@FormularioMateriaActivity)
                    .setTitle("Materia actualizada")
                    .setText("Nombre:  ${materiaFormulario.nombre}")
                    .setDuration(10000)
                    .setOnHideListener {
                        regresar(estudiante)
                    }
                    .show()


            }


        }

        button_foto.setOnClickListener {
            tomarFoto()
        }

    }
    fun regresar(estudiante: Estudiante?){
        val intent = Intent(this, ListarMateriaActivity::class.java)
        intent.putExtra("estudiante_pasar", estudiante)
        this.startActivity(intent)
        this.finish()
    }

    fun obtenerParametros(): MateriaHTTP {

        val nombre = editText_nombre_materia.text.toString()
        val descripcion = editText_desc_materia.text.toString()
        val fechaCreacion = editText_fechac_materia.text.toString()
        val codigoBarra = editText_codigo_barra.text.toString().toInt()
        val codigo = editText_codigo_materia.text.toString().toInt()
        val horasSemana = editText_horas_materia.text.toString().toInt()
        val activo = checkBox_activo.isChecked()

        return MateriaHTTP(codigoBarra, nombre, codigo, descripcion, activo, fechaCreacion,horasSemana)
    }

    fun mostrarDatos(materia: Materia) {
        editText_codigo_barra.setText(materia.codigoBarra.toString())
        editText_nombre_materia.setText(materia.nombre)
        editText_fechac_materia.setText(materia.fechaCreacion)
        editText_codigo_materia.setText(materia.codigo.toString())
        editText_desc_materia.setText(materia.descripcion)
        editText_horas_materia.setText(materia.numeroHorasPorSemana.toString())
        checkBox_activo.isChecked = materia.activo
    }
    fun tomarFoto() {
        val archivoImagen = crearArchivo(
            "JPEG_",
            Environment.DIRECTORY_PICTURES,
            ".jpg")

        pathActualFoto = archivoImagen.absolutePath

        enviarIntentFoto(archivoImagen)

    }

    private fun crearArchivo(
        prefijo: String,
        directorio: String,
        extension: String): File {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(Date())

        val prefijoImagen = prefijo + timeStamp + "_"

        val directorioAGuardarImagen = getExternalFilesDir(directorio)

        return File.createTempFile(
            prefijoImagen, /* prefijo */
            extension, /* fufijo */
            directorioAGuardarImagen /* directorio */
        )
    }

    private fun enviarIntentFoto(archivo: File) {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.examenapplication",
            archivo)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(takePictureIntent, TOMAR_FOTO_REQUEST);

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TOMAR_FOTO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    obtenerInfoCodigoBarras(obtenerBitmapDeArchivo(pathActualFoto))
                    if (!respuestasBarCode.isEmpty()){
                        editText_codigo_barra.setText(respuestasBarCode[0].toString())
                    }
                    for (barcode in respuestasBarCode){
                        Log.i("barcode", barcode)
                    }
                }
            }

        }
    }

    fun obtenerBitmapDeArchivo(path: String): Bitmap {
        val archivoImagen = File(path)
        return BitmapFactory
            .decodeFile(
                archivoImagen.getAbsolutePath()
            )
    }

    fun obtenerInfoCodigoBarras(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
            .visionBarcodeDetector
        Log.i("info", "------- Entro a detectar")
        val result = detector.detectInImage(image)
            .addOnSuccessListener { barCodes ->
                Log.i("info", "------- tamano del barcode ${barCodes.size}")
                respuestasBarCode.add("Ejemplo")
                for (barcode in barCodes) {
                    val bounds = barcode.getBoundingBox()
                    val corners = barcode.getCornerPoints()

                    val rawValue = barcode.getRawValue()

                    Log.i("info", "------- $bounds")
                    Log.i("info", "------- $corners")
                    Log.i("info", "------- $rawValue")

                    respuestasBarCode.add(rawValue.toString())
                }
            }
            .addOnFailureListener {
                Log.i("info", "------- No reconocio nada")
            }
    }



    companion object {
        val TOMAR_FOTO_REQUEST = 1
        var respuestasBarCode = ArrayList<String>()
    }

}