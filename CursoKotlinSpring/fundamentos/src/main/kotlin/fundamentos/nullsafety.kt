package org.example.fundamentos

fun main() {
    //var nome: String = null //ERRADO: nao é permitido colocar um valor nulo em uma variavel não nula
    //var nome: String? = null //CERTO: agora ela pode receber valores nulos


    /*var nome: String? = null
    // forma antiga ainda é aceita
    if (nome != null)
        println(nome.length)

    // nova forma: so executa caso a variavel nome nao seja nula, se for nula ele printa "null"
    println(nome?.length)
    // tambem pode ter mais de um
    println(nome?.length?.toShort())


    // esta garantindo que a variavel nunca sera nula
    val toShort = nome!!.length.toShort()*/

    //Em lista
    // Pode conter valores nulos mas NÃO pode ser nula
    var lista: List<Int?> = listOf(1, 2, null, 3)
    // Pode ser nula
    var listaNullable: List<Int>? = null
    // Pode ser nula e pode conter valores nulos
    var listaNullableItens: List<Int?>? = null


    //elvs
    var nome: String? = null/*"Gustavo"*/

    //var tamanho: Int = nome != null ? nome.length : 0 // No java
    var tamanho: Int = nome?.length ?: 0 // Em elvs
    println(tamanho)
}