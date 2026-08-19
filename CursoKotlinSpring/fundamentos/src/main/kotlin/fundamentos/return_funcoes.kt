package org.example.fundamentos

fun retornaNumeroPorExtenso(numero: Int): String {
    if (numero == 5) return "Cinco"
    else if (numero == 6) return "Seis"
    else return "Numero não mapeado"
}

fun main() {
    println(retornaNumeroPorExtenso(4))
    println(retornaNumeroPorExtenso(5))
    println(retornaNumeroPorExtenso(6))
}