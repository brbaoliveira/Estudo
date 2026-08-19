package org.example.fundamentos

fun main() {
    //Tomar bastante cuidadd com !!, pois se for nula o programa para e retorna um Exception
    //var pessoa: Pessoa? = Pessoa( "Gustavo", 24)
    var pessoa: Pessoa? = null
    println(pessoa!!.nome)
}