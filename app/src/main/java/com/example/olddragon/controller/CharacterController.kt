package com.example.olddragon.controller

import com.example.olddragon.data.repository.PersonagemRepository
import com.example.olddragon.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CharacterController(private val repository: PersonagemRepository) {
    private val _personagem = MutableStateFlow<Personagem?>(null)
    val personagem: StateFlow<Personagem?> = _personagem

    private val _personagemSalvo = MutableStateFlow(false)
    val personagemSalvo: StateFlow<Boolean> = _personagemSalvo

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    suspend fun criarPersonagem(
        nome: String,
        atributos: Atributos,
        nomeRaca: String,
        nomeClasse: String
    ) {
        val alinhamento = determinarAlinhamento(nomeRaca)

        _personagem.value = Personagem(
            nome = nome,
            atributos = atributos,
            raca = nomeRaca,
            classe = nomeClasse,
            nivel = 1,
            alinhamento = alinhamento
        )
    }

    suspend fun salvarPersonagem() {
        try {
            _personagem.value?.let { personagem ->
                // Verificar se já existe personagem com mesmo nome
                val existente = repository.obterPorNome(personagem.nome)
                if (existente != null) {
                    _mensagemErro.value = "Já existe um personagem com esse nome!"
                    return
                }

                repository.inserir(personagem)
                _personagemSalvo.value = true
                _mensagemErro.value = null
            }
        } catch (e: Exception) {
            _mensagemErro.value = "Erro ao salvar personagem: ${e.message}"
            _personagemSalvo.value = false
        }
    }

    fun carregarPersonagens() = repository.todosPersonagens

    suspend fun deletarPersonagem(id: Long) {
        try {
            repository.deletarPorId(id)
            _mensagemErro.value = null
        } catch (e: Exception) {
            _mensagemErro.value = "Erro ao deletar personagem: ${e.message}"
        }
    }

    private fun determinarAlinhamento(nomeRaca: String): String {
        return when (nomeRaca) {
            "Humano" -> listOf("Ordem", "Neutro", "Caos").random()
            "Meio-Elfo" -> "Caos"
            else -> "Neutro"
        }
    }

    fun calcularTotalAtributos(atributos: Atributos): Int {
        return atributos.forca + atributos.destreza + atributos.constituicao +
                atributos.inteligencia + atributos.sabedoria + atributos.carisma
    }

    fun limparPersonagem() {
        _personagem.value = null
        _personagemSalvo.value = false
        _mensagemErro.value = null
    }

    fun limparMensagemErro() {
        _mensagemErro.value = null
    }
}