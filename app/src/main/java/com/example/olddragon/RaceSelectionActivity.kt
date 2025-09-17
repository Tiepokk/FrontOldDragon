package com.example.olddragon

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.olddragon.model.*
import com.example.olddragon.ui.theme.OldDragonTheme

class RaceSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val attributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("attributes", Atributos::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("attributes") as? Atributos
        }

        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RaceSelectionScreen(
                        attributes = attributes,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaceSelectionScreen(
    attributes: Atributos?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedRace by remember { mutableStateOf<String?>(null) }
    var showRaceDetails by remember { mutableStateOf(false) }

    // Raças baseadas no sistema Old Dragon
    val races = listOf(
        "Humano" to "Versáteis e adaptáveis, podem ter qualquer alinhamento.",
        "Elfo" to "Seres graciosos com infravisão e resistências naturais.",
        "Anão" to "Vigorosos mineradores com conhecimento sobre pedras.",
        "Halfling" to "Pequenos e destemidos, excelentes em furtividade.",
        "Gnomo" to "Sagazes avaliadores com habilidades mágicas naturais.",
        "Meio-Elfo" to "Combinam versatilidade humana com graça élfica."
    )

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
        races.forEach { (raceName, description) ->
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
                            selected = selectedRace == raceName,
                            onClick = {
                                selectedRace = raceName
                                showRaceDetails = true
                            }
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = raceName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = description,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Detalhes da raça selecionada
        selectedRace?.let { race ->
            if (showRaceDetails) {
                RaceDetailsCard(raceName = race)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Continuar
        Button(
            onClick = {
                selectedRace?.let { race ->
                    val intent = Intent(context, ClassSelectionActivity::class.java)
                    intent.putExtra("attributes", attributes)
                    intent.putExtra("selectedRace", race)
                    context.startActivity(intent)
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
            onClick = {
                (context as ComponentActivity).finish()
            },
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

@Composable
fun RaceDetailsCard(raceName: String) {
    val raceDetails = when (raceName) {
        "Humano" -> listOf(
            "• Movimento: 9 metros",
            "• Infravisão: Nenhuma",
            "• Alinhamento: Aleatório (Ordem/Neutro/Caos)",
            "• Aprendizado: +10% de experiência",
            "• Adaptabilidade: +1 em todas as Jogadas de Proteção",
            "• Nenhuma restrição de equipamento"
        )
        "Elfo" -> listOf(
            "• Movimento: 9 metros",
            "• Infravisão: 18 metros",
            "• Alinhamento típico: Neutro",
            "• Percepção Natural: Detecta portas secretas",
            "• Gracioso: +1 em Jogadas de Proteção de Destreza",
            "• Arma Racial: +1 de dano com arcos",
            "• Imunidades: Sono e paralisia de ghoul"
        )
        "Anão" -> listOf(
            "• Movimento: 6 metros",
            "• Infravisão: 18 metros",
            "• Alinhamento típico: Neutro",
            "• Mineradores: Detectam elementos de pedra",
            "• Vigoroso: +1 em Jogadas de Proteção de Constituição",
            "• Inimigos: Ataque fácil contra goblins e orcs",
            "• Restrição: Não podem usar armas grandes"
        )
        "Halfling" -> listOf(
            "• Movimento: 6 metros",
            "• Infravisão: Nenhuma",
            "• Alinhamento típico: Neutro",
            "• Furtivos: Habilidade natural para se esconder",
            "• Destemidos: +1 em todas as Jogadas de Proteção",
            "• Bons de Mira: Ataque fácil à distância (10m+)",
            "• Ataque difícil contra gigantes/ogros",
            "• Restrição: Apenas armas pequenas"
        )
        "Gnomo" -> listOf(
            "• Movimento: 6 metros",
            "• Infravisão: 18 metros",
            "• Alinhamento típico: Neutro",
            "• Avaliadores: Detectam diversos elementos",
            "• Sagazes e Vigorosos: +1 em JP de Inteligência e Constituição",
            "• Restrições: Sem armas grandes nem armaduras pesadas"
        )
        "Meio-Elfo" -> listOf(
            "• Movimento: 9 metros",
            "• Infravisão: 9 metros",
            "• Alinhamento típico: Caos",
            "• Aprendizado: +10% de experiência",
            "• Gracioso e Vigoroso: +1 em JP de Destreza e Constituição",
            "• Idiomas Extras: Pode aprender idiomas adicionais",
            "• Imunidades: Sono e veneno"
        )
        else -> listOf("Detalhes não disponíveis")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Detalhes da Raça: $raceName",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            raceDetails.forEach { detail ->
                Text(
                    text = detail,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RaceSelectionPreview() {
}