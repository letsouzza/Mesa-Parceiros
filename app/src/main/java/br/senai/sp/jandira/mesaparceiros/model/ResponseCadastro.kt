package br.senai.sp.jandira.mesaparceiros.model

import com.google.gson.JsonArray

data class ResponseCadastro(
    var status: Boolean = false,
    var statusCode: Int = 0,
    var message: String = "",
    var usuario: UsuarioResponsePost
)
