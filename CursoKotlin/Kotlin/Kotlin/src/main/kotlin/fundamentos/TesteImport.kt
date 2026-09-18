package org.example.fundamentos

import org.example.fundamentos.pacoteA.simplesFuncao as funcaoSimples
import org.example.fundamentos.pacoteA.Coisa
import org.example.fundamentos.pacoteA.FaceMoeda.CARA
import org.example.fundamentos.pacoteB.*

fun main() {
    kotlin.io.println(funcaoSimples("Ok"))

    val coisa = Coisa("Bola")
    println(coisa.nome)

    println(CARA)

    println("${soma(2, 3)} ${subtracao(4, 6)}")
}