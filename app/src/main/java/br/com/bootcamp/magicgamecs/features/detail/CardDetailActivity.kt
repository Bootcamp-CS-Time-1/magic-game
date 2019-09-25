package br.com.bootcamp.magicgamecs.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.features.home.CardImageRequestListener
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_card_detail.*
import kotlinx.android.synthetic.main.item_main_activity_card.*
import kotlinx.android.synthetic.main.item_main_activity_card.view.*
import kotlinx.android.synthetic.main.item_main_activity_card.view.imageView_card_item
import kotlinx.android.synthetic.main.item_main_activity_card.view.shimmer_card
import kotlinx.android.synthetic.main.item_main_activity_card.view.text_card_placeholder

class CardDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val card = intent.getParcelableExtra<Card>("CARD")

        Glide.with(this)
            .load(card.imageUrl)
            .placeholder(R.drawable.placeholder_card)
            .error(R.drawable.placeholder_card)
            .addListener(
                CardImageRequestListener(
                    card,
                    shimmer_card,
                    text_card_placeholder
                )
            )
            .into(imageView_card_item)

        imageView_close_detail.setOnClickListener {
            finish()
        }

    }
}
