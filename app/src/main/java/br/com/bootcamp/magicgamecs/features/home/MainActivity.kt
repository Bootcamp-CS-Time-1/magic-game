package br.com.bootcamp.magicgamecs.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.features.favorites.FavoritesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, CollectionFragment())
            .commit()

        btTelaSets.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, CollectionFragment())
                .commit()
        }

        btTelaFavoritos.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, FavoritesFragment())
                .commit()
        }
    }

}
