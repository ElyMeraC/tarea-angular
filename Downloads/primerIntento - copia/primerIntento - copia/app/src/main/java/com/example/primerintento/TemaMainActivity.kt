package com.example.primerintento

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TemaMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tema_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // 1. Enlazamos los componentes usando tus IDs exactos por defecto
        val contenedorPrincipal = findViewById<ConstraintLayout>(R.id.main)
        val radioGroupTemas = findViewById<RadioGroup>(R.id.radioGroupTemas)
        val btnAplicarTema = findViewById<Button>(R.id.btnAplicarTema)
        val btnVolverTema = findViewById<Button>(R.id.btnVolverTema)

        val lblTituloTema = findViewById<TextView>(R.id.lblTituloTema)
        val lblInstruccionTema = findViewById<TextView>(R.id.lblInstruccionTema)

        val rbTemaClaro = findViewById<RadioButton>(R.id.rbTemaClaro)
        val rbTemaOscuro = findViewById<RadioButton>(R.id.rbTemaOscuro)
        val rbTemaSistema = findViewById<RadioButton>(R.id.rbTemaSistema)

        // Cambiamos visualmente los textos por código para que coincidan con los colores que quieres probar
        rbTemaClaro.text = "Fondo Blanco"
        rbTemaOscuro.text = "Fondo Azul (Por Defecto)"
        rbTemaSistema.text = "Fondo Plomo / Gris"

        // 2. Lógica para aplicar los colores HEX personalizados
        btnAplicarTema.setOnClickListener {
            val seleccionadoId = radioGroupTemas.checkedRadioButtonId

            if (seleccionadoId == -1) {
                Toast.makeText(this, "Por favor, elija una opción de apariencia", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (seleccionadoId) {
                R.id.rbTemaClaro -> {
                    // ---> FONDO BLANCO PURÍSIMO
                    contenedorPrincipal.setBackgroundColor(Color.parseColor("#FFFFFF"))

                    // Colores de texto en Azul Oscuro (#01022e) para que resalten perfectamente en el fondo blanco
                    val colorOscuroTexto = Color.parseColor("#01022e")
                    lblTituloTema.setTextColor(colorOscuroTexto)
                    lblInstruccionTema.setTextColor(colorOscuroTexto)
                    rbTemaClaro.setTextColor(colorOscuroTexto)
                    rbTemaOscuro.setTextColor(colorOscuroTexto)
                    rbTemaSistema.setTextColor(colorOscuroTexto)
                    rbTemaClaro.buttonTintList = ColorStateList.valueOf(Color.BLACK)
                    rbTemaOscuro.buttonTintList = ColorStateList.valueOf(Color.BLACK)
                    rbTemaSistema.buttonTintList = ColorStateList.valueOf(Color.BLACK)

                    // Cambiamos el estilo del botón outlined para que se lea oscuro sobre el fondo blanco
                    btnVolverTema.setTextColor(colorOscuroTexto)

                    Toast.makeText(this, "Apariencia de Fondo Blanco Aplicada", Toast.LENGTH_SHORT).show()
                }

                R.id.rbTemaOscuro -> {
                    // ---> FONDO AZUL CORPORATIVO ORIGINAL (#01022e)
                    contenedorPrincipal.setBackgroundColor(Color.parseColor("#01022e"))

                    // Regresamos todos los textos a Blanco Puro
                    lblTituloTema.setTextColor(Color.WHITE)
                    lblInstruccionTema.setTextColor(Color.WHITE)
                    rbTemaClaro.setTextColor(Color.WHITE)
                    rbTemaOscuro.setTextColor(Color.WHITE)
                    rbTemaSistema.setTextColor(Color.WHITE)
                    btnVolverTema.setTextColor(Color.WHITE)
                    rbTemaClaro.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                    rbTemaOscuro.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                    rbTemaSistema.buttonTintList = ColorStateList.valueOf(Color.WHITE)

                    Toast.makeText(this, "Fondo Azul Restaurado", Toast.LENGTH_SHORT).show()
                }

                R.id.rbTemaSistema -> {
                    // ---> FONDO PLOMO / GRIS OSCURO (#515A5A)
                    contenedorPrincipal.setBackgroundColor(Color.parseColor("#515A5A"))

                    // Los textos en Blanco resaltan increíble sobre este plomo
                    lblTituloTema.setTextColor(Color.WHITE)
                    lblInstruccionTema.setTextColor(Color.WHITE)
                    rbTemaClaro.setTextColor(Color.WHITE)
                    rbTemaOscuro.setTextColor(Color.WHITE)
                    rbTemaSistema.setTextColor(Color.WHITE)
                    btnVolverTema.setTextColor(Color.WHITE)
                    rbTemaClaro.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                    rbTemaOscuro.buttonTintList = ColorStateList.valueOf(Color.WHITE)
                    rbTemaSistema.buttonTintList = ColorStateList.valueOf(Color.WHITE)

                    Toast.makeText(this, "Apariencia Plomo / Gris Aplicada", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 3. Botón ordenado para volver al menú de la app
        btnVolverTema.setOnClickListener {
            val intentMenu = Intent(this, MenuMainActivity::class.java)
            intentMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intentMenu)
            finish()
        }



    }
}