package br.senai.sp.jandira.mesaparceiros.model

data class ListTipoPeso(
    var status: Boolean = false,
    var statusCode: Int = 0,
    var items: Int = 0,
    var tipos: List<TipoPeso>
)
