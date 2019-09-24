package br.com.bootcamp.magicgamecs.core.di.modules

import br.com.bootcamp.magicgamecs.core.di.API_BASE_URL
import br.com.bootcamp.magicgamecs.core.di.REQUEST_TIMEOUT
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val netModule = module {

    factory<WebServices> {
        get<Retrofit>().create(WebServices::class.java)
    }

    factory<Retrofit> {
        Retrofit.Builder()
            .baseUrl(getProperty<String>(API_BASE_URL))
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory<Gson> {
        GsonBuilder()
            .create()
    }

    factory<Interceptor> {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(getProperty(REQUEST_TIMEOUT), TimeUnit.MILLISECONDS)
            .callTimeout(getProperty(REQUEST_TIMEOUT), TimeUnit.MILLISECONDS)
            .readTimeout(getProperty(REQUEST_TIMEOUT), TimeUnit.MILLISECONDS)
            .addInterceptor(get())
            .build()
    }

}