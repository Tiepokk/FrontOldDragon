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
import androidx.compose.ui.Modifier
import com.example.olddragon.controller.ClassController
import com.example.olddragon.model.Atributos
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.ClassSelectionScreen

class ClassSelectionActivity : ComponentActivity() {
    private val classController = ClassController()

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

        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val classes by classController.classesSelecionaveis.collectAsState()

                    ClassSelectionScreen(
                        attributes = attributes,
                        selectedRace = selectedRace,
                        classes = classes,
                        modifier = Modifier.padding(innerPadding),
                        onClassSelected = { className ->
                            classController.selecionarClasse(className)
                            val intent = Intent(this@ClassSelectionActivity, CharacterCreationActivity::class.java)
                            intent.putExtra("attributes", attributes)
                            intent.putExtra("selectedRace", selectedRace)
                            intent.putExtra("selectedClass", className)
                            startActivity(intent)
                        },
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }
}