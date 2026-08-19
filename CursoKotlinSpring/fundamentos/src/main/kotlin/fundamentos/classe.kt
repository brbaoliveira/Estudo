package org.example.fundamentos

/*data class Pessoa(var nome: String, var idade: Int) {
}//retorno: Pessoa(nome=Gustavo", idade=24)*/

/*class Pessoa(var nome: String, var idade: Int) {
}//retorno: org.example.fundamentos.Pessoa@30f39991*/

class Pessoa(var nome: String, var idade: Int) {
    override fun toString(): String {
        return "Classe Pessoa - Nome: ${nome}, Idade: ${idade}"
    }
}//retorno: Classe Pessoa - Nome: Gustavo, Idade: 24

fun main() {
    var gustavo = Pessoa("Gustavo", 24)
    println(gustavo)
}
