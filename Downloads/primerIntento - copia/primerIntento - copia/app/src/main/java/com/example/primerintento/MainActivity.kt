package com.example.primerintento

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btningresar).setOnClickListener{
            val txtNombre= findViewById<EditText>(R.id.txtnombre).text.toString().uppercase()
            if (txtNombre.isNotEmpty())
            {
                Toast.makeText(this, "Presionaste el boton \n${txtNombre} ", Toast.LENGTH_SHORT).show()
                tts?.speak("Bienvenido "+txtNombre, TextToSpeech.QUEUE_FLUSH,null,"")
                findViewById<ProgressBar>(R.id.progressBar).visibility= View.GONE

                val pasarAlMenu = Intent(this, MenuMainActivity::class.java).apply {
                    putExtra("USER_NAME", txtNombre)
                }
                startActivity(pasarAlMenu)
            }
            else{
                Toast.makeText(this, "Debe ingresar su Nombre en el cuadro de texto ", Toast.LENGTH_SHORT).show()
                tts?.speak("Debe ingresar su Nombre en el cuadro de texto ", TextToSpeech.QUEUE_FLUSH,null,"")
            }
        }

        tts = TextToSpeech(this, this )

    }

    override fun onInit(status: Int) {
            if(status== TextToSpeech.SUCCESS){
                tts?.setLanguage(Locale("ES"))
                findViewById<TextView>(R.id.lblsaludo).text = "Voz Disponible"
            }
        else{
                findViewById<TextView>(R.id.lblsaludo).text = "Voz No Disponible :(.."
            }
    }
}

