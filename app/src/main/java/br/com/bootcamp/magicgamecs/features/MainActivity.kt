package br.com.bootcamp.magicgamecs.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.models.retrofit.services.WebServices
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val webServices by inject<WebServices>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}
