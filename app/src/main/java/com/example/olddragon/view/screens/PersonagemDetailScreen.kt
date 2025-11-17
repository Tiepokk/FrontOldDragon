package com.example.olddragon.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olddragon.model.Personagem
import com.example.olddragon.view.components.CharacterSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonagemDetailScreen(
    personagem: Personagem,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onVoltar: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(personagem.nome) },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar personagem",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ficha completa do personagem
            CharacterSheet(personagem = personagem)

            Spacer(modifier = Modifier.height(16.dp))

            // Informações adicionais
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Informações da Raça",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Raça: ${personagem.raca}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Aqui você pode adicionar mais detalhes da raça
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Informações da Classe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Classe: ${personagem.classe}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Aqui você pode adicionar mais detalhes da classe
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Voltar
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Voltar")
            }
        }
    }

    // Diálogo de confirmação de exclusão
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Deletar Personagem") },
            text = { Text("Tem certeza que deseja deletar ${personagem.nome}? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Deletar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}