package br.senai.sp.jandira.mesaparceiros.service

import br.senai.sp.jandira.mesaparceiros.model.EmpresaCadastro
import br.senai.sp.jandira.mesaparceiros.model.EmpresaLogin
import br.senai.sp.jandira.mesaparceiros.model.ResponseCadastro
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EmpresaService {

    @Headers("Content-Type: application/json")
    @POST("empresa")
    fun insertUser(@Body empresa: EmpresaCadastro): Call<ResponseCadastro>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body empresaLogin: EmpresaLogin): Call<ResponseCadastro>
}