package com.example.primerintento
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class EncuestaResultMainActivity : AppCompatActivity(), TextToSpeech.OnInitListener{
    var tts: TextToSpeech? = null
    private var puntajeRecibido: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_encuesta_result_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


         puntajeRecibido = intent.getIntExtra("PUNTAJE_FINAL", 0)
        //val nombre = intent.getIntExtra("USER_NAME", 0)
        val lblRespuesta = findViewById<TextView>(R.id.lblRespuesta)
        val mensaje = "Gracias por tu Retroalimentación. Tu Calificación es: $puntajeRecibido Puntos"
        lblRespuesta.text = mensaje
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()


        tts = TextToSpeech(this, this )


        val btnSalir = findViewById<Button>(R.id.btnSalirEncuesta)

        btnSalir.setOnClickListener {
            val intentMenu = Intent(this, MenuMainActivity::class.java)
            intentMenu.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intentMenu)
            finish()
        }



    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val resultado = tts?.setLanguage(Locale("es", "EC")) // Configurado en español

            if (resultado != TextToSpeech.LANG_MISSING_DATA && resultado != TextToSpeech.LANG_NOT_SUPPORTED) {
                // Si el idioma es correcto, el teléfono habla de inmediato al abrir la pantalla
                val mensajeVoz = "Gracias por tu Retroalimentación, tu calificación es $puntajeRecibido puntos"
                tts?.speak(mensajeVoz, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }

    // Liberamos la memoria del motor de voz al cerrar la pantalla para que no enlentezca el celular
    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }
}