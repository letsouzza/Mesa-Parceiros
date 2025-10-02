package br.senai.sp.jandira.mesaparceiros.model

data class EmpresaLogin(
    var email: String = "",
    var senha: String = "",
    var tipo: String = "empresa",
)
