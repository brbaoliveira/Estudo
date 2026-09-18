package org.example.fundamentos.controles

fun main() {
    val nota: Double = 9.3

    // Usando operador range
    if (nota in 9.0..10.0) {
        println("Fantastico")
    } else if (nota in 7.0..8.0) {
        println("Parabens")
    } else if (nota in 4.0..6.0) {
        println("Tem como recuperar")
    } else if (nota in 0.0..3.0) {
        println("Te vejo no proximo semestre")
    } else {
        println("Nota invalida")
    }
}