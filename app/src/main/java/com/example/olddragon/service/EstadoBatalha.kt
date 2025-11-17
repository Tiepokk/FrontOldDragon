package com.example.olddragon.service

import com.example.olddragon.model.Batalha

sealed class EstadoBatalha {
    object Aguardando : EstadoBatalha()
    data class EmAndamento(val batalha: Batalha) : EstadoBatalha()
    data class Finalizada(val batalha: Batalha, val vitoria: Boolean) : EstadoBatalha()
    object Cancelada : EstadoBatalha()
}