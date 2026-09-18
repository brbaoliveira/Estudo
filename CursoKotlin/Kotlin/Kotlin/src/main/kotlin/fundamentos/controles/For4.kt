package org.example.fundamentos.controles

fun main() {
    val alunos = arrayListOf("André", "Carla", "Marcos")
    for ((indice, aluno) in alunos.withIndex()) {
        println("${indice + 1} = $aluno")
    }
}