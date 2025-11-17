package com.example.olddragon.model

import java.io.Serializable

data class Batalha(
    val id: Long = System.currentTimeMillis(),
    val personagem: Combatente,
    val inimigos: List<Combatente>,
    val turnos: MutableList<TurnoBatalha> = mutableListOf(),
    var turnoAtual: Int = 0,
    var finalizada: Boolean = false,
    var vitoria: Boolean = false
) : Serializable {

    fun adicionarTurno(turno: TurnoBatalha) {
        turnos.add(turno)
        turnoAtual++
    }

    fun finalizarBatalha(vitoria: Boolean) {
        finalizada = true
        this.vitoria = vitoria
    }
}

fun Personagem.toCombatente(): Combatente {
    // Cálculo de pontos de vida baseado na constituição
    val modificadorConstituicao = (this.atributos.constituicao - 10) / 2
    val pontosVida = 8 + modificadorConstituicao // d8 base + modificador

    // Cálculo de CA (Classe de Armadura)
    val modificadorDestreza = (this.atributos.destreza - 10) / 2
    val ca = 10 + modificadorDestreza // CA base + modificador de destreza

    // Cálculo de bônus de ataque
    val modificadorForca = (this.atributos.forca - 10) / 2
    val bonusAtaque = modificadorForca + 1 // modificador + nível

    // Dano (1d8 + modificador de força)
    val dano = if (modificadorForca >= 0) {
        "1d8+$modificadorForca"
    } else {
        "1d8$modificadorForca"
    }

    // Iniciativa (modificador de destreza)
    val iniciativa = modificadorDestreza

    return Combatente(
        nome = this.nome,
        pontosVidaMaximos = pontosVida.coerceAtLeast(1),
        pontosVidaAtuais = pontosVida.coerceAtLeast(1),
        classeArmadura = ca,
        bonusAtaque = bonusAtaque,
        dano = dano,
        iniciativa = iniciativa,
        tipo = TipoCombatente.PERSONAGEM
    )
}