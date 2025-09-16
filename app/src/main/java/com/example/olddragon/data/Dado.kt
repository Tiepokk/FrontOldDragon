package com.example.olddragon.data

import java.util.Random

class Dado(private val random: Random = Random()) {

    fun rolar(quantidadeDados: Int, quantidadeLados: Int) : MutableList<Int> {
        val valoresDados = mutableListOf<Int>()

        for (i in 0 until quantidadeDados) {
            valoresDados.add(random.nextInt(quantidadeLados) + 1)
        }
        return valoresDados
    }

    fun rolarDadosAtributos(isHeroico: Boolean = false) : MutableList<Int> {
        val listaAtributos: MutableList<Int> = mutableListOf()
        repeat(6){
            if (isHeroico) {
                var valoresDados = mutableListOf<Int>()
                valoresDados = rolar(4, 6)
                valoresDados.remove(valoresDados.minOrNull())
                listaAtributos.add(valoresDados.sum())
            } else {
                listaAtributos.add(rolar(3, 6).sum())
            }
        }
        return listaAtributos
    }
}