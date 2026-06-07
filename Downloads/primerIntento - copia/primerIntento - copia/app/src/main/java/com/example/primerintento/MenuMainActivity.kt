package com.example.primerintento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreRecibido = intent.getStringExtra("USER_NAME") ?: "USUARIO"

        val lblBienvenida = findViewById<TextView>(R.id.lblBienvenida)
        lblBienvenida.text= "Bienvenido $nombreRecibido".uppercase()

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val btnIngresarMenu = findViewById<Button>(R.id.btnIngresarMenu)

        btnIngresarMenu.setOnClickListener {
            val seleccionadoId = radioGroup.checkedRadioButtonId

            if (seleccionadoId == -1) {
                Toast.makeText(this, "Debe seleccionar una opción del menú", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // EVALUAMOS CON TUS NUEVOS IDs DE LOS RADIO BUTTONS (rb...)
            val intentDestino = when (seleccionadoId) {
                R.id.rbencuesta -> Intent(this, EncuestaMainActivity::class.java)
                R.id.rbselectema -> Intent(this, TemaMainActivity::class.java)
                R.id.rbquiz -> Intent(this, QuizMainActivity::class.java)
                R.id.rbcalculador -> Intent(this, PropinaMainActivity::class.java)
                R.id.rbconversor -> Intent(this, ConversorMainActivity::class.java)
                else -> null
            }



            if (intentDestino != null) {
                intentDestino.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intentDestino)
            }
        }


    }
}