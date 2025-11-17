package com.example.olddragon.model

import java.io.Serializable

data class TurnoBatalha(
    val numero: Int,
    val ataques: List<ResultadoAtaque>,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable