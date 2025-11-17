package com.example.olddragon.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olddragon.model.*
import com.example.olddragon.service.EstadoBatalha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(
    personagem: Combatente?,
    inimigos: List<Combatente>,
    batalha: Batalha?,
    estadoBatalha: EstadoBatalha,
    modifier: Modifier = Modifier,
    onAdicionarInimigo: (String) -> Unit,
    onRemoverInimigo: (Int) -> Unit,
    onIniciarBatalha: () -> Unit,
    onPararBatalha: () -> Unit,
    onVoltar: () -> Unit
) {
    var showAddEnemyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "⚔️ Simulador de Batalha",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Informações do personagem
        personagem?.let {
            CombatenteCard(
                combatente = it,
                titulo = "Seu Personagem"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de inimigos
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Inimigos (${inimigos.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (estadoBatalha is EstadoBatalha.Aguardando) {
                        IconButton(onClick = { showAddEnemyDialog = true }) {
                            Icon(Icons.Default.Add, "Adicionar inimigo")
                        }
                    }
                }

                if (inimigos.isEmpty()) {
                    Text(
                        text = "Nenhum inimigo adicionado",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    inimigos.forEachIndexed { index, inimigo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${index + 1}. ${inimigo.nome}",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "PV: ${inimigo.pontosVidaAtuais}/${inimigo.pontosVidaMaximos} | CA: ${inimigo.classeArmadura}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (estadoBatalha is EstadoBatalha.Aguardando) {
                                IconButton(onClick = { onRemoverInimigo(index) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        "Remover inimigo",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status da batalha
        when (estadoBatalha) {
            is EstadoBatalha.Aguardando -> {
                Button(
                    onClick = onIniciarBatalha,
                    enabled = personagem != null && inimigos.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Iniciar Batalha", fontSize = 16.sp)
                }
            }
            is EstadoBatalha.EmAndamento -> {
                BatalhaEmAndamentoCard(batalha = estadoBatalha.batalha)

                Button(
                    onClick = onPararBatalha,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Parar Batalha", fontSize = 16.sp)
                }
            }
            is EstadoBatalha.Finalizada -> {
                BatalhaFinalizadaCard(
                    batalha = estadoBatalha.batalha,
                    vitoria = estadoBatalha.vitoria
                )
            }
            is EstadoBatalha.Cancelada -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Batalha cancelada",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVoltar,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Voltar", fontSize = 16.sp)
        }
    }

    // Dialog para adicionar inimigo
    if (showAddEnemyDialog) {
        AddEnemyDialog(
            onDismiss = { showAddEnemyDialog = false },
            onConfirm = { nomeInimigo ->
                onAdicionarInimigo(nomeInimigo)
                showAddEnemyDialog = false
            }
        )
    }
}

@Composable
fun CombatenteCard(
    combatente: Combatente,
    titulo: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = combatente.nome,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem("PV", "${combatente.pontosVidaAtuais}/${combatente.pontosVidaMaximos}")
                InfoItem("CA", combatente.classeArmadura.toString())
                InfoItem("Ataque", "+${combatente.bonusAtaque}")
                InfoItem("Dano", combatente.dano)
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BatalhaEmAndamentoCard(batalha: Batalha) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "⚔️ Batalha em Andamento - Turno ${batalha.turnoAtual}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(batalha.turnos.reversed()) { turno ->
                    TurnoCard(turno)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status atual
            Text(
                text = "${batalha.personagem.nome}: ${batalha.personagem.pontosVidaAtuais} PV",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            batalha.inimigos.filter { it.estaVivo() }.forEach { inimigo ->
                Text(
                    text = "${inimigo.nome}: ${inimigo.pontosVidaAtuais} PV",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun TurnoCard(turno: TurnoBatalha) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Turno ${turno.numero}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            turno.ataques.forEach { ataque ->
                val cor = if (ataque.acertou) {
                    if (ataque.morreu) Color.Red else Color.Green
                } else {
                    Color.Gray
                }

                Text(
                    text = if (ataque.acertou) {
                        "• ${ataque.atacante} acertou ${ataque.defensor} (${ataque.rolagem}) causando ${ataque.dano} de dano" +
                                if (ataque.morreu) " 💀" else ""
                    } else {
                        "• ${ataque.atacante} errou ${ataque.defensor} (${ataque.rolagem})"
                    },
                    fontSize = 11.sp,
                    color = cor,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }
    }
}

@Composable
fun BatalhaFinalizadaCard(batalha: Batalha, vitoria: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (vitoria) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = if (vitoria) "🎉 VITÓRIA!" else "💀 DERROTA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (vitoria) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = if (vitoria) {
                    "${batalha.personagem.nome} venceu após ${batalha.turnoAtual} turnos!"
                } else {
                    "${batalha.personagem.nome} foi derrotado após ${batalha.turnoAtual} turnos."
                },
                fontSize = 16.sp,
                color = if (vitoria) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Histórico da Batalha",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(batalha.turnos) { turno ->
                    TurnoCard(turno)
                }
            }
        }
    }
}

@Composable
fun AddEnemyDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedEnemy by remember { mutableStateOf("Goblin") }
    val enemies = InimigoFactory.obterInimigosDisponiveis()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Inimigo") },
        text = {
            Column {
                Text("Escolha um inimigo para adicionar à batalha:")

                Spacer(modifier = Modifier.height(8.dp))

                enemies.forEach { enemy ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedEnemy == enemy,
                            onClick = { selectedEnemy = enemy }
                        )
                        Text(
                            text = enemy,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedEnemy) }) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}