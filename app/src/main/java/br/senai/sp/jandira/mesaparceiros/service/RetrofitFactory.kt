package br.senai.sp.jandira.mesaparceiros.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    val BASE_URL = "http://10.107.140.7:8080/v1/mesa-plus/"
                            // 10.107.144.28
    private val RETROFIT_FACTORY = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getEmpresaService() : EmpresaService{
        return RETROFIT_FACTORY
            .create(EmpresaService::class.java)
    }

    fun getSenhaService() : SenhaService{
        return RETROFIT_FACTORY
            .create(SenhaService::class.java)
    }

    fun getCategoryService() : AlimentoService{
        return RETROFIT_FACTORY
            .create(AlimentoService::class.java)
        // Criar o serviço
    }

    fun getAlimentoService() : AlimentoService{
        return RETROFIT_FACTORY
            .create(AlimentoService::class.java)
        // Criar o serviço
    }

}