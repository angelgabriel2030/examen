package com.example.examen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var textViewTitulo: TextView
    private lateinit var viewRojo: View
    private lateinit var viewAzul: View
    private lateinit var viewVerde: View
    private lateinit var viewAmarillo: View
    private lateinit var botonReiniciar: Button
    private lateinit var botonRendirse: Button
    private val secuenciaJuego = mutableListOf<Int>()
    private var indiceJugador = 0
    private var ronda = 0
    private var isPlayerTurn = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializarVistas()
        configurarListeners()
        iniciarJuego()
    }

    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.textView_titulo)
        viewRojo = findViewById(R.id.view_rojo)
        viewAzul = findViewById(R.id.view_azul)
        viewVerde = findViewById(R.id.view_verde)
        viewAmarillo = findViewById(R.id.view_amarillo)
        botonReiniciar = findViewById(R.id.button_uno)
        botonRendirse = findViewById(R.id.button_dos)
    }

    private fun configurarListeners() {
        viewRojo.setOnClickListener(this)
        viewAzul.setOnClickListener(this)
        viewVerde.setOnClickListener(this)
        viewAmarillo.setOnClickListener(this)
        botonReiniciar.text = "Reiniciar"
        botonReiniciar.setOnClickListener {
            iniciarJuego()
            Toast.makeText(this, "Juego Reiniciado", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Juego Iniciado", Toast.LENGTH_SHORT).show()
        }
        botonRendirse.text = "Rendirse"
        botonRendirse.setOnClickListener {
            if (isPlayerTurn) {
                Toast.makeText(this, "Te Rendiste, Juego Terminado, llegaste a la  ronda $ronda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniciarJuego() {
        ronda = 0
        secuenciaJuego.clear()
        handler.removeCallbacksAndMessages(null)
        siguienteRonda()
    }

    private fun siguienteRonda() {
        ronda++
        textViewTitulo.text = "Ronda: $ronda"
        indiceJugador = 0
        isPlayerTurn = false
        desactivarCuadrantes()
        val siguienteColorId = listOf(R.id.view_rojo, R.id.view_azul, R.id.view_verde, R.id.view_amarillo).random()
        secuenciaJuego.add(siguienteColorId)
        handler.postDelayed({
            mostrarSecuencia()
        }, 1000)
    }

    private fun mostrarSecuencia(indice: Int = 0) {
        if (indice >= secuenciaJuego.size) {
            isPlayerTurn = true
            activarCuadrantes()
            textViewTitulo.text = "Tu turno"
            return
        }
        val viewId = secuenciaJuego[indice]
        val view = findViewById<View>(viewId)
        handler.postDelayed({
            iluminarVista(view, true)
            handler.postDelayed({
                iluminarVista(view, false)
                mostrarSecuencia(indice + 1)
            }, 400)
        }, 400)
    }

    override fun onClick(view: View?) {
        if (!isPlayerTurn || view == null) return
        iluminarVista(view, true)
        handler.postDelayed({ iluminarVista(view, false) }, 200)
        if (view.id == secuenciaJuego[indiceJugador]) {
            indiceJugador++
            if (indiceJugador >= secuenciaJuego.size) {
                Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
                handler.postDelayed({ siguienteRonda() }, 1000)
            }
        } else {
            Toast.makeText(this, "Incorrecto Llegaste a la ronda $ronda.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun iluminarVista(view: View, iluminar: Boolean) {
        val colorRes = when (view.id) {
            R.id.view_rojo -> if (iluminar) R.color.rojo else R.color.rojo_brillante
            R.id.view_azul -> if (iluminar) R.color.azul else R.color.azul_brillante
            R.id.view_verde -> if (iluminar) R.color.verde else R.color.verde_brillante
            R.id.view_amarillo -> if (iluminar) R.color.amarillo else R.color.amarillo_brillante
            else -> android.R.color.transparent
        }
        view.setBackgroundColor(getColor(colorRes))
    }
    private fun activarCuadrantes() {
        viewRojo.isClickable = true
        viewAzul.isClickable = true
        viewVerde.isClickable = true
        viewAmarillo.isClickable = true
    }

    private fun desactivarCuadrantes() {
        viewRojo.isClickable = false
        viewAzul.isClickable = false
        viewVerde.isClickable = false
        viewAmarillo.isClickable = false
    }
}



