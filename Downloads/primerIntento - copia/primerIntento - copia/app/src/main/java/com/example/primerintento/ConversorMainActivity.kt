package com.example.primerintento

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge

import java.util.Locale
class ConversorMainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ttsListo = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conversor_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializar motor de Texto a Voz (TTS)
        tts = TextToSpeech(this, this)

        // Vinculación de Componentes de Longitud
        val txtLongitud = findViewById<EditText>(R.id.txtLongitud)
        val rgLongitud = findViewById<RadioGroup>(R.id.rgLongitud)
        val lblResLongitud = findViewById<TextView>(R.id.lblResLongitud)

        // Vinculación de Componentes de Temperatura
        val txtTemperatura = findViewById<EditText>(R.id.txtTemperatura)
        val rgTemperatura = findViewById<RadioGroup>(R.id.rgTemperatura)
        val lblResTemperatura = findViewById<TextView>(R.id.lblResTemperatura)

        // Vinculación de Componentes de Peso
        val txtPeso = findViewById<EditText>(R.id.txtPeso)
        val rgPeso = findViewById<RadioGroup>(R.id.rgPeso)
        val lblResPeso = findViewById<TextView>(R.id.lblResPeso)

        // Botones de Acción
        val btnCalcularConversor = findViewById<Button>(R.id.btnCalcularConversor)
        val btnVolverConversor = findViewById<Button>(R.id.btnVolverConversor)

        // ===================================================
        // 🔄 CAMBIO DINÁMICO DE HINTS AL SELECCIONAR UNIDAD
        // ===================================================
        rgLongitud.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCm -> txtLongitud.hint = "Ingrese valor en CM"
                R.id.rbMetro -> txtLongitud.hint = "Ingrese valor en Metro"
                R.id.rbKm -> txtLongitud.hint = "Ingrese valor en KM"
            }
        }

        rgTemperatura.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCelsius -> txtTemperatura.hint = "Ingrese valor en Celsius"
                R.id.rbFahrenheit -> txtTemperatura.hint = "Ingrese valor en Fahrenheit"
                R.id.rbKelvin -> txtTemperatura.hint = "Ingrese valor en Kelvin"
            }
        }

        rgPeso.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbGramo -> txtPeso.hint = "Ingrese valor en Gramo"
                R.id.rbLibra -> txtPeso.hint = "Ingrese valor en Libra"
                R.id.rbKg -> txtPeso.hint = "Ingrese valor en Kilogramo"
            }
        }


        btnCalcularConversor.setOnClickListener {
            var textoVozCompleto = ""
            var almenosUnoProcesado = false

            // --- 1. SECCIÓN LONGITUD ---
            val strLong = txtLongitud.text.toString().trim()
            if (strLong.isNotEmpty()) {
                val idSeleccionado = rgLongitud.checkedRadioButtonId
                if (idSeleccionado == -1) {
                    Toast.makeText(this, "Seleccione la unidad actual de Longitud", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val valor = strLong.toDoubleOrNull() ?: 0.0
                var res1 = 0.0; var uniRes1 = ""
                var res2 = 0.0; var uniRes2 = ""

                when (idSeleccionado) {
                    R.id.rbCm -> {
                        res1 = valor / 100.0; uniRes1 = "m"
                        res2 = valor / 100000.0; uniRes2 = "km"
                    }
                    R.id.rbMetro -> {
                        res1 = valor * 100.0; uniRes1 = "cm"
                        res2 = valor / 1000.0; uniRes2 = "km"
                    }
                    R.id.rbKm -> {
                        res1 = valor * 100000.0; uniRes1 = "cm"
                        res2 = valor * 1000.0; uniRes2 = "m"
                    }
                }
                // Visualización optimizada y compacta en interfaz
                lblResLongitud.text = String.format("Long: %.2f %s y %.4f %s", res1, uniRes1, res2, uniRes2)

                // Texto extendido y natural para la lectura por voz
                val audioUni1 = if (uniRes1 == "m") "Metros" else if (uniRes1 == "cm") "Centímetros" else "Kilómetros"
                val audioUni2 = if (uniRes2 == "km") "Kilómetros" else if (uniRes2 == "m") "Metros" else "Centímetros"
                textoVozCompleto += "En longitud, equivale a %.2f %s y %s %s. ".format(res1, audioUni1, res2, audioUni2)
                almenosUnoProcesado = true
            }

            // --- 2. SECCIÓN TEMPERATURA ---
            val strTemp = txtTemperatura.text.toString().trim()
            if (strTemp.isNotEmpty()) {
                val idSeleccionado = rgTemperatura.checkedRadioButtonId
                if (idSeleccionado == -1) {
                    Toast.makeText(this, "Seleccione la unidad actual de Temperatura", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val valor = strTemp.toDoubleOrNull() ?: 0.0
                var res1 = 0.0; var uniRes1 = ""
                var res2 = 0.0; var uniRes2 = ""

                when (idSeleccionado) {
                    R.id.rbCelsius -> {
                        res1 = (valor * 9 / 5) + 32; uniRes1 = "°F"
                        res2 = valor + 273.15; uniRes2 = "K"
                    }
                    R.id.rbFahrenheit -> {
                        res1 = (valor - 32) * 5 / 9; uniRes1 = "°C"
                        res2 = (valor - 32) * 5 / 9 + 273.15; uniRes2 = "K"
                    }
                    R.id.rbKelvin -> {
                        res1 = valor - 273.15; uniRes1 = "°C"
                        res2 = (valor - 273.15) * 9 / 5 + 32; uniRes2 = "°F"
                    }
                }
                // Visualización optimizada y compacta en interfaz
                lblResTemperatura.text = String.format("Temp: %.2f %s y %.2f %s", res1, uniRes1, res2, uniRes2)

                // Texto extendido y natural para la lectura por voz
                val audioUni1 = if (uniRes1 == "°F") "Grados Fahrenheit" else "Grados Celsius"
                val audioUni2 = if (uniRes2 == "K") "Grados Kelvin" else "Grados Fahrenheit"
                textoVozCompleto += "En temperatura, equivale a %.2f %s y %.2f %s. ".format(res1, audioUni1, res2, audioUni2)
                almenosUnoProcesado = true
            }

            // --- 3. SECCIÓN PESO ---
            val strPeso = txtPeso.text.toString().trim()
            if (strPeso.isNotEmpty()) {
                val idSeleccionado = rgPeso.checkedRadioButtonId
                if (idSeleccionado == -1) {
                    Toast.makeText(this, "Seleccione la unidad actual de Peso", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val valor = strPeso.toDoubleOrNull() ?: 0.0
                var res1 = 0.0; var uniRes1 = ""
                var res2 = 0.0; var uniRes2 = ""

                when (idSeleccionado) {
                    R.id.rbGramo -> {
                        res1 = valor * 0.00220462; uniRes1 = "lb"
                        res2 = valor / 1000.0; uniRes2 = "kg"
                    }
                    R.id.rbLibra -> {
                        res1 = valor * 453.592; uniRes1 = "g"
                        res2 = valor / 2.20462; uniRes2 = "kg"
                    }
                    R.id.rbKg -> {
                        res1 = valor * 1000.0; uniRes1 = "g"
                        res2 = valor * 2.20462; uniRes2 = "lb"
                    }
                }
                // Visualización optimizada y compacta en interfaz
                lblResPeso.text = String.format("Peso: %.2f %s y %.2f %s", res1, uniRes1, res2, uniRes2)

                // Texto extendido y natural para la lectura por voz
                val audioUni1 = if (uniRes1 == "lb") "Libras" else if (uniRes1 == "g") "Gramos" else "Kilogramos"
                val audioUni2 = if (uniRes2 == "kg") "Kilogramos" else if (uniRes2 == "lb") "Libras" else "Gramos"
                textoVozCompleto += "En peso, equivale a %.2f %s y %.2f %s. ".format(res1, audioUni1, res2, audioUni2)
                almenosUnoProcesado = true
            }

            // Validación global de campos vacíos
            if (!almenosUnoProcesado) {
                Toast.makeText(this, "Por favor, ingrese un valor en algún campo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Conversiones calculadas", Toast.LENGTH_SHORT).show()

            // Ejecución del audio unificado
            if (ttsListo && textoVozCompleto.isNotEmpty()) {
                tts?.speak(textoVozCompleto, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }

        // Navegación de retorno al menú
        btnVolverConversor.setOnClickListener {
            val intentMenu = Intent(this, MenuMainActivity::class.java)
            intentMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intentMenu)
            finish()
        }
    }


    // CONFIGURACIÓN DEL MOTOR DE VOZ (TTS)
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val resultado = tts?.setLanguage(Locale("es", "EC")) // Localización óptima para Ecuador
            if (resultado != TextToSpeech.LANG_MISSING_DATA && resultado != TextToSpeech.LANG_NOT_SUPPORTED) {
                ttsListo = true
            }
        }
    }


    // LIMPIEZA AUTOMÁTICA AL VOLVER A LA ACTIVITY
    override fun onRestart() {
        super.onRestart()
        findViewById<EditText>(R.id.txtLongitud).text.clear()
        findViewById<RadioGroup>(R.id.rgLongitud).clearCheck()
        findViewById<TextView>(R.id.lblResLongitud).text = "Long: ---"
        findViewById<EditText>(R.id.txtLongitud).hint = "Ejemplo conversión longitud (Metros)"

        findViewById<EditText>(R.id.txtTemperatura).text.clear()
        findViewById<RadioGroup>(R.id.rgTemperatura).clearCheck()
        findViewById<TextView>(R.id.lblResTemperatura).text = "Temp: ---"
        findViewById<EditText>(R.id.txtTemperatura).hint = "Ejemplo conversión temperatura (Celsius)"

        findViewById<EditText>(R.id.txtPeso).text.clear()
        findViewById<RadioGroup>(R.id.rgPeso).clearCheck()
        findViewById<TextView>(R.id.lblResPeso).text = "Peso: ---"
        findViewById<EditText>(R.id.txtPeso).hint = "Ejemplo conversión peso (Kilogramos)"
    }

    // Liberar recursos de memoria del parlante
    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}