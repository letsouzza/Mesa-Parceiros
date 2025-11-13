package br.senai.sp.jandira.mesaparceiros.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro
import br.senai.sp.jandira.mesaparceiros.model.EmpresaLogin
import br.senai.sp.jandira.mesaparceiros.model.ListEmpresa
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

@Composable
fun LoginScreen(navegacao: NavHostController?) {

    var emailState by remember {mutableStateOf("")}
    var senhaState by remember {mutableStateOf("")}
    var senhaVisivel by remember { mutableStateOf(false) }

    val empresaApi = RetrofitFactory().getEmpresaService()

    var mostrarMensagemSucesso by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userFile = context.getSharedPreferences("user_file", Context.MODE_PRIVATE)
    val editor = userFile.edit()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B4227))
    ){
        Column(
            modifier= Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(R.drawable.logoclara),
                contentDescription = "",
                modifier = Modifier
                    .padding(10.dp).size(350.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(580.dp),
                colors = CardDefaults.cardColors(Color(0xFFFFF9EB)),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize().padding(20.dp)
                ){
                    Spacer(Modifier.padding(5.dp))
                    Text(
                        text= stringResource(R.string.login),
                        fontSize = 40.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1B4227),
                        modifier= Modifier
                            .padding(start = 20.dp)
                    )
                    Spacer(Modifier.padding(15.dp))
                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { it ->
                            emailState = it
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFE6B1),
                            focusedContainerColor = Color(0xFFFFE6B1),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(30.dp),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.email
                                ),
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                color = Color(0x99000000)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.padding(10.dp))
                    OutlinedTextField(
                        value = senhaState,
                        onValueChange = { it ->
                            senhaState = it
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFE6B1),
                            focusedContainerColor = Color(0xFFFFE6B1),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(30.dp),
                        trailingIcon = {
                            val icon = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff

                            IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "",
                                    tint = Color(0xFF1B4227)
                                )
                            }
                        },
                        visualTransformation =
                            if (senhaVisivel) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.senha
                                ),
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                color = Color(0x99000000)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.padding(5.dp))
                    Text(
                        text = stringResource(R.string.esqueci_senha),
                        fontSize = 14.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B4227),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 25.dp)
                            .clickable{navegacao!!.navigate("recuperacao")}
                    )
                    Spacer(Modifier.padding(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = stringResource(R.string.nao_tem_login),
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(end = 5.dp)
                        )
                        Text(
                            text = stringResource(R.string.cadastre_se),
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B4227),
                            modifier = Modifier
                                .clickable{navegacao?.navigate("cadastro")}
                        )
                    }
                    Button(
                        onClick = {
                            val body = EmpresaLogin(
                                email = emailState,
                                senha = senhaState,
                                tipo = "empresa"
                            )

                            GlobalScope.launch(Dispatchers.IO){
                                try {
                                    val response = empresaApi.loginEmpresa(body).await()
                                    val userId = response.usuario.id.toInt()

                                    // Persistir o ID do usuário
                                    editor.putInt("id", userId)
                                    editor.putBoolean("empresa_logada", true)

                                    // Buscar dados completos da empresa por ID
                                    empresaApi.empresaPorId(userId).enqueue(object : retrofit2.Callback<EmpresaCadastro> {
                                        override fun onResponse(
                                            call: retrofit2.Call<EmpresaCadastro>,
                                            response: retrofit2.Response<EmpresaCadastro>
                                        ) {
                                            if (response.isSuccessful) {
                                                response.body()?.let { empresa ->
                                                    // Verificar se a empresa tem dados válidos
                                                    if (empresa.id != 0 && empresa.nome.isNotBlank()) {
                                                        // Salvar dados da empresa
                                                        editor.putString("empresa_nome", empresa.nome)
                                                        editor.putString("empresa_email", empresa.email)
                                                        editor.putString("empresa_telefone", empresa.telefone)
                                                        editor.putString("empresa_cnpj", empresa.cnpjMei)
                                                        editor.putString("empresa_foto", empresa.foto)
                                                        editor.commit()
                                                    } else {
                                                        // Buscar na lista de empresas como alternativa
                                                        RetrofitFactory().getEmpresaService().listEmpresa().enqueue(object : retrofit2.Callback<ListEmpresa> {
                                                            override fun onResponse(call: retrofit2.Call<ListEmpresa>, response: retrofit2.Response<ListEmpresa>) {
                                                                if (response.isSuccessful) {
                                                                    response.body()?.let { listEmpresa ->
                                                                        val empresaEncontrada = listEmpresa.empresas.find { it.id == userId }
                                                                        empresaEncontrada?.let { emp ->
                                                                            editor.putString("empresa_nome", emp.nome)
                                                                            editor.putString("empresa_email", emp.email)
                                                                            editor.putString("empresa_telefone", emp.telefone)
                                                                            editor.putString("empresa_cnpj", emp.cnpjMei)
                                                                            editor.putString("empresa_foto", emp.foto)
                                                                            editor.commit()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            override fun onFailure(call: retrofit2.Call<ListEmpresa>, t: Throwable) {
                                                                editor.apply()
                                                            }
                                                        })
                                                    }
                                                }
                                            } else {
                                                editor.apply()
                                            }
                                        }

                                        override fun onFailure(call: retrofit2.Call<EmpresaCadastro>, t: Throwable) {
                                            editor.apply()
                                        }
                                    })

                                    // Atualizar o estado na thread principal para mostrar o AlertDialog
                                    launch(Dispatchers.Main) {
                                        mostrarMensagemSucesso = true
                                    }

                                } catch (e: Exception) {
                                    launch(Dispatchers.Main) {
                                        println("Erro ao logar: ${e.message}")
                                    }
                                }
                            }

                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 30.dp)
                            .width(180.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF1B4227)),
                    ) {
                        Text(
                            text = stringResource(R.string.entrar),
                            fontSize = 20.sp,
                            fontFamily = poppinsFamily,
                            color = Color.White

                        )
                    }
                }
            }
        }
        if (mostrarMensagemSucesso){
            AlertDialog(
                onDismissRequest = {
                    mostrarMensagemSucesso = false
                },
                title = {
                    Text(
                        text = "Mesa+ Parceiros",
                        fontSize = 25.sp,
                        fontFamily = poppinsFamily,
                        fontWeight =  FontWeight.SemiBold,
                        color = Color(0xFF1B4227)

                    )
                },
                text = {
                    Text(
                        text = "Bem vindo ao nosso App!",
                        fontSize = 15.sp,
                        fontFamily = poppinsFamily,
                        color = Color(0x99000000)
                    )
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(
                        onClick = {
                            navegacao!!.navigate("home")
                        }
                    ){
                        Text(
                            text= "Ok",
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily,
                            fontWeight =  FontWeight.SemiBold,
                            color = Color(0xFF1B4227)
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(null)
}