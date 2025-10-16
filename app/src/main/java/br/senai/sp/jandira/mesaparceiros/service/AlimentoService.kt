package br.senai.sp.jandira.mesaparceiros.service

import br.senai.sp.jandira.mesaparceiros.model.ResultCategoria
import retrofit2.http.GET

interface AlimentoService {

    @GET("categoria")
    fun listCategoria(): retrofit2.Call<ResultCategoria>
}