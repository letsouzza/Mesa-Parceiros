package br.senai.sp.jandira.mesaparceiros.model

import com.google.gson.annotations.SerializedName

data class Alimento(
    var id: Int = 0,
    var nome: String = "",
    var quantidade: String = "",
    @SerializedName("data_de_validade")var prazo: String = "",
    var descricao: String = "",
//    var peso: String = "",
    var imagem: String = "",
    @SerializedName("id_empresa") var idEmpresa: Int = 1,
    val categoria: List<Categoria> // ou List<Int> dependendo do tipo
)
