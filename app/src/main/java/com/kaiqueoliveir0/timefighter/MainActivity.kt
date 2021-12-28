package com.kaiqueoliveir0.timefighter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    internal lateinit var botao_Pressione: Button
    internal lateinit var texto_pontuacao: TextView
    internal lateinit var texto_tempo: TextView

    internal var pontuacao = 0

    internal var inicio_jogo = false

    internal lateinit var contador_Tempo : CountDownTimer
    internal val contadorInicial : Long = 60000
    internal val contadorIntervalo : Long = 1000
    internal var tempoRestanteNoContador : Long = 60000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate chamada. Pontuação é $pontuacao")

        botao_Pressione = findViewById(R.id.Botao_Pressione)
        texto_pontuacao = findViewById(R.id.Texto_pontuacao)
        texto_tempo = findViewById(R.id.Texto_tempo)

        botao_Pressione.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()

        }

        if (savedInstanceState!=null){
            pontuacao = savedInstanceState.getInt(SCORE_KEY)
            tempoRestanteNoContador = savedInstanceState.getLong(TIME_LEFT_KEY)
            restauraJogo()
        }
        else {
            reiniciaJogo()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, pontuacao)
        outState.putLong(TIME_LEFT_KEY, tempoRestanteNoContador)
        contador_Tempo.cancel()

        Log.d(TAG, "onSaveInstanceState. Pontuação salva: $pontuacao. Tempo restante: $tempoRestanteNoContador")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun reiniciaJogo(){
        pontuacao = 0

        texto_pontuacao.text = getString(R.string.suaPontuacao, pontuacao)

        val tempoInicialRestante = contadorInicial/1000
        texto_tempo.text = getString(R.string.tempoRestante, tempoInicialRestante)
        contador_Tempo = object : CountDownTimer(contadorInicial, contadorIntervalo){
            override fun onTick(miliAteOFim: Long) {
                tempoRestanteNoContador = miliAteOFim
                val tempoRestante = miliAteOFim / 1000
                texto_tempo.text = getString(R.string.tempoRestante, tempoRestante)
            }

            override fun onFinish() {
                encerra_Jogo()
            }
        }
        inicio_jogo = false
    }

    private fun restauraJogo(){
        texto_pontuacao.text = getString(R.string.suaPontuacao, pontuacao)

        val TempoRestaurado = tempoRestanteNoContador / 1000
        texto_tempo.text = getString(R.string.tempoRestante, TempoRestaurado)

        contador_Tempo = object : CountDownTimer(tempoRestanteNoContador, contadorIntervalo){
            override fun onTick(miliAteoFim: Long) {
                tempoRestanteNoContador = miliAteoFim
                val tempoRestante = miliAteoFim / 1000
                texto_tempo.text = getString(R.string.tempoRestante, tempoRestante)
            }

            override fun onFinish() {
                encerra_Jogo()
            }
        }
        contador_Tempo.start()
        inicio_jogo = true
    }

    private fun incrementScore() {
        if(!inicio_jogo){
            comeca_Jogo()
        }
        pontuacao++
        val novaPontuacao = getString(R.string.suaPontuacao, pontuacao)
        texto_pontuacao.text = novaPontuacao

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.piscar)
        texto_pontuacao.startAnimation(blinkAnimation)
    }

    private fun comeca_Jogo(){
        contador_Tempo.start()
        inicio_jogo = true

    }
    private fun encerra_Jogo(){
        Toast.makeText(this, getString(R.string.GameOver, pontuacao), Toast.LENGTH_LONG).show()
        reiniciaJogo()
    }
}