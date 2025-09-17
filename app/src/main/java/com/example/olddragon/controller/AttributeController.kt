package com.example.olddragon.controller

import com.example.olddragon.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AttributeController {
    private val dado = Dado()

    private val _atributos = MutableStateFlow<Atributos?>(null)
    val atributos: StateFlow<Atributos?> = _atributos

    private val _valoresBrutos = MutableStateFlow<List<Int>>(emptyList())
    val valoresBrutos: StateFlow<List<Int>> = _valoresBrutos

    fun gerarAtributos(metodo: MetodoGeracao) {
        val valores = when (metodo) {
            MetodoGeracao.CLASSICO -> dado.rolarDadosAtributos(false)
            MetodoGeracao.AVENTUREIRO -> dado.rolarDadosAtributos(false)
            MetodoGeracao.HEROICO -> dado.rolarDadosAtributos(true)
        }

        _valoresBrutos.value = valores

        // Se for metodo clássico, distribui automaticamente
        if (metodo == MetodoGeracao.CLASSICO) {
            val escolhedor = AtributosAleatorios()
            _atributos.value = escolhedor.escolherDistribuicao(valores.toMutableList())
        }
    }

    fun distribuirAtributos(distribuicao: Map<String, Int>) {
        _atributos.value = Atributos(
            forca = distribuicao["Força"] ?: 0,
            destreza = distribuicao["Destreza"] ?: 0,
            constituicao = distribuicao["Constituição"] ?: 0,
            inteligencia = distribuicao["Inteligência"] ?: 0,
            sabedoria = distribuicao["Sabedoria"] ?: 0,
            carisma = distribuicao["Carisma"] ?: 0
        )
    }

    fun limparAtributos() {
        _atributos.value = null
        _valoresBrutos.value = emptyList()
    }
}

enum class MetodoGeracao {
    CLASSICO, AVENTUREIRO, HEROICO
}