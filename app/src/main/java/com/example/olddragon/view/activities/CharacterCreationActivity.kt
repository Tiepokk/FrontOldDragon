package com.example.olddragon.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.olddragon.controller.CharacterController
import com.example.olddragon.data.database.AppDatabase
import com.example.olddragon.data.repository.PersonagemRepository
import com.example.olddragon.model.Atributos
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.CharacterCreationScreen
import kotlinx.coroutines.launch

class CharacterCreationActivity : ComponentActivity() {
    private lateinit var characterController: CharacterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar banco de dados e repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PersonagemRepository(database.personagemDao())
        characterController = CharacterController(repository)

        val attributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("attributes", Atributos::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("attributes") as? Atributos
        }

        val selectedRace = intent.getStringExtra("selectedRace")
        val selectedClass = intent.getStringExtra("selectedClass")

        setContent {
            OldDragonTheme {
                val scope = rememberCoroutineScope()
                val personagem by characterController.personagem.collectAsState()
                val personagemSalvo by characterController.personagemSalvo.collectAsState()
                val mensagemErro by characterController.mensagemErro.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CharacterCreationScreen(
                        attributes = attributes,
                        selectedRace = selectedRace,
                        selectedClass = selectedClass,
                        modifier = Modifier.padding(innerPadding),
                        onCreateCharacter = { nome, atributos, raca, classe ->
                            scope.launch {
                                characterController.criarPersonagem(nome, atributos, raca, classe)
                            }
                        },
                        onSaveCharacter = {
                            scope.launch {
                                characterController.salvarPersonagem()
                            }
                        },
                        createdCharacter = personagem,
                        personagemSalvo = personagemSalvo,
                        mensagemErro = mensagemErro,
                        onClearError = { characterController.limparMensagemErro() },
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }
}