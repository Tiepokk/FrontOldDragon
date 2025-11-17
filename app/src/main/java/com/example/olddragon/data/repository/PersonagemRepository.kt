package com.example.olddragon.data.repository

import com.example.olddragon.data.dao.PersonagemDao
import com.example.olddragon.data.entity.PersonagemEntity
import com.example.olddragon.model.Personagem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonagemRepository(private val personagemDao: PersonagemDao) {

    val todosPersonagens: Flow<List<Personagem>> = personagemDao.obterTodos()
        .map { entities -> entities.map { it.toPersonagem() } }

    suspend fun inserir(personagem: Personagem): Long {
        val entity = PersonagemEntity.fromPersonagem(personagem)
        return personagemDao.inserir(entity)
    }

    suspend fun atualizar(personagem: Personagem) {
        val entity = PersonagemEntity.fromPersonagem(personagem, personagem.id)
        personagemDao.atualizar(entity)
    }

    suspend fun deletar(personagem: Personagem) {
        val entity = PersonagemEntity.fromPersonagem(personagem, personagem.id)
        personagemDao.deletar(entity)
    }

    suspend fun deletarPorId(id: Long) {
        personagemDao.deletarPorId(id)
    }

    suspend fun obterPorId(id: Long): Personagem? {
        return personagemDao.obterPorId(id)?.toPersonagem()
    }

    suspend fun obterPorNome(nome: String): Personagem? {
        return personagemDao.obterPorNome(nome)?.toPersonagem()
    }

    fun obterPorClasse(classe: String): Flow<List<Personagem>> {
        return personagemDao.obterPorClasse(classe)
            .map { entities -> entities.map { it.toPersonagem() } }
    }

    fun obterPorRaca(raca: String): Flow<List<Personagem>> {
        return personagemDao.obterPorRaca(raca)
            .map { entities -> entities.map { it.toPersonagem() } }
    }

    suspend fun contarPersonagens(): Int {
        return personagemDao.contarPersonagens()
    }
}