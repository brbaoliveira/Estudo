package org.example.fundamentos

fun main() {
    //FOR
    //printa1a10()
    //printa10a1()
    //printaPar1a10()
    //printaRange(10, 20)

    //WHILE
    //whileMenorQue10()
    doWhileMenorQue10()
}

fun whileMenorQue10(){
    var x = 0
    while (x < 10) {
        println(x)
        x++
    }
}

fun doWhileMenorQue10(){
    var x = 0
    do {
        println(x)
        x++
    } while (x < 10)
}

//FOR
fun printa1a10(){
    for (numero in 1 .. 10)
        println(numero)
}

fun printa10a1(){
    for (numero in 10 downTo 1)
        println(numero)
}

fun printaPar1a10(){
    for (numero in 2 .. 10 step 2)
        println(numero)
}

fun printaRange(inicio: Int, fim: Int){
    for (numero in inicio .. fim)
        println(numero)
}