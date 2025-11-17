package com.example.olddragon.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.olddragon.controller.PersonagemListController
import com.example.olddragon.data.database.AppDatabase
import com.example.olddragon.data.repository.PersonagemRepository
import com.example.olddragon.model.Personagem
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.PersonagemDetailScreen
import kotlinx.coroutines.launch

class PersonagemDetailActivity : ComponentActivity() {
    private lateinit var personagemListController: PersonagemListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val personagemId = intent.getLongExtra("personagemId", 0L)

        // Inicializar banco de dados e repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PersonagemRepository(database.personagemDao())
        personagemListController = PersonagemListController(repository)

        setContent {
            OldDragonTheme {
                val scope = rememberCoroutineScope()
                var personagem by remember { mutableStateOf<Personagem?>(null) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(personagemId) {
                    personagem = personagemListController.obterPersonagemPorId(personagemId)
                    isLoading = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (!isLoading && personagem != null) {
                        PersonagemDetailScreen(
                            personagem = personagem!!,
                            modifier = Modifier.padding(innerPadding),
                            onDelete = {
                                scope.launch {
                                    personagemListController.deletarPersonagem(personagemId)
                                    finish()
                                }
                            },
                            onVoltar = { finish() }
                        )
                    }
                }
            }
        }
    }
}