package com.example.olddragon.controller

import com.example.olddragon.model.Raca
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RaceController {
    private val _racasSelecionaveis = MutableStateFlow(Raca.obterTodasRacas())
    val racasSelecionaveis: StateFlow<List<Raca>> = _racasSelecionaveis

    private val _racaSelecionada = MutableStateFlow<Raca?>(null)
    val racaSelecionada: StateFlow<Raca?> = _racaSelecionada

    fun selecionarRaca(nomeRaca: String) {
        _racaSelecionada.value = Raca.obterRacaPorNome(nomeRaca)
    }

    fun obterDetalhesRaca(nomeRaca: String): List<String> {
        return Raca.obterRacaPorNome(nomeRaca)?.detalhes ?: emptyList()
    }

    fun limparSelecao() {
        _racaSelecionada.value = null
    }
}