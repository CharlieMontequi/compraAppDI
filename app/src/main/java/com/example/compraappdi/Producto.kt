package com.example.compraappdi

data class Producto(
    val nombre: String,
    val cantidad: String,
    val lugarCompra: String,
    val urgente: Boolean
) {
    override fun toString(): String {
        return "Nombre: $nombre, Cantidad: $cantidad, Lugar de Compra: $lugarCompra, Urgente: ${if (urgente) "Sí" else "No"}"
    }
}