package br.com.bootcamp.magicgamecs.features.home

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.features.favorites.FavoritesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            selectFragment(CollectionFragment(), btTelaSets, btTelaFavoritos)

        btTelaSets.setOnClickListener {
            selectFragment(CollectionFragment(), btTelaSets, btTelaFavoritos)
        }

        btTelaFavoritos.setOnClickListener {
            selectFragment(FavoritesFragment(), btTelaFavoritos, btTelaSets)
        }
    }

    private fun selectFragment(fragment: Fragment, selected: Button, unselected: Button) {
        TextViewCompat.setTextAppearance(unselected, R.style.RobotoTextViewStyle)
        TextViewCompat.setTextAppearance(selected, R.style.RobotoTextViewStyle_Bold)
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

}
