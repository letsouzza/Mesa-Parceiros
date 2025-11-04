package br.senai.sp.jandira.mesaparceiros.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.Alimento
import br.senai.sp.jandira.mesaparceiros.model.ResponseGeral
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraInferior
import br.senai.sp.jandira.mesaparceiros.service.AzureUploadService.uploadImageToAzure
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.MesaParceirosTheme
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material.icons.filled.CloudUpload
import br.senai.sp.jandira.mesaparceiros.model.Categoria

@Composable
fun CadastroAlimentoSegundo(navegacao: NavHostController?) {

    // 1) Estado para armazenar o URI da imagem escolhida
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // 2) Estado para armazenar a URL retornada pelo Azure
    var imageUrl by remember { mutableStateOf<String?>(null) }

    // 3) Launcher para pegar o arquivo via Galeria
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
        }

    var imageState by remember { mutableStateOf("") }
    var quantidadeState by remember { mutableStateOf("") }
    var prazoState by remember { mutableStateOf("") }
    var pesoState by remember { mutableStateOf("") }
    var controleNavegacao = rememberNavController()

    val context = LocalContext.current
    val userFile = context.getSharedPreferences("user_file", Context.MODE_PRIVATE)
    val titulo = userFile.getString("titulo", "")
    val descricao = userFile.getString("descricao", "")
    val idUser = userFile.getInt("id", 0)
    val categoriasString = userFile.getString("categorias", "") ?: ""
    val categoriasList = categoriasString.split(",").mapNotNull { it.toIntOrNull() }

    var mostrarMensagemSucesso by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(R.drawable.logoclara),
                    contentDescription = "",
                )
                Text(
                    text = stringResource(R.string.cadastro_alimento),
                    fontFamily = poppinsFamily,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background,
                    lineHeight = 42.sp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f)
                    .background(MaterialTheme.colorScheme.onBackground)
            ){
                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .padding(top = 30.dp)
                                .height(210.dp)
                                .border(0.8.dp, Color(0xFF1B4227), RoundedCornerShape(35.dp))
                                .background(Color.White, RoundedCornerShape(35.dp))
                                .clickable { pickImageLauncher.launch("image/*") }
                        ) {
                            if (imageUri == null) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = "Upload de imagem",
                                        tint = Color(0xFF1B4227),
                                        modifier = Modifier.size(50.dp)
                                    )

                                    Text(
                                        text = "Foto:",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0x99000000),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            } else {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = "Imagem Selecionada",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(35.dp))
                                )
                            }
                        }
                        Spacer(Modifier.padding(top = 10.dp))
                        OutlinedTextField(
                            value = quantidadeState,
                            onValueChange = {quantidadeState = it},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFFFFFFF),
                                focusedContainerColor = Color(0xFFFFFFFF),
                                unfocusedBorderColor = Color(0xFF1B4227),
                                focusedBorderColor = Color(0xFF1B4227),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(30.dp),
                            label = {
                                Text(
                                    text = stringResource(
                                        R.string.quantidade
                                    ),
                                    fontSize = 20.sp,
                                    fontFamily = poppinsFamily,
                                    color = Color(0x99000000)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        )
                        Spacer(Modifier.padding(top = 10.dp))
                        OutlinedTextField(
                            value = prazoState,
                            onValueChange = {prazoState = it},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFFFFFFF),
                                focusedContainerColor = Color(0xFFFFFFFF),
                                unfocusedBorderColor = Color(0xFF1B4227),
                                focusedBorderColor = Color(0xFF1B4227),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(30.dp),
                            label = {
                                Text(
                                    text = stringResource(
                                        R.string.prazo
                                    ),
                                    fontSize = 20.sp,
                                    fontFamily = poppinsFamily,
                                    color = Color(0x99000000)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        )
                        Spacer(Modifier.padding(top = 10.dp))
                        OutlinedTextField(
                            value = pesoState,
                            onValueChange = {pesoState = it},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFFFFFFF),
                                focusedContainerColor = Color(0xFFFFFFFF),
                                unfocusedBorderColor = Color(0xFF1B4227),
                                focusedBorderColor = Color(0xFF1B4227),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(30.dp),
                            label = {
                                Text(
                                    text = stringResource(
                                        R.string.peso
                                    ),
                                    fontSize = 20.sp,
                                    fontFamily = poppinsFamily,
                                    color = Color(0x99000000)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                                .padding(top = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            IconButton(
                                onClick = {
                                    navegacao!!.navigate("cadastroAlimento1?fromSecond=true")
                                },
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(40.dp)
                                    .background(Color(0xFFFFDA8B), shape = CircleShape) // fundo amarelo claro
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack, // seta simples
                                    contentDescription = "",
                                    tint = Color.Black, // cor da seta
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    // 1) Executa a rotina de upload em background
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            // 2) Chama a sua função de upload e aguarda a URL
                                            val urlRetornada = uploadImageToAzure(context, imageUri!!)

                                            val categoriasJsonList = categoriasList.map {
                                                Categoria(
                                                    it
                                                )
                                            }

                                            val body = Alimento(
                                                nome = "$titulo",
                                                quantidade = quantidadeState,
                                                prazo = prazoState,
                                                descricao = "$descricao",
                                                peso = pesoState.toDoubleOrNull() ?: 0.0,
                                                idTipoPeso = 1,
                                                imagem = "$urlRetornada",
                                                idEmpresa = idUser,
                                                categorias = categoriasJsonList
                                            )
                                            Log.d("lara", "$body")

                                            val sendAlimento = RetrofitFactory()
                                                .getAlimentoService()
                                                .insertAlimento(body)

                                            sendAlimento.enqueue(object : Callback<ResponseGeral> {
                                                override fun onResponse(
                                                    call: Call<ResponseGeral>,
                                                    response: Response<ResponseGeral>
                                                ) {
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(
                                                            context,
                                                            "Cadastro OK: ${response.body()?.message}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                        navegacao?.navigate("home")
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Erro no servidor: código ${response.code()}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                        println("erro ${response}")
                                                    }

                                                }

                                                override fun onFailure(p0: Call<ResponseGeral>, p1: Throwable) {
                                                    Log.e("Erro", "Não foi possível cadastrar")
                                                }
                                            })
                                            // 3) Volta na Main e guarda a URL num estado para usar depois
                                            withContext(Dispatchers.Main) {
                                                imageUrl = urlRetornada
                                            }
                                        } catch (e: Exception) {
                                            Log.e("ExemploEnvio", "Falha no upload: ${e.message}")
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, "Erro no upload", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(Color(0xFFFFDA8B))
                            ) {
                                Text(
                                    text = stringResource(R.string.cadastrar),
                                    fontSize = 20.sp,
                                    fontFamily = poppinsFamily,
                                    color = Color.Black
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom
                        ){
                            BarraInferior(controleNavegacao)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MostrarDados() {
    TODO("Not yet implemented")
}

@Preview
@Composable
private fun CadastroAlimentoSegundoPreview() {
    MesaParceirosTheme {
        CadastroAlimentoSegundo(null)
    }
}