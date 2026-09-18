package org.example.fundamentos.controles

fun main() {
    val nota = 10

    // Usando operador range
    when (nota) {
        10,9 -> println("Fantastico")
        8, 7 -> println("Parabens")
        6, 5, 4 -> println("Tem como recuperar")
        in 0..3 -> println("Tem como recuperar")
        else  -> println("Nota ivalida")
    }
}