package com.example.olddragon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.olddragon.model.Atributos
import com.example.olddragon.model.Personagem

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val raca: String,
    val classe: String,
    val nivel: Int,
    val alinhamento: String,
    // Atributos
    val forca: Int,
    val destreza: Int,
    val constituicao: Int,
    val inteligencia: Int,
    val sabedoria: Int,
    val carisma: Int,
    val dataCriacao: Long = System.currentTimeMillis()
) {
    fun toPersonagem(): Personagem {
        return Personagem(
            id = id,
            nome = nome,
            atributos = Atributos(
                forca = forca,
                destreza = destreza,
                constituicao = constituicao,
                inteligencia = inteligencia,
                sabedoria = sabedoria,
                carisma = carisma
            ),
            raca = raca,
            classe = classe,
            nivel = nivel,
            alinhamento = alinhamento
        )
    }

    companion object {
        fun fromPersonagem(personagem: Personagem, id: Long = 0): PersonagemEntity {
            return PersonagemEntity(
                id = id,
                nome = personagem.nome,
                raca = personagem.raca,
                classe = personagem.classe,
                nivel = personagem.nivel,
                alinhamento = personagem.alinhamento,
                forca = personagem.atributos.forca,
                destreza = personagem.atributos.destreza,
                constituicao = personagem.atributos.constituicao,
                inteligencia = personagem.atributos.inteligencia,
                sabedoria = personagem.atributos.sabedoria,
                carisma = personagem.atributos.carisma
            )
        }
    }
}