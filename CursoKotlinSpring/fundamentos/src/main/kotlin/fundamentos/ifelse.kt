package org.example.fundamentos

fun main() {
    /*parOuImpar(2)
    parOuImpar(3)*/

    /*resultadoNota(3)
    resultadoNota(5)
    resultadoNota(8)*/
    println(resultadoNota(3))
    println(resultadoNota(5))
    println(resultadoNota(8))
}

fun parOuImpar(numero: Int) {
    if (numero % 2 == 0)
        println("Par")
    else
        println("Impar")
}

/*
fun resultadoNota(nota: Int) {
    if (nota > 6)
        println("Passou")
    else if (nota >= 4)
        println("Recuperação")
    else
        println("Reprovou")
}*/
/*
fun resultadoNota(nota: Int) {
    return println(
        if (nota > 6)
            "Passou"
        else if (nota >= 4)
            "Recuperação"
        else
            "Reprovou"
    )
}*/
fun resultadoNota(nota: Int): String {
    return if (nota > 6)
        "Passou"
    else if (nota >= 4)
        "Recuperação"
    else
        "Reprovou"
}