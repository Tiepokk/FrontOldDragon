package com.example.olddragon.model

import java.io.Serializable

data class Atributos(
    var forca: Int,
    var destreza: Int,
    var constituicao: Int,
    var inteligencia: Int,
    var sabedoria: Int,
    var carisma: Int
) : Serializable