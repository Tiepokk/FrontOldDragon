package com.example.olddragon.model

object InimigoFactory {

    fun criarGoblin(): Combatente {
        return Combatente(
            nome = "Goblin",
            pontosVidaMaximos = 7,
            pontosVidaAtuais = 7,
            classeArmadura = 15,
            bonusAtaque = 4,
            dano = "1d6+2",
            iniciativa = 2,
            tipo = TipoCombatente.INIMIGO
        )
    }

    fun criarOrc(): Combatente {
        return Combatente(
            nome = "Orc",
            pontosVidaMaximos = 15,
            pontosVidaAtuais = 15,
            classeArmadura = 13,
            bonusAtaque = 5,
            dano = "1d12+3",
            iniciativa = 1,
            tipo = TipoCombatente.INIMIGO
        )
    }

    fun criarEsqueleto(): Combatente {
        return Combatente(
            nome = "Esqueleto",
            pontosVidaMaximos = 13,
            pontosVidaAtuais = 13,
            classeArmadura = 13,
            bonusAtaque = 4,
            dano = "1d6+2",
            iniciativa = 2,
            tipo = TipoCombatente.INIMIGO
        )
    }

    fun criarLobo(): Combatente {
        return Combatente(
            nome = "Lobo",
            pontosVidaMaximos = 11,
            pontosVidaAtuais = 11,
            classeArmadura = 13,
            bonusAtaque = 4,
            dano = "2d4+2",
            iniciativa = 3,
            tipo = TipoCombatente.INIMIGO
        )
    }

    fun obterInimigosDisponiveis(): List<String> {
        return listOf("Goblin", "Orc", "Esqueleto", "Lobo")
    }

    fun criarInimigoPorNome(nome: String): Combatente {
        return when (nome) {
            "Goblin" -> criarGoblin()
            "Orc" -> criarOrc()
            "Esqueleto" -> criarEsqueleto()
            "Lobo" -> criarLobo()
            else -> criarGoblin()
        }
    }
}