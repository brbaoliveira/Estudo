package org.example.fundamentos

fun main() {
    var nomeVar = "Gustavo"
    val nomeVal = "Gustavo"

    nomeVar = "Daniel" // Permite alteração
   // nomeVal = "Daniel" // Não permite alteração
}

//dentro da classe é necessario inicializar as variaveis
class variaveis {
    var teste: String = ""

    //lateinit significa que sera inserida depois
    lateinit var testeLateinit: String
}