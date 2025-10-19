package com.example.olddragon.model

import java.io.Serializable

data class Personagem(
    val id: Long = 0, // Adicionado ID para identificação no banco
    val nome: String,
    val atributos: Atributos,
    val raca: String,
    val classe: String,
    val nivel: Int = 1,
    val alinhamento: String
) : Serializable