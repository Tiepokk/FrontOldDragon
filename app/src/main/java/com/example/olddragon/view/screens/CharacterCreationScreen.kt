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
import com.example.olddragon.model.Personagem
import com.example.olddragon.view.components.CharacterSheet

@Composable
fun CharacterCreationScreen(
    attributes: Atributos?,
    selectedRace: String?,
    selectedClass: String?,
    modifier: Modifier = Modifier,
    onCreateCharacter: (String, Atributos, String, String) -> Unit,
    onSaveCharacter: () -> Unit,
    createdCharacter: Personagem?,
    personagemSalvo: Boolean,
    mensagemErro: String?,
    onClearError: () -> Unit,
    onVoltar: () -> Unit
) {
    var characterName by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Mostrar diálogo de sucesso quando personagem for salvo
    LaunchedEffect(personagemSalvo) {
        if (personagemSalvo) {
            showSuccessDialog = true
        }
    }

    // Snackbar para mensagens de erro
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensagemErro) {
        mensagemErro?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            onClearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Criação de Personagem",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Resumo das seleções anteriores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Resumo do Personagem",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    selectedClass?.let { classe ->
                        Text(text = "Classe: $classe", fontSize = 14.sp)
                    }

                    selectedRace?.let { raca ->
                        Text(text = "Raça: $raca", fontSize = 14.sp)
                    }

                    attributes?.let { attr ->
                        Text(
                            text = "Atributos: FOR ${attr.forca}, DES ${attr.destreza}, CON ${attr.constituicao}, INT ${attr.inteligencia}, SAB ${attr.sabedoria}, CAR ${attr.carisma}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Nome do personagem
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Nome do Personagem",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = characterName,
                        onValueChange = { characterName = it },
                        placeholder = { Text("Digite o nome do seu personagem") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = createdCharacter == null
                    )
                }
            }

            // Botão para finalizar criação
            if (createdCharacter == null) {
                Button(
                    onClick = {
                        if (attributes != null && selectedRace != null && selectedClass != null) {
                            onCreateCharacter(characterName, attributes, selectedRace, selectedClass)
                        }
                    },
                    enabled = characterName.isNotBlank() && attributes != null && selectedRace != null && selectedClass != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Finalizar Personagem",
                        fontSize = 16.sp
                    )
                }
            }

            // Ficha do personagem e botão salvar
            createdCharacter?.let { personagem ->
                CharacterSheet(personagem = personagem)

                if (!personagemSalvo) {
                    Button(
                        onClick = { onSaveCharacter() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Salvar Personagem",
                            fontSize = 16.sp
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "✓ Personagem salvo com sucesso!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
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
                    text = if (personagemSalvo) "Voltar ao Menu" else "Voltar",
                    fontSize = 16.sp
                )
            }
        }
    }

    // Diálogo de sucesso
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Sucesso!") },
            text = { Text("Personagem salvo com sucesso no banco de dados.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}