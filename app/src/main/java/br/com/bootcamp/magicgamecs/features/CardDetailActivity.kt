package br.com.bootcamp.magicgamecs.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bootcamp.magicgamecs.R
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_card_detail.*

class CardDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val card = intent.getParcelableExtra<Card>("CARD")

        Glide.with(this)
            .load(card.imageUrl)
            .placeholder(R.drawable.placeholder_card)
            .error(R.drawable.placeholder_card)
            //.transform(RoundedCorners(10))
            .into(imageView_card_detail)

//        val imageCarousel : CarouselPicker = findViewById(R.id.carousel)
//
//        val imageItems = ArrayList<CarouselPicker.PickerItem>()
//        imageItems.add(CarouselPicker.DrawableItem(R.drawable.placeholder_card))
//        imageItems.add(CarouselPicker.DrawableItem(R.drawable.placeholder_card))
//        imageItems.add(CarouselPicker.DrawableItem(R.drawable.placeholder_card))
//        imageItems.add(CarouselPicker.DrawableItem(R.drawable.fundo))
//        val imageAdapter = CarouselPicker.CarouselViewAdapter(this, imageItems, 0)
//        imageCarousel.setAdapter(imageAdapter)
    }
}
