package br.senai.sp.jandira.mesaparceiros.model

import com.google.gson.annotations.SerializedName

data class EmpresaUpdate(
    var id: Int = 0,
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    var telefone: String = "",
    var foto: String = ""
)
