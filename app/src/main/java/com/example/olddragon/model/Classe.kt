package com.example.olddragon.model

data class Classe(
    val nome: String,
    val descricao: String,
    val detalhes: List<String>,
    val dadoVida: String,
    val baseAtaque: String,
    val jogadasProtecao: String
) {
    companion object {
        fun obterTodasClasses(): List<Classe> = listOf(
            Classe(
                nome = "Ranger",
                descricao = "Caçador experiente dos ermos, rastreador e combatente versátil.",
                detalhes = listOf(
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
                ),
                dadoVida = "d6",
                baseAtaque = "Lenta",
                jogadasProtecao = "Normal"
            ),
            Classe(
                nome = "Bárbaro",
                descricao = "Guerreiro selvagem com força primitiva e resistência natural.",
                detalhes = listOf(
                    "Dado de Vida: d10 + Vigor Bárbaro",
                    "Base de Ataque: Rápida",
                    "Jogadas de Proteção: Normal + JPC",
                    "Armas: Todas (sem itens mágicos)",
                    "Armaduras: Apenas couro",
                    "Habilidades:",
                    "• Talentos Selvagens (3º nível)",
                    "• Surpresa Selvagem (6º nível)",
                    "• Força do Totem (10º nível)"
                ),
                dadoVida = "d10",
                baseAtaque = "Rápida",
                jogadasProtecao = "Normal + JPC"
            ),
            Classe(
                nome = "Druida",
                descricao = "Protetor da natureza com poderes mágicos e transformação animal.",
                detalhes = listOf(
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
                ),
                dadoVida = "d8",
                baseAtaque = "Normal",
                jogadasProtecao = "Bom"
            )
        )

        fun obterClassePorNome(nome: String): Classe? = obterTodasClasses().find { it.nome == nome }
    }
}