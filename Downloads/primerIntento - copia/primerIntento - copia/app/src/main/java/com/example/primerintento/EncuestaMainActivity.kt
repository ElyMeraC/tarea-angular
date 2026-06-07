package com.example.primerintento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EncuestaMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_encuesta_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val radioGroup1 = findViewById<RadioGroup>(R.id.radioGroup1)
        val radioGroup2 = findViewById<RadioGroup>(R.id.radioGroup2)
        val radioGroup3 = findViewById<RadioGroup>(R.id.radioGroup3)
        val radioGroup4 = findViewById<RadioGroup>(R.id.radioGroup4)
        val radioGroup5 = findViewById<RadioGroup>(R.id.radioGroup5)

        findViewById<Button>(R.id.btnEncuesta).setOnClickListener {

            val seleccionadoId1 = radioGroup1.checkedRadioButtonId
            val seleccionadoId2 = radioGroup2.checkedRadioButtonId
            val seleccionadoId3 = radioGroup3.checkedRadioButtonId
            val seleccionadoId4 = radioGroup4.checkedRadioButtonId
            val seleccionadoId5 = radioGroup5.checkedRadioButtonId

            if (seleccionadoId1 == -1 || seleccionadoId2 == -1 || seleccionadoId3 == -1 || seleccionadoId4 == -1 ||  seleccionadoId5 == -1) {

                Toast.makeText(this, "Por favor, responda todas las preguntas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val puntos1 = when (seleccionadoId1) {
                R.id.rbBuena -> 3
                R.id.rbRegular -> 2
                R.id.rbMala -> 0
                else -> 0
            }

            val puntos2 = when (seleccionadoId2) {
                R.id.rb2Buena -> 3
                R.id.rb2Regular -> 2
                R.id.rb2Mala -> 0
                else -> 0
            }

            val puntos3 = when (seleccionadoId3) {
                R.id.rb3Buena -> 3
                R.id.rb3Regular -> 2
                R.id.rb3Mala -> 0
                else -> 0
            }

            val puntos4 = when (seleccionadoId4) {
                R.id.rb4Buena -> 3
                R.id.rb4Regular -> 2
                R.id.rb4Mala -> 0
                else -> 0
            }

            val puntos5 = when (seleccionadoId5) {
                R.id.rb5Buena -> 3
                R.id.rb5Regular -> 2
                R.id.rb5Mala -> 0
                else -> 0
            }

            val puntajeTotal = puntos1 + puntos2 + puntos3 + puntos4 + puntos5

            val intentResultados = Intent(this, EncuestaResultMainActivity::class.java)
            intentResultados.putExtra("PUNTAJE_FINAL", puntajeTotal)

            Toast.makeText(this, "Encuesta guardada con éxito \n Puntaje Total:$puntajeTotal / 15 puntos", Toast.LENGTH_SHORT).show()


            startActivity(intentResultados)
            finish()

        }

    }

    override fun onRestart() {
        super.onRestart()

        // Buscamos los grupos en el reinicio de la pantalla
        val radioGroup1 = findViewById<RadioGroup>(R.id.radioGroup1)
        val radioGroup2 = findViewById<RadioGroup>(R.id.radioGroup2)
        val radioGroup3 = findViewById<RadioGroup>(R.id.radioGroup3)
        val radioGroup4 = findViewById<RadioGroup>(R.id.radioGroup4)
        val radioGroup5 = findViewById<RadioGroup>(R.id.radioGroup5)

        // Reseteamos la selección a vacío
        radioGroup1.clearCheck()
        radioGroup2.clearCheck()
        radioGroup3.clearCheck()
        radioGroup4.clearCheck()
        radioGroup5.clearCheck()
    }
}