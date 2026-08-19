package org.example.fundamentos

fun main(): Unit {
    dizOi(retornaNome(), 24)
    dizOi(idade = 24, nome = retornaNome())
    dizOi(retornaNome())
}
/*
igual a
fun main() {

}*/
//retorna um valor
fun retornaNome(): String{
    return "Gustavo"
}

//valor padrao
fun dizOi(nome: String, idade: Int = 20) {
    println("Oi ${nome}, parabáns pelos seus ${idade} anos")
}

/*
fun dizOi(nome: String, idade: Int) {
    println("Oi ${nome}, parabáns pelos seus ${idade} anos")
}*/
