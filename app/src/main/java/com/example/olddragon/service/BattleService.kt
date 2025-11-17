package com.example.olddragon.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.olddragon.R
import com.example.olddragon.controller.BattleSimulator
import com.example.olddragon.model.Batalha
import com.example.olddragon.model.TurnoBatalha
import com.example.olddragon.view.activities.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BattleService : Service() {

    private val binder = BattleServiceBinder()
    private val simulator = BattleSimulator()
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var batalhaAtual: Batalha? = null
    private var batalhaContinuando = false

    private val _estadoBatalha = MutableStateFlow<EstadoBatalha>(EstadoBatalha.Aguardando)
    val estadoBatalha: StateFlow<EstadoBatalha> = _estadoBatalha

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "battle_channel"
        private const val CHANNEL_NAME = "Batalhas"
        const val ACTION_START_BATTLE = "action_start_battle"
        const val ACTION_STOP_BATTLE = "action_stop_battle"
        const val EXTRA_BATALHA = "extra_batalha"
    }

    inner class BattleServiceBinder : Binder() {
        fun getService(): BattleService = this@BattleService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        criarCanalNotificacao()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_BATTLE -> {
                val batalha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra(EXTRA_BATALHA, Batalha::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getSerializableExtra(EXTRA_BATALHA) as? Batalha
                }

                batalha?.let {
                    iniciarBatalha(it)
                }
            }
            ACTION_STOP_BATTLE -> {
                pararBatalha()
            }
        }

        return START_STICKY
    }

    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações de batalhas em andamento"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun criarNotificacao(mensagem: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Batalha em Andamento")
            .setContentText(mensagem)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    fun iniciarBatalha(batalha: Batalha) {
        batalhaAtual = batalha
        batalhaContinuando = true

        val notificacao = criarNotificacao("Batalha iniciada: ${batalha.personagem.nome} vs ${batalha.inimigos.size} inimigo(s)")
        startForeground(NOTIFICATION_ID, notificacao)

        _estadoBatalha.value = EstadoBatalha.EmAndamento(batalha)

        serviceScope.launch {
            executarBatalha()
        }
    }

    private suspend fun executarBatalha() {
        batalhaAtual?.let { batalha ->
            while (batalhaContinuando && !simulator.verificarFimBatalha(batalha)) {
                // Simular turno
                val turno = simulator.simularTurno(batalha)
                batalha.adicionarTurno(turno)

                // Atualizar estado
                _estadoBatalha.value = EstadoBatalha.EmAndamento(batalha)

                // Atualizar notificação
                atualizarNotificacao(batalha, turno)

                // Aguardar antes do próximo turno (2 segundos)
                delay(2000)
            }

            // Batalha finalizada
            finalizarBatalha(batalha)
        }
    }

    private fun atualizarNotificacao(batalha: Batalha, turno: TurnoBatalha) {
        val mensagem = "Turno ${batalha.turnoAtual}: ${batalha.personagem.nome} (${batalha.personagem.pontosVidaAtuais} PV)"
        val notificacao = criarNotificacao(mensagem)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificacao)
    }

    private fun finalizarBatalha(batalha: Batalha) {
        val vitoria = simulator.personagemVenceu(batalha)
        batalha.finalizarBatalha(vitoria)

        _estadoBatalha.value = EstadoBatalha.Finalizada(batalha, vitoria)

        // Se o personagem morreu, enviar notificação especial
        if (!vitoria && !batalha.personagem.estaVivo()) {
            enviarNotificacaoMorte(batalha.personagem.nome)
        } else if (vitoria) {
            enviarNotificacaoVitoria(batalha.personagem.nome)
        }

        // Parar serviço
        batalhaContinuando = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun enviarNotificacaoMorte(nomePersonagem: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificacao = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("💀 Seu Personagem Morreu!")
            .setContentText("$nomePersonagem foi derrotado em combate.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$nomePersonagem foi derrotado em combate. A aventura chegou ao fim...")
            )
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID + 1, notificacao)
    }

    private fun enviarNotificacaoVitoria(nomePersonagem: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificacao = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("⚔️ Vitória!")
            .setContentText("$nomePersonagem venceu a batalha!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID + 2, notificacao)
    }

    fun pararBatalha() {
        batalhaContinuando = false
        batalhaAtual?.let {
            it.finalizada = true
            _estadoBatalha.value = EstadoBatalha.Cancelada
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}

