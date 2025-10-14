package br.senai.sp.jandira.mesaparceiros.screens

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.mesaparceiros.ui.theme.MesaParceirosTheme
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily

@Composable
fun CadastroAlimentoPrimeiro() {

    var nomeState by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }


    MesaParceirosTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 30.dp, horizontal = 15.dp)
                        ) {
                            OutlinedTextField(
                                value = nomeState,
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
                                            R.string.nome
                                        ),
                                        fontSize = 20.sp,
                                        fontFamily = poppinsFamily,
                                        color = Color(0x99000000)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(Modifier.padding(top = 15.dp))
                            OutlinedTextField(
                                value = descricao,
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
                                            R.string.descricao
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

                        }
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CadastroAlimentoPrimeiroPreview() {
    MesaParceirosTheme {
        CadastroAlimentoPrimeiro()
    }
}