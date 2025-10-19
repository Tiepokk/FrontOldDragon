package com.example.olddragon.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.olddragon.ui.theme.OldDragonTheme
import com.example.olddragon.view.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onGerarAtributos = {
                            val intent = Intent(this@MainActivity, AttributeGeneratorActivity::class.java)
                            startActivity(intent)
                        },
                        onVerPersonagens = {
                            val intent = Intent(this@MainActivity, PersonagemListActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}