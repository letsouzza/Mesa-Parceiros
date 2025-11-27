package br.senai.sp.jandira.mesaparceiros.screens.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.EmpresaUpdate
import br.senai.sp.jandira.mesaparceiros.model.ResponseGeral
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import br.senai.sp.jandira.mesaparceiros.ui.theme.primaryLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class DadosEmpresa(
    val nome: String = "",
    val endereco: String = "",
    val telefone: String = "",
    val email: String = "",
    val cnpj: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalEdicaoEmpresa(
    dadosAtuais: DadosEmpresa,
    empresaId: Int,
    onDismiss: () -> Unit,
    onAtualizar: (DadosEmpresa) -> Unit
) {
    var nome by remember { mutableStateOf(dadosAtuais.nome) }
    var endereco by remember { mutableStateOf(dadosAtuais.endereco) }
    var telefone by remember { mutableStateOf(dadosAtuais.telefone) }
    var email by remember { mutableStateOf(dadosAtuais.email) }
    var cnpj by remember { mutableStateOf(dadosAtuais.cnpj) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Fechar o modal após 2 segundos de sucesso
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            kotlinx.coroutines.delay(2000)
            onDismiss()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Editar Informações",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = primaryLight
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da Empresa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = endereco,
                    onValueChange = { endereco = it },
                    label = { Text("Endereço") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = primaryLight
                        )
                    ) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    if (isLoading) {
                        CircularProgressIndicator(color = primaryLight)
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        Button(
                            onClick = {
                                if (nome.isBlank() || email.isBlank() || telefone.isBlank()) {
                                    errorMessage = "Preencha todos os campos obrigatórios"
                                    showErrorMessage = true
                                    return@Button
                                }
                                
                                isLoading = true
                                showErrorMessage = false
                                
                                // Criar objeto de atualização
                                val empresaUpdate = EmpresaUpdate(
                                    id = empresaId,
                                    nome = nome,
                                    email = email,
                                    senha = "", // Senha não é alterada nesta tela
                                    telefone = telefone,
                                    foto = "" // Foto será mantida como está
                                )
                                
                                // Chamar API de atualização
                                RetrofitFactory().getEmpresaService().updateEmpresa(empresaId, empresaUpdate)
                                    .enqueue(object : Callback<ResponseGeral> {
                                        override fun onResponse(
                                            call: Call<ResponseGeral>,
                                            response: Response<ResponseGeral>
                                        ) {
                                            isLoading = false
                                            
                                            if (response.isSuccessful) {
                                                val responseBody = response.body()
                                                if (responseBody?.status == true) {
                                                    // Atualizar com os dados retornados da API
                                                    responseBody.empresa?.let { empresa ->
                                                        onAtualizar(
                                                            DadosEmpresa(
                                                                nome = empresa.nome,
                                                                endereco = empresa.endereco ?: "",
                                                                telefone = empresa.telefone,
                                                                email = empresa.email,
                                                                cnpj = empresa.cnpjMei
                                                            )
                                                        )
                                                    }
                                                    showSuccessMessage = true
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Dados atualizados com sucesso!")
                                                    }
                                                } else {
                                                    errorMessage = responseBody?.message ?: "Erro ao atualizar dados"
                                                    showErrorMessage = true
                                                }
                                            } else {
                                                errorMessage = "Erro ao atualizar: ${response.code()}"
                                                showErrorMessage = true
                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseGeral>, t: Throwable) {
                                            isLoading = false
                                            errorMessage = "Falha na conexão: ${t.message}"
                                            showErrorMessage = true
                                            Log.e("ModalEdicaoEmpresa", "Falha na requisição de atualização", t)
                                        }
                                    })
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryLight
                            )
                        ) {
                            Text("Atualizar")
                        }
                    }
                }
                
                // Exibir mensagem de erro se necessário
                if (showErrorMessage) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
