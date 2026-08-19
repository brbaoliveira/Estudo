package org.example.fundamentos

fun main() {
    val x = 16

    /*when(x) {
        //5 -> println("x == 5")
        //duas validacoes, 5 OU -5
        5, -5 -> println("x == 5")
        8 -> println("x == 8")
        9 -> println("x == 9")
        10 -> {
            println("x == 10")
            println("x é uma dezena")
        }
        // pode usar Ranges
        in 11 .. 15 -> println("x esta entre 11 e 15")
        // negacao Ranges
        !in 16 .. 20 -> println("x nao esta no range de 16 a 20")
        else -> println("Numero nao mapeado")
    }*/

    //println(comecaComOi("oi, tudo bem?"))
    when {
        comecaComOi(5) -> println("5")
        comecaComOi("oi, tudo bem?") -> println("oi, tudo bem?")
    }
}

// Any aceita valores de qualquer tipo, seja String, Int, Boolean...
fun comecaComOi(x: Any): Boolean{
    return when(x) {
        is String -> x.startsWith("oi")
        else -> false
    }
}