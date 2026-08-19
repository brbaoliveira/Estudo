package com.example.jetpackcompose

import android.app.Activity
import android.app.LocaleManager
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import java.util.Locale

@Composable
fun TelaPrincipal(
    themeMode: ThemeMode,
    onChangeTheme: (ThemeMode) -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Tema atual: $themeMode",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onChangeTheme(ThemeMode.LIGHT) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Claro ☀️")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onChangeTheme(ThemeMode.DARK) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escuro 🌙")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onChangeTheme(ThemeMode.SYSTEM) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sistema 📱")
            }
        }
    }
}

@Composable
fun ConfigurarSistemaUI(darkTheme: Boolean) {

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {

            val window = (view.context as Activity).window

            // 🎨 cor da barra de status (topo)
            window.statusBarColor = if (darkTheme)
                Color.Black.toArgb()
            else
                Color.White.toArgb()

            // 🎨 cor da barra de navegação (baixo)
            window.navigationBarColor = if (darkTheme)
                Color.Black.toArgb()
            else
                Color.White.toArgb()

            val controller = WindowCompat.getInsetsController(window, view)

            // 🔥 cor dos ícones (true = escuro, false = claro)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }
}

@Composable
fun TelaIdioma() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Altera apenas a variável de estado
        Button(
            onClick = { currentLanguageState = LanguageMode.PORTUGUESE },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.idioma_pt))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { currentLanguageState = LanguageMode.ENGLISH },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.idioma_en))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { currentLanguageState = LanguageMode.SYSTEM },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.idioma_sistema))
        }
    }
}

var currentLanguageState by mutableStateOf(LanguageMode.SYSTEM)

@Composable
fun AppLanguageProvider(
    language: LanguageMode,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val locale = remember(language) {
        when (language) {
            LanguageMode.PORTUGUESE -> Locale("pt", "BR")
            LanguageMode.ENGLISH -> Locale("en")
            LanguageMode.SYSTEM -> Locale.getDefault()
        }
    }

    // Cria uma nova configuração com o Locale desejado
    val config = remember(locale) {
        Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
    }

    // Aplica o novo Contexto sem reiniciar a Activity
    CompositionLocalProvider(
        LocalContext provides context.createConfigurationContext(config)
    ) {
        content()
    }
}