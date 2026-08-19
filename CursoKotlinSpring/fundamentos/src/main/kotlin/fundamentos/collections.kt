package org.example.fundamentos

fun main() {
    //LISTA IMAUTAVEL
    /*var lista = listOf(1, 2, 3, 4, 6)
    //val pares = lista.filter { it % 2 == 0 } // Filtra os valores pares
    val pares = lista.filter { it % 2 == 0 }.first() // Filtra o primeiro valor par

    println(pares)

    // For percorrendo a lista e printando os valores de cada posicao
    lista.forEach {
        println(it)
    }
    // Outro for mas com o mesmo resultado
    for (numero in lista)
        println(numero)

    // printa o valor da posicao 0
    println(lista[0])
    println(lista.get(0))
    // printa o tamanho da lista
    println(lista.size)
    // printa o indice/posicao referente ao valor
    println(lista.indexOf(6))*/


    //LISTA MUTAVEL
    var lista = mutableListOf(1, 2, 3, 4, 6)
    println(lista)
    // Adiciona elementos
    lista.add(8)
    println(lista)
    // Remove elementos pela posicao
    lista.removeAt(0)
    println(lista)
    // Remove elementos pelo proprio elemento
    lista.remove(8)
    println(lista)

    // subscreve o elemento na posicao 0 novamente, ex: antes lista[0] == 2 depois fica lista[0] == 20
    lista[0] = 20
    println(lista)

    // Ordena a lista do manor para o maior, se for lista de strings ordena em ordem alfabetica
    lista.sort()
    println(lista)

    // Embaralha/bagunça a lista
    lista.shuffle()
    println(lista)

    //SET
    // Set é uma lista que nao recebe valores duplicados
    var setNumeros = setOf(1,2,3,4,2) // retorna [1, 2, 3, 4]
    println(setNumeros)

    //MAP
    var mapNomes = mutableMapOf("Gustavo" to 24, "Daniel" to 20)
    println(mapNomes)
    // Adicionar valores
    mapNomes.put("Bruno", 30)
    println(mapNomes)
    // Alterar valores
    mapNomes["Bruno"] = 35
    println(mapNomes)
    // Remover valores, sempre pela chave
    mapNomes.remove("Bruno")
    println(mapNomes)
    // Nao aceita chaves iguais, ele subscreve o valor se a chave ja existir, mas nesse caso verifica se existe, se existir ele ignora
    mapNomes.putIfAbsent("Gustavo", 28)
    println(mapNomes)

}