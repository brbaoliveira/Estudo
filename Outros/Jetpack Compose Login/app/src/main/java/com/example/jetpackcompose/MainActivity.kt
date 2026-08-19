package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcompose.login.TelaLogin
import com.example.jetpackcompose.login.TelaLoginAnimada
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import com.example.jetpackcompose.ui.theme.TemaApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*setContent {
            JetpackComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaLoginAnimada(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }*/

        /*setContent {
            // ESTADO DO TEMA
            var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }

            // ✅ CONVERSÃO CORRETA
            val darkTheme: Boolean = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            TemaApp(darkTheme = darkTheme) {

                ConfigurarSistemaUI(darkTheme)

                TelaPrincipal(
                    themeMode = themeMode,
                    onChangeTheme = { newTheme ->
                        themeMode = newTheme
                    }
                )
            }
        }*/

        setContent {
            // O app escuta o estado global de idioma
            AppLanguageProvider(language = currentLanguageState) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TelaIdioma()
                }
            }
        }

    }
}

