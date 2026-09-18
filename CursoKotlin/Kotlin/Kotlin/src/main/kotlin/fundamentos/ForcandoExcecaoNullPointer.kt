package org.example.fundamentos

fun main() {
    val a: Int? = null
    println(a?.inc())

    println("Momento do erro...")
    println(a!!.inc())
}