package com.example.olddragon.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olddragon.model.Atributos
import com.example.olddragon.model.Raca
import com.example.olddragon.view.components.RaceDetailsCard

@Composable
fun RaceSelectionScreen(
    attributes: Atributos?,
    races: List<Raca>,
    modifier: Modifier = Modifier,
    onRaceSelected: (String) -> Unit,
    onVoltar: () -> Unit
) {
    var selectedRace by remember { mutableStateOf<String?>(null) }
    var showRaceDetails by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seleção de Raça",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Mostrar atributos atuais se disponíveis
        attributes?.let { attr ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Seus Atributos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val atributos = listOf(
                        "Força" to attr.forca,
                        "Destreza" to attr.destreza,
                        "Constituição" to attr.constituicao,
                        "Inteligência" to attr.inteligencia,
                        "Sabedoria" to attr.sabedoria,
                        "Carisma" to attr.carisma
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
                }
            }
        }

        // Lista de raças
        races.forEach { raca ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedRace == raca.nome,
                            onClick = {
                                selectedRace = raca.nome
                                showRaceDetails = true
                            }
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = raca.nome,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = raca.descricao,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Detalhes da raça selecionada
        selectedRace?.let { raceName ->
            if (showRaceDetails) {
                val raca = races.find { it.nome == raceName }
                raca?.let {
                    RaceDetailsCard(
                        raceName = it.nome,
                        details = it.detalhes
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Continuar
        Button(
            onClick = {
                selectedRace?.let { race ->
                    onRaceSelected(race)
                }
            },
            enabled = selectedRace != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Continuar para Seleção de Classe",
                fontSize = 16.sp
            )
        }

        // Botão Voltar
        OutlinedButton(
            onClick = onVoltar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Voltar",
                fontSize = 16.sp
            )
        }
    }
}