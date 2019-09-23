package br.com.bootcamp.magicgamecs.features.home

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.widget.TextView
import br.com.bootcamp.magicgamecs.models.pojo.Card
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout

class CardImageRequestListener(
    private val card: Card,
    private val shimmer: ShimmerFrameLayout,
    private val textPlaceholder: TextView
) : RequestListener<Drawable> {

    init {
        textPlaceholder.text = ""
        startShimmer()
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        textPlaceholder.text = card.name
        stopShimmer()
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        stopShimmer()
        return false
    }

    private fun startShimmer() {
        shimmer.setShimmer(
            Shimmer.AlphaHighlightBuilder()
                .setAutoStart(true)
                .setDuration(1300)
                .setRepeatMode(ValueAnimator.RESTART)
                .build()
        )
    }

    private fun stopShimmer() {
        shimmer.stopShimmer()
        shimmer.setShimmer(null)
        shimmer.clearAnimation()
    }

}