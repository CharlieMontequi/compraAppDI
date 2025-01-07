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
import android.widget.ListView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val listaProducto = mutableListOf<Producto>()

    private lateinit var adaptador: AdaptadorPersonalizado
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listado = findViewById<ListView>(R.id.listaCompra)
        adaptador = AdaptadorPersonalizado(this, R.layout.elementos_lista_compra, listaProducto)
        listado.adapter = adaptador


        val botonAniadirCompra = findViewById<Button>(R.id.b_aniadie)
        botonAniadirCompra.setOnClickListener {
            showDialogoCompra()
        }
        val botonImprimir = findViewById<Button>(R.id._imprimir)
        botonImprimir.setOnClickListener { showDialogImprimir() }

        val botonBorrarSeleccion = findViewById<Button>(R.id.b_borrar)
        botonBorrarSeleccion.setOnClickListener {
            borrarProductoSelecciondo()
        }
        val botonLimpiar = findViewById<Button>(R.id.b_limpiar)
        botonLimpiar.setOnClickListener {
            listaProducto.removeAll(listaProducto)
            adaptador.notifyDataSetChanged()
        }

    }
    private fun borrarProductoSelecciondo(){

        val productosParaBorrar = mutableListOf<Producto>()
        val listado = findViewById<ListView>(R.id.listaCompra)
        for (i in 0 until listado.childCount){
            val rowview = listado.getChildAt(i)
            val checkboxMarcado = rowview.findViewById<CheckBox>(R.id.ChechBoxBorrar)
            if(checkboxMarcado.isChecked){
                productosParaBorrar.add(listaProducto[i])
            }
        }
        for(i in 0 until listado.childCount){
            val rowview = listado.getChildAt(i)
            val checkboxMarcado = rowview.findViewById<CheckBox>(R.id.ChechBoxBorrar)
            if (checkboxMarcado.isChecked){
                checkboxMarcado.isChecked=false

            }
        }

        listaProducto.removeAll(productosParaBorrar)
        adaptador.notifyDataSetChanged()
    }

    private fun showDialogoCompra() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.emergente_add, null)
        val nombrePoducto = dialogView.findViewById<EditText>(R.id.editTextNombreProducto)
        val cantidad = dialogView.findViewById<EditText>(R.id.editTextNumber)
        val lugarCompra = dialogView.findViewById<Spinner>(R.id.spinner)
        val urgente = dialogView.findViewById<Switch>(R.id.switch1)
        val arraySitios = resources.getStringArray(R.array.lugaresCompra)
        val adaptadorSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySitios)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lugarCompra.adapter = adaptadorSpinner

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setCancelable(false) // evitar que se cierre al pinchar fuera
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Guardar") { _, _ ->
                val producto = Producto(
                    nombrePoducto.text.toString(),
                    cantidad.text.toString(),
                    lugarCompra.selectedItem.toString(),
                    urgente.isChecked
                )
                listaProducto.add(producto)
                adaptador.notifyDataSetChanged()


            }

        builder.create().show()
    }

    private fun showDialogImprimir() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.imprimir_compra, null)
        val tvImprimir = dialogView.findViewById<TextView>(R.id.textViewListaImpresa)
        var posicion = 0
        val builder = AlertDialog.Builder(this)


        if (listaProducto.isNotEmpty()) {

            val texto = StringBuilder()
            for (productoEspecifico in listaProducto) {
                texto.append(
                    "${productoEspecifico.nombre} ${productoEspecifico.cantidad} ${productoEspecifico.lugarCompra} ${
                        if (productoEspecifico.urgente) "Urge" else "No urge"
                    }\n"
                )
            }
            // Asigna el texto generado al TextView
            tvImprimir.text = texto.toString()
        }

        builder.setView(dialogView)
            .setCancelable(false)
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Guardar") { _, _ ->

                Toast.makeText(
                    applicationContext,
                    "Se ha guardado la lista correctamente",
                    Toast.LENGTH_SHORT
                ).show()

            }
        builder.create().show()

    }

    // adaptador personalizado
    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        private val objets: MutableList<Producto>
    ) : ArrayAdapter<Producto>(context, resource, objets) {

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
            val productoEspecifico = listaProducto[position]
            rowView.findViewById<CheckBox>(R.id.ChechBoxBorrar).text =
                "${productoEspecifico.cantidad} ${productoEspecifico.nombre} ${productoEspecifico.lugarCompra} ${if (productoEspecifico.urgente) "Urge" else "No urge"}\n"

            return rowView
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            // Este método se llama para mostrar una vista personalizada en el elemento seleccionado

            // Llama a la función para crear la fila personalizada y la devuelve
            return crearFilaPersonalizada(position, convertView, parent)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val checkBox = findViewById<CheckBox>(R.id.ChechBoxBorrar)
        checkBox.isChecked

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}