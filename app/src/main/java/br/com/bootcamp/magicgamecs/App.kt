package br.com.bootcamp.magicgamecs

import android.app.Application
import br.com.bootcamp.magicgamecs.core.di.setUpDI

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setUp()
    }

    open fun setUp() {
        setUpDI()
    }
}