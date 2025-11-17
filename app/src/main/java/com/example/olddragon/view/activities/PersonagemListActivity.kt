package com.example.olddragon.view.activities

import android.content.Intent
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
import com.example.olddragon.controller.PersonagemListController
import com.example.olddragon.data.database.AppDatabase
import com.example.olddragon.data.repository.PersonagemRepository
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.PersonagemListScreen
import kotlinx.coroutines.launch

class PersonagemListActivity : ComponentActivity() {
    private lateinit var personagemListController: PersonagemListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar banco de dados e repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PersonagemRepository(database.personagemDao())
        personagemListController = PersonagemListController(repository)

        setContent {
            OldDragonTheme {
                val scope = rememberCoroutineScope()
                val personagens by personagemListController.personagens.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PersonagemListScreen(
                        personagens = personagens,
                        modifier = Modifier.padding(innerPadding),
                        onPersonagemClick = { personagem ->
                            val intent = Intent(this@PersonagemListActivity, PersonagemDetailActivity::class.java)
                            intent.putExtra("personagemId", personagem.id)
                            startActivity(intent)
                        },
                        onDeletePersonagem = { id ->
                            scope.launch {
                                personagemListController.deletarPersonagem(id)
                            }
                        },
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }
}