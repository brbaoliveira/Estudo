package com.example.jetpackcompose.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme


@Composable
fun TelaLogin(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {

    val usuario = viewModel.usuario.value
    val senha = viewModel.senha.value
    val mensagem = viewModel.mensagem.value
    val carregando = viewModel.carregando.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Login", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = usuario,
            onValueChange = { viewModel.usuario.value = it },
            label = { Text("Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = senha,
            onValueChange = { viewModel.senha.value = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.login() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (carregando) {
            CircularProgressIndicator()
        }

        /*if (mensagem.isNotEmpty()) {
            Text(mensagem, color = Color.Red)
        }*/
        AnimatedVisibility(
            visible = mensagem.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut()
        ) {
            Text(mensagem, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    val fakeViewModel = LoginViewModel().apply {
        usuario.value = "teste"
        senha.value = "123456"
        mensagem.value = "Erro de login"
        carregando.value = false
    }

    JetpackComposeTheme {
        TelaLogin(viewModel = fakeViewModel)
    }
}