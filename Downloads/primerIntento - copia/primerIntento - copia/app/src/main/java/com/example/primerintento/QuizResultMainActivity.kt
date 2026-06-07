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
import android.speech.tts.TextToSpeech
import android.widget.TextView
import java.util.Locale


class QuizResultMainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var puntajeRecibido: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_result_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Atrapamos el puntaje enviado desde el QuizMainActivity
        puntajeRecibido = intent.getIntExtra("QUIZ_PUNTAJE", 0)

        val lblRespuesta = findViewById<TextView>(R.id.lblRespuesta)
        val mensaje = "¡Quiz Finalizado!\n\n\nTu puntuación es: $puntajeRecibido de 15 puntos"

        lblRespuesta.text = mensaje
        Toast.makeText(this, "Evaluación procesada con éxito", Toast.LENGTH_SHORT).show()

        // Inicializamos el sintetizador de voz (TTS)
        tts = TextToSpeech(this, this)

        val btnSalirQuiz = findViewById<Button>(R.id.btnSalirQuiz)
        btnSalirQuiz.setOnClickListener {
            val intentMenu = Intent(this, MenuMainActivity::class.java)
            intentMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intentMenu)
            finish()
        }
    }

    // El motor de voz se ejecuta de inmediato al terminar de inicializarse
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val resultado = tts?.setLanguage(Locale("es", "EC")) // Configurado en español

            if (resultado != TextToSpeech.LANG_MISSING_DATA && resultado != TextToSpeech.LANG_NOT_SUPPORTED) {
                val mensajeVoz = "Cuestionario completado. Tu puntuación es $puntajeRecibido sobre 15 puntos "
                tts?.speak(mensajeVoz, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }

    // Preventivo: Liberamos recursos del sistema para que no consuma batería de más
    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

}