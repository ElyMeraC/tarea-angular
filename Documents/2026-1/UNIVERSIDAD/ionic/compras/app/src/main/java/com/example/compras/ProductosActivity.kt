package com.example.compras

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProductosActivity : AppCompatActivity() {

    private val catalogo = listOf(
        Producto(1, "Laptop Pro 15\"",    999.99),
        Producto(2, "Mouse Inalambrico",   25.00),
        Producto(3, "Teclado Mecanico",    45.00),
        Producto(4, "Monitor 24\"",       299.99),
        Producto(5, "Auriculares BT",      79.99),
        Producto(6, "Webcam HD",           89.99)
    )

    private val checkBoxes = mutableListOf<CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val llProductos  = findViewById<LinearLayout>(R.id.llProductos)
        val btnVerCarrito = findViewById<Button>(R.id.btnVerCarrito)

        catalogo.forEach { producto ->
            val tarjeta = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also { it.setMargins(0, 0, 0, 8) }
                setPadding(16, 20, 16, 20)
                setBackgroundColor(Color.WHITE)
            }

            val cb = CheckBox(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                text = producto.nombre
                textSize = 15f
                setOnCheckedChangeListener { _, _ -> actualizarBoton(btnVerCarrito) }
            }

            val tvPrecio = TextView(this).apply {
                text = "S/ %.2f".format(producto.precio)
                textSize = 15f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.parseColor("#1565C0"))
            }

            tarjeta.addView(cb)
            tarjeta.addView(tvPrecio)
            llProductos.addView(tarjeta)
            checkBoxes.add(cb)
        }

        btnVerCarrito.setOnClickListener {
            val seleccionados = ArrayList<Producto>()
            checkBoxes.forEachIndexed { i, cb ->
                if (cb.isChecked) seleccionados.add(catalogo[i].copy())
            }
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putExtra("PRODUCTOS_SELECCIONADOS", seleccionados)
            startActivity(intent)
        }
    }

    private fun actualizarBoton(btn: Button) {
        btn.isEnabled = checkBoxes.any { it.isChecked }
    }
}
