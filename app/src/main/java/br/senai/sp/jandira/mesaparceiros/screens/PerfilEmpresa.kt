package br.senai.sp.jandira.mesaparceiros.screens

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.AlimentoFiltro
import br.senai.sp.jandira.mesaparceiros.model.ListAlimentoFiltro
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraInferior
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraDeTitulo
import br.senai.sp.jandira.mesaparceiros.screens.components.CardAlimento
import br.senai.sp.jandira.mesaparceiros.screens.components.DadosEmpresa
import br.senai.sp.jandira.mesaparceiros.screens.components.ModalEdicaoEmpresa
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.MesaParceirosTheme
import br.senai.sp.jandira.mesaparceiros.ui.theme.backgroundLight
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import br.senai.sp.jandira.mesaparceiros.ui.theme.primaryLight
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun PerfilEmpresa(navegacao: NavHostController?) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_file", android.content.Context.MODE_PRIVATE)
    
    // Estados para os dados da empresa
    var dadosEmpresa by remember { mutableStateOf(DadosEmpresa()) }
    var empresaFotoUrl by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarModal by remember { mutableStateOf(false) }
    var temAlteracoesPendentes by remember { mutableStateOf(false) }
    
    // Estados para os alimentos da empresa
    var alimentosEmpresa by remember { mutableStateOf(listOf<AlimentoFiltro>()) }
    var carregandoAlimentos by remember { mutableStateOf(true) }

    // Carrega dados do SharedPreferences e alimentos da empresa
    LaunchedEffect(Unit) {
        val savedNome = prefs.getString("empresa_nome", "") ?: ""
        val savedEmail = prefs.getString("empresa_email", "") ?: ""
        val savedTelefone = prefs.getString("empresa_telefone", "") ?: ""
        val savedCnpj = prefs.getString("empresa_cnpj", "") ?: ""
        val savedFoto = prefs.getString("empresa_foto", "") ?: ""

        dadosEmpresa = DadosEmpresa(
            nome = savedNome.ifBlank { "Não possui" },
            endereco = "Não possui",
            telefone = savedTelefone.ifBlank { "Não possui" },
            email = savedEmail.ifBlank { "Não possui" },
            cnpj = savedCnpj.ifBlank { "Não possui" }
        )
        empresaFotoUrl = savedFoto
        
        // Carregar alimentos da empresa
        val empresaId = prefs.getInt("id", 0)
        if (empresaId > 0) {
            RetrofitFactory().getAlimentoService().filtroEmpresa(empresaId)
                .enqueue(object : Callback<ListAlimentoFiltro> {
                    override fun onResponse(
                        call: Call<ListAlimentoFiltro>,
                        response: Response<ListAlimentoFiltro>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { listAlimento ->
                                alimentosEmpresa = listAlimento.resultFiltro ?: emptyList()
                            }
                        }
                        carregandoAlimentos = false
                    }

                    override fun onFailure(call: Call<ListAlimentoFiltro>, t: Throwable) {
                        carregandoAlimentos = false
                    }
                })
        } else {
            carregandoAlimentos = false
        }
    }
    
    val seletorImagem = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imagemUri = it
            temAlteracoesPendentes = true
        }
    }
    
    Scaffold(
        topBar = { 
            Column {
                BarraDeTitulo()
                if (temAlteracoesPendentes) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                // TODO: Implementar salvamento das modificações
                                temAlteracoesPendentes = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryLight
                            )
                        ) {
                            Text("Salvar Modificações")
                        }
                    }
                }
            }
        },
        bottomBar = { BarraInferior(navegacao) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundLight)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Card principal de informações
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EAD2))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Lápis no topo do card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { mostrarModal = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(id = R.string.editar),
                                    tint = primaryLight
                                )
                            }
                        }
                        
                        // Espaço para o avatar sobreposto
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Texto "Editar" abaixo da imagem
                        Text(
                            text = stringResource(id = R.string.editar),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { seletorImagem.launch("image/*") },
                            color = primaryLight,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nome da empresa
                        Text(
                            text = dadosEmpresa.nome,
                            color = Color.Black,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                        Divider(color = Color(0x33000000))

                        // Endereço
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.endereco) + " " + dadosEmpresa.endereco,
                            color = Color.Black,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp
                        )
                        Divider(color = Color(0x33000000))

                        // Telefone
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.telefone) + " " + dadosEmpresa.telefone,
                            color = Color.Black,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp
                        )
                        Divider(color = Color(0x33000000))

                        // Email
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.email) + " " + dadosEmpresa.email,
                            color = Color.Black,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp
                        )
                        Divider(color = Color(0x33000000))

                        // CNPJ
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.cnpj) + " " + dadosEmpresa.cnpj,
                            color = Color.Black,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp
                        )
                        Divider(color = Color(0x33000000))
                        
                        // Alterar senha
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Alterar senha",
                            modifier = Modifier.clickable {
                                navegacao?.navigate("recuperacao")
                            },
                            color = primaryLight,
                            fontFamily = poppinsFamily,
                            fontSize = 16.sp
                        )
                    }
                }

                // Avatar circular sobreposto
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(96.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = primaryLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    when {
                        imagemUri != null -> {
                            AsyncImage(
                                model = imagemUri,
                                contentDescription = "Logo da empresa",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        empresaFotoUrl.isNotBlank() -> {
                            AsyncImage(
                                model = empresaFotoUrl,
                                contentDescription = "Logo da empresa",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = "Ícone da empresa",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            
            // Modal de edição
            if (mostrarModal) {
                ModalEdicaoEmpresa(
                    dadosAtuais = dadosEmpresa,
                    onDismiss = { mostrarModal = false },
                    onAtualizar = { novosDados ->
                        dadosEmpresa = novosDados
                        mostrarModal = false
                        temAlteracoesPendentes = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Título Minhas publicações
            Text(
                text = stringResource(id = R.string.minhas_publicacoes),
                color = primaryLight,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de alimentos da empresa
            if (carregandoAlimentos) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            } else if (alimentosEmpresa.isEmpty()) {
                Text(
                    text = "Nenhum alimento cadastrado",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontFamily = poppinsFamily
                )
            } else {
                // Lista de alimentos da empresa
                alimentosEmpresa.forEach { alimento ->
                    CardAlimento(
                        img = alimento.imagem ?: "",
                        nome = alimento.nome ?: "Sem nome",
                        prazo = alimento.prazo ?: "Sem data",
                        quantidade = alimento.quantidade ?: "0",
                        imgEmpresa = alimento.fotoEmpresa ?: "",
                        empresa = alimento.nomeEmpresa ?: "Sem empresa"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
}

@Preview
@Composable
private fun PerfilEmpresaPreview() {
    MesaParceirosTheme {
        PerfilEmpresa(null)
    }
}
