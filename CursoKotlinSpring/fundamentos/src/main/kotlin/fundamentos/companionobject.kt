package org.example.fundamentos

class MinhaClasse(var nome: String, var endereco: String, var iadade: Int) {
    companion object {
        fun criaClasseComValoresPadrao(): MinhaClasse {
            return MinhaClasse("Gustavo", "Rua Teste", 24)
        }
    }
}

class SegundaClasse (var nome: String, var endereco: String, var iadade: Int) {
    fun criaClasseComValoresPadrao(): SegundaClasse {
        return SegundaClasse("Gustavo", "Rua Teste", 24)
    }
}

fun main(){
    var segundaClasse = SegundaClasse("Gustavo", "Rua Teste", 24).criaClasseComValoresPadrao()
    // No java é como os valores/metodos estaticos
    var minhaClasse = MinhaClasse.criaClasseComValoresPadrao()
}