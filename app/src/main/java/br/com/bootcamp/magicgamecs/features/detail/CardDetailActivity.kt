package br.com.bootcamp.magicgamecs.features.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.features.detail.DetailViewModel
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_card_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardDetailActivity : AppCompatActivity(R.layout.activity_card_detail) {

    private val detailViewModel by viewModel<DetailViewModel>()

    private val card by lazy {
        intent.getParcelableExtra<Card>("CARD")
    }
    private lateinit var cards: List<Card>

    private var inDeck = false


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        card_image.card = card

        imageView_close_detail.setOnClickListener {
            finish()
        }


        materialButton.setOnClickListener {
            if (inDeck) {
                detailViewModel.removeCard(card)
            } else {
                detailViewModel.addCard(card)
            }
        }

        initObservables()
        isFavorite()
    }

    private fun initObservables() {
        detailViewModel.observerCards().observe(this, Observer {
            cards = it
        })
    }

    private fun isFavorite() {
        detailViewModel.getCardFavorite(card).observe(this, Observer {
            if (it != null) {
                inDeck = true
                materialButton.text = resources.getText(R.string.remove_card_to_deck)
            } else {
                materialButton.text = resources.getText(R.string.add_card_to_deck)
            }
        })
    }

}
