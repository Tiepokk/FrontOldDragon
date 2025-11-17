package com.example.olddragon.model

import java.io.Serializable

data class Combatente(
    val nome: String,
    val pontosVidaMaximos: Int,
    var pontosVidaAtuais: Int,
    val classeArmadura: Int,
    val bonusAtaque: Int,
    val dano: String, // Ex: "1d8+2"
    val iniciativa: Int,
    val tipo: TipoCombatente
) : Serializable {
    fun estaVivo(): Boolean = pontosVidaAtuais > 0

    fun receberDano(dano: Int) {
        pontosVidaAtuais = (pontosVidaAtuais - dano).coerceAtLeast(0)
    }
}

enum class TipoCombatente {
    PERSONAGEM,
    INIMIGO
}