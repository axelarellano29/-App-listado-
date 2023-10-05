package com.example.listalumnos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
//import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Vinculacion de las vistas con MainActivity
    private lateinit var binding: ActivityMainBinding

    //Variable para el idAlumno
    var idAlumno: Int = 0
    // ArrayList of class ItemsViewModel
    private var  data = ArrayList<Alumno>()
    //Referenciamos el adapter
    private lateinit var rvAdapter : AlumnoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Creamos la conexión con la BD
        val dbalumnos = DBHelperAlumno(this)
        //Abrimos la base de datos solo para leer
        val db = dbalumnos.readableDatabase

        //Declaramos un cursor para recorrer los registros en la tabla
        val cursor = db.rawQuery("SELECT * FROM alumnos",null)

        if(cursor.moveToFirst()){
            do {
                //Pasamos los valores de la tabla a variable locales
                idAlumno = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                var itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                var itemCue = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                var itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                var itemImg = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                data.add(
                    Alumno("${itemNom}",
                        "${itemCue}",
                        "${itemCorr}",
                        "${itemImg}"
                    )
                )

            }while (cursor.moveToNext())
            //Cerramos la base de datos
            db.close()
            //Cerramos el cursor
            cursor.close()

            binding.recyclerview.layoutManager = LinearLayoutManager(this)

            // This will pass the ArrayList to our Adapter
            rvAdapter = AlumnoAdapter(this, data, object : AlumnoAdapter.OptionsMenuClickListener{
                override fun onOptionsMenuClicked(position: Int) {
                    //Toast.makeText(this@MainActivity,"onItemClick ${position}",Toast.LENGTH_LONG).show()
                    //funcion para llamar menu opciones
                    itemOptiomsMenu(position)
                }
            })

            binding.recyclerview.adapter = rvAdapter

        } //Fin del if cursor.moveToFirst

        //Add elements to data array
        /*data.add(Alumno("José Nabor", "20102345","jmorfin@ucol.mx","https://imagenpng.com/wp-content/uploads/2017/02/pokemon-hulu-pikach.jpg"))
        data.add(Alumno("Luis Antonio", "20112345","jmorfin@ucol.mx","https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg"))
        data.add(Alumno("Juan Pedro", "20122345","jmorfin@ucol.mx","https://i.pinimg.com/736x/9f/6e/fa/9f6efa277ddcc1e8cfd059f2c560ee53--clipart-gratis-vector-clipart.jpg"))*/

        //Variable para recibir extras
        val parExtra = intent.extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val image = parExtra?.getString("image")

        //Preguntamos se el mensaje es para nuevo alumno
        if (msje== "nuevo"){
            //Sacamos en una variable el total de elementos en nuestra lista
            val insertIndex: Int = data.count()
            //Usamos la variable insertIndex para indicar la posición del nuevo alumno
            data.add(insertIndex,
                Alumno(
                    "${nombre}",
                    "$cuenta}",
                    "${correo}",
                    "${image}"
                )
            )
            //Notificamos que se inserto un nuevo elemento en la lista
            rvAdapter.notifyItemInserted(insertIndex)

        }


        binding.faButton.setOnClickListener {
            val intento1 = Intent(this,MainActivityNuevo::class.java)
            //intento1.putExtra("valor1","Hola mundo")
            startActivity(intento1)

        }

    }

    private fun itemOptiomsMenu(position: Int) {
        val popupMenu = PopupMenu(this,binding.recyclerview[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)
//Para cambiarnos de activity
        val intento2 = Intent(this,MainActivityNuevo::class.java)
//Implementar el click en el item
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.borrar -> {
                        val tmpAlum = data[position]
                        data.remove(tmpAlum)
                        rvAdapter.notifyDataSetChanged()
                        return true
                    }
                    R.id.editar ->{
                        //Tomamos los datos del alumno, en la posición de la lista donde hicieron click
                        val nombre = data[position].nombre
                        val cuenta = data[position].cuenta
                        val correo = data[position].correo
                        val image = data[position].imagen
                        //En position tengo el indice del elemento en la lista
                        val idAlum: Int = position
                        intento2.putExtra("mensaje","edit")
                        intento2.putExtra("nombre","${nombre}")
                        intento2.putExtra("cuenta","${cuenta}")
                        intento2.putExtra("correo","${correo}")
                        intento2.putExtra("image","${image}")
                        //Pasamos por extras el idAlum para poder saber cual editar de la lista (ArrayList)
                        intento2.putExtra("idA",idAlum)
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