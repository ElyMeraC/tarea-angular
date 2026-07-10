package com.example.compras

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ConfirmacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion)

        val productos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            @Suppress("UNCHECKED_CAST")
            intent.getSerializableExtra("PRODUCTOS_CARRITO", ArrayList::class.java) as ArrayList<Producto>
        } else {
            @Suppress("UNCHECKED_CAST", "DEPRECATION")
            intent.getSerializableExtra("PRODUCTOS_CARRITO") as ArrayList<Producto>
        }
        val total     = intent.getDoubleExtra("TOTAL", 0.0)
        val nombre    = intent.getStringExtra("NOMBRE")    ?: ""
        val apellido  = intent.getStringExtra("APELLIDO")  ?: ""
        val email     = intent.getStringExtra("EMAIL")     ?: ""
        val telefono  = intent.getStringExtra("TELEFONO")  ?: ""
        val direccion = intent.getStringExtra("DIRECCION") ?: ""
        val ciudad    = intent.getStringExtra("CIUDAD")    ?: ""
        val cp        = intent.getStringExtra("CODIGO_POSTAL") ?: ""

        val tvProductos   = findViewById<TextView>(R.id.tvProductos)
        val tvDatosEnvio  = findViewById<TextView>(R.id.tvDatosEnvio)
        val tvTotal       = findViewById<TextView>(R.id.tvTotal)
        val btnVolver     = findViewById<Button>(R.id.btnVolver)
        val btnFinalizar  = findViewById<Button>(R.id.btnFinalizar)

        val sbProductos = StringBuilder()
        productos.forEach { p ->
            sbProductos.appendLine("• ${p.nombre}  x${p.cantidad}   →   S/ %.2f".format(p.subtotal))
        }
        tvProductos.text = sbProductos.toString().trimEnd()

        tvDatosEnvio.text = buildString {
            appendLine("$nombre $apellido")
            appendLine(email)
            appendLine(telefono)
            appendLine(direccion)
            append(ciudad)
            if (cp.isNotEmpty()) append(" - C.P. $cp")
        }

        tvTotal.text = "TOTAL A PAGAR:  S/ %.2f".format(total)

        btnVolver.setOnClickListener { finish() }

        btnFinalizar.setOnClickListener {
            Toast.makeText(this, "¡Pedido confirmado! Gracias por tu compra.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProductosActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
