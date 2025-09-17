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
import com.example.olddragon.model.Classe
import com.example.olddragon.view.components.ClassDetailsCard

@Composable
fun ClassSelectionScreen(
    attributes: Atributos?,
    selectedRace: String?,
    classes: List<Classe>,
    modifier: Modifier = Modifier,
    onClassSelected: (String) -> Unit,
    onVoltar: () -> Unit
) {
    var selectedClass by remember { mutableStateOf<String?>(null) }
    var showClassDetails by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seleção de Classe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Mostrar seleções anteriores
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Seleções Anteriores",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                selectedRace?.let { race ->
                    Text(text = "Raça: $race", fontSize = 14.sp)
                }

                attributes?.let { attr ->
                    Text(
                        text = "Atributos: FOR ${attr.forca}, DES ${attr.destreza}, CON ${attr.constituicao}, INT ${attr.inteligencia}, SAB ${attr.sabedoria}, CAR ${attr.carisma}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Seleção de classe
        Text(
            text = "Escolha sua Classe",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        classes.forEach { classe ->
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
                            selected = selectedClass == classe.nome,
                            onClick = {
                                selectedClass = classe.nome
                                showClassDetails = true
                            }
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = classe.nome,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = classe.descricao,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Detalhes da classe selecionada
        if (showClassDetails && selectedClass != null) {
            val classe = classes.find { it.nome == selectedClass }
            classe?.let {
                ClassDetailsCard(
                    className = it.nome,
                    details = it.detalhes
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Continuar
        Button(
            onClick = {
                selectedClass?.let { classe ->
                    onClassSelected(classe)
                }
            },
            enabled = selectedClass != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Criar Personagem",
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
