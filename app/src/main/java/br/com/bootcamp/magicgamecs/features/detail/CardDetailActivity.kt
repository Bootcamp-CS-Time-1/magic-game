package br.com.bootcamp.magicgamecs.features.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.models.pojo.Card
import kotlinx.android.synthetic.main.activity_card_detail.*

class CardDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val card = intent.getParcelableExtra<Card>("CARD")
        card_image.card = card

        imageView_close_detail.setOnClickListener {
            finish()
        }

    }
}
