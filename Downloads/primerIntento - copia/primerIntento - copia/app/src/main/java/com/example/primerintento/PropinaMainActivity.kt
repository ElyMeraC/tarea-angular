package com.example.primerintento

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.graphics.Color

import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import java.util.Locale
class PropinaMainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ttsListo = false
    override fun onCreate(savedInstanceState: Bundle?) {


            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_propina_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            // Inicializamos el motor de voz TextToSpeech para esta pantalla
            tts = TextToSpeech(this, this)

            val txtMonto = findViewById<EditText>(R.id.txtMonto)
            val rgPorcentajes = findViewById<RadioGroup>(R.id.rgPorcentajes)
            val lblResultadoPropina = findViewById<TextView>(R.id.lblResultadoPropina)
            val btnCalcularPropina = findViewById<Button>(R.id.btnCalcularPropina)
            val btnVolverPropina = findViewById<Button>(R.id.btnVolverPropina)

            btnCalcularPropina.setOnClickListener {
                val montoTexto = txtMonto.text.toString().trim()
                val seleccionadoId = rgPorcentajes.checkedRadioButtonId

                if (montoTexto.isEmpty()) {
                    Toast.makeText(this, "Por favor, ingrese el monto de la factura", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (seleccionadoId == -1) {
                    Toast.makeText(this, "Por favor, seleccione un porcentaje", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val facturaMonto = montoTexto.toDoubleOrNull() ?: 0.0

                val porcentaje = when (seleccionadoId) {
                    R.id.rbPropina10 -> 0.10
                    R.id.rbPropina15 -> 0.15
                    R.id.rbPropina20 -> 0.20
                    else -> 0.0
                }

                val propinaCalculada = facturaMonto * porcentaje
                val totalNeto = facturaMonto + propinaCalculada

                // 1. Mostrar los valores en el TextView de la pantalla
                lblResultadoPropina.text = String.format("Propina: $%.2f\nTotal Neto: $%.2f", propinaCalculada, totalNeto)

                // 2. Mostrar el Toast flotante con el valor específico de la propina
                val mensajeToast = String.format("Propina calculada: $%.2f", propinaCalculada)
                Toast.makeText(this, mensajeToast, Toast.LENGTH_LONG).show()

                // 3. Hablar en voz alta la cantidad de propina a dar
                if (ttsListo) {
                    // Todo el texto va dentro de las mismas comillas, usando dos comodines %.2f
                    val mensajeVoz = String.format(
                        "La cantidad de propina a dar es de %.2f dólares, y el valor neto a pagar sería %.2f dólares",
                        propinaCalculada,
                        totalNeto
                    )
                    tts?.speak(mensajeVoz, TextToSpeech.QUEUE_FLUSH, null, "")
                }
            }

            btnVolverPropina.setOnClickListener {
                val intentMenu = Intent(this, MenuMainActivity::class.java)
                intentMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intentMenu)
                finish()
            }
    }

            // Configuración obligatoria del motor de voz en español de Ecuador
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    val resultado = tts?.setLanguage(Locale("es", "EC"))
                    if (resultado != TextToSpeech.LANG_MISSING_DATA && resultado != TextToSpeech.LANG_NOT_SUPPORTED) {
                        ttsListo = true
                    }
                }
            }

            // Limpieza de campos al reabrir la pantalla
            override fun onRestart() {
                super.onRestart()
                findViewById<EditText>(R.id.txtMonto).text.clear()
                findViewById<RadioGroup>(R.id.rgPorcentajes).clearCheck()
                findViewById<TextView>(R.id.lblResultadoPropina).text = "Propina: $0.00\nTotal Neto: $0.00 "
            }

            // Liberar memoria del sintetizador al salir
            override fun onDestroy() {
                tts?.stop()
                tts?.shutdown()
                super.onDestroy()
            }
}