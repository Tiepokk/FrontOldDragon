// view/screens/AttributeGeneratorScreen.kt
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
import com.example.olddragon.controller.MetodoGeracao
import com.example.olddragon.model.Atributos
import com.example.olddragon.view.components.AttributeDisplay

@Composable
fun AttributeGeneratorScreen(
    modifier: Modifier = Modifier,
    atributos: Atributos?,
    valoresBrutos: List<Int>,
    onGerarAtributos: (MetodoGeracao) -> Unit,
    onContinuarCriacao: (Atributos) -> Unit,
    onVoltar: () -> Unit,
    onContinuarParaSelecao: (List<Int>) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("Clássico") }

    val methods = listOf("Clássico", "Aventureiro", "Heroico")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gerador de Atributos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Seleção de Método
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Método de Geração",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Clássico: 3d6 para cada atributo\nAventureiro: 3d6 para cada atributo\nHeroico: 4d6, descarta o menor",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                methods.forEach { method ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedMethod == method,
                            onClick = { selectedMethod = method }
                        )
                        Text(
                            text = method,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Botão Gerar
        Button(
            onClick = {
                val metodo = when (selectedMethod) {
                    "Heroico" -> MetodoGeracao.HEROICO
                    "Aventureiro" -> MetodoGeracao.AVENTUREIRO
                    else -> MetodoGeracao.CLASSICO
                }
                onGerarAtributos(metodo)

                // Se for aventureiro ou heroico, vai para tela de seleção
                if (selectedMethod == "Aventureiro" || selectedMethod == "Heroico") {
                    onContinuarParaSelecao(valoresBrutos)
                }
            },
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

        // Exibir valores brutos (apenas para método clássico)
        if (valoresBrutos.isNotEmpty() && selectedMethod == "Clássico") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Valores Gerados",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = valoresBrutos.sorted().reversed().joinToString(", "),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Soma total: ${valoresBrutos.sum()}",
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Exibir Atributos Gerados
        atributos?.let { attributes ->
            AttributeDisplay(attributes = attributes)

            // Botão para continuar criando personagem
            Button(
                onClick = { onContinuarCriacao(attributes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Criar Personagem com estes Atributos",
                    fontSize = 16.sp
                )
            }
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