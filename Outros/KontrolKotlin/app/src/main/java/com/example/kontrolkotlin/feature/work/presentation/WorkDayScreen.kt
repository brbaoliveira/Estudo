package com.kontrol.app.feature.work.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

/**
 * ---------------------------------------------------------------------
 * ATENÇÃO: os valores abaixo (marcações do dia, horas feitas, total a
 * receber e o histórico do mês) são apenas dados de EXEMPLO para exibir
 * o layout. Na versão final eles devem vir do WorkDayViewModel, que por
 * sua vez consulta o Repository (GET /work-records, GET /work-records/{date}),
 * seguindo a arquitetura MVVM + StateFlow definida na especificação do
 * Kontrol. Nada aqui está fixo — é só ilustração de como a tela fica.
 * ---------------------------------------------------------------------
 */

/** Uma marcação de ponto (entrada, saída do almoço, volta, saída). */
data class PontoMarcacao(
    val label: String,
    val hora: String,
    val icon: ImageVector,
)

/** Uma linha do histórico de dias trabalhados no mês. */
data class RegistroDia(
    val dia: Int,
    val horas: String,
    val totalDoDia: Double,
)

/** Estado completo da tela — viria do ViewModel via StateFlow/collectAsState. */
data class WorkDayUiState(
    val marcacoes: List<PontoMarcacao>,
    val horasFeitasHoje: String,
    val totalAReceberHoje: Double,
    val registrosDoMes: List<RegistroDia>,
    val totalHorasMes: String,
    val totalReceberMes: Double,
)

private fun sampleUiState(): WorkDayUiState {
    val registros = (1..8).map { dia ->
        RegistroDia(dia = dia, horas = "8h", totalDoDia = 134.40)
    }
    return WorkDayUiState(
        marcacoes = listOf(
            PontoMarcacao("Entrada", "08:00", Icons.Filled.Login),
            PontoMarcacao("Saída", "12:00", Icons.Filled.RestaurantMenu),
            PontoMarcacao("Volta", "13:00", Icons.Filled.Work),
            PontoMarcacao("Saída", "17:00", Icons.Filled.Logout),
        ),
        horasFeitasHoje = "8h",
        totalAReceberHoje = 134.40,
        registrosDoMes = registros,
        totalHorasMes = "72h",
        totalReceberMes = registros.sumOf { it.totalDoDia },
    )
}

private val GradientStart = Color(0xFF1E5F8C)
private val GradientEnd = Color(0xFF2E9E6B)
private val CardBlueBg = Color(0xFFDCEBF7)
private val CardGreenBg = Color(0xFFDCF3E6)
private val ScreenBg = Color(0xFFF4F6F8)

private fun formatMoney(value: Double): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return nf.format(value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkDayScreen(
    uiState: WorkDayUiState = sampleUiState(),
    onBackClick: () -> Unit = {},
    onCalcularValorClick: () -> Unit = {},
) {
    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Marcação de Ponto",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd),
                        endY = 340f,
                    ),
                )
                .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item { MarcacoesCard(marcacoes = uiState.marcacoes) }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ResumoCard(
                            modifier = Modifier.weight(1f),
                            titulo = "Horas Feitas",
                            valor = uiState.horasFeitasHoje,
                            icon = Icons.Filled.AccessTime,
                            corFundo = GradientStart,
                        )
                        ResumoCard(
                            modifier = Modifier.weight(1f),
                            titulo = "Total a Receber",
                            valor = formatMoney(uiState.totalAReceberHoje),
                            icon = Icons.Filled.Payments,
                            corFundo = GradientEnd,
                        )
                    }
                }

                item {
                    Button(
                        onClick = onCalcularValorClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GradientStart),
                    ) {
                        Text(
                            text = "Calcular Valor",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                item { HistoricoMesCard(uiState = uiState) }
            }
        }
    }
}

@Composable
private fun MarcacoesCard(marcacoes: List<PontoMarcacao>) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            marcacoes.forEach { marcacao ->
                MarcacaoItem(marcacao = marcacao, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MarcacaoItem(marcacao: PontoMarcacao, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .background(CardBlueBg, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = marcacao.icon,
                contentDescription = marcacao.label,
                tint = GradientStart,
            )
        }
        androidx.compose.foundation.layout.Spacer(Modifier.height(6.dp))
        Text(
            text = marcacao.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2B3A42),
        )
        Text(
            text = marcacao.hora,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF16232B),
        )
    }
}

@Composable
private fun ResumoCard(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    icon: ImageVector,
    corFundo: Color,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = corFundo),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
            )
            androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
            Text(
                text = titulo,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.9f),
            )
            Text(
                text = valor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun HistoricoMesCard(uiState: WorkDayUiState) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Dia",
                    modifier = Modifier.weight(0.6f),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontSize = 13.sp,
                )
                Text(
                    text = "Horas",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontSize = 13.sp,
                )
                Text(
                    text = "Total do Mês",
                    modifier = Modifier.weight(1.2f),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontSize = 13.sp,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            uiState.registrosDoMes.forEach { registro ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = registro.dia.toString(),
                        modifier = Modifier.weight(0.6f),
                        fontWeight = FontWeight.SemiBold,
                        color = GradientStart,
                    )
                    Text(
                        text = registro.horas,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = formatMoney(registro.totalDoDia),
                        modifier = Modifier.weight(1.2f),
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Total de Horas", color = Color.Gray, fontSize = 13.sp)
                Text(
                    text = formatMoney(uiState.totalReceberMes),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            androidx.compose.foundation.layout.Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(CardBlueBg, CardGreenBg)),
                        RoundedCornerShape(14.dp),
                    )
                    .padding(vertical = 14.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = uiState.totalHorasMes,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = GradientStart,
                )
                Text(
                    text = formatMoney(uiState.totalReceberMes),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B4332),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkDayScreenPreview() {
    MaterialTheme {
        WorkDayScreen()
    }
}
