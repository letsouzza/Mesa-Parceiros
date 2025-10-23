package br.senai.sp.jandira.mesaparceiros.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.mesaparceiros.model.ResultCategoria
import coil.compose.AsyncImage
import br.senai.sp.jandira.mesaparceiros.R

@Composable
fun CardAlimento(
    img: String = "https://mesaplustcc.blob.core.windows.net/fotos/106aab1d-a736-429f-9b8a-af40d61562d3.jpg",
    nome: String = "Feijão",
    prazo: String = "2026-09-08",
    quantidade: String = "50",
    imgEmpresa: String = "",
    empresa: String = "atacadao",
) {
    Card(
        modifier = Modifier
            .width(370.dp)
            .height(130.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ){
            Card(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f),
                colors = CardDefaults.cardColors(Color.Magenta)
            ){
                AsyncImage(
                    model = img,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 15.dp)
            ){
                Text(
                    text = nome
                )
                Text(
                    text = stringResource(R.string.prazo) + prazo
                )
                Text(
                    text = stringResource(R.string.quantidade) + quantidade
                )
                Row(
                    modifier = Modifier
                        .weight(0.5f),
                    verticalAlignment = Alignment.Bottom
                ){
                    Card(
                        modifier = Modifier
                            .size(30.dp),
                        colors = CardDefaults.cardColors(Color.Magenta),
                        shape = CircleShape
                    ){
                        AsyncImage(
                            model = imgEmpresa,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        text = empresa
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ){
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun CardAlimentoPreview() {
        CardAlimento()
}