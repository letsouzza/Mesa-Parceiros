package br.senai.sp.jandira.mesaparceiros.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import br.senai.sp.jandira.mesaparceiros.model.Alimento
import br.senai.sp.jandira.mesaparceiros.model.AlimentoResponse
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraInferior
import br.senai.sp.jandira.mesaparceiros.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesScreen(
    navController: NavHostController?,
    alimentoId: Int = 0
) {
    // Estados para os dados do alimento
    var alimento by remember { mutableStateOf<Alimento?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Estados da UI
    var isFavorito by remember { mutableStateOf(false) }
    var quantidadeCarrinho by remember { mutableStateOf(1) }
    val quantidadeMaxima = alimento?.quantidade?.toIntOrNull() ?: 1

    // Carregar detalhes do alimento quando o ID mudar
    LaunchedEffect(alimentoId) {
        if (alimentoId > 0) {
            val call = RetrofitFactory().getAlimentoService().getAlimentoPorId(alimentoId)
            call.enqueue(object : Callback<AlimentoResponse> {
                override fun onResponse(call: Call<AlimentoResponse>, response: Response<AlimentoResponse>) {
                    isLoading = false
                    if (response.isSuccessful) {
                        response.body()?.let { alimentoResponse ->
                            if (alimentoResponse.alimento.isNotEmpty()) {
                                alimento = alimentoResponse.alimento[0]
                            } else {
                                errorMessage = "Alimento não encontrado"
                            }
                        } ?: run {
                            errorMessage = "Resposta inválida do servidor"
                        }
                    } else {
                        errorMessage = "Erro ao carregar: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<AlimentoResponse>, t: Throwable) {
                    isLoading = false
                    errorMessage = "Falha na conexão: ${t.message}"
                    Log.e("DetalhesScreen", "Erro na requisição", t)
                }
            })
        } else {
            isLoading = false
            errorMessage = "ID do alimento não fornecido"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController?.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isFavorito = !isFavorito }
                    ) {
                        Icon(
                            imageVector = if (isFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoritar",
                            tint = if (isFavorito) Color.Red else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BarraInferior(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagem do produto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = primaryLight)
                        }
                    }
                    errorMessage != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = errorMessage!!,
                                color = Color.Red,
                                fontFamily = poppinsFamily
                            )
                        }
                    }
                    alimento != null -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(alimento?.imagem ?: "")
                                .crossfade(true)
                                .build(),
                            contentDescription = alimento?.nome,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Conteúdo principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = backgroundLight,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(24.dp)
            ) {
                // Nome do produto
                Text(
                    text = alimento?.nome ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryLight,
                    fontFamily = poppinsFamily
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card da empresa
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = secondaryLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ícone da empresa (placeholder)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = primaryLight,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = alimento?.empresa?.nome?.firstOrNull()?.toString() ?: "",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = alimento?.empresa?.nome ?: "",
                                fontWeight = FontWeight.SemiBold,
                                color = primaryLight,
                                fontFamily = poppinsFamily
                            )
                            Text(
                                text = alimento?.empresa?.email ?: "",
                                color = primaryLight.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                fontFamily = poppinsFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Seção Detalhes
                Text(
                    text = "Detalhes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Data de validade
                Row {
                    Text(
                        text = "Data de validade: ",
                        fontWeight = FontWeight.SemiBold,
                        color = primaryLight,
                        fontFamily = poppinsFamily
                    )
                    Text(
                        text = formatarDataValidade(alimento?.prazo ?: "") ?: "Não informada",
                        color = primaryLight.copy(alpha = 0.8f),
                        fontFamily = poppinsFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Descrição
                Row {
                    Text(
                        text = "Descrição: ",
                        fontWeight = FontWeight.SemiBold,
                        color = primaryLight
                    )
                    Text(
                        text = alimento?.descricao ?: "",
                        color = primaryLight.copy(alpha = 0.8f),
                        fontFamily = poppinsFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quantidade
                Row {
                    Text(
                        text = "Quantidade: ",
                        fontWeight = FontWeight.SemiBold,
                        color = primaryLight
                    )
                    Text(
                        text = alimento?.quantidade ?: "0",
                        color = primaryLight.copy(alpha = 0.8f),
                        fontFamily = poppinsFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Peso
                Row {
                    Text(
                        text = "Peso: ",
                        fontWeight = FontWeight.SemiBold,
                        color = primaryLight
                    )
                    Text(
                        text = "${alimento?.peso ?: ""} ${alimento?.tipoPeso?.firstOrNull()?.tipo ?: ""}",
                        color = primaryLight.copy(alpha = 0.8f),
                        fontFamily = poppinsFamily
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Seção Categoria
                Text(
                    text = "Categoria",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryLight
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tags de categorias
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    alimento?.categorias?.forEach { categoria ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = secondaryLight),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = categoria.nome,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = primaryLight,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private fun formatarDataValidade(data: String): String? {
    return try {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dataFormatada = formatoEntrada.parse(data)
        val formatoSaida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dataFormatada?.let { formatoSaida.format(it) }
    } catch (e: Exception) {
        data // Retorna a data original se não conseguir formatar
    }
}

@Preview
@Composable
private fun DetalhesScreenPreview() {
    MesaParceirosTheme {
        DetalhesScreen(
            navController = null,
            alimentoId = 1 // ID de exemplo para preview
        )
    }
}