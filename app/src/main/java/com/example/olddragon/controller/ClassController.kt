package com.example.olddragon.controller

import com.example.olddragon.model.Classe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClassController {
    private val _classesSelecionaveis = MutableStateFlow(Classe.obterTodasClasses())
    val classesSelecionaveis: StateFlow<List<Classe>> = _classesSelecionaveis

    private val _classeSelecionada = MutableStateFlow<Classe?>(null)
    val classeSelecionada: StateFlow<Classe?> = _classeSelecionada

    fun selecionarClasse(nomeClasse: String) {
        _classeSelecionada.value = Classe.obterClassePorNome(nomeClasse)
    }

    fun obterDetalhesClasse(nomeClasse: String): List<String> {
        return Classe.obterClassePorNome(nomeClasse)?.detalhes ?: emptyList()
    }

    fun limparSelecao() {
        _classeSelecionada.value = null
    }
}