package com.example.compras

import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun subtotalCalculoCorrecto() {
        val p = Producto(1, "Test", 10.0, 3)
        assertEquals(30.0, p.subtotal, 0.001)
    }
}
