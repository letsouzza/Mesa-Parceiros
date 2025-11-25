package br.senai.sp.jandira.mesaparceiros.screens.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
                    
                    Button(
                        onClick = {
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
                                        if (response.isSuccessful) {
                                            Log.d("ModalEdicaoEmpresa", "Empresa atualizada com sucesso")
                                            // Notificar o componente pai sobre a atualização
                                            onAtualizar(
                                                DadosEmpresa(
                                                    nome = nome,
                                                    endereco = endereco,
                                                    telefone = telefone,
                                                    email = email,
                                                    cnpj = cnpj
                                                )
                                            )
                                        } else {
                                            Log.e("ModalEdicaoEmpresa", "Erro ao atualizar empresa: ${response.code()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseGeral>, t: Throwable) {
                                        Log.e("ModalEdicaoEmpresa", "Falha na requisição de atualização", t)
                                    }
                                })
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryLight
                        )
                    ) {
                        Text("Atualizar")
                    }
                }
            }
        }
    }
}
