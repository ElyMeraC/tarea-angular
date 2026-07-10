package com.example.compras

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DatosEnvioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_envio)

        val productos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            @Suppress("UNCHECKED_CAST")
            intent.getSerializableExtra("PRODUCTOS_CARRITO", ArrayList::class.java) as ArrayList<Producto>
        } else {
            @Suppress("UNCHECKED_CAST", "DEPRECATION")
            intent.getSerializableExtra("PRODUCTOS_CARRITO") as ArrayList<Producto>
        }
        val total     = intent.getDoubleExtra("TOTAL", 0.0)

        val tvResumen      = findViewById<TextView>(R.id.tvResumenPedido)
        val etNombre       = findViewById<EditText>(R.id.etNombre)
        val etApellido     = findViewById<EditText>(R.id.etApellido)
        val etEmail        = findViewById<EditText>(R.id.etEmail)
        val etTelefono     = findViewById<EditText>(R.id.etTelefono)
        val etDireccion    = findViewById<EditText>(R.id.etDireccion)
        val etCiudad       = findViewById<EditText>(R.id.etCiudad)
        val etCodigoPostal = findViewById<EditText>(R.id.etCodigoPostal)
        val btnVolver      = findViewById<Button>(R.id.btnVolver)
        val btnConfirmar   = findViewById<Button>(R.id.btnConfirmar)

        tvResumen.text = "${productos.size} producto(s)  •  Total: S/ %.2f".format(total)

        btnVolver.setOnClickListener { finish() }

        btnConfirmar.setOnClickListener {
            val nombre    = etNombre.text.toString().trim()
            val apellido  = etApellido.text.toString().trim()
            val email     = etEmail.text.toString().trim()
            val telefono  = etTelefono.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val ciudad    = etCiudad.text.toString().trim()
            val cp        = etCodigoPostal.text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() ||
                telefono.isEmpty() || direccion.isEmpty() || ciudad.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos obligatorios (*)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ConfirmacionActivity::class.java)
            intent.putExtra("PRODUCTOS_CARRITO", productos)
            intent.putExtra("TOTAL",         total)
            intent.putExtra("NOMBRE",        nombre)
            intent.putExtra("APELLIDO",      apellido)
            intent.putExtra("EMAIL",         email)
            intent.putExtra("TELEFONO",      telefono)
            intent.putExtra("DIRECCION",     direccion)
            intent.putExtra("CIUDAD",        ciudad)
            intent.putExtra("CODIGO_POSTAL", cp)
            startActivity(intent)
        }
    }
}
