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
import com.example.olddragon.view.components.AttributeDisplay
import com.example.olddragon.view.components.AttributeSelector

@Composable
fun AttributeSelectionScreen(
    availableValues: List<Int>,
    modifier: Modifier = Modifier,
    onAttributesSelected: (Atributos) -> Unit,
    onVoltar: () -> Unit
) {
    var selectedAttributes by remember { mutableStateOf(mapOf<String, Int?>()) }
    var generatedAttributes by remember { mutableStateOf<Atributos?>(null) }

    val attributeNames = listOf(
        "Força", "Destreza", "Constituição",
        "Inteligência", "Sabedoria", "Carisma"
    )

    // Inicializar mapa de atributos
    LaunchedEffect(Unit) {
        selectedAttributes = attributeNames.associateWith { null }
    }

    // Função para calcular valores restantes
    fun getRemainingValues(): List<Int> {
        val usedValues = selectedAttributes.values.filterNotNull()
        val remainingValues = availableValues.toMutableList()
        usedValues.forEach { usedValue ->
            remainingValues.remove(usedValue)
        }
        return remainingValues
    }

    // Função para obter valores disponíveis para um atributo específico
    fun getAvailableValuesForAttribute(attributeName: String): List<Int> {
        val currentValue = selectedAttributes[attributeName]
        val remainingValues = getRemainingValues().toMutableList()
        currentValue?.let { remainingValues.add(it) }
        return remainingValues.distinct().sorted().reversed()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Distribuir Atributos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar valores disponíveis
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Valores Disponíveis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val remainingValues = getRemainingValues().sorted().reversed()

                Text(
                    text = if (remainingValues.isNotEmpty()) remainingValues.joinToString(", ") else "Todos os valores foram distribuídos",
                    fontSize = 16.sp
                )

                Text(
                    text = "Total disponível: ${availableValues.sum()}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Seleção de atributos
        attributeNames.forEach { attributeName ->
            AttributeSelector(
                attributeName = attributeName,
                selectedValue = selectedAttributes[attributeName],
                availableValues = getAvailableValuesForAttribute(attributeName),
                onValueSelected = { value ->
                    selectedAttributes = selectedAttributes.toMutableMap().apply {
                        this[attributeName] = value
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botão para gerar atributos
        val allAttributesSelected = selectedAttributes.values.all { it != null }

        Button(
            onClick = {
                if (allAttributesSelected) {
                    generatedAttributes = Atributos(
                        forca = selectedAttributes["Força"]!!,
                        destreza = selectedAttributes["Destreza"]!!,
                        constituicao = selectedAttributes["Constituição"]!!,
                        inteligencia = selectedAttributes["Inteligência"]!!,
                        sabedoria = selectedAttributes["Sabedoria"]!!,
                        carisma = selectedAttributes["Carisma"]!!
                    )
                }
            },
            enabled = allAttributesSelected,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Finalizar Distribuição",
                fontSize = 16.sp
            )
        }

        // Exibir atributos finais
        generatedAttributes?.let { attributes ->
            AttributeDisplay(attributes = attributes)

            Button(
                onClick = { onAttributesSelected(attributes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp)
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