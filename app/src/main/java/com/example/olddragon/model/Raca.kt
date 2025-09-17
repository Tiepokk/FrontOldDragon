package com.example.olddragon.model

data class Raca(
    val nome: String,
    val descricao: String,
    val detalhes: List<String>,
    val movimento: String,
    val infravisao: String,
    val alinhamentoTipico: String
) {
    companion object {
        fun obterTodasRacas(): List<Raca> = listOf(
            Raca(
                nome = "Humano",
                descricao = "Versáteis e adaptáveis, podem ter qualquer alinhamento.",
                detalhes = listOf(
                    "• Movimento: 9 metros",
                    "• Infravisão: Nenhuma",
                    "• Alinhamento: Aleatório (Ordem/Neutro/Caos)",
                    "• Aprendizado: +10% de experiência",
                    "• Adaptabilidade: +1 em todas as Jogadas de Proteção",
                    "• Nenhuma restrição de equipamento"
                ),
                movimento = "9 metros",
                infravisao = "Nenhuma",
                alinhamentoTipico = "Aleatório"
            ),
            Raca(
                nome = "Elfo",
                descricao = "Seres graciosos com infravisão e resistências naturais.",
                detalhes = listOf(
                    "• Movimento: 9 metros",
                    "• Infravisão: 18 metros",
                    "• Alinhamento típico: Neutro",
                    "• Percepção Natural: Detecta portas secretas",
                    "• Gracioso: +1 em Jogadas de Proteção de Destreza",
                    "• Arma Racial: +1 de dano com arcos",
                    "• Imunidades: Sono e paralisia de ghoul"
                ),
                movimento = "9 metros",
                infravisao = "18 metros",
                alinhamentoTipico = "Neutro"
            ),
            Raca(
                nome = "Anão",
                descricao = "Vigorosos mineradores com conhecimento sobre pedras.",
                detalhes = listOf(
                    "• Movimento: 6 metros",
                    "• Infravisão: 18 metros",
                    "• Alinhamento típico: Neutro",
                    "• Mineradores: Detectam elementos de pedra",
                    "• Vigoroso: +1 em Jogadas de Proteção de Constituição",
                    "• Inimigos: Ataque fácil contra goblins e orcs",
                    "• Restrição: Não podem usar armas grandes"
                ),
                movimento = "6 metros",
                infravisao = "18 metros",
                alinhamentoTipico = "Neutro"
            ),
            Raca(
                nome = "Halfling",
                descricao = "Pequenos e destemidos, excelentes em furtividade.",
                detalhes = listOf(
                    "• Movimento: 6 metros",
                    "• Infravisão: Nenhuma",
                    "• Alinhamento típico: Neutro",
                    "• Furtivos: Habilidade natural para se esconder",
                    "• Destemidos: +1 em todas as Jogadas de Proteção",
                    "• Bons de Mira: Ataque fácil à distância (10m+)",
                    "• Ataque difícil contra gigantes/ogros",
                    "• Restrição: Apenas armas pequenas"
                ),
                movimento = "6 metros",
                infravisao = "Nenhuma",
                alinhamentoTipico = "Neutro"
            ),
            Raca(
                nome = "Gnomo",
                descricao = "Sagazes avaliadores com habilidades mágicas naturais.",
                detalhes = listOf(
                    "• Movimento: 6 metros",
                    "• Infravisão: 18 metros",
                    "• Alinhamento típico: Neutro",
                    "• Avaliadores: Detectam diversos elementos",
                    "• Sagazes e Vigorosos: +1 em JP de Inteligência e Constituição",
                    "• Restrições: Sem armas grandes nem armaduras pesadas"
                ),
                movimento = "6 metros",
                infravisao = "18 metros",
                alinhamentoTipico = "Neutro"
            ),
            Raca(
                nome = "Meio-Elfo",
                descricao = "Combinam versatilidade humana com graça élfica.",
                detalhes = listOf(
                    "• Movimento: 9 metros",
                    "• Infravisão: 9 metros",
                    "• Alinhamento típico: Caos",
                    "• Aprendizado: +10% de experiência",
                    "• Gracioso e Vigoroso: +1 em JP de Destreza e Constituição",
                    "• Idiomas Extras: Pode aprender idiomas adicionais",
                    "• Imunidades: Sono e veneno"
                ),
                movimento = "9 metros",
                infravisao = "9 metros",
                alinhamentoTipico = "Caos"
            )
        )

        fun obterRacaPorNome(nome: String): Raca? = obterTodasRacas().find { it.nome == nome }
    }
}