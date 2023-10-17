package com.example.nuevopaquete

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.Alumno
import com.example.listalumnos.AlumnoAdapter
import com.example.listalumnos.MainActivityNuevo
import com.example.listalumnos.R
import com.example.listalumnos.databinding.ActivityMainBinding
//import com.example.nuevopaquete.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var idAlumno: Int = 0
    private var data = ArrayList<Alumno>()
    private lateinit var rvAdapter: AlumnoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aquí debes agregar la lógica para inicializar y configurar el RecyclerView,
        // así como el adaptador (rvAdapter).

        // Ejemplo:
        // binding.recyclerview.layoutManager = LinearLayoutManager(this)
        // rvAdapter = AlumnoAdapter(this, data, object : AlumnoAdapter.OptionsMenuClickListener {
        //     override fun onOptionsMenuClicked(position: Int) {
        //         itemOptiomsMenu(position)
        //     }
        // })
        // binding.recyclerview.adapter = rvAdapter
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
                                val alumnoId = data[position].id

                                // Aquí debes agregar la lógica para eliminar el registro de la base de datos
                                // y notificar al adaptador (rvAdapter).

                                // Ejemplo:
                                // val dbalumnos = DBHelperAlumno(this@MainActivity)
                                // dbalumnos.deleteAlumno(alumnoId)
                                // val tmpAlum = data[position]
                                // data.remove(tmpAlum)
                                // rvAdapter.notifyDataSetChanged()
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
