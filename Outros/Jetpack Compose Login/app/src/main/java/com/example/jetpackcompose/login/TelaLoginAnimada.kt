package com.example.jetpackcompose.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme


@Composable
fun TelaLoginAnimada(
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

        // 🔄 LOADING COM ANIMAÇÃO
        AnimatedVisibility(
            visible = carregando,
            enter = fadeIn(animationSpec = tween(300)) +
                    scaleIn(initialScale = 0.8f),
            exit = fadeOut()
        ) {
            CircularProgressIndicator()
        }

        // FADE
        /*AnimatedVisibility(
            visible = mensagem.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(mensagem, color = Color.Red)
        }*/

        // SLIDE
        /*AnimatedVisibility(
            visible = mensagem.isNotEmpty(),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically()
        ) {
            Text(mensagem, color = Color.Red)
        }*/

        // SCALE
        /*AnimatedVisibility(
            visible = mensagem.isNotEmpty(),
            enter = scaleIn(initialScale = 0.5f),
            exit = scaleOut()
        ) {
            Text(mensagem, color = Color.Red)
        }*/

        //Transitions (combinação completa)
        AnimatedVisibility(
            visible = mensagem.isNotEmpty(),
            enter =
                fadeIn() +
                        slideInVertically(initialOffsetY = { it / 2 }) +
                        scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut()
        ) {
            Text(mensagem, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginAnimada() {
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

