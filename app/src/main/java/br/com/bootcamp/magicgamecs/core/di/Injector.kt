package br.com.bootcamp.magicgamecs.core.di

import android.app.Application
import br.com.bootcamp.magicgamecs.BuildConfig
import br.com.bootcamp.magicgamecs.core.di.modules.appModule
import br.com.bootcamp.magicgamecs.core.di.modules.netModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

const val API_BASE_URL = "API_BASE_URL"
const val ENABLE_HTTP_LOG = "ENABLE_HTTP_LOG"
const val REQUEST_TIMEOUT = "REQUEST_TIMEOUT"

/**
 * Inicializa os modulos com as declarações das dependencias
 *
 * @see <a href="https://insert-koin.io/">https://insert-koin.io/</a>
 */
fun Application.setUpDI() {
    startKoin {
        androidLogger()

        androidContext(this@setUpDI)

        properties(
            /**
             * Define as propriedades que podem ser utilizadas através de `getProperty<T>(PROPERTY_NAME)`
             * Onde `T` é o tipo da propriedade, podendo ser `String`, `Int`, etc..
             * e `PROPERTY_NAME` é o nome da propriedade
             * exemplo: `getProperty<Boolean>(ENABLE_HTTP_LOG)`
             */
            mapOf(
                API_BASE_URL to BuildConfig.API_BASE_URL,
                ENABLE_HTTP_LOG to BuildConfig.DEBUG,
                REQUEST_TIMEOUT to 30 * 1000L // 30s
            )
        )

        modules(
            /**
             * Lista de modulos onde são declaradas as dependências
             * para novos modulos, criá-los em `modules/` e registrá-los abaixo
             */
            listOf(
                netModule,
                appModule
            )
        )
    }
}