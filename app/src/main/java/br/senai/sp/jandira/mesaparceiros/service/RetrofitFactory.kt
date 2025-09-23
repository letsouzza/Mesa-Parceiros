package br.senai.sp.jandira.mesaparceiros.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "http://:8080/v1/msa-plus/"

    private val RETROFIT_FACTORY = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getEmpresaRegisterService() : EmpresaService{
        return RETROFIT_FACTORY
            .create(EmpresaService::class.java)
    }

}