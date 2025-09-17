package com.example.olddragon.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olddragon.model.Atributos

@Composable
fun AttributeDisplay(
    attributes: Atributos,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Atributos Gerados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val atributos = listOf(
                "Força" to attributes.forca,
                "Destreza" to attributes.destreza,
                "Constituição" to attributes.constituicao,
                "Inteligência" to attributes.inteligencia,
                "Sabedoria" to attributes.sabedoria,
                "Carisma" to attributes.carisma
            )

            atributos.forEach { (nome, valor) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$nome:",
                        fontSize = 16.sp
                    )
                    Text(
                        text = valor.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = (attributes.forca + attributes.destreza + attributes.constituicao +
                            attributes.inteligencia + attributes.sabedoria + attributes.carisma).toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}