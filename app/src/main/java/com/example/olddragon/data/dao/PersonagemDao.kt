package com.example.olddragon.data.dao

import androidx.room.*
import com.example.olddragon.data.entity.PersonagemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonagemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(personagem: PersonagemEntity): Long

    @Update
    suspend fun atualizar(personagem: PersonagemEntity)

    @Delete
    suspend fun deletar(personagem: PersonagemEntity)

    @Query("SELECT * FROM personagens ORDER BY dataCriacao DESC")
    fun obterTodos(): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE id = :id")
    suspend fun obterPorId(id: Long): PersonagemEntity?

    @Query("SELECT * FROM personagens WHERE nome = :nome")
    suspend fun obterPorNome(nome: String): PersonagemEntity?

    @Query("SELECT * FROM personagens WHERE classe = :classe")
    fun obterPorClasse(classe: String): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE raca = :raca")
    fun obterPorRaca(raca: String): Flow<List<PersonagemEntity>>

    @Query("DELETE FROM personagens WHERE id = :id")
    suspend fun deletarPorId(id: Long)

    @Query("SELECT COUNT(*) FROM personagens")
    suspend fun contarPersonagens(): Int
}