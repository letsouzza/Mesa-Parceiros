package br.senai.sp.jandira.mesaparceiros.screens.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.ListTipoPeso
import br.senai.sp.jandira.mesaparceiros.model.TipoPeso
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTipoPeso(
    tipoPesoSelecionado: TipoPeso?,
    onTipoPesoSelected: (TipoPeso) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
    var tipoPesoList by remember { mutableStateOf(listOf<TipoPeso>()) }

    // Carregar tipos de peso da API usando GlobalScope
    LaunchedEffect(Unit) {
        GlobalScope.launch {
            val callTipoPeso = RetrofitFactory().getAlimentoService().listTipoPeso()
            callTipoPeso.enqueue(object : Callback<ListTipoPeso> {
                override fun onResponse(call: Call<ListTipoPeso>, response: Response<ListTipoPeso>) {
                    Log.d("TipoPeso", "Response recebido: ${response.code()}")
                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.d("TipoPeso", "Body: $body")
                        val tiposApi = body?.tipos ?: listOf()
                        if (tiposApi.isNotEmpty()) {
                            tipoPesoList = tiposApi
                            Log.d("TipoPeso", "Tipos carregados da API: ${tipoPesoList.size}")
                            tipoPesoList.forEach { tipo ->
                                Log.d("TipoPeso", "Tipo: id=${tipo.id}, tipo=${tipo.tipo}")
                            }
                        } else {
                            Log.d("TipoPeso", "API retornou lista vazia")
                        }
                    } else {
                        Log.e("TipoPeso", "Erro ao carregar tipos: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ListTipoPeso>, throwable: Throwable) {
                    Log.e("TipoPeso", "Falha na requisição: ${throwable.message}")
                }
            })
        }
    }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            // Campo do dropdown
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = tipoPesoSelecionado?.tipo ?: "",
                onValueChange = {},
                label = {
                    Text(
                        text = stringResource(R.string.tipo_peso),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = poppinsFamily,
                        color = Color(0x99000000)
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(30.dp),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedBorderColor = Color(0xFF1B4227),
                    focusedBorderColor = Color(0xFF1B4227),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            // Menu dropdown
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                tipoPesoList.forEach { tipo ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        text = {
                            Text(
                                text = tipo.tipo,
                                color = Color.Black,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        },
                        onClick = {
                            onTipoPesoSelected(tipo)
                            expanded = false
                        }
                    )
                    
                    if (tipo != tipoPesoList.last()) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}