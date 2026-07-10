package com.example.compras

import java.io.Serializable

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    var cantidad: Int = 1
) : Serializable {
    val subtotal: Double get() = precio * cantidad
}
