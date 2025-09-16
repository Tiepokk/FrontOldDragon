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
import com.example.olddragon.data.*
import com.example.olddragon.ui.theme.OldDragonTheme

class AttributeGeneratorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OldDragonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AttributeGeneratorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributeGeneratorScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf("Clássico") }
    var selectedDistribution by remember { mutableStateOf("Aleatório") }
    var generatedAttributes by remember { mutableStateOf<Atributos?>(null) }
    var rawValues by remember { mutableStateOf<List<Int>>(emptyList()) }

    val methods = listOf("Clássico", "Aventureiro", "Heroico")
    val distributions = listOf("Aleatório", "Escolher")

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

        // Seleção de Distribuição
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Distribuição de Atributos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Aleatório: Valores distribuídos automaticamente\nEscolher: Você escolhe onde colocar cada valor",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                distributions.forEach { distribution ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedDistribution == distribution,
                            onClick = { selectedDistribution = distribution }
                        )
                        Text(
                            text = distribution,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Botão Gerar
        Button(
            onClick = {
                val dados = Dado()
                val valores = when (selectedMethod) {
                    "Heroico" -> dados.rolarDadosAtributos(true)
                    else -> dados.rolarDadosAtributos(false)
                }

                rawValues = valores

                if (selectedDistribution == "Escolher") {
                    val intent = Intent(context, AttributeSelectionActivity::class.java)
                    intent.putIntegerArrayListExtra("values", ArrayList(valores))
                    context.startActivity(intent)
                } else {
                    val escolhedor = AtributosAleatorios()
                    generatedAttributes = escolhedor.escolherDistribuicao(valores.toMutableList())
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

        // Exibir valores brutos
        if (rawValues.isNotEmpty() && selectedDistribution == "Aleatório") {
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
                        text = rawValues.sorted().reversed().joinToString(", "),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Soma total: ${rawValues.sum()}",
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Exibir Atributos Gerados
        generatedAttributes?.let { attributes ->
            AttributeDisplay(attributes = attributes)
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
fun AttributeDisplay(attributes: Atributos) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Atributos Gerados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val atributos = listOf(
                "Força" to attributes.forca,
                "Destreza" to attributes.destreza,
                "Constituição" to attributes.constituicao,
                "Inteligência" to attributes.inteligencia,
                "Sabedoria" to attributes.sabedoria,
                "Carisma" to attributes.carisma
            )

            atributos.forEach { (nome, valor) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$nome:",
                        fontSize = 16.sp
                    )
                    Text(
                        text = valor.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = (attributes.forca + attributes.destreza + attributes.constituicao +
                            attributes.inteligencia + attributes.sabedoria + attributes.carisma).toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttributeGeneratorPreview() {
    OldDragonTheme {
        AttributeGeneratorScreen()
    }
}