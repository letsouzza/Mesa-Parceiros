package br.senai.sp.jandira.mesaparceiros.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraInferior
import br.senai.sp.jandira.mesaparceiros.screens.components.BarraDeTitulo
import br.senai.sp.jandira.mesaparceiros.screens.components.CardAlimento
import br.senai.sp.jandira.mesaparceiros.service.AzureUploadService
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.backgroundLight
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import br.senai.sp.jandira.mesaparceiros.ui.theme.primaryLight
import br.senai.sp.jandira.mesaparceiros.ui.theme.secondaryLight
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PerfilEmpresa(navegacao: NavHostController?) {
    var empresa by remember { mutableStateOf<EmpresaCadastro?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Estados editáveis
    var nomeState by remember { mutableStateOf("") }
    var enderecoState by remember { mutableStateOf("") }
    var telefoneState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var senhaState by remember { mutableStateOf("") }
    var cnpjState by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val userFile = context.getSharedPreferences("user_file", Context.MODE_PRIVATE)
    val idEmpresa = userFile.getInt("id", 0)
    
    // Launcher para seleção de imagem
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }
    
    // Carregar dados da empresa
    LaunchedEffect(Unit) {
        if (idEmpresa != 0) {
            val call = RetrofitFactory().getEmpresaService().empresaPorId(idEmpresa)
            call.enqueue(object : Callback<EmpresaCadastro> {
                override fun onResponse(call: Call<EmpresaCadastro>, response: Response<EmpresaCadastro>) {
                    if (response.isSuccessful) {
                        empresa = response.body()
                        empresa?.let {
                            nomeState = it.nome
                            enderecoState = "Não possui" // EmpresaCadastro não tem endereço
                            telefoneState = it.telefone
                            emailState = it.email
                            senhaState = "******"
                            cnpjState = it.cnpjMei
                        }
                    } else {
                        Log.e("PerfilEmpresa", "Erro ao carregar empresa: ${response.code()}")
                    }
                    isLoading = false
                }
                
                override fun onFailure(call: Call<EmpresaCadastro>, throwable: Throwable) {
                    Log.e("PerfilEmpresa", "Falha na requisição: ${throwable.message}")
                    isLoading = false
                }
            })
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Usar BarraDeTitulo existente
            BarraDeTitulo()
            
            // Conteúdo principal
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else {
                    // Card do perfil
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Foto da empresa centralizada
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable { 
                                            if (isEditing) pickImageLauncher.launch("image/*") 
                                        },
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(Color.Red)
                                ) {
                                    AsyncImage(
                                        model = imageUri ?: empresa?.foto ?: "https://via.placeholder.com/100",
                                        contentDescription = "Logo da empresa",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Título da empresa centralizado
                            Text(
                                text = empresa?.nome ?: stringResource(R.string.nao_possui),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Botão editar centralizado
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = { isEditing = !isEditing }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.editar),
                                        tint = primaryLight
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Campos do formulário
                            CampoPerfilEmpresa(
                                label = stringResource(R.string.endereco),
                                value = enderecoState,
                                onValueChange = { enderecoState = it },
                                isEditing = isEditing
                            )
                            
                            CampoPerfilEmpresa(
                                label = stringResource(R.string.telefone),
                                value = telefoneState,
                                onValueChange = { telefoneState = it },
                                isEditing = isEditing
                            )
                            
                            CampoPerfilEmpresa(
                                label = stringResource(R.string.email),
                                value = emailState,
                                onValueChange = { emailState = it },
                                isEditing = isEditing
                            )
                            
                            CampoPerfilEmpresa(
                                label = stringResource(R.string.cnpj),
                                value = cnpjState,
                                onValueChange = { cnpjState = it },
                                isEditing = isEditing
                            )
                            
                            // Botão atualizar (só aparece quando está editando)
                            if (isEditing) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = {
                                        // TODO: Implementar atualização dos dados
                                        Toast.makeText(context, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show()
                                        isEditing = false
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(secondaryLight),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.atualizar_dados),
                                        color = Color.Black,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Seção "Minhas publicações"
                    Text(
                        text = stringResource(R.string.minhas_publicacoes),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        color = primaryLight
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Card de exemplo (como na imagem)
                    CardAlimento(
                        img = "https://via.placeholder.com/80",
                        nome = "Feijão",
                        prazo = "25/09/25",
                        quantidade = "5",
                        imgEmpresa = empresa?.foto ?: "",
                        empresa = "Atacadão"
                    )
                }
            }
            
            // Barra inferior
            BarraInferior(navegacao)
        }
    }
}

@Composable
fun CampoPerfilEmpresa(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            
            IconButton(
                onClick = { /* Ação do lápis individual */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.editar),
                    tint = primaryLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = primaryLight
                ),
                shape = RoundedCornerShape(8.dp)
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
