package br.com.bootcamp.magicgamecs.features.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.models.pojo.Card
import br.com.bootcamp.magicgamecs.models.room.AppDataBase
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_card_detail.*

class CardDetailActivity : AppCompatActivity() {

    private var mDb : AppDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        mDb = AppDataBase.getInstance(applicationContext)

        val card = intent.getParcelableExtra<Card>("CARD")
        card_image.card = card


        materialButton.setOnClickListener {
            mDb?.cardDao()?.insertCard(card)
        }

        imageView_close_detail.setOnClickListener {
            finish()
        }

        val lista = mDb?.cardDao()?.loadAllCards()
        Toast.makeText(this, lista?.get(0)?.types?.get(0), Toast.LENGTH_SHORT).show()


    }
}
