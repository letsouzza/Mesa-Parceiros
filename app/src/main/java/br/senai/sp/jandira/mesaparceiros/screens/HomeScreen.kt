package br.senai.sp.jandira.mesaparceiros.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Locale
import br.senai.sp.jandira.mesaparceiros.model.Alimento
import br.senai.sp.jandira.mesaparceiros.model.ListAlimento
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraDeTitulo
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraInferior
import br.senai.sp.jandira.mesaparceiros.screens.components.CardAlimento
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.MesaParceirosTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Função para formatar a data de yyyy-MM-dd para dd/MM/yy
fun formatarData(dataOriginal: String): String {
    return try {
        val formatoOriginal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatoDesejado = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val data = formatoOriginal.parse(dataOriginal)
        formatoDesejado.format(data ?: return dataOriginal)
    } catch (e: Exception) {
        dataOriginal // Retorna a data original se houver erro
    }
}

@Composable
fun HomeScreen(navegacao: NavHostController?) {

    var controleNavegacao = rememberNavController()

    // Estados para controlar a UI
    var alimentoList = remember {
        mutableStateOf(listOf<Alimento>())
    }
    var isLoading = remember {
        mutableStateOf(true)
    }
    var errorMessage = remember {
        mutableStateOf<String?>(null)
    }

    // Carregar dados da API quando a tela for criada
    LaunchedEffect(Unit) {
        // Obter um Retrofit Factory
        val callRetrofit = RetrofitFactory()
            .getAlimentoService()
            .listAlimento()

        // Enviar a requisição
        callRetrofit.enqueue(object : Callback<ListAlimento> {
            override fun onResponse(call: Call<ListAlimento>, response: Response<ListAlimento>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { listAlimento ->
                        alimentoList.value = listAlimento.alimentos
                        Log.d("HomeScreen", "Alimentos carregados: ${listAlimento.alimentos.size}")
                    }
                } else {
                    errorMessage.value = "Erro ao carregar alimentos: ${response.code()}"
                    Log.e("HomeScreen", "Erro na resposta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListAlimento>, t: Throwable) {
                isLoading.value = false
                errorMessage.value = "Erro de conexão: ${t.message}"
                Log.e("HomeScreen", "Erro na requisição", t)
            }
        })
    }

    Scaffold (
        topBar = {
            BarraDeTitulo()
        },
        bottomBar = {
            BarraInferior(controleNavegacao)
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading.value -> {
                        // Estado de carregamento
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage.value != null -> {
                        // Estado de erro
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage.value ?: "Erro desconhecido",
                                color = Color.Red
                            )
                        }
                    }
                    alimentoList.value.isEmpty() -> {
                        // Estado vazio
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Nenhum alimento encontrado")
                        }
                    }
                    else -> {
                        // Lista de alimentos
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(alimentoList.value) { alimento ->
                                CardAlimento(
                                    img = alimento.imagem,
                                    nome = alimento.nome,
                                    prazo = formatarData(alimento.prazo),
                                    quantidade = alimento.quantidade,
                                    imgEmpresa = "", // Pode ser implementado depois se necessário
                                    empresa = "Atacadão" // Pode ser obtido da empresa associada
                                )
                            }
                        }
                    }
                }
            }
        }
    )

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() {
    MesaParceirosTheme {
        HomeScreen(null)
    }
}