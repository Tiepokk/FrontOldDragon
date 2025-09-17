package com.example.olddragon.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olddragon.model.Personagem

@Composable
fun CharacterSheet(
    personagem: Personagem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ficha do Personagem",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Informações básicas
            Text(text = "Nome: ${personagem.nome}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = "Raça: ${personagem.raca}", fontSize = 14.sp)
            Text(text = "Classe: ${personagem.classe}", fontSize = 14.sp)
            Text(text = "Alinhamento: ${personagem.alinhamento}", fontSize = 14.sp)
            Text(text = "Nível: ${personagem.nivel}", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Atributos
            Text(
                text = "Atributos",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val atributos = listOf(
                "Força" to personagem.atributos.forca,
                "Destreza" to personagem.atributos.destreza,
                "Constituição" to personagem.atributos.constituicao,
                "Inteligência" to personagem.atributos.inteligencia,
                "Sabedoria" to personagem.atributos.sabedoria,
                "Carisma" to personagem.atributos.carisma
            )

            atributos.forEach { (nome, valor) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "$nome:", fontSize = 14.sp)
                    Text(text = valor.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total dos Atributos:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = (personagem.atributos.forca + personagem.atributos.destreza +
                            personagem.atributos.constituicao + personagem.atributos.inteligencia +
                            personagem.atributos.sabedoria + personagem.atributos.carisma).toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}