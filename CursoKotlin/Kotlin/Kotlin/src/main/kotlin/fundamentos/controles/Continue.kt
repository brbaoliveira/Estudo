package org.example.fundamentos.controles

fun main() {
    for (i in 1..10) {
        if (i == 5) {
            continue
        }
        println("Atual: $i")
    }
    println("Fim")
}