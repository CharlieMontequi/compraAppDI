package com.example.compraappdi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val listaProducto =mutableListOf<Producto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonAniadirCompra = findViewById<Button>(R.id.b_aniadie)
        botonAniadirCompra.setOnClickListener { showDialogoCompra() }


    }
    private fun showDialogoCompra(){

        val dialogView = LayoutInflater.from(this). inflate(R.layout.emergente_add,null)
        val nombrePoducto = dialogView.findViewById<EditText>(R.id.editTextNombreProducto)
        val cantidad = dialogView.findViewById<EditText>(R.id.editTextNumber)
        val lugarCompra = dialogView.findViewById<Spinner>(R.id.spinner)
        val urgente = dialogView.findViewById<Switch>(R.id.switch1)
        val arraySitios = resources.getStringArray(R.array.lugaresCompra)
        val adaptadorSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySitios)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lugarCompra.adapter= adaptadorSpinner

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setCancelable(false) // evitar que se cierre al pinchar fuera
            .setNegativeButton("Cancelar"){dialog, _ -> dialog.dismiss()
            }
            .setPositiveButton("Guardar"){_, _->
                val producto = Producto(nombrePoducto.text.toString(), cantidad.text.toString(), lugarCompra.selectedItem.toString(), urgente.isChecked )
                listaProducto.add(producto)
            }

        builder.create().show()
    }

// adaptador personalizado
    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objets: Array<String>
    ) : ArrayAdapter<String>(context, resource, objets){

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

            return crearFilaPersonalizada(position, convertView, parent)
        }
        private fun crearFilaPersonalizada(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            val layoutInflater = LayoutInflater.from(context)
            val rowView = convertView ?: layoutInflater.inflate(
                R.layout.elementos_lista_compra,
                parent,
                false
            )
            rowView.findViewById<CheckBox>(R.id.ChechBoxBorrar)
            rowView.findViewById<TextView>(R.id.TVNombre)

            return rowView
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}