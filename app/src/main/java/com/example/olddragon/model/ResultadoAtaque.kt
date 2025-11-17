package com.example.olddragon.model

import java.io.Serializable

data class ResultadoAtaque(
    val atacante: String,
    val defensor: String,
    val rolagem: Int,
    val acertou: Boolean,
    val dano: Int,
    val vidaRestante: Int,
    val morreu: Boolean
) : Serializable