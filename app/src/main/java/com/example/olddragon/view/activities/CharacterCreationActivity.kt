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
import androidx.compose.ui.Modifier
import com.example.olddragon.controller.CharacterController
import com.example.olddragon.model.Atributos
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.CharacterCreationScreen

class CharacterCreationActivity : ComponentActivity() {
    private val characterController = CharacterController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val personagem by characterController.personagem.collectAsState()

                    CharacterCreationScreen(
                        attributes = attributes,
                        selectedRace = selectedRace,
                        selectedClass = selectedClass,
                        modifier = Modifier.padding(innerPadding),
                        onCreateCharacter = { nome, atributos, raca, classe ->
                            characterController.criarPersonagem(nome, atributos, raca, classe)
                        },
                        onCharacterCreated = { /* Handle character created if needed */ },
                        createdCharacter = personagem,
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }
}