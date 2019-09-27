package br.com.bootcamp.magicgamecs

import br.com.bootcamp.magicgamecs.core.config.MOCK_SERVER_URL
import br.com.bootcamp.magicgamecs.core.di.API_BASE_URL
import br.com.bootcamp.magicgamecs.core.di.ENABLE_HTTP_LOG
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class TestApp : App() {
    override fun setUp() {
        startKoin {
            androidLogger()

            androidContext(this@TestApp)

            properties(
                mapOf(
                    API_BASE_URL to MOCK_SERVER_URL,
                    ENABLE_HTTP_LOG to true
                )
            )

            modules(
                listOf(
                )
            )
        }
    }
}