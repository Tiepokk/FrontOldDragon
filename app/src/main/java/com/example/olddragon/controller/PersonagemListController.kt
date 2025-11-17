package com.example.olddragon.controller

import com.example.olddragon.data.repository.PersonagemRepository
import com.example.olddragon.model.Personagem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonagemListController(private val repository: PersonagemRepository) {

    private val _personagens = MutableStateFlow<List<Personagem>>(emptyList())
    val personagens: StateFlow<List<Personagem>> = _personagens

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    init {
        carregarPersonagens()
    }

    private fun carregarPersonagens() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.todosPersonagens.collect { lista ->
                _personagens.value = lista
            }
        }
    }

    suspend fun deletarPersonagem(id: Long) {
        try {
            repository.deletarPorId(id)
            _mensagemErro.value = null
        } catch (e: Exception) {
            _mensagemErro.value = "Erro ao deletar personagem: ${e.message}"
        }
    }

    suspend fun obterPersonagemPorId(id: Long): Personagem? {
        return try {
            repository.obterPorId(id)
        } catch (e: Exception) {
            _mensagemErro.value = "Erro ao buscar personagem: ${e.message}"
            null
        }
    }

    fun limparMensagemErro() {
        _mensagemErro.value = null
    }
}