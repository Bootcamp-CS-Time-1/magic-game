package br.com.bootcamp.magicgamecs.features.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.features.detail.DetailViewModel
import br.com.bootcamp.magicgamecs.features.detail.DetailViewModelFactory
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_card_detail.*

class CardDetailActivity : AppCompatActivity() {

    private val detailViewModel by lazy {
        ViewModelProviders.of(this, detailiewModelFactory).get(DetailViewModel::class.java)
    }

    private val detailiewModelFactory by lazy {
        DetailViewModelFactory(this, card)
    }

    private lateinit var card: Card
    private lateinit var cards: List<Card>

    private var inDeck = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)


        val card = intent.getParcelableExtra<Card>("CARD")
        card_image.card = card


        imageView_close_detail.setOnClickListener {
            finish()
        }


        materialButton.setOnClickListener {
            if (inDeck) {
                detailViewModel.removeCard()
            } else {
                detailViewModel.addCard()
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
        detailViewModel.getCardFavorite().observe(this, Observer {
            if (it != null) {
                inDeck = true
                materialButton.text = resources.getText(R.string.remove_card_to_deck)
            } else {
                materialButton.text = resources.getText(R.string.add_card_to_deck)
            }
        })
    }

}
