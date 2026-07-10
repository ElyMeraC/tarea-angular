package com.example.compras

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CarritoActivity : AppCompatActivity() {

    private lateinit var productos: ArrayList<Producto>
    private lateinit var tvTotal: TextView
    private lateinit var llCarrito: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        productos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            @Suppress("UNCHECKED_CAST")
            intent.getSerializableExtra("PRODUCTOS_SELECCIONADOS", ArrayList::class.java) as ArrayList<Producto>
        } else {
            @Suppress("UNCHECKED_CAST", "DEPRECATION")
            intent.getSerializableExtra("PRODUCTOS_SELECCIONADOS") as ArrayList<Producto>
        }

        tvTotal   = findViewById(R.id.tvTotal)
        llCarrito = findViewById(R.id.llCarrito)

        val btnVolver    = findViewById<Button>(R.id.btnVolver)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)

        renderizarCarrito()

        btnVolver.setOnClickListener { finish() }

        btnContinuar.setOnClickListener {
            val total  = productos.sumOf { it.subtotal }
            val intent = Intent(this, DatosEnvioActivity::class.java)
            intent.putExtra("PRODUCTOS_CARRITO", productos)
            intent.putExtra("TOTAL", total)
            startActivity(intent)
        }
    }

    private fun renderizarCarrito() {
        llCarrito.removeAllViews()

        productos.forEach { producto ->
            val tarjeta = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also { it.setMargins(0, 0, 0, 8) }
                setPadding(16, 14, 16, 14)
                setBackgroundColor(Color.WHITE)
            }

            val tvNombre = TextView(this).apply {
                text = producto.nombre
                textSize = 16f
                setTextColor(Color.parseColor("#212121"))
                setTypeface(null, Typeface.BOLD)
            }

            val fila = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also { it.topMargin = 8 }
                gravity = Gravity.CENTER_VERTICAL
            }

            val tvCantidad = TextView(this).apply {
                text = producto.cantidad.toString()
                textSize = 16f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            val tvSubtotal = TextView(this).apply {
                text = "S/ %.2f".format(producto.subtotal)
                textSize = 15f
                gravity = Gravity.END
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                setTextColor(Color.parseColor("#1565C0"))
                setTypeface(null, Typeface.BOLD)
            }

            val btnMenos = Button(this).apply {
                text = "−"
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(90, 90)
                setBackgroundColor(Color.parseColor("#E3F2FD"))
                setTextColor(Color.parseColor("#1565C0"))
            }

            val btnMas = Button(this).apply {
                text = "+"
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(90, 90)
                setBackgroundColor(Color.parseColor("#1565C0"))
                setTextColor(Color.WHITE)
            }

            btnMenos.setOnClickListener {
                if (producto.cantidad > 1) {
                    producto.cantidad--
                    tvCantidad.text = producto.cantidad.toString()
                    tvSubtotal.text = "S/ %.2f".format(producto.subtotal)
                    actualizarTotal()
                }
            }

            btnMas.setOnClickListener {
                if (producto.cantidad < 10) {
                    producto.cantidad++
                    tvCantidad.text = producto.cantidad.toString()
                    tvSubtotal.text = "S/ %.2f".format(producto.subtotal)
                    actualizarTotal()
                }
            }

            fila.addView(btnMenos)
            fila.addView(tvCantidad)
            fila.addView(btnMas)
            fila.addView(tvSubtotal)

            tarjeta.addView(tvNombre)
            tarjeta.addView(fila)
            llCarrito.addView(tarjeta)
        }

        actualizarTotal()
    }

    private fun actualizarTotal() {
        val total = productos.sumOf { it.subtotal }
        tvTotal.text = "Total:  S/ %.2f".format(total)
    }
}
