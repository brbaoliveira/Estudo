package com.kontrol.app.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val Blue=Color(0xFF1976D2); val Green=Color(0xFF48A868); val Navy=Color(0xFF17324D); val Background=Color(0xFFF7F8FA)
private val Scheme=lightColorScheme(primary=Blue,secondary=Green,background=Background,surface=Color.White)
@Composable fun KontrolTheme(content:@Composable()->Unit){MaterialTheme(colorScheme=Scheme,typography=Typography(),content=content)}
