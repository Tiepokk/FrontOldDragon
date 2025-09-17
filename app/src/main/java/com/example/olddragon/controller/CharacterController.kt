package com.example.olddragon.controller

import com.example.olddragon.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CharacterController {
    private val _personagem = MutableStateFlow<Personagem?>(null)
    val personagem: StateFlow<Personagem?> = _personagem

    fun criarPersonagem(
        nome: String,
        atributos: Atributos,
        nomeRaca: String,
        nomeClasse: String
    ) {
        val alinhamento = determinarAlinhamento(nomeRaca)

        _personagem.value = Personagem(
            nome = nome,
            atributos = atributos,
            raca = nomeRaca,
            classe = nomeClasse,
            nivel = 1,
            alinhamento = alinhamento
        )
    }

    private fun determinarAlinhamento(nomeRaca: String): String {
        return when (nomeRaca) {
            "Humano" -> listOf("Ordem", "Neutro", "Caos").random()
            "Meio-Elfo" -> "Caos"
            else -> "Neutro"
        }
    }

    fun calcularTotalAtributos(atributos: Atributos): Int {
        return atributos.forca + atributos.destreza + atributos.constituicao +
                atributos.inteligencia + atributos.sabedoria + atributos.carisma
    }

    fun limparPersonagem() {
        _personagem.value = null
    }
}