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
import com.example.olddragon.controller.AttributeController
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.AttributeGeneratorScreen

class AttributeGeneratorActivity : ComponentActivity() {
    private val attributeController = AttributeController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val atributos by attributeController.atributos.collectAsState()
                    val valoresBrutos by attributeController.valoresBrutos.collectAsState()

                    AttributeGeneratorScreen(
                        modifier = Modifier.padding(innerPadding),
                        atributos = atributos,
                        valoresBrutos = valoresBrutos,
                        onGerarAtributos = { metodo ->
                            attributeController.gerarAtributos(metodo)
                        },
                        onContinuarCriacao = { attributes ->
                            val intent = Intent(this@AttributeGeneratorActivity, RaceSelectionActivity::class.java)
                            intent.putExtra("attributes", attributes)
                            startActivity(intent)
                        },
                        onVoltar = { finish() },
                        onContinuarParaSelecao = { valores ->
                            val intent = Intent(this@AttributeGeneratorActivity, AttributeSelectionActivity::class.java)
                            intent.putIntegerArrayListExtra("values", ArrayList(valores))
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}