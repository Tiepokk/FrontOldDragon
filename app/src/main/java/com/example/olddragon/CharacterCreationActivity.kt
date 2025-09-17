package com.example.olddragon

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
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

class CharacterCreationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val attributes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("attributes", Atributos::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("attributes") as? Atributos
        }

        val selectedRace = intent.getStringExtra("selectedRace")
        val selectedClass = intent.getStringExtra("selectedClass")

        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CharacterCreationScreen(
                        attributes = attributes,
                        selectedRace = selectedRace,
                        selectedClass = selectedClass,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreationScreen(
    attributes: Atributos?,
    selectedRace: String?,
    selectedClass: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var characterName by remember { mutableStateOf("") }
    var showCharacterSheet by remember { mutableStateOf(false) }

    // CORREÇÃO: Alinhamento baseado na raça (seguindo o sistema Old Dragon)
    val selectedAlignment = when (selectedRace) {
        "Humano" -> listOf("Ordem", "Neutro", "Caos").random() // Alinhamento aleatório
        "Elfo" -> "Neutro"
        "Anão" -> "Neutro"
        "Halfling" -> "Neutro"
        "Gnomo" -> "Neutro"
        "Meio-Elfo" -> "Caos"
        else -> "Neutro"
    }

    // CORREÇÃO: Nível sempre começa em 1
    val characterLevel = 1

    Column(
        modifier = modifier
            .fillMaxSize()
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Botão para finalizar criação
        Button(
            onClick = { showCharacterSheet = true },
            enabled = characterName.isNotBlank(),
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

        // Ficha do personagem
        if (showCharacterSheet) {
            CharacterSheet(
                name = characterName,
                attributes = attributes,
                selectedClass = selectedClass,
                selectedRace = selectedRace,
                alignment = selectedAlignment,
                level = characterLevel
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
fun CharacterSheet(
    name: String,
    attributes: Atributos?,
    selectedClass: String?,
    selectedRace: String?,
    alignment: String,
    level: Int
) {
    Card(
        modifier = Modifier
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
            Text(text = "Nome: $name", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = "Raça: ${selectedRace ?: "Não definida"}", fontSize = 14.sp)
            Text(text = "Classe: ${selectedClass ?: "Não definida"}", fontSize = 14.sp)
            Text(text = "Alinhamento: $alignment", fontSize = 14.sp)
            Text(text = "Nível: $level", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Atributos
            attributes?.let { attr ->
                Text(
                    text = "Atributos",
                    fontSize = 16.sp,
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

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
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
                        text = (attr.forca + attr.destreza + attr.constituicao +
                                attr.inteligencia + attr.sabedoria + attr.carisma).toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCreationPreview() {
}