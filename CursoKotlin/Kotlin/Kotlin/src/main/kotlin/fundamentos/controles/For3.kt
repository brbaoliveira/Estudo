package org.example.fundamentos.controles

fun main() {
    for (i in 0..100 step 5) {
        println("i = $i")
    }

    for (i in 0 downTo 100 step 5) {
        println("i = $i")
    }
}