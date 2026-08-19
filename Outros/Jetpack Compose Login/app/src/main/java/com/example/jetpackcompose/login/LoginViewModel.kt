package com.example.jetpackcompose.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var usuario = mutableStateOf("")
    var senha = mutableStateOf("")
    var mensagem = mutableStateOf("")
    var carregando = mutableStateOf(false)

    fun login() {

        mensagem.value = ""

        if (usuario.value.isEmpty() || senha.value.isEmpty()) {
            mensagem.value = "Preencha todos os campos"
            return
        }

        carregando.value = true
        carregando.value = false

        if (usuario.value == "admin" && senha.value == "123") {
            mensagem.value = "Login OK ✅"
        } else {
            mensagem.value = "Erro ❌"
        }
    }
}