package com.example.olddragon.data

class AtributosAleatorios : EscolherDistribuicao {
    override fun escolherDistribuicao(valoresAtributos: MutableList<Int>): Atributos {
        return Atributos(
            valoresAtributos[0],
            valoresAtributos[1],
            valoresAtributos[2],
            valoresAtributos[3],
            valoresAtributos[4],
            valoresAtributos[5]
        )
    }
}