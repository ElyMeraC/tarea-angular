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

class QuizMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val rgQuiz1 = findViewById<RadioGroup>(R.id.rgQuiz1)
        val rgQuiz2 = findViewById<RadioGroup>(R.id.rgQuiz2)
        val rgQuiz3 = findViewById<RadioGroup>(R.id.rgQuiz3)
        val btnCalificarQuiz = findViewById<Button>(R.id.btnCalificarQuiz)
        val btnVolverQuiz = findViewById<Button>(R.id.btnVolverQuiz)

        btnCalificarQuiz.setOnClickListener {
            val id1 = rgQuiz1.checkedRadioButtonId
            val id2 = rgQuiz2.checkedRadioButtonId
            val id3 = rgQuiz3.checkedRadioButtonId

            // 1. Validamos que ninguna pregunta quede en blanco
            if (id1 == -1 || id2 == -1 || id3 == -1) {
                Toast.makeText(this, "Por favor, responda todas las preguntas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Evaluamos la Pregunta 1: Correcta es Kotlin
            val puntos1 = if (id1 == R.id.rbQ1Kotlin) 5 else 0

            // 3. Evaluamos la Pregunta 2: Correcta es Layout
            val puntos2 = if (id2 == R.id.rbQ2Layout) 5 else 0

            // 4. Evaluamos la Pregunta 3: Correcta es Intent
            val puntos3 = if (id3 == R.id.rbQ3Intent) 5 else 0

            // 5. Calculamos el total
            val puntajeFinal = puntos1 + puntos2 + puntos3

            // Mostramos el resultado al usuario
            Toast.makeText(this, "¡Cuestionario Evaluado!\nTu puntaje es: $puntajeFinal / 15 puntos", Toast.LENGTH_LONG).show()
            val intentResult = Intent(this, QuizResultMainActivity::class.java)
            intentResult.putExtra("QUIZ_PUNTAJE", puntajeFinal)
            startActivity(intentResult)
        }

        // Regresar al Menú principal de forma limpia
        btnVolverQuiz.setOnClickListener {
            val intentMenu = Intent(this, MenuMainActivity::class.java)
            intentMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intentMenu)
            finish()
        }
    }

    // Resetea las selecciones de los RadioButtons si se sale y se vuelve a entrar
    override fun onRestart() {
        super.onRestart()
        findViewById<RadioGroup>(R.id.rgQuiz1).clearCheck()
        findViewById<RadioGroup>(R.id.rgQuiz2).clearCheck()
        findViewById<RadioGroup>(R.id.rgQuiz3).clearCheck()
    }
}