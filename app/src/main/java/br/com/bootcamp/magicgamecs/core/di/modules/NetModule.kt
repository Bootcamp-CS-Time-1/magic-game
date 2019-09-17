package br.com.bootcamp.magicgamecs.core.di.modules

import br.com.bootcamp.magicgamecs.core.di.API_BASE_URL
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val netModule = module {

    factory<WebServices> {
        get<Retrofit>().create(WebServices::class.java)
    }

    factory<Retrofit> {
        Retrofit.Builder()
            .baseUrl(getProperty<String>(API_BASE_URL))
            .client(get())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory<Gson> {
        GsonBuilder()
            .create()
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get()).build()
    }

    factory<WebServices> {
        get<Retrofit>()
            .create(WebServices::class.java)
    }

}