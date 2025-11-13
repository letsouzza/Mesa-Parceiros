package br.senai.sp.jandira.mesaparceiros.service

import br.senai.sp.jandira.mesaparceiros.model.Alimento
import br.senai.sp.jandira.mesaparceiros.model.ListAlimento
import br.senai.sp.jandira.mesaparceiros.model.ListAlimentoFiltro
import br.senai.sp.jandira.mesaparceiros.model.ResponseGeral
import br.senai.sp.jandira.mesaparceiros.model.ListCategoria
import br.senai.sp.jandira.mesaparceiros.model.ListTipoPeso
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AlimentoService {

    @GET("categoria")
    fun listCategoria(): retrofit2.Call<ListCategoria>

    @GET("filtroCat/{id}")
    fun filtroCategoria(@Path("id") id: Int): retrofit2.Call<ListAlimentoFiltro>

    @GET("empresaAlimento/{id}")
    fun filtroEmpresa(@Path("id") id: Int): retrofit2.Call<ListAlimentoFiltro>

    @GET("alimentos")
    fun listAlimento(): retrofit2.Call<ListAlimento>

    @GET("tipoPeso")
    fun listTipoPeso(): retrofit2.Call<ListTipoPeso>

    @Headers("Content-Type: application/json")
    @POST("alimentos")
    fun insertAlimento(@Body alimento: Alimento): retrofit2.Call<ResponseGeral>
}