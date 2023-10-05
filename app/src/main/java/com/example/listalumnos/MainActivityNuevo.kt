package com.example.listalumnos

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.listalumnos.databinding.ActivityMainNuevoBinding

class MainActivityNuevo : AppCompatActivity() {
    private lateinit var binding: ActivityMainNuevoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNuevoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Creamos la conexion a la BD
        val dbalumnos = DBHelperAlumno(this)

        binding.btnGuardar.setOnClickListener {
            //Pasamos los valores de los editText a variables
            val txtNom = binding.txtNombre.text
            val txtCue = binding.txtCuenta.text
            val txtCorr = binding.txtCorreo.text
            val txtImg = binding.txtImage.text

            //Abrimos la base de datos en modo escritura
            val db = dbalumnos.writableDatabase

            //Pasamos los valores de las cajas a una variable ContentValues
            val newReg = ContentValues()
            newReg.put("nombre",txtNom.toString())
            newReg.put("nocuenta",txtCue.toString())
            newReg.put("email",txtCorr.toString())
            newReg.put("imagen",txtImg.toString())

            //Insertamos el registro
            val res = db.insert("alumnos", null, newReg)
            db.close()

            if (res.toInt() == -1) {
                Toast.makeText(this, "No se inserto el registro", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Registro insertado con éxito", Toast.LENGTH_LONG).show()
                binding.txtNombre.text.clear()
                binding.txtCuenta.text.clear()
                binding.txtCorreo.text.clear()
                binding.txtImage.text.clear()
            }

            //Creamos el Intent para pasarnos al MainActivity
            val intento2 = Intent(this,MainActivity::class.java)
            startActivity(intento2)

        }
    }
}