package br.com.bootcamp.magicgamecs.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import br.com.bootcamp.magicgamecs.TestApp

class MockTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, TestApp::class.java.name, context)
}