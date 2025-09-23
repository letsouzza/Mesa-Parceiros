package br.senai.sp.jandira.mesaparceiros.model

import com.google.gson.annotations.SerializedName

data class EmpresaCadastro(
    var id: Int = 0,
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    @SerializedName("cnpj_mei") var cnpjMei: String = "",
    var telefone: String = "",
    var foto: String = ""
)
