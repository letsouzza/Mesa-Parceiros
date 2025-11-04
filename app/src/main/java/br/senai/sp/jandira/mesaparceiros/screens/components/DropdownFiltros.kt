package br.senai.sp.jandira.mesaparceiros.screens.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.mesaparceiros.R
import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro
import br.senai.sp.jandira.mesaparceiros.model.ListEmpresa
import br.senai.sp.jandira.mesaparceiros.service.RetrofitFactory
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily
import br.senai.sp.jandira.mesaparceiros.ui.theme.primaryLight
import br.senai.sp.jandira.mesaparceiros.ui.theme.secondaryLight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DropdownFiltros(
    modifier: Modifier = Modifier
) {
    val showDropdown = remember { mutableStateOf(false) }
    val filtroTexto = remember { mutableStateOf("") }
    val empresaList = remember { mutableStateOf(listOf<EmpresaCadastro>()) }
    val empresaSelecionada = remember { mutableStateOf<EmpresaCadastro?>(null) }
    
    // Carregar empresas da API
    LaunchedEffect(Unit) {
        val callEmpresa = RetrofitFactory()
            .getEmpresaService()
            .listEmpresa()
            
        callEmpresa.enqueue(object : Callback<ListEmpresa> {
            override fun onResponse(call: Call<ListEmpresa>, response: Response<ListEmpresa>) {
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        empresaList.value = result.empresas
                        Log.d("DropdownFiltros", "Empresas carregadas: ${result.empresas.size}")
                    }
                } else {
                    Log.e("DropdownFiltros", "Erro ao carregar empresas: ${response.code()}")
                }
            }
            
            override fun onFailure(call: Call<ListEmpresa>, t: Throwable) {
                Log.e("DropdownFiltros", "Erro na requisição de empresas", t)
            }
        })
    }
    
    Box(modifier = modifier) {
        Button(
            onClick = { showDropdown.value = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = secondaryLight,
                contentColor = primaryLight
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.filtros),
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        
        DropdownMenu(
            expanded = showDropdown.value,
            onDismissRequest = { showDropdown.value = false },
            modifier = Modifier.width(280.dp)
        ) {
            // Filtro por empresa
            DropdownMenuItem(
                text = { 
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.filtrar_por_empresa),
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Lista de empresas com scroll
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(empresaList.value) { empresa ->
                                Button(
                                    onClick = { 
                                        empresaSelecionada.value = empresa
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (empresaSelecionada.value?.id == empresa.id) 
                                            primaryLight else secondaryLight,
                                        contentColor = primaryLight
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = empresa.nome,
                                        fontFamily = poppinsFamily,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Botão Filtrar
                        Button(
                            onClick = { 
                                // Implementar lógica de filtro
                                showDropdown.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = secondaryLight,
                                contentColor = primaryLight
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.filtrar),
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                onClick = { }
            )
            
            // Separador visual
            DropdownMenuItem(
                text = { 
                    Spacer(modifier = Modifier.height(8.dp))
                },
                onClick = { }
            )
            
            // Filtro por data
            DropdownMenuItem(
                text = { 
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.filtrar_por_data),
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = filtroTexto.value,
                            onValueChange = { filtroTexto.value = it },
                            placeholder = { 
                                Text(
                                    "DD/MM/AAAA",
                                    fontFamily = poppinsFamily
                                ) 
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Botão Filtrar
                        Button(
                            onClick = { 
                                // Implementar lógica de filtro por data
                                showDropdown.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = secondaryLight,
                                contentColor = primaryLight
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.filtrar),
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                onClick = { }
            )
        }
    }
}

@Preview
@Composable
private fun DropdownFiltrosPreview() {
    DropdownFiltros()
}
