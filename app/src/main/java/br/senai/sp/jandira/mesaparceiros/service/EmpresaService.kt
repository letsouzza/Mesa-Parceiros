package br.senai.sp.jandira.mesaparceiros.service

import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro
import br.senai.sp.jandira.mesaparceiros.model.EmpresaLogin
import br.senai.sp.jandira.mesaparceiros.model.EmpresaUpdate
import br.senai.sp.jandira.mesaparceiros.model.ListEmpresa
import br.senai.sp.jandira.mesaparceiros.model.ResponseCadastro
import br.senai.sp.jandira.mesaparceiros.model.ResponseGeral
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmpresaService {

    @Headers("Content-Type: application/json")
    @POST("empresa")
    fun insertEmpresa(@Body empresa: EmpresaCadastro): Call<ResponseCadastro>

    @GET("empresa")
    fun listEmpresa(): retrofit2.Call<ListEmpresa>

    @GET("empresa/{id}")
    fun empresaPorId(@Path("id") id: Int): retrofit2.Call<EmpresaCadastro>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginEmpresa(@Body empresaLogin: EmpresaLogin): Call<ResponseCadastro>

    @Headers("Content-Type: application/json")
    @PUT("empresa/{id}")
    fun updateEmpresa(@Path("id") id: Int, @Body empresa: EmpresaUpdate): Call<ResponseGeral>
}
