package com.example.olddragon.controller

import com.example.olddragon.model.*
import kotlin.random.Random

class BattleSimulator {

    private val dado = Dado()

    fun simularTurno(batalha: Batalha): TurnoBatalha {
        val ataques = mutableListOf<ResultadoAtaque>()

        // Ordenar combatentes por iniciativa (maior primeiro)
        val todosVivos = mutableListOf<Combatente>()
        if (batalha.personagem.estaVivo()) {
            todosVivos.add(batalha.personagem)
        }
        todosVivos.addAll(batalha.inimigos.filter { it.estaVivo() })

        val ordemIniciativa = todosVivos.sortedByDescending { it.iniciativa }

        // Cada combatente ataca
        ordemIniciativa.forEach { atacante ->
            if (atacante.estaVivo()) {
                // Escolher alvo
                val alvo = escolherAlvo(atacante, batalha)

                alvo?.let {
                    val resultado = realizarAtaque(atacante, it)
                    ataques.add(resultado)
                }
            }
        }

        return TurnoBatalha(
            numero = batalha.turnoAtual + 1,
            ataques = ataques
        )
    }

    private fun escolherAlvo(atacante: Combatente, batalha: Batalha): Combatente? {
        return when (atacante.tipo) {
            TipoCombatente.PERSONAGEM -> {
                // Personagem ataca inimigos vivos aleatoriamente
                batalha.inimigos.filter { it.estaVivo() }.randomOrNull()
            }
            TipoCombatente.INIMIGO -> {
                // Inimigos atacam o personagem se estiver vivo
                if (batalha.personagem.estaVivo()) {
                    batalha.personagem
                } else {
                    null
                }
            }
        }
    }

    private fun realizarAtaque(atacante: Combatente, defensor: Combatente): ResultadoAtaque {
        // Rolar d20 para ataque
        val rolagemAtaque = rolarD20()
        val totalAtaque = rolagemAtaque + atacante.bonusAtaque

        // Verificar se acertou (total >= CA do defensor)
        val acertou = totalAtaque >= defensor.classeArmadura

        var dano = 0
        if (acertou) {
            // Calcular dano
            dano = calcularDano(atacante.dano)
        }

        // Aplicar dano
        val vidaAntes = defensor.pontosVidaAtuais
        defensor.receberDano(dano)
        val morreu = !defensor.estaVivo()

        return ResultadoAtaque(
            atacante = atacante.nome,
            defensor = defensor.nome,
            rolagem = rolagemAtaque,
            acertou = acertou,
            dano = dano,
            vidaRestante = defensor.pontosVidaAtuais,
            morreu = morreu
        )
    }

    private fun rolarD20(): Int {
        return Random.nextInt(1, 21)
    }

    private fun calcularDano(danoDef: String): Int {
        try {
            // Parse da string de dano
            val partes = danoDef.split(Regex("[+-]"))
            val dadoParte = partes[0]

            // Extrair quantidade de dados e lados
            val dadoInfo = dadoParte.split("d")
            val quantidade = dadoInfo[0].toIntOrNull() ?: 1
            val lados = dadoInfo[1].toIntOrNull() ?: 6

            // Rolar dados
            var total = 0
            repeat(quantidade) {
                total += Random.nextInt(1, lados + 1)
            }

            // Adicionar modificador
            if (partes.size > 1) {
                val modificador = partes[1].toIntOrNull() ?: 0
                total += if (danoDef.contains("-")) -modificador else modificador
            }

            return total.coerceAtLeast(0)
        } catch (e: Exception) {
            return 1 // Dano mínimo em caso de erro
        }
    }

    fun verificarFimBatalha(batalha: Batalha): Boolean {
        val personagemVivo = batalha.personagem.estaVivo()
        val inimigosVivos = batalha.inimigos.any { it.estaVivo() }

        return !personagemVivo || !inimigosVivos
    }

    fun personagemVenceu(batalha: Batalha): Boolean {
        return batalha.personagem.estaVivo() && batalha.inimigos.none { it.estaVivo() }
    }
}