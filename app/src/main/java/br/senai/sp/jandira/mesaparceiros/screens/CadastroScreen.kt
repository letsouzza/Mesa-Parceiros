package br.senai.sp.jandira.mesaparceiros.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import br.senai.sp.jandira.mesaparceiros.util.Formatters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

@Composable
fun CadastroEmpresa(navegacao: NavHostController?) {
    

    var nameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var cnpjState by remember { mutableStateOf("") }
    var telefoneState by remember { mutableStateOf("") }
    var telefoneValue by remember { mutableStateOf(TextFieldValue("")) }
    var senhaState by remember { mutableStateOf(TextFieldValue("")) }
    var senhaVisivel by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    
    // Evita que o teclado abra automaticamente
    DisposableEffect(Unit) {
        keyboardController?.hide()
        onDispose {}
    }
    
    LaunchedEffect(telefoneState) {
        telefoneValue = TextFieldValue(
            text = Formatters.formatPhoneNumber(telefoneState),
            selection = TextRange(Formatters.formatPhoneNumber(telefoneState).length)
        )
    }
    var isNomeError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isCnpjError by remember { mutableStateOf(false) }
    var isTelefoneError by remember { mutableStateOf(false) }
    var senhaValidation by remember { 
        mutableStateOf(Formatters.PasswordValidationResult(false)) 
    }

    var mostrarMensagemSucesso by remember { mutableStateOf(false) }

    val empresaApi = RetrofitFactory().getEmpresaService()

    fun validar(): Boolean {
        isNomeError = nameState.length < 3
        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(emailState).matches()
        isCnpjError = cnpjState.filter { it.isDigit() }.length != 14
        isTelefoneError = telefoneState.filter { it.isDigit() }.length !in 10..11
        senhaValidation = Formatters.validatePassword(senhaState.text)
        
        return !isNomeError && !isEmailError && !isCnpjError && !isTelefoneError && senhaValidation.isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B4227))
    ){
        Column(
            modifier= Modifier
                .fillMaxSize()
        ){
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xD9FFF9EB)
                )
            ){
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(R.drawable.logoescura),
                        contentDescription = "",
                        modifier = Modifier
                            .size(180.dp)
                    )
                    Text(
                        text = stringResource(R.string.cadastre_se),
                        fontSize = 35.sp,
                        fontFamily = poppinsFamily,
                        fontWeight =  FontWeight.Normal,
                        color = Color(0xFF1B4227)
                    )
                    Spacer(Modifier.padding(7.dp))
                    OutlinedTextField(
                        value = nameState,
                        onValueChange = { it ->
                            nameState = it
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFF1B4227),
                            focusedBorderColor = Color(0xFF1B4227),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.nome
                                ),
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                color = Color(0x99000000)
                            )
                        },
                        isError = isNomeError,
                        supportingText = {
                            if(isNomeError){
                                Text(text = "Nome é obrigatório e deve ter no mínimo 3 caracters")
                            }
                        },
                        trailingIcon = {
                            if(isEmailError){
                                Icon(imageVector = Icons.Default.Info, contentDescription = "")
                            }
                        },
                        modifier = Modifier
                            .width(315.dp)

                    )
                    Spacer(Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { it ->
                            emailState = it
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFF1B4227),
                            focusedBorderColor = Color(0xFF1B4227),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
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
                        isError = isEmailError,
                        supportingText = {
                            if(isEmailError){
                                Text(text = "Email é obrigatório")
                            }
                        },
                        trailingIcon = {
                            if(isEmailError){
                                Icon(imageVector = Icons.Default.Info, contentDescription = "")
                            }
                        },
                        modifier = Modifier
                            .width(315.dp)

                    )
                    Spacer(Modifier.padding(5.dp))
                    var cnpjValue by remember { mutableStateOf(TextFieldValue("")) }
                    
                    LaunchedEffect(cnpjState) {
                        cnpjValue = TextFieldValue(
                            text = Formatters.formatCnpj(cnpjState),
                            selection = TextRange(Formatters.formatCnpj(cnpjState).length)
                        )
                    }
                    
                    OutlinedTextField(
                        value = cnpjValue,
                        onValueChange = { newValue ->
                            val digits = newValue.text.filter { char -> char.isDigit() }
                            if (digits.length <= 14) {
                                cnpjState = digits
                                cnpjValue = newValue.copy(
                                    text = Formatters.formatCnpj(digits),
                                    selection = TextRange(Formatters.formatCnpj(digits).length)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFF1B4227),
                            focusedBorderColor = Color(0xFF1B4227),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.cnpj
                                ),
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                color = Color(0x99000000)
                            )
                        },
                        isError = isCnpjError,
                        supportingText = {
                            if (isCnpjError) {
                                Text("CNPJ deve ter 14 dígitos")
                            }
                        },
                        trailingIcon = {
                            if (isCnpjError) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Erro no CNPJ",
                                    tint = Color.Red
                                )
                            }
                        },
                        modifier = Modifier
                            .width(315.dp)

                    )
                    Spacer(Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = telefoneValue,
                        onValueChange = { newValue ->
                            val digits = newValue.text.filter { char -> char.isDigit() }
                            if (digits.length <= 11) {
                                telefoneState = digits
                                telefoneValue = newValue.copy(
                                    text = Formatters.formatPhoneNumber(digits),
                                    selection = TextRange(Formatters.formatPhoneNumber(digits).length)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFF1B4227),
                            focusedBorderColor = Color(0xFF1B4227),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = {
                            Text(
                                text = stringResource(
                                    R.string.telefone
                                ),
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                color = Color(0x99000000)
                            )
                        },
                        isError = isTelefoneError,
                        supportingText = {
                            if (isTelefoneError) {
                                Text("Telefone deve ter entre 10 e 11 dígitos")
                            }
                        },
                        trailingIcon = {
                            if (isTelefoneError) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Erro no telefone",
                                    tint = Color.Red
                                )
                            }
                        },
                        modifier = Modifier
                            .width(315.dp)

                    )
                    Spacer(Modifier.padding(5.dp))
                    
                    // Campo de senha simplificado
                    OutlinedTextField(
                        value = senhaState.text,
                        onValueChange = { newValue ->
                            senhaState = TextFieldValue(
                                text = newValue.take(100),
                                selection = androidx.compose.ui.text.TextRange(newValue.length)
                            )
                            senhaValidation = Formatters.validatePassword(newValue)
                        },
                        modifier = Modifier
                            .width(315.dp)
                            .focusRequester(focusRequester),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFF1B4227),
                            focusedBorderColor = Color(0xFF1B4227),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            val icon = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                                    tint = Color(0xFF1B4227)
                                )
                            }
                        },
                        visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                        label = {
                            Text(
                                text = stringResource(R.string.senha),
                                fontSize = 20.sp,
                                fontFamily = poppinsFamily,
                                color = Color(0x99000000)
                            )
                        },
                        isError = senhaState.text.isNotEmpty() && !senhaValidation.isValid,
                        singleLine = true
                    )
                    
                    // Mensagens de validação da senha (fora do TextField para evitar mudanças de layout)
                    if (senhaState.text.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .width(315.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            Text("A senha deve conter:")
                            Text(
                                "• Mínimo 8 caracteres: ${if (senhaValidation.hasMinLength) "✓" else "✗"}",
                                color = if (senhaValidation.hasMinLength) Color.Green else Color.Red,
                                fontSize = 12.sp
                            )
                            Text(
                                "• Pelo menos 1 letra maiúscula: ${if (senhaValidation.hasUppercase) "✓" else "✗"}",
                                color = if (senhaValidation.hasUppercase) Color.Green else Color.Red,
                                fontSize = 12.sp
                            )
                            Text(
                                "• Pelo menos 1 caractere especial: ${if (senhaValidation.hasSpecialChar) "✓" else "✗"}",
                                color = if (senhaValidation.hasSpecialChar) Color.Green else Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(Modifier.padding(5.dp))
                    Text(
                        text = stringResource(R.string.ja_tem_login),
                        fontSize = 14.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B4227),
                        modifier = Modifier
                            .clickable{navegacao?.navigate("login")}
                            .align(Alignment.End)
                            .padding(end = 25.dp)
                    )
                    Spacer(Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            if (validar()){
                                val body = EmpresaCadastro(
                                    nome = nameState,
                                    email = emailState,
                                    senha = senhaState.text,
                                    cnpjMei = cnpjState,
                                    telefone = telefoneState
                                )

                                GlobalScope.launch(Dispatchers.IO){
                                    val empresaNova = empresaApi.insertEmpresa(body).await()
                                    mostrarMensagemSucesso = true
                                    println("deu CERTOOOOOOOO")
                                }

                            }else{
                                println("******************** Dados errados")
                            }

                        },
                        modifier = Modifier
                            .width(230.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFDA8B)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cadastrar),
                            fontSize = 20.sp,
                            fontFamily = poppinsFamily,
                            fontWeight =  FontWeight.Normal,
                            color = Color.Black
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
                        text = "Sucesso",
                        fontSize = 25.sp,
                        fontFamily = poppinsFamily,
                        fontWeight =  FontWeight.SemiBold,
                        color = Color(0xFF1B4227)

                    )
                },
                text = {
                    Text(
                        text = "Empresa $nameState cadastrado com sucesso!",
                        fontSize = 15.sp,
                        fontFamily = poppinsFamily,
                        color = Color(0x99000000)
                    )
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(
                        onClick = {
                            navegacao!!.navigate("login")
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
private fun CadastroEmpresaPreview() {
    CadastroEmpresa(null)
}