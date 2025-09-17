package com.example.olddragon.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onGerarAtributos: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Old Dragon",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Sistema de RPG Clássico",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Botão para geração de atributos
        Button(
            onClick = onGerarAtributos,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Gerar Atributos",
                fontSize = 16.sp
            )
        }

        // Informações sobre o aplicativo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Funcionalidades",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "• Geração de atributos (Clássico, Aventureiro, Heroico)",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "• Seleção de classes (Ranger, Bárbaro, Druida)",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "• Criação completa de personagem",
                    fontSize = 14.sp
                )
            }
        }
    }
}