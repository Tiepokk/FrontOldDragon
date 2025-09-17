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
import com.example.olddragon.controller.RaceController
import com.example.olddragon.model.Atributos
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.RaceSelectionScreen

class RaceSelectionActivity : ComponentActivity() {
    private val raceController = RaceController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val attributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("attributes", Atributos::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("attributes") as? Atributos
        }

        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val races by raceController.racasSelecionaveis.collectAsState()

                    RaceSelectionScreen(
                        attributes = attributes,
                        races = races,
                        modifier = Modifier.padding(innerPadding),
                        onRaceSelected = { raceName ->
                            raceController.selecionarRaca(raceName)
                            val intent = Intent(this@RaceSelectionActivity, ClassSelectionActivity::class.java)
                            intent.putExtra("attributes", attributes)
                            intent.putExtra("selectedRace", raceName)
                            startActivity(intent)
                        },
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }
}