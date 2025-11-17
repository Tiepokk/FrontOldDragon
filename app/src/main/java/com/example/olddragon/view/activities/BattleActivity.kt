package com.example.olddragon.view.activities

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.olddragon.data.database.AppDatabase
import com.example.olddragon.data.repository.PersonagemRepository
import com.example.olddragon.model.Batalha
import com.example.olddragon.model.Combatente
import com.example.olddragon.model.InimigoFactory
import com.example.olddragon.model.toCombatente
import com.example.olddragon.service.BattleService
import com.example.olddragon.service.EstadoBatalha
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.BattleScreen
import kotlinx.coroutines.launch

class BattleActivity : ComponentActivity() {

    private var battleService: BattleService? = null
    private var serviceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BattleService.BattleServiceBinder
            battleService = binder.getService()
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            battleService = null
            serviceBound = false
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // Permissão negada, mas continuamos (notificações não são críticas)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Solicitar permissão para notificações (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val personagemId = intent.getLongExtra("personagemId", 0L)

        // Bind ao serviço
        val serviceIntent = Intent(this, BattleService::class.java)
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)

        setContent {
            OldDragonTheme {
                val scope = rememberCoroutineScope()
                val database = AppDatabase.getDatabase(applicationContext)
                val repository = PersonagemRepository(database.personagemDao())

                var personagemCombatente by remember { mutableStateOf<Combatente?>(null) }
                var inimigos by remember { mutableStateOf<List<Combatente>>(emptyList()) }
                var batalhaCriada by remember { mutableStateOf<Batalha?>(null) }
                var estadoBatalha by remember { mutableStateOf<EstadoBatalha>(EstadoBatalha.Aguardando) }

                // Coletar estado do serviço
                LaunchedEffect(serviceBound) {
                    if (serviceBound) {
                        battleService?.estadoBatalha?.collect { estado ->
                            estadoBatalha = estado
                        }
                    }
                }

                // Carregar personagem
                LaunchedEffect(personagemId) {
                    scope.launch {
                        val personagem = repository.obterPorId(personagemId)
                        personagem?.let {
                            personagemCombatente = it.toCombatente()
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BattleScreen(
                        personagem = personagemCombatente,
                        inimigos = inimigos,
                        batalha = batalhaCriada,
                        estadoBatalha = estadoBatalha,
                        modifier = Modifier.padding(innerPadding),
                        onAdicionarInimigo = { nomeInimigo ->
                            val novoInimigo = InimigoFactory.criarInimigoPorNome(nomeInimigo)
                            inimigos = inimigos + novoInimigo
                        },
                        onRemoverInimigo = { index ->
                            inimigos = inimigos.filterIndexed { i, _ -> i != index }
                        },
                        onIniciarBatalha = {
                            personagemCombatente?.let { personagem ->
                                if (inimigos.isNotEmpty()) {
                                    val batalha = Batalha(
                                        personagem = personagem.copy(),
                                        inimigos = inimigos.map { it.copy() }
                                    )
                                    batalhaCriada = batalha

                                    // Iniciar serviço
                                    val serviceIntent = Intent(this@BattleActivity, BattleService::class.java).apply {
                                        action = BattleService.ACTION_START_BATTLE
                                        putExtra(BattleService.EXTRA_BATALHA, batalha)
                                    }

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        startForegroundService(serviceIntent)
                                    } else {
                                        startService(serviceIntent)
                                    }
                                }
                            }
                        },
                        onPararBatalha = {
                            val serviceIntent = Intent(this@BattleActivity, BattleService::class.java).apply {
                                action = BattleService.ACTION_STOP_BATTLE
                            }
                            startService(serviceIntent)
                        },
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
        }
    }
}