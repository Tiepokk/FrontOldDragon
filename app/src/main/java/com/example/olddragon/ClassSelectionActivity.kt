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

class ClassSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val attributes = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("attributes", Atributos::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("attributes") as? Atributos
        }
        val selectedRace = intent.getStringExtra("selectedRace")

        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClassSelectionScreen(
                        attributes = attributes,
                        selectedRace = selectedRace,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSelectionScreen(
    attributes: Atributos?,
    selectedRace: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedClass by remember { mutableStateOf<String?>(null) }
    var showClassDetails by remember { mutableStateOf(false) }

    // CORREÇÃO: Apenas as 3 classes solicitadas
    val classes = listOf(
        "Ranger" to "Caçador experiente dos ermos, rastreador e combatente versátil.",
        "Bárbaro" to "Guerreiro selvagem com força primitiva e resistência natural.",
        "Druida" to "Protetor da natureza com poderes mágicos e transformação animal."
    )

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

        classes.forEach { (className, description) ->
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
                            selected = selectedClass == className,
                            onClick = {
                                selectedClass = className
                                showClassDetails = true
                            }
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = className,
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

        // Detalhes da classe selecionada
        if (showClassDetails && selectedClass != null) {
            ClassDetailsCard(className = selectedClass!!)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Continuar
        Button(
            onClick = {
                val intent = Intent(context, CharacterCreationActivity::class.java)
                intent.putExtra("attributes", attributes)
                intent.putExtra("selectedRace", selectedRace)
                intent.putExtra("selectedClass", selectedClass)
                context.startActivity(intent)
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
fun ClassDetailsCard(className: String) {
    val classDetails = when (className) {
        "Ranger" -> listOf(
            "Dado de Vida: d6",
            "Base de Ataque: Lenta",
            "Jogadas de Proteção: Normal",
            "Armas: Pequenas e médias (+grandes no 3º)",
            "Armaduras: Leves",
            "Habilidades:",
            "• Inimigo Mortal (1º nível)",
            "• Combativo (3º nível)",
            "• Previdência (6º nível)",
            "• Companheiro Animal (10º nível)"
        )
        "Bárbaro" -> listOf(
            "Dado de Vida: d10 + Vigor Bárbaro",
            "Base de Ataque: Rápida",
            "Jogadas de Proteção: Normal + JPC",
            "Armas: Todas (sem itens mágicos)",
            "Armaduras: Apenas couro",
            "Habilidades:",
            "• Talentos Selvagens (3º nível)",
            "• Surpresa Selvagem (6º nível)",
            "• Força do Totem (10º nível)"
        )
        "Druida" -> listOf(
            "Dado de Vida: d8",
            "Base de Ataque: Normal",
            "Jogadas de Proteção: Bom",
            "Armas: Sem armas metálicas",
            "Armaduras: Sem armaduras metálicas",
            "Habilidades:",
            "• Herbalismo (1º nível)",
            "• Previdência (3º nível)",
            "• Transformação (6º nível)",
            "• Transformação Melhorada (10º nível)"
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
                text = "Detalhes da Classe: $className",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            classDetails.forEach { detail ->
                Text(
                    text = detail,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassSelectionPreview() {
}