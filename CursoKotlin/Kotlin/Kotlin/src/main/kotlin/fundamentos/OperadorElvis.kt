package org.example.fundamentos

fun main() {
    val opcional: String? = null
    val obrigatorio: String = opcional ?: "Valor Padrão"
    println(obrigatorio)
}