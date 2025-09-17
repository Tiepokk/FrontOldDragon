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
import com.example.olddragon.view.screens.AttributeSelectionScreen

class AttributeSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val values = intent.getIntegerArrayListExtra("values") ?: arrayListOf()

        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AttributeSelectionScreen(
                        availableValues = values.toList(),
                        modifier = Modifier.padding(innerPadding),
                        onAttributesSelected = { attributes ->
                            val intent = Intent(this@AttributeSelectionActivity, RaceSelectionActivity::class.java)
                            intent.putExtra("attributes", attributes)
                            startActivity(intent)
                        },
                        onVoltar = { finish() }
                    )
                }
            }
        }
    }
}