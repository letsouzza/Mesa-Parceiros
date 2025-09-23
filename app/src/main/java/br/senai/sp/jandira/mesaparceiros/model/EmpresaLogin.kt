package br.senai.sp.jandira.mesaparceiros.model

import com.google.gson.annotations.SerializedName

data class EmpresaLogin(
    var email: String = "",
    var senha: String = "",
    @SerializedName("nome") var tipoUsuario: String = "empresa",
)
