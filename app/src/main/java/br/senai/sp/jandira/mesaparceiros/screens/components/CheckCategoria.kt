package br.senai.sp.jandira.mesaparceiros.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.mesaparceiros.ui.theme.poppinsFamily

@Composable
fun CategoryCheck(

    onCategoriaSelecionada: (Boolean?) -> Unit,
    checkedText: String,
    check: Boolean

){

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(
                checked = check,
                onCheckedChange = onCategoriaSelecionada
            )
            Text(
                text = checkedText,
                fontFamily = poppinsFamily,
                fontSize = 15.sp,
                color = Color(0xFF261C09)
            )
        }
    }
}

@Preview
@Composable
private fun CategoryCheckPreview(){
    CategoryCheck({ true } , "" , false)
}