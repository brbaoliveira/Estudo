package org.example.fundamentos

class Carro(var cor: String, val anoFabricacao: Int, var dono: Dono) {
}

class Dono(var nome: String, var idade: Int) {
}

fun main() {
    var carro = Carro("Branco", 2021, Dono("Gustavo", 24))

    carro.cor = "Preto"
    println(carro.cor)

    carro.dono.nome = "Daniel"
    println(carro.dono.nome)
}