package br.com.bootcamp.magicgamecs.core.di.modules

import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val netModule = module {

    factory<WebServices>{
        get<Retrofit>().create(WebServices::class.java)
    }

    /*factory<Retrofit>{
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/
}