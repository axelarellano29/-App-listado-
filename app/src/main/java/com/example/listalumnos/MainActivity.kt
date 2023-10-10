package com.example.listalumnos

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Vinculación de las vistas con MainActivity
    private lateinit var binding: ActivityMainBinding

    // Variable para el idAlumno
    var idAlumno: Int = 0
    // ArrayList of class ItemsViewModel
    private var data = ArrayList<Alumno>()
    // Referenciamos el adapter
    private lateinit var rvAdapter: AlumnoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creamos la conexión con la BD
        val dbalumnos = DBHelperAlumno(this)
        val db = dbalumnos.readableDatabase

        // Declaramos un cursor para recorrer los registros en la tabla
        val cursor = db.rawQuery("SELECT * FROM alumnos", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val itemCue = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                val itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val itemImg = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                data.add(Alumno(id, itemNom, itemCue, itemCorr, itemImg))
            } while (cursor.moveToNext())
            db.close()
            cursor.close()

            binding.recyclerview.layoutManager = LinearLayoutManager(this)

            rvAdapter = AlumnoAdapter(this, data, object : AlumnoAdapter.OptionsMenuClickListener {
                override fun onOptionsMenuClicked(position: Int) {
                    itemOptiomsMenu(position)
                }
            })

            binding.recyclerview.adapter = rvAdapter
        }

        val parExtra = intent.extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val image = parExtra?.getString("image")

        if (msje == "nuevo") {
            val insertIndex: Int = data.count()
            data.add(insertIndex, Alumno(0, nombre!!, cuenta!!, correo!!, image!!)) // Assuming id is auto-increment, so passing 0
            rvAdapter.notifyItemInserted(insertIndex)
        }

        binding.faButton.setOnClickListener {
            val intento1 = Intent(this, MainActivityNuevo::class.java)
            startActivity(intento1)
        }
    }

    private fun itemOptiomsMenu(position: Int) {
        val popupMenu = PopupMenu(this, binding.recyclerview[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)

        val intento2 = Intent(this, MainActivityNuevo::class.java)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.borrar -> {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                            .setPositiveButton("Sí") { _, _ ->
                                // Obtener el id del alumno que se desea eliminar
                                val alumnoId = data[position].id

                                // Eliminar el registro de la base de datos
                                val dbalumnos = DBHelperAlumno(this@MainActivity)
                                dbalumnos.deleteAlumno(alumnoId)

                                // Eliminar el registro de la lista y notificar al adaptador
                                val tmpAlum = data[position]
                                data.remove(tmpAlum)
                                rvAdapter.notifyDataSetChanged()
                            }
                            .setNegativeButton("No", null)
                            .show()
                        return true
                    }
                    R.id.editar -> {
                        val nombre = data[position].nombre
                        val cuenta = data[position].cuenta
                        val correo = data[position].correo
                        val image = data[position].imagen
                        val idAlum: Int = position
                        intento2.putExtra("mensaje", "edit")
                        intento2.putExtra("nombre", "$nombre")
                        intento2.putExtra("cuenta", "$cuenta")
                        intento2.putExtra("correo", "$correo")
                        intento2.putExtra("image", "$image")
                        intento2.putExtra("idA", idAlum)
                        startActivity(intento2)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
}
