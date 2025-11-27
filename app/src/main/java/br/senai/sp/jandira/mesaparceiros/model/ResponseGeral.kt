package br.senai.sp.jandira.mesaparceiros.model

import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro

data class ResponseGeral(
    var status: Boolean = false,
    var statusCode: Int = 0,
    var message: String = "",
    var empresa: EmpresaCadastro? = null
)
