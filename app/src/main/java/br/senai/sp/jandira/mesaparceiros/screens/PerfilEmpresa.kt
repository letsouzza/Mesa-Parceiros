package br.senai.sp.jandira.mesaparceiros.screens

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
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
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraInferior
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraDeTitulo
import br.senai.sp.jandira.mesaparceiros.screens.components.CardAlimento
import br.senai.sp.jandira.mesaparceiros.screens.components.DadosEmpresa
import br.senai.sp.jandira.mesaparceiros.screens.components.ModalEdicaoEmpresa
import br.senai.sp.jandira.mesaparceiros.ui.theme.MesaParceirosTheme
import br.senai.sp.jandira.mesaparceiros.ui.theme.backgroundLight
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import br.senai.sp.jandira.mesaparceiros.ui.theme.primaryLight
import coil.compose.AsyncImage

@Composable
fun PerfilEmpresa(navegacao: NavHostController?) {
    var dadosEmpresa by remember {
        mutableStateOf(
            DadosEmpresa(
                nome = "MC Donald's",
                endereco = "Rua Teste, 2000 - Jardim Teste",
                telefone = "(11) 97890-0009",
                email = "mcDonalds@gmail.com",
                cnpj = "05.311.244/0001-09"
            )
        )
    }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarModal by remember { mutableStateOf(false) }
    var temAlteracoesPendentes by remember { mutableStateOf(false) }
    
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
                    if (imagemUri != null) {
                        AsyncImage(
                            model = imagemUri,
                            contentDescription = "Logo da empresa",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
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

            // Card de exemplo de publicação
            CardAlimento()

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
