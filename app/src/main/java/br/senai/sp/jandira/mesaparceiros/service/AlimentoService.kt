package br.senai.sp.jandira.mesaparceiros.service

import br.senai.sp.jandira.mesaparceiros.model.Alimento
import br.senai.sp.jandira.mesaparceiros.model.ListAlimento
import br.senai.sp.jandira.mesaparceiros.model.ResponseGeral
import br.senai.sp.jandira.mesaparceiros.model.ResultCategoria
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AlimentoService {

    @GET("categoria")
    fun listCategoria(): retrofit2.Call<ResultCategoria>

    @GET("categoria")
    fun listAlimento(): retrofit2.Call<ListAlimento>

    @Headers("Content-Type: application/json")
    @POST("alimentos")
    fun insertAlimento(@Body alimento: Alimento): retrofit2.Call<ResponseGeral>
}