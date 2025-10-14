package br.senai.sp.jandira.mesaparceiros.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily

@Composable
fun CadastroAlimentoSegundo() {

    var quantidadeState by remember { mutableStateOf("") }
    var prazoState by remember { mutableStateOf("") }
    var pesoState by remember { mutableStateOf("") }


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
                            .padding(vertical = 30.dp, horizontal = 15.dp)
                    ){
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
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
                                        R.string.foto
                                    ),
                                    fontSize = 20.sp,
                                    fontFamily = poppinsFamily,
                                    color = Color(0x99000000)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(Modifier.padding(top = 10.dp))
                        OutlinedTextField(
                            value = quantidadeState,
                            onValueChange = {},
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
                        )
                        Spacer(Modifier.padding(top = 10.dp))
                        OutlinedTextField(
                            value = prazoState,
                            onValueChange = {},
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
                        )
                        Spacer(Modifier.padding(top = 10.dp))
                        OutlinedTextField(
                            value = pesoState,
                            onValueChange = {},
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
                        )

                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CadastroAlimentoSegundoPreview() {
    CadastroAlimentoSegundo()
}