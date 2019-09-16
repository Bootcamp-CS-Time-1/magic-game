package br.com.bootcamp.magicgamecs

import android.app.Application
import br.com.bootcamp.magicgamecs.core.di.setUpDI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpDI()
    }
}